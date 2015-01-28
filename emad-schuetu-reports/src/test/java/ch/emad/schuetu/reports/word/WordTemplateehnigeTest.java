/**
 * Apache License 2.0
 */
package ch.emad.schuetu.reports.word;


import com.googlecode.madschuelerturnier.business.pdf.JasperResultConverter;
import com.googlecode.madschuelerturnier.business.pdf.Mannschaft2;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Test fuer die Word Templateengine
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class WordTemplateehnigeTest {

    private String template = "betreuer-rechnung-2";
    @Test
    public void testTemplateengine() {
        // template laden und parameter auslesen
        WordTemplatEngine engine = new WordTemplatEngine();
        Map<String,String> map = engine.getParametermapForTemplate(template,true);

        System.out.println(map);

        Assert.assertEquals("nicht die gewuenschte parameter anzahl im template gefunden", 11, map.size());

        for(String str : map.values()){
            Assert.assertTrue("value in der map mit falschem default wert",str.contains("*"));
        }

        // parameter ersetzen
        byte[] docx = engine.replacePlaceholdersInDOCX(template,engine.getParametermapForTemplate(template,true));
        byte[] pdf = engine.convertDOCXToPDF(docx);
        File dir = targetDir();
        String file = dir.getAbsolutePath() + "/pdf.pdf";
        String filedocx = dir.getAbsolutePath() + "/pdf.docx";
        try {
            FileUtils.writeByteArrayToFile(new File(file),pdf);
            FileUtils.writeByteArrayToFile(new File(filedocx),docx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public File targetDir(){
        String relPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        File targetDir = new File(relPath+"../../target");
        if(!targetDir.exists()) {
            targetDir.mkdir();
        }
        return targetDir;
    }
}

