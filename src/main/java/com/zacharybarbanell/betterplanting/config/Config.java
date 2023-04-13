package com.zacharybarbanell.betterplanting.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static class Server {
        public final ForgeConfigSpec.BooleanValue autoSelectCrops;
        public final ForgeConfigSpec.DoubleValue minHeight;
        public Server(ForgeConfigSpec.Builder builder) {
            autoSelectCrops = builder
                .comment("Whether the mod should try to automatically determine which crops to handle.  If this is set to false, modded items must be tagged with betterplanting:plantable to be planted.")
                .define("autoSelectCrops", true);

            minHeight = builder
                .comment("The minimum height that items must fall to be planted.  Setting this below 0.864 is not recommended, since it will allow items to be planted after being broken.")
                .defineInRange("minHeight", 0.864, 0, Float.POSITIVE_INFINITY);
        }

        public boolean autoSelectCrops() {
            return autoSelectCrops.get();
        }

        public double minHeight() {
            return minHeight.get();
        }
    }

    public static final Server SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;
    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();
    }
}
