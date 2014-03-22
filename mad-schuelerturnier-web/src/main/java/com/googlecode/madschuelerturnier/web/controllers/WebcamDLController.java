package com.googlecode.madschuelerturnier.web.controllers;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.CaptureEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequestMapping("/aa")
public class WebcamDLController {

    public static  byte[] data;

    @RequestMapping(value = "/ww")
    public void getFile(HttpServletResponse response) {

        try {



            // get your file as InputStream
            InputStream is = new ByteArrayInputStream(data);
            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            //LOG.error(ex.getMessage(),ex);
        }

    }
}
                        