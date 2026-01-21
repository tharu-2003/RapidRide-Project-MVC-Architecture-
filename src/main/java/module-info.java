module com.ijse.gdse72.rapidride {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.graphics;
    requires mysql.connector.j;
    requires jdk.jdi;
    requires java.sql;
    requires lombok;
    requires java.desktop;
    requires net.sf.jasperreports.core;

    opens com.ijse.gdse72.rapidride.dto.tm to javafx.fxml;
    opens com.ijse.gdse72.rapidride.controller to javafx.fxml;
    exports com.ijse.gdse72.rapidride;
    exports com.ijse.gdse72.rapidride.dto.tm;
}