package shadows.fastfurnace.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SmokerBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import shadows.fastfurnace.tile.TileFastSmoker;

public class BlockFastSmoker extends SmokerBlock {

	public BlockFastSmoker() {
		super(Block.Properties.from(Blocks.SMOKER));
		setRegistryName("minecraft", "smoker");
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileFastSmoker();
	}
}