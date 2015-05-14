/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.xls;

import ch.emad.model.schuetu.model.*;
import ch.emad.model.common.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Testet den XLS Export aus der Datenbank
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
public class ExcelWithDatabaseIntegrationTest {

    Map<String,GPLer> land = new HashMap<>();
    Map<String,GPLer> verein = new HashMap<>();

    private static final Logger LOG = Logger.getLogger(ExcelWithDatabaseIntegrationTest.class);

    private FromXLSLoader2 xls = new FromXLSLoader2();

    @Test
    public void testParseMannschaften() {
/**
        List<GPLer> m = null;
        List<GPLer> m2 = null;
        List<GPLer> m3 = null;
        try {
            m = xls.convertXLSToGPler(FileUtils.readFileToByteArray(new File("d:/gp/2015.xls")));
            m2 = xls.convertXLSToGPler(FileUtils.readFileToByteArray(new File("d:/gp/2014.xls")));
            m3 = xls.convertXLSToGPler(FileUtils.readFileToByteArray(new File("d:/gp/2013.xls")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(m.size());
        System.out.println(m2.size());
        System.out.println(m3.size());


        for (GPLer gpLer : m) {
            System.out.println(gpLer);
        }

        for (GPLer gpLer : m2) {

            System.out.println(gpLer);
        }

        for (GPLer gpLer : m3) {
            System.out.println(gpLer);
        }
**/
    }

}
