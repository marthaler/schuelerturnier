package com.googlecode.mad_schuelerturnier.test.pic;

import marvin.image.MarvinImage;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CornerUtil {



    public static BufferedImage setCorners(BufferedImage image) {
		
		MarvinImagePlugin moravec = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.corner.moravec");
		moravec.load();
		moravec.setAttribute("threshold", 50000);

		// 1. Figures
		MarvinImage imageIn = new MarvinImage(image);
		MarvinAttributes attr = new MarvinAttributes();


        attr = new MarvinAttributes();
        moravec.process(imageIn, null, attr);
        imageIn = showCorners(imageIn, attr, 3);



       return  CornerUtil.showCorners(imageIn, attr, 5).getBufferedImage();
	}
	
	private static MarvinImage showCorners(MarvinImage image, MarvinAttributes attr, int rectSize){
		MarvinImage ret = image.clone();
		int[][] cornernessMap = (int[][]) attr.get("cornernessMap");
		int rsize=0;
		for(int x=0; x<cornernessMap.length; x++){
			for(int y=0; y<cornernessMap[0].length; y++){
				// Is it a corner?
				if(cornernessMap[x][y] > 0){
					rsize = Math.min(Math.min(Math.min(x, rectSize), Math.min(cornernessMap.length-x, rectSize)), Math.min(Math.min(y, rectSize), Math.min(cornernessMap[0].length-y, rectSize)));
					ret.fillRect(x, y, rsize, rsize, Color.red);
				}				
			}
		}
		
		return ret;
	}
}