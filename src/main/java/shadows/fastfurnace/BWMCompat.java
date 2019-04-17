package shadows.fastfurnace;

import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.crafting.HCFurnace;

public class BWMCompat {

	public static boolean isBWMFurnaceEnabled() {
		return ModuleLoader.isFeatureEnabled(HCFurnace.class);
	}

}
