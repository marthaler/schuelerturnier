package com.googlecode.madschuelerturnier.business.printer;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.googlecode.madschuelerturnier.business.integration.jms.SchuelerturnierTransportController;

import com.googlecode.madschuelerturnier.business.integration.jms.TransportControllerFactory;
import com.googlecode.madschuelerturnier.model.messages.IncommingMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author dama
 */

public class Controller implements Initializable , ApplicationListener<IncommingMessage>, ApplicationContextAware {

    private static final Logger LOG = Logger.getLogger(Controller.class);

    SchuelerturnierTransportController cont;

    ApplicationContext applicationContext;

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


    @FXML
    private TableView<IncommingMessage> table;

    public Controller(){

        INSTANCE = this;






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


            cont = TransportControllerFactory.getInstance().createController("NO URL",urltext.getText())     ;

            verbinden.setText("Trennen");
            verbunden.setFill(Paint.valueOf("GREEN"));
            urltext.setDisable(true);
        }

        ProgressThread th = new ProgressThread(progress);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


    public void onApplicationEvent(final IncommingMessage event) {

           if(table.getItems() == null){
               ObservableList<IncommingMessage> items  =FXCollections.observableArrayList ();
               table.setItems(items);
           }
        table.getItems().add(event);



        LOG.debug("incomming message: " + event);




    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
          this.applicationContext  = applicationContext;
    }
}
