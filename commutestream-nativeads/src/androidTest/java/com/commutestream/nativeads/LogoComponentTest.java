package com.commutestream.nativeads;

import android.graphics.Bitmap;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.commutestream.nativeads.components.LogoComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;
import com.google.protobuf.ByteString;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LogoComponentTest {
    @Test
    public void newFromMessage() {
        long componentID = 1234;
        int black = 0;
        int white = 255 << 16 | 255 << 8 | 255;
        Bitmap img = Bitmap.createBitmap(new int[]{black,white,black,white},2, 2, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 0, outStream);
        Csnmessages.LogoComponent msg = Csnmessages.LogoComponent.newBuilder()
                .setComponentId(componentID)
                .setImage(ByteString.copyFrom(outStream.toByteArray()))
                .build();
        LogoComponent logo = new LogoComponent(msg);
        assertThat(logo.getComponentID(), equalTo(componentID));
        assertThat(logo.getLogo().getWidth(), equalTo(img.getWidth()));
        assertThat(logo.getLogo().getHeight(), equalTo(img.getHeight()));
    }
}
