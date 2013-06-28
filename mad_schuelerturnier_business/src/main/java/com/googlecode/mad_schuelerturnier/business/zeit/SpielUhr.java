/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.zeit;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * mad letzte aenderung: $Date: 2012-01-09 21:02:56 +0100 (Mo, 09 Jan 2012) $
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @version $Revision: 155 $
 * @headurl $HeadURL:
 * https://mad-schuelerturnier.googlecode.com/svn/trunk/mad_schuelereturnier
 * /src/main/java/com/googlecode/mad_schuelerturnier/business/
 * controller/leiter/HTMLSchiriConverter.java $
 */
@Component
public class SpielUhr implements ApplicationListener<ZeitPuls> {

    public static String NOT_INIT = "noch_nicht_gesetzt";

    public SpielUhr() {
    }

    private String richtigeZeit = NOT_INIT;
    private String spielZeit = NOT_INIT;

    public void onApplicationEvent(final ZeitPuls event) {

        final ZeitPuls p = event;

        final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        this.richtigeZeit = fmt.format(p.getEchteZeit().toDate());
        this.spielZeit = fmt.format(p.getSpielZeit().toDate());

    }

    public String getRichtigeZeit() {
        return this.richtigeZeit;
    }

    public String getSpielZeit() {
        return this.spielZeit;
    }

}