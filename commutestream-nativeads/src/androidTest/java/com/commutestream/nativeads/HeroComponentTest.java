package com.commutestream.nativeads;

import android.graphics.Bitmap;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.commutestream.nativeads.components.HeroComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;
import com.google.protobuf.ByteString;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class HeroComponentTest {
    @Test
    public void newFromMessageImage() {
        long componentID = 505;
        int black = 0;
        int white = 255 << 16 | 255 << 8 | 255;
        Bitmap img = Bitmap.createBitmap(new int[]{black,white,black,white},2, 2, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 0, outStream);
        Csnmessages.HeroComponent msg = Csnmessages.HeroComponent.newBuilder()
                .setComponentId(componentID)
                .setKind(Csnmessages.HeroKind.Image)
                .setBlob(ByteString.copyFrom(outStream.toByteArray()))
                .build();
        HeroComponent hero = new HeroComponent(msg);
        assertThat(hero.getComponentID(), equalTo(componentID));
        assertThat(hero.getKind(), equalTo(HeroComponent.HERO_IMAGE));
        assertThat(hero.getImage(), notNullValue());
        assertThat(hero.getHtml(), nullValue());
        assertThat(hero.getImage().getWidth(), equalTo(img.getWidth()));
        assertThat(hero.getImage().getHeight(), equalTo(img.getHeight()));
        CSNLog.v("image test complete");
    }

    @Test
    public void newFromMessageHtml() {
        long componentID = 505;
        String html = "<html><body><h1>Ad Here</h1></body></html>";
        Csnmessages.HeroComponent msg = Csnmessages.HeroComponent.newBuilder()
                .setComponentId(componentID)
                .setKind(Csnmessages.HeroKind.HTML)
                .setBlob(ByteString.copyFrom(html.getBytes()))
                .build();
        HeroComponent hero = new HeroComponent(msg);
        assertThat(hero.getComponentID(), equalTo(componentID));
        assertThat(hero.getKind(), equalTo(HeroComponent.HERO_HTML));
        assertThat(hero.getImage(), nullValue());
        assertThat(hero.getHtml(), notNullValue());
        assertThat(hero.getHtml(), equalTo(html));
        CSNLog.v("html test complete");
    }
}
