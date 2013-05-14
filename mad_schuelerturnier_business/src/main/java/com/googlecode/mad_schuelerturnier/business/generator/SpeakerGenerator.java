package com.googlecode.mad_schuelerturnier.business.generator;

import com.googlecode.mad_schuelerturnier.business.controller.leiter.converter.HTMLOutConverter;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielZeilenRepository;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 28.12.12
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class SpeakerGenerator {

    private static final Logger LOG = Logger.getLogger(SpeakerGenerator.class);

    @Autowired
    SpielZeilenRepository repo;

    private static String folder;

    private String delim = System.getProperty("file.separator");

    @Async
    public void init(String path){

        folder = path + "sound" + delim;

        List<SpielZeile> zeilen = repo.findGruppenSpielZeilen();
        for (SpielZeile spielZeile : zeilen) {

            if(new File(folder+""+spielZeile.getId()+".mp3").exists()){
                 continue;
            }

            boolean ahere = false;
            boolean bhere = false;
            boolean chere = false;

            // files vorbereiten

            if(spielZeile.getA() != null){
            String urlA = generateString(spielZeile.getA(), "A");
            source(urlA,"a.mp3".replace("/","-"));
                ahere= true;
            }

            if(spielZeile.getB() != null){
            String urlB = generateString(spielZeile.getB(), "B");
            source(urlB,"b.mp3".replace("/","-"));
                bhere= true;
            }

            if(spielZeile.getC() != null){
            String urlC = generateString(spielZeile.getC(), "C");
            source(urlC,"c.mp3".replace("/","-"));
                chere= true;
            }

             FileUtils.deleteQuietly(new File(folder+""+spielZeile.getId()+".mp3"));


            if(ahere && bhere){
                mergeFile(folder + "a.mp3", folder + "b.mp3", folder + "d.mp3");
                if(chere){
                    mergeFile(folder + "d.mp3", folder + "c.mp3", folder + "e.mp3");
                }
            } else if(ahere && chere){
                mergeFile(folder + "a.mp3", folder + "c.mp3", folder + "e.mp3");
            } else if(bhere && chere){
                mergeFile(folder + "b.mp3", folder + "c.mp3", folder + "e.mp3");
            }else if(ahere){
                try {
                    FileUtils.moveFile(new File(folder +"a.mp3"),new File(folder+"e.mp3"));
                } catch (IOException e) {
                    LOG.error(e.getMessage(),e);
                }
            }  else if(bhere){
                try {
                    FileUtils.moveFile(new File(folder +"b.mp3"),new File(folder+"e.mp3"));
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            } else if(chere){
                try {
                    FileUtils.moveFile(new File(folder +"c.mp3"),new File(folder+"e.mp3"));
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }

            try {
                if(new File(folder +"e.mp3").exists()) {
                FileUtils.moveFile(new File(folder +"e.mp3"),new File(folder+""+spielZeile.getId()+".mp3"));
                FileUtils.deleteQuietly(new File(folder +"a.mp3"));
                FileUtils.deleteQuietly(new File(folder +"b.mp3"));
                FileUtils.deleteQuietly(new File(folder +"c.mp3"));
                FileUtils.deleteQuietly(new File(folder +"d.mp3"));
                FileUtils.deleteQuietly(new File(folder +"e.mp3"));
            }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }

        }
    }

    public void mergeFile(String file1, String file2, String toFile){
        try {
            File f1 = new File(file1);
            File f2 = new File(file2);
            File f3 = new File(toFile);

            FileInputStream fin1 = new FileInputStream(f1);
            FileInputStream fin2 = new FileInputStream(f2);

            FileOutputStream fout = new FileOutputStream(f3);

            int length;
            byte[] buff = new byte[8000];
            while ( (length = fin1.read(buff)) > 0){
                fout.write(buff, 0, length);
            }
            fin1.close();

            while ( (length = fin2.read(buff)) > 0){
                fout.write(buff, 0, length);
            }
            fin2.close();
            fout.close();
        }
        catch (Exception e){
            LOG.error(e.getMessage(), e);
        }
    }

    private String generateString(Spiel spiel, String platz) {

        String temp = "Auf Platz "+platz+". Bereitmachen. " + spiel.getMannschaftAName() + " gegen " + spiel.getMannschaftBName();
        temp = temp.replace("K", "K. ");
        temp = temp.replace("M", "M. ");

        try {
            temp = URLEncoder.encode(temp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.fatal(e.getMessage(),e);
        }
        temp = "http://translate.google.com/translate_tts?tl=de&q="  + temp;
        return temp;
    }

    public static void source(String urlSite, String file) {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Test Client");
        client.getParams().setParameter("http.connection.timeout",new Integer(5000));

        GetMethod method  = new GetMethod();
        FileOutputStream fos = null;

        try {

            method.setURI(new URI(urlSite, true));
            int returnCode = client.executeMethod(method);

            if(returnCode != HttpStatus.SC_OK) {
                LOG.error("Unable to fetch image, status code: " + returnCode);

            }

            byte[] imageData = method.getResponseBody();
            fos = new FileOutputStream(new File( folder + file));
            fos.write(imageData);

        } catch (Exception he) {
            System.err.println(he);

        } finally {
            method.releaseConnection();
            if(fos != null) try { fos.close(); } catch (Exception fe) {}
        }

    }

    public static String getFolder() {
        return folder;
    }
}
