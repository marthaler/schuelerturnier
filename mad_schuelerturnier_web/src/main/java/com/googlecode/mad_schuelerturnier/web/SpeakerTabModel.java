package com.googlecode.mad_schuelerturnier.web;

import org.primefaces.event.TabChangeEvent;
import org.springframework.stereotype.Component;

@Component
public class SpeakerTabModel {

    private String open = "";

    private boolean _1Active = true;
    private boolean _2Active = true;
    private boolean _3Active = true;
    private boolean _4Active = true;
    private boolean _5Active = true;

    public String getOpen(){
        prepareOpen();
        return open;
    }

    public void setOpen(String str){
        prepareOpen();
    }

    public void prepareOpen(){

       String ret = "";

        if(_1Active){
            ret = ret + "0,";
        }

        if(_2Active){
            ret = ret + "1,";
        }

        if(_3Active){
            ret = ret + "2,";
        }

        if(_4Active){
            ret = ret + "3,";
        }

        if(_5Active){
            ret = ret + "4,";
        }
        if(ret.length()> 0){
        ret = ret.substring(0,ret.length() -1);
        }
        open = ret;
    }

    public void onTabChange(final TabChangeEvent event) {
        //System.out.println(event.getTab().getChildCount());
    }

    public boolean is_4Active() {
        return _4Active;
    }

    public void set_4Active(boolean _4Active) {
        this._4Active = _4Active;
    }

    public boolean is_1Active() {
        return _1Active;
    }

    public void set_1Active(boolean _1Active) {
        this._1Active = _1Active;
    }

    public boolean is_2Active() {
        return _2Active;
    }

    public void set_2Active(boolean _2Active) {
        this._2Active = _2Active;
    }

    public boolean is_3Active() {
        return _3Active;
    }

    public void set_3Active(boolean _3Active) {
        this._3Active = _3Active;
    }

    public boolean is_5Active() {
        return _5Active;
    }

    public void set_5Active(boolean _5Active) {
        this._5Active = _5Active;
    }

}