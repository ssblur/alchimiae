package com.ssblur.fabric;

import com.ssblur.alchimiae.AlchimiaeMod;
import net.fabricmc.api.ModInitializer;

public final class AlchimiaeModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AlchimiaeMod.init();
    }
}
