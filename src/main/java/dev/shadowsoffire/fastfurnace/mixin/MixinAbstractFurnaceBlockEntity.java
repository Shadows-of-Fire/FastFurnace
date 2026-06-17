package dev.shadowsoffire.fastfurnace.mixin;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager.CachedCheck;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = AbstractFurnaceBlockEntity.class, remap = false)
public abstract class MixinAbstractFurnaceBlockEntity extends BaseContainerBlockEntity {

    protected MixinAbstractFurnaceBlockEntity(BlockEntityType<?> p_155076_, BlockPos p_155077_, BlockState p_155078_) {
        super(p_155076_, p_155077_, p_155078_);
    }

    @Unique
    @Nullable
    protected RecipeHolder<AbstractCookingRecipe> curRecipe;

    @Unique
    protected ItemStack failedMatch = ItemStack.EMPTY;

    @Shadow
    protected RecipeType<? extends AbstractCookingRecipe> recipeType;

    @Nullable
    @SuppressWarnings("unchecked")
    protected RecipeHolder<AbstractCookingRecipe> getRecipe(ServerLevel level) {
        ItemStack input = this.getItem(0);
        if (input.isEmpty() || ItemStack.isSameItemSameComponents(this.failedMatch, input)) {
            return null;
        }

        SingleRecipeInput recipeInput = new SingleRecipeInput(input);
        if (this.curRecipe != null && this.curRecipe.value().matches(recipeInput, level)) {
            return this.curRecipe;
        }
        else {
            RecipeHolder<AbstractCookingRecipe> rec = level.recipeAccess()
                .getRecipeFor((RecipeType<AbstractCookingRecipe>) this.recipeType, recipeInput, level)
                .orElse(null);

            if (rec == null) {
                this.failedMatch = input.copy();
            }
            else {
                this.failedMatch = ItemStack.EMPTY;
            }

            return this.curRecipe = rec;
        }
    }

    @Inject(at = @At("HEAD"), method = "getTotalCookTime", cancellable = true)
    private static void fastfurnace_useFFRecipeCache(ServerLevel pLevel, AbstractFurnaceBlockEntity pBlockEntity, CallbackInfoReturnable<Integer> cir) {
        RecipeHolder<AbstractCookingRecipe> rec = ((MixinAbstractFurnaceBlockEntity) (Object) pBlockEntity).getRecipe(pLevel);
        cir.setReturnValue(rec == null ? 200 : rec.value().cookingTime());
    }

    @Redirect(method = "serverTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager$CachedCheck;getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/server/level/ServerLevel;)Ljava/util/Optional;"), require = 1)
    private static Optional<RecipeHolder<?>> fastfurnace_getRecipe(CachedCheck<?, ?> c, RecipeInput inv, ServerLevel level, ServerLevel levelAgain, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity) {
        return Optional.ofNullable(((MixinAbstractFurnaceBlockEntity) (Object) blockEntity).getRecipe(level));
    }

}
