/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.enums;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public enum SpielEnum {
    GRUPPE("GRUPPE"), GFINAL("GFINAL"), KFINAL("KFINAL");

    private String text;

    SpielEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static SpielEnum fromString(String text) {
        if (text != null) {
            for (SpielEnum b : SpielEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
