package com.googlecode.madschuelerturnier.web.delegate;

import com.googlecode.madschuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
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
public class EintragDelegate {

    @Autowired
    private CVSMannschaftParser inporter;

    @Autowired
    private MannschaftRepository repo;

    private String text = "";

    private static final Logger LOG = Logger.getLogger(EintragDelegate.class);

    public void eintragen() {
        List<Mannschaft> liste = inporter.parseFileContent(this.text);
        repo.save(liste);
        text = "Mannschaften erfolgreich importiert";
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

}
