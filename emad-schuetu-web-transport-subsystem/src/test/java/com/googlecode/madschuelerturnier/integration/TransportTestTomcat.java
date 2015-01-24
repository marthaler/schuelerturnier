package com.googlecode.madschuelerturnier.integration;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;

public class TransportTestTomcat implements Runnable {

    private Tomcat tomcat;
    private Thread serverThread;
    private String folder;

    String webappPath = "";
    String targetPath = "";

    public TransportTestTomcat(int port, String contextPath, String folder) throws ServletException {
        this.folder = folder;
        tomcat = new Tomcat();
        tomcat.setPort(port);

        if (new File("src/test/webapp").exists()) {
            webappPath = "src/test/webapp";
            targetPath = "";
        } else {
            webappPath = "emad-schuetu-web-transport-subsystem/src/test/webapp";
            targetPath = "emad-schuetu-web-transport-subsystem/target/tomcat";
        }

        tomcat.addWebapp(contextPath, new File(webappPath).getAbsolutePath());

        tomcat.setBaseDir(targetPath + folder);


        serverThread = new Thread(this);

    }

    public void start() {
        serverThread.start();
    }

    public void run() {
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        tomcat.getServer().await();
    }

    public void stop() {
        try {
            tomcat.stop();
            tomcat.destroy();
            deleteDirectory(new File(targetPath + folder));
        } catch (Exception e) {
            // do nothing
        }
    }

    void deleteDirectory(File path) {
        if (path == null) return;
        if (path.exists()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                    f.delete();
                } else {
                    f.delete();
                }
            }
            path.delete();
        }
    }
}
