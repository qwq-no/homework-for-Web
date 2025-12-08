package com.example.used.DAO;

import com.example.used.entity.Item;
import java.util.List;

public interface ItemDao {
    List<Item> findAll();
    void save(Item item); // ← 这个方法必须被实现！
    void deleteById(int id);
    Item findByIdAndUserId(int itemId, int userId);
    List<Item> searchItems(String keyword);
}