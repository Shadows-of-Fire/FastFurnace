package shadows.fastfurnace;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IRegistryDelegate;
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
	public static Map<IRegistryDelegate<Item>, Integer> VANILLA_BURNS = new HashMap<>();

	public static boolean useStrictMatching = true;

	public FastFurnace() {
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::blocks);
		FurnaceTileEntity.getBurnTimes().entrySet().stream().forEach(e -> VANILLA_BURNS.put(e.getKey().delegate, e.getValue()));
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

	public static boolean isFuel(ItemStack stack) {
		int ret = stack.getBurnTime();
		return ForgeEventFactory.getItemBurnTime(stack, ret == -1 ? VANILLA_BURNS.getOrDefault(stack.getItem().delegate, 0) : ret) > 0;
	}
}
