module com.example.panoply {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires google.cloud.storage;
    requires google.cloud.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    opens com.example.panoply to javafx.fxml;
    exports com.example.panoply;
    exports com.example.panoply.GUI;
    opens com.example.panoply.GUI to javafx.fxml;

}