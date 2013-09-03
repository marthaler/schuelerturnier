/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.business.xls.ToXLSDumper;
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

    @Autowired
    private ToXLSDumper xml;

    public StreamedContent getFile() {
        ByteArrayInputStream stream = new ByteArrayInputStream(xml.mannschaftenFromDBtoXLS());
        return new DefaultStreamedContent(stream, "application/msexcel", "schuetu.xls");
    }
}
                    