/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

package ch.emad.schuetu.reports.word;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Component
public class WordTemplatEngine {

    private static final Logger LOG = Logger.getLogger(WordTemplatEngine.class);

    private static final String DOCUMENT_XML = "word/document.xml";

    public byte[] createPDFFromDOCXTemplate(String template, Map<String, String> replaceMap)throws  Exception{
        // validate
        String ret = validateTemplate(readFile(template), replaceMap);
        if(!ret.isEmpty()){
            LOG.error(ret);
            throw new Exception("ungueltiger template aufruf " + ret);
        }
        // docx replace
        byte[] docx = replacePlaceholdersInDOCX(template, replaceMap);

        byte[] pdf = convertDOCXToPDF(docx);

        return pdf;

    }

    public byte[] createPDFFromDOCXTemplate(byte[] template, Map<String, String> replaceMap) throws  Exception{
        // validate
        String ret = validateTemplate(template, replaceMap);
        if(!ret.isEmpty()){
            LOG.error(ret);
            throw new Exception("ungueltiger template aufruf " + ret);
        }
        // docx replace
        byte[] docx = replacePlaceholdersInDOCX(template, replaceMap);

        byte[] pdf = convertDOCXToPDF(docx);

        return pdf;

    }

    public String validateTemplate(byte [] template, Map<String, String> replaceMap) {
        StringBuilder builder = new StringBuilder();
        Map<String,String> map = getParametermapForTemplate(template, true);

        for(String key : map.keySet()){
            if(!replaceMap.containsKey(key)){
                builder.append("Key im Template nicht gefunden: " + key + "\n");
            }
        }

        if(replaceMap.size() > map.size()){
            builder.append("Es wurden nicht alle Keys im Word Template gedunden, es fehlen "+(replaceMap.size() -map.size()) +"\n");
        }

        return builder.toString();
    }

    protected byte[] replacePlaceholdersInDOCX(String template, Map<String, String> replaceMap) {
        return replacePlaceholdersInDOCX(this.readFile(template), replaceMap);
    }

    protected byte[] replacePlaceholdersInDOCX(byte[] docx, Map<String, String> replaceMap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(docx));
            final ZipOutputStream zipOut = new ZipOutputStream(output);
            ZipEntry inEntry;
            while ((inEntry = zipIn.getNextEntry()) != null) {
                final ZipEntry outEntry = new ZipEntry(inEntry.getName());
                zipOut.putNextEntry(outEntry);
                if (inEntry.getName().equals(DOCUMENT_XML)) {
                    String content = IOUtils.toString(zipIn, CharEncoding.UTF_8);
                    for (String key : replaceMap.keySet()) {
                        content = content.replace(key, replaceMap.get(key));
                    }
                    IOUtils.write(content, zipOut, CharEncoding.UTF_8);
                } else {
                    IOUtils.copy(zipIn, zipOut);
                }
                zipOut.closeEntry();
            }
            zipIn.close();
            zipOut.finish();
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return output.toByteArray();
    }

    protected byte[] convertDOCXToPDF(byte[] docx) {
        try {
            InputStream in = new ByteArrayInputStream(docx);
            XWPFDocument document = new XWPFDocument(in);
            PdfOptions options = PdfOptions.create();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfConverter.getInstance().convert(document, out, options);
            return out.toByteArray();
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }
        return null;
    }

    public byte[] mergePDF(List<byte[]> pdf) {
        PDFMergerUtility ut = new PDFMergerUtility();
        for (byte[] b : pdf) {
            ut.addSource(new ByteArrayInputStream(b));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ut.setDestinationStream(out);
        try {
            ut.mergeDocuments();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return out.toByteArray();
    }

    protected Map getParametermapForTemplate(byte[] template, boolean substitueValues) {
        Map<String,String> map = new HashMap<String,String>();
        try {
            ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(template));
            ZipEntry inEntry;
            while ((inEntry = zipIn.getNextEntry()) != null) {
                if (inEntry.getName().equals(DOCUMENT_XML)) {
                    String content = IOUtils.toString(zipIn, CharEncoding.UTF_8);
                    Pattern pattern = Pattern.compile("V[0-9]{2}");
                    Matcher matcher = pattern.matcher(content);
                    while (matcher.find()) {
                        String res = matcher.group();
                        if(substitueValues) {
                            map.put(res, "*"+res.toUpperCase()+"*");
                        } else {
                            map.put(res, res);
                        }
                    }
                }
            }
            zipIn.close();
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return map;
    }

    protected byte[] readFile(String file) {
        byte[] in = null;
        URL url = com.google.common.io.Resources.getResource("doc-templates/" + file + ".docx");
        try {
            in = com.google.common.io.Resources.toByteArray(url);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return in;
    }

}
