package com.potato.instock.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.potato.instock.exceptions.item.ItemInvalidIdException;
import com.potato.instock.exceptions.item.ItemNotFoundException;
import com.potato.instock.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ItemService {
    private final WebClient webClient;
    private static final int MAX_ENTRIES = 500;
    private static final int TTL = 600 * 1000; // 10 minutes
    private final Map <String, Item> cache;

    @Autowired
    public ItemService() {
        this.webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setDoNotTrackEnabled(true);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
        cache = new LinkedHashMap<>(MAX_ENTRIES + 1, .75F, true) {
            public boolean removeEldestEntry(Map.Entry<String, Item> eldest) {
                return size() > MAX_ENTRIES;
            }
        };
    }

    private Item getItemFromAmazon(String itemId) {
        // Amazon's item ids (sku) is 10 characters long
        if (itemId.length() != 10) {
            throw new ItemInvalidIdException("item id for amazon is invalid");
        }
        if (cache.containsKey(itemId)) {
            return cache.get(itemId);
        }

        String url = "https://amazon.com/dp/" + itemId;
        HtmlPage page;

        try {
            page = webClient.getPage(url);
        } catch (Exception e) {
            throw new ItemNotFoundException("item from amazon with id: " + itemId + " not found");
        }

        HtmlSpan expression = page.getFirstByXPath("//*[@id=\"corePrice_feature_div\"]/div/span/span[2]");
        String price = expression != null ? expression.asNormalizedText() : "";

        // If price is empty, check another path
        if (price.equals("")) {
            expression = page.getFirstByXPath("//*[@id=\"corePriceDisplay_desktop_feature_div\"]/div[1]/span/span[2]");
            price = expression != null ? expression.asNormalizedText() : "";
        }

        // Check another path...
        if (price.equals("")) {
            expression = page.getFirstByXPath("//*[@id=\"corePrice_feature_div\"]/div/span/span[1]");
            price = expression != null ? expression.asNormalizedText() : "";
        }

        // And another one!
        if (price.equals("")) {
            expression = page.getFirstByXPath("//*[@id=\"corePriceDisplay_desktop_feature_div\"]/div[1]/span[2]/span[1]");
            price = expression != null ? expression.asNormalizedText() : "";
        }

        expression = page.getFirstByXPath("//*[@id=\"productTitle\"]");
        String name = expression != null ? expression.asNormalizedText() : "";

        // Check instock span first
        expression = page.getFirstByXPath("//*[@id=\"availability\"]/span");
        boolean inStock = expression != null && expression.asNormalizedText().length() > 0;

        // if instock is false, check if "add to cart" button exists
        if (!inStock) {
            expression = page.getFirstByXPath("//*[@id=\"submit.add-to-cart-announce\"]");
            inStock = expression != null && expression.asNormalizedText().equals("Add to Cart");
        }

        if (name == null && price == null) {
            throw new ItemNotFoundException("item from amazon with id: " + itemId + " not found");
        }

        final Item item = Item.builder()
                .itemId(itemId)
                .price(price)
                .name(name)
                .website("amazon")
                .inStock(inStock)
                .lastUpdated(System.currentTimeMillis())
                .build();
        cache.put(itemId, item);
        return item;
    }

    public Item getItemFromWebsite(String itemId, String website) {
        if (website.equalsIgnoreCase("amazon")) {
            return getItemFromAmazon(itemId);
        }

        throw new ItemNotFoundException("Invalid website name");
    }

    @Scheduled(fixedRate = TTL, initialDelay = 10000)
    private void evictFromCache() {
        final Iterator<Map.Entry<String, Item>> iter = cache.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Item> entry = iter.next();
            long time = System.currentTimeMillis() - entry.getValue().getLastUpdated();
            if (time > TTL) {
                iter.remove();
            }
        }
    }
}
