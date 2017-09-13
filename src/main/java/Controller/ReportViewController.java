package Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import Model.COA;
import Model.ErrorNotification;
import Model.Report;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author Ramon Johnson
 * @version 0.0.0.1
 * 9/13/2017
 */
public class ReportViewController implements Initializable
{
    private SessionFactory sessionFactory;

    private Stage stage;

    private ObservableList<String> partnumbers;

    public ReportViewController(SessionFactory _factory, Stage _stage)
    {
        sessionFactory = _factory;
        stage = _stage;
        try
        {
            partnumbers = loadPartNumbers();
        } catch(ParserConfigurationException | IOException | SAXException e)
        {
            new ErrorNotification("Load Fail", e.getMessage(), Pos.BASELINE_RIGHT);
        }
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="tableView"
    private TableView<Report> tableView; // Value injected by FXMLLoader

    @FXML // fx:id="partNumberColumn"
    private TableColumn<Report, String> partNumberColumn; // Value injected by FXMLLoader

    @FXML // fx:id="quantityColumn"
    private TableColumn<Report, String> quantityColumn; // Value injected by FXMLLoader

    @FXML // fx:id="datePicker"
    private DatePicker datePicker; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="reportButton"
    private Button reportButton; // Value injected by FXMLLoader

    @FXML
    void exitStage(ActionEvent event)
    {
        stage.close();
    }

    @FXML
    void viewReport(ActionEvent event)
    {
        retrieveQuery().run();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'ReportView.fxml'.";
        assert partNumberColumn != null : "fx:id=\"partNumberColumn\" was not injected: check your FXML file 'ReportView.fxml'.";
        assert quantityColumn != null : "fx:id=\"quantityColumn\" was not injected: check your FXML file 'ReportView.fxml'.";
        assert datePicker != null : "fx:id=\"datePicker\" was not injected: check your FXML file 'ReportView.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'ReportView.fxml'.";
        assert reportButton != null : "fx:id=\"reportButton\" was not injected: check your FXML file 'ReportView.fxml'.";

    }

    private void setupTableColumns()
    {
        partNumberColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Report, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Report, String> param)
            {
                return new SimpleStringProperty(param.getValue().getPartNumber());
            }
        });

        quantityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Report, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Report, String> param)
            {
                return new SimpleStringProperty(param.getValue().getQuantity());
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
        setupTableColumns();
        datePicker.setValue(LocalDate.now());
        retrieveQuery().run();
    }

    private Task<Void> retrieveQuery()
    {
        return new Task<Void>()
        {
            @Override
            protected Void call() throws Exception
            {
                ObservableList<Report> reports = FXCollections.observableArrayList();
                Session session = sessionFactory.openSession();
                session.getTransaction().begin();

                Query query = session.createQuery("from COA WHERE createdDate = :datePicker").setParameter("datePicker", datePicker.getValue());
                List<COA> coaList = query.list();
                for(String string : partnumbers)
                {
                    int quantity = 0;
                    for(COA coa : coaList)
                    {
                        if(coa.getPartNumber().equalsIgnoreCase(string))
                            ++quantity;
                    }
                    if(quantity > 0)
                        reports.add(new Report(string, String.valueOf(quantity)));
                }
                tableView.setItems(reports);
                return null;
            }
        };
    }

    private ObservableList<String> loadPartNumbers() throws ParserConfigurationException, IOException, SAXException
    {
        File file = new File("\\\\SUSMID8000\\D\\Programs\\COA\\Types of COAs.xml");
//            File file = new File("D:\\IdeaProjects\\COA\\src\\main\\resources\\Files\\Operating Systems.xml");
        ObservableList<String> list = FXCollections.observableArrayList();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(file);
        NodeList nodeList = document.getElementsByTagName("model");
        for(int x = 0, size = nodeList.getLength(); x < size; x++)
        {
            String partNumber = nodeList.item(x).getAttributes().getNamedItem("number").getNodeValue();
            String description = nodeList.item(x).getAttributes().getNamedItem("description").getNodeValue();
            list.add(partNumber);
        }
        return list;
    }
}
