package com.googlecode.mad_schuelerturnier.business.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class TrimWhite {

    private BufferedImage img;


    public static BufferedImage toBinaryImage(final BufferedImage image) {
        final BufferedImage blackAndWhiteImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_BYTE_BINARY);
        final Graphics2D g = (Graphics2D) blackAndWhiteImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return blackAndWhiteImage;
    }

    public TrimWhite(File input) {
        try {
            img = ImageIO.read(input);

            img = toBinaryImage(img);
        } catch (IOException e) {
            throw new RuntimeException( "Problem reading image", e );
        }
    }

    public void trim() {
        int width  = getTrimmedWidth();
        int height = getTrimmedHeight();

        BufferedImage newImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = newImg.createGraphics();
        g.drawImage( img, 0, 0, null );
        img = newImg;
    }

    public void write(File f) {
        try {
            ImageIO.write(img, "bmp", f);
        } catch (IOException e) {
            throw new RuntimeException( "Problem writing image", e );
        }
    }

    private int getTrimmedWidth() {
        int height       = this.img.getHeight();
        int width        = this.img.getWidth();
        int trimmedWidth = 0;

        for(int i = 0; i < height; i++) {
            for(int j = width - 1; j >= 0; j--) {
                if(img.getRGB(j, i) != Color.WHITE.getRGB() &&
                        j > trimmedWidth) {
                    trimmedWidth = j;
                    break;
                }
            }
        }

        return trimmedWidth;
    }

    private int getTrimmedHeight() {
        int width         = this.img.getWidth();
        int height        = this.img.getHeight();
        int trimmedHeight = 0;

        for(int i = 0; i < width; i++) {
            for(int j = height - 1; j >= 0; j--) {
                if(img.getRGB(i, j) != Color.WHITE.getRGB() &&
                        j > trimmedHeight) {
                    trimmedHeight = j;
                    break;
                }
            }
        }

        return trimmedHeight;
    }

    public static void main(String[] args) {




        TrimWhite trim = new TrimWhite(new File("/a.jpg"));
        trim.trim();
        trim.write(new File("/bmp2.bmp"));
    }

    private String getIP()
    {
        // This try will give the Public IP Address of the Host.
        try
        {
            URL url = new URL("http://automation.whatismyip.com/n09230945.asp");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ipAddress = new String();
            ipAddress = (in.readLine()).trim();
        /* IF not connected to internet, then
         * the above code will return one empty
         * String, we can check it's length and
         * if length is not greater than zero,
         * then we can go for LAN IP or Local IP
         * or PRIVATE IP
         */
            if (!(ipAddress.length() > 0))
            {
                try
                {
                    InetAddress ip = InetAddress.getLocalHost();
                    System.out.println((ip.getHostAddress()).trim());
                    return ((ip.getHostAddress()).trim());
                }
                catch(Exception ex)
                {
                    return "ERROR";
                }
            }
            System.out.println("IP Address is : " + ipAddress);

            return (ipAddress);
        }
        catch(Exception e)
        {
            // This try will give the Private IP of the Host.
            try
            {
                InetAddress ip = InetAddress.getLocalHost();
                System.out.println((ip.getHostAddress()).trim());
                return ((ip.getHostAddress()).trim());
            }
            catch(Exception ex)
            {
                return "ERROR";
            }
        }
    }
}