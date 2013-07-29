package com.googlecode.mad_schuelerturnier.business.xls;

import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
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
 * Generiert die Daten aus einem XLS
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class FromXLS {

    private static final Logger LOG = Logger.getLogger(MannschaftRepository.class);

    public List<Mannschaft> convertXLSToMannschaften(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-mannschaft-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);
            Mannschaft mannschaft = new Mannschaft();

            List mannschaften = new ArrayList();
            Map beans = new HashMap();
            beans.put("mannschaften", mannschaften);
            beans.put("mannschaft", mannschaft);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);

            LOG.info("jxls lesestatus: " + readStatus.isStatusOK());

            return (List<Mannschaft>) beans.get("mannschaften");

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

}
