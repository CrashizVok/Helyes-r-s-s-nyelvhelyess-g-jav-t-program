module com.example.javitoprogram {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javitoprogram to javafx.fxml;
    exports com.example.javitoprogram;
}