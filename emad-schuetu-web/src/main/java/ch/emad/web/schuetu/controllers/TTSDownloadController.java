/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu.controllers;

import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.SpielZeile;
import ch.emad.persistence.schuetu.repository.SpielZeilenRepository;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Controller, welcher on demand Anfragen als TTS zurueckgibt (Weiterleitung zum TTS Server)
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
@RequestMapping("/tts/{typ}")
public class TTSDownloadController {

    private static final Logger LOG = Logger.getLogger(TTSDownloadController.class);

    @Autowired
    private SpielZeilenRepository repo;

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable("typ") String typ, @PathVariable("id") String id, HttpServletResponse response) {

        String talk = "keinen Text zum vorlesen vorhanden";

        if (typ.equals("spielansage")) {
            talk = this.generateFromZeile(id);
        }

        if (typ.equals("minute")) {
            if (id.equals("1")) {
                talk = "Die Spiele dauern noch eine Minute";
            } else {

                talk = "Die Spiele dauern noch " + id + " Minuten";
            }
        }

        if (typ.equals("frei")) {
            talk = id;
        }

        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet("http://localhost:59125/process?" + this.generateSettings() + URLEncoder.encode(talk, "UTF-8"));
            CloseableHttpResponse rs = httpclient.execute(httpget);
            try {
                HttpEntity entity = rs.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    // headers weiterkopieren
                    Header[] headers = rs.getAllHeaders();
                    for (Header h : headers) {
                        response.setHeader(h.getName(), h.getValue());
                    }
                    // stream weiterleiten
                    IOUtils.copy(instream, response.getOutputStream());
                    response.flushBuffer();
                }

            } finally {
                rs.close();
            }

        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private String generateFromZeile(String id) {
        StringBuffer buff = new StringBuffer();
        SpielZeile zeile = repo.findOne(Long.parseLong(id));

        if (zeile.getA() != null) {
            buff.append(generateFromSpiel(zeile.getA(), "A") + " ");
        }

        if (zeile.getB() != null) {
            buff.append(generateFromSpiel(zeile.getB(), "B") + " ");
        }

        if (zeile.getC() != null) {
            buff.append(generateFromSpiel(zeile.getB(), "C") + " ");
        }
        return buff.toString();
    }

    private String generateFromSpiel(Spiel spiel, String platz) {
        return "Auf Platz " + platz + ", bereitmachen " + spiel.getMannschaftAName() + " gegen " + spiel.getMannschaftBName() + ".";
    }

    private String generateSettings() {
        StringBuffer buff = new StringBuffer();
        buff.append("INPUT_TYPE=TEXT");
        buff.append("&");
        buff.append("OUTPUT_TYPE=AUDIO");
        buff.append("&");
        buff.append("AUDIO=WAVE");
        buff.append("&");
        buff.append("LOCALE=de");
        buff.append("&");
        buff.append("VOICE=bits3-hsmm");
        buff.append("&");
        buff.append("INPUT_TEXT=");
        return buff.toString();
    }

}
