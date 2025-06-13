/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafegui;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File; // Needed for creating directory
import java.util.stream.Collectors; // Needed for stream usage

/**
 *
 * @author anisp
 */
public class TextReceiptGenerator implements ReceiptGenerator {

    private String receiptsDirectory; // Field to store the receipts directory

    // Constructor to accept the receipts directory
    public TextReceiptGenerator(String receiptsDirectory) {
        this.receiptsDirectory = receiptsDirectory;
        // Ensure the directory exists
        File dir = new File(receiptsDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void generateReceipt(Order order) {
        String filename = receiptsDirectory + "/receipt_" + order.getOrderId() + ".txt"; // Use the directory field
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=====================================");
            writer.println("         RESTAURANT RECEIPT          ");
            writer.println("=====================================");
            writer.println("Order ID: " + order.getOrderId());
            writer.println("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("-------------------------------------");
            writer.println("Customer Name: " + order.getCustomerName());
            writer.println("Order Type: " + order.getOrderType());
            if (!order.getTableNumber().isEmpty()) {
                writer.println("Table Number: " + order.getTableNumber());
            }
            writer.println("Payment Method: " + order.getPaymentMethod());
            writer.println("-------------------------------------");
            writer.println("Items Ordered:");
            for (CartItem item : order.getItems()) {
                writer.println("- " + item.getMenuItem().getName() +
                               (item.getSelectedAddOns().isEmpty() ? "" : " (" +
                                item.getSelectedAddOns().stream().map(AddOn::getName).collect(Collectors.joining(", ")) + ")") +
                               " x" + item.getQuantity() +
                               " @ RM" + String.format("%.2f", item.getMenuItem().getPrice()) +
                               " = RM" + String.format("%.2f", item.getTotalPrice()));
            }
            writer.println("-------------------------------------");
            writer.println("Total: RM " + String.format("%.2f", order.getTotal()));
            writer.println("Status: " + order.getStatus());
            writer.println("Estimated Time: 15 minutes");
            writer.println("=====================================");
            System.out.println("Receipt generated: " + filename);
        } catch (IOException e) {
            System.err.println("Error generating receipt: " + e.getMessage());
        }
    }
}