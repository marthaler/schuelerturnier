package ch.emad.model.schuetu.web.integration;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;

class JunitAppender extends AppenderSkeleton {

    private final List<LoggingEvent> log = new ArrayList<LoggingEvent>();
    private final List<LoggingEvent> logerror = new ArrayList<LoggingEvent>();

    @Override
    public boolean requiresLayout() {
        return false;
    }

    @Override
    protected void append(final LoggingEvent loggingEvent) {
        if (loggingEvent.getLevel() == Level.ERROR || loggingEvent.getLevel() == Level.FATAL) {
            logerror.add(loggingEvent);
        }
        log.add(loggingEvent);
    }

    @Override
    public void close() {
        // noop
    }

    public boolean hasErrors() {
        if (logerror.size() > 0) {
            return true;
        }
        return false;
    }

}