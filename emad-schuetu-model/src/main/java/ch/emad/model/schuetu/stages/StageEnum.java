/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */
package ch.emad.model.schuetu.stages;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.0.1
 */
public enum StageEnum {
    DEVELOPMENT("dev"),TEST("test"),PRODUCTION("prod"), INTEGRATION("int"),VERSION("vers");

    private String text;

    StageEnum(String text) {
        this.text = text;
    }

    public static StageEnum fromString(String text) {
        if (text != null) {
            for (StageEnum b : StageEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return StageEnum.DEVELOPMENT;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public String toString() {
        return text;
    }
}
