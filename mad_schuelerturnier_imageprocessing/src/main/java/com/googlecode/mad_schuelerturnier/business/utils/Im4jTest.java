package com.googlecode.mad_schuelerturnier.business.utils;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 25.05.13
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class Im4jTest {


    public static void main(String [] args){
        // create command
        ConvertCmd cmd = new ConvertCmd();

// create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.addImage("/a.jpg");
        op.resize(800,600);
        op.addImage("/myimage_small.jpg");

// execute the operation
        try {
            cmd.run(op);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IM4JavaException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
