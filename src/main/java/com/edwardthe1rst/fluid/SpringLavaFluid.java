package com.edwardthe1rst.fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.edwardthe1rst.BetterSprings;
import com.edwardthe1rst.SpringTerraForming;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class SpringLavaFluid extends FlowableFluid {
    public static final IntProperty AGE = Properties.AGE_25;

    public SpringLavaFluid() {
        super();
    }

    protected void onAgeEnd(World world, BlockPos pos, FluidState state) {
        if (state.isStill()) {
            SpringTerraForming.placeLavaHead(world, pos, state);
        } else {
            SpringTerraForming.placeLavaPath(world, pos, state);
        }
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
        super.appendProperties(builder);
        builder.add(AGE);
    }

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == getStill() || fluid == getFlowing();
	}
 
	@Override
	protected boolean isInfinite(World world) {
		return false;
	}
 
	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
	}
 
	@Override
	protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
		return false;
	}
 
	@Override
	protected int getFlowSpeed(WorldView worldView) {
		return 1;
	}
 
	@Override
	protected int getLevelDecreasePerBlock(WorldView world) {
      return world.getDimension().ultrawarm() ? 1 : 2;
	}
 
	@Override
	public int getTickRate(WorldView worldView) {
		return 1;
	}
 
	@Override
	protected float getBlastResistance() {
		return 100.0F;
	}

    @Override
    public Fluid getStill() {
        return BetterSprings.SPRING_LAVA_STILL;
    }
    
    @Override
    public Fluid getFlowing() {
        return BetterSprings.SPRING_LAVA_FLOWING;
    }
    
    @Override
    public Item getBucketItem() {
        return  Items.LAVA_BUCKET;
    }

    @Override
    public void onScheduledTick(World world, BlockPos pos, FluidState state) {
        int age;
        try {
            age = world.getBlockState(pos).get(AGE);
        } catch (Exception e) {
            age = 0;
        }
        super.onScheduledTick(world, pos, state);
        if (world.getBlockState(pos).getBlock() == BetterSprings.SPRING_LAVA_BLOCK) {
            if (age == 0) {
                onAgeEnd(world, pos, state);
            } else if (age > 0) {
                if (world.getBlockState(pos).getBlock() == BetterSprings.SPRING_LAVA_BLOCK) {
                    world.setBlockState(pos, world.getBlockState(pos).with(AGE, age - 1), Block.NOTIFY_ALL);
                }
                world.scheduleFluidTick(pos, state.getFluid(), 1);
            }
            if (age == 1 && world.getBlockState(pos.down()).getBlock() == BetterSprings.SPRING_LAVA_BLOCK) {
                SpringTerraForming.placeLavaPath(world, pos, world.getFluidState(pos));
            }
        }      
    }

    
    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return BetterSprings.SPRING_LAVA_BLOCK.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    @Override
    protected void flow(WorldAccess world, BlockPos pos, BlockState state, Direction direction, FluidState fluidState) {
        super.flow(world, pos, state, direction, fluidState);
        if (world.getBlockState(pos).getBlock() == BetterSprings.SPRING_LAVA_BLOCK) {
            world.setBlockState(pos, world.getBlockState(pos).with(AGE, 20), Block.NOTIFY_ALL);
        } else if(world.getBlockState(pos).isAir()) {
            onAgeEnd((World) world, pos, fluidState);
        }
    }

    @Override
    protected Map<Direction, FluidState> getSpread(World world, BlockPos pos, BlockState state) {

        if (world.getBlockState(pos).get(AGE) < 15) {
            return Map.of();
        }

        FluidState selfFluid = world.getFluidState(pos);

        // Don't spread if touching 2 or more springs, or one with lower level
        int touchingSprings = 0;
        for (Direction dir : Direction.values()) {
            FluidState neighbour = world.getFluidState(pos.offset(dir));
            if (neighbour.getBlockState().getBlock() == BetterSprings.SPRING_LAVA_BLOCK) {
                touchingSprings++;
                if (neighbour.getLevel() <= selfFluid.getLevel() && dir != Direction.UP) {
                    return Map.of();
                }
            }
        }
        if (touchingSprings >= 2) { 
            return Map.of();
        }

        Map<Direction, FluidState> all = super.getSpread(world, pos, state);
        if (all.isEmpty()) return all;

        List<Direction> validDirs = new ArrayList<>();

        for (Map.Entry<Direction, FluidState> entry : all.entrySet()) {
            Direction dir = entry.getKey();
            BlockPos target = pos.offset(dir);

            // Check target block won't have too many springs
            int springCount = 0;
            for (Direction d : Direction.values()) {
                if (world.getFluidState(target.offset(d)).getBlockState().getBlock() == BetterSprings.SPRING_LAVA_BLOCK) {
                    springCount++;
                    if (springCount >= 2) break;
                }
            }
            if (springCount >= 2) continue;

            // Check flow is allowed
            if (canFlow(world, pos, state, dir, target, world.getBlockState(target), world.getFluidState(target), entry.getValue().getFluid())) {
                validDirs.add(dir);
            }
        }

        if (validDirs.isEmpty()) {
            return Map.of();
        }

        // Pick a random direction from validDirs based on block position for deterministic randomness
        int hash = pos.getX() * 31 + pos.getY() * 17 + pos.getZ() * 13;
        int idx = Math.floorMod(hash, validDirs.size());
        Direction chosen = validDirs.get(idx);
        return Map.of(chosen, all.get(chosen));
    }

    public Map<Direction, FluidState> _getSpread(World world, BlockPos pos, BlockState state) {
        return super.getSpread(world, pos, state);
    }
    
    public static class Flowing extends SpringLavaFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }
    
        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }
    
        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }
    
    public static class Still extends SpringLavaFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }
    
        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}