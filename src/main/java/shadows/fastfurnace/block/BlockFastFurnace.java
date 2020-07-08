package shadows.fastfurnace.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import shadows.fastfurnace.tile.TileFastFurnace;

public class BlockFastFurnace extends FurnaceBlock {

	public BlockFastFurnace() {
		// TODO MCP-name?: func_235838_a_ -> lightValue
		super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).func_235838_a_((blockState) -> 13));
		setRegistryName("minecraft", "furnace");
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileFastFurnace();
	}
}