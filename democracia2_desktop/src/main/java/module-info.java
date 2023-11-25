module com.example.democracia2_desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires javafx.web;


    opens com.example.democracia2_desktop to javafx.fxml;
    opens com.example.democracia2_desktop.presentation.controllers to javafx.fxml;
    exports com.example.democracia2_desktop;
}
