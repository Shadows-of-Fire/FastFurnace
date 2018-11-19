package shadows.fastfurnace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import shadows.fastfurnace.block.BlockFastFurnace;
import shadows.fastfurnace.block.TileFastFurnace;

@Mod(modid = FastFurnace.MODID, name = FastFurnace.MODNAME, version = FastFurnace.VERSION)
public class FastFurnace {

	public static final String MODID = "fastfurnace";
	public static final String MODNAME = "FastFurnace";
	public static final String VERSION = "1.2.1";

	public static final Logger LOG = LogManager.getLogger(MODID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
		GameRegistry.registerTileEntity(TileFastFurnace.class, new ResourceLocation("minecraft", "furnace"));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void blockBois(Register<Block> e) {
		Block b = new BlockFastFurnace(false).setRegistryName("minecraft", "furnace");
		e.getRegistry().registerAll(b, new BlockFastFurnace(true).setRegistryName("minecraft", "lit_furnace"));
		ForgeRegistries.ITEMS.register(new ItemBlock(b) {
			public String getCreatorModId(net.minecraft.item.ItemStack itemStack) {
				return MODID;
			}
		}.setRegistryName(b.getRegistryName()));
	}

}
