package shadows.fastfurnace.block;

import net.minecraft.block.BlastFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import shadows.fastfurnace.tile.TileFastBlastFurnace;

public class BlockFastBlastFurnace extends BlastFurnaceBlock {

	public BlockFastBlastFurnace() {
		super(Block.Properties.from(Blocks.BLAST_FURNACE));
		setRegistryName("minecraft", "blast_furnace");
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileFastBlastFurnace();
	}
}