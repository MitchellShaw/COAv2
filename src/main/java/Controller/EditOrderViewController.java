package Controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import Model.Functions;
import Model.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class EditOrderViewController implements Initializable
{
    private SessionFactory sessionFactory;

    private Stage stage;

    private int orderNumber;

    private LocalDate ssd;

    private int quantity;

    EditOrderViewController(SessionFactory sessionFactory, Stage stage, int orderNumber, LocalDate ssd, int quantity)
    {
        this.sessionFactory = sessionFactory;
        this.stage = stage;
        this.orderNumber = orderNumber;
        this.ssd = ssd;
        this.quantity = quantity;
    }

    @FXML
    private DatePicker datePicker;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="orderNumberLabel"
    private Label orderNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    @FXML // fx:id="quantityTextField"
    private TextField quantityTextField; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert orderNumberLabel != null : "fx:id=\"orderNumberLabel\" was not injected: check your FXML file 'EditOrderView.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'EditOrderView.fxml'.";
        assert submitButton != null : "fx:id=\"submitButton\" was not injected: check your FXML file 'EditOrderView.fxml'.";
        assert quantityTextField != null : "fx:id=\"quantityTextField\" was not injected: check your FXML file 'EditOrderView.fxml'.";
    }

    @FXML
    void exitStage(ActionEvent event) {
        stage.close();
    }

    @FXML
    void submitData(ActionEvent event) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from Order WHERE orderNumber = :orderNumber").setParameter("orderNumber", orderNumber);
        List<Order> orderList = query.list();
        for(Order order: orderList)
        {
            order.setQuantity(Integer.parseInt(quantityTextField.getText()));
            order.setScheduledShipDate(datePicker.getValue());
            session.save(order);
            session.getTransaction().commit();
        }
        session.close();
        stage.close();
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
        Functions.numericTextFieldWithMinimum(quantityTextField, 1);
        orderNumberLabel.setText(String.format("Order No: %d", orderNumber));
        quantityTextField.setText(String.valueOf(quantity));
        datePicker.setValue(ssd);

    }
}
