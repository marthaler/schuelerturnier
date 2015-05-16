//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ch.emad.web.schuetu.controllers;

import ch.emad.dropbox.DropboxConnector;
import ch.emad.model.common.utils.XstreamUtil;
import ch.emad.model.schuetu.model.support.WebSessionLog2;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import ch.emad.persistence.schuetu.repository.WebSessionLogRepository2;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class SessionHelper {
    private static final Logger LOG = Logger.getLogger(SessionHelper.class);
    private String jahr;
    @Autowired
    private WebSessionLogRepository2 logRepo;

    @Autowired
    @Qualifier("dropbox")
    private DropboxConnector dropboxConnector;

    public SessionHelper() {
    }

    public void invoke(HttpServletRequest request, String typ) {
        try {
            String e = request.getSession().getId();
            WebSessionLog2 logS = this.logRepo.findWebSessionLogById(e);
            if(logS == null) {
                logS = new WebSessionLog2();
                logS.setSessionid(e);
                logS.setUserAgent(request.getHeader("User-Agent"));
                logS.setTyp(typ);
            }

            logS.setRequests(logS.getRequests() + 1);
            logS.setEnd(new Date());
            this.logRepo.save(logS);
        } catch (Exception var5) {
            LOG.info("fehler bei der session-speicherung: " + var5.getMessage());
        }

    }

    public String getJahr() {
        return this.jahr;
    }

    public void setJahr(String jahr) {
        this.jahr = jahr;
    }

    public void dump() {
        this.dropboxConnector.saveFile("statistikdump.xml", XstreamUtil.serializeToString(this.logRepo.findAll()).getBytes());
    }
}
