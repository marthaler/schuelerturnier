package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.business.controller.leiter.converter.HTMLOutConverter;
import com.googlecode.madschuelerturnier.business.generator.BarcodeGenerator;
import com.googlecode.madschuelerturnier.business.generator.SpeakerGenerator;
import com.googlecode.madschuelerturnier.business.out.OutToWebsitePublisher;
import com.googlecode.madschuelerturnier.business.picture.PictureAgent;
import com.googlecode.madschuelerturnier.business.print.PrintAgent;
import com.googlecode.madschuelerturnier.business.scanner.ScannerAgent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class LoginPrepareAction {

    public static final String STATIC = "static";
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
        LOG.info("LoginPrepareAction: path=" + path);
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        ip = httpServletRequest.getServerName();


        InetAddress ipA = null;
        try {
            ipA = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOG.error(e.getMessage(), e);
        }
        if (ipA != null) {
            LOG.info("" + "ip of my system is := " + ipA.getHostAddress());

            this.ip = "http://" + ipA.getHostAddress() + ":" + httpServletRequest.getServerPort();
        }
        speakerGenerator.init(path + STATIC + delim);

        printAgent.init(path + STATIC + delim);

        converter.setPath(path + STATIC + delim);

        barcodeGenerator.init(path + STATIC + delim);

        outToWebsite.init(path + STATIC + delim);

        pictureAgent.init(path + STATIC + delim);

        scannerAgent.init(path + STATIC + delim);

        init = true;

        LOG.info("LoginPrepareAction: job gestartet");

    }

    public String getIp() {
        return ip;
    }

}