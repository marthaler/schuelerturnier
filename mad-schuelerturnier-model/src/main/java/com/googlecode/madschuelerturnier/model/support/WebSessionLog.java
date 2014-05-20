/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.support;


import com.googlecode.madschuelerturnier.model.Persistent;
import org.apache.log4j.Logger;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

/**
 * Log Objekt zum ueberwachen der Web Taetigkeit
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.1
 */
@Entity
public class WebSessionLog extends Persistent {

    private static final long serialVersionUID = 1L;

    private String sessionid;

    private Date start = new Date();

    private Date end = new Date();

    private int requests = 0;

    private String userAgent;

    private String typ;

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
