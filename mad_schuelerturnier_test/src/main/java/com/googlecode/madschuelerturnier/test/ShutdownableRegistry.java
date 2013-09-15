package com.googlecode.madschuelerturnier.test;

import com.googlecode.madschuelerturnier.test.matchonly.MatchOnly;
import com.googlecode.madschuelerturnier.test.matchonly.SeleniumSpeakerThread;
import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 04.01.13
 * Time: 23:29
 * To change this template use File | Settings | File Templates.
 */
public class ShutdownableRegistry {

    //SeleniumDriverWrapper ut = new SeleniumDriverWrapper();

    private static final Logger LOG = Logger.getLogger(ShutdownableRegistry.class);

    private static ShutdownableRegistry INSTANCE;

    private final File folder = new File("/temp/");

    private boolean mailSendt = false;

    private Map<Typen, Shutdownable> shutdownlist = new HashMap<Typen, Shutdownable>();

    private ShutdownThread sd;

    private boolean failed = true;
    private boolean down = false;
    private String message = "";

    public void addShutdownable(Shutdownable sd) {
        LOG.info("shutdownable registred: " + sd.getFileExtension());
        shutdownlist.put(sd.getFileExtension(), sd);
    }

    public Shutdownable getShutdownableByKey(Typen type) {
        return this.shutdownlist.get(type);
    }


    private ShutdownableRegistry() {

    }

    public static synchronized ShutdownableRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShutdownableRegistry();
        }
        return INSTANCE;
    }

    public void shutdownReal() {


        LOG.info("shutdown: " + shutdownlist.keySet());
        for (Typen key : shutdownlist.keySet()) {
            LOG.info("shutdown: " + key);
            shutdownlist.get(key).shutdown();
            LOG.info("shutdown: " + key + " ok");
        }
        LOG.info("shutdown: send mail");
        this.sendMail(message);
        LOG.info("shutdown: mail ok, sleep 30s");
        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("shutdown: sleep ok");
        int ret = 0;
        if (failed) {
            ret = 1;
        }
        System.exit(ret);
    }


    public synchronized void shutdown(boolean failed, String mailSubject) {

        if (down) {
            return;
        }

        // hack
        SeleniumSpeakerThread th = new SeleniumSpeakerThread();
        th.run();

        this.message = mailSubject;
        this.failed = failed;
        this.sendMail(message);
        String[] temp = null;
        MatchOnly.main(temp);
        // this.sd = new ShutdownThread();
        // down = true;
        // sd.start();
    }


    public synchronized void sendMail(String subject) {

        if (mailSendt) {
            return;
        }
        //
        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.gmail.com");
        email.setSslSmtpPort("465");
        email.setSSL(true);

        String[] ext = {"png", "log", "txt"};
        Collection<File> files = FileUtils.listFiles(folder, ext, false);
        for (File file : files) {

            try {
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(file.getPath());
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription(file.getName());
                attachment.setName(file.getName());
                email.attach(attachment);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }

        }

        try {
            try {
                email.setAuthentication("marthaler.worb@gmail.com", FileUtils.readFileToString(new File("/schuetu/pw.sh")));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            email.addTo("marthaler.worb@gmail.com", "Daniel Marthaler");
            email.setFrom("jenkins@schuelerturnier-scworb.ch", "Jenkins");
            email.setSubject(subject);
            email.setMsg("best regards from schuetutest");
            // send the email
            email.send();

            try {
                FileUtils.deleteDirectory(folder);
                FileUtils.forceMkdir(folder);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }


        } catch (EmailException e) {
            LOG.error(e.getMessage(), e);
        }

        mailSendt = true;

    }

}
