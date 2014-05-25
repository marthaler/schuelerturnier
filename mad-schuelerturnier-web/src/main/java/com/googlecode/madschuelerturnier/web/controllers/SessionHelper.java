package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.model.support.WebSessionLog;
import com.googlecode.madschuelerturnier.model.util.XstreamUtil;
import com.googlecode.madschuelerturnier.persistence.repository.WebSessionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
/**
 * Helper zum Speichern der Session Statistiken
 *
 * @author marthaler.worb@gmail.com
 * @since 1.3.1
 */
@Component
public class SessionHelper {

    private String jahr;

    @Autowired
    private WebSessionLogRepository logRepo;

    @Autowired
    private DropboxConnector dropboxConnector;

    public void invoke(HttpServletRequest request, String typ) {
        String id = request.getSession().getId();
        WebSessionLog logS = logRepo.findWebSessionLogById(id);
        if (logS == null) {
            logS = new WebSessionLog();
            logS.setSessionid(id);
            logS.setUserAgent(request.getHeader("User-Agent"));
            logS.setTyp(typ);
        }
        logS.setRequests(logS.getRequests() + 1);
        logS.setEnd(new Date());
        logRepo.save(logS);
    }

    public String getJahr() {
        return jahr;
    }

    public void setJahr(String jahr) {
        this.jahr = jahr;
    }

    public void dump(){
        dropboxConnector.saveFile("statistikdump.xml",XstreamUtil.serializeToString(logRepo.findAll()).getBytes());
    }

}