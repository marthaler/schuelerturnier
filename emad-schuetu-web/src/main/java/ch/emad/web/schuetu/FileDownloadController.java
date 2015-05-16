/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu;

import ch.emad.business.schuetu.Business;
import ch.emad.business.schuetu.xls.ToXLSDumper2;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * Ermoeglicht das Exportieren der ganzen Spieldaten als Excel-File
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class FileDownloadController {

    private static final Logger LOG = Logger.getLogger(FileDownloadController.class);

    public FileDownloadController() {
        LOG.info("Instanziert: FileDownloadController");
    }

    @Autowired
    private ToXLSDumper2 xml;

    @Autowired
    private Business business;

    public StreamedContent getFile() {
        ByteArrayInputStream stream = new ByteArrayInputStream(xml.mannschaftenFromDBtoXLS(business.getSpielEinstellungen()));
        return new DefaultStreamedContent(stream, "application/msexcel", "schuetu.xls");
    }
}
                    