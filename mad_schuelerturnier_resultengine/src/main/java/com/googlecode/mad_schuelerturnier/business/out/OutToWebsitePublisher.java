/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.out;

import com.googlecode.mad_schuelerturnier.business.zeit.ZeitPuls;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * mad letzte aenderung: $Date: 2012-01-11 22:50:30 +0100 (Mi, 11 Jan 2012) $
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @version $Revision: 158 $
 * @headurl $HeadURL: https://mad-schuelerturnier.googlecode.com/svn/trunk/mad_schuelereturnier/src/main/java/com/googlecode/mad_schuelerturnier/business/controller/out/OutToWebsitePublisher.java $
 */
@Component
public class OutToWebsitePublisher implements ApplicationListener<ZeitPuls> {

    private final static Logger LOG = Logger.getLogger(OutToWebsitePublisher.class);

    private static final String marker = "[ranglisten]";
    private static final String dateMarker = "[dateTime]";

    private boolean ftpEin = false;
    private boolean isTest = false;

    private String ftpServer = "ftp.schuelerturnier-scworb.ch";
    private int ftpPort = 21;
    private String ftpUnterordner = "2013";
    private String ftpUser = "turnier@schuelerturnier-scworb.ch";
    private String ftpPassword = "schuetu12";
    private String ftpDateFormat = "dd.MM.yyyy HH:mm:ss";

    private String ftpPath = null;


    private final Map<String, String> map = new HashMap<String, String>();
    private final Map<String, String> mapPublished = new HashMap<String, String>();

    ZeitPuls zeit = null;

    boolean folderhere = false;

    public OutToWebsitePublisher() {
        OutToWebsitePublisher.LOG.info("OutToWebsitePublisher: start");
        this.map.put("info.txt", new Date().toString());
    }

    public void init(String path) {
        this.ftpPath = path + "ftp" + System.getProperty("file.separator");

        new File(ftpPath).mkdirs();

        LOG.info("ftpPath erstellt: " + ftpPath);

        this.ftpEin = true;

    }

    @Scheduled(fixedRate = 1000 * 10)
    public void publish() {

        if (!ftpEin) {
            return;
        }

        final List<String> keyList = new ArrayList<String>();

        for (final String string : this.map.keySet()) {
            keyList.add(string);
        }

        for (final String file : keyList) {

            if (this.mapPublished.containsKey(file)) {
                if (!this.mapPublished.get(file).equals(this.map.get(file))) {

                    for (int i = 0; i < 10; i++) {

                        SimpleDateFormat sdf = new SimpleDateFormat(ftpDateFormat);
                        String content = this.map.get(file);
                        content = content.replace(OutToWebsitePublisher.dateMarker, sdf.format(new Date()));

                        try {
                            FileUtils.writeStringToFile(new File(this.ftpPath + file),content);
                        } catch (IOException e) {
                            LOG.error(e.getMessage(),e);
                        }

                        final OutToWebsiteSender p = new OutToWebsiteSender(file, content, ftpUnterordner, ftpServer, ftpPort, ftpUser, ftpPassword, isTest);
                        try {
                            p.join(15000);
                        } catch (final InterruptedException e) {
                            OutToWebsitePublisher.LOG.error(e.getMessage(), e);
                        }

                        boolean ok = p.isOk();

                        if (p.isTimeUp() && !ok) {
                            return;
                        }

                        if (ok) {
                            this.mapPublished.put(file, this.map.get(file));
                            break;
                        }
                    }
                }
            } else {
                for (int i = 0; i < 10; i++) {

                    SimpleDateFormat sdf = new SimpleDateFormat(ftpDateFormat);
                    String content = this.map.get(file);
                    content = content.replace(OutToWebsitePublisher.dateMarker, sdf.format(new Date()));

                    try {
                        FileUtils.writeStringToFile(new File(this.ftpPath + file),content);
                    } catch (IOException e) {
                       LOG.error(e.getMessage(),e);
                    }

                    final OutToWebsiteSender p = new OutToWebsiteSender(file, content, ftpUnterordner, ftpServer, ftpPort, ftpUser, ftpPassword, isTest);
                    try {
                        p.join(15000);
                    } catch (final InterruptedException e) {
                        OutToWebsitePublisher.LOG.error(e.getMessage(), e);
                    }
                    boolean ok = false;

                    ok = p.isOk();

                    if (p.isTimeUp() && !ok) {
                        return;
                    }

                    if (ok) {
                        this.mapPublished.put(file, this.map.get(file));
                        break;
                    }
                }
            }
        }
    }

    public void addPage(final String name, final String content) {
        this.map.put(name.replace("..", ".").toLowerCase(), content);
    }

    public void reconnect(){
        OutToWebsiteSender.DOWN = false;
    }


    public void onApplicationEvent(ZeitPuls event) {
        if (zeit == null) {
            zeit = event;
            // < 2012, also ist es eine testdurchfuehrung, somit wird immer ein neuer unterordner erstellt
            if (zeit.getSpielZeit().getYear() < 2012) {
                this.ftpUnterordner = "" + zeit.getEchteZeit().toDate().toString().replace(" ", "_").toLowerCase().replace(":", "-");
                this.isTest = true;
            }
        }
    }
}