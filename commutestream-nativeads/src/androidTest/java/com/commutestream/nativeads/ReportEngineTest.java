package com.commutestream.nativeads;

import android.os.SystemClock;

import com.commutestream.nativeads.components.ViewComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;
import com.commutestream.nativeads.reporting.ReportEngine;

import org.junit.Test;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class ReportEngineTest {
    @Test
    public void addTwoInteractions() {
        Random rand = new Random();
        ViewComponent comp = new ViewComponent.Builder()
                .setComponentID(rand.nextLong())
                .build();
        Ad ad = new Ad.Builder()
                .setAdID(rand.nextLong())
                .setView(comp)
                .build();
        ReportEngine re = new ReportEngine(UUID.randomUUID(), UUID.randomUUID(), false, new ArrayList<InetAddress>());
        re.addInteraction(ad, comp, Csnmessages.ComponentInteractionKind.Tap);
        re.addInteraction(ad, comp, Csnmessages.ComponentInteractionKind.Tap);
        Csnmessages.AdReports reports = re.build();
        assertThat(reports.getAdReportsCount(), equalTo(1));
        assertThat(reports.getAdReports(0).getAdId(), equalTo(ad.getAdID()));
        assertThat(reports.getAdReports(0).getComponentsCount(), equalTo(1));
        assertThat(reports.getAdReports(0).getComponents(0).getInteractionsCount(), equalTo(2));
    }

    @Test
    public void addTwoVisibilitySamples() {
        long timestamp = System.currentTimeMillis();
        Random rand = new Random();
        ViewComponent comp = new ViewComponent.Builder()
                .setComponentID(rand.nextLong())
                .build();
        Ad ad = new Ad.Builder()
                .setAdID(rand.nextLong())
                .setView(comp)
                .build();
        ReportEngine re = new ReportEngine(UUID.randomUUID(), UUID.randomUUID(), false, new ArrayList<InetAddress>());
        re.addVisibility(ad, comp, 0.5, 0.25);
        re.addVisibility(ad, comp, 0.75, 0.4);
        Csnmessages.AdReports reports = re.build();
        assertThat(reports.getAdReportsCount(), equalTo(1));
        assertThat(reports.getAdReports(0).getAdId(), equalTo(ad.getAdID()));
        assertThat(reports.getAdReports(0).getComponentsCount(), equalTo(1));
        assertThat(reports.getAdReports(0).getComponents(0).getVisibilityEpoch(), greaterThanOrEqualTo(timestamp - 50));
        assertThat(reports.getAdReports(0).getComponents(0).getVisibilityEpoch(), lessThanOrEqualTo(timestamp + 50));
        assertThat(reports.getAdReports(0).getComponents(0).getVisibilitySampleCount(), equalTo(2L));
        assertThat(reports.getAdReports(0).getComponents(0).getViewVisibilitySamplesCount(), equalTo(1));
        assertThat(reports.getAdReports(0).getComponents(0).getDeviceVisibilitySamplesCount(), equalTo(1));
    }
}
