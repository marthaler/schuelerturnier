/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu;

import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class SpeakerReloadAction implements Action {

    public Event execute(RequestContext context) {
        return new Event(this, "speaker_view");
    }
}