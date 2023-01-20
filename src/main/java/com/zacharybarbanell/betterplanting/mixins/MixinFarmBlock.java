package com.zacharybarbanell.betterplanting.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(FarmBlock.class)
public abstract class MixinFarmBlock extends Block {

    public MixinFarmBlock() {super(BlockBehaviour.Properties.of(Material.STONE));} //stripped during compilation

    @Inject(method = "fallOn", at = @At("HEAD"))
    protected void onFallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci){
        if(fallDistance > 0.864 && !level.isClientSide() && entity instanceof ItemEntity){
            ItemStack itemstack = ((ItemEntity) entity).getItem();
            if(itemstack.getItem() instanceof BlockItem && ((BlockItem) itemstack.getItem()).getBlock() instanceof CropBlock){
                BlockPlaceContext fakecontext = new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, itemstack, new BlockHitResult(null, Direction.UP, pos, false));
                ((BlockItem) itemstack.getItem()).place(fakecontext);
            }
        }
    }
    
}
