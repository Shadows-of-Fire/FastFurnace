package shadows.fastfurnace;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(FastFurnace.MODID)
public class FastFurnace {

	public static final String MODID = "fastfurnace";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public FastFurnace() {
		String version = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString();
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> version, (remoteVer, isNetwork) -> remoteVer == null || version.equals(remoteVer)));
	}

}
