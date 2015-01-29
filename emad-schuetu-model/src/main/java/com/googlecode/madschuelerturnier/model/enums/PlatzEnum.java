/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.enums;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public enum PlatzEnum {
    A("A"), B("B"), C("C");

    private String text;

    PlatzEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static PlatzEnum fromString(String text) {
        if (text != null) {
            for (PlatzEnum b : PlatzEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
