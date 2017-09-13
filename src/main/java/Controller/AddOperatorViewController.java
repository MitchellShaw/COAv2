package Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Model.ErrorNotification;
import Model.Notification;
import Model.Operator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author Ramon Johnson
 * @version 1.0.0.0
 * 8/21/2017
 */
public class AddOperatorViewController implements Initializable
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

    AddOperatorViewController(SessionFactory _factory)
    {
        sessionFactory = _factory;
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="gridP"
    private GridPane gridP; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    @FXML // fx:id="rqsIDTextField"
    private TextField rqsIDTextField; // Value injected by FXMLLoader

    @FXML // fx:id="firstNameTextField"
    private TextField firstNameTextField; // Value injected by FXMLLoader

    @FXML // fx:id="lastNameTextField"
    private TextField lastNameTextField; // Value injected by FXMLLoader

    /**
     * @param event ActionEvent passed down through JavaFX
     *              This method clears all of the fileds in this view
     */
    @FXML
    void clearFields(ActionEvent event) {
        Platform.runLater(() ->
        {
            rqsIDTextField.setText("");
            firstNameTextField.setText("");
            lastNameTextField.setText("");
        });
    }

    /**
     * @param event ActionEvent passed down through JavaFX
     *              This method closes the stage of this view
     */
    @FXML
    void closeStage(ActionEvent event) {
        stage.close();
    }

    /**
     * @param event ActionEvent passed down through JavaFX
     *              This method creates an user object in the database
     */
    @FXML
    void createUserInDatabase(ActionEvent event) {
        //--- Check to make sure user doesn't exist in database ---//
        if(!checkUserExist(rqsIDTextField.getText()))
        {
            Session session = sessionFactory.openSession();
            session.getTransaction().begin();

            Operator operator = new Operator();
            operator.setOperator(rqsIDTextField.getText());
            operator.setFirstName(firstNameTextField.getText());
            operator.setLastName(lastNameTextField.getText());
            session.save(operator);
            session.getTransaction().commit();
            session.close();
            new Notification("Operator", rqsIDTextField.getText() + " added to the database successfully", Pos.BASELINE_RIGHT);
        }
        else
            new ErrorNotification("Operator", rqsIDTextField.getText() + " is already in the database", Pos.BASELINE_RIGHT);
    }

    /**
     * @param _text Username to search the database for
     * @return Returns true if the username is found, false if it was not.
     */
    private boolean checkUserExist(String _text)
    {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from Operator WHERE operator = :op").setParameter("op", _text);
        List<Operator> operatorList = query.list();
        if(operatorList.size() > 0)
        {
            session.close();
            return true;
        }
        else
        {
            session.close();
            return false;
        }
    }

    @FXML
    private void mouseDragged(MouseEvent event)
    {
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    private void mousePressed(MouseEvent event)
    {
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize()
    {
        assert gridP != null : "fx:id=\"gridP\" was not injected: check your FXML file 'AddOperatorView.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'AddOperatorView.fxml'.";
        assert clearButton != null : "fx:id=\"clearButton\" was not injected: check your FXML file 'AddOperatorView.fxml'.";
        assert submitButton != null : "fx:id=\"submitButton\" was not injected: check your FXML file 'AddOperatorView.fxml'.";
        assert rqsIDTextField != null : "fx:id=\"rqsIDTextField\" was not injected: check your FXML file 'AddOperatorView.fxml'.";
        assert firstNameTextField != null : "fx:id=\"firstNameTextField\" was not injected: check your FXML file 'AddOperatorView.fxml'.";
        assert lastNameTextField != null : "fx:id=\"lastNameTextField\" was not injected: check your FXML file 'AddOperatorView.fxml'.";
    }

    /**
     * Setter for property 'stage'.
     *
     * @param _stage Value to set for property 'stage'.
     */
    void setStage(Stage _stage)
    {
        stage = _stage;
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
}
