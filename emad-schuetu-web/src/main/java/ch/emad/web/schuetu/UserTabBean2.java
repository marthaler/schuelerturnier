/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu;

import org.primefaces.event.TabChangeEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("session")
@Component
public class UserTabBean2 {

    private int activeIndex = 0;

    public void onTabChange(TabChangeEvent event) {
        activeIndex = Integer.parseInt(event.getTab().getId().replace("tab", ""));
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }
}
                    