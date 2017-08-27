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
    /**
     * Stage object to be used for closing the window when the use for this View is done
     */
    private Stage stage;

    /**
     * SessionFactory that is passed down to all classes of this Application so it can be used
     * to run/create queries and make changes to the database by creating Session objects
     */
    private SessionFactory sessionFactory;

    /**
     * Offset location for X
     */
    private static double xOffset = 0;

    /**
     * Offset location for Y
     */
    private static double yOffset = 0;

    /**
     * Order number needs to be saved so it can be set when this view is shown
     */
    private int orderNumber;

    /**
     * Scheduled ship date needs to be saved so it can be set when this view is shown
     */
    private LocalDate ssd;

    /**
     * The number of units needs to be saved so it can be set when this view is shown
     */
    private int quantity;

    /**
     * @param sessionFactory SessionFactory to be used to create Session
     * @param stage Stage object so the exit button can close this stage
     * @param orderNumber Order number so it can be saved locally in this class
     * @param ssd Schedule ship date so it can be saved locally in this class
     * @param quantity Quantity so it can be saved locally in this class
     */
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

    /**
     * ResourceBundle that was given to the FXMLLoader
     */
    @FXML
    private ResourceBundle resources;

    /**
     * URL location of the FXML file that was given to the FXMLLoader
     */
    @FXML
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
