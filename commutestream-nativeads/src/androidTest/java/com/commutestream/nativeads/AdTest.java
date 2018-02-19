package com.commutestream.nativeads;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.commutestream.nativeads.components.ActionComponent;
import com.commutestream.nativeads.components.Colors;
import com.commutestream.nativeads.protobuf.Csnmessages;
import com.google.protobuf.ByteString;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static android.support.test.InstrumentationRegistry.getContext;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class AdTest {
    @Test
    public void newFromMessage() throws NoSuchAlgorithmException {
        AdRequest adRequest = new AdRequest();
        adRequest.addAgency("bill and ted");
        adRequest.addStop("bill and ted", "timemachine", "ancient rome");
        Csnmessages.AdResponse adResponse = addMockAd(responseBuilder(adRequest)).build();
        Csnmessages.NativeAd nativeAd = adResponse.getAds(0);
        Ad ad = new Ad(adResponse.getAds(0));
        assertThat(ad.getAdID(), equalTo(nativeAd.getAdId()));
        assertThat(ad.getRequestID(), equalTo(nativeAd.getRequestId()));
        assertThat(ad.getVersionID(), equalTo(nativeAd.getVersionId()));
        assertThat(ad.getAdvertiser().getComponentID(), equalTo(nativeAd.getAdvertiser().getComponentId()));
        assertThat(ad.getAdvertiser().getAdvertiser(), equalTo(nativeAd.getAdvertiser().getAdvertiser()));
        assertThat(ad.getHeadline().getComponentID(), equalTo(nativeAd.getHeadline().getComponentId()));
        assertThat(ad.getHeadline().getHeadline(), equalTo(nativeAd.getHeadline().getHeadline()));
        assertThat(ad.getBody().getComponentID(), equalTo(nativeAd.getBody().getComponentId()));
        assertThat(ad.getBody().getBody(), equalTo(nativeAd.getBody().getBody()));
        assertThat(ad.getLocation().getComponentID(), equalTo(nativeAd.getLocation().getComponentId()));
        assertThat(ad.getLocation().getName(), equalTo(nativeAd.getLocation().getName()));
        assertThat(ad.getLocation().getAddress(), equalTo(nativeAd.getLocation().getAddress()));
        assertThat(ad.getLocation().getLongitude(), equalTo(nativeAd.getLocation().getLocation().getLon()));
        assertThat(ad.getLocation().getLatitude(), equalTo(nativeAd.getLocation().getLocation().getLat()));
        assertThat(ad.getView().getComponentID(), equalTo(nativeAd.getView().getComponentId()));
        assertThat(ad.getSecondaryAction().getComponentID(), equalTo(nativeAd.getSecondaryActionScreen().getComponentId()));
        assertThat(ad.getSecondaryAction().getTitle(), equalTo(nativeAd.getSecondaryActionScreen().getTitle()));
        assertThat(ad.getSecondaryAction().getSubtitle(), equalTo(nativeAd.getSecondaryActionScreen().getSubtitle()));
        assertThat(ad.getLogo().getComponentID(), equalTo(nativeAd.getLogo().getComponentId()));
        assertThat(ad.getLogo().getLogo().sameAs(BitmapFactory.decodeByteArray(nativeAd.getLogo().getImage().toByteArray(), 0, nativeAd.getLogo().getImage().size())), equalTo(true));
        assertThat(ad.getHero().getComponentID(), equalTo(nativeAd.getHero().getComponentId()));
        //assertThat(ad.getHero().getKind(), equalTo();
        assertThat(ad.getHero().getImage().sameAs(BitmapFactory.decodeByteArray(nativeAd.getHero().getBlob().toByteArray(), 0, nativeAd.getHero().getBlob().size())), equalTo(true));
        assertThat(ad.getActions().size(), equalTo(nativeAd.getActionsCount()));
        for(int i = 0; i < ad.getActions().size(); i++) {
            assertEqualActions(ad.getActions().get(i), nativeAd.getActions(i));
        }
        assertEqualColors(ad.getColors(), nativeAd.getColors());
    }

    private byte[] getDrawable(int resId, Bitmap.CompressFormat format) {
        Resources res = getContext().getResources();
        Drawable drawable = res.getDrawable(resId);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(format, 100, stream);
        return stream.toByteArray();
    }

    private Csnmessages.AdResponse.Builder responseBuilder(AdRequest adRequest) throws
            NoSuchAlgorithmException {
        return Csnmessages.AdResponse.newBuilder()
                .setHashId(ByteString.copyFrom(adRequest.sha256()));
    }

    private Csnmessages.AdResponse.Builder addMockAd(Csnmessages.AdResponse.Builder responseBuilder) {
        Random r = new Random();
        responseBuilder.addAds(randomAd(r));
        return responseBuilder;
    }

    private void assertEqualActions(ActionComponent val, Csnmessages.ActionComponent msg) {
        assertThat(val.getComponentID(), equalTo(msg.getComponentId()));
        assertThat(val.getTitle(), equalTo(msg.getTitle()));
        assertThat(val.getUrl(), equalTo(msg.getUrl()));
        //assertThat(val.getKind(), equalTo(msg.getKind()));
        assertEqualColors(val.getColors(), msg.getColors());
    }

    private void assertEqualColors(Colors val, Csnmessages.Colors msg) {
        assertEqualColor(val.getForeground(), msg.getForeground());
        assertEqualColor(val.getBackground(), msg.getBackground());
    }

    private void assertEqualColor(int val, Csnmessages.Color msg) {
        assertThat(Colors.unpackRed(val), equalTo(msg.getRed()));
        assertThat(Colors.unpackGreen(val), equalTo(msg.getGreen()));
        assertThat(Colors.unpackBlue(val), equalTo(msg.getBlue()));
    }

    private Csnmessages.NativeAd randomAd(Random r) {
        byte[] logoBytes = getDrawable(R.drawable.csn_test_logo, Bitmap.CompressFormat.PNG);
        byte[] heroBytes = getDrawable(R.drawable.csn_test_hero, Bitmap.CompressFormat.JPEG);
        return Csnmessages.NativeAd.newBuilder()
                .setRequestId(r.nextLong())
                .setAdId(r.nextLong())
                .setVersionId(r.nextLong())
                .setColors(randomColors(r))
                .setAdvertiser(Csnmessages.AdvertiserComponent.newBuilder()
                        .setComponentId(r.nextLong())
                        .setAdvertiser("Advertiser " + r.nextLong())
                        .build()
                )
                .setHeadline(Csnmessages.HeadlineComponent.newBuilder()
                        .setComponentId(r.nextLong())
                        .setHeadline("Headline " + r.nextLong())
                )

                .setBody(Csnmessages.BodyComponent.newBuilder()
                        .setComponentId(r.nextLong())
                        .setBody("Body " + r.nextLong())
                        .build()
                )
                .setLocation(Csnmessages.LocationComponent.newBuilder()
                        .setComponentId(r.nextLong())
                        .setAddress("Address " + r.nextLong())
                        .setName("Location " + r.nextLong())
                        .setLocation(Csnmessages.Location.newBuilder()
                                .setLat(r.nextDouble())
                                .setLon(r.nextDouble())
                                .build()
                        )
                        .build()
                )
                .setView(Csnmessages.ViewComponent.newBuilder()
                        .setComponentId(r.nextLong())
                        .build()
                )
                .setSecondaryActionScreen(Csnmessages.SecondaryActionComponent.newBuilder()
                        .setComponentId(r.nextLong())
                        .setTitle("Secondary Title " + r.nextLong())
                        .setSubtitle("Secondary Subtitle " + r.nextLong())
                        .build()
                )
                .setLogo(Csnmessages.LogoComponent.newBuilder()
                        .setComponentId(r.nextLong())
                        .setImage(ByteString.copyFrom(logoBytes))
                        .build()
                )
                .setHero(Csnmessages.HeroComponent.newBuilder()
                        .setComponentId(r.nextLong())
                        .setKind(Csnmessages.HeroKind.Image)
                        .setBlob(ByteString.copyFrom(heroBytes))
                        .build()
                )
                .addActions(randomAction(r))
                .addActions(randomAction(r))
                .addActions(randomAction(r))
                .build();
    }


    private Csnmessages.ActionComponent randomAction(Random r) {
        return Csnmessages.ActionComponent.newBuilder()
                .setComponentId(r.nextLong())
                .setColors(randomColors(r))
                .setTitle("Action " + r.nextInt())
                .setKind(Csnmessages.ActionKind.Url)
                .setUrl("https://wikipedia.com?random=" + r.nextLong())
                .build();
    }

    private Csnmessages.Color randomColor(Random r) {
        return Csnmessages.Color.newBuilder()
                .setRed(r.nextInt(255))
                .setGreen(r.nextInt(255))
                .setBlue(r.nextInt(255))
                .build();
    }

    private Csnmessages.Colors randomColors(Random r) {
        return Csnmessages.Colors.newBuilder()
                .setForeground(randomColor(r))
                .setBackground(randomColor(r))
                .build();
    }
}
