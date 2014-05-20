package com.googlecode.madschuelerturnier.web;
/**
 * Apache License 2.0
 */
import org.primefaces.event.TabChangeEvent;
import org.springframework.stereotype.Component;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class SpeakerTabModel {

    private String open = "";

    private boolean is1Active = true;
    private boolean is2Active = true;
    private boolean is3Active = true;
    private boolean is4Active = true;
    private boolean is5Active = true;

    public String getOpen() {
        prepareOpen();
        return open;
    }

    public void setOpen(String str) {
        prepareOpen();
    }

    public void prepareOpen() {

        String ret = "";

        if (is1Active) {
            ret = ret + "0,";
        }

        if (is2Active) {
            ret = ret + "1,";
        }

        if (is3Active) {
            ret = ret + "2,";
        }

        if (is4Active) {
            ret = ret + "3,";
        }

        if (is5Active) {
            ret = ret + "4,";
        }
        if (ret.length() > 0) {
            ret = ret.substring(0, ret.length() - 1);
        }
        open = ret;
    }

    public void onTabChange(final TabChangeEvent event) {

    }

    public boolean isIs4Active() {
        return is4Active;
    }

    public void setIs4Active(boolean is4Active) {
        this.is4Active = is4Active;
    }

    public boolean isIs1Active() {
        return is1Active;
    }

    public void setIs1Active(boolean is1Active) {
        this.is1Active = is1Active;
    }

    public boolean isIs2Active() {
        return is2Active;
    }

    public void setIs2Active(boolean is2Active) {
        this.is2Active = is2Active;
    }

    public boolean isIs3Active() {
        return is3Active;
    }

    public void setIs3Active(boolean is3Active) {
        this.is3Active = is3Active;
    }

    public boolean isIs5Active() {
        return is5Active;
    }

    public void setIs5Active(boolean is5Active) {
        this.is5Active = is5Active;
    }

}