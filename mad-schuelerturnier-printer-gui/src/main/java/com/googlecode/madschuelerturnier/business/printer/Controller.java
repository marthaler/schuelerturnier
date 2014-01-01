/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.printer;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.googlecode.madschuelerturnier.business.integration.jms.SchuelerturnierTransportControllerImpl;

import com.googlecode.madschuelerturnier.business.integration.jms.TransportControllerFactory;
import com.googlecode.madschuelerturnier.model.messages.File;
import com.googlecode.madschuelerturnier.model.messages.IncommingMessage;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import javax.print.*;

/**
 * Controller und model fuer den printer
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class Controller implements Initializable, ApplicationListener<IncommingMessage>, ApplicationContextAware {

    private static final Logger LOG = Logger.getLogger(Controller.class);

    private boolean init = false;

    SchuelerturnierTransportControllerImpl cont;

    ApplicationContext applicationContext;

    private static Controller INSTANCE = null;

    @FXML
    private Button beenden;

    @FXML
    private CheckBox drucken;

    @FXML
    private Button verbinden;

    @FXML
    private Rectangle verbunden;

    @FXML
    private TextField urltext;

    @FXML
    private TextField urltextmy;

    @FXML
    private ProgressIndicator progress;

    @FXML
    private TableView<File> table;

    ContextMenu menu = new ContextMenu();

    public Controller() {
        INSTANCE = this;
    }
    private void init() {
        if (this.init) {
            return;
        }
        MenuItem oeffnenMenue = new MenuItem("Öffnen");
        oeffnenMenue.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                File item = table.getItems().get(table.getSelectionModel().getSelectedIndex());
                if (item != null) {

                    open(item);

                }
            }
        });

        MenuItem druckenMenu = new MenuItem("Drucken");
        druckenMenu.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                File item = table.getItems().get(table.getSelectionModel().getSelectedIndex());
                if (item != null) {
                    drucken(item);
                }
            }
        });

        menu.getItems().addAll(oeffnenMenue);
        menu.getItems().addAll(druckenMenu);

        table.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouse) {
                if (mouse.getButton() == MouseButton.SECONDARY) {
                    //add some menu items here
                    menu.show(table, mouse.getScreenX(), mouse.getScreenY());
                } else {
                    if (menu != null) {
                        menu.hide();
                    }
                }
            }
        });


        this.init = true;
    }

    public void open(File item) {
        try {
            java.io.File f = java.io.File.createTempFile("tmp", ".pdf");

            IOUtils.write(item.getContent(), new FileOutputStream(f));
            f.deleteOnExit();

            if (System.getProperties().getProperty("os.name").contains("Mac")) {
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open",
                        f.getAbsolutePath()});
            } else {
                Runtime.getRuntime()
                        .exec("rundll32 url.dll,FileProtocolHandler " + f.getAbsolutePath());
            }

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }



    public static Controller getInstance() {
        return INSTANCE;
    }

    @FXML
    private void handleButtonBeendenAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleButtonVerbindenAction(ActionEvent event) {
        if (verbinden.getText().equals("Trennen")) {
            verbinden.setText("Verbinden");
            verbunden.setFill(Paint.valueOf("RED"));
            urltext.setDisable(false);
            cont.shutdown();

        } else {

            String url = "http://" + urltext.getText() + "/app/transport";

            cont = TransportControllerFactory.getInstance().createController(urltextmy.getText(), url);

            // filter nach File setzen
            cont.getMessagefilter().add(File.class.getCanonicalName());

            verbinden.setText("Trennen");
            verbunden.setFill(Paint.valueOf("GREEN"));
            urltext.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void onApplicationEvent(final IncommingMessage event) {

        init();
        File file = (File) event.getPayload();

        if (table.getItems() == null) {
            ObservableList<File> items = FXCollections.observableArrayList();
            table.setItems(items);
        }

        if (this.drucken.isSelected()) {
            drucken(file);
        } else {
            table.getItems().add(0, file);
        }

        LOG.debug("incomming message: " + file);

    }

    private void drucken(File file) {

        file.updateDruckzeit();

        if (table.getItems().contains(file)) {
            table.getItems().remove(file);
        }
        table.getItems().add(0, file);

        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;

        ByteArrayInputStream bin = new ByteArrayInputStream(file.getContent());

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
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
