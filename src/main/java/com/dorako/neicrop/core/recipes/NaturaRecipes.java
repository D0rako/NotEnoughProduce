package com.dorako.neicrop.core.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.dorako.neicrop.core.FieldItems;
import com.dorako.neicrop.core.PlantRecipe;

import mods.natura.common.NContent;
import mods.natura.common.PHNatura;

public class NaturaRecipes {

    private static final String modId = "Natura";
    private static final PlantRecipe.ModOrigin modOrigin = PlantRecipe.ModOrigin.NATURA;

    public static List<PlantRecipe> generateRecipes() {
        List<PlantRecipe> output = new ArrayList<>();

        // saplings
        int SAPLING_SIZE_A = 4;
        int SAPLING_SIZE_B = 7;
        ItemStack[][] treeProduces = {
            // maple
            { new ItemStack(NContent.rareTree), new ItemStack(NContent.rareLeaves) },
            // silverbell
            { new ItemStack(NContent.rareTree, 1, 1), new ItemStack(NContent.rareLeaves, 1, 1) },
            // purpleheart
            { new ItemStack(NContent.rareTree, 1, 2), new ItemStack(NContent.rareLeaves, 1, 2) },
            // tiger
            { new ItemStack(NContent.rareTree, 1, 3), new ItemStack(NContent.rareLeaves, 1, 3) },
            // willow
            { new ItemStack(NContent.willow), new ItemStack(NContent.floraLeavesNoColor, 1, 3) },
            // redwood
            { new ItemStack(NContent.redwood), new ItemStack(NContent.floraLeaves) },
            // eucalyptus
            { new ItemStack(NContent.tree), new ItemStack(NContent.floraLeaves, 1, 1) },
            // hopseed
            { new ItemStack(NContent.tree, 1, 3), new ItemStack(NContent.floraLeaves, 1, 2) },
            // sakura
            { new ItemStack(NContent.tree, 1, 1), new ItemStack(NContent.floraLeavesNoColor) },
            // ghostwood
            { new ItemStack(NContent.tree, 1, 2), new ItemStack(NContent.floraLeavesNoColor, 1, 1) },
            // bloodwood (leaves drop redstone)
            { new ItemStack(NContent.bloodwood), new ItemStack(NContent.floraLeavesNoColor, 1, 2) },
            // darkwood (leaves subtype 2 drops potash apples)
            { new ItemStack(NContent.darkTree), new ItemStack(NContent.darkLeaves) },
            // fusewood
            { new ItemStack(NContent.darkTree, 1, 1), new ItemStack(NContent.darkLeaves, 1, 3) } };

        FieldItems.EnumFullPlantType defaultType = FieldItems.EnumFullPlantType.Plains;
        Map<Integer, FieldItems.EnumFullPlantType> typeOverride = new HashMap<>();
        typeOverride.put(9, FieldItems.EnumFullPlantType.Nether);
        typeOverride.put(10, FieldItems.EnumFullPlantType.NBlood);
        typeOverride.put(11, FieldItems.EnumFullPlantType.Nether);
        typeOverride.put(12, FieldItems.EnumFullPlantType.Nether);

        for (int i = 0; i < SAPLING_SIZE_A + SAPLING_SIZE_B + 2; i++) {
            ItemStack seed;
            if (i <= SAPLING_SIZE_A) {
                seed = new ItemStack(NContent.rareSapling, 1, i);
            } else {
                seed = new ItemStack(NContent.floraSapling, 1, i - SAPLING_SIZE_A - 1);
            }

            FieldItems.EnumFullPlantType type = defaultType;
            if (typeOverride.containsKey(i)) {
                type = typeOverride.get(i);
            }

            List<ItemStack> produce = Arrays.asList(treeProduces[i]);

            List<ItemStack> secondaryProduce = new ArrayList<>();
            secondaryProduce.add(seed);
            if (i == 10) secondaryProduce.add(new ItemStack(Items.redstone));
            if (i == 11) secondaryProduce.add(new ItemStack(NContent.potashApple));

            String note = null;
            if (i == 11) note = StatCollector.translateToLocal("neicrop.notes.nFruitLeaves");

            output.add(
                new PlantRecipe(
                    seed,
                    type,
                    PlantRecipe.EnumPlantProcesses.BASIC,
                    produce,
                    secondaryProduce,
                    note,
                    PlantRecipe.RecipeType.TREE,
                    modOrigin));
        }

        // barley and cotton
        int seedCount = 2;
        for (int i = 0; i < seedCount; i++) {
            ItemStack seed = new ItemStack(NContent.seeds, 1, i);

            List<ItemStack> produce = new ArrayList<>();
            produce.add(new ItemStack(NContent.plantItem, 1, i * 3));

            output.add(
                new PlantRecipe(
                    seed,
                    FieldItems.EnumFullPlantType.Crop,
                    PlantRecipe.EnumPlantProcesses.BASIC,
                    produce,
                    new ArrayList<>(),
                    null,
                    PlantRecipe.RecipeType.CROP,
                    modOrigin));
        }

        // berry bushes
        if (PHNatura.enableBerryBushes) {
            int berryAmount = 4;
            for (int i = 0; i < berryAmount; i++) {
                ItemStack seed = new ItemStack(NContent.berryBush, 1, i);

                List<ItemStack> produce = new ArrayList<>();
                produce.add(seed);

                List<ItemStack> secondaryProduce = new ArrayList<>();
                secondaryProduce.add(new ItemStack(NContent.berryItem, 1, i));

                output.add(
                    new PlantRecipe(
                        seed,
                        FieldItems.EnumFullPlantType.Plains,
                        PlantRecipe.EnumPlantProcesses.BASIC,
                        produce,
                        secondaryProduce,
                        StatCollector.translateToLocal("neicrop.notes.growUpwards"),
                        PlantRecipe.RecipeType.GROWS,
                        modOrigin));
            }
        }
        if (PHNatura.enableNetherBerryBushes) {
            int netherBerryAmount = 4;
            for (int i = 0; i < netherBerryAmount; i++) {
                ItemStack seed = new ItemStack(NContent.netherBerryBush, 1, i);

                List<ItemStack> produce = new ArrayList<>();
                produce.add(seed);

                List<ItemStack> secondaryProduce = new ArrayList<>();
                secondaryProduce.add(new ItemStack(NContent.netherBerryItem, 1, i));

                output.add(
                    new PlantRecipe(
                        seed,
                        FieldItems.EnumFullPlantType.Nether,
                        PlantRecipe.EnumPlantProcesses.BASIC,
                        produce,
                        secondaryProduce,
                        StatCollector.translateToLocal("neicrop.notes.growUpwards"),
                        PlantRecipe.RecipeType.GROWS,
                        modOrigin));
            }
        }

        // glowshrooms
        int glowshroomAmount = 3;
        Block[] indexToGlowshroomBlock = { NContent.glowshroomGreen, NContent.glowshroomPurple,
            NContent.glowshroomBlue };
        for (int i = 0; i < glowshroomAmount; i++) {
            ItemStack seed = new ItemStack(NContent.glowshroom, 1, i);

            List<ItemStack> itself = new ArrayList<>();
            itself.add(seed);

            List<ItemStack> block = new ArrayList<>();
            block.add(new ItemStack(indexToGlowshroomBlock[i]));

            // duplication
            output.add(
                new PlantRecipe(
                    seed,
                    FieldItems.EnumFullPlantType.Mushroom,
                    PlantRecipe.EnumPlantProcesses.BASIC,
                    itself,
                    new ArrayList<>(),
                    StatCollector.translateToLocal("neicrop.notes.duplicating"),
                    PlantRecipe.RecipeType.DUPLICATING,
                    modOrigin));

            // tree
            output.add(
                new PlantRecipe(
                    seed,
                    FieldItems.EnumFullPlantType.Mushroom,
                    PlantRecipe.EnumPlantProcesses.BONEMEAL,
                    block,
                    itself,
                    null,
                    PlantRecipe.RecipeType.TREE,
                    modOrigin));
        }

        // saguaro cactus
        ItemStack seed = new ItemStack(NContent.seedFood);

        List<ItemStack> cactusProduce = new ArrayList<>();
        cactusProduce.add(new ItemStack(NContent.saguaro));

        List<ItemStack> cactusSecondaryProduce = new ArrayList<>();
        cactusSecondaryProduce.add(new ItemStack(NContent.seedFood));

        output.add(
            new PlantRecipe(
                seed,
                FieldItems.EnumFullPlantType.Desert,
                PlantRecipe.EnumPlantProcesses.BASIC,
                cactusProduce,
                cactusSecondaryProduce,
                StatCollector.translateToLocal("neicrop.notes.fruitRainOnly") + "|"
                    + StatCollector.translateToLocal("neicrop.notes.leftClickFruit"),
                PlantRecipe.RecipeType.TREE,
                modOrigin));

        // thornvines
        ItemStack thornvine = new ItemStack(NContent.thornVines);

        List<ItemStack> thornVineProduce = new ArrayList<>();
        thornVineProduce.add(thornvine);

        output.add(
            new PlantRecipe(
                thornvine,
                null,
                PlantRecipe.EnumPlantProcesses.BASIC,
                thornVineProduce,
                new ArrayList<>(),
                StatCollector.translateToLocal("neicrop.notes.vineLike"),
                PlantRecipe.RecipeType.VINE,
                modOrigin));

        return output;
    }
}
