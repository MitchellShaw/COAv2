package Controller;

import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import Model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by Ramon Johnson
 * 2017-08-18.
 */
public class AssignCOAViewController implements Initializable
{
    /**
     * Stage object so it can be closed
     */
    private Stage stage;

    /**
     * SessionFactory object so this window can update the database
     */
    private SessionFactory sessionFactory;


    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // fx:id="coaSerialLabel"
    private Label coaSerialLabel; // Value injected by FXMLLoader

    @FXML // fx:id="serialNumberLabel"
    private Label serialNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleNumberLabel"
    private Label scheduleNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="operatorLabel"
    private Label operatorLabel; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberLabel"
    private Label orderNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="productIDLabel"
    private Label productIDLabel; // Value injected by FXMLLoader

    @FXML // fx:id="osLabel"
    private Label osLabel; // Value injected by FXMLLoader

    @FXML // fx:id="gridP"
    private GridPane gridP; // Value injected by FXMLLoader

    @FXML // fx:id="coaSerialTextField"
    private TextField coaSerialTextField; // Value injected by FXMLLoader

    @FXML // fx:id="serialNumberTextField"
    private TextField serialNumberTextField; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleNumberTextField"
    private TextField scheduleNumberTextField; // Value injected by FXMLLoader

    @FXML // fx:id="operatorTextField"
    private TextField operatorTextField; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberTextField"
    private TextField orderNumberTextField; // Value injected by FXMLLoader

    @FXML // fx:id="productIDTextField"
    private TextField productIDTextField; // Value injected by FXMLLoader

    @FXML // fx:id="osTextField"
    private TextField osTextField; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    /**
     * @param _factory SessionFactory object to create Queries and Update database.
     */
    AssignCOAViewController(SessionFactory _factory)
    {
        sessionFactory = _factory;
    }

    /**
     * @param event Event to clear the text fields except the Operator field
     */
    @FXML
    void clearField(ActionEvent event) {
        Platform.runLater(() ->
        {
            coaSerialTextField.setText("");
            serialNumberTextField.setText("");
            scheduleNumberTextField.setText("");
            orderNumberTextField.setText("");
            osTextField.setText("");
        });

    }

    /**
     * @param event Event to close the Stage object
     */
    @FXML
    void closeStage(ActionEvent event) {
        stage.close();
    }

    /**
     * @param event Event to send Information to the database
     */
    @FXML
    @SuppressWarnings("Duplicates")
    void sendToDatabase(ActionEvent event)
    {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from Operator WHERE operator = :op").setParameter("op", operatorTextField.getText());
        List<Operator> operators = query.list();
        Unit unit = new Unit();
        unit.setProductID(productIDTextField.getText());
        unit.setScheduleNumber(Integer.parseInt(scheduleNumberTextField.getText()));
        unit.setSerialNumber(serialNumberTextField.getText());
        if(operators.size() < 1 )
        {
            //--- Operator doesn't exist, tell Operator they need to create account --//
            session.close();
            new Alert(Alert.AlertType.ERROR, "Operator doesn't exist in the database.\nYou need to create an account before use", ButtonType.CLOSE).showAndWait();
        }
        else
        {
            //--- Get the Operator Object then get the COAOrder object ----//
            Operator operator = operators.get(0);
            query = session.createQuery("from COAOrder WHERE orderNumber = :order").setParameter("order", Integer.parseInt(orderNumberTextField.getText()));
            List<COAOrder> coaOrders = query.list();
            if(coaOrders.size() < 1)
            {
                //--- Operator doesn't exist, tell Operator they need to create account --//
                session.close();
                new Alert(Alert.AlertType.ERROR, "Operator doesn't exist, create Operator before use", ButtonType.CLOSE).showAndWait();
            }
            else
            {
                //--- Get the order and add COA to it and reset view ---//
                COAOrder order = coaOrders.get(0);
                COA coa = new COA();
                coa.setOperatingSystem(osTextField.getText());
                coa.setSerialNumber(coaSerialTextField.getText());
                coa.setOrder(order);
                coa.setUnitSerialNumber(serialNumberTextField.getText());
                coa.setUnit(unit);
                operator.addCOA(coa);
                order.addCOA(coa);
                unit.setCoa(coa);
                session.save(coa);
                session.save(order);
                session.save(operator);
                session.save(unit);
                session.getTransaction().commit();
                session.close();
                Platform.runLater(() ->
                {
                    osTextField.setText("");
                    coaSerialTextField.setText("");
                    serialNumberTextField.setText("");
                    scheduleNumberTextField.setText("");
                    orderNumberTextField.setText("");
                    new Alert(Alert.AlertType.INFORMATION, MessageFormat.format("COA: {1}\nSerial number: {2}\nSchedule number: {3}", coaSerialTextField.getText(), serialNumberTextField.getText(), scheduleNumberTextField.getText()),ButtonType.CLOSE).showAndWait();
                });
            }
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    private void initialize()
    {
        assert gridP != null : "fx:id=\"gridP\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert coaSerialLabel != null : "fx:id=\"coaSerialLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert productIDLabel != null : "fx:id=\"productIDLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert serialNumberLabel != null : "fx:id=\"serialNumberLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert scheduleNumberLabel != null : "fx:id=\"scheduleNumberLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert orderNumberLabel != null : "fx:id=\"orderNumberLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert operatorLabel != null : "fx:id=\"operatorLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert osLabel != null : "fx:id=\"osLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert coaSerialTextField != null : "fx:id=\"coaSerialTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert productIDTextField != null : "fx:id=\"productIDTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert serialNumberTextField != null : "fx:id=\"serialNumberTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert scheduleNumberTextField != null : "fx:id=\"scheduleNumberTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert orderNumberTextField != null : "fx:id=\"orderNumberTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert operatorTextField != null : "fx:id=\"operatorTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert osTextField != null : "fx:id=\"osTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert clearButton != null : "fx:id=\"clearButton\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert submitButton != null : "fx:id=\"submitButton\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
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
        Functions.setToolTip(coaSerialLabel, "This is the serial number on the COA");
        Functions.setToolTip(serialNumberLabel, "This is the serial number located on the unit");
        Functions.setToolTip(scheduleNumberLabel, "This is the schedule number that corresponds to the serial number");
        Functions.setToolTip(operatorLabel, "This is the operator performing the task of assigning COA's to units");
        Functions.setToolTip(productIDLabel, "This is Product Label located on the ticket");
        Functions.setToolTip(coaSerialTextField, "Enter the Windows serial number here");
        Functions.setToolTip(serialNumberTextField, "Enter the serial number from the unit here");
        Functions.setToolTip(scheduleNumberTextField, "Enter the schedule from the ticket here and it should corresponds to the serial number.");
        Functions.setToolTip(operatorTextField, "Enter your RQS ID to continue");
        Functions.setToolTip(productIDTextField, "Enter the Product ID of the unit");
        Functions.setToolTip(submitButton, "This will insert the data to the data");
        Functions.setToolTip(clearButton, "This will reset the fields on the window");
        Functions.setToolTip(exitButton, "This will close the window");
        Functions.scheduleNumberTextFieldTiedToButton(scheduleNumberTextField, submitButton);
        Functions.serialNumberTextFieldTiedToButton(serialNumberTextField, submitButton);
        submitButton.setDisable(true);
    }

    void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
}
