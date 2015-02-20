package com.googlecode.madschuelerturnier.business.serienbriefe;

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

}
