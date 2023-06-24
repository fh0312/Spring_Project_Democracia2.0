module pt.ul.fc.di.css.javafxexample {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens pt.ul.fc.di.css.javafxexample to javafx.fxml, javafx.web;
    opens pt.ul.fc.di.css.javafxexample.presentation.control to javafx.fxml;
    opens pt.ul.fc.di.css.javafxexample.DTOs to com.fasterxml.jackson.databind;
    exports pt.ul.fc.di.css.javafxexample;
    exports pt.ul.fc.di.css.javafxexample.DTOs to com.fasterxml.jackson.databind;
}
