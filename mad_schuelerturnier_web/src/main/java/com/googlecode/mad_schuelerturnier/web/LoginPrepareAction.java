package com.googlecode.mad_schuelerturnier.web;

import com.googlecode.mad_schuelerturnier.business.IBusiness;
import com.googlecode.mad_schuelerturnier.business.controller.leiter.converter.HTMLOutConverter;
import com.googlecode.mad_schuelerturnier.business.generator.BarcodeGenerator;
import com.googlecode.mad_schuelerturnier.business.generator.SpeakerGenerator;
import com.googlecode.mad_schuelerturnier.business.out.OutToWebsitePublisher;
import com.googlecode.mad_schuelerturnier.business.picture.PictureAgent;
import com.googlecode.mad_schuelerturnier.business.print.PrintAgent;
import com.googlecode.mad_schuelerturnier.business.scanner.ScannerAgent;
import com.googlecode.mad_schuelerturnier.model.enums.SpielPhasenEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
    OutToWebsitePublisher outToWebsite;

    @Autowired
    IBusiness business;

    @Autowired
    ScannerAgent scannerAgent;

    @Autowired
    PictureAgent pictureAgent;

    private String ip;

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

        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        ip = httpServletRequest.getServerName();


        InetAddress IP = null;
        try {
            IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("IP of my system is := " + IP.getHostAddress());

        ip = "http://" + IP.getHostAddress() + ":" + httpServletRequest.getServerPort();

        speakerGenerator.init(path + "static" + delim);

        printAgent.init(path + "static" + delim);

        converter.setPath(path + "static" + delim);

        barcodeGenerator.init(path + "static" + delim);

        outToWebsite.init(path + "static" + delim);

        pictureAgent.init(path + "static" + delim);

        scannerAgent.init(path + "static" + delim);

        init = true;

        LOG.info("LoginPrepareAction: job gestartet");

    }

    public String getIp() {
        return ip;
    }

}