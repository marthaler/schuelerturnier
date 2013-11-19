package com.googlecode.madschuelerturnier.business;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;

/**
 *
 * @author dama
 */
public class App extends javafx.application.Application {


    @Override
    public void start(Stage stage) throws Exception {

        File f1 =   new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        System.out.println(f1.getAbsolutePath());

        String [] path = f1.getAbsolutePath().split("/");

        String neu ="/";
        for(int i = 0; i < path.length -1; i++){
            neu = neu + path[i] + "/";
        }

        System.out.println(neu);

        File ft = new File(neu + "/lib");

        File[] fl = ft.listFiles();



        System.out.println("->" + fl[0]);

        java.util.jar.JarFile jar = new java.util.jar.JarFile(fl[0]);
        java.util.Enumeration en = jar.entries();
        while (en.hasMoreElements()) {
            java.util.jar.JarEntry file = (java.util.jar.JarEntry) en.nextElement();
            if(!file.getName().contains(".wa")){
               System.out.println("ja")  ;
               continue;
            }
            java.io.File f = new java.io.File("/temp/test" + java.io.File.separator + file.getName());
            if (file.isDirectory()) { // if its a directory, create it
                f.mkdir();
                continue;
            }
            java.io.InputStream is = jar.getInputStream(file); // get the input stream
            java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
            while (is.available() > 0) {  // write contents of 'is' to 'fos'
                fos.write(is.read());
            }
            fos.close();
            is.close();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/form.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);

        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
