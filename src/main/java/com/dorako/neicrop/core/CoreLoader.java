package com.dorako.neicrop.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dorako.neicrop.core.recipes.BiomesOPlentyRecipes;
import com.dorako.neicrop.core.recipes.HarvestcraftRecipes;
import com.dorako.neicrop.core.recipes.NaturaRecipes;
import com.dorako.neicrop.core.recipes.NetherHarvestRecipes;
import com.dorako.neicrop.core.recipes.VanillaRecipes;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CoreLoader {

    private static List<PlantRecipe> allRecipes;

    @SideOnly(Side.CLIENT)
    public static void generateAllRecipes() {
        List<PlantRecipe> allPlants = new ArrayList<>();
        allPlants.addAll(VanillaRecipes.generateRecipes());

        if (Loader.isModLoaded("harvestcraft")) {
            allPlants.addAll(HarvestcraftRecipes.generateRecipes());
        }
        if (Loader.isModLoaded("harvestthenether")) {
            allPlants.addAll(NetherHarvestRecipes.generateRecipes());
        }
        if (Loader.isModLoaded("BiomesOPlenty")) {
            allPlants.addAll(BiomesOPlentyRecipes.generateRecipes());
        }
        if (Loader.isModLoaded("Natura")) {
            allPlants.addAll(NaturaRecipes.generateRecipes());
        }

        Collections.sort(allPlants);

        allRecipes = new ArrayList<>();
        allRecipes.addAll(allPlants);
    }

    public static List<PlantRecipe> getRecipes() {
        return allRecipes;
    }
}
