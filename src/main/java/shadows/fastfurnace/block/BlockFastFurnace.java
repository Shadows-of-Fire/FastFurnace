package shadows.fastfurnace.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import shadows.fastfurnace.tile.TileFastFurnace;
import shadows.placebo.util.IReplacementBlock;

public class BlockFastFurnace extends FurnaceBlock implements IReplacementBlock {

	public BlockFastFurnace() {
		super(Block.Properties.from(Blocks.FURNACE));
		setRegistryName("minecraft", "furnace");
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileFastFurnace();
	}

	@Override
	public void _setDefaultState(BlockState state) {
		this.setDefaultState(state);
	}

	protected StateContainer<Block, BlockState> container;

	@Override
	public void setStateContainer(StateContainer<Block, BlockState> container) {
		this.container = container;
	}

	@Override
	public StateContainer<Block, BlockState> getStateContainer() {
		return container == null ? super.getStateContainer() : container;
	}
}