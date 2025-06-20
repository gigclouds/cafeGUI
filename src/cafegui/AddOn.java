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

public class AddOn {
    private String name;
    private double price;

    public AddOn(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " (+RM " + String.format("%.2f", price) + ")";
    }
}
