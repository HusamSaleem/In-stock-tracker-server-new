package com.potato.instock;

import com.potato.instock.exceptions.item.ItemInvalidIdException;
import com.potato.instock.exceptions.item.ItemNotFoundException;
import com.potato.instock.item.Item;
import com.potato.instock.item.ItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTests {

    @InjectMocks
    ItemService itemService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void tryGettingItemWithInvalidWebsite() {
        String itemId = "B0815XFSGK";
        String website = "amazonian";

        Item item = itemService.getItemFromWebsite(itemId, website);

        assertNull(item);
    }

    @Test(expected = ItemNotFoundException.class)
    public void tryGettingItemWithValidWebsiteInvalidID() {
        String itemId = "!@#$%^&*()";
        String website = "amazon";

        Item item = itemService.getItemFromWebsite(itemId, website);
    }

    @Test(expected = ItemInvalidIdException.class)
    public void tryGettingItemWithValidWebsiteInvalidID2() {
        String itemId = "123456";
        String website = "amazon";

        Item item = itemService.getItemFromWebsite(itemId, website);
    }

    @Test
    public void canGetItem() {
        String itemId = "B0815XFSGK";
        String website = "amazon";

        Item item = itemService.getItemFromWebsite(itemId, website);

        assertEquals(item.getItemId(), itemId);
    }

}
