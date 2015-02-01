/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.xls;

import com.googlecode.madschuelerturnier.model.*;
import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.ReaderConfig;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generiert die Daten aus einem XLS-Dump
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class FromXLSLoader {

    private static final Logger LOG = Logger.getLogger(MannschaftRepository.class);
    public static final String JXLS_LESESTATUS = "jxls lesestatus: ";

    public List<Mannschaft> convertXLSToMannschaften(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-mannschaft-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);

            List mannschaften = new ArrayList();
            Mannschaft mannschaft = new Mannschaft();

            Map beans = new HashMap();
            beans.put("mannschaften", mannschaften);
            beans.put("mannschaft", mannschaft);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);

            LOG.info(JXLS_LESESTATUS + readStatus.isStatusOK());

            return (List<Mannschaft>) beans.get("mannschaften");

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public List<Spiel> convertXLSToSpiele(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-spiel-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);

            List spiele = new ArrayList();
            Spiel spiel = new Spiel();

            Map beans = new HashMap();
            beans.put("spiele", spiele);
            beans.put("spiel", spiel);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);

            LOG.info(JXLS_LESESTATUS + readStatus.isStatusOK());

            return (List<Spiel>) beans.get("spiele");

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public List<Korrektur> convertXLSToKorrektur(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-korrektur-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);

            List korrekturen = new ArrayList();
            Korrektur korrektur = new Korrektur();

            Map beans = new HashMap();
            beans.put("korrekturen", korrekturen);
            beans.put("korrektur", korrektur);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);

            LOG.info(JXLS_LESESTATUS + readStatus.isStatusOK());

            return (List<Korrektur>) beans.get("korrekturen");


        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public List<DBAuthUser> convertXLSToDBAuthUsers(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-user-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);

            List users = new ArrayList();
            DBAuthUser user = new DBAuthUser();

            Map beans = new HashMap();
            beans.put("users", users);
            beans.put("user", user);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);

            LOG.info(JXLS_LESESTATUS + readStatus.isStatusOK());

            return (List<DBAuthUser>) beans.get("users");


        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public List<File> convertXLSToFiles(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-attachement-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);

            List files = new ArrayList();
            File file = new File();

            Map beans = new HashMap();
            beans.put("attachements", files);
            beans.put("attachement", file);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);

            LOG.info(JXLS_LESESTATUS + readStatus.isStatusOK());

            return (List<File>) beans.get("attachements");

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public List<Text> convertXLSToTexte(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-text-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);

            List texte = new ArrayList();
            Text text = new Text();

            Map beans = new HashMap();
            beans.put("texte", texte);
            beans.put("text", text);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);

            LOG.info(JXLS_LESESTATUS + readStatus.isStatusOK());

            return (List<Text>) beans.get("texte");

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public List<Penalty> convertXLSToPenalty(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-penalty-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);

            List penaltys = new ArrayList();
            Penalty penalty = new Penalty();

            Map beans = new HashMap();
            beans.put("penaltys", penaltys);
            beans.put("penalty", penalty);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);

            LOG.info(JXLS_LESESTATUS + readStatus.isStatusOK());

            return (List<Penalty>) beans.get("penaltys");

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public List<Kontakt> convertXLSToKontakt(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-kontakt-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);

            List kontakte = new ArrayList();
            Kontakt kontakt = new Kontakt();

            Map beans = new HashMap();
            beans.put("kontakte", kontakte);
            beans.put("kontakt", kontakt);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);

            LOG.info(JXLS_LESESTATUS + readStatus.isStatusOK());

            return (List<Kontakt>) beans.get("kontakte");

        } catch (Exception e) {
           e.printStackTrace();
            LOG.error(e.getMessage(), e);
        }

        return null;
    }


}
