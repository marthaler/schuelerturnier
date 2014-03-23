/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.scanner;


import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class ScannerAgent {

    private static final int DEFALUTWERTEFAKTOR = 7;

    private static final int TRIGGERDELAY = 6 * 1000;

    private static final Logger LOG = Logger.getLogger(ScannerAgent.class);
    public static final String PROTOCOLL = "http://";


    private boolean geradeamGehen = false;

    private String imageUrl = "192.168.2.3";

    private boolean running = true;

    private boolean init = false;

    private boolean deleteGarbage = false;

    private int wartefaktor = DEFALUTWERTEFAKTOR;

    private String path = "/";

    private String schirizettel = "/";

    @Autowired
    private SpielRepository spielRepository;

    public void init(String path) {
        this.schirizettel = path + "schirizettel";
        this.path = path + "scannbilder";
        boolean erstellt = new File(this.path).mkdirs();
        LOG.info("scannbilder erstellt: " + path + " -> " + erstellt);
        this.init = true;
    }

    public void reset() {
        try {
            this.geradeamGehen = true;
            openConnection(PROTOCOLL + imageUrl + "/decoder_control.cgi?command=31");
            Thread.sleep(wartefaktor * 1000 * 2);
            this.geradeamGehen = false;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Scheduled(fixedDelay = TRIGGERDELAY) //NOSONAR
    private void trigger() {
        if (!init || !running) {
            return;
        }
        scann();
    }

    public void doNow() { //NOSONAR
        String image = null;
        try {
            image = saveImage();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        process(image);
    }

    public void scann() {

        if (this.geradeamGehen) {
            return;
        }

        this.geradeamGehen = true;

        List<String> files = new ArrayList<String>();

        try {

            openConnection(PROTOCOLL + imageUrl + "/decoder_control.cgi?command=33");

            Thread.sleep(wartefaktor * 1000);
            files.add(saveImage());
            openConnection(PROTOCOLL + imageUrl + "/decoder_control.cgi?command=35");
            Thread.sleep(wartefaktor * 1000);
            files.add(saveImage());
            openConnection(PROTOCOLL + imageUrl + "/decoder_control.cgi?command=37");
            Thread.sleep(wartefaktor * 1000);
            files.add(saveImage());
            openConnection(PROTOCOLL + imageUrl + "/decoder_control.cgi?command=39");
            Thread.sleep(wartefaktor * 1000);

            openConnection(PROTOCOLL + imageUrl + "/decoder_control.cgi?command=31");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }


        for (String file : files) {

            process(file);

        }
        this.geradeamGehen = false;
    }

    private void process(String file) {
        GryscaleConverter.convertToGray(file, file);
        Cropper.crop(file, file, 0, 180, 610, 280);
        String res = BarcodeDecoder.decode(file);

        if (res.length() == 2) {
            Spiel sp = this.spielRepository.findSpielByIdString(res);
            try {
                FileUtils.moveFile(new File(file), new File(schirizettel + System.getProperty("file.separator") + sp.getId() + ".png"));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        if (this.deleteGarbage) {
            FileUtils.deleteQuietly(new File(file));
        }
    }

    public String saveImage() throws IOException {

        String imageUrlN = PROTOCOLL + imageUrl + "/snapshot.cgi";

        String destinationFile = path + System.getProperty("file.separator") + System.currentTimeMillis() + ".png";

        new File(destinationFile).createNewFile();

        URL url = new URL(imageUrlN);
        URLConnection uc = url.openConnection();
        String userpass = "admin" + ":" + "";
        String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
        uc.setRequestProperty("Authorization", basicAuth);

        InputStream is = uc.getInputStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
        return destinationFile;
    }

    public void openConnection(String imageUrl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(imageUrl);
            URLConnection uc = url.openConnection();
            String userpass = "admin" + ":" + "";
            String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            uc.setRequestProperty("Authorization", basicAuth);
            is = uc.getInputStream();
        } catch (Exception e) {
            LOG.debug("kameraverbindung nicht moeglich");
            this.running = false;
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    public boolean isDeleteGarbage() {
        return deleteGarbage;
    }

    public void setDeleteGarbage(boolean deleteGarbage) {
        this.deleteGarbage = deleteGarbage;
    }

    public int getWartefaktor() {
        return wartefaktor;
    }

    public void setWartefaktor(int wartefaktor) {
        this.wartefaktor = wartefaktor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}