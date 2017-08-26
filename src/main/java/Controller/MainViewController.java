package Controller;
/**
 * Sample Skeleton for 'MainView.fxml' Controller Class
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.SessionFactory;

public class MainViewController implements Initializable
{
    private SessionFactory sessionFactory;

    private Stage mainStage;

    public MainViewController(SessionFactory _factory)
    {
        sessionFactory = _factory;
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="gridP"
    private GridPane gridP; // Value injected by FXMLLoader

    @FXML // fx:id="createOrderButton"
    private Button createOrderButton; // Value injected by FXMLLoader

    @FXML // fx:id="assignCOAsButton"
    private Button assignCOAsButton; // Value injected by FXMLLoader

    @FXML // fx:id="checkOrderStatusButton"
    private Button checkOrderStatusButton; // Value injected by FXMLLoader

    @FXML // fx:id="addOperatorButton"
    private Button addOperatorButton; // Value injected by FXMLLoader


    @FXML
    void assignCOAs(ActionEvent event) throws IOException
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
    void checkOrders(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(new URL(getProductionPath() + "/resources/FXML's/OpenOrdersView.fxml"));
        OpenOrderViewController openOrderViewController = new OpenOrderViewController(sessionFactory);
        Stage stage = new Stage();
        openOrderViewController.setStage(stage);
        loader.setController(openOrderViewController);
        stage.setTitle("Certificate of Authenticity Dashboard");
        openOrderViewController.setStage(stage);
        GridPane pane = loader.load();
        stage.setScene(new Scene(pane));
        Functions.setUpIcons(stage);
        stage.setOnCloseRequest(event1 -> stage.close());
        stage.show();
    }

    @FXML
    void createOrder(ActionEvent event) throws IOException
    {

        FXMLLoader fxmlLoader = new FXMLLoader(new URL(getProductionPath() + "/resources/FXML's/CreateOrderView.fxml"));
        CreateOrderViewController createOrderViewController = new CreateOrderViewController(sessionFactory);
        fxmlLoader.setController(createOrderViewController);
        GridPane pane = fxmlLoader.load();
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

    @FXML
    void addOperator(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(new URL(getProductionPath() + "/resources/FXML's/AddOperatorView.fxml"));
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
    private // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert gridP != null : "fx:id=\"gridP\" was not injected: check your FXML file 'MainView.fxml'.";
        assert createOrderButton != null : "fx:id=\"createOrderButton\" was not injected: check your FXML file 'MainView.fxml'.";
        assert assignCOAsButton != null : "fx:id=\"assignCOAsButton\" was not injected: check your FXML file 'MainView.fxml'.";
        assert checkOrderStatusButton != null : "fx:id=\"checkOrderStatusButton\" was not injected: check your FXML file 'MainView.fxml'.";
        assert addOperatorButton != null : "fx:id=\"addOperatorButton\" was not injected: check your FXML file 'MainView.fxml'.";
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

    /**
     * Setter for property 'mainStage'.
     *
     * @param mainStage Value to set for property 'mainStage'.
     */
    public void setMainStage(Stage mainStage)
    {
        this.mainStage = mainStage;
    }
}
