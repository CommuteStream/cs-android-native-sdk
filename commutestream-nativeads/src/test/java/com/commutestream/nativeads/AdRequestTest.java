package com.commutestream.nativeads;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class AdRequestTest {
    @Test
    public void testAgencyOnly() throws NoSuchAlgorithmException {
        TransitAgency ta = new TransitAgency("bogus");
        AdRequest ar = new AdRequest();
        String hash = ar.sha256();
        ar.addAgency(ta);
        assertThat(hash, not(ar.sha256()));
    }

    @Test
    public void testRouteOnly() throws NoSuchAlgorithmException {
        TransitRoute tr = new TransitRoute("bogus", "hocus");
        AdRequest ar = new AdRequest();
        String hash = ar.sha256();
        ar.addRoute(tr);
        assertThat(hash, not(ar.sha256()));
    }

    @Test
    public void testStopOnly() throws NoSuchAlgorithmException {
        TransitStop ts = new TransitStop("bogus", "hocus", "pocus");
        AdRequest ar = new AdRequest();
        String hash = ar.sha256();
        ar.addStop(ts);
        assertThat(hash, not(ar.sha256()));
    }

    @Test(expected=NullPointerException.class)
    public void testStopOnlyWithNull() throws NoSuchAlgorithmException {
        TransitStop ts = new TransitStop("bogus", null, "pocus");
        AdRequest ar = new AdRequest();
        String hash = ar.sha256();
        ar.addStop(ts);
        assertThat(hash, not(ar.sha256()));
    }

    @Test
    public void testConvienenceAdd() throws NoSuchAlgorithmException {
        AdRequest ar = new AdRequest();
        String hash = ar.sha256();
        ar.addAgency("bogus");
        assertThat(hash, not(ar.sha256()));
        hash = ar.sha256();
        ar.addRoute("bogus", "route");
        assertThat(hash, not(ar.sha256()));
        hash = ar.sha256();
        ar.addStop("bogus", "route", "stop");
        assertThat(hash, not(ar.sha256()));
    }
}
