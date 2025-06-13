/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafegui;

import java.util.List;

/**
 *
 * @author anisp
 */
public class SimpleOrderProcessor extends AbstractOrderProcessor {
    private int orderCounter;

    public SimpleOrderProcessor(List<Order> orders, ReceiptGenerator receiptGenerator, int initialOrderCounter) {
        super(orders, receiptGenerator);
        this.orderCounter = initialOrderCounter;
    }

    @Override
    public void processOrder(String customerName, String orderType, String tableNumber, String paymentMethod, List<CartItem> cart) {
        double total = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();

        Order order = new Order(orderCounter++, customerName, orderType, tableNumber, paymentMethod, cart, total);
        orders.add(order);
        receiptGenerator.generateReceipt(order);
    }
    
    @Override
    public void deleteOrder(Order order) {
        // Restore stock for deleted order
        for (CartItem item : order.getItems()) {
            MenuItem menuItem = item.getMenuItem();
            menuItem.setStock(menuItem.getStock() + item.getQuantity());
        }
        orders.remove(order);
    }
}