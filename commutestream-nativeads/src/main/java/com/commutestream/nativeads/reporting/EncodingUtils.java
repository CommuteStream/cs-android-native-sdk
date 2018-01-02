package com.commutestream.nativeads.reporting;

public class EncodingUtils {

    private EncodingUtils() {}

    // encode a double visibility percentage (0.0 to 1.0) value into a 4 bit histogram value
    public static byte encodeVisibility(double visibility) {
        return (byte)(Math.round(visibility*15.0) & 0x0F);
    }

    // set a 4 bit portion of a 64bit value to the sample value which is a 4 bit value itself
    public static long setVisibilitySample(long sample, int position, byte value) {
        long shift = position*4;
        long valueMask = 0x000000000000000FL;
        valueMask = valueMask << shift;
        long sampleMask = valueMask ^ 0xFFFFFFFFFFFFFFFFL;
        long shiftedValue = ((long)value) << shift;
        long maskedSample = sample & sampleMask;
        long newSample = maskedSample | shiftedValue;
        return newSample;
    }
}
