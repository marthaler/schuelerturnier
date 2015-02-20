/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

package ch.emad.schuetu.reports.word;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class WordTemplatModelchanger {

    private static final Logger LOG = Logger.getLogger(WordTemplatModelchanger.class);

    public static String invokeMethod(String method,Object obj) {
        try {
            Method meth = obj.getClass().getMethod(method);
            Object returnValue = meth.invoke(obj);
            if(returnValue != null){
                return returnValue.toString();
            }
            return "";
        } catch (SecurityException e) {
            LOG.error(e);
        } catch (NoSuchMethodException e) {
            LOG.error(e);
        } catch (InvocationTargetException e) {
            LOG.error(e);
        } catch (IllegalAccessException e) {
            LOG.error(e);
        }
        return "";
    }

}
