package com.googlecode.mad_schuelerturnier.web;

import com.googlecode.mad_schuelerturnier.business.xls.ToXLS;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class FileDownloadController {

    private static final Logger LOG = Logger.getLogger(MannschaftRepository.class);


    @Autowired
    private ToXLS xml;

    public StreamedContent getFile() {


        ByteArrayInputStream stream = new ByteArrayInputStream(xml.dumpMannschaftenFromDB());


        return new DefaultStreamedContent(stream, "application/msexcel", "schuetu.xls");
    }
}
                    