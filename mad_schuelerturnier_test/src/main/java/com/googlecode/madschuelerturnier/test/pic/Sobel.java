package com.googlecode.madschuelerturnier.test.pic;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;
//import org.marvinproject.image.convolution.Convolution;

/**
 * @author Gabriel Ambr�sio Archanjo
 */
public class Sobel extends MarvinAbstractImagePlugin {


    public static void main(String[] args) {

        Sobel s = new Sobel();
        s.load();
        MarvinImage imageIn = MarvinImageIO.loadImage("/res/m.png");
        MarvinImage imageOut = MarvinImageIO.loadImage("/res/m1.png");
        s.process(imageIn, imageOut);


    }


    // Definitions
    double[][] matrixSobelX = new double[][]{
            {1, 0},
            {0, -1}
    };

    double[][] matrixSobelY = new double[][]{
            {0, 1},
            {-1, 0}
    };

    private MarvinImagePlugin convolution;

    public void load() {
        convolution = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.convolution.jar");
        //convolution = new Convolution();
        convolution.load();
    }

    public MarvinAttributesPanel getAttributesPanel() {
        return null;
    }

    public void process
            (
                    MarvinImage imageIn,
                    MarvinImage imageOut,
                    MarvinAttributes attrOut,
                    MarvinImageMask mask,
                    boolean previewMode
            ) {
        convolution.setAttribute("matrix", matrixSobelX);

        convolution.process(imageIn, imageOut, null, mask, previewMode);

        convolution.setAttribute("matrix", matrixSobelY);
        convolution.process(imageIn, imageOut, null, mask, previewMode);

        MarvinImageIO.saveImage(imageOut, "/res/m3.png");

    }
}