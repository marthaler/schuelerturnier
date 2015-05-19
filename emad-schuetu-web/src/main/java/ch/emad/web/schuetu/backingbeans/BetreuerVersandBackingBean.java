/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu.backingbeans;

import ch.emad.business.schuetu.serienbriefe.TemplateBusiness;
import ch.emad.model.schuetu.interfaces.RechnungReportable;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing Bean fuer den Versand an Betreuer (Rechnungen und Couverts)
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component(value = "betrVersandBB")
@Scope("session")
public class BetreuerVersandBackingBean implements Serializable{

    @Autowired
    private KontakteBackingBean kontakteBB;

    @Autowired
    private TemplateBusiness template;

    public String couvertTitel(){
        return kontakteBB.couvertTitel();
    }

    public StreamedContent downloadCouverts(){
        InputStream is = new ByteArrayInputStream(kontakteBB.downloadCouverts(getRechungen()));
        return new DefaultStreamedContent(is, "application/pdf", "couverts.pdf");
    }

    public StreamedContent download10Stamps(){
        InputStream is = new ByteArrayInputStream(kontakteBB.download10Stamps());
        return new DefaultStreamedContent(is, "application/pdf", "couverts.pdf");
    }


    public StreamedContent downloadRechnungen(){
        InputStream is = new ByteArrayInputStream(template.getBriefe(getRechungen(),"betreuer-rechnung"));
        return new DefaultStreamedContent(is, "application/pdf", "rechnungen.pdf");
    }

    private List<RechnungReportable> getRechungen(){
        kontakteBB.setSelectedList("mannschaften-aktuell");
        List<RechnungReportable> temp = new ArrayList<RechnungReportable>();
        for(RechnungReportable rep :kontakteBB.getAll()){
            if(rep.getEnabled()){
                temp.add(rep);
            }
        }
        return temp;
    }
}