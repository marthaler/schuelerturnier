/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.xls;

import ch.emad.model.schuetu.model.GPLer;
import ch.emad.persistence.schuetu.repository.MannschaftRepository;
import com.google.common.io.Resources;
import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
public class FromXLSLoader2 {

    private static final Logger LOG = Logger.getLogger(MannschaftRepository.class);
    public static final String JXLS_LESESTATUS = "jxls lesestatus: ";


    public List<GPLer> convertXLSToGPler(byte[] arr) {

        try {

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/jxls-gp-mapping.xml"));
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

            InputStream inputXLS = new ByteArrayInputStream(arr);

            List rechnungen = new ArrayList();
            Object rechnung = new GPLer();

            Map beans = new HashMap();
            beans.put("gplers", rechnungen);
            beans.put("gpler", rechnung);

            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
            System.out.println(readStatus.isStatusOK());
            LOG.info(JXLS_LESESTATUS + readStatus.isStatusOK());

            return (List<GPLer>) beans.get("gplers");

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public static byte[] readFile(String file) {
        byte[] in = null;
        URL url = Resources.getResource(file);
        try {
            in = Resources.toByteArray(url);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return in;
    }


}
