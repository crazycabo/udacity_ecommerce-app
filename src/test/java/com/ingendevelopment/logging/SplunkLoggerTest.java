package com.ingendevelopment.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Brian Smith on 10/27/21.
 * Description:
 */
@RunWith(MockitoJUnitRunner.class)
public class SplunkLoggerTest {

    @Spy
    private SplunkLogger splunkLogger;

    @Test
    public void logEventToSplunk() {
        splunkLogger.logMessage("failure", "controller", SplunkLogger.Severity.WARNING);

        verify(splunkLogger, times(1))
                .sendEvent("failure", "controller", SplunkLogger.Severity.WARNING);
    }

    @Test
    public void logExceptionEventToSplunk() {
        splunkLogger.logException(new RuntimeException("Uncaught Exception"), "controller");

        verify(splunkLogger, times(1))
                .sendEvent("Uncaught Exception", "controller", SplunkLogger.Severity.ERROR);
    }
}
