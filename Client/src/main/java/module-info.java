module com.lab21.client {
    requires javafx.controls;
    requires javafx.fxml;
            
            requires com.dlsc.formsfx;
                    
    opens com.lab21.client to javafx.fxml;
    exports com.lab21.client;
}