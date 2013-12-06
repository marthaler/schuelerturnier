package com.googlecode.madschuelerturnier.business.dropbox;

import com.dropbox.core.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Stellt die Verbindung zum gesharten Dropbox Ordner her
 * Liest und Speichert Files aus diesem
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component
public class DropboxConnectorImpl implements DropboxConnector {

    private static final Logger LOG = Logger.getLogger(DropboxConnectorImpl.class);

    @Value("${dropbox.key:zpay56b7nn29idg}")
    private final String APP_KEY = "zpay56b7nn29idg";
    @Value("${dropbox.secret:jya3m54g1ximgn9}")
    private final String APP_SECRET = "jya3m54g1ximgn9";

    @Value("${dropbox.folder:/shared/z_schuelerturnier/informatik/test}")
    private String rootFolder ="/shared/z_schuelerturnier/informatik/test";

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
            config = new DbxRequestConfig("Schuelerturnier/1.2.8", Locale.getDefault().toString());
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
                result.add(child.name);
            }
        } catch (DbxException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public byte[] loadFile(String file) {
        LOG.info("DropboxConnectorImpl: will load file -> " + file);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            DbxEntry.File downloadedFile = client.getFile(rootFolder +"/" + file, null,outputStream);
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
}
