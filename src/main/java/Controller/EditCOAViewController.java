package Controller;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

/**
 * @author Ramon Johnson
 * @version 0.0.0.1
 * 9/6/2017
 */
public class EditCOAViewController implements Initializable
{
    private SessionFactory sessionFactory;

    private Stage stage;

    private ObservableList<String> operators;


    public EditCOAViewController(SessionFactory _factory, Stage _stage, String _operator, ObservableList<String> _operators ,String _orderNumber, ObservableList<String> _orderNumbers, String _partNumber, ObservableList<String> _partNumbers)
    {
        sessionFactory = _factory;
        stage = _stage;
        operatorTextField.setText(_operator);
        operators = _operators;
        orderNumberChoiceBox.setItems(_orderNumbers);
        orderNumberChoiceBox.setValue(_orderNumber);
        partNumberChoiceBox.setItems(_partNumbers);
        partNumberChoiceBox.setValue(_partNumber);
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="serialNumberTextField"
    private TextField serialNumberTextField; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberChoiceBox"
    private ChoiceBox<String> orderNumberChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="partNumberChoiceBox"
    private ChoiceBox<String> partNumberChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="operatorTextField"
    private TextField operatorTextField; // Value injected by FXMLLoader

    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    @FXML
    void exitStage(ActionEvent event) {
        stage.close();
    }

    @FXML
    void editCOA(ActionEvent event)
    {
        //--- Verify a few things ---//

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert serialNumberTextField != null : "fx:id=\"serialNumberTextField\" was not injected: check your FXML file 'EditCOAView.fxml'.";
        assert orderNumberChoiceBox != null : "fx:id=\"orderNumberChoiceBox\" was not injected: check your FXML file 'EditCOAView.fxml'.";
        assert partNumberChoiceBox != null : "fx:id=\"partNumberChoiceBox\" was not injected: check your FXML file 'EditCOAView.fxml'.";
        assert operatorTextField != null : "fx:id=\"operatorTextField\" was not injected: check your FXML file 'EditCOAView.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'EditCOAView.fxml'.";
        assert submitButton != null : "fx:id=\"submitButton\" was not injected: check your FXML file 'EditCOAView.fxml'.";
    }

    private void bindings()
    {
        StringProperty opsp = operatorTextField.textProperty();
        StringProperty snsp = serialNumberTextField.textProperty();
        BooleanBinding operatorBooleanBinding = opsp.isEmpty().or(opsp.length().lessThan(3).or(opsp.isEqualTo("")));
        BooleanBinding serialNumberBooleanBinding = snsp.length().lessThan(6).or(snsp.isEqualTo(""));
        ReadOnlyStringProperty selection = operatorTextField.textProperty();
        BooleanBinding isOperator = Bindings.createBooleanBinding(() -> operators.contains(selection.get()), selection);
        submitButton.disableProperty().bind(operatorBooleanBinding.or(serialNumberBooleanBinding.or(isOperator)));
    }


    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        bindings();
    }
}
