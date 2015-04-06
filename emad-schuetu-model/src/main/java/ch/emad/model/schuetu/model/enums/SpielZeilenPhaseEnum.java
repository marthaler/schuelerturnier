/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.enums;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public enum SpielZeilenPhaseEnum {
    A_ANSTEHEND("A_ANSTEHEND"), B_ZUR_VORBEREITUNG("B_ZUR_VORBEREITUNG"), C_VORBEREITET("C_VORBEREITET"), D_SPIELEND("D_SPIELEND"), E_BEENDET("E_BEENDET");

    private String text;

    SpielZeilenPhaseEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static SpielZeilenPhaseEnum fromString(String text) {
        if (text != null) {
            for (SpielZeilenPhaseEnum b : SpielZeilenPhaseEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return SpielZeilenPhaseEnum.A_ANSTEHEND;
    }
}
