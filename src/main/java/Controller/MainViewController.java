package Controller;
/**
 * Sample Skeleton for 'MainView.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.hibernate.SessionFactory;

public class MainViewController
{
    private SessionFactory sessionFactory;
    public MainViewController(SessionFactory _factory)
    {
        sessionFactory = _factory;
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="checkOrderStatus"
    private Button checkOrderStatus; // Value injected by FXMLLoader

    @FXML // fx:id="assignCOAsButton"
    private Button assignCOAsButton; // Value injected by FXMLLoader

    @FXML // fx:id="checkOrderStatusButton"
    private Button checkOrderStatusButton; // Value injected by FXMLLoader

    @FXML
    void assignCOAs(ActionEvent event) {

    }

    @FXML
    void checkOrder(ActionEvent event) {

    }

    @FXML
    void checkOrderStatus(ActionEvent event)
    {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert checkOrderStatus != null : "fx:id=\"checkOrderStatus\" was not injected: check your FXML file 'MainView.fxml'.";
        assert assignCOAsButton != null : "fx:id=\"assignCOAsButton\" was not injected: check your FXML file 'MainView.fxml'.";
        assert checkOrderStatusButton != null : "fx:id=\"checkOrderStatusButton\" was not injected: check your FXML file 'MainView.fxml'.";

    }
}
