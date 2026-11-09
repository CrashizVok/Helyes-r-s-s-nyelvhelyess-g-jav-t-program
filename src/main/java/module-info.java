module com.example.javitoprogram {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;


    opens com.example.javitoprogram to javafx.fxml;
    exports com.example.javitoprogram;
}