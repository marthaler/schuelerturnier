/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.serienbriefe;

import java.util.Date;
import java.util.Map;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.0.1
 */
public class TemplateBusinessBean {

    public void setCheckVersion(int checkVersion) {
        this.checkVersion = checkVersion;
    }

    private Date checkdate = new Date();
    private int checkVersion = 0;

    private byte[] template = null;
    private byte[] oktemplate = null;

    private String name;

    private boolean templateHere = false;
    private boolean templateValueMapHere = false;

    private boolean templateValide = false;

    private String validationError;

    public Date getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    public String getValidationError() {
        return validationError;
    }

    public String getValidationErrorHTML() {
        if(validationError != null){
            return validationError.replace("\n","</br>");
        }
        return "";
    }

    public void setValidationError(String validationError) {
        this.validationError = validationError;
    }

    private Map<String,String> templateValueMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getOktemplate() {
        return oktemplate;
    }

    public void setOktemplate(byte[] oktemplate) {
        this.oktemplate = oktemplate;
    }

    public byte[] getTemplate() {
        return template;
    }

    public void setTemplate(byte[] template) {
        this.template = template;
    }

    public boolean isTemplateHere() {
        return templateHere;
    }

    public void setTemplateHere(boolean templateHere) {
        this.templateHere = templateHere;
    }

    public boolean isTemplateValide() {
        return templateValide;
    }

    public void setTemplateValide(boolean templateValide) {
        this.templateValide = templateValide;
    }

    public Map<String, String> getTemplateValueMap() {
        return templateValueMap;
    }

    public void setTemplateValueMap(Map<String, String> templateValueMap) {
        this.templateValueMap = templateValueMap;
    }

    public boolean isTemplateValueMapHere() {
        return templateValueMapHere;
    }

    public void setTemplateValueMapHere(boolean templateValueMapHere) {
        this.templateValueMapHere = templateValueMapHere;
    }

    public int getCheckVersion() {
        return checkVersion;
    }

    public void countCheckversion() {
        this.checkVersion ++;
    }

}