package com.googlecode.mad_schuelerturnier.web;

import com.googlecode.mad_schuelerturnier.business.IBusiness;
import com.googlecode.mad_schuelerturnier.business.controller.leiter.converter.HTMLOutConverter;
import com.googlecode.mad_schuelerturnier.business.generator.BarcodeGenerator;
import com.googlecode.mad_schuelerturnier.business.generator.SpeakerGenerator;
import com.googlecode.mad_schuelerturnier.business.out.OutToWebsitePublisher;
import com.googlecode.mad_schuelerturnier.business.picture.PictureAgent;
import com.googlecode.mad_schuelerturnier.business.print.PrintAgent;
import com.googlecode.mad_schuelerturnier.business.scanner.ScannerAgent;
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
    private SpeakerGenerator speakerGenerator;

    @Autowired
    private PrintAgent printAgent;

    @Autowired
    private BarcodeGenerator barcodeGenerator;

    @Autowired
    private OutToWebsitePublisher outToWebsite;

    @Autowired
    private IBusiness business;

    @Autowired
    private ScannerAgent scannerAgent;

    @Autowired
    private PictureAgent pictureAgent;

    @Autowired
    private HTMLOutConverter converter;


    private String ip;


    private String delim = System.getProperty("file.separator");

    public void execute() throws IOException {

        if (init) {
            LOG.info("LoginPrepareAction: bereits aufgerufen, nicht mehr noetig");
            return;
        }

        LOG.info("LoginPrepareAction: wird aufgerufen");

        FacesContext context2 = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context2.getExternalContext().getContext();

        String path = sc.getRealPath("index.html").replace("index.html", "");

        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        ip = httpServletRequest.getServerName();


        InetAddress ipA = null;
        try {
            ipA = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("" + "ip of my system is := " + ipA.getHostAddress());

        this.ip = "http://" + ipA.getHostAddress() + ":" + httpServletRequest.getServerPort();

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