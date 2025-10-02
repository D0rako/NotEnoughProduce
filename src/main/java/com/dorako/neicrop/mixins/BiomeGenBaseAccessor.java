package com.dorako.neicrop.mixins;

import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BiomeGenBase.class)
public interface BiomeGenBaseAccessor {

    @Accessor("flowers")
    List<BiomeGenBase.FlowerEntry> getFlowers();
}
