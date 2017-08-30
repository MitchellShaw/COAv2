package Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by Ramon Johnson
 * 2017-08-18.
 * AssignCOAViewController should allow the operator to select the order number and based on that order number,
 * fill the choice box full of serial numbers for the COAs based on the order number. This list should decrement everytime
 * the submit button is pressed.
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

    /**
     * Order Object to be used to set information in the view
     */
    private Order order;

    /**
     * Operator object to be used to set the correction information in the database
     */
    private Operator operator;

    private COA coa;

    /**
     * Offset location for X
     */
    private static double xOffset = 0;

    /**
     * Offset location for Y
     */
    private static double yOffset = 0;


    @FXML // fx:id="gridP"
    private GridPane gridP; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberLabel"
    private Label orderNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="coaSerialLabel"
    private Label coaSerialLabel; // Value injected by FXMLLoader

    @FXML // fx:id="serialNumberLabel"
    private Label serialNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="productIDLabel"
    private Label productIDLabel; // Value injected by FXMLLoader

    @FXML // fx:id="modelNumberLabel"
    private Label modelNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleNumberLabel"
    private Label scheduleNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="operatorLabel"
    private Label operatorLabel; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberChoiceBox"
    private ChoiceBox<String> orderNumberChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="coaSerialChoiceBox"
    private ChoiceBox<String> coaSerialChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="productIDTextField"
    private TextField productIDTextField; // Value injected by FXMLLoader

    @FXML // fx:id="serialNumberTextField"
    private TextField serialNumberTextField; // Value injected by FXMLLoader

    @FXML // fx:id="modelNumberTextField"
    private TextField modelNumberTextField; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleNumberTextField"
    private TextField scheduleNumberTextField; // Value injected by FXMLLoader

    @FXML // fx:id="operatorTextField"
    private TextField operatorTextField; // Value injected by FXMLLoader

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
     * @param event Event to clear the text fields except the Operator field
     */
    @FXML
    void clearField(ActionEvent event) {
        Platform.runLater(() ->
        {
            serialNumberTextField.setText("");
            scheduleNumberTextField.setText("");
            coaSerialChoiceBox.setValue("");
            serialNumberTextField.setText("");
            productIDTextField.setText("");
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
    void sendToDatabase(ActionEvent event)
    {
        //--- Search for operator ---//
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        if(checkForOperator(session) && checkTextFieldsFormat(event))
        {
            session.clear();
            //--- Create unit from the information for the text fields ---//
            Unit unit = new Unit();
            unit.setSerialNumber(serialNumberTextField.getText());
            unit.setProductID(productIDTextField.getText());
            unit.setScheduleNumber(Integer.parseInt(scheduleNumberTextField.getText()));
            Order selectedOrder = getOrderFromChoiceBoxSelection(session);
            unit.setOrder(selectedOrder);
            unit.setModelNumber(modelNumberTextField.getText());

            //--- Get the COA object from the ChoiceBox ---//
            COA coa = getCOAFromSerialNumber(coaSerialChoiceBox.getValue());
            coa.setOperatorID(operator);
            coa.setUnit(unit);
            coa.setAssigned(true);

            //--- Once COA object created, set the COA for the unit object ---//
            unit.setCoa(coa);

            session.getTransaction().begin();
            session.update(coa);
            session.save(unit);
            session.update(selectedOrder);
            session.getTransaction().commit();
            session.close();
            new Alert(Alert.AlertType.INFORMATION, "Successfully assigned COA to unit", ButtonType.CLOSE).showAndWait();
            serialNumberTextField.setText("");
            scheduleNumberTextField.setText("");
            coaSerialChoiceBox.getItems().remove(coaSerialChoiceBox.getValue());

        }
        else
            session.close();
    }

    private COA getCOAFromSerialNumber(String value)
    {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM COA WHERE serialNumber = :serial");
        query.setParameter("serial", value);
        List<COA> coas = query.list();
        COA temp = null;
        for(COA coa : coas)
        {
            temp = coa;
        }
        session.close();
        return temp;
    }

    /**
     * @param _session Session object to be used to run queries
     * @return Returns Order object
     */
    private Order getOrderFromChoiceBoxSelection(Session _session)
    {
        _session.getTransaction().begin();
        Query query = _session.createQuery("from Order WHERE orderNumber = :orderNum").setParameter("orderNum", Integer.parseInt(orderNumberChoiceBox.getValue()));
        List<Order> orders = query.list();
        if(orders.size() > 0)
        {
            order = orders.get(0);
            _session.clear();
            return orders.get(0);
        }
        else
        {
            _session.clear();
            return null;
        }
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    private void initialize()
    {
        assert gridP != null : "fx:id=\"gridP\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert serialNumberLabel != null : "fx:id=\"serialNumberLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert productIDLabel != null : "fx:id=\"productIDLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert modelNumberLabel != null : "fx:id=\"modelNumberLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert scheduleNumberLabel != null : "fx:id=\"scheduleNumberLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert orderNumberLabel != null : "fx:id=\"orderNumberLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert operatorLabel != null : "fx:id=\"operatorLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert coaSerialLabel != null : "fx:id=\"coaSerialLabel\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert serialNumberTextField != null : "fx:id=\"serialNumberTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert productIDTextField != null : "fx:id=\"productIDTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert modelNumberTextField != null : "fx:id=\"modelNumberTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert scheduleNumberTextField != null : "fx:id=\"scheduleNumberTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert orderNumberChoiceBox != null : "fx:id=\"orderNumberChoiceBox\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
        assert operatorTextField != null : "fx:id=\"operatorTextField\" was not injected: check your FXML file 'AssignCOAView.fxml'.";
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
        loadChoiceBoxWithOrders();
        Functions.setToolTip(coaSerialLabel, "This is the serial number on the COA");
        Functions.setToolTip(serialNumberLabel, "This is the serial number located on the unit");
        Functions.setToolTip(scheduleNumberLabel, "This is the schedule number that corresponds to the serial number");
        Functions.setToolTip(operatorLabel, "This is the operator performing the task of assigning COA's to units");
        Functions.setToolTip(productIDLabel, "This is Product Label located on the ticket");
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
        orderNumberChoiceBox.addEventFilter(ActionEvent.ACTION, event ->
        {
            ObservableList<String> temp = FXCollections.observableArrayList();

            Session session = sessionFactory.openSession();
            session.getTransaction().begin();
            Query query = session.createQuery("FROM COA where order.orderNumber = :orderNum AND assigned = false").setParameter("orderNum", Integer.parseInt(orderNumberChoiceBox.getValue()));
            List<COA> coas = query.list();
            for(COA coa : coas)
            {
                temp.add(coa.getSerialNumber());
            }
            session.close();
            coaSerialChoiceBox.setItems(temp);
        });

        orderNumberChoiceBox.addEventFilter(ActionEvent.ACTION, event ->
        {
            //--- Get the count, should be more than 0, if not display a message saying no one has created the COA's for this order ---//
        });

    }

    /**
     * This method finds all of the orders and loads them up in the choiceBox
     */
    private void loadChoiceBoxWithOrders()
    {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from Order WHERE isFinished = false");
        List<Order> orders = query.list();
        if(orders.size() > 0) {
            ObservableList<String> list = FXCollections.observableArrayList();
            for (Order o : orders) {
                list.add(String.valueOf(o.getOrderNumber()));
            }
            session.close();
            orderNumberChoiceBox.setItems(list);
        }
        else
        {
            new Alert(Alert.AlertType.ERROR, "No open Orders exists, tell your Leadership to create it",ButtonType.CLOSE).showAndWait();
            session.close();
            stage.close();
        }
    }

    /**
     * @param _session Session object used for Queries
     * @return Returns true if the operator exist
     */
    private boolean checkForOperator(Session _session)
    {
        Query query = _session.createQuery("from Operator WHERE operator = :op").setParameter("op", operatorTextField.getText());
        List<Operator> operators = query.list();
        if(operators.size() > 0)
        {
            operator = operators.get(0);
            return true;
        }
        return false;
    }

    /**
     * @param _message String variable of the message to be displayed in the AlertBox
     * @param _event Event to be used to consume the event if need be
     */
    private void displayErrorMessage(String _message, ActionEvent _event)
    {
        new Alert(Alert.AlertType.ERROR, _message, ButtonType.CLOSE).showAndWait();
        _event.consume();
    }

    /**
     * @param _event ActionEvent to be used to consume the event if needed
     * @return Returns true if all the formats of the text files are correct
     */
    private boolean checkTextFieldsFormat(ActionEvent _event)
    {
        if(serialNumberTextField.getText().matches("\\d{2}-\\d{8}") || serialNumberTextField.getText().matches("\\d{8}"))
            if(productIDTextField.getText().matches("\\d{4}MC\\d{1,6}"))
                if(modelNumberTextField.getText().matches("\\d{4}-\\d{4}-\\d{4}"))
                    if(scheduleNumberTextField.getText().matches("\\d{7,8}"))
                        if(!orderNumberChoiceBox.getValue().equalsIgnoreCase(""))
                            if (operatorTextField.getText().length() > 2)
                                return coaSerialChoiceBox.getValue().length() > 6 || consumeEventWithMessage("The COA\'s Serial number is to short", _event);
                            else
                                return consumeEventWithMessage("Operator length is to short", _event);
                        else
                            return consumeEventWithMessage("Order number is not in the correct format", _event);
                    else
                        return consumeEventWithMessage("Schedule number is not in the correct format", _event);
                else
                    return consumeEventWithMessage("Model number is not in the correct format", _event);
            else
                return consumeEventWithMessage("Product ID is not in the correct format", _event);
        else
            return consumeEventWithMessage("Serial number isn't in the correct format", _event);


    }

    /**
     * @param _message Message to be sent to AlertBox.
     * @param _event Event to be consumed when the node is found
     * @return Returns true if the execution executed
     */
    private boolean consumeEventWithMessage(String _message, ActionEvent _event)
    {
        displayErrorMessage(_message, _event);
        return false;
    }

    /**
     * Setter for property 'stage'.
     *
     * @param stage Value to set for property 'stage'.
     */
    void setStage(Stage stage)
    {
        this.stage = stage;
    }

    /**
     * Setter for property 'sessionFactory'.
     *
     * @param sessionFactory Value to set for property 'sessionFactory'.
     */
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }


}
