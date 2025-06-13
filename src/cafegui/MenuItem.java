/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafegui;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author anisp
 */
public class MenuItem {

    private String name;
    private String category;
    private double price;
    private int stock;
    private String imageUrl;
    private List<AddOn> addOns; // NEW: For customizable options

    // Constructor without addOns (for non-customizable items)
    public MenuItem(String name, String category, double price, int stock, String imageUrl) {
        this(name, category, price, stock, imageUrl, Collections.emptyList()); // Default to no add-ons
    }

    // NEW: Updated constructor to include addOns
    public MenuItem(String name, String category, double price, int stock, String imageUrl, List<AddOn> addOns) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.addOns = addOns;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImageUrl() { return imageUrl; }
    public List<AddOn> getAddOns() { return addOns; } // NEW: Getter for addOns
}