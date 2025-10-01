package com.dorako.neicrop.core;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CoreLoader {

    private static List<PlantRecipe> allRecipes;

    @SideOnly(Side.CLIENT)
    public static void generateAllRecipes() {
        List<PlantRecipe> allPlants = new ArrayList<>();
        allPlants.addAll(VanillaRecipes.generateRecipes());

        // add mod checks + generators here

        if (allRecipes == null) {
            allRecipes = new ArrayList<>();
        }
        allRecipes.addAll(allPlants);
    }

    public static List<PlantRecipe> getRecipes() {
        return allRecipes;
    }
}
