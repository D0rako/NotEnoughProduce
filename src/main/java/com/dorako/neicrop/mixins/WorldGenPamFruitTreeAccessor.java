package com.dorako.neicrop.mixins;

import net.minecraft.block.Block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.pam.harvestcraft.WorldGenPamFruitTree;

@Mixin(value = WorldGenPamFruitTree.class)
public interface WorldGenPamFruitTreeAccessor {

    @Accessor("metaWood")
    int getMetaWood();

    @Accessor("metaLeaves")
    int getMetaLeaves();

    @Accessor("fruitType")
    Block getFruitType();

}
