package com.edwardthe1rst.block;

import com.edwardthe1rst.BetterSprings;
import com.edwardthe1rst.fluid.SpringLavaFluid;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.state.StateManager;

public class SpringLavaBlock extends FluidBlock {
    public SpringLavaBlock() {
        super(BetterSprings.SPRING_LAVA_STILL, FabricBlockSettings.copyOf(Blocks.LAVA));
        this.setDefaultState(this.stateManager.getDefaultState().with(SpringLavaFluid.AGE, 20));
    }
    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SpringLavaFluid.AGE);
    }
}
