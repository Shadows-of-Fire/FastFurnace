package shadows.fastfurnace.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

@Mixin(AbstractFurnaceTileEntity.class)
public abstract class MixinAbstractFurnaceTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {

	protected MixinAbstractFurnaceTileEntity(TileEntityType<?> p_i48285_1_) {
		super(p_i48285_1_);
	}

	protected AbstractCookingRecipe curRecipe;
	protected ItemStack failedMatch = ItemStack.EMPTY;

	@Shadow
	protected IRecipeType<? extends AbstractCookingRecipe> recipeType;

	@SuppressWarnings("unchecked")
	protected AbstractCookingRecipe getRecipe() {
		ItemStack input = this.getItem(0);
		if (input.isEmpty() || (ItemStack.isSame(failedMatch, input) && ItemStack.tagMatches(failedMatch, input))) return null;
		if (curRecipe != null && curRecipe.matches(this, level)) return curRecipe;
		else {
			AbstractCookingRecipe rec = level.getRecipeManager().getRecipeFor((IRecipeType<AbstractCookingRecipe>) this.recipeType, this, this.level).orElse(null);
			System.out.println(rec);
			if (rec == null) failedMatch = input.copy();
			else failedMatch = ItemStack.EMPTY;
			return curRecipe = rec;
		}
	}

	@Overwrite
	protected int getTotalCookTime() {
		AbstractCookingRecipe rec = getRecipe();
		return rec == null ? 200 : rec.getCookingTime();
	}

	@Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/crafting/RecipeManager;getRecipeFor(Lnet/minecraft/item/crafting/IRecipeType;Lnet/minecraft/inventory/IInventory;Lnet/minecraft/world/World;)Ljava/util/Optional;"), require = 1)
	public Optional<AbstractCookingRecipe> getRecipe(RecipeManager mgr, IRecipeType<AbstractCookingRecipe> type, IInventory inv, World level) {
		return Optional.ofNullable(getRecipe());
	}

}
