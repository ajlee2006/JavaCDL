module Project {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    opens sample.Controller to javafx.fxml;
    opens sample;
}