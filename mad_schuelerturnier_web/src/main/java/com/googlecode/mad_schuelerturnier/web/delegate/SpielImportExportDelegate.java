package com.googlecode.mad_schuelerturnier.web.delegate;

import com.googlecode.mad_schuelerturnier.business.ImportExportBusiness;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.bean.SessionScoped;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
@SessionScoped
public class SpielImportExportDelegate {

    @Autowired
    private ImportExportBusiness importer;

    private String text = "";

    private static final Logger LOG = Logger.getLogger(SpielImportExportDelegate.class);

    public void eintragen() {
        importer.updateSpiele(text, false, true);
        text = "Spiele erfolgreich importiert";
    }

    public void eintragenBestaetigt() {
        importer.updateSpiele(text, true, false);
        text = "Spiele erfolgreich importiert";
    }

    public void eintragenBeide() {
        importer.updateSpiele(text, true, true);
        text = "Spiele erfolgreich importiert";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isBlockField() {
        if (text.contains("erfolgreich einge")) {
            return true;
        }
        return false;
    }

    public List<Spiel> getSpiele() {
        return this.importer.loadAllSpiele();
    }

}
