package dev.shadowsoffire.fastfurnace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(FastFurnace.MODID)
public class FastFurnace {

    public static final String MODID = "fastfurnace";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public FastFurnace() {
        String version = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString();
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> version, (remoteVer, isNetwork) -> remoteVer == null || version.equals(remoteVer)));
    }

}
