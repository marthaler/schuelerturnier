package com.googlecode.madschuelerturnier.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.CaptureEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@Scope("session")
public class WebcamController {

    private  byte[] data;


    private List<String> photos = new ArrayList<String>();

    private String getRandomImageName() {
		int i = (int) (Math.random() * 10000000);

		return String.valueOf(i);
	}

    public List<String> getPhotos() {
        return photos;
    }


    @RequestMapping(value = "/webcam", method = RequestMethod.GET)
    public void getFile(
            HttpServletResponse response) {

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
    public void oncapture(CaptureEvent captureEvent) {
        String photo = getRandomImageName();
        this.photos.add(0,photo);
        byte[] data = captureEvent.getData();

        WebcamDLController.data = captureEvent.getData();

        this.data = captureEvent.getData();

		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String newFileName = servletContext.getRealPath("") + File.separator + "photocam" + File.separator + photo + ".png";

		FileImageOutputStream imageOutput;
		try {
			imageOutput = new FileImageOutputStream(new File(newFileName));
			imageOutput.write(data, 0, data.length);
			imageOutput.close();
		}
        catch(Exception e) {
			throw new FacesException("Error in writing captured image.");
		}
    }
}
                        