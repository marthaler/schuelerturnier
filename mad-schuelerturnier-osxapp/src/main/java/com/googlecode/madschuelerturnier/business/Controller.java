package com.googlecode.madschuelerturnier.business;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;

import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;



import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author dama
 */

public class Controller implements Initializable{

    private boolean init = false;

  //  private static final Logger LOG = Logger.getLogger(Controller.class);


    private static Controller INSTANCE = null;
    @FXML
    private Button beenden;

    @FXML
    private Button verbinden;

    @FXML
    private Rectangle verbunden;

    @FXML
    private TextField urltext;

    @FXML
    private ProgressIndicator progress;



    public Controller(){

        INSTANCE = this;
    }

    private void start(){
        if(this.init){
                    return;
        }


        this.init = true;
    }

    @FXML
    private void handleTableAction(ActionEvent event) {
        System.out.println(event);
    }


    public static Controller getInstance(){
        return INSTANCE;
    }


    @FXML
    private void handleButtonBeendenAction(ActionEvent event) {
       System.exit(0);
    }

    @FXML
    private void handleButtonVerbindenAction(ActionEvent event) {
        if(verbinden.getText().equals("Trennen")){
            verbinden.setText("Verbinden");
            verbunden.setFill(Paint.valueOf("RED"));
            urltext.setDisable(false);
        }  else{



            verbinden.setText("Trennen");
            verbunden.setFill(Paint.valueOf("GREEN"));
            urltext.setDisable(true);

            try {
            Class<?> cls = Class.forName("org.apache.tomcat.maven.runner.Tomcat7RunnerCli");
            Method meth = cls.getMethod("main", String[].class);
            String[] params = null; // init params accordingly

                meth.invoke(null, (Object) params); // static method doesn't have an instance
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }



}
