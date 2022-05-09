module com.myprivate.currency_converter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.xml;

    requires org.kordamp.ikonli.elusive;

    opens com.myprivate.currency_converter to javafx.fxml;
    exports com.myprivate.currency_converter;
    exports com.myprivate.currency_converter.outDatedFiles;
    opens com.myprivate.currency_converter.outDatedFiles to javafx.fxml;
}