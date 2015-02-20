/**
 * Apache License 2.0
 */
package ch.emad.schuetu.reports.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PDF Manipulationen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class PDFUtil {

    public static List<byte[]> splitPDFToPNG(byte [] in) {

        List<byte[]> outlist = new ArrayList<>();
        PDDocument document = null;
        try {
            InputStream ins = new ByteArrayInputStream(in);
            document = PDDocument.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
        int page = 0;
        for (PDPage pdPage : pdPages)
        {
            ++page;
            BufferedImage bim = null;
            try {
                bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ByteArrayOutputStream outStr = new ByteArrayOutputStream();
                ImageIOUtil.writeImage(bim, "png", outStr, 300);
                outlist.add(outStr.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outlist;
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

