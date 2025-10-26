module com.joseramos.sistemaadocao {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;

    exports com.joseramos.sistemaadocao.entidades;
    exports com.joseramos.sistemaadocao.service;
    exports com.joseramos.sistemaadocao.repository;
    exports com.joseramos.sistemaadocao.GUI;
}