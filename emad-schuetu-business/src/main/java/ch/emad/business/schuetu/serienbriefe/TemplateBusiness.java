package ch.emad.business.schuetu.serienbriefe;

import ch.emad.model.schuetu.interfaces.RechnungReportable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TemplateBusiness {

    void reloadBean(String name);
    Collection<TemplateBusinessBean> getTemplateBeans();
    byte[] getTemplate(String name);
    byte[] getTemplate(String name,Collection<Map<String,String>> map);
    byte[] getBriefe(List obj,String name);
    byte[] getRechnungen();
    byte[] getRechnungen(List<RechnungReportable> rechnungen);

}
