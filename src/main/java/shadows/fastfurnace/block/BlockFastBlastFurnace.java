package shadows.fastfurnace.block;

import net.minecraft.block.BlastFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import shadows.fastfurnace.tile.TileFastBlastFurnace;

public class BlockFastBlastFurnace extends BlastFurnaceBlock {

	public BlockFastBlastFurnace() {
		// TODO MCP-name?: func_235838_a_ -> lightValue
		super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).func_235838_a_((blockState) -> 13));
		setRegistryName("minecraft", "blast_furnace");
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileFastBlastFurnace();
	}
}