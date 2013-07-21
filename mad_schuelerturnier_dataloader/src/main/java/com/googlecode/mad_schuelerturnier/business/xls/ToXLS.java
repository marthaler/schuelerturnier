package com.googlecode.mad_schuelerturnier.business.xls;

import com.google.common.io.Resources;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
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
public class ToXLS {

    @Autowired
    private MannschaftRepository repo;

    private static final Logger LOG = Logger.getLogger(MannschaftRepository.class);

    public byte[] mannschaftenFromDBtoXLS() {
        return convertModelToXLS(repo.findAll(), null, null);
    }

    public void dumpMOdelToXLSFile(List<Mannschaft> mannschaften, File file) {
        try {
            byte[] wb = convertModelToXLS(mannschaften, null, readFreshTemplate(null));
            FileOutputStream out = new FileOutputStream(file);
            out.write(wb);
            out.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private byte[] convertModelToXLS(List<Mannschaft> mannschaften, List<Spiel> spiele, byte[] template) {

        if (spiele == null) {
            spiele = new ArrayList();
        }
        if (mannschaften == null) {
            mannschaften = new ArrayList();
        }
        Map beans = new HashMap();
        beans.put("mannschaften", mannschaften);
        beans.put("spiele", spiele);
        XLSTransformer transformer = new XLSTransformer();
        byte[] arr = readFreshTemplate(template);
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
    private byte[] readFreshTemplate(byte[] in) {
        if (in != null) {
            return in;
        }
        URL url = Resources.getResource("jxls-template.xls");
        try {
            in = Resources.toByteArray(url);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return in;
    }

}
