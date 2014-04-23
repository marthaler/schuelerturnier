/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

package com.googlecode.madschuelerturnier.business.dropbox;

import com.dropbox.core.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Stellt die Verbindung zum gesharten Dropbox Ordner her
 * Liest und Speichert Files aus diesem
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component("localDropboxConnector")
public class DropboxConnectorImpl implements DropboxConnector {

    private String selectedGame = "";

    private static final Logger LOG = Logger.getLogger(DropboxConnectorImpl.class);

    @Value("${dropbox.key:zpay56b7nn29idg}")
    private final String APP_KEY = "zpay56b7nn29idg";
    @Value("${dropbox.secret:jya3m54g1ximgn9}")
    private final String APP_SECRET = "jya3m54g1ximgn9";

    @Value("${dropbox.folder:/shared/z_schuelerturnier/informatik/applikationsdaten}")
    private String rootFolder = "/shared/z_schuelerturnier/informatik/applikationsdaten";

    private DbxClient client;

    private String authorizeUrl = null;

    private DbxWebAuthNoRedirect webAuth = null;

    private DbxRequestConfig config = null;


    @Override
    public Boolean isConnected() {
        if (client == null) {
            return false;
        }
        return true;
    }

    @Override
    public String getLoginURL() {
        if (authorizeUrl == null) {
            DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
            config = new DbxRequestConfig("Schuelerturnier/1.3.0", Locale.getDefault().toString());
            webAuth = new DbxWebAuthNoRedirect(config, appInfo);
            authorizeUrl = webAuth.start();
        }
        return authorizeUrl;
    }

    @Override
    public void insertToken(String token) {
        DbxAuthFinish authFinish = null;
        if (webAuth != null) {
            try {
                authFinish = webAuth.finish(token);
                client = new DbxClient(config, authFinish.accessToken);
                LOG.info("DropboxConnectorImpl: verbindung ok -> " + client.getAccountInfo().displayName);
            } catch (DbxException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public List<String> getFilesInFolder() {
        List<String> result = new ArrayList<String>();
        try {
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(this.rootFolder);
            for (DbxEntry child : listing.children) {
                if (child.isFile()) {
                    result.add(child.name);
                }
            }
        } catch (DbxException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    public List<String> getFilesInAltFolder() {

        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return null;
        }

        List<String> result = new ArrayList<String>();
        try {
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(this.rootFolder + "/alt");
            for (DbxEntry child : listing.children) {
                if (child.isFile() && child.name.contains("xls")) {
                    result.add("/alt/" + child.name);
                }
            }
        } catch (DbxException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    public List<String> getFoldersInRootFolder() {

        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return null;
        }

        List<String> result = new ArrayList<String>();
        try {
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(this.rootFolder);
            for (DbxEntry child : listing.children) {
                if (child.isFolder() && !child.name.contains("alt")) {
                    result.add(child.name);
                }
            }
        } catch (DbxException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public byte[] loadFile(String file) {

        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return null;
        }

        LOG.info("DropboxConnectorImpl: will load file -> " + file);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            DbxEntry.File downloadedFile = client.getFile(rootFolder + "/" + file, null, outputStream);
            LOG.info("DropboxConnectorImpl: file loaded; size -> " + downloadedFile.humanSize);
            return outputStream.toByteArray();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    @Override
    public void saveFile(String file, byte[] content) {

        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return;
        }

        LOG.info("DropboxConnectorImpl: upload file -> " + file);
        ByteArrayInputStream inputFile = new ByteArrayInputStream(content);

        try {
            client.delete(rootFolder + "/" + file);
        } catch (DbxException e) {
            LOG.debug("fehler beim loeschen eines files: " + e.getMessage());
        }

        try {

            DbxEntry.File uploadedFile = client.uploadFile(rootFolder + "/" + file, DbxWriteMode.add(), content.length, inputFile);
            LOG.info("DropboxConnectorImpl: upload ok; size -> " + uploadedFile.humanSize);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (inputFile != null) {
                try {
                    inputFile.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public byte[] selectGame(String folder) {
        this.selectedGame = folder;
        return this.loadFile(this.selectedGame + "/" + this.selectedGame + ".xls");
    }

    @Override
    public List<String> getAllGames() {
        return getFoldersInRootFolder();
    }

    public String getSelectedGame() {
        return selectedGame;
    }

    @Override
    public byte[] loadGameAttachemt(String file) {

        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return null;
        }

        DbxEntry.WithChildren listing = null;
        try {
            listing = client.getMetadataWithChildren(this.rootFolder + "/" + this.selectedGame + "/attachements");
        } catch (DbxException e) {
            LOG.error(e.getMessage(), e);
        }

        for (DbxEntry child : listing.children) {
            if (child.isFile() && child.name.contains(file)) {
                return this.loadFile(this.selectedGame + "/attachements/" + child.name);
            }
        }
        return null;
    }

    private String loadGameAttachemtMD5(String file) {

        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return null;
        }

        byte[] f = this.loadFile(this.selectedGame + "/attachements/md5/" + file + ".md5.txt");
        if (f != null) {
            return new String(f);
        }
        return null;
    }

    @Override
    public void saveGameAttachemt(String file, String suffix, byte[] content) {

        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return;
        }

        if (this.generateMD5(content).equals(loadGameAttachemtMD5(file))) {
            LOG.debug("brauche attachement nicht zu sichern inhalt ist gleich");
        } else {
            deleteGameAttachemt(file);
            this.saveFile(this.selectedGame + "/attachements/" + file + "." + suffix, content);
            this.saveFile(this.selectedGame + "/attachements/md5/" + file + ".md5.txt", generateMD5(content).getBytes());
        }
    }

    @Override
    public void deleteGameAttachemt(String file) {
        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return;
        }
        DbxEntry.WithChildren listing = null;

        try {
            listing = client.getMetadataWithChildren(this.rootFolder + "/" + this.selectedGame + "/attachements");

            if (listing == null) {
                LOG.debug("attachement ordner nicht vorhanden: loesch nicht");
                return;
            }

        } catch (DbxException e) {
            LOG.error(e.getMessage(), e);
        }
        try {
            for (DbxEntry child : listing.children) {
                if (child.isFile() && child.name.contains(file)) {
                    client.delete(rootFolder + "/" + this.selectedGame + "/attachements/" + child.name);
                    client.delete(rootFolder + "/" + this.selectedGame + "/attachements/md5/" + file + ".md5.txt");
                }
            }
        } catch (DbxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void saveGame(byte[] content) {
        this.saveFile(this.selectedGame + "/" + this.selectedGame + ".xls", content);
        this.saveFile("alt/" + this.selectedGame + ".xls", content);
    }

    @Override
    public void saveOldGame(String jahr,String content) {
        try {
            this.saveFile("alt/" + jahr + "-websitedump.xml", content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, String> loadOldGames() {
        Map<String,String> result = new HashMap<String,String>();
        try {
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(this.rootFolder + "/alt");
            for (DbxEntry child : listing.children) {
                if (child.isFile() && child.name.contains("xml")) {

                    LOG.info("DropboxConnectorImpl: will load OldGames files");
                    ByteArrayOutputStream outputStream = null;
                    try {
                        outputStream = new ByteArrayOutputStream();
                        DbxEntry.File downloadedFile = client.getFile(child.asFile().path, null, outputStream);
                        LOG.info("DropboxConnectorImpl: file loaded; size -> " + downloadedFile.humanSize);
                        String out = outputStream.toString("utf-8");
                        String[] arr = child.asFile().path.split("/");
                        String str = arr[arr.length-1].replace("-websitedump.xml","");
                        result.put(str,out);
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    } finally {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                LOG.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        } catch (DbxException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    private String generateMD5(byte[] args) {
        return DigestUtils.md5DigestAsHex(args);
    }

}
