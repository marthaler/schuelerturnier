/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.SeleniumEintragerThread;
import com.googlecode.madschuelerturnier.SeleniumKontrolliererThread;
import com.googlecode.madschuelerturnier.SeleniumSpeakerThread;
import com.googlecode.madschuelerturnier.SeleniumWebcamThread;
import org.apache.log4j.Logger;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.springframework.stereotype.Component;

/**
 * Controller mit Session Scope, welcher den Testprozess durchführt
 *
 * @author marthaler.worb@gmail.com
 * @since 1.3.0
 */
@Component
public class BackingBean {

    private static final Logger LOG = Logger.getLogger(BackingBean.class);

    private byte[] game = null;

    private int tab;

    private String methode = "absteigend";
    private String speaker_url = "http://87.230.15.247";
    private String speaker_password = "1234";
    private String speaker_user = "tester1915speaker";
    private boolean speaker_on = true;

    private String eintrager_url = "http://87.230.15.247";

    private String eintrager_password = "1234";
    private String eintrager_user = "tester1915eintrager";
    private boolean eintrager_on = true;

    private String kontrollierer_url = "http://87.230.15.247";

    private String kontrollierer_password = "1234";
    private String kontrollierer_user = "tester1915kontrollierer";
    private boolean kontrollierer_on = true;

    private String webcam_url = "http://localhost:8082";
    private boolean webcam_on = true;

    private SeleniumSpeakerThread speaker;
    private SeleniumEintragerThread eintrager;
    private SeleniumKontrolliererThread kontrollierer;
    private SeleniumWebcamThread webcam;

    public boolean hasGame() {
        if (game == null) {
            return false;
        }
        return true;
    }

    public void onTabChange(TabChangeEvent event) {
        String tab = event.getTab().getId();
        TabView view = (TabView) event.getTab().getParent();
        for (int i = 0; i < view.getChildCount(); i++) {
            if (tab.equals(view.getChildren().get(i).getId())) {
                this.tab = i;
            }
        }
    }

    public void start() {

        if (speaker_on) {
            speaker = new SeleniumSpeakerThread(speaker_user, speaker_password, speaker_url);
            speaker.start();
        }

        if (eintrager_on) {
            eintrager = new SeleniumEintragerThread(eintrager_user, eintrager_password, eintrager_url, this.methode, this.game);
            eintrager.start();
        }

        if (kontrollierer_on) {
            kontrollierer = new SeleniumKontrolliererThread(kontrollierer_user, kontrollierer_password, kontrollierer_url);
            kontrollierer.start();
        }

        if (webcam_on) {
            webcam = new SeleniumWebcamThread(webcam_url);
            webcam.start();
        }
    }

    public void stop() {
        speaker.shutDown();
        eintrager.shutDown();
        kontrollierer.shutDown();
        webcam.shutDown();
    }

    public void handleFileUpload(FileUploadEvent event) {

        game = event.getFile().getContents();

    }

    public String getSpeaker_url() {
        return speaker_url;
    }

    public void setSpeaker_url(String speaker_url) {
        this.speaker_url = speaker_url;
    }

    public String getSpeaker_password() {
        return speaker_password;
    }

    public void setSpeaker_password(String speaker_password) {
        this.speaker_password = speaker_password;
    }

    public String getSpeaker_user() {
        return speaker_user;
    }

    public void setSpeaker_user(String speaker_user) {
        this.speaker_user = speaker_user;
    }

    public boolean isSpeaker_on() {
        return speaker_on;
    }

    public void setSpeaker_on(boolean speaker_on) {
        this.speaker_on = speaker_on;
    }

    public String getEintrager_url() {
        return eintrager_url;
    }

    public void setEintrager_url(String eintrager_url) {
        this.eintrager_url = eintrager_url;
    }

    public String getEintrager_password() {
        return eintrager_password;
    }

    public void setEintrager_password(String eintrager_password) {
        this.eintrager_password = eintrager_password;
    }

    public String getEintrager_user() {
        return eintrager_user;
    }

    public void setEintrager_user(String eintrager_user) {
        this.eintrager_user = eintrager_user;
    }

    public boolean isEintrager_on() {
        return eintrager_on;
    }

    public void setEintrager_on(boolean eintrager_on) {
        this.eintrager_on = eintrager_on;
    }

    public String getKontrollierer_url() {
        return kontrollierer_url;
    }

    public void setKontrollierer_url(String kontrollierer_url) {
        this.kontrollierer_url = kontrollierer_url;
    }

    public String getKontrollierer_password() {
        return kontrollierer_password;
    }

    public void setKontrollierer_password(String kontrollierer_password) {
        this.kontrollierer_password = kontrollierer_password;
    }

    public String getKontrollierer_user() {
        return kontrollierer_user;
    }

    public void setKontrollierer_user(String kontrollierer_user) {
        this.kontrollierer_user = kontrollierer_user;
    }

    public boolean isKontrollierer_on() {
        return kontrollierer_on;
    }

    public void setKontrollierer_on(boolean kontrollierer_on) {
        this.kontrollierer_on = kontrollierer_on;
    }

    public String getWebcam_url() {
        return webcam_url;
    }

    public void setWebcam_url(String webcam_url) {
        this.webcam_url = webcam_url;
    }

    public boolean isWebcam_on() {
        return webcam_on;
    }

    public void setWebcam_on(boolean webcam_on) {
        this.webcam_on = webcam_on;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

}
                        