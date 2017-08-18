import Controller.MainViewController;
import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.net.URL;

/**
 * @author Ramon Johnson
 * @version 0.0.0.1
 * 8/17/2017
 */
public class App extends Application
{
    /**
     * SessionFactory object that will be persistent throughout the application
     */
    private SessionFactory sessionFactory;

    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("COA Tracker");
        setupHibernate();
        System.out.printf("%s\n", getProductionPath());
        FXMLLoader loader = new FXMLLoader(new URL(getProductionPath() + "/production/resources/FXML's/MainView.fxml"));
        MainViewController mainViewController = new MainViewController(sessionFactory);
        loader.setController(mainViewController);
        GridPane gridPane = new GridPane();
        gridPane = loader.load();
        Functions.setUpIcons(primaryStage);
        primaryStage.setScene(new Scene(gridPane));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
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
            System.out.printf("%s\n", split[index]);
        }
        return registrationViewPath.toString();
    }

    /**
     * Sets up Hibernate framework
     */
    private void setupHibernate()
    {
        Configuration configuration = new Configuration().addAnnotatedClass(COA.class).addAnnotatedClass(COAOrder.class).addAnnotatedClass(Operator.class).addAnnotatedClass(Unit.class).configure();

        configuration.configure("hibernate.cfg.xml");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

}