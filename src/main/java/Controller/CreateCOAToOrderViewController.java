package Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

import Model.COA;
import Model.Operator;
import Model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by Ramon Johnson
 * 2017-08-28
 * @version 0.0.0.1
 */
public class CreateCOAToOrderViewController
{
    private SessionFactory sessionFactory;

    private Stage stage;

    public CreateCOAToOrderViewController(SessionFactory _factory)
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

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    /* For later use!!!
        cancelButton.disableProperty().bind(service.stateProperty().isNotEqualTo(RUNNING));
        resetButton.disableProperty().bind(Bindings.or(service.stateProperty().isEqualTo(RUNNING),service.stateProperty().isEqualTo(SCHEDULED)));
     */

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

        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(coa);
        session.getTransaction().commit();
        session.close();
    }

    private Order getOrder(String _orderNumber)
    {
        Order order = null;
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM Order WHERE orderNumber = :ord").setParameter("ord", _orderNumber);
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
        orderNumberChoiceBox.setValue("");
        partNumberChoiceBox.setValue("");
        operatorTextField.setText("");
        coaTextField.setText("");
    }

    @FXML
    void exitStage(ActionEvent event) {
        stage.close();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize()
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

        GetCountFromOrder getCountFromOrder = new GetCountFromOrder();
        getCountFromOrder.setPeriod(Duration.seconds(1));
        getCountFromOrder.setRestartOnFailure(true);

        GetOrders getOrders = new GetOrders();
        ObservableList<String> orderItems = getOrders.createTask().getValue();
        if(orderItems == null)
        {
            //--- Stop the program here and explain there are no orders to assign COA's too --//
            new Alert(Alert.AlertType.ERROR, "There are no open orders in the system.\nHave someone create the order so you can use this form", ButtonType.CLOSE).showAndWait();
            stage.close();
        }
        else
            orderNumberChoiceBox.setItems(orderItems);

        orderNumberChoiceBox.addEventFilter(ActionEvent.ACTION, event ->
        {
            if(orderNumberChoiceBox.getValue().length() < 1 || orderNumberChoiceBox.getValue() == null)
                getCountFromOrder.cancel();
            else
                getCountFromOrder.start();
        });

        messageLabel.textProperty().bind(getCountFromOrder.lastValueProperty());

        operatorTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            operatorTextField.setText(newValue.toLowerCase());
        });
    }

    public void setStage(Stage _stage)
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

    private class GetCountFromOrder extends ScheduledService<String>
    {

        /**
         * Invoked after the Service is started on the JavaFX Application Thread.
         * Implementations should save off any state into final variables prior to
         * creating the Task, since accessing properties defined on the Service
         * within the background thread code of the Task will result in exceptions.
         * <p>
         * For example:
         * <pre><code>
         *     protected Task createTask() {
         *         final String url = myService.getUrl();
         *         return new Task&lt;String&gt;() {
         *             protected String call() {
         *                 URL u = new URL("http://www.oracle.com");
         *                 BufferedReader in = new BufferedReader(
         *                         new InputStreamReader(u.openStream()));
         *                 String result = in.readLine();
         *                 in.close();
         *                 return result;
         *             }
         *         }
         *     }
         * </code></pre>
         * <p>
         * <p>
         * If the Task is a pre-defined class (as opposed to being an
         * anonymous class), and if it followed the recommended best-practice,
         * then there is no need to save off state prior to constructing
         * the Task since its state is completely provided in its constructor.
         * </p>
         * <p>
         * <pre><code>
         *     protected Task createTask() {
         *         // This is safe because getUrl is called on the FX Application
         *         // Thread and the FirstLineReaderTasks stores it as an
         *         // immutable property
         *         return new FirstLineReaderTask(myService.getUrl());
         *     }
         * </code></pre>
         *
         * @return the Task to execute
         */
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
                        return String.format("%s/%d COA's Assigned", count.toString(), temp.getQuantity());
                    }
                }
            };
        }
    }

    private class GetOrders extends Service<ObservableList<String>>
    {
        /**
         * Invoked after the Service is started on the JavaFX Application Thread.
         * Implementations should save off any state into final variables prior to
         * creating the Task, since accessing properties defined on the Service
         * within the background thread code of the Task will result in exceptions.
         * <p>
         * For example:
         * <pre><code>
         *     protected Task createTask() {
         *         final String url = myService.getUrl();
         *         return new Task&lt;String&gt;() {
         *             protected String call() {
         *                 URL u = new URL("http://www.oracle.com");
         *                 BufferedReader in = new BufferedReader(
         *                         new InputStreamReader(u.openStream()));
         *                 String result = in.readLine();
         *                 in.close();
         *                 return result;
         *             }
         *         }
         *     }
         * </code></pre>
         * If the Task is a pre-defined class (as opposed to being an
         * anonymous class), and if it followed the recommended best-practice,
         * then there is no need to save off state prior to constructing
         * the Task since its state is completely provided in its constructor.
         * </p>
         * <pre><code>
         *     protected Task createTask() {
         *         // This is safe because getUrl is called on the FX Application
         *         // Thread and the FirstLineReaderTasks stores it as an
         *         // immutable property
         *         return new FirstLineReaderTask(myService.getUrl());
         *     }
         * </code></pre>
         *
         * @return the Task to execute
         */
        @Override
        protected Task<ObservableList<String>> createTask()
        {
            return new Task<ObservableList<String>>()
            {
                @Override
                protected ObservableList<String> call() throws Exception
                {
                    Session session = sessionFactory.openSession();
                    session.getTransaction().begin();
                    Query query = session.createQuery("FROM Order");
                    List<Order> orderList = query.list();
                    ObservableList<String> orders = FXCollections.observableArrayList();
                    for(Order order : orderList)
                        orders.add(String.valueOf(order.getOrderNumber()));

                    session.close();
                    if(orders.size() > 0)
                        return orders;
                    else
                        return null;
                }
            };
        }
    }
}

