package Controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import Model.COA;
import Model.Operator;
import Model.Order;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author Ramon Johnson
 * 2017-08-28
 * @version 1.0.0.1
 */
public class CreateCOAToOrderViewController implements Initializable
{
    private SessionFactory sessionFactory;

    private Stage stage;

    private TreeMap<String, String> partNumbers = new TreeMap<>();

    ObservableList<Operator> operatorObservableList = FXCollections.observableArrayList();

    CreateCOAToOrderViewController(SessionFactory _factory)
    {
        sessionFactory = _factory;
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="messageLabel"
    private Label messageLabel; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberTextField"
    private ChoiceBox<String> orderNumberChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="partNumberChoiceBox"
    private ChoiceBox<String> partNumberChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="operatorTextField"
    private TextField operatorTextField; // Value injected by FXMLLoader

    @FXML // fx:id="mcTextField"
    private TextField mcTextField; // Value injected by FXMLLoader

    @FXML // fx:id="coaTextField"
    private TextField coaTextField; // Value injected by FXMLLoader

    @FXML // fx:id="quantity"
    private TextField quantity; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    /* For later use!!!
        cancelButton.disableProperty().bind(service.stateProperty().isNotEqualTo(RUNNING));
        resetButton.disableProperty().bind(Bindings.or(service.stateProperty().isEqualTo(RUNNING),service.stateProperty().isEqualTo(SCHEDULED)));
        https://gist.github.com/james-d/9904574
     */

    public void bindings()
    {
        operatorObservableList.add(getOperator(operatorTextField.getText()));
        submitButton.disableProperty().bind(coaTextField.textProperty().isEmpty().or(partNumberChoiceBox.valueProperty().isNull()).or(operatorTextField.textProperty().isEmpty()).or(quantity.textProperty().isEmpty()));
        BooleanBinding isOperator = Bindings.createBooleanBinding(this::isOperator, operatorObservableList);
    }


    @FXML
    void addCoaToDatabase(ActionEvent event) {
        //--- I know the order exists because I pull the information before hand ---//
        if(orderNumberChoiceBox.getValue().length() != 0 &&
                partNumberChoiceBox.getValue().length() != 0)
        {
            if(isOperator())
            {
                //--- Get the format of all COA's to set a standard ---//
                //--- if(coaSerialTextField is not in the correct format say so here )
                //---   {}
                addCOA();
                coaTextField.setText("");
            }
            else
            {
                //--- Explain operator doesn't exists ---//
                new Alert(Alert.AlertType.ERROR, "Operator " + operatorTextField.getText() + " doesn't exist in the database", ButtonType.CLOSE).showAndWait();
                event.consume();
            }
        }
        else
        {
            //--- Display message saying that these 2 can't be empty ---//
            new Alert(Alert.AlertType.ERROR,"Either there isn't an order selected or part number selected or both. Please select a value for a both", ButtonType.CLOSE).showAndWait();
            event.consume();
        }
        //--- I know the part number exists because I prepared that information in a text file ---//
        //--- Operator, I don't want a list of all of them but I can check if the operatorID is found! ---//


        //--- Don't reset everything, chances are everything is the same but the serial ---//

    }

    /**
     * This method executes the necessary steps to get the COA and add it to database
     */
    private void addCOA()
    {
        Order order = getOrder(orderNumberChoiceBox.getValue());
        assert order != null;

        Operator operator = getOperator(operatorTextField.getText());
        assert operator != null;

        COA coa = new COA();
        coa.setOperatorID(operator);
        coa.setSerialNumber(coaTextField.getText());
        coa.setPartNumber(partNumberChoiceBox.getValue());
        coa.setOrder(order);
        coa.setCreateTime(LocalTime.now());
        coa.setCreatedDate(LocalDate.now());

        //--- Add COA's to these objects list ---//
        order.addCOA(coa);
        operator.addCOA(coa);

        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(coa);
        session.update(order);
        session.update(operator);
        try
        {
            session.getTransaction().commit();
        }
        catch(Exception e)
        {
            new Alert(Alert.AlertType.ERROR, "Check serial number, it may have already been created\n" + e.getMessage() , ButtonType.CLOSE).showAndWait();
        }
        session.close();
    }

    private Order getOrder(String _orderNumber)
    {
        Order order = null;
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM Order WHERE orderNumber = " + Integer.parseInt(_orderNumber));
        List<Order> orders = query.list();
        for(Order o: orders)
            order = o;
        session.close();
        return order;
    }

    private Operator getOperator(String _operator)
    {
        Operator operator = null;
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM Operator WHERE operator = :op").setParameter("op", _operator);
        List<Operator> operators = query.list();
        for (Operator operator1 : operators)
            operator = operator1;
        session.close();
        return operator;
    }

    @FXML
    void clearFields(ActionEvent event)
    {
        orderNumberChoiceBox.setValue(" ");
        partNumberChoiceBox.setValue(" ");
        operatorTextField.setText("");
        coaTextField.setText("");
    }

    @FXML
    void exitStage(ActionEvent event) {
        stage.close();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws Exception
    {
        assert orderNumberChoiceBox != null : "fx:id=\"orderNumberTextField\" was not injected: check your FXML file 'CreateCOAToOrderView.fxml'.";
        assert partNumberChoiceBox != null : "fx:id=\"partNumberChoiceBox\" was not injected: check your FXML file 'CreateCOAToOrderView.fxml'.";
        assert operatorTextField != null : "fx:id=\"operatorTextField\" was not injected: check your FXML file 'CreateCOAToOrderView.fxml'.";
        assert mcTextField != null : "fx:id=\"mcTextField\" was not injected: check your FXML file 'CreateCOAToOrderView.fxml'.";
        assert coaTextField != null : "fx:id=\"coaTextField\" was not injected: check your FXML file 'CreateCOAToOrderView.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'CreateCOAToOrderView.fxml'.";
        assert clearButton != null : "fx:id=\"clearButton\" was not injected: check your FXML file 'CreateCOAToOrderView.fxml'.";
        assert submitButton != null : "fx:id=\"submitButton\" was not injected: check your FXML file 'CreateCOAToOrderView.fxml'.";
        assert messageLabel != null : "fx:id=\"messageLabel\" was not injected: check your FXML file 'CreateCOAToOrderView.fxml'.";
    }

    void setStage(Stage _stage)
    {
        stage = _stage;
    }

    private boolean isOperator()
    {
        boolean result = false;
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM Operator WHERE operator = :op").setParameter("op", operatorTextField.getText());
        if(query.list().size() > 0)
        {
            session.close();
            return true;
        }
        session.close();
        return result;
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
        this.location = location;
        try
        {
            partNumberChoiceBox.setItems(new GetPartNumbers().call());
        } catch(Exception e)
        {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE).showAndWait();
        }

        GetCountFromOrder getCountFromOrder = new GetCountFromOrder();
        getCountFromOrder.setPeriod(Duration.seconds(1));
        getCountFromOrder.setRestartOnFailure(true);

        ObservableList<String> orderItems = null;
        try
        {
            orderItems = new CheckForOrders().call();
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        orderNumberChoiceBox.addEventFilter(ActionEvent.ACTION, event ->
        {
            if(orderNumberChoiceBox.getValue().length() < 1 || orderNumberChoiceBox.getValue() == null)
                getCountFromOrder.cancel();
            else
            {
                if(!getCountFromOrder.isRunning())
                    getCountFromOrder.start();
            }
        });

        messageLabel.textProperty().bind(getCountFromOrder.lastValueProperty());

        operatorTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            operatorTextField.setText(newValue.toLowerCase());
        });

        if(orderItems == null)
        {
            //--- Stop the program here and explain there are no orders to assign COA's too --//
            new Alert(Alert.AlertType.ERROR, "There are no open orders in the system.\nHave someone create the order so you can use this form", ButtonType.CLOSE).showAndWait();
            stage.close();
        }
        else
            orderNumberChoiceBox.setItems(orderItems);
    }

    private class CheckForOrders extends Task<ObservableList<String>>
    {

        /**
         * Invoked when the Task is executed, the call method must be overridden and
         * implemented by subclasses. The call method actually performs the
         * background thread logic. Only the updateProgress, updateMessage, updateValue and
         * updateTitle methods of Task may be called from code within this method.
         * Any other interaction with the Task from the background thread will result
         * in runtime exceptions.
         *
         * @return The result of the background work, if any.
         * @throws Exception an unhandled exception which occurred during the
         *                   background operation
         */
        @Override
        @SuppressWarnings("Duplicates")
        protected ObservableList<String> call() throws Exception
        {
            Session session = sessionFactory.openSession();
            session.getTransaction().begin();
            Query query = session.createQuery("FROM Order WHERE isFinished = false");
            List<Order> orderList = query.list();
            ObservableList<String> orders = FXCollections.observableArrayList();
            for(Order order : orderList)
            {
                orders.add(String.valueOf(order.getOrderNumber()));
                //--- System.out.println(MessageFormat.format("Order: {0}",order.getOrderNumber())); --/
            }
            session.close();
            if(orders.size() > 0)
                return orders;
            else
                return null;
        }
    }

    private class GetCountFromOrder extends ScheduledService<String>
    {
        @Override
        protected Task<String> createTask()
        {
            return new Task<String>()
            {
                @Override
                protected String call() throws Exception
                {
                    if(isCancelled())
                    {
                        //updateMessage("Error retrieving information with selected order");
                        //updateValue("Error retrieving information with selected order");
                        return "Error retrieving information with selected order";
                    }
                    else
                    {
                        Session session = sessionFactory.openSession();
                        session.getTransaction().begin();
                        Query query = session.createQuery("SELECT COUNT(*) FROM COA WHERE order.orderNumber = :orderChoice");
                        query.setString("orderChoice", orderNumberChoiceBox.getValue());
                        Long count = (Long) query.uniqueResult();

                        query = session.createQuery("FROM Order WHERE orderNumber = :orderNum");
                        query.setString("orderNum", orderNumberChoiceBox.getValue());
                        List<Order> orderList = query.list();
                        Order temp = null;
                        for (Order order : orderList)
                            temp = order;

                        session.close();
                        assert temp != null;
                        //updateValue(String.format("%s/%d COA's Assigned", count.toString(), temp.getQuantity()));
                        //updateMessage(String.format("%s/%d COA's Assigned", count.toString(), temp.getQuantity()));
                        if(count.toString().equalsIgnoreCase(String.valueOf(temp.getQuantity())))
                            submitButton.setDisable(true);
                        else
                            submitButton.setDisable(false);
                        return String.format("%s/%d COA's Assigned", count.toString(), temp.getQuantity());
                    }
                }
            };
        }
    }

    private class GetPartNumbers extends Task<ObservableList<String>>
    {

        /**
         * Invoked when the Task is executed, the call method must be overridden and
         * implemented by subclasses. The call method actually performs the
         * background thread logic. Only the updateProgress, updateMessage, updateValue and
         * updateTitle methods of Task may be called from code within this method.
         * Any other interaction with the Task from the background thread will result
         * in runtime exceptions.
         *
         * @return The result of the background work, if any.
         * @throws Exception an unhandled exception which occurred during the
         *                   background operation
         */
        @Override
        protected ObservableList<String> call() throws Exception
        {
            File file = new File("\\\\153.61.177.74\\d\\Programs\\COA\\Types of COAs.xml");
            ObservableList<String> list = FXCollections.observableArrayList();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            NodeList nodeList = document.getElementsByTagName("model");
            for(int x = 0, size = nodeList.getLength(); x < size; x++)
            {
                String partNumber = nodeList.item(x).getAttributes().getNamedItem("number").getNodeValue();
                String description = nodeList.item(x).getAttributes().getNamedItem("description").getNodeValue();
                list.add(partNumber);
                partNumbers.put(partNumber, description);
            }
            return list;
        }
    }
}

