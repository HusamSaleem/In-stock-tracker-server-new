package com.potato.instock.item;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.potato.instock.exceptions.item.ItemInvalidIdException;
import com.potato.instock.exceptions.item.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ItemService {

    private final WebClient webClient;
    private final HashMap<String, Item> cache; // items the cache can previously hold
    private final int TIME_IN_CACHE_THRESHHOLD = 300; // 300 seconds before the item in the cache should be updated

    @Autowired
    public ItemService() {
        this.cache = new HashMap<>();
        this.webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setDoNotTrackEnabled(true);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
    }

    private Item getItemFromAmazon(String itemId) {
        // Amazon's item ids (sku) is 10 characters long
        if (itemId.length() != 10) {
            throw new ItemInvalidIdException("item id for amazon is invalid");
        }

        // Check if item is already in cache
        if (cache.containsKey(itemId)) {
            if ((System.currentTimeMillis() - cache.get(itemId).getLastUpdated()) / 1000 <= TIME_IN_CACHE_THRESHHOLD) {
                return cache.get(itemId);
            }
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

        Item item = new Item(itemId, price, name, "amazon", inStock, System.currentTimeMillis());
        cache.put(itemId, item);
        return item;
    }

    public Item getItemFromWebsite(String itemId, String website) {
        if (website.equalsIgnoreCase("amazon")) {
            return getItemFromAmazon(itemId);
        }

        throw new ItemNotFoundException("Invalid website name");
    }
}
