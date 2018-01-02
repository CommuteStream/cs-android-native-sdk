package com.commutestream.nativeads;


import com.commutestream.nativeads.reporting.EncodingUtils;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EncodingUtilsTest {
    @Test
    public void testEncodeVisibility() {
        assertThat((int)EncodingUtils.encodeVisibility(0.0), equalTo(0));
        assertThat((int)EncodingUtils.encodeVisibility(1.0), equalTo(15));
        assertThat((int)EncodingUtils.encodeVisibility(0.5), equalTo(8));
    }

    @Test
    public void testSetVisibilitySample() {
        long sample = 0x0000DEADBABE0000L;
        assertThat(EncodingUtils.setVisibilitySample(sample, 0, (byte)0x01), equalTo(0X0000DEADBABE0001L));
        assertThat(EncodingUtils.setVisibilitySample(sample, 15, (byte)0x01), equalTo(0X1000DEADBABE0000L));
    }
}
