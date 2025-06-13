/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafegui;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author anisp
 */
public class CartItem {
    private MenuItem menuItem;
    private int quantity;
    private List<AddOn> selectedAddOns; // Store a list of selected add-ons

    // Constructor now takes List<AddOn>
    public CartItem(MenuItem menuItem, int quantity, List<AddOn> selectedAddOns) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.selectedAddOns = selectedAddOns;
    }

    public double getTotalPrice() {
        double basePrice = menuItem.getPrice();
        double customizationCost = selectedAddOns.stream()
                                                .mapToDouble(AddOn::getPrice)
                                                .sum();
        return (basePrice + customizationCost) * quantity;
    }

    @Override
    public String toString() {
        String customizations = "";
        if (!selectedAddOns.isEmpty()) {
            customizations = " (" + selectedAddOns.stream()
                                                  .map(AddOn::getName)
                                                  .collect(Collectors.joining(", ")) + ")";
        }
        return menuItem.getName() + customizations + " x" + quantity + " - RM " + String.format("%.2f", getTotalPrice());
    }

    // Getters
    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public List<AddOn> getSelectedAddOns() { return selectedAddOns; }
}