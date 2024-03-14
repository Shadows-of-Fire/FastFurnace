package dev.shadowsoffire.fastfurnace.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager.CachedCheck;
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

    @Unique
    protected AbstractCookingRecipe curRecipe;
    @Unique
    protected ItemStack failedMatch = ItemStack.EMPTY;

    @Shadow
    protected RecipeType<? extends AbstractCookingRecipe> recipeType;

    @SuppressWarnings("unchecked")
    protected AbstractCookingRecipe getRecipe() {
        ItemStack input = this.getItem(0);
        if (input.isEmpty() || ItemStack.isSameItemSameTags(this.failedMatch, input)) return null;
        if (this.curRecipe != null && this.curRecipe.matches(this, this.level)) return this.curRecipe;
        else {
            AbstractCookingRecipe rec = this.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) this.recipeType, this, this.level).orElse(null);
            if (rec == null) this.failedMatch = input.copy();
            else this.failedMatch = ItemStack.EMPTY;
            return this.curRecipe = rec;
        }
    }

    // Has to be public due to possiblity of AT application to this method.
    @Inject(at = @At("HEAD"), method = "getTotalCookTime", cancellable = true)
    private static void fastfurnace_useFFRecipeCache(Level pLevel, AbstractFurnaceBlockEntity pBlockEntity, CallbackInfoReturnable<Integer> cir) {
        AbstractCookingRecipe rec = ((MixinAbstractFurnaceBlockEntity) (Object) pBlockEntity).getRecipe();
        cir.setReturnValue(rec == null ? 200 : rec.getCookingTime());
    }

    @Redirect(method = "serverTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager$CachedCheck;getRecipeFor(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"), require = 1)
    private static Optional<AbstractCookingRecipe> getRecipe(CachedCheck<?, ?> c, Container inv, Level level) {
        return Optional.ofNullable(((MixinAbstractFurnaceBlockEntity) inv).getRecipe());
    }

}
