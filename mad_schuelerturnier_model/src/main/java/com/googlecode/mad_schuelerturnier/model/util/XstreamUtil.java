package com.googlecode.mad_schuelerturnier.model.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 04.05.13
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */
public class XstreamUtil {


    public static void saveObject(Object obj,String filename){
        XStream xStream = new XStream(new DomDriver());
        try {
            FileUtils.writeStringToFile(new File("/" + filename + ".xml"),xStream.toXML(obj));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object loadObject(String filename){
        XStream xStream = new XStream(new DomDriver());
        return xStream.fromXML(new File("./src/test/resources" + filename + ".xml"));
    }

}
