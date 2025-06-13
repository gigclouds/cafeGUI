/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafegui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author anisp
 */
public class CartManager {
    private ObservableList<CartItem> cartItems;

    public CartManager() {
        cartItems = FXCollections.observableArrayList();
    }

    public ObservableList<CartItem> getCartItems() {
        return cartItems;
    }

    public void addItem(CartItem item) {
        cartItems.add(item);
    }

    public void removeItem(CartItem item) {
        cartItems.remove(item);
        // Restore stock
        item.getMenuItem().setStock(item.getMenuItem().getStock() + item.getQuantity());
    }

    public double calculateTotal() {
        return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    public void clearCart() {
        cartItems.clear();
    }
}