/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.helper;

import org.apache.log4j.Logger;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */

public final class IDGeneratorContainer {

    private static final Logger LOG = Logger.getLogger(IDGeneratorContainer.class);

    private static int pointer1 = -0;
    private static int pointer2 = -1;

    private static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private IDGeneratorContainer() {

    }

    public static synchronized String getNext() {

        IDGeneratorContainer.pointer2++;

        if (IDGeneratorContainer.pointer2 >= IDGeneratorContainer.letters.length) {
            IDGeneratorContainer.pointer1++;
            IDGeneratorContainer.pointer2 = 0;
        }

        if (IDGeneratorContainer.pointer1 >= letters.length) {
            IDGeneratorContainer.pointer1 = 0;
            LOG.warn("!!! achtung, id's fangen wieder von vorne an, limite wurde erreicht");
        }

        String temp = IDGeneratorContainer.letters[IDGeneratorContainer.pointer1] + IDGeneratorContainer.letters[IDGeneratorContainer.pointer2];

        return temp;
    }

}