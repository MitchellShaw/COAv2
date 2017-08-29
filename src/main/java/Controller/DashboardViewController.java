package Controller;


import java.io.IOException;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.*;

import Model.Functions;
import Model.Order;
import insidefx.undecorator.UndecoratorScene;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javassist.Loader;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author Ramon Johnson
 * @version 0.0.0.1
 * 8/25/2017
 */
public class DashboardViewController implements Initializable
{

    /**
     * SessionFactory object to create Session's to run queries
     */
    private SessionFactory sessionFactory;

    /**
     * Stage object to close the Stage when the exit window is closed
     */
    private Stage stage;

    /**
     * Observable Arraylist to hold the data to submit to the TableView
     */
    private ObservableList<Order> data = FXCollections.observableArrayList();

    /**
     * Setter for property 'stage'.
     *
     * @param _stage Value to set for property 'stage'.
     */
    public void setStage(Stage _stage)
    {
        stage = _stage;
    }

    public DashboardViewController(SessionFactory _factory)
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

    @FXML // fx:id="addOperatorButton"
    private Button addOperatorButton; // Value injected by FXMLLoader

    @FXML // fx:id="assignCOAButton"
    private Button assignCOAButton; // Value injected by FXMLLoader

    @FXML // fx:id="createCOAButton"
    private Button createCOAButton; // Value injected by FXMLLoader

    @FXML // fx:id="createOrderButton"
    private Button createOrderButton; // Value injected by FXMLLoader

    @FXML // fx:id="completeOrderButton"
    private Button completeOrderButton; // Value injected by FXMLLoader

    @FXML // fx:id="editOrderButton"
    private Button editOrderButton; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="table"
    private TableView<Order> table; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberColumn"
    private TableColumn<Order, String> orderNumberColumn; // Value injected by FXMLLoader

    @FXML // fx:id="quantityColumn"
    private TableColumn<Order, String> quantityColumn; // Value injected by FXMLLoader

    @FXML // fx:id="quantityRemainingColumn"
    private TableColumn<Order, String> quantityRemainingColumn; // Value injected by FXMLLoader

    @FXML // fx:id="quantityRemainingColumn1"
    private TableColumn<Order, String> ssdColumn; // Value injected by FXMLLoader

    @FXML
    void addOperator(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(new URL(getProductionPath() + "/production/resources/FXML's/AddOperatorView.fxml"));
        AddOperatorViewController addOperatorView = new AddOperatorViewController(sessionFactory);
        loader.setController(addOperatorView);
        GridPane pane = loader.load();
        Stage stage = new Stage(StageStyle.UNDECORATED);
        addOperatorView.setStage(stage);
        stage.setResizable(false);
        stage.setTitle("Create New Operator");
        stage.setScene(new Scene(pane));
        stage.initModality(Modality.APPLICATION_MODAL);
        Functions.setUpIcons(stage);
        stage.setOnCloseRequest(event1 ->
        {
            stage.close();
        });
        stage.show();
    }

    @FXML
    void assignCOA(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(new URL(getProductionPath() + "/resources/FXML's/AssignCOAView.fxml"));
        AssignCOAViewController assignCOAViewController = new AssignCOAViewController(sessionFactory);
        loader.setController(assignCOAViewController);
        GridPane pane = loader.load();
        Stage stage = new Stage(StageStyle.UNDECORATED);
        assignCOAViewController.setStage(stage);
        stage.setResizable(false);
        stage.setTitle("Assign COA Form");
        stage.setScene(new Scene(pane));
        stage.initModality(Modality.APPLICATION_MODAL);
        Functions.setUpIcons(stage);
        stage.setOnCloseRequest(event1 -> stage.close());
        stage.show();
    }

    @FXML
    void createOrder(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(new URL(getProductionPath() + "/resources/FXML's/CreateOrderView.fxml"));
        CreateOrderViewController createOrderViewController = new CreateOrderViewController(sessionFactory);
        loader.setController(createOrderViewController);
        GridPane pane = loader.load();
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("Create Order");
        stage.setScene(new Scene(pane));
        stage.initModality(Modality.APPLICATION_MODAL);
        Functions.setUpIcons(stage);
        createOrderViewController.setStage(stage);
        stage.setOnCloseRequest(event1 -> stage.close());
        stage.show();

    }

    static String getPathOfClass(Class _class)
    {
        String className = _class.getSimpleName();
        String testPath = String.valueOf(_class.getResource(className+".class"));
        String[] split = testPath.split("/");
        StringBuilder registrationViewPath = new StringBuilder("");
        for(int index = 0; index < split.length - 3; index++)
        {
            registrationViewPath.append(split[index]+"/");
            System.out.printf("%s\n", split[index]);
        }
        return registrationViewPath.toString();
    }

    @FXML
    void createCOA(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(new URL(getProductionPath() + "/resources/FXML's/CreateCOAToOrderView.fxml"));
        CreateCOAToOrderViewController createCOAToOrderViewController = new CreateCOAToOrderViewController(sessionFactory);
        loader.setController(createCOAToOrderViewController);
        Stage _stage = new Stage();
        createCOAToOrderViewController.setStage(_stage);
        GridPane pane = loader.load();
        Functions.setUpIcons(_stage);
        final UndecoratorScene undecoratorScene = new UndecoratorScene(_stage, pane);
        _stage.setScene(undecoratorScene);
        _stage.setResizable(false);
        _stage.setOnCloseRequest(event1 -> _stage.close());
        _stage.show();
    }

    /**
     * @param event ActionEvent passed down through JavaFX
     *              This method will check the quantity remaining, and if the number isn't 0 it will not set this
     *              order to complete
     */
    @FXML
    void completeOrder(ActionEvent event)
    {
        event.consume();
        Order temp = table.getSelectionModel().getSelectedItem();
        if(temp != null)
        {
            if((temp.getQuantity() - temp.getCompleted()) > 0)
            {
                new Alert(Alert.AlertType.ERROR, "There are still more COA's that need to be assigned.", ButtonType.CLOSE).showAndWait();
            }
            else
            {
                if((temp.getQuantity() - temp.getCompleted()) == 0)
                {
                    Session session = sessionFactory.openSession();
                    session.getTransaction().begin();
                    Query query = session.createQuery("from Order WHERE orderNumber = :_orderNumber").setParameter("_orderNumber", temp.getOrderNumber());;
                    List<Order> orders = query.list();
                    for(Order o : orders)
                    {
                        o.setFinished(true);
                        session.save(o);
                        session.getTransaction().commit();
                    }
                    session.close();
                }
            }
        }
    }

    /**
     * editOrder method allows a user to edit the quantity of units a order number needs
     * @param event ActionEvent passed down through JavaFX
     * @throws IOException If EditOrderView.fxml can't be found this exception would be thrown
     */
    @FXML
    void editOrder(ActionEvent event) throws IOException
    {
        event.consume();
        Order temp = table.getSelectionModel().getSelectedItem();
        if(temp != null)
        {
            Stage editStage = new Stage(StageStyle.UNDECORATED);
            editStage.initModality(Modality.APPLICATION_MODAL);
            String testPath = String.format("%s\n", getClass().getResource("DashboardViewController.class"));
            String[] path = testPath.split("classes");
            FXMLLoader loader = new FXMLLoader(new URL(path[0] + "resources/FXML's/EditOrderView.fxml"));
            EditOrderViewController editOrderViewController = new EditOrderViewController(sessionFactory, editStage, temp.getOrderNumber(), temp.getScheduledShipDate(), temp.getQuantity());
            loader.setController(editOrderViewController);
            GridPane gridPane = loader.load();
            Functions.setUpIcons(editStage);
            editStage.setOnCloseRequest(event1 -> editStage.close());
            editStage.setOnCloseRequest(event1 ->
            {
                editStage.close();
            });
            editStage.setScene(new Scene(gridPane));
            editStage.show();
        }
    }

    /**
     * exitView method exits out of the program entirely
     * @param event ActionEvent passed down through JavaFX
     */
    @FXML
    void exitView(ActionEvent event)
    {
        System.exit(0);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    private void initialize()
    {
        assert pane != null : "fx:id=\"pane\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert openOrder != null : "fx:id=\"openOrder\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert buttonBar != null : "fx:id=\"buttonBar\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert addOperatorButton != null : "fx:id=\"addOperatorButton\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert assignCOAButton != null : "fx:id=\"assignCOAButton\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert createCOAButton != null : "fx:id=\"createCOAButton\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert createOrderButton != null : "fx:id=\"createOrderButton\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert completeOrderButton != null : "fx:id=\"completeOrderButton\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert editOrderButton != null : "fx:id=\"editOrderButton\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'OpenOrdersView.fxml'.";
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
        table.setItems(data);
        BackgroundService backgroundService = new BackgroundService();
        backgroundService.setRestartOnFailure(true);
        backgroundService.setPeriod(Duration.millis(500));
        SetCompletedService setCompletedService = new SetCompletedService();
        setCompletedService.setRestartOnFailure(true);
        setCompletedService.setPeriod(Duration.seconds(2));
        backgroundService.start();
        setCompletedService.start();
    }

    /**
     * Method sets all the of columns in the tableView
     */
    private void setUpTableColumns()
    {
        orderNumberColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> param)
            {
                return new SimpleStringProperty(String.valueOf(param.getValue().getOrderNumber()));
            }
        });
        quantityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> param)
            {
                return new SimpleStringProperty(String.valueOf(param.getValue().getQuantity()));
            }
        });

        quantityRemainingColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> param)
            {
                return new SimpleStringProperty(String.valueOf(param.getValue().getQuantity() - param.getValue().getCompleted()));
            }
        });

        ssdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> param)
            {
                return new SimpleStringProperty(String.valueOf(param.getValue().getScheduledShipDate().toString()));
            }
        });
    }

    /**
     * SetCompletedService retrieves the count of COA's associated with an
     * order and sets the completed number for the Order object
     */
    private class SetCompletedService extends ScheduledService<Void>
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
        protected Task<Void> createTask()
        {
            return new Task<Void>()
            {
                @Override
                protected Void call() throws Exception
                {
                    Session session = sessionFactory.openSession();
                    session.getTransaction().begin();
                    if(data.size() > 0)
                    {
                        for(Order $order: data)
                        {
                            int orderNumber = $order.getOrderNumber();
                            Query query = session.createQuery("from COA WHERE order.orderNumber= :number").setParameter("number", orderNumber);
                            $order.setCompleted(query.list().size());
                        }
                    }
                    session.close();
                    return null;
                }
            };
        }
    }

    /**
     * BackgroundService class continuously checks the database for any changes
     * and makes the tableView refresh to display the data
     */
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
        protected Task<Void> createTask()
        {
            return new Task<Void>()
            {
                @Override
                protected Void call() throws Exception
                {
                    Session session = sessionFactory.openSession();
                    session.getTransaction().begin();
                    Query query = session.createQuery("from Order WHERE isFinished = false");
                    List<Order> orderList = query.list();
                    if(orderList.size() > data.size())
                        databaseGreaterThanMap(orderList, data);
                    else if(orderList.size() < data.size())
                        databaseLesserThanMap(orderList, data);
                    else if(orderList.size() == data.size())
                        databaseEqualToMap(orderList, data);
                    else
                    {
                        data = FXCollections.observableArrayList();
                    }
                    session.close();
                    table.refresh();
                    return null;
                }
            };
        }

        /**
         * @param _order List from the Query
         * @param _orders List saved that is set to the Program
         */
        private void databaseGreaterThanMap(List<Order> _order, ObservableList<Order> _orders)
        {
            //--- This method gets activated by the list from the Query being bigger than the data saved ---//
            Map<Integer, Order> $orderMap = orderMap(_order);
            Map<Integer, Order> $observableMap = orderMap(_orders);
            //-- Since _orderMap is greater, we can easily add to observable ---//
            for(Integer $order : $orderMap.keySet())
            {
                if($observableMap.containsKey($order))
                {
                    Order temp = $orderMap.get($order);
                    Order data = $observableMap.get($order);
                    data.setQuantity(temp.getQuantity());
                    data.setScheduledShipDate(temp.getScheduledShipDate());
                    //System.out.printf("********************************GREATER THAN**************************\n%s | %s\n",temp.toString(), data.toString() );
                }
                else
                {
                    _orders.add($orderMap.get($order));
                    //System.out.printf("********************************NEW********************************\n%s\n",$orderMap.get($order).toString() );
                }
            }
        }

        /**
         * @param _order List from the Query
         * @param _orders List saved that is set to the Program
         */
        private void databaseLesserThanMap(List<Order> _order, ObservableList<Order> _orders)
        {
            //--- This method is activated when the list from the Query is less than the data saved ---//
            Map<Integer, Order> $orderMap = orderMap(_order);
            Map<Integer, Order> $observableMap = orderMap(_orders);
            //--- We have to remove from observable List
            for (Integer $order : $orderMap.keySet())
            {
                if($observableMap.containsKey($order))
                {
                    Order temp = $orderMap.get($order);
                    Order data = $observableMap.get($order);
                    data.setQuantity(temp.getQuantity());
                    data.setScheduledShipDate(temp.getScheduledShipDate());
                    //System.out.printf("********************************LESSER THAN**************************\n%s | %s\n",temp.toString(), data.toString() );
                }
            }

            for(Integer $order : $observableMap.keySet())
            {
                if(!$orderMap.containsKey($order))
                {
                    _orders.remove($observableMap.get($order));
                }
            }
        }

        /**
         * @param _order List from the Query
         * @param _orders List saved that is set to the Program
         */
        private void databaseEqualToMap(List<Order> _order, ObservableList<Order> _orders)
        {
            //--- This method is activated if both list are the same, just send the details over to observable ---//
            Map<Integer, Order> $orderMap = orderMap(_order);
            Map<Integer, Order> $observableMap = orderMap(_orders);
            for(Integer $order: $observableMap.keySet())
            {
                //--- There is a possibility for null here, but it should be very very very hard to do so ---//
                Order temp = $orderMap.get($order);
                Order data = $observableMap.get($order);
                data.setQuantity(temp.getQuantity());
                data.setScheduledShipDate(temp.getScheduledShipDate());
                //System.out.printf("********************************EQUAL********************************\n%s | %s\n",temp.toString(), data.toString() );
            }
        }

        /**
         * @param _theList List from Query.list() method
         * @return Returns TreeMap object
         */
        private Map<Integer, Order>orderMap(List<Order> _theList)
        {
            Map<Integer, Order> _temp = new TreeMap<>();
            for(Order _order: _theList)
            {
                _temp.put(_order.getOrderNumber(), _order);
            }
            return _temp;
        }

        /**
         * @param _theList List from the variable data
         * @return Returns TreeMap object
         */
        private Map<Integer, Order>orderMap(ObservableList<Order> _theList)
        {
            Map<Integer, Order> _temp = new TreeMap<>();
            for(Order _order: _theList)
            {
                _temp.put(_order.getOrderNumber(), _order);
            }
            return _temp;
        }
    }

    /**
     * Getter for property 'productionPath'.
     *
     * @return Value for property 'productionPath'.
     */
    @SuppressWarnings("Duplicates")
    private String getProductionPath()
    {
        String className = this.getClass().getSimpleName();
        String testPath = String.valueOf(this.getClass().getResource(className+".class"));
        String[] split = testPath.split("/");
        StringBuilder registrationViewPath = new StringBuilder("");
        for(int index = 0; index < split.length - 3; index++)
        {
            registrationViewPath.append(split[index]+"/");
            //System.out.printf("%s\n", split[index]);
        }
        return registrationViewPath.toString();
    }
}
