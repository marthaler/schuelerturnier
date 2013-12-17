/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.xls;

import com.googlecode.madschuelerturnier.model.*;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import net.sf.jxls.reader.ReaderBuilder;
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

    public SpielEinstellungen convertXLSToEinstellung(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-einstellungen-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);

            List einstellungen = new ArrayList();
            SpielEinstellungen einstellung = new SpielEinstellungen();

            Map beans = new HashMap();
            beans.put("einstellungen", einstellungen);
            beans.put("einstellunge", einstellung);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);

            LOG.info(JXLS_LESESTATUS + readStatus.isStatusOK());

            List<SpielEinstellungen> einst = (List<SpielEinstellungen>) beans.get("einstellungen");

            return einst.get(0);

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

}
