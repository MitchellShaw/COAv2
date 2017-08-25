package Controller;


import java.net.URL;
import java.util.ResourceBundle;

import Model.Order;
import Model.OrderProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author Ramon Johnson
 * @version 0.0.0.1
 * 8/25/2017
 */
public class OpenOrderViewController implements Initializable
{

    /**
     * SessionFactory object to create Session's to run queries
     */
    private SessionFactory sessionFactory;

    /**
     * Stage object to close the Stage when the exit window is closed
     */
    private Stage stage;

    private ObservableList<Order> orders = FXCollections.observableArrayList();

    // ---https://stackoverflow.com/questions/18618653/binding-hashmap-with-tableview-javafx ---//
    ObservableMap<Integer, Order> map = FXCollections.observableHashMap();

    public void setStage(Stage _stage)
    {
        stage = _stage;
    }

    public OpenOrderViewController(SessionFactory _factory)
    {
        sessionFactory = _factory;
    }


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="pane"
    private GridPane pane; // Value injected by FXMLLoader

    @FXML // fx:id="openOrder"
    private Label openOrder; // Value injected by FXMLLoader

    @FXML // fx:id="buttonBar"
    private ButtonBar buttonBar; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="editButton"
    private Button editButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    @FXML // fx:id="table"
    private TableView<OrderProperty> table; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberColumn"
    private TableColumn orderNumberColumn; // Value injected by FXMLLoader

    @FXML // fx:id="quantityColumn"
    private TableColumn quantityColumn; // Value injected by FXMLLoader

    @FXML // fx:id="quantityRemainingColumn"
    private TableColumn quantityRemainingColumn; // Value injected by FXMLLoader

    @FXML // fx:id="quantityRemainingColumn1"
    private TableColumn ssdColumn; // Value injected by FXMLLoader

    @FXML
    void editData(ActionEvent event)
    {

    }

    @FXML
    void enterData(ActionEvent event)
    {

    }

    @FXML
    void exitView(ActionEvent event)
    {
        stage.close();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize()
    {
        assert pane != null : "fx:id=\"pane\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert openOrder != null : "fx:id=\"openOrder\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert buttonBar != null : "fx:id=\"buttonBar\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert editButton != null : "fx:id=\"editButton\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert submitButton != null : "fx:id=\"submitButton\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert orderNumberColumn != null : "fx:id=\"orderNumberColumn\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert quantityColumn != null : "fx:id=\"quantityColumn\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert quantityRemainingColumn != null : "fx:id=\"quantityRemainingColumn\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert ssdColumn != null : "fx:id=\"ssdColumn\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
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
        setUpTableColumns();
        setUpCallBack();
    }

    private void setUpTableColumns()
    {
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<OrderProperty, String>("orderNumber"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<OrderProperty, String>("quantity"));
        quantityRemainingColumn.setCellValueFactory(new PropertyValueFactory<OrderProperty, String>("quantityRemaining"));
        ssdColumn.setCellValueFactory(new PropertyValueFactory<OrderProperty, String>("scheduleShipDate"));
        //--- Testing purposes, trying to see what the graph would look like ---//
        /*OrderProperty one = new OrderProperty();
        one.setOrderNumber(String.valueOf(12345678));
        one.setQuantity("5");
        one.setQuantityRemaining("3");
        one.setScheduleShipDate("2017/08/25");

        OrderProperty two = new OrderProperty();
        two.setOrderNumber(String.valueOf(23456789));
        two.setQuantity("10");
        two.setQuantityRemaining("7");
        two.setScheduleShipDate("2017/08/26");

        OrderProperty three = new OrderProperty();
        three.setOrderNumber(String.valueOf(47568456));
        three.setQuantity("47");
        three.setQuantityRemaining("25");
        three.setScheduleShipDate("2017/08/29");
        ObservableList<OrderProperty> orders = FXCollections.observableArrayList();
        orders.addAll(one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three,one, two, three,one ,two ,three);
        table.setItems(orders);*/

    }

    private void setUpCallBack()
    {

    }

    private class BackgroundService extends ScheduledService<Void>
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
        protected Task<Void> createTask()
        {
            return new Task<Void>()
            {
                @Override
                protected Void call() throws Exception
                {
                    Session session = sessionFactory.openSession();
                    session.getTransaction().begin();
                    Query query = session.createQuery("from Order");
                    ObservableList<Order> orderObservableList = (ObservableList<Order>) query.list();
                    for(Order order : orderObservableList)
                    {

                    }
                    return null;
                }
            };
        }
    }
}
