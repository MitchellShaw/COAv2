package Controller;
/**
 * Sample Skeleton for 'CreateOrderView.fxml' Controller Class
 */

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.COA;
import Model.Order;
import Model.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author Ramon Johnson
 * 2017/08/28
 * @version 1.0.0.1
 */
public class CreateOrderViewController implements Initializable
{
    /**
     * Stage object to be used for closing the window when the use for this View is done
     */
    private Stage stage;

    /**
     * SessionFactory that is passed down to all classes of this Application so it can be used
     * to run/create queries and make changes to the database by creating Session objects
     */
    private SessionFactory sessionFactory;

    /**
     * Offset location for X
     */
    private static double xOffset = 0;

    /**
     * Offset location for Y
     */
    private static double yOffset = 0;

    CreateOrderViewController(SessionFactory _factory)
    {
        sessionFactory = _factory;
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="gridP"
    private GridPane gridP; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberTextField"
    private TextField orderNumberTextField; // Value injected by FXMLLoader

    @FXML // fx:id="countUnitFields"
    private TextField countUnitFields; // Value injected by FXMLLoader

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader

    @FXML // fx:id="datePicker"
    private DatePicker datePicker; // Value injected by FXMLLoader


    /**
     * @param event Event object that will close the window
     */
    @FXML
    private void closeStage(ActionEvent event) {
        stage.close();
    }

    /**
     * @param event Event object that will send Order to the database
     */
    @FXML
    private void sendDataToDatabase(ActionEvent event)
    {
        Order coaOrder = new Order();
        coaOrder.setOrderNumber(Integer.parseInt(orderNumberTextField.getText()));
        coaOrder.setScheduledShipDate(datePicker.getValue());
        coaOrder.setCoaList(new ArrayList<COA>());
        coaOrder.setQuantity(Integer.parseInt(countUnitFields.getText()));
        coaOrder.setFinished(false);
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(coaOrder);
        session.getTransaction().commit();
        session.close();
        closeStage(event);
    }

    @FXML
    private void mouseDragged(MouseEvent event)
    {
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    private void mousePressed(MouseEvent event)
    {
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    /**
     * This method was autogenerated by SceneBuilder
     */
    @FXML   // This method is called by the FXMLLoader when initialization is complete
    private void initialize() {
        assert gridP != null : "fx:id=\"gridP\" was not injected: check your FXML file 'CreateOrderView.fxml'.";
        assert orderNumberTextField != null : "fx:id=\"classTextField1\" was not injected: check your FXML file 'CreateOrderView.fxml'.";
        assert countUnitFields != null : "fx:id=\"countUnitFields\" was not injected: check your FXML file 'CreateOrderView.fxml'.";
        assert submitButton != null : "fx:id=\"submitButton\" was not injected: check your FXML file 'CreateOrderView.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'CreateOrderView.fxml'.";
        assert datePicker != null : "fx:id=\"datePicker\" was not injected: check your FXML file 'CreateOrderView.fxml'.";
        submitButton.setDisable(true);
    }

    /**
     * @param _tf Numeric TextField object that needs to be at least 7 digits long
     */
    private void setupOrderTextField(TextField _tf)
    {
        _tf.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(!newValue.matches("\\d{0,10}"))
            {
                _tf.setText(oldValue);
                submitButton.setDisable(true);
            }
            else
            {
                if(newValue.length() > 7)
                    submitButton.setDisable(false);
            }
        });
    }

    /**
     * @param _tf TextFieldObject that needs to be numeric up to 3 digits
     */
    private void setupNumberOfUnitsTextField(TextField _tf)
    {
        _tf.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(!newValue.matches("\\d{0,3}"))
            {
                _tf.setText(oldValue);
                submitButton.setDisable(true);
            }
            else
                submitButton.setDisable(false);
        });
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
        initialize();
        datePicker.setValue(LocalDate.now());
        setupOrderTextField(orderNumberTextField);
        setupNumberOfUnitsTextField(countUnitFields);
    }

    void setStage(Stage stage)
    {
        this.stage = stage;
        Functions.setUpIcons(stage);
    }
}
