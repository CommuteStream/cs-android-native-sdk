package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages;

import java.util.HashSet;
import java.util.Set;

public class AdUnitSettings {
    public boolean enabled;
    public Set<String> agencies;

    AdUnitSettings(Csnmessages.AdUnitSettings settings) {
        enabled = false;
        agencies = new HashSet();
    }
}
