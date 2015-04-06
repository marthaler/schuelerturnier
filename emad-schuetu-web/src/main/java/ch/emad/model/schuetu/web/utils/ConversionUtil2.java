/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.web.utils;

import org.springframework.stereotype.Component;

/**
 * Konvertiert typen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class ConversionUtil2 {

    public Long stringToLong(String in) {
        return Long.parseLong(in);
    }

}
