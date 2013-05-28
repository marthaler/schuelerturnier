package com.googlecode.mad_schuelerturnier.business.scanner;

import org.apache.xerces.impl.dv.util.Base64;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class SaveImageFromUrl {

	public void scann()  {
		String imageUrl = "http://admin@192.168.1.107/snapshot.cgi";
		//String destinationFile = "/image.jpg";



		//saveImage(imageUrl, destinationFile);
	}

	public void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
        URLConnection uc = url.openConnection();
        String userpass = "admin" + ":" + "";
        String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
        uc.setRequestProperty ("Authorization", basicAuth);

		InputStream is = uc.getInputStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}


    public void openConnection(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection uc = url.openConnection();
        String userpass = "admin" + ":" + "";
        String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
        uc.setRequestProperty ("Authorization", basicAuth);
        InputStream is = uc.getInputStream();
        is.close();

    }

}
