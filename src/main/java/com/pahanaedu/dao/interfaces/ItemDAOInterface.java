package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.Item;

import java.util.List;

public interface ItemDAOInterface {
    List<Item> getAllItems();

    Item getItemById(int itemId);

    List<Item> getItemsByCategory(int categoryId);

    boolean createItem(Item item);

    boolean updateItem(Item item);

    boolean deleteItem(int itemId);

    boolean isItemNameExists(String name);

    boolean isItemNameExistsForUpdate(String name, int itemId);

    List<String> getAllItemNames();
}
