/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.enums;

/**
 * Eine Anrede
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.13
 */
public enum AnredeEnum {
    FRAU("Frau"), HERR("Herr"), AN("An");

    private String text;

    AnredeEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static AnredeEnum fromString(String text) {
        if (text != null) {
            for (AnredeEnum b : AnredeEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return AnredeEnum.AN;
    }
}
