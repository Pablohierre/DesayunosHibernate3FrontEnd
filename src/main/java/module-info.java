module com.mycompany.desayunoshibernate {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires java.persistence;
    requires java.desktop;
    requires jasperreports;

    opens com.mycompany.desayunoshibernate3 to javafx.fxml, org.hibernate.orm.core, java.sql;
    opens com.mycompany.desayunoshibernate3.models;
    exports com.mycompany.desayunoshibernate3;


}
