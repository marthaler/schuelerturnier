package com.googlecode.mad_schuelerturnier.business.picture;

import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 21.04.13
 * Time: 19:20
 * To change this template use File | Settings | File Templates.
 */

@Component
public class PictureAgent {

    public String picToCheck;

    private String selected;

    @Autowired
    private SpielRepository spielRepository;

    private static final Logger LOG = Logger.getLogger(PictureAgent.class);

    private boolean running = false;

    private boolean init = false;

    private String tempPath = "";
    private String schirizettel = "";

    private String printer = "brother";

    private Map<String, String> map = new HashMap();

    public void init(String path) {
        this.tempPath = path + "tempSchueri" + System.getProperty("file.separator");
        this.schirizettel = path + "schirizettel" + System.getProperty("file.separator");

        new File(tempPath).mkdirs();
        new File(schirizettel).mkdirs();

        LOG.info("tempPath erstellt: " + tempPath);
        LOG.info("schirizettel erstellt: " + schirizettel);

        this.running = true;
        this.init = true;

    }

    public boolean existiert(String id){
        return new File(schirizettel + id +".png").exists();
    }

    public void zuweisen(){
        try {

            if(this.selected != null){
                this.selected = this.selected.toLowerCase();
            } else{
                LOG.error("keine auswahl bei der schirizettelzuweisung");
            }

            File source = new File(tempPath + picToCheck);
            File dest = new File(schirizettel + this.selected + ".png");
            FileUtils.copyFile(source, dest);
            FileUtils.deleteQuietly(source);

            this.picToCheck = null;
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }

    }

    public boolean hasPicToCheck(){
        if(getPicToCheck() == null){
             return false;
        }
        return true;
    }

    public String getPicToCheck(){
        String ret = "";
        if(picToCheck == null){
            Collection<File> files = FileUtils.listFiles(new File(tempPath), null, false);
            for (File f : files) {
                if(f.getName().contains("Bildschirmfoto")){
                picToCheck =  f.getName();
                }
            }
        }

        if(picToCheck == null){
            return null;
        }

        ret = "resources/static/tempSchueri/" +  picToCheck;
        return ret;
    }

    @Scheduled(fixedDelay = 10000)
    private void check() {

        if (!init) {
            return;
        }

        Collection<File> files = FileUtils.listFiles(new File("/Users/dama/Desktop"), null, false);
        for (File f : files) {
            if (f.getName().contains("Bildschirmfoto")) {
                try {
                    FileUtils.copyFile(f, new File(this.tempPath + f.getName().replace(" ","")));
                    FileUtils.deleteQuietly(f);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }


        }
    }


    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
