package shadows.fastfurnace;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IRegistryDelegate;
import shadows.fastfurnace.block.BlockFastBlastFurnace;
import shadows.fastfurnace.block.BlockFastFurnace;
import shadows.fastfurnace.block.BlockFastSmoker;
import shadows.fastfurnace.tile.TileFastBlastFurnace;
import shadows.fastfurnace.tile.TileFastFurnace;
import shadows.fastfurnace.tile.TileFastSmoker;
import shadows.placebo.util.PlaceboUtil;
import shadows.placebo.util.ReflectionHelper;

@Mod(FastFurnace.MODID)
public class FastFurnace {

	public static final String MODID = "fastfurnace";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static Map<IRegistryDelegate<Item>, Integer> VANILLA_BURNS = new HashMap<>();

	public static boolean useStrictMatching = true;

	public FastFurnace() {
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::blocks);
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
	public void setup(FMLCommonSetupEvent e) {
		Map<BlockState, PointOfInterestType> types = ReflectionHelper.getPrivateValue(PointOfInterestType.class, null, ASMAPI.mapField("field_221073_u"));
		Blocks.BLAST_FURNACE.getStateContainer().getValidStates().forEach(s -> types.put(s, PointOfInterestType.ARMORER));
		Blocks.SMOKER.getStateContainer().getValidStates().forEach(s -> types.put(s, PointOfInterestType.BUTCHER));
	}

}
