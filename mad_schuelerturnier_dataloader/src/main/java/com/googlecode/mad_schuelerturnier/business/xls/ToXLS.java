package com.googlecode.mad_schuelerturnier.business.xls;

import com.google.common.io.Resources;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
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
        return convertMannschaftenToXLS(repo.findAll());
    }

    public void dumpMannschaftenToXLSFile(List<Mannschaft> mannschaften, File file) {
        try {
            byte[] wb = convertMannschaftenToXLS(mannschaften);
            FileOutputStream out = new FileOutputStream(file);
            out.write(wb);
            out.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private byte[] convertMannschaftenToXLS(List<Mannschaft> mannschaften) {

        Map beans = new HashMap();
        beans.put("mannschaften", mannschaften);
        XLSTransformer transformer = new XLSTransformer();
        byte[] arr = null;
        try {
            URL url = Resources.getResource("jxls-template.xls");
            try {
                arr = Resources.toByteArray(url);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }

            InputStream is = new ByteArrayInputStream(arr);
            Workbook wb = WorkbookFactory.create(is);

            transformer.transformWorkbook(wb, beans);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            wb.write(out);

            return out.toByteArray();

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } catch (InvalidFormatException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

}
