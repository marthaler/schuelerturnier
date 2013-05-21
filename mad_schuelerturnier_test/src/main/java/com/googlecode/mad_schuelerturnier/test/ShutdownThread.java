package com.googlecode.mad_schuelerturnier.test;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 06.01.13
 * Time: 19:47
 * To change this template use File | Settings | File Templates.
 */
public class ShutdownThread extends Thread {

    @Override
    public void run() {
        ShutdownableRegistry.getInstance().shutdownReal();
    }

}
