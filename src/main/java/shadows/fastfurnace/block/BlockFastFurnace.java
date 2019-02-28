package shadows.fastfurnace.block;

import java.util.Random;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFastFurnace extends BlockFurnace {

	public BlockFastFurnace(boolean burn) {
		super(burn);
		setHardness(3.5F);
		setSoundType(SoundType.STONE);
		setTranslationKey("furnace");
		setCreativeTab(CreativeTabs.DECORATIONS);
		if (burn) setLightLevel(0.875F);
	}

	static Item furnace;

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return furnace == null ? furnace = Item.getItemFromBlock(Blocks.FURNACE) : furnace;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState oldState) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != Blocks.FURNACE && state.getBlock() != Blocks.LIT_FURNACE) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileFastFurnace) {
				InventoryHelper.dropInventoryItems(world, pos, (TileFastFurnace) tileentity);
				world.updateComparatorOutputLevel(pos, this);
			}
			world.removeTileEntity(pos);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFastFurnace();
	}
}