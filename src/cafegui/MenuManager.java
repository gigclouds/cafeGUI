/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafegui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Arrays; // NEW
import java.util.Collections; // NEW

public class MenuManager {
    private ObservableList<MenuItem> menuItems;

    public MenuManager() {
        this.menuItems = FXCollections.observableArrayList();
        initializeMenuItems();
    }

    private void initializeMenuItems() {
        menuItems.add(new MenuItem("Chicken Rice", "Rice", 5.00, 50, "https://static01.nyt.com/images/2025/01/28/multimedia/KP-Hainan-Chicken-Rice-hcgv/KP-Hainan-Chicken-Rice-hcgv-mediumSquareAt3X.jpg"));
        menuItems.add(new MenuItem("Gepuk Rice", "Rice", 6.00, 30, "https://ayamgepuktopglobal.my/wp-content/uploads/2023/12/ayam-gepok-top-global-set-b-min.jpg"));
        menuItems.add(new MenuItem("Mee Goreng", "Mee", 6.00, 40, "https://ucarecdn.com/e2fd02f3-bfeb-43a5-a564-72bab62a13f5/-/scale_crop/1280x1280/center/-/quality/normal/-/format/jpeg/mee-goreng-mamak.jpg"));
        menuItems.add(new MenuItem("Bihun Soup", "Mee", 4.00, 35, "https://ronamy.b-cdn.net/wp-content/uploads/2021/12/resepi-bihun-sup-chinese-style.png"));
        menuItems.add(new MenuItem("Sandwich", "Snack", 3.50, 25, "https://static.toiimg.com/thumb/83740315.cms?width=1200&height=900"));
        // Drinks with customizable add-ons using the new MenuItem constructor
        menuItems.add(new MenuItem("Latte", "Drink", 3.00, 100, "https://cdn.vox-cdn.com/uploads/chorus_image/image/73942014/IMG_1503.0.jpg",
                Arrays.asList(new AddOn("Honey", 0.50), new AddOn("Sugar", 0.50), new AddOn("Extra Shot", 1.00))));
        menuItems.add(new MenuItem("Tea", "Drink", 2.00, 80, "https://www.rappler.com/tachyon/2019/09/Screen-Shot-2022-05-17-at-4.42.48-PM.png",
                Arrays.asList(new AddOn("Sugar", 0.50), new AddOn("Lemon Slice", 0.20))));
        menuItems.add(new MenuItem("Chocolate", "Drink", 2.00, 60, "https://princesspinkygirl.com/wp-content/uploads/2021/01/Dirty-Snowman-square.jpg",
                Arrays.asList(new AddOn("Whipped Cream", 1.00), new AddOn("Marshmallows", 0.75))));
        menuItems.add(new MenuItem("Water", "Drink", 1.50, 200, "https://cdn1.npcdn.net/images/2ee7f03265ee71980dae20eaedcc9a24_1658811657.jpeg",
                Collections.emptyList())); // No add-ons for water, use emptyList
        menuItems.add(new MenuItem("Orange Juice", "Drink", 3.00, 50, "https://www.organicfacts.net/wp-content/uploads/orangejuice-1.jpg",
                Arrays.asList(new AddOn("Ice", 0.50))));
    }

    public ObservableList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public MenuItem getMenuItemByName(String name) {
        return menuItems.stream()
                        .filter(item -> item.getName().equals(name))
                        .findFirst()
                        .orElse(null);
    }
}
