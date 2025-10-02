package com.dorako.neicrop.mixins;

import net.minecraft.world.gen.feature.WorldGenerator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.pam.harvestcraft.BlockPamSapling;

@Mixin(value = BlockPamSapling.class)
public interface BlockPamSaplingAccessor {

    @Accessor("treeGen")
    WorldGenerator getTreeGen();
}
