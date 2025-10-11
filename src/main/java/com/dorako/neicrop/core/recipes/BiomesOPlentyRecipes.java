package com.dorako.neicrop.core.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.dorako.neicrop.core.FieldItems;
import com.dorako.neicrop.core.PlantRecipe;

import biomesoplenty.api.content.BOPCBlocks;
import biomesoplenty.api.content.BOPCItems;
import biomesoplenty.common.blocks.BlockFlowerVine;
import biomesoplenty.common.blocks.BlockIvy;
import biomesoplenty.common.blocks.BlockMoss;
import biomesoplenty.common.blocks.BlockTreeMoss;
import biomesoplenty.common.blocks.BlockWisteria;

public class BiomesOPlentyRecipes {

    private static final String modId = "BiomesOPlenty";
    private static final PlantRecipe.ModOrigin modOrigin = PlantRecipe.ModOrigin.BIOMESOPLENTY;

    public static List<PlantRecipe> generateRecipes() {
        List<PlantRecipe> output = new ArrayList<>();

        for (Object blockRaw : Block.blockRegistry) {
            // vines
            if (blockRaw instanceof BlockIvy || blockRaw instanceof BlockFlowerVine
                || blockRaw instanceof BlockTreeMoss
                || blockRaw instanceof BlockWisteria) {
                Block seed = (Block) blockRaw;
                List<ItemStack> produce = new ArrayList<>();
                produce.add(new ItemStack((Block) blockRaw));

                output.add(
                    new PlantRecipe(
                        new ItemStack(seed),
                        null,
                        PlantRecipe.EnumPlantProcesses.BASIC,
                        produce,
                        new ArrayList<>(),
                        StatCollector.translateToLocal("neicrop.notes.vineLike"),
                        PlantRecipe.RecipeType.VINE,
                        modOrigin));
            }
            // moss
            if (blockRaw instanceof BlockMoss seed) {
                List<ItemStack> produce = new ArrayList<>();
                produce.add(new ItemStack((Block) blockRaw));

                output.add(
                    new PlantRecipe(
                        new ItemStack(seed),
                        FieldItems.EnumFullPlantType.BOPMoss,
                        PlantRecipe.EnumPlantProcesses.BASIC,
                        produce,
                        new ArrayList<>(),
                        StatCollector.translateToLocal("neicrop.notes.vineLike"),
                        PlantRecipe.RecipeType.VINE,
                        modOrigin));
            }
        }

        // saplings
        int SAPLING_SIZE_A = 15;
        int SAPLING_SIZE_B = 7;
        ItemStack[][] treeProduces = {
            // apple
            { new ItemStack(Blocks.log), new ItemStack(BOPCBlocks.appleLeaves) },
            // yellow autumn
            { new ItemStack(Blocks.log, 1, 2), new ItemStack(BOPCBlocks.leaves1) },
            // bamboo
            { new ItemStack(BOPCBlocks.bamboo), new ItemStack(BOPCBlocks.leaves1, 1, 1) },
            // magic
            { new ItemStack(BOPCBlocks.logs2, 1, 1), new ItemStack(BOPCBlocks.leaves1, 1, 2) },
            // dark
            { new ItemStack(BOPCBlocks.logs2, 1, 2), new ItemStack(BOPCBlocks.leaves1, 1, 3) },
            // dead
            { new ItemStack(Blocks.log), new ItemStack(BOPCBlocks.leaves2) },
            // fir - also has pinecone fruit
            { new ItemStack(BOPCBlocks.logs1, 1, 3), new ItemStack(BOPCBlocks.leaves2, 1, 1),
                new ItemStack(BOPCBlocks.fruitBop, 1, 4) },
            // ethereal - unused (exists, but does not grow)
            {},
            // orange autumn
            { new ItemStack(BOPCBlocks.logs2, 1, 1), new ItemStack(BOPCBlocks.leaves2, 1, 3) },
            // origin
            { new ItemStack(Blocks.log), new ItemStack(BOPCBlocks.leaves3) },
            // pink cherry
            { new ItemStack(BOPCBlocks.logs1, 1, 1), new ItemStack(BOPCBlocks.leaves3, 1, 1) },
            // maple
            { new ItemStack(Blocks.log), new ItemStack(BOPCBlocks.leaves3, 1, 2) },
            // white cherry
            { new ItemStack(BOPCBlocks.logs1, 1, 1), new ItemStack(BOPCBlocks.leaves3, 1, 3) },
            // hellbark
            { new ItemStack(BOPCBlocks.logs4, 1, 1), new ItemStack(BOPCBlocks.leaves4) },
            // jacaranda
            { new ItemStack(BOPCBlocks.logs4, 1, 2), new ItemStack(BOPCBlocks.leaves4, 1, 1) },
            // persimmon
            { new ItemStack(Blocks.log), new ItemStack(BOPCBlocks.persimmonLeaves) },
            // sacred oak
            { new ItemStack(BOPCBlocks.logs1), new ItemStack(BOPCBlocks.colorizedLeaves1) },
            // mangrove
            { new ItemStack(BOPCBlocks.logs2, 1, 2), new ItemStack(BOPCBlocks.colorizedLeaves1, 1, 1) },
            // palm (technically can't grow on dirt, only grass, not sure who cares though)
            { new ItemStack(BOPCBlocks.logs2, 1, 3), new ItemStack(BOPCBlocks.colorizedLeaves1, 1, 2) },
            // redwood
            { new ItemStack(BOPCBlocks.logs3), new ItemStack(BOPCBlocks.colorizedLeaves1, 1, 3) },
            // willow
            { new ItemStack(BOPCBlocks.logs3, 1, 1), new ItemStack(BOPCBlocks.colorizedLeaves2) },
            // pine
            { new ItemStack(BOPCBlocks.logs4), new ItemStack(BOPCBlocks.colorizedLeaves2, 1, 1) },
            // mahogany
            { new ItemStack(BOPCBlocks.logs4, 1, 3), new ItemStack(BOPCBlocks.colorizedLeaves2, 1, 2) },
            // flowering
            { new ItemStack(Blocks.log), new ItemStack(Blocks.leaves),
                new ItemStack(BOPCBlocks.colorizedLeaves2, 1, 3) } };

        FieldItems.EnumFullPlantType defaultType = FieldItems.EnumFullPlantType.Plains;
        Map<Integer, FieldItems.EnumFullPlantType> typeOverride = new HashMap<>();
        typeOverride.put(13, FieldItems.EnumFullPlantType.OvergrownNetherrack);
        typeOverride.put(16, FieldItems.EnumFullPlantType.Dirt);
        typeOverride.put(17, FieldItems.EnumFullPlantType.Desert);
        typeOverride.put(21, FieldItems.EnumFullPlantType.BOPPine);

        // Regular BOP leaves with meta % 4 == 0 gives persimmon 2% of the time
        // Colourized drop nothing
        Map<Integer, ItemStack> secondaryProduceOverride = new HashMap<>();
        secondaryProduceOverride.put(0, new ItemStack(Items.apple));
        secondaryProduceOverride.put(1, new ItemStack(BOPCItems.food, 1, 8));
        secondaryProduceOverride.put(4, new ItemStack(BOPCItems.food, 1, 8));
        secondaryProduceOverride.put(9, new ItemStack(BOPCItems.food, 1, 8));
        secondaryProduceOverride.put(13, new ItemStack(BOPCItems.food, 1, 8));
        secondaryProduceOverride.put(15, new ItemStack(BOPCItems.food, 1, 8));

        Map<Integer, String> noteOverride = new HashMap<>();
        noteOverride.put(
            0,
            StatCollector.translateToLocal("neicrop.notes.leftClickFruit") + "|"
                + StatCollector.translateToLocal("neicrop.notes.bopFruitLeaves"));
        noteOverride.put(
            15,
            StatCollector.translateToLocal("neicrop.notes.leftClickFruit") + "|"
                + StatCollector.translateToLocal("neicrop.notes.bopFruitLeaves"));

        for (int i = 0; i < SAPLING_SIZE_A + SAPLING_SIZE_B + 2; i++) {
            if (i == 7) continue;

            boolean isSaplingA = true;
            int metaIndex = i;
            if (i > SAPLING_SIZE_A) {
                metaIndex -= SAPLING_SIZE_A + 1;
                isSaplingA = false;
            }

            ItemStack seed;

            if (isSaplingA) seed = new ItemStack(BOPCBlocks.saplings, 1, metaIndex);
            else seed = new ItemStack(BOPCBlocks.colorizedSaplings, 1, metaIndex);

            FieldItems.EnumFullPlantType fieldType = defaultType;
            if (typeOverride.containsKey(i)) {
                fieldType = typeOverride.get(i);
            }

            List<ItemStack> produce = new ArrayList<>();
            Collections.addAll(produce, treeProduces[i]);

            List<ItemStack> secondaryProduce = new ArrayList<>();
            // flowering oak drops oak leaf things
            if (i == 23) {
                secondaryProduce.add(new ItemStack(Blocks.sapling));
                secondaryProduce.add(new ItemStack(Items.apple));
            }
            secondaryProduce.add(seed);
            if (secondaryProduceOverride.containsKey(i)) {
                secondaryProduce.add(secondaryProduceOverride.get(i));
            }

            String note = null;
            if (noteOverride.containsKey(i)) note = noteOverride.get(i);

            output.add(
                new PlantRecipe(
                    seed,
                    fieldType,
                    PlantRecipe.EnumPlantProcesses.BASIC,
                    produce,
                    secondaryProduce,
                    note,
                    PlantRecipe.RecipeType.TREE,
                    modOrigin));
        }

        // turnip (should not be obtainable in GTNH unless seeds are cheated in)
        List<ItemStack> turnipOutput = new ArrayList<>();
        turnipOutput.add(new ItemStack(BOPCItems.food, 1, 11));
        output.add(
            new PlantRecipe(
                new ItemStack(BOPCItems.turnipSeeds),
                FieldItems.EnumFullPlantType.Crop,
                PlantRecipe.EnumPlantProcesses.BASIC,
                turnipOutput,
                new ArrayList<>(),
                null,
                PlantRecipe.RecipeType.CROP,
                modOrigin));

        // bamboo
        List<ItemStack> bambooOutput = new ArrayList<>();
        bambooOutput.add(new ItemStack(BOPCBlocks.bamboo));
        output.add(
            new PlantRecipe(
                new ItemStack(BOPCBlocks.bamboo),
                FieldItems.EnumFullPlantType.Plains,
                PlantRecipe.EnumPlantProcesses.BASIC,
                bambooOutput,
                new ArrayList<>(),
                StatCollector.translateToLocal("neicrop.notes.growUpwards"),
                PlantRecipe.RecipeType.GROWS,
                modOrigin));

        // berries
        List<ItemStack> berryOutput = new ArrayList<>();
        berryOutput.add(new ItemStack(BOPCItems.food, 1, 0));
        output.add(
            new PlantRecipe(
                new ItemStack(BOPCBlocks.foliage, 1, 8),
                FieldItems.EnumFullPlantType.Plains,
                PlantRecipe.EnumPlantProcesses.BASIC,
                berryOutput,
                new ArrayList<>(),
                null,
                PlantRecipe.RecipeType.BUSH,
                modOrigin));

        // NOTE: mushroom variants are types of bushes, not mushrooms, so none of them do mushroomey things, so
        // they're not listed here

        // NOTE: rivercane and wild carrots are decoration blocks with zero innate reproduction capabilites

        // NOTE: koru drops turnip seeds, but it's non-renewable so it's beyond the scope of this mod (also gtnh changes
        // the seed to harvestcraft seeds anyway)

        return output;
    }
}
