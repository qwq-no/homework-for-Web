package com.example.used.DAO;

import com.example.used.entity.Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.example.used.service.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.used.DAO.ItemDao;
import java.sql.*;
public class ItemDaoImpl implements ItemDao {

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        // 使用 JOIN 获取 username
        String sql = """
            SELECT i.id, i.name, i.description, i.user_id, u.username
            FROM items i
            JOIN users u ON i.user_id = u.id
            ORDER BY i.id DESC
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setUserId(rs.getInt("user_id"));
                item.setUsername(rs.getString("username")); // 来自 users 表
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }


    @Override
    public void save(Item item) {
        String sql = "INSERT INTO items (name, description, user_id) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setInt(3, item.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("保存商品失败", e);
        }
    }

    @Override
    public void deleteById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("商品ID必须大于0");
        }

        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate(); // DELETE 是更新操作
        } catch (SQLException e) {
            throw new RuntimeException("删除商品失败，ID: " + id, e);
        }
    }
    @Override
    public Item findByIdAndUserId(int itemId, int userId) {
        String sql = "SELECT id, name, description, user_id FROM items WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setName(rs.getString("name"));
                    item.setDescription(rs.getString("description"));
                    item.setUserId(rs.getInt("user_id"));
                    return item;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询商品失败", e);
        }
        return null;
    }

    @Override
    public List<Item> searchItems(String keyword) {
        String sql = """
        SELECT i.id, i.name, i.description, i.user_id, u.username
        FROM items i
        JOIN users u ON i.user_id = u.id
        WHERE i.name LIKE ? OR i.description LIKE ?
        ORDER BY i.id DESC
        """;

        List<Item> items = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setName(rs.getString("name"));
                    item.setDescription(rs.getString("description"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setUsername(rs.getString("username"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("搜索商品失败: " + keyword, e);
        }
        return items;
    }
}