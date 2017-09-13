package Controller;


import java.io.IOException;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.*;

import Model.*;
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
import javafx.geometry.Pos;
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
 * 8/25/2017
 * @version 1.0.0.1
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
     * Observable ArrayList to hold the data to submit to the TableView
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

    @FXML // fx:id="table"
    private TableView<Order> table; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberColumn"
    private TableColumn<Order, String> orderNumberColumn; // Value injected by FXMLLoader

    @FXML // fx:id="quantityColumn"
    private TableColumn<Order, String> quantityColumn; // Value injected by FXMLLoader

    @FXML // fx:id="quantityRemainingColumn"
    private TableColumn<Order, String> quantityRemainingColumn; // Value injected by FXMLLoader

    @FXML // fx:id="ssdColumn"
    private TableColumn<Order, String> ssdColumn; // Value injected by FXMLLoader

    @FXML // fx:id="buttonBar"
    private ButtonBar buttonBar; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="reportButton"
    private Button reportButton; // Value injected by FXMLLoader

    @FXML // fx:id="coaButton"
    private Button coaButton; // Value injected by FXMLLoader

    @FXML // fx:id="assignCOAButton"
    private Button assignCOAButton; // Value injected by FXMLLoader

    @FXML // fx:id="editOrderButton"
    private Button editOrderButton; // Value injected by FXMLLoader

    @FXML // fx:id="completeOrderButton"
    private Button completeOrderButton; // Value injected by FXMLLoader

    @FXML // fx:id="createOrderButton"
    private Button createOrderButton; // Value injected by FXMLLoader

    @FXML // fx:id="addOperatorButton"
    private Button addOperatorButton; // Value injected by FXMLLoader


        @FXML
        void addOperator(ActionEvent event) throws IOException
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML's/AddOperatorView.fxml"));
            AddOperatorViewController addOperatorView = new AddOperatorViewController(sessionFactory);
            loader.setController(addOperatorView);
            Stage stage = new Stage(StageStyle.UNDECORATED);
            addOperatorView.setStage(stage);
            GridPane pane = loader.load();
            stage.setResizable(false);
            stage.setScene(new Scene(pane));
            stage.initModality(Modality.APPLICATION_MODAL);
            Functions.setUpIcons(stage);
            stage.setOnCloseRequest(event1 -> stage.close());
            stage.show();
        }

        @FXML
        void assignCOA(ActionEvent event) throws IOException
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML's/AssignCOAView.fxml"));
            AssignCOAViewController assignCOAViewController = new AssignCOAViewController(sessionFactory);
            loader.setController(assignCOAViewController);
            Stage stage = new Stage(StageStyle.UNDECORATED);
            assignCOAViewController.setStage(stage);
            GridPane pane = loader.load();
            stage.setResizable(false);
            stage.setScene(new Scene(pane));
            stage.initModality(Modality.APPLICATION_MODAL);
            Functions.setUpIcons(stage);
            stage.setOnCloseRequest(event1 -> stage.close());
            stage.show();
        }

        @FXML
        void createOrder(ActionEvent event) throws IOException
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML's/CreateOrderView.fxml"));
            CreateOrderViewController createOrderViewController = new CreateOrderViewController(sessionFactory);
            loader.setController(createOrderViewController);
            Stage stage = new Stage(StageStyle.UNDECORATED);
            createOrderViewController.setStage(stage);
            GridPane pane = loader.load();
            stage.setResizable(false);
            stage.setTitle("Create Order");
            stage.setScene(new Scene(pane));
            stage.initModality(Modality.APPLICATION_MODAL);
            Functions.setUpIcons(stage);
            stage.setOnCloseRequest(event1 -> stage.close());
            stage.show();
        }

        @FXML
        void coaDashboard(ActionEvent event) throws IOException
        {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML's/COADashboard.fxml"));
            COADashboardController coaDashboardController = new COADashboardController(sessionFactory,stage);
            loader.setController(coaDashboardController);
            GridPane pane = loader.load();
            stage.setTitle("COA Dashboard");
            final UndecoratorScene undecoratorScene = new UndecoratorScene(stage, pane);
            stage.setScene(undecoratorScene);
            Functions.setUpIcons(stage);
            stage.setOnCloseRequest(event1 -> stage.close());
            stage.show();
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
                new ErrorNotification("Certificate of Authenticity", "There are still more COA's to be assigned", Pos.BASELINE_RIGHT);
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
                        session.update(o);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML's/EditOrderView.fxml"));
            Stage stage1 = new Stage(StageStyle.UNDECORATED);
            EditOrderViewController editOrderViewController = new EditOrderViewController(sessionFactory, stage1, temp.getOrderNumber(), temp.getScheduledShipDate(), temp.getQuantity());
            loader.setController(editOrderViewController);
            GridPane gridPane = loader.load();
            Functions.setUpIcons(stage1);
            stage1.initModality(Modality.APPLICATION_MODAL);
            stage1.setOnCloseRequest(event1 -> stage1.close());
            stage1.setScene(new Scene(gridPane));
            stage1.show();
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

    @FXML
    void viewReport(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML's/ReportView.fxml"));
        Stage stage1 = new Stage(StageStyle.UNDECORATED);
        ReportViewController reportViewController = new ReportViewController(sessionFactory, stage1);
        loader.setController(reportViewController);
        GridPane gridPane = loader.load();
        Functions.setUpIcons(stage1);
        stage1.setOnCloseRequest(event1 -> stage1.close());
        stage1.setScene(new Scene(gridPane));
        stage1.show();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert pane != null : "fx:id=\"pane\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert openOrder != null : "fx:id=\"openOrder\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert orderNumberColumn != null : "fx:id=\"orderNumberColumn\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert quantityColumn != null : "fx:id=\"quantityColumn\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert quantityRemainingColumn != null : "fx:id=\"quantityRemainingColumn\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert ssdColumn != null : "fx:id=\"ssdColumn\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert buttonBar != null : "fx:id=\"buttonBar\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert reportButton != null : "fx:id=\"reportButton\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert coaButton != null : "fx:id=\"coaButton\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert assignCOAButton != null : "fx:id=\"assignCOAButton\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert editOrderButton != null : "fx:id=\"editOrderButton\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert completeOrderButton != null : "fx:id=\"completeOrderButton\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert createOrderButton != null : "fx:id=\"createOrderButton\" was not injected: check your FXML file 'DashboardView.fxml'.";
        assert addOperatorButton != null : "fx:id=\"addOperatorButton\" was not injected: check your FXML file 'DashboardView.fxml'.";
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

        @Override
        protected Task<Void> createTask()
        {
            return new Task<Void>()
            {
                @Override
                protected Void call() throws Exception
                {
                    if(data.size() > 0)
                    {
                        for(Order $order: data)
                        {
                            int count = 0;
                            for(COA coa : $order.getCoaList())
                            {
                                if(coa.isAssigned())
                                    count++;
                            }

                            $order.setCompleted(count);
                        }
                    }
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

        @Override
        @SuppressWarnings("Duplicates")
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
                }
                else
                    _orders.add($orderMap.get($order));
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
            //--- We have to remove from observable List --//
            for (Integer $order : $orderMap.keySet())
            {
                if($observableMap.containsKey($order))
                {
                    Order temp = $orderMap.get($order);
                    Order data = $observableMap.get($order);
                    data.setQuantity(temp.getQuantity());
                    data.setScheduledShipDate(temp.getScheduledShipDate());
                }
            }

            for(Integer $order : $observableMap.keySet())
            {
                if(!$orderMap.containsKey($order))
                    _orders.remove($observableMap.get($order));
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
                data.setCoaList(temp.getCoaList());
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
                _temp.put(_order.getOrderNumber(), _order);
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
                _temp.put(_order.getOrderNumber(), _order);
            return _temp;
        }
    }
}
