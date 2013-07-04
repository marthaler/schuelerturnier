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
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 04.07.13
 * Time: 21:43
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ToXLS {

    @Autowired
    private MannschaftRepository repo;

    private static final Logger LOG = Logger.getLogger(MannschaftRepository.class);

    public byte[] dumpMannschaftenFromDB() {
        return convert(repo.findAll());
    }


    public void dumpMannschaften(List<Mannschaft> mannschaften, String file) {

        try {
            byte[] wb = convert(mannschaften);

            FileOutputStream out = new FileOutputStream(file);
            out.write(wb);
            out.close();

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);

        }
    }

    private byte[] convert(List<Mannschaft> mannschaften) {

        Map beans = new HashMap();
        beans.put("mannschaften", mannschaften);
        XLSTransformer transformer = new XLSTransformer();
        byte[] arr = null;
        try {

            URL url = Resources.getResource("template-mannschaften.xls");
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
