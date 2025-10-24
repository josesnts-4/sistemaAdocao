module com.joseramos.sistemaadocao {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.joseramos.sistemaadocao to javafx.fxml;
    exports com.joseramos.sistemaadocao;
    exports com.joseramos.sistemaadocao.casoPrecise;
    opens com.joseramos.sistemaadocao.casoPrecise to javafx.fxml;
}