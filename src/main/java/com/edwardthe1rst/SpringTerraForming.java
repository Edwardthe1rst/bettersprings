package com.edwardthe1rst;
import com.edwardthe1rst.fluid.SpringFluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class SpringTerraForming {
    public static void placeHead(WorldAccess world, BlockPos pos, FluidState fluid) {
        for (Direction direction : Direction.values()) {
            if (world.getBlockState(pos.offset(direction)).isAir()) {
                for (int dy = -4; dy <= 4; dy++) {
                    BlockPos targetPos = pos.offset(direction).add(0, dy, 0);
                    if (!exposed(world, targetPos)) {
                        _replaceBlock(world, targetPos, Blocks.AIR.getDefaultState());
                    }
                }
                break;
            }
        }
        for (int dx = -6; dx <= 6; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -6; dz <= 6; dz++) {
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    BlockPos targetPos = pos.add(dx, dy, dz);
                    if (dx == 0 && dy < 1 && dz == 0 && !exposed(world, targetPos)) {
                        _replaceBlock(world, targetPos, Blocks.AIR.getDefaultState());
                    } else if (distance <= 2.5 && !exposed(world, targetPos)) {
                        _replaceBlock(world, targetPos, Blocks.WATER.getDefaultState());
                    } else if (distance <= 3.5) {
                        _replaceBlock(world, targetPos, Blocks.TUFF.getDefaultState());
                    } else if (distance <= 4.5) {
                        _replaceBlock(world, targetPos, Blocks.ANDESITE.getDefaultState());
                    }
                }
            }
        }
    }
    public static void placePath(WorldAccess world, BlockPos pos, FluidState fluid) {
        Integer tail = 0;
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    BlockPos targetPos = pos.add(dx, dy, dz);
                    if (world.getBlockState(pos.down()).getBlock() != BetterSprings.SPRING_WATER_BLOCK) {
                        if (distance <= 1.5) {
                            carve(world, targetPos);
                        } else if (distance <= 2.5 && Math.abs(dx) <= 1 && Math.abs(dz) <= 1 && dy < -1) {
                            if (world.getBlockState(targetPos).isIn(BlockTags.DIRT)) {
                                _replaceBlock(world, targetPos, Blocks.MUD.getDefaultState());
                            } else {
                                _replaceBlock(world, targetPos, Blocks.DEEPSLATE.getDefaultState());
                            }
                        } else if (distance < 3 && exposed(world, targetPos)) {
                            _replaceBlock(world, targetPos, Blocks.MOSS_BLOCK.getDefaultState());
                        } else if (distance < 4 && world.getRandom().nextBetween(0, 4) == 0) {
                            _replaceBlock(world, targetPos, Blocks.MOSS_BLOCK.getDefaultState());
                        }
                    }
                    else if (distance == 1 && world.getBlockState(pos.down()).getBlock() == BetterSprings.SPRING_WATER_BLOCK) {
                        carve(world, targetPos);
                    }
                    if (world.getBlockState(targetPos).getBlock() == BetterSprings.SPRING_WATER_BLOCK && pos != targetPos) {
                        ++tail;
                    }
                }
            }
        }
        if (tail < 5) {
            placeTail(world, pos, fluid);
        }
    }

    public static void placeTail(WorldAccess world, BlockPos pos, FluidState fluid) {
        Boolean spring = false;
        for (int dx = -6; dx <= 6; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -6; dz <= 6; dz++) {
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    BlockPos targetPos = pos.add(dx, dy, dz);
                    if (distance <= 2.5) {
                        if (dy <= -1) {
                            Direction dir = sideExposed(world, targetPos);
                            if (dir != Direction.UP && sideExposed(world, targetPos.offset(dir)) != Direction.UP) {
                                if (!spring) {
                                    _replaceBlock(world, targetPos, Blocks.TUFF.getDefaultState());
                                    _replaceBlock(world, targetPos.offset(dir), Blocks.ACACIA_LOG.getDefaultState());// BetterSprings.SPRING_WATER_BLOCK.getDefaultState().with(SpringFluid.AGE, 20));
                                    spring = true;
                                } else {
                                    _replaceBlock(world, targetPos, Blocks.TUFF.getDefaultState());
                                }
                            } else {
                                _replaceBlock(world, targetPos, Blocks.WATER.getDefaultState());
                            }
                        } else {
                            _replaceBlock(world, targetPos, Blocks.AIR.getDefaultState());
                        }
                    } else if (distance <= 3.5) {
                        _replaceBlock(world, targetPos, Blocks.TUFF.getDefaultState());
                    } else if (distance <= 4.5) {
                        _replaceBlock(world, targetPos, Blocks.ANDESITE.getDefaultState());
                    }
                }
            }
        }
    }

    public static void placeLavaHead(WorldAccess world, BlockPos pos, FluidState fluid) {
        for (Direction direction : Direction.values()) {
            if (world.getBlockState(pos.offset(direction)).isAir()) {
                for (int dy = -4; dy <= 4; dy++) {
                    BlockPos targetPos = pos.offset(direction).add(0, dy, 0);
                    if (!exposed(world, targetPos)) {
                        _replaceBlock(world, targetPos, Blocks.AIR.getDefaultState());
                    }
                }
                break;
            }
        }
        for (int dx = -6; dx <= 6; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -6; dz <= 6; dz++) {
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    BlockPos targetPos = pos.add(dx, dy, dz);
                    if (dx == 0 && dy < 1 && dz == 0 && !exposed(world, targetPos)) {
                        _replaceBlock(world, targetPos, Blocks.AIR.getDefaultState());
                    } else if (distance <= 2.5 && !exposed(world, targetPos)) {
                        _replaceBlock(world, targetPos, Blocks.LAVA.getDefaultState());
                    } else if (distance <= 3.5) {
                        _replaceBlock(world, targetPos, Blocks.MAGMA_BLOCK.getDefaultState());
                    } else if (distance <= 4.5) {
                        _replaceBlock(world, targetPos, Blocks.BLACKSTONE.getDefaultState());
                    }
                }
            }
        }
    }
    public static void placeLavaPath(WorldAccess world, BlockPos pos, FluidState fluid) {
        Integer tail = 0;
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    BlockPos targetPos = pos.add(dx, dy, dz);
                    if (world.getBlockState(pos.down()).getBlock() != BetterSprings.SPRING_LAVA_BLOCK) {
                        if (distance <= 1.5) {
                            carve(world, targetPos);
                        } else if (distance <= 2.5 && Math.abs(dx) <= 1 && Math.abs(dz) <= 1 && dy < -1) {
                            _replaceBlock(world, targetPos, Blocks.MAGMA_BLOCK.getDefaultState());
                        } else if (distance < 3 && exposed(world, targetPos)) {
                            _replaceBlock(world, targetPos, Blocks.BLACKSTONE.getDefaultState());
                        } else if (distance < 4 && world.getRandom().nextBetween(0, 4) == 0) {
                            _replaceBlock(world, targetPos, Blocks.BLACKSTONE.getDefaultState());
                        }
                    }
                    else if (distance == 1 && world.getBlockState(pos.down()).getBlock() == BetterSprings.SPRING_LAVA_BLOCK) {
                        carve(world, targetPos);
                    }
                    if (world.getBlockState(targetPos).getBlock() == BetterSprings.SPRING_LAVA_BLOCK && pos != targetPos) {
                        ++tail;
                    }
                }
            }
        }
        if (tail < 5) {
            placeLavaTail(world, pos, fluid);
        }
    }

    public static void placeLavaTail(WorldAccess world, BlockPos pos, FluidState fluid) {
        Boolean spring = false;
        for (int dx = -6; dx <= 6; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -6; dz <= 6; dz++) {
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    BlockPos targetPos = pos.add(dx, dy, dz);
                    if (distance <= 2.5) {
                        if (dy <= -1) {
                            Direction dir = sideExposed(world, targetPos);
                            if (dir != Direction.UP && sideExposed(world, targetPos.offset(dir)) != Direction.UP) {
                                if (!spring) {
                                    _replaceBlock(world, targetPos, Blocks.TUFF.getDefaultState());
                                    _replaceBlock(world, targetPos.offset(dir), Blocks.ACACIA_LOG.getDefaultState());// BetterSprings.SPRING_WATER_BLOCK.getDefaultState().with(SpringFluid.AGE, 20));
                                    spring = true;
                                } else {
                                    _replaceBlock(world, targetPos, Blocks.MAGMA_BLOCK.getDefaultState());
                                }
                            } else {
                                _replaceBlock(world, targetPos, Blocks.LAVA.getDefaultState());
                            }
                        } else {
                            _replaceBlock(world, targetPos, Blocks.AIR.getDefaultState());
                        }
                    } else if (distance <= 3.5) {
                        _replaceBlock(world, targetPos, Blocks.MAGMA_BLOCK.getDefaultState());
                    } else if (distance <= 4.5) {
                        _replaceBlock(world, targetPos, Blocks.BLACKSTONE.getDefaultState());
                    }
                }
            }
        }
    }


    public static Boolean _replaceBlock (WorldAccess world, BlockPos pos, BlockState state) {
        if (world.getBlockState(pos).isIn(BlockTags.AZALEA_ROOT_REPLACEABLE)) {
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
            return true;
        } else {
            return false;
        }
    }

    public static Boolean carve (WorldAccess world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if ((state.isIn(BlockTags.AZALEA_ROOT_REPLACEABLE) || state.isIn(BlockTags.OVERWORLD_CARVER_REPLACEABLES) || state.isIn(BlockTags.NETHER_CARVER_REPLACEABLES)) && 
            !waterExposed(world, pos)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
            return true;
        } else {
            return false;
        }
    }

    public static Boolean exposed(WorldAccess world, BlockPos pos) {
        for (Direction direction: Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            if (world.getBlockState(neighborPos).isIn(BlockTags.REPLACEABLE) && world.getFluidState(neighborPos).getFluid() == Fluids.EMPTY) {
                return true;
            }
        }
        return false;
    }

    public static Direction sideExposed(WorldAccess world, BlockPos pos) {
        for (Direction direction: Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            if (world.getBlockState(neighborPos).isIn(BlockTags.REPLACEABLE) &&
                world.getBlockState(neighborPos).getBlock() != Blocks.WATER &&
                direction != Direction.DOWN &&
                direction != Direction.UP) {
                return direction;
            }
        }
        return Direction.UP;
    }

    public static Boolean waterExposed(WorldAccess world, BlockPos pos) {
        for (Direction direction: Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            Fluid fluid = world.getBlockState(neighborPos).getFluidState().getFluid();
            if (fluid == Fluids.LAVA || fluid == Fluids.WATER) {
                return true;
            }
        }
        return false;
    }

}
