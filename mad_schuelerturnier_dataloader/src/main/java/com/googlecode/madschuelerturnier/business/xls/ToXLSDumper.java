/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.xls;

import com.google.common.io.Resources;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielEinstellungenRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generiert einen XLS Export aus der Datenbank
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class ToXLSDumper {

    @Autowired
    private MannschaftRepository repo;

    @Autowired
    private SpielRepository srepo;

    @Autowired
    private SpielEinstellungenRepository seinst;

    private static final Logger LOG = Logger.getLogger(MannschaftRepository.class);

    public byte[] mannschaftenFromDBtoXLS() {
        return convertModelToXLS(repo.findAll(), srepo.findAll(), seinst.findAll());
    }

    public void dumpMOdelToXLSFile(List<Mannschaft> mannschaften, List<Spiel> spiele, File file) {
        try {
            byte[] wb = convertModelToXLS(mannschaften, spiele, null);
            FileOutputStream out = new FileOutputStream(file);
            out.write(wb);
            out.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private byte[] convertModelToXLS(List<Mannschaft> mannschaftenIn, List<Spiel> spieleIn, List<SpielEinstellungen> einstellungenIn) {

        List<SpielEinstellungen> einstellungen = einstellungenIn;
        List<Spiel> spiele = spieleIn;
        List<Mannschaft> mannschaften = mannschaftenIn;

        // SpielEinstellungen aufbereiten
        if (einstellungen == null) {
            einstellungen = new ArrayList();
        }

        if (spiele == null) {
            spiele = new ArrayList();
        }

        if (mannschaften == null) {
            mannschaften = new ArrayList();
        }

        Map beans = new HashMap();

        beans.put("mannschaften", mannschaften);
        beans.put("spiele", spiele);
        beans.put("einstellungen", einstellungen);

        XLSTransformer transformer = new XLSTransformer();
        byte[] arr = readFreshTemplate();
        try {

            InputStream is = new ByteArrayInputStream(arr);
            Workbook wb = WorkbookFactory.create(is);

            transformer.transformWorkbook(wb, beans);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            wb.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /*
    * Template neu lesen, falls nicht schon bereits ein Template uebergeben wurde (= nicht null ist)
    */
    private byte[] readFreshTemplate() {
        byte[] in = null;
        URL url = Resources.getResource("jxls-template.xls");
        try {
            in = Resources.toByteArray(url);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return in;
    }

}
