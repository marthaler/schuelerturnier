/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.pdf;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class MannschaftBegeiterA5CouverPDFCreator {
    @Autowired
    private MannschaftRepository repo;

    private static final Logger LOG = Logger.getLogger(MannschaftBegeiterA5CouverPDFCreator.class);

    public byte[] createPdfFromDB(){
        return createPdf(repo.findAll());
    }

    public byte[] createPdf(List<Mannschaft> mannschaften) {
        try {
            Document document = new Document(PageSize.A5.rotate());

            ByteArrayOutputStream arrOut = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, arrOut);

            document.setMargins(410, 5, 240, 5);

            document.open();

            for (int i = 0; i < mannschaften.size(); i++) {
                Mannschaft m = mannschaften.get(i);

                document.add(new Paragraph(m.getBegleitpersonAnrede()));
                document.add(new Paragraph(m.getBegleitpersonName()));
                document.add(new Paragraph(m.getBegleitpersonStrasse()));
                document.add(new Paragraph(m.getBegleitpersonPLZOrt()));

                if (i < mannschaften.size()) {
                    document.newPage();
                }
            }

            document.close();

            return arrOut.toByteArray();

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
}

