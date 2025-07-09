package com.edwardthe1rst.block;

import com.edwardthe1rst.BetterSprings;
import com.edwardthe1rst.fluid.SpringFluid;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.state.StateManager;

public class SpringBlock extends FluidBlock {
    public SpringBlock() {
        super(BetterSprings.SPRING_WATER_STILL, FabricBlockSettings.copyOf(Blocks.WATER));
        this.setDefaultState(this.stateManager.getDefaultState().with(SpringFluid.AGE, 20));
    }
    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SpringFluid.AGE);
    }
}
