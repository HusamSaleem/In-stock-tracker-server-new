package com.potato.instock.controller;

import com.potato.instock.model.Item;
import com.potato.instock.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/item")
@CrossOrigin
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Item getItemFromWebsite(
            @RequestParam String itemId,
            @RequestParam String website) {
        return itemService.getItemFromWebsite(itemId, website);
    }
}
