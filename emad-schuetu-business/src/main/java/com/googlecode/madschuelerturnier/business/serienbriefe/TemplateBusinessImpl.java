/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.serienbriefe;

import ch.emad.schuetu.reports.interfaces.CouvertReportable;
import ch.emad.schuetu.reports.word.WordTemplatEngine;
import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.business.websiteinfo.WebsiteInfoService;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.0.1
 */
@Controller(value = "templateBusiness")
public class TemplateBusinessImpl implements TemplateBusiness {

    private static final Logger LOG = Logger.getLogger(TemplateBusinessImpl.class);

    private static Map<String, TemplateBusinessBean> templateBusinessBeanMap = new HashMap<String, TemplateBusinessBean>();

    @Autowired
    private DropboxConnector dropbox;

    @Autowired
    private MannschaftRepository repo;

    @Autowired
    private WordTemplatEngine wordengine;

    @PostConstruct
    private void initTrigger() {
        init();
    }

    @Async
    private void init() {

        for (String name : getTemplateStrings()) {
            TemplateBusinessBean bean = new TemplateBusinessBean();
            bean.setName(name);
            initTemplate(bean);
        }

    }

    @Override
    public void reloadBean(String name) {
        for (TemplateBusinessBean bean : getTemplateBeans()) {
            if (bean.getName().equals(name)) {
                int count = bean.getCheckVersion();
                bean = new TemplateBusinessBean();
                bean.setCheckVersion(count);
                bean.setName(name);
                initTemplateSync(bean);
            }
        }
    }

    @Override
    public Collection<TemplateBusinessBean> getTemplateBeans() {
        Set<String> keys = TemplateBusinessImpl.templateBusinessBeanMap.keySet();
        for (String name : getTemplateStrings()) {
            if (!keys.contains(name)) {
                try {
                    TemplateBusinessBean bean = new TemplateBusinessBean();
                    bean.setName(name);
                    initTemplate(bean);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return templateBusinessBeanMap.values();
    }

    @Override
    public byte[] getTemplate(String name) {
        return templateBusinessBeanMap.get(name).getOktemplate();
    }

    @Override
    public byte[] getTemplate(String name, Collection<Map<String, String>> map) {
        List<byte[]> arr = new ArrayList<>();
        for(Map<String, String> m :map){
            try {
                arr.add(wordengine.createPDFFromDOCXTemplate(templateBusinessBeanMap.get(name).getTemplate(), m));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wordengine.mergePDF(arr);
    }

    @Override
    public byte[] getRechnungen() {
        return this.getTemplate("betreuer-rechnung-2",TemplateMapper.convertRechnungen(repo.findAll()));
    }

    @Async
    public void initTemplate(TemplateBusinessBean bean) {
        initTemplateSync(bean);
    }

    public void initTemplateSync(TemplateBusinessBean bean) {

        bean.countCheckversion();

        // docxTemplate
        byte[] docx = dropbox.loadFile("templates/" + bean.getName() + ".docx");
        if (docx != null && docx.length > 0) {
            bean.setTemplate(docx);
            bean.setTemplateHere(true);
        } else {
            bean.setTemplateHere(false);
        }

        // properties
        byte[] properties = dropbox.loadFile("templates/" + bean.getName() + ".properties");

        if (properties != null && properties.length > 0) {
            Properties p = new Properties();
            try {
                p.load(new ByteArrayInputStream(properties));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Map<String, String> map = (Map) p;
            bean.setTemplateValueMap(map);
            bean.setTemplateValueMapHere(true);
        } else {
            bean.setTemplateValueMapHere(false);
            templateBusinessBeanMap.put(bean.getName(), bean);
            return;
        }

        try {

            String result = wordengine.validateTemplate(bean.getTemplate(), bean.getTemplateValueMap());
            if (result.isEmpty()) {
                bean.setTemplateValide(true);
            } else {
                bean.setValidationError(result);
                templateBusinessBeanMap.put(bean.getName(), bean);
                return;
            }

            byte[] finished = wordengine.createPDFFromDOCXTemplate(bean.getTemplate(), bean.getTemplateValueMap());
            bean.setOktemplate(finished);
        } catch (Exception e) {
            e.printStackTrace();
        }
        templateBusinessBeanMap.put(bean.getName(), bean);
    }

    private List<String> getTemplateStrings() {
        List<String> ret = new ArrayList<>();
        List<String> files = dropbox.getFilesInFolder("templates");
        for (String file : files) {
            if (file.contains(".docx")) {
                ret.add(file.replace(".docx", ""));
            }
        }
        return ret;
    }

}