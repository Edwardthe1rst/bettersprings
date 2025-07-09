package com.edwardthe1rst;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edwardthe1rst.block.SpringBlock;
import com.edwardthe1rst.block.SpringLavaBlock;
import com.edwardthe1rst.fluid.SpringFluid;
import com.edwardthe1rst.fluid.SpringLavaFluid;

public class BetterSprings implements ModInitializer {
	public static final String MOD_ID = "bettersprings";
    public static final FlowableFluid SPRING_WATER_STILL = new SpringFluid.Still();
    public static final FlowableFluid SPRING_WATER_FLOWING = new SpringFluid.Flowing();
	public static final FluidBlock SPRING_WATER_BLOCK = new SpringBlock();
	public static final FlowableFluid SPRING_LAVA_STILL = new SpringLavaFluid.Still();
    public static final FlowableFluid SPRING_LAVA_FLOWING = new SpringLavaFluid.Flowing();
	public static final FluidBlock SPRING_LAVA_BLOCK = new SpringLavaBlock();

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Registry.register(Registries.FLUID, new Identifier(MOD_ID, "spring_water"), SPRING_WATER_STILL);
        Registry.register(Registries.FLUID, new Identifier(MOD_ID, "spring_water_flowing"), SPRING_WATER_FLOWING);
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "spring_water"), SPRING_WATER_BLOCK);
		Registry.register(Registries.FLUID, new Identifier(MOD_ID, "spring_lava"), SPRING_LAVA_STILL);
        Registry.register(Registries.FLUID, new Identifier(MOD_ID, "spring_lava_flowing"), SPRING_LAVA_FLOWING);
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "spring_lava"), SPRING_LAVA_BLOCK);

		LOGGER.info("Hello Fabric world!");
	}
}