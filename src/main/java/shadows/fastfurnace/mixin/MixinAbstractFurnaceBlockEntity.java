package shadows.fastfurnace.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity extends BaseContainerBlockEntity {

	protected MixinAbstractFurnaceBlockEntity(BlockEntityType<?> p_155076_, BlockPos p_155077_, BlockState p_155078_) {
		super(p_155076_, p_155077_, p_155078_);
	}

	protected AbstractCookingRecipe curRecipe;
	protected ItemStack failedMatch = ItemStack.EMPTY;

	@Shadow
	protected RecipeType<? extends AbstractCookingRecipe> recipeType;

	@SuppressWarnings("unchecked")
	protected AbstractCookingRecipe getRecipe() {
		ItemStack input = this.getItem(0);
		if (input.isEmpty() || (ItemStack.isSame(failedMatch, input) && ItemStack.tagMatches(failedMatch, input))) return null;
		if (curRecipe != null && curRecipe.matches(this, level)) return curRecipe;
		else {
			AbstractCookingRecipe rec = level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) this.recipeType, this, this.level).orElse(null);
			if (rec == null) failedMatch = input.copy();
			else failedMatch = ItemStack.EMPTY;
			return curRecipe = rec;
		}
	}

	@Overwrite
	private static int getTotalCookTime(Level pLevel, RecipeType<? extends AbstractCookingRecipe> pRecipeType, Container pContainer) {
		AbstractCookingRecipe rec = ((MixinAbstractFurnaceBlockEntity) pContainer).getRecipe();
		return rec == null ? 200 : rec.getCookingTime();
	}

	@Redirect(method = "serverTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"), require = 1)
	private static Optional<AbstractCookingRecipe> getRecipe(RecipeManager mgr, RecipeType<AbstractCookingRecipe> type, Container inv, Level level) {
		return Optional.ofNullable(((MixinAbstractFurnaceBlockEntity) inv).getRecipe());
	}

}