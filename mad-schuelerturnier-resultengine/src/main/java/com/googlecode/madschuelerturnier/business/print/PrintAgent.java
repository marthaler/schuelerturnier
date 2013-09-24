package com.googlecode.madschuelerturnier.business.print;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 21.04.13
 * Time: 19:20
 * To change this template us   e File | Settings | File Templates.
 */

@Component
public class PrintAgent {

    private static final Logger LOG = Logger.getLogger(PrintAgent.class);

    private boolean running = false;

    private boolean init = false;

    private String pathprinter = "";
    private String pathdone = "";
    private String printtemplates = "";

    private String printer = "brother";

    private Map<String, String> map = new HashMap<String, String>();

    public void init(String path) {
        this.pathprinter = path + "printer" + System.getProperty("file.separator");
        this.pathdone = path + "printerdone" + System.getProperty("file.separator");
        this.printtemplates = path + "printtemplates" + System.getProperty("file.separator");
        boolean pathprinterB = new File(pathprinter).mkdirs();
        boolean pathdoneB = new File(pathdone).mkdirs();

        LOG.info("pathdone erstellt: " + pathdone + " " + pathprinterB);
        LOG.info("pathprinter erstellt: " + pathprinter + " " + pathdoneB);

        this.running = true;
        this.init = true;

        if (map.size() > 0) {
            Set<String> keys = map.keySet();

            for (String key : keys) {
                String content = map.get(key);
                this.printPage(key, content);
            }
        }
    }

    public void printTestpage() {
        this.printPage("testpage", "Dies ist eine Testseite. Ein At:@ Und die lieben Umlaute: ä ö ü");
    }

    public void printPage(String name, String content) {

        if (this.init) {
            this.saveFileToPrint(name, content);
        } else {
            map.put(name, content);
        }

        check();
    }

    @Scheduled(fixedDelay = 15000)
    private void check() {

        if (!init) {
            return;
        }

        Collection<File> files = FileUtils.listFiles(new File(pathprinter), null, false);
        for (File f : files) {
            if (f.getName().contains(".xml")) {
                continue;
            }

            if (running) {
                this.print(f);
            }

            try {
                FileUtils.copyFile(f, new File(this.pathdone + f.getName()));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            FileUtils.deleteQuietly(f);
        }
    }

    public void saveFileToPrint(String name, String htmlContent) {
        OutputStream os = null;
        try {

            if (!this.init) {
                map.put(name, htmlContent);
                return;
            }

            CleanerProperties props = new CleanerProperties();

            // set some properties to non-default values
            props.setTranslateSpecialEntities(true);
            props.setTransResCharsToNCR(true);
            props.setOmitComments(true);

            // do parsing
            LOG.info("HtmlCleaner! Body wegnehmen");
            TagNode tagNode = new HtmlCleaner(props).clean(
                    htmlContent
            );

            Object[] o = tagNode.evaluateXPath("//body");

            // serialize to xml file
            new PrettyXmlSerializer(props).writeToFile(
                    //  --> mit utf-8 wurden sonderzeichen falsch gedruckt pathprinter+"out.xml", "utf-8"
                    (TagNode) o[0], pathprinter + "out.xml"
            );

            String outputFile = pathprinter + name + ".pdf";
            os = new FileOutputStream(outputFile);

            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, os);
            doc.open();
            HTMLWorker hw = new HTMLWorker(doc);

            hw.parse(new FileReader(pathprinter + "out.xml"));
            doc.close();

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        FileUtils.deleteQuietly(new File(pathprinter + "out.xml"));
    }

    public void printSchiriAnleitung() {
        try {
            FileUtils.copyFile(new File(this.printtemplates + "schirizettel.pdf"), new File(this.pathprinter + "schirizettel.pdf"));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void print(File f) {

        try {
            LOG.debug("file zum drucken: " + f.getCanonicalPath());
            LOG.debug("file existiert: " + f.exists());
            LOG.debug("file ist file: " + f.isFile());
            LOG.debug("file ist directory: " + f.isDirectory());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        FileInputStream in = null;

        try {
            in = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
        DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;

        // find the printing service
        AttributeSet attributeSet = new HashAttributeSet();
        attributeSet.add(new PrinterName(printer, null));
        attributeSet.add(new Copies(1));

        PrintService service = PrintServiceLookup.lookupDefaultPrintService();


        if (service == null) {
            LOG.error("default printer ist: null");
            return;
        }

        //create document
        Doc doc = new SimpleDoc(in, flavor, null);

        DocPrintJob job = service.createPrintJob();

        LOG.info("PrintObserver: printing: " + f.getName());

        try {
            job.print(doc, null);
        } catch (PrintException e) {
            LOG.error(e.getMessage(), e);
        }

        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }
}
