/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package cafegui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.File; // Needed for receipts directory creation
import javafx.scene.Node; // Needed for iterating children in GridPane (refreshMenuGrid)
import java.util.stream.Collectors; // NEW for stream usage in customization

/**
 *
 * @author anisp
 */
public class CafeGUI extends Application {

    private MenuManager menuManager;
    private CartManager cartManager;
    private ObservableList<Order> orders;
    private AbstractOrderProcessor orderProcessor;
    private ReceiptGenerator receiptGenerator;

    // Main stage and scenes
    private Stage primaryStage;
    private Scene mainScene, placeOrderScene, viewOrderScene, loginScene;

    // Make orderTable and orderDetailsArea fields for access across methods
    private TableView<Order> orderTable;
    private TextArea orderDetailsArea;

    // Total label reference for updating
    private Label placeOrderTotalLabel;

    // Make orderMenuGrid a class field so it can be accessed from cart item removal's action
    private GridPane orderMenuGrid;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialize managers and processors
        menuManager = new MenuManager();
        orders = FXCollections.observableArrayList(); // Initialize orders here
        receiptGenerator = new TextReceiptGenerator("receipts"); // Corrected constructor call
        orderProcessor = new SimpleOrderProcessor(orders, receiptGenerator, 1); // Starting order ID from 1

        // Ensure receipts directory exists (handled by TextReceiptGenerator constructor now, but keeping for clarity)
        File receiptsDir = new File("receipts");
        if (!receiptsDir.exists()) {
            receiptsDir.mkdirs();
        }

        // Create main scene
        createMainScene();

        // Create login scene
        LoginScene loginSceneHandler = new LoginScene(primaryStage, mainScene);
        loginScene = loginSceneHandler.createScene();

        primaryStage.setTitle("Restaurant Ordering System");
        primaryStage.setScene(loginScene); // Start with login scene
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.show();
    }

    private void createMainScene() {
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(50));
        mainLayout.setStyle("-fx-background-color: #White;");

        // Welcome heading
        Label welcomeLabel = new Label("Welcome to Our Restaurant");
        welcomeLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Buttons
        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button placeOrderBtn = new Button("Place Order");
        Button viewOrderBtn = new Button("View Orders");
        Button exitBtn = new Button("Exit");

        // Style buttons
        String buttonStyle = "-fx-font-size: 20px; -fx-padding: 20 40; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 10; -fx-min-width: 200px;";
        placeOrderBtn.setStyle(buttonStyle);
        viewOrderBtn.setStyle(buttonStyle);
        exitBtn.setStyle(buttonStyle + "-fx-background-color: #e74c3c;");

        // Button hover effects
        placeOrderBtn.setOnMouseEntered(e -> placeOrderBtn.setStyle(buttonStyle + "-fx-background-color: #2980b9;"));
        placeOrderBtn.setOnMouseExited(e -> placeOrderBtn.setStyle(buttonStyle));

        viewOrderBtn.setOnMouseEntered(e -> viewOrderBtn.setStyle(buttonStyle + "-fx-background-color: #2980b9;"));
        viewOrderBtn.setOnMouseExited(e -> viewOrderBtn.setStyle(buttonStyle));

        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle(buttonStyle + "-fx-background-color: #c0392b;"));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle(buttonStyle + "-fx-background-color: #e74c3c;"));

        // Button actions
        placeOrderBtn.setOnAction(e -> showPlaceOrderScene());
        viewOrderBtn.setOnAction(e -> showViewOrderScene());
        exitBtn.setOnAction(e -> primaryStage.close());

        buttonBox.getChildren().addAll(placeOrderBtn, viewOrderBtn, exitBtn);
        mainLayout.getChildren().addAll(welcomeLabel, buttonBox);

        mainScene = new Scene(mainLayout);
    }

    private void showPlaceOrderScene() {
        cartManager = new CartManager(); // New cart for each new order

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label title = new Label("Place Your Order");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Customer info form
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setPrefWidth(200);

        ComboBox<String> orderTypeBox = new ComboBox<>();
        orderTypeBox.setItems(FXCollections.observableArrayList("Takeaway", "Dine In"));
        orderTypeBox.setPromptText("Select order type");
        orderTypeBox.setPrefWidth(200);

        TextField tableField = new TextField();
        tableField.setPromptText("Table number");
        tableField.setPrefWidth(200);
        tableField.setDisable(true);

        // Payment method selection
        ComboBox<String> paymentBox = new ComboBox<>();
        paymentBox.setItems(FXCollections.observableArrayList("Cash", "Card", "E-Wallet"));
        paymentBox.setPromptText("Select payment method");
        paymentBox.setPrefWidth(200);

        orderTypeBox.setOnAction(e -> {
            tableField.setDisable(!orderTypeBox.getValue().equals("Dine In"));
        });

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Order Type:"), 0, 1);
        form.add(orderTypeBox, 1, 1);
        form.add(new Label("Table Number:"), 0, 2);
        form.add(tableField, 1, 2);
        form.add(new Label("Payment Method:"), 0, 3);
        form.add(paymentBox, 1, 3);

        // Cart display
        ListView<CartItem> cartView = new ListView<>(cartManager.getCartItems());
        cartView.setPrefHeight(200);

        // Custom cell factory for cartView to show "Remove" button per item
        cartView.setCellFactory(lv -> new ListCell<CartItem>() {
            private final HBox hbox = new HBox(10);
            private final Label itemDetails = new Label();
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 3; -fx-font-size: 10px; -fx-padding: 3 8;");
                removeButton.setOnAction(event -> {
                    CartItem itemToRemove = getItem();
                    if (itemToRemove != null) {
                        // Restore stock when item is removed from cart
                        itemToRemove.getMenuItem().setStock(itemToRemove.getMenuItem().getStock() + itemToRemove.getQuantity());
                        cartManager.removeItem(itemToRemove);
                        
                        // Refresh the menu grid to show updated stock
                        refreshMenuGrid(orderMenuGrid); // Pass orderMenuGrid directly now
                        // updateTotalLabel() is already triggered by cartManager's ObservableList listener
                    }
                });
                hbox.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(itemDetails, Priority.ALWAYS); // Allow item details to take up space
                hbox.getChildren().addAll(itemDetails, removeButton);
            }

            @Override
            protected void updateItem(CartItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    itemDetails.setText(item.toString());
                    setGraphic(hbox);
                }
            }
        });

        // Total label
        placeOrderTotalLabel = new Label("Total: RM 0.00");
        placeOrderTotalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        // Add listener to automatically update total when cart changes
        cartManager.getCartItems().addListener((javafx.collections.ListChangeListener<CartItem>) change -> {
            updateTotalLabel();
        });

        // Menu items for ordering
        orderMenuGrid = new GridPane();
        orderMenuGrid.setHgap(15);
        orderMenuGrid.setVgap(15);
        orderMenuGrid.setAlignment(Pos.CENTER);

        refreshMenuGrid(orderMenuGrid); // Populate initial menu

        ScrollPane menuScrollPane = new ScrollPane(orderMenuGrid);
        menuScrollPane.setPrefHeight(400);
        menuScrollPane.setStyle("-fx-background-color: white;");

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button checkoutBtn = new Button("Checkout");
        Button backBtn = new Button("Back to Main");

        checkoutBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 5;");
        backBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 5;");

        checkoutBtn.setOnAction(e -> {
            if (nameField.getText().trim().isEmpty() || orderTypeBox.getValue() == null || paymentBox.getValue() == null) {
                showAlert("Error", "Please fill in all required fields including payment method.");
                return;
            }

            if (orderTypeBox.getValue().equals("Dine In") && tableField.getText().trim().isEmpty()) {
                showAlert("Error", "Please enter table number for dine-in orders.");
                return;
            }

            if (cartManager.getCartItems().isEmpty()) {
                showAlert("Error", "Please add items to your cart.");
                return;
            }

            processOrder(nameField.getText().trim(), orderTypeBox.getValue(),
                    tableField.getText().trim(), paymentBox.getValue(), new ArrayList<>(cartManager.getCartItems())); // Pass a copy
        });

        backBtn.setOnAction(e -> primaryStage.setScene(mainScene));

        buttonBox.getChildren().addAll(checkoutBtn, backBtn);

        layout.getChildren().addAll(title, form, new Label("Menu:"), menuScrollPane,
                new Label("Cart:"), cartView, placeOrderTotalLabel, buttonBox);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        placeOrderScene = new Scene(scrollPane, 1000, 700);
        primaryStage.setScene(placeOrderScene);
    }

    private void refreshMenuGrid(GridPane grid) {
        grid.getChildren().clear(); // Clear existing items
        int col = 0, row = 0;
        for (MenuItem item : menuManager.getMenuItems()) {
            VBox itemBox = createOrderItemBox(item); // Pass only item
            grid.add(itemBox, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createOrderItemBox(MenuItem item) {
        VBox itemBox = new VBox(8);
        itemBox.setAlignment(Pos.CENTER);
        itemBox.setPadding(new Insets(10));
        itemBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-color: white; -fx-background-radius: 5;");
        itemBox.setPrefWidth(180);

        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        Label priceLabel = new Label("RM " + String.format("%.2f", item.getPrice()));
        priceLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #e74c3c;");

        // Keep a reference to the stock label so we can update it
        Label stockLabel = new Label("Stock: " + item.getStock());
        stockLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #95a5a6;");

        Button addBtn = new Button("Add to Cart");
        addBtn.setStyle("-fx-font-size: 10px; -fx-padding: 5 10; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 3;");

        ImageView imageView = null;
        try {
            Image image = new Image(item.getImageUrl(), 150, 100, true, true);
            imageView = new ImageView(image);
        } catch (Exception e) {
            System.out.println("Failed to load image for " + item.getName());
        }

        addBtn.setOnAction(e -> {
            if (item.getStock() <= 0) {
                showAlert("Out of Stock", item.getName() + " is out of stock!");
                return;
            }

            if (item.getCategory().equals("Drink") && !item.getAddOns().isEmpty()) { // Check if it's a drink and has add-ons
                showDrinkCustomization(item); // Pass only item, use cartManager
            } else {
                // For food items or drinks without add-ons
                CartItem cartItem = new CartItem(item, 1, new ArrayList<>()); // No selected add-ons
                cartManager.addItem(cartItem);
                item.setStock(item.getStock() - 1);
                stockLabel.setText("Stock: " + item.getStock()); // Update stock label
            }
        });

        if (imageView != null) {
            itemBox.getChildren().add(imageView);
        }
        itemBox.getChildren().addAll(nameLabel, priceLabel, stockLabel, addBtn);
        return itemBox;
    }

    private void showDrinkCustomization(MenuItem item) {
        Dialog<List<AddOn>> dialog = new Dialog<>();
        dialog.setTitle("Customize " + item.getName());
        dialog.setHeaderText("Add customizations:");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        // Map to hold checkboxes and their associated add-ons
        List<CheckBox> addOnCheckboxes = new ArrayList<>();

        Label totalLabel = new Label("Total: RM " + String.format("%.2f", item.getPrice()));
        totalLabel.setStyle("-fx-font-weight: bold;");

        // Create checkboxes dynamically for each add-on
        for (AddOn addOn : item.getAddOns()) {
            CheckBox cb = new CheckBox(addOn.toString()); // AddOn's toString provides formatted text
            addOnCheckboxes.add(cb);
            content.getChildren().add(cb);
        }

        // Add a listener to update the total label whenever a checkbox is selected/deselected
        Runnable updateTotal = () -> {
            double currentTotal = item.getPrice();
            for (int i = 0; i < addOnCheckboxes.size(); i++) {
                if (addOnCheckboxes.get(i).isSelected()) {
                    currentTotal += item.getAddOns().get(i).getPrice();
                }
            }
            totalLabel.setText("Total: RM " + String.format("%.2f", currentTotal));
        };

        // Attach listener to all checkboxes
        for (CheckBox cb : addOnCheckboxes) {
            cb.setOnAction(e -> updateTotal.run());
        }

        content.getChildren().add(totalLabel); // Add total label last
        dialog.getDialogPane().setContent(content);

        ButtonType addButtonType = new ButtonType("Add to Cart", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                List<AddOn> selected = new ArrayList<>();
                for (int i = 0; i < addOnCheckboxes.size(); i++) {
                    if (addOnCheckboxes.get(i).isSelected()) {
                        selected.add(item.getAddOns().get(i));
                    }
                }
                return selected; // Return the list of selected AddOn objects
            }
            return null;
        });

        Optional<List<AddOn>> result = dialog.showAndWait();
        result.ifPresent(selectedAddOns -> {
            // Create CartItem with selected add-ons
            CartItem cartItem = new CartItem(item, 1, selectedAddOns);
            cartManager.addItem(cartItem);
            item.setStock(item.getStock() - 1);
            // Refresh the specific item's stock label in the menu grid
            for (Node node : orderMenuGrid.getChildren()) {
                if (node instanceof VBox) {
                    VBox itemBox = (VBox) node;
                    // Check if first child is ImageView to determine position of name/stock label
                    if (!itemBox.getChildren().isEmpty() && itemBox.getChildren().get(0) instanceof ImageView) {
                        Label associatedNameLabel = (Label) itemBox.getChildren().get(1); // Name at index 1 after ImageView
                        if (associatedNameLabel.getText().equals(item.getName())) {
                            Label stockLabel = (Label) itemBox.getChildren().get(3); // Stock at index 3
                            stockLabel.setText("Stock: " + item.getStock());
                            break;
                        }
                    } else if (!itemBox.getChildren().isEmpty() && itemBox.getChildren().get(0) instanceof Label) {
                        Label associatedNameLabel = (Label) itemBox.getChildren().get(0); // Name at index 0 if no ImageView
                        if (associatedNameLabel.getText().equals(item.getName())) {
                            Label stockLabel = (Label) itemBox.getChildren().get(2); // Stock at index 2
                            stockLabel.setText("Stock: " + item.getStock());
                            break;
                        }
                    }
                }
            }
        });
    }

    private void updateTotalLabel() {
        placeOrderTotalLabel.setText("Total: RM " + String.format("%.2f", cartManager.calculateTotal()));
    }

    private void processOrder(String customerName, String orderType, String tableNumber, String paymentMethod, List<CartItem> cart) {
        orderProcessor.processOrder(customerName, orderType, tableNumber, paymentMethod, cart);

        // Get the last order from the orders list, assuming it's the one just processed
        Order lastOrder = orders.get(orders.size() - 1);

        showAlert("Order Confirmed",
                "Order #" + lastOrder.getOrderId() + " has been placed successfully!\n" +
                        "Payment Method: " + paymentMethod + "\n" +
                        "Estimated time: 15 minutes\n" +
                        "Total: RM " + String.format("%.2f", lastOrder.getTotal()));

        // Clear the cart after successful order
        cartManager.clearCart();
        primaryStage.setScene(mainScene);
    }

    private void showViewOrderScene() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label title = new Label("Your Orders");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        orderTable = new TableView<>();
        orderTable.setItems(orders);

        TableColumn<Order, Integer> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getOrderId()).asObject());
        idCol.setPrefWidth(80);

        TableColumn<Order, String> nameCol = new TableColumn<>("Customer");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCustomerName()));
        nameCol.setPrefWidth(120);

        TableColumn<Order, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getOrderType()));
        typeCol.setPrefWidth(100);

        TableColumn<Order, String> tableCol = new TableColumn<>("Table");
        tableCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTableNumber()));
        tableCol.setPrefWidth(80);

        TableColumn<Order, String> paymentCol = new TableColumn<>("Payment");
        paymentCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPaymentMethod()));
        paymentCol.setPrefWidth(100);

        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        statusCol.setPrefWidth(100);

        TableColumn<Order, Double> totalCol = new TableColumn<>("Total (RM)");
        totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotal()).asObject());
        totalCol.setPrefWidth(100);

        TableColumn<Order, String> itemsCol = new TableColumn<>("Items");
        itemsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getItems().size() + " item(s)"));
        itemsCol.setPrefWidth(80);

        orderTable.getColumns().addAll(idCol, nameCol, typeCol, tableCol, paymentCol, statusCol, totalCol, itemsCol);

        // Order details area
        orderDetailsArea = new TextArea();
        orderDetailsArea.setPrefRowCount(4);
        orderDetailsArea.setEditable(false);
        orderDetailsArea.setPromptText("Select an order to view details...");

        orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                StringBuilder details = new StringBuilder();
                details.append("Order #").append(newSelection.getOrderId()).append(" Details:\n");
                details.append("Customer: ").append(newSelection.getCustomerName()).append("\n");
                details.append("Type: ").append(newSelection.getOrderType()).append("\n");
                if (!newSelection.getTableNumber().isEmpty()) {
                    details.append("Table: ").append(newSelection.getTableNumber()).append("\n");
                }
                details.append("Payment: ").append(newSelection.getPaymentMethod()).append("\n");
                details.append("Status: ").append(newSelection.getStatus()).append("\n");
                details.append("Items:\n");
                for (CartItem item : newSelection.getItems()) {
                    details.append("- ").append(item.toString()).append("\n");
                }
                details.append("Total: RM ").append(String.format("%.2f", newSelection.getTotal()));
                orderDetailsArea.setText(details.toString());
            }
        });

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button editBtn = new Button("Edit Order");
        Button deleteBtn = new Button("Delete Order");
        Button backBtn = new Button("Back to Main");

        editBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        backBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");

        editBtn.setOnAction(e -> {
            Order selected = orderTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditOrderDialog(selected);
            } else {
                showAlert("No Selection", "Please select an order to edit.");
            }
        });

        deleteBtn.setOnAction(e -> {
            Order selected = orderTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Order");
                alert.setHeaderText("Are you sure you want to delete this order?");
                alert.setContentText("Order #" + selected.getOrderId() + " will be permanently deleted.\nStock will be restored for all items.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    orderProcessor.deleteOrder(selected); // Use orderProcessor to delete and restore stock
                    orderDetailsArea.clear();
                    showAlert("Order Deleted", "Order #" + selected.getOrderId() + " has been deleted successfully.\nStock has been restored.");
                }
            } else {
                showAlert("No Selection", "Please select an order to delete.");
            }
        });

        backBtn.setOnAction(e -> primaryStage.setScene(mainScene));

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        refreshBtn.setOnAction(e -> {
            orderTable.refresh();
            showAlert("Refreshed", "Order list has been refreshed!");
        });

        buttonBox.getChildren().addAll(editBtn, deleteBtn, refreshBtn, backBtn);

        Label detailsLabel = new Label("Order Details:");
        detailsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        layout.getChildren().addAll(title, orderTable, detailsLabel, orderDetailsArea, buttonBox);

        viewOrderScene = new Scene(layout, 1000, 700);
        primaryStage.setScene(viewOrderScene);
    }

    private void showEditOrderDialog(Order order) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Order #" + order.getOrderId());
        dialog.setHeaderText("Edit order details:");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Customer name field
        TextField nameField = new TextField(order.getCustomerName());
        nameField.setPromptText("Customer name");

        // Order type
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.setItems(FXCollections.observableArrayList("Takeaway", "Dine In"));
        typeBox.setValue(order.getOrderType());

        // Table number
        TextField tableField = new TextField(order.getTableNumber());
        tableField.setPromptText("Table number");
        tableField.setDisable(!order.getOrderType().equals("Dine In"));

        // Payment method
        ComboBox<String> paymentBox = new ComboBox<>();
        paymentBox.setItems(FXCollections.observableArrayList("Cash", "Card", "E-Wallet"));
        paymentBox.setValue(order.getPaymentMethod());

        typeBox.setOnAction(e -> {
            tableField.setDisable(!typeBox.getValue().equals("Dine In"));
            if (!typeBox.getValue().equals("Dine In")) {
                tableField.clear();
            }
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Customer Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Order Type:"), 0, 1);
        form.add(typeBox, 1, 1);
        form.add(new Label("Table Number:"), 0, 2);
        form.add(tableField, 1, 2);
        form.add(new Label("Payment Method:"), 0, 3);
        form.add(paymentBox, 1, 3);

        content.getChildren().add(form);
        dialog.getDialogPane().setContent(content);

        ButtonType saveButtonType = new ButtonType("Save Changes", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (nameField.getText().trim().isEmpty()) {
                    showAlert("Error", "Customer name cannot be empty.");
                    return false;
                }

                if (typeBox.getValue().equals("Dine In") && tableField.getText().trim().isEmpty()) {
                    showAlert("Error", "Table number is required for dine-in orders.");
                    return false;
                }

                order.setCustomerName(nameField.getText().trim());
                order.setOrderType(typeBox.getValue());
                order.setTableNumber(tableField.getText().trim());
                order.setPaymentMethod(paymentBox.getValue());

                return true;
            }
            return false;
        });

        Optional<Boolean> result = dialog.showAndWait();
        if (result.isPresent() && result.get()) {
            // Refresh the table view to show updated data
            orderTable.refresh();

            // Update the order details area if this order is still selected
            if (orderTable.getSelectionModel().getSelectedItem() == order) {
                StringBuilder details = new StringBuilder();
                details.append("Order #").append(order.getOrderId()).append(" Details:\n");
                details.append("Customer: ").append(order.getCustomerName()).append("\n");
                details.append("Type: ").append(order.getOrderType()).append("\n");
                if (!order.getTableNumber().isEmpty()) {
                    details.append("Table: ").append(order.getTableNumber()).append("\n");
                }
                details.append("Payment: ").append(order.getPaymentMethod()).append("\n");
                details.append("Status: ").append(order.getStatus()).append("\n");
                details.append("Items:\n");
                for (CartItem item : order.getItems()) {
                    details.append("- ").append(item.toString()).append("\n");
                }
                details.append("Total: RM ").append(String.format("%.2f", order.getTotal()));
                orderDetailsArea.setText(details.toString());
            }

            showAlert("Order Updated", "Order #" + order.getOrderId() + " has been updated successfully!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}