package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import Model.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by Ramon Johnson
 * 2017-09-02.
 */
public class COADashboardController implements Initializable
{
    private SessionFactory sessionFactory;

    private Stage stage;

    private Scene oldScene;

    MultipleSelectionModel<COA> selectionModel;

    private ObservableList<COA> data = FXCollections.observableArrayList();

    public COADashboardController(SessionFactory _factory, Stage _stage)
    {
        sessionFactory = _factory;
        stage = _stage;
    }

    /**
     * Observable ArrayList to hold the data to submit to the TableView
     */

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="tableView"
    private TableView<COA> tableView; // Value injected by FXMLLoader

    @FXML // fx:id="serialNumberColumn"
    private TableColumn<COA, String> serialNumberColumn; // Value injected by FXMLLoader

    @FXML // fx:id="partNumberColumn"
    private TableColumn<COA, String> partNumberColumn; // Value injected by FXMLLoader

    @FXML // fx:id="orderNumberColumn"
    private TableColumn<COA, String> orderNumberColumn; // Value injected by FXMLLoader

    @FXML // fx:id="operatorColumn"
    private TableColumn<COA, String> operatorColumn; // Value injected by FXMLLoader

    @FXML // fx:id="dateAssignedColumn"
    private TableColumn<COA, String> dateAssignedColumn; // Value injected by FXMLLoader

    @FXML // fx:id="assignedColumn"
    private TableColumn<COA, String> assignedColumn; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="delinkCOAButton"
    private Button delinkCOAButton; // Value injected by FXMLLoader

    @FXML // fx:id="deleteCOA"
    private Button deleteCOA; // Value injected by FXMLLoader

    @FXML // fx:id="editCOA"
    private Button editCOA; // Value injected by FXMLLoader

    @FXML // fx:id="createCOA"
    private Button createCOA; // Value injected by FXMLLoader

    @FXML
    void createCOA(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML's/CreateCOAToOrderView.fxml"));
        CreateCOAToOrderViewController createCOAToOrderViewController = new CreateCOAToOrderViewController(sessionFactory);
        loader.setController(createCOAToOrderViewController);
        Stage _stage = new Stage(StageStyle.UNDECORATED);
        createCOAToOrderViewController.setStage(_stage);
        GridPane pane = loader.load();
        _stage.setScene(new Scene(pane));
        _stage.setResizable(false);
        Functions.setUpIcons(_stage);
        _stage.setOnCloseRequest(event1 -> _stage.close());
        _stage.show();
    }

    @FXML
    void editCOA(ActionEvent event)
    {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML's/EditCOAView.fxml"));
        //EditCOAViewController editCOAViewController = new EditCOAViewController(sessionFactory,stage , )
    }


    @FXML
    void deleteCOA(ActionEvent event)
    {
        ObservableList<COA> coaObservableList = tableView.getSelectionModel().getSelectedItems();
        Session session = sessionFactory.openSession();
        for(COA coa: coaObservableList)
        {

            session.getTransaction().begin();
            Order order = getOrderWithCOA(coa);
            order.getCoaList().remove(getCOAFromOrderList(order, coa.getSerialNumber()));
            session.delete(coa);
            session.update(order);
            session.getTransaction().commit();
            session.clear();
        }
        session.close();
    }

    @FXML
    void delinkCOA(ActionEvent event)
    {
        ObservableList<COA> coaObservableList = tableView.getSelectionModel().getSelectedItems();
        Session session = sessionFactory.openSession();
        for(COA coa: coaObservableList)
        {
            session.getTransaction().begin();
            Unit unit = coa.getUnit();
            Operator operator = getOperatorWithCOA(coa);

            //COA orderCOA;
            operator.getCoaList().remove(getCOAFromOperatorList(operator, coa.getSerialNumber()));

            coa.setAssigned(false);
            coa.setUnit(null);

            session.delete(unit);
            session.update(operator);

            session.update(coa);
            session.getTransaction().commit();
            session.clear();
            tableView.refresh();
        }
        session.close();
    }


    @FXML
    void exitDashboard(ActionEvent event)
    {
        stage.close();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert serialNumberColumn != null : "fx:id=\"serialNumberColumn\" was not injected: check your FXML file 'COADashboard.fxml'.";
        assert partNumberColumn != null : "fx:id=\"partNumberColumn\" was not injected: check your FXML file 'COADashboard.fxml'.";
        assert orderNumberColumn != null : "fx:id=\"orderNumberColumn\" was not injected: check your FXML file 'COADashboard.fxml'.";
        assert dateAssignedColumn != null : "fx:id=\"dateAssignedColumn\" was not injected: check your FXML file 'COADashboard.fxml'.";
        assert assignedColumn != null : "fx:id=\"assignedColumn\" was not injected: check your FXML file 'COADashboard.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'COADashboard.fxml'.";
        assert delinkCOAButton != null : "fx:id=\"removeCOAFromUnit\" was not injected: check your FXML file 'COADashboard.fxml'.";
        assert deleteCOA != null : "fx:id=\"deleteCOA\" was not injected: check your FXML file 'COADashboard.fxml'.";
        assert editCOA != null : "fx:id=\"editCOA\" was not injected: check your FXML file 'COADashboard.fxml'.";
        assert createCOA != null : "fx:id=\"createCOA\" was not injected: check your FXML file 'COADashboard.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'COADashboard.fxml'.";
    }

    private void setUpTableColumns()
    {
        serialNumberColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<COA, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<COA, String> param)
            {
                return new SimpleStringProperty(param.getValue().getSerialNumber());
            }
        });

        partNumberColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<COA, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<COA, String> param)
            {
                return new SimpleStringProperty(param.getValue().getPartNumber());
            }
        });

        orderNumberColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<COA, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<COA, String> param)
            {
                return new SimpleStringProperty(String.valueOf(param.getValue().getOrder().getOrderNumber()));
            }
        });

        dateAssignedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<COA, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<COA, String> param)
            {
                return new SimpleStringProperty(param.getValue().getCreatedDate().toString());
            }
        });

        assignedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<COA, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<COA, String> param)
            {
                return new SimpleStringProperty(param.getValue().getAssignedProperty());
            }
        });

        operatorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<COA, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<COA, String> param)
            {
                return new SimpleStringProperty(param.getValue().getOperatorID().getOperator());
            }
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
        setUpTableColumns();
        selectionModel = tableView != null ? tableView.getSelectionModel() : null;
        tableView.setItems(data);
        DatabaseService databaseService = new DatabaseService();
        databaseService.setRestartOnFailure(true);
        databaseService.setPeriod(Duration.seconds(2));
        databaseService.start();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        bindings();
    }

    private void bindings()
    {
        //--- Can't delete COA if it is assigned to a Unit ---//
        ReadOnlyObjectProperty<COA> selection = selectionModel.selectedItemProperty();
        BooleanBinding disable = tableView.getSelectionModel().selectedItemProperty().isNull();

        //--- Can't delete if it's assigned ---//
        BooleanBinding isAssigned = Bindings.createBooleanBinding(() -> selection.get().isAssigned(), selection);
        BooleanBinding notAssigned = Bindings.createBooleanBinding(() -> !selection.get().isAssigned(), selection);

        editCOA.disableProperty().bind(disable);
        delinkCOAButton.disableProperty().bind(disable.or(notAssigned));
        deleteCOA.disableProperty().bind(disable.or(isAssigned));
    }

    /**
     * DatabaseService class continuously checks the database for any changes
     * and makes the tableView refresh to display the data
     */
    private class DatabaseService extends ScheduledService<Void>
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
                    Query query = session.createQuery("from COA WHERE order.isFinished = false ORDER BY createdDate desc ");
                    List<COA> coaList = query.list();
                    if (coaList.size() > data.size())
                        databaseGreaterThanMap(coaList, data);
                    else if (coaList.size() < data.size())
                        databaseLesserThanMap(coaList, data);
                    else if (coaList.size() == data.size())
                        databaseEqualToMap(coaList, data);
                    else {
                        data = FXCollections.observableArrayList();
                    }
                    session.close();
                    tableView.refresh();
                    return null;
                }
            };
        }

        /**
         * @param _coaList List from the Query
         * @param _coas List saved that is set to the Program
         */
        @SuppressWarnings("Duplicates")
        private void databaseGreaterThanMap(List<COA> _coaList, ObservableList<COA> _coas)
        {
            //--- This method gets activated by the list from the Query being bigger than the data saved ---//
            Map<Integer, COA> $coaMap = coaMap(_coaList);
            Map<Integer, COA> $observableMap = coaMap(_coas);
            //-- Since _orderMap is greater, we can easily add to observable ---//
            for(Integer $coa : $coaMap.keySet())
            {
                if($observableMap.containsKey($coa))
                {
                    COA temp = $coaMap.get($coa);
                    COA data = $observableMap.get($coa);
                    data.setCreatedDate(temp.getCreatedDate());
                    data.setCreateTime(temp.getCreateTime());
                    data.setAssigned(temp.isAssigned());
                    data.setOrder(temp.getOrder());
                    data.setUnit(temp.getUnit());
                    data.setOperatorID(temp.getOperatorID());
                }
                else
                    _coas.add($coaMap.get($coa));
            }
        }

        /**
         * @param _coaList List from the Query
         * @param _coas List saved that is set to the Program
         */
        @SuppressWarnings("Duplicates")
        private void databaseLesserThanMap(List<COA> _coaList, ObservableList<COA> _coas)
        {
            //--- This method is activated when the list from the Query is less than the data saved ---//
            Map<Integer, COA> $coaMap = coaMap(_coaList);
            Map<Integer, COA> $observableMap = coaMap(_coas);
            //--- We have to remove from observable List --//
            for (Integer $coa : $coaMap.keySet())
            {
                if($observableMap.containsKey($coa))
                {
                    COA temp = $coaMap.get($coa);
                    COA data = $observableMap.get($coa);
                    data.setCreatedDate(temp.getCreatedDate());
                    data.setCreateTime(temp.getCreateTime());
                    data.setAssigned(temp.isAssigned());
                    data.setOrder(temp.getOrder());
                    data.setUnit(temp.getUnit());
                    data.setOperatorID(temp.getOperatorID());
                }
            }

            for(Integer $coa : $observableMap.keySet())
            {
                if(!$coaMap.containsKey($coa))
                {
                    _coas.remove($observableMap.get($coa));
                }
            }
        }

        /**
         * @param _coalList List from the Query
         * @param _coas List saved that is set to the Program
         */
        @SuppressWarnings("Duplicates")
        private void databaseEqualToMap(List<COA> _coalList, ObservableList<COA> _coas)
        {
            //--- This method is activated if both list are the same, just send the details over to observable ---//
            Map<Integer, COA> $orderMap = coaMap(_coalList);
            Map<Integer, COA> $observableMap = coaMap(_coas);
            for(Integer $coa: $observableMap.keySet())
            {
                //--- There is a possibility for null here, but it should be very very very hard to do so ---//
                COA temp = $orderMap.get($coa);
                COA data = $observableMap.get($coa);
                data.setCreatedDate(temp.getCreatedDate());
                data.setCreateTime(temp.getCreateTime());
                data.setAssigned(temp.isAssigned());
                data.setOrder(temp.getOrder());
                data.setUnit(temp.getUnit());
                data.setOperatorID(temp.getOperatorID());
            }
        }

        /**
         * @param _theList List from Query.list() method
         * @return Returns TreeMap object
         */
        private Map<Integer, COA> coaMap(List<COA> _theList)
        {
            Map<Integer, COA> _temp = new TreeMap<>();
            for(COA _coa: _theList)
            {
                _temp.put(Integer.valueOf(_coa.getSerialNumber()), _coa);
            }
            return _temp;
        }

        /**
         * @param _theList List from the variable data
         * @return Returns TreeMap object
         */
        private Map<Integer, COA> coaMap(ObservableList<COA> _theList)
        {
            Map<Integer, COA> _temp = new TreeMap<>();
            for(COA _coa: _theList)
            {
                _temp.put(Integer.valueOf(_coa.getSerialNumber()), _coa);
            }
            return _temp;
        }
    }

    private Operator getOperatorWithCOA(COA _coa)
    {
        Operator temp = null;
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM Operator");
        List<Operator> operatorList = query.list();
        for (Operator operator : operatorList) 
        {
            for (COA coa : operator.getCoaList())
            {
                if(coa.getSerialNumber().equalsIgnoreCase(_coa.getSerialNumber()))
                    temp = operator;
            }
        }
        session.close();
        return temp;
    }

    private Order getOrderWithCOA(COA _coa)
    {
        Order temp = null;
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM Order");
        List<Order> orderList = query.list();
        for (Order order : orderList)
        {
            for (COA coa : order.getCoaList()) {
                if(coa.getSerialNumber().equalsIgnoreCase(_coa.getSerialNumber()))
                {
                    temp = order;
                    break;
                }
            }
        }
        session.close();
        return temp;
    }

    private COA getCOAFromOperatorList(Operator _operator, String _serialNumber)
    {
        COA temp = null;
        for (COA coa : _operator.getCoaList())
        {
            if (coa.getSerialNumber().equalsIgnoreCase(_serialNumber))
            {
                temp = coa;
            }
        }
        return temp;
    }

    private COA getCOAFromOrderList(Order _order, String _serialNumber)
    {
        COA temp = null;
        for (COA coa : _order.getCoaList())
        {
            if (coa.getSerialNumber().equalsIgnoreCase(_serialNumber))
            {
                temp = coa;
            }
        }
        return temp;
    }
}
