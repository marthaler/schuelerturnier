package com.googlecode.madschuelerturnier.business.printer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author dama
 */
public class App extends javafx.application.Application {

    private ApplicationContext context;

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/form.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        context = new ClassPathXmlApplicationContext("spring-business-context.xml");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
