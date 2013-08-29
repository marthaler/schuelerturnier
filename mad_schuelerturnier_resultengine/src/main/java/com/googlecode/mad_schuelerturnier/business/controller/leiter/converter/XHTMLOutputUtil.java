/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.controller.leiter.converter;

import org.apache.log4j.Logger;
import org.htmlcleaner.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Erstellt XHTML Output
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.5
 */
@Component
public class XHTMLOutputUtil {

    private static final Logger LOG = Logger.getLogger(HTMLSchiriConverter.class);

    @Value("print_folder")
    private String printfolderPath = "/print";

    public String cleanup(String input, boolean justbody) {

        CleanerProperties props = new CleanerProperties();

        props.setTranslateSpecialEntities(true);
        props.setTransResCharsToNCR(true);
        props.setOmitComments(true);
        props.setOmitDoctypeDeclaration(true);

        TagNode tagNode = new HtmlCleaner(props).clean(
                input
        );

        Object[] o = new Object[1];

        try {

            if (justbody) {

                o = tagNode.evaluateXPath("//body");
            } else {
                o[0] = tagNode;
            }
        } catch (XPatherException e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
        }

        TagNode tag = (TagNode) o[0];
        String ret = "";

        SimpleHtmlSerializer serializer = new SimpleHtmlSerializer(props);

        try {
            ret = serializer.getAsString(tag);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return ret;
    }

}
