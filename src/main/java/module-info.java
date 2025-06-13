module com.example.demo3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.demo3 to javafx.fxml;
    exports com.example.demo3;
}