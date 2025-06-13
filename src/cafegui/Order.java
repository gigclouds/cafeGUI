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
public class Order {
     private int orderId;
    private String customerName;
    private String orderType;
    private String tableNumber;
    private String paymentMethod;
    private List<CartItem> items;
    private double total;
    private String status;
    
    public Order(int orderId, String customerName, String orderType, String tableNumber, 
                 String paymentMethod, List<CartItem> items, double total) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderType = orderType;
        this.tableNumber = tableNumber;
        this.paymentMethod = paymentMethod;
        this.items = items;
        this.total = total;
        this.status = "Pending";
    }
    
    // Getters and setters
    public int getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public List<CartItem> getItems() { return items; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}