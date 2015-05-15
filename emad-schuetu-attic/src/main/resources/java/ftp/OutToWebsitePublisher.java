/**
 * Apache License 2.0
 */
package ftp;

import com.googlecode.madschuelerturnier.business.zeit.ZeitPuls;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class OutToWebsitePublisher implements ApplicationListener<ZeitPuls> {

    private static final Logger LOG = Logger.getLogger(OutToWebsitePublisher.class);

    private static final String DATE_MARKER = "[dateTime]";

    private boolean ftpEin = false;

    @Value("${ftp.server}")
    private String ftpServer = "";
    @Value("${ftp.port:21}")
    private int ftpPort;
    @Value("${ftp.folder}")
    private String ftpUnterordner = "";
    @Value("${ftp.user}")
    private String ftpUser = "";
    @Value("${ftp.password}")
    private String ftpPassword = "";
    @Value("${ftp.dateformat}")
    private String ftpDateFormat = "";

    private String ftpPath = null;

    private final Map<String, String> map = new HashMap<String, String>();
    private final Map<String, String> mapPublished = new HashMap<String, String>();

    private ZeitPuls zeit = null;

    public OutToWebsitePublisher() {
        OutToWebsitePublisher.LOG.info("OutToWebsitePublisher: start");
        this.map.put("info.txt", new Date().toString());
    }

    public void init(String path) {
        this.ftpPath = path + "ftp" + System.getProperty("file.separator");

        boolean created = new File(ftpPath).mkdirs();

        LOG.info("ftpPath erstellt: " + ftpPath + " ->" + created);

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
                        content = content.replace(OutToWebsitePublisher.DATE_MARKER, sdf.format(new Date()));

                        final OutToWebsiteSender p = new OutToWebsiteSender(file, content, ftpUnterordner, ftpServer, ftpPort, ftpUser, ftpPassword);
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
                    content = content.replace(OutToWebsitePublisher.DATE_MARKER, sdf.format(new Date()));

                    final OutToWebsiteSender p = new OutToWebsiteSender(file, content, ftpUnterordner, ftpServer, ftpPort, ftpUser, ftpPassword);
                    try {
                        p.join(15000);
                    } catch (final InterruptedException e) {
                        OutToWebsitePublisher.LOG.error(e.getMessage(), e);
                    }
                    boolean ok;

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

    public void addPage(final String file, final String content) {
        this.map.put(file.replace("src/main/resources", ".").toLowerCase(), content);

        if (this.ftpEin) {
            try {
                FileUtils.writeStringToFile(new File(this.ftpPath + file.toLowerCase()), content);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public void reconnect() {
        OutToWebsiteSender.down = false;
    }


    public void onApplicationEvent(ZeitPuls event) {
        if (zeit == null) {
            zeit = event;
            // < 2012, also ist es eine testdurchfuehrung, somit wird immer ein neuer unterordner erstellt
            if (zeit.getSpielZeit().getYear() < 2012) {
                this.ftpUnterordner = "" + zeit.getEchteZeit().toDate().toString().replace(" ", "_").toLowerCase().replace(":", "-");
            }
        }
    }
}