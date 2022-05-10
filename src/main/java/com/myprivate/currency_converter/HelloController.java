package com.myprivate.currency_converter;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.elusive.Elusive;
import org.kordamp.ikonli.javafx.FontIcon;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Locale;


public class HelloController {
    @FXML
    public GridPane mainGrid;
    @FXML
    public BorderPane rootPane;
    @FXML
    public Button connectionIcon;
    @FXML
    public Button refreshButton;
    @FXML
    public TextField upperTextField;
    @FXML
    public TextField lowerTextField;
    @FXML
    public ComboBox upperCombo;
    @FXML
    public ComboBox lowerCombo;

    private int previousOperationFlag;

    private XMLReader xmlReader;
    private ObservableList<Currency> currenciesObservable;
    private boolean connectionVerification;

    private final FontIcon okIcon = FontIcon.of(Elusive.RSS, 20, Color.DARKGREEN);
    private final FontIcon errorIcon = FontIcon.of(Elusive.RSS, 20, Color.RED);
    private XMLCommunication xmlCommunication;
    private boolean previousStateOfConnectionVerification;


    public HelloController() throws IOException, ParserConfigurationException, SAXException {

        xmlReader = new XMLReader();
        currenciesObservable = FXCollections.observableList(xmlReader.getXmlList());
        connectionVerification = false;
        xmlCommunication = new XMLCommunication();
    }

    @FXML
    private void initialize() throws ParserConfigurationException, IOException, SAXException {

        upperCombo.setItems(currenciesObservable);
        lowerCombo.setItems(currenciesObservable);

        this.xmlReader.populateCurrencyList(false);
        guiCodeModifiers();
        connectionListenerTasker();

    }


    private void guiCodeModifiers() {
        //column and row size based on % size of screen
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(c1, c2);

        RowConstraints r1 = new RowConstraints();
        r1.setPercentHeight(30);
        RowConstraints r2 = new RowConstraints();
        r2.setPercentHeight(30);
        RowConstraints r3 = new RowConstraints();
        r3.setPercentHeight(30);
        mainGrid.getRowConstraints().addAll(r1, r2, r3);

        //font sizing
        DoubleProperty fontSize = new SimpleDoubleProperty(10);
        fontSize.bind(rootPane.widthProperty().add(rootPane.heightProperty()).divide(70));
        mainGrid.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));

    }

    private void calculations(TextField textInput, TextField textOutput, ComboBox comboInput, ComboBox comboOutput) {
        try {
            double inputFromUpper = Double.parseDouble(textInput.getText());
            int upperIndex = comboInput.getSelectionModel().getSelectedIndex();
            int lowerIndex = comboOutput.getSelectionModel().getSelectedIndex();
            double calculation = (inputFromUpper * xmlReader.getElement(upperIndex)) / xmlReader.getElement(lowerIndex);
            textOutput.setText(String.format(Locale.US, "%.02f", calculation));
        } catch (IndexOutOfBoundsException e) {
            alertForIncompleteData();
        } catch (NumberFormatException e) {
            alertForIncompleteData();
            upperTextField.clear();
            lowerTextField.clear();
        }
    }

    @FXML
    public void onClickCalculations() {

        if (upperTextField.getText().isEmpty()) {
            calculations(lowerTextField, upperTextField, lowerCombo, upperCombo);
            this.previousOperationFlag = 1;

        } else if (lowerTextField.getText().isEmpty()) {
            calculations(upperTextField, lowerTextField, upperCombo, lowerCombo);
            this.previousOperationFlag = 2;

        } else {
            if (this.previousOperationFlag == 1) {
                upperTextField.clear();
                calculations(lowerTextField, upperTextField, lowerCombo, upperCombo);
            } else if (this.previousOperationFlag == 2) {
                lowerTextField.clear();
                calculations(upperTextField, lowerTextField, upperCombo, lowerCombo);
            }
        }

    }

    @FXML
    public void upperTextBoxSetup() {
        if (!lowerTextField.getText().isEmpty()) {
            lowerTextField.clear();
        }
    }

    @FXML
    public void lowerTextBoxSetup() {
        if (!upperTextField.getText().isEmpty()) {
            upperTextField.clear();
        }
    }

    private void alertForIncompleteData() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("");
        alert.setContentText("Currencies not selected or TextBox data is invalid. " +
                "Check if both currencies are selected and TextBoxFields consist only of numbers and \".\" operator. In case of invalid data," +
                " both TextBoxes will be cleared");
        alert.showAndWait();
    }


    private void alertForConnectionIssues() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("");
        alert.setContentText("Connection Error. Check connection status icon on the left");
        alert.showAndWait();
    }

    @FXML
    public void comboBoxClearForUpper() {
        if (!upperTextField.getText().isEmpty() && !lowerTextField.getText().isEmpty()) {
            lowerTextField.clear();
        }
    }

    @FXML
    public void comboBoxClearForLower() {
        if (!upperTextField.getText().isEmpty() && !lowerTextField.getText().isEmpty()) {
            upperTextField.clear();
        }
    }

    @FXML
    public void closeApp() {
        System.exit(0);
    }

    @FXML
    public void refreshCurrencyRatings() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        if (!this.connectionVerification) {
            alertForConnectionIssues();
        } else {
            this.xmlReader.xmlListUpdate();
            currenciesObservable = FXCollections.observableList(xmlReader.getXmlList());
            upperCombo.setItems(currenciesObservable);
            lowerCombo.setItems(currenciesObservable);

        }
    }

    private void connectionListenerMethod() throws IOException {
        boolean control = xmlCommunication.connectionListener();
        if (control && previousStateOfConnectionVerification != control) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    connectionIcon.setTooltip(new Tooltip("You are Currently Online"));
                    connectionIcon.setGraphic(okIcon);
                    connectionVerification = true;
                    System.out.println("icon should be green!\n");
                    previousStateOfConnectionVerification = true;

                }
            });
        } else if (!control && previousStateOfConnectionVerification != control) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    connectionIcon.setTooltip(new Tooltip("You are Currently Offline"));
                    connectionIcon.setGraphic(errorIcon);
                    connectionVerification = false;
                    System.out.println("icon should be red!\n");
                    previousStateOfConnectionVerification = false;
                }
            });

        }
    }

    private void connectionListenerTasker() {
        Thread th;
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                while (true) {
                    try {
                        connectionListenerMethod();
                        Thread.sleep(2000);
                        System.out.println("loop works");
                        System.out.println("value of connection flag is equal to:" + connectionVerification);
                    } catch (IOException | InterruptedException e) {
                        System.out.println("error");
                    }

                }
            }
        };

        th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}