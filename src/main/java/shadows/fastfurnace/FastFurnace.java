package shadows.fastfurnace;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import shadows.fastfurnace.block.BlockFastBlastFurnace;
import shadows.fastfurnace.block.BlockFastFurnace;
import shadows.fastfurnace.block.BlockFastSmoker;
import shadows.fastfurnace.tile.TileFastBlastFurnace;
import shadows.fastfurnace.tile.TileFastFurnace;
import shadows.fastfurnace.tile.TileFastSmoker;
import shadows.placebo.util.PlaceboUtil;

@Mod(FastFurnace.MODID)
public class FastFurnace {

	public static final String MODID = "fastfurnace";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static Map<Item, Integer> VANILLA_BURNS;

	public static boolean useStrictMatching = true;

	public FastFurnace() {
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::blocks);
		MinecraftForge.EVENT_BUS.addListener(this::started);
	}

	@SubscribeEvent
	public void blocks(Register<Block> e) {
		Block b;
		PlaceboUtil.registerOverrideBlock(b = new BlockFastFurnace(), MODID);
		TileEntityType.FURNACE.factory = TileFastFurnace::new;
		TileEntityType.FURNACE.validBlocks = ImmutableSet.of(b);
		PlaceboUtil.registerOverrideBlock(b = new BlockFastBlastFurnace(), MODID);
		TileEntityType.BLAST_FURNACE.factory = TileFastBlastFurnace::new;
		TileEntityType.BLAST_FURNACE.validBlocks = ImmutableSet.of(b);
		PlaceboUtil.registerOverrideBlock(b = new BlockFastSmoker(), MODID);
		TileEntityType.SMOKER.factory = TileFastSmoker::new;
		TileEntityType.SMOKER.validBlocks = ImmutableSet.of(b);
	}

	@SubscribeEvent
	public void started(FMLServerStartingEvent e) {
		VANILLA_BURNS = AbstractFurnaceTileEntity.getBurnTimes();
		e.getServer().getResourceManager().addReloadListener(new ReloadListener<Object>() {

			@Override
			protected Object prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
				return null;
			}

			@Override
			protected void apply(Object splashList, IResourceManager resourceManagerIn, IProfiler profilerIn) {
				VANILLA_BURNS = AbstractFurnaceTileEntity.getBurnTimes();
			}
		});

	}

	public static boolean isFuel(ItemStack stack) {
		int ret = stack.getBurnTime();
		return ForgeEventFactory.getItemBurnTime(stack, ret == -1 ? VANILLA_BURNS.getOrDefault(stack.getItem(), 0) : ret) > 0;
	}
}
