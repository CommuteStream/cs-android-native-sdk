package com.commutestream.nativeads;

import com.commutestream.nativeads.reporting.ImpressionDetector;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ImpressionDetectorTest {
    @Test
    public void testVisibilityImpression() throws InterruptedException {
        ImpressionDetector id = new ImpressionDetector(1000, 0.1, 10, 10);
        assertThat(id.addVisibility(0.1, 0.0), equalTo(false));
        Thread.sleep(10);
        assertThat(id.addVisibility(0.0, 0.0), equalTo(false));
        assertThat(id.addVisibility(0.11, 0.0), equalTo(false));
        Thread.sleep(20);
        assertThat(id.addVisibility(0.11, 0.0), equalTo(true));
        assertThat(id.addVisibility(0.11, 0.0), equalTo(false));
        Thread.sleep(20);
        assertThat(id.addVisibility(0.11, 0.0), equalTo(true));
    }
}
