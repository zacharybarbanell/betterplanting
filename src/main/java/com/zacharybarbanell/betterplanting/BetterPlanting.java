package com.zacharybarbanell.betterplanting;

import com.zacharybarbanell.betterplanting.config.Config;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod("betterplanting")
public class BetterPlanting {
    public BetterPlanting() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
    }
}
