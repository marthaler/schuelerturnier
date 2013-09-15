package com.googlecode.mad_schuelerturnier.business.dataloader;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 12.01.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CVSSpielParser {

    private static final Logger LOG = Logger.getLogger(CVSSpielParser.class);

    public synchronized List<Spiel> loadMannschaften4Jahr(String jahr) {

        return parseFileContent(loadCSVFile(jahr));

    }

    public String loadCSVFile(String jahr) {

        String text = "";
        URL url = Resources.getResource("testspiele-orig-" + jahr + ".csv");
        try {
            text = Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return text;

    }

    public List<Spiel> parseFileContent(String text) {
        List<Spiel> result = new ArrayList<Spiel>();
        LOG.info("parse: " + text);
        CSVReader reader = new CSVReader(new StringReader(text), ',', '\"', 1);

        try {
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
                result.add(parseLine(line));
            }

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    public Spiel parseLine(String[] myEntry) {
        Long id = null;
        Spiel spiel = new Spiel();

        // 1  id string
        spiel.setIdString(myEntry[0].trim());

        spiel.setToreABestaetigt(Integer.parseInt(myEntry[2].trim()));

        spiel.setToreBBestaetigt(Integer.parseInt(myEntry[4].trim()));

        return spiel;
    }

}