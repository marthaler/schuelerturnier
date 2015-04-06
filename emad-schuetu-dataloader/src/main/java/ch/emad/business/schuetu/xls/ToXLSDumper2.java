/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.xls;

import ch.emad.model.common.model.DBAuthUser;
import ch.emad.model.common.model.File;
import ch.emad.model.common.model.Text;
import ch.emad.model.schuetu.model.*;
import ch.emad.persistence.common.DBAuthUserRepository;
import ch.emad.persistence.common.FileRepository;
import ch.emad.persistence.common.TextRepository;
import ch.emad.persistence.schuetu.repository.*;
import com.google.common.io.Resources;
import ch.emad.model.schuetu.model.comperators.SpielZeitComperator;
import ch.emad.model.schuetu.model.support.File2;
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
public class ToXLSDumper2 {

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
        List<File> file2s = filesIn;
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

        if (file2s == null) {
            file2s = new ArrayList();
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
        beans.put("attachements", file2s);
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
