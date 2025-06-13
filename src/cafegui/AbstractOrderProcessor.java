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
public abstract class AbstractOrderProcessor {
    protected List<Order> orders;
    protected ReceiptGenerator receiptGenerator;

    public AbstractOrderProcessor(List<Order> orders, ReceiptGenerator receiptGenerator) {
        this.orders = orders;
        this.receiptGenerator = receiptGenerator;
    }

    public abstract void processOrder(String customerName, String orderType, String tableNumber, String paymentMethod, List<CartItem> cart);
    
    public abstract void deleteOrder(Order order);
}