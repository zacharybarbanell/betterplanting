package com.zacharybarbanell.betterplanting.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.zacharybarbanell.betterplanting.config.Config;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(Entity.class)
public class MixinEntity {

    @Shadow
    public Level level;

    @Shadow
    public float fallDistance;

    private static TagKey<Item> PLACABLE = ItemTags.create(new ResourceLocation("betterplanting", "plantable"));

    private static boolean isValid(BlockItem blockitem) {
        return blockitem.builtInRegistryHolder().is(PLACABLE) || blockitem.getBlock() instanceof CropBlock && Config.SERVER.autoSelectCrops();
    }

    @Inject(
        method = "checkFallDamage", 
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V"),
        require = 1    
    )
    protected void afterFallOn(double vy, boolean onGround, BlockState state, BlockPos pos, CallbackInfo ci) {
        if(!level.isClientSide() && this.fallDistance > Config.SERVER.minHeight() && (Object) this instanceof ItemEntity itementity){
            ItemStack itemstack = itementity.getItem();
            if(itemstack.getItem() instanceof BlockItem blockitem && isValid(blockitem)){
                BlockPlaceContext fakecontext = new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, itemstack, new BlockHitResult(null, Direction.UP, pos, false));
                blockitem.place(fakecontext);
            }
        }   
    }
}
