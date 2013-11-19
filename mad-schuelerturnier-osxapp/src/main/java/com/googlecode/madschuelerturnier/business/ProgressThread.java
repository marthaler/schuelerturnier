package com.googlecode.madschuelerturnier.business;

import javafx.scene.control.ProgressIndicator;

/**
 * Created by dama on 15.11.13.
 */
public class ProgressThread extends Thread{

    private ProgressIndicator progress;

    public ProgressThread(ProgressIndicator progress){
        this.progress = progress;
        this.start();
        progress.setVisible(true);
    }
    public void run(){
        for(double i = 0;i < 1;i+=0.01){
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progress.setProgress(i);
        }
        progress.setVisible(false);
    }
}
