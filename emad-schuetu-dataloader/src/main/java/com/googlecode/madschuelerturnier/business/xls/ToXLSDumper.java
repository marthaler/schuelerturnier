/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.xls;

import com.google.common.io.Resources;
import com.googlecode.madschuelerturnier.model.*;
import com.googlecode.madschuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.madschuelerturnier.model.comperators.SpielZeitComperator;
import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.*;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Generiert einen XLS Export aus der Datenbank
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class ToXLSDumper {

    @Autowired
    private MannschaftRepository repo;

    @Autowired
    private SpielRepository srepo;

    @Autowired
    private DBAuthUserRepository authRepo;

    @Autowired
    private KorrekturRepository krepo;

    @Autowired
    private FileRepository frepo;

    @Autowired
    private TextRepository trepo;

    @Autowired
    private PenaltyRepository prepo;

    @Autowired
    private KontaktRepository korepo;

    private static final Logger LOG = Logger.getLogger(MannschaftRepository.class);

    public byte[] mannschaftenFromDBtoXLS() {
        return convertModelToXLS(repo.findAll(), srepo.findAll(), krepo.findAll(), authRepo.findAll(), frepo.findAll(), trepo.findAll(), prepo.findAll(),korepo.findAll());
    }

    public byte[] mannschaftenFromDBtoXLS(SpielEinstellungen einst) {
        List<SpielEinstellungen> einstellungen = new ArrayList<SpielEinstellungen>();
        einstellungen.add(einst);
        return convertModelToXLS(repo.findAll(), srepo.findAll(), krepo.findAll(), authRepo.findAll(), frepo.findAll(), trepo.findAll(),prepo.findAll(),korepo.findAll());
    }

    protected byte[] convertModelToXLS(List<Mannschaft> mannschaftenIn, List<Spiel> spieleIn, List<Korrektur> korrekturenIn, List<DBAuthUser> usersIn, List<File> filesIn,List<Text> texteIn,List<Penalty> penaltyIn,List<Kontakt> kontakteIn) {

        List<Spiel> spiele = spieleIn;
        List<Mannschaft> mannschaften = mannschaftenIn;
        List<Korrektur> korrekturen = korrekturenIn;
        List<DBAuthUser> users = usersIn;
        List<File> files = filesIn;
        List<Text> texte = texteIn;
        List<Penalty> penaltys = penaltyIn;
        List<Kontakt> kontakte = kontakteIn;

        if (spiele == null) {
            spiele = new ArrayList();
        } else {
            // setze dummy Mannschaften fuer das speichern der Finale
            Mannschaft m = new Mannschaft();
            m.setId(0l);
            for(Spiel s : spiele){
                if(s.getMannschaftA() == null){
                    s.setMannschaftA(m);
                }
                if(s.getMannschaftB() == null){
                    s.setMannschaftB(m);
                }
            }

        }
        Collections.sort(spiele,new SpielZeitComperator());

        if (mannschaften == null) {
            mannschaften = new ArrayList();
        }

        if (korrekturen == null) {
            korrekturen = new ArrayList();
        }

        if (users == null) {
            korrekturen = new ArrayList();
        }

        if (files == null) {
            files = new ArrayList();
        }

        if (texte == null) {
            texte = new ArrayList();
        }

        if (penaltys == null) {
            penaltys = new ArrayList();
        }

        if (kontakte == null) {
            kontakte = new ArrayList();
        }

        Map beans = new HashMap();

        beans.put("mannschaften", mannschaften);
        beans.put("spiele", spiele);
        beans.put("korrekturen", korrekturen);
        beans.put("users", users);
        beans.put("attachements", files);
        beans.put("texte", texte);
        beans.put("penaltys", penaltys);
        beans.put("kontakte", kontakte);

        XLSTransformer transformer = new XLSTransformer();
        byte[] arr = readFreshTemplate();
        try {

            InputStream is = new ByteArrayInputStream(arr);
            Workbook wb = WorkbookFactory.create(is);

            transformer.transformWorkbook(wb, beans);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            wb.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /*
    * Template neu lesen, falls nicht schon bereits ein Template uebergeben wurde (= nicht null ist)
    */
    private byte[] readFreshTemplate() {
        byte[] in = null;
        URL url = Resources.getResource("jxls-template.xls");
        try {
            in = Resources.toByteArray(url);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return in;
    }

}
