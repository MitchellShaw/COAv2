import Controller.DashboardViewController;
import Model.*;
import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Ramon Johnson
 * @version 1.0.0.1
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
        setupHibernate();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML's/DashboardView.fxml"));
        DashboardViewController dashboardViewController = new DashboardViewController(sessionFactory);
        dashboardViewController.setStage(primaryStage);
        loader.setController(dashboardViewController);
        GridPane pane = loader.load();
        Functions.setUpIcons(primaryStage);
        final UndecoratorScene undecoratorScene = new UndecoratorScene(primaryStage, pane);
        undecoratorScene.setFadeInTransition();
        primaryStage.setOnCloseRequest(event1 ->
            System.exit(0));
        primaryStage.setScene(undecoratorScene);
        primaryStage.toFront();
        primaryStage.show();
    }


    /**
     * Sets up Hibernate framework
     */
    private void setupHibernate()
    {
        Configuration configuration = new Configuration().addAnnotatedClass(COA.class).addAnnotatedClass(Order.class).addAnnotatedClass(Operator.class).addAnnotatedClass(Unit.class).configure();

        configuration.configure("hibernate.cfg.xml");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

}
