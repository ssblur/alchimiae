package com.ssblur.fabric.client;

import com.ssblur.alchimiae.AlchimiaeMod;
import net.fabricmc.api.ClientModInitializer;

public final class AlchimiaeModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AlchimiaeMod.INSTANCE.clientInit();
    }
}
