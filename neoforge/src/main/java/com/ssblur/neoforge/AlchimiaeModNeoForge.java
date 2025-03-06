package com.ssblur.neoforge;

import com.ssblur.alchimiae.AlchimiaeMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(AlchimiaeMod.MOD_ID)
public final class AlchimiaeModNeoForge {
    public AlchimiaeModNeoForge() {
        AlchimiaeMod.INSTANCE.init();
        if (FMLEnvironment.dist == Dist.CLIENT) AlchimiaeMod.INSTANCE.clientInit();
    }
}
