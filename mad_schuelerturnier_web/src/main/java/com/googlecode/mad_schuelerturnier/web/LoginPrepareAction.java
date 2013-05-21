package com.googlecode.mad_schuelerturnier.web;

import com.googlecode.mad_schuelerturnier.business.IBusiness;
import com.googlecode.mad_schuelerturnier.business.controller.leiter.converter.HTMLOutConverter;
import com.googlecode.mad_schuelerturnier.business.generator.BarcodeGenerator;
import com.googlecode.mad_schuelerturnier.business.generator.SpeakerGenerator;
import com.googlecode.mad_schuelerturnier.business.print.PrintAgent;
import com.googlecode.mad_schuelerturnier.model.enums.SpielPhasenEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.IOException;

@Component
public class LoginPrepareAction {

    private boolean init = false;

    private static final Logger LOG = Logger.getLogger(LoginPrepareAction.class);

    @Autowired
    SpeakerGenerator speakerGenerator;

    @Autowired
    PrintAgent printAgent;

    @Autowired
    BarcodeGenerator barcodeGenerator;

    @Autowired
    IBusiness business;

    @Autowired
    private HTMLOutConverter converter;

    private String delim = System.getProperty("file.separator");

    public void execute() throws IOException {

        if (init) {
            LOG.info("LoginPrepareAction: bereits aufgerufen, nicht mehr noetig");
            return;
        }

        if (business.getSpielEinstellungen().getPhase() != SpielPhasenEnum.E_SPIELBEREIT && business.getSpielEinstellungen().getPhase() != SpielPhasenEnum.F_SPIELEN && business.getSpielEinstellungen().getPhase() != SpielPhasenEnum.G_ABGESCHLOSSEN) {
            LOG.info("LoginPrepareAction: falsche spiel pahase aufruf nicht moeglich: " + business.getSpielEinstellungen().getPhase());
            return;
        }

        LOG.info("LoginPrepareAction: wird aufgerufen");

        FacesContext context2 = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context2.getExternalContext().getContext();

        String path = sc.getRealPath("index.html").replace("index.html", "");

        speakerGenerator.init(path + "static" + delim);

        printAgent.init(path + "static" + delim);

        converter.setPath(path + "static" + delim);

        barcodeGenerator.init(path + "static" + delim);

        init = true;

        LOG.info("LoginPrepareAction: job gestartet");

    }

}