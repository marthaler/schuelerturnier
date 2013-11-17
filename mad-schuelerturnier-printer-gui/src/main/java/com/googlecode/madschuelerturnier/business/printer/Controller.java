package com.googlecode.madschuelerturnier.business.printer;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.googlecode.madschuelerturnier.business.integration.jms.SchuelerturnierTransportController;

import com.googlecode.madschuelerturnier.business.integration.jms.TransportControllerFactory;
import com.googlecode.madschuelerturnier.model.messages.File;
import com.googlecode.madschuelerturnier.model.messages.IncommingMessage;
import com.googlecode.madschuelerturnier.model.messages.MessageWrapper;
import com.sun.prism.impl.Disposer;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.print.*;

/**
 *
 * @author dama
 */

public class Controller implements Initializable , ApplicationListener<IncommingMessage>, ApplicationContextAware {

    private boolean init = false;

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
    private TableView<File> table;

    public Controller(){

        INSTANCE = this;
    }

    private void start(){
        if(this.init){
                    return;
        }
        table.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object o2) {
                File file = (File) o;

                if(file == null){
                    file = (File) o2;
                }

                try {
                    java.io.File f = java.io.File.createTempFile("tmp", ".pdf");

                    IOUtils.write(file.getContent(),new FileOutputStream(f));
                    f.deleteOnExit();
                    //Desktop.getDesktop().open(f);


                    Runtime.getRuntime().exec(new String[]{"/usr/bin/open",
                            f.getAbsolutePath()});


                        /** windows
                    Process p = Runtime
                            .getRuntime()
                            .exec("rundll32 url.dll,FileProtocolHandler c:\\Java-Interview.pdf");
                   **/


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

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

            cont = TransportControllerFactory.getInstance().createController("NO URL",urltext.getText())     ;
             // filter nach Files setzen
            cont.getMessagefilter().add("com.googlecode.madschuelerturnier.model.messages.File");

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
         start();
        File file = (File) event.getPayload();

           if(table.getItems() == null){
               ObservableList<File> items  =FXCollections.observableArrayList ();
               table.setItems(items);
           }
        file.updateDruckzeit();
        table.getItems().add(0, file);


        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;

        ByteArrayInputStream bin = new ByteArrayInputStream(file.getContent());

        //create document
        Doc doc = new SimpleDoc(bin, flavor, null);

        DocPrintJob job = service.createPrintJob();



        try {
            job.print(doc, null);
        } catch (PrintException e) {
            LOG.error(e.getMessage(), e);
        }

        try {
            if (bin != null) {
                bin.close();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }


        LOG.debug("incomming message: " + file);


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
          this.applicationContext  = applicationContext;
    }
}
