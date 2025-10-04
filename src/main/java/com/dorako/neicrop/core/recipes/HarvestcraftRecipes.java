package com.dorako.neicrop.core.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import com.dorako.neicrop.Config;
import com.dorako.neicrop.NEICrop;
import com.dorako.neicrop.core.FieldItems;
import com.dorako.neicrop.core.PlantRecipe;
import com.dorako.neicrop.mixins.BlockPamSaplingAccessor;
import com.dorako.neicrop.mixins.WorldGenPamFruitLogTreeAccessor;
import com.dorako.neicrop.mixins.WorldGenPamFruitTreeAccessor;
import com.pam.harvestcraft.BlockPamCrop;
import com.pam.harvestcraft.BlockPamFruit;
import com.pam.harvestcraft.BlockPamFruitingLog;
import com.pam.harvestcraft.BlockPamSapling;
import com.pam.harvestcraft.BlockRegistry;
import com.pam.harvestcraft.ItemRegistry;
import com.pam.harvestcraft.WorldGenPamFruitLogTree;
import com.pam.harvestcraft.WorldGenPamFruitTree;
import com.pam.harvestcraft.base.BlockGarden;

public class HarvestcraftRecipes {

    private static final String modId = "harvestcraft";
    private static final PlantRecipe.ModOrigin modOrigin = PlantRecipe.ModOrigin.HARVESTCRAFT;

    private static final boolean INCLUDE_GARDENS = Config.showHarvestcraftGardens;

    public static List<PlantRecipe> generateRecipes() {
        List<PlantRecipe> output = new ArrayList<>();

        for (Object blockRaw : Block.blockRegistry) {
            // gardens
            if (INCLUDE_GARDENS && blockRaw instanceof BlockGarden garden) {
                ItemStack seed = new ItemStack(garden);

                List<ItemStack> produce = new ArrayList<>();
                produce.add(seed);

                List<Item> secondaryProduceItems = BlockGarden.getDropList(garden);
                List<ItemStack> secondaryProduce = new ArrayList<>();
                for (Item item : secondaryProduceItems) secondaryProduce.add(new ItemStack(item));

                output.add(
                    new PlantRecipe(
                        seed,
                        FieldItems.EnumFullPlantType.Plains,
                        PlantRecipe.EnumPlantProcesses.BASIC,
                        produce,
                        secondaryProduce,
                        StatCollector.translateToLocal("neicrop.notes.duplicating"),
                        PlantRecipe.RecipeType.DUPLICATING,
                        modOrigin));
            }
            // fruit trees
            if (blockRaw instanceof BlockPamSapling) {
                ItemStack seed = new ItemStack((Block) blockRaw);
                BlockPamSaplingAccessor saplingBlock = (BlockPamSaplingAccessor) blockRaw;

                int woodType = -1;
                int leafType = -1;
                Block fruitBlock = null;
                if (saplingBlock.getTreeGen() instanceof WorldGenPamFruitTree) {
                    WorldGenPamFruitTreeAccessor treeGen = (WorldGenPamFruitTreeAccessor) saplingBlock.getTreeGen();
                    woodType = treeGen.getMetaWood();
                    leafType = treeGen.getMetaLeaves();
                    fruitBlock = treeGen.getFruitType();
                } else if (saplingBlock.getTreeGen() instanceof WorldGenPamFruitLogTree) {
                    WorldGenPamFruitLogTreeAccessor treeGen = (WorldGenPamFruitLogTreeAccessor) saplingBlock
                        .getTreeGen();
                    woodType = treeGen.getMetaWood();
                    leafType = treeGen.getMetaLeaves();
                    fruitBlock = treeGen.getFruitType();
                } else {
                    NEICrop.LOG.error(StatCollector.translateToLocal("neicrop.errors.hc"));
                }

                List<ItemStack> produce = new ArrayList<>();
                List<ItemStack> secondaryProduce = new ArrayList<>();

                String note = "";

                // get raw fruit from right-clicking 'fruit' block
                if (fruitBlock instanceof BlockPamFruit rawFruitBlock) {
                    produce.add(new ItemStack(Blocks.log, 1, woodType));
                    produce.add(new ItemStack(Blocks.leaves, 1, leafType));

                    Item result = rawFruitBlock.getItemDropped(0, null, 0);
                    secondaryProduce.add(new ItemStack(result));

                    note = StatCollector.translateToLocal("neicrop.notes.hasFruit");
                }
                // add log AND raw fruit from etc etc
                else if (fruitBlock instanceof BlockPamFruitingLog fruitLogBlock) {
                    produce.add(new ItemStack(fruitLogBlock));
                    produce.add(new ItemStack(Blocks.leaves, 1, leafType));

                    ItemStack result = fruitLogBlock.getDropItem();
                    secondaryProduce.add(result);

                    note = StatCollector.translateToLocal("neicrop.notes.hasLogFruit");
                } else {
                    NEICrop.LOG.error(StatCollector.translateToLocal("neicrop.errors.hc"));
                }

                output.add(
                    new PlantRecipe(
                        seed,
                        FieldItems.EnumFullPlantType.Plains,
                        PlantRecipe.EnumPlantProcesses.BASIC,
                        produce,
                        secondaryProduce,
                        note,
                        PlantRecipe.RecipeType.TREE,
                        modOrigin));
            }
            // crops
            if (blockRaw instanceof BlockPamCrop cropBlock) {
                // crop blocks internally contain a switch map and this is the best way to get it, yaaay
                boolean currentSeedSetting = BlockRegistry.cropsdropSeeds;
                BlockRegistry.cropsdropSeeds = true;
                Item rawSeed = cropBlock.getItemDropped(0, null, 0);
                Item rawProduce = cropBlock.getItemDropped(7, null, 0);
                BlockRegistry.cropsdropSeeds = currentSeedSetting;

                FieldItems.EnumFullPlantType fieldType;
                if (!ItemRegistry.enablecropitemsasseeds) {
                    boolean isWaterPlant = ((IPlantable) rawSeed).getPlantType(null, 0, 0, 0) == EnumPlantType.Water;

                    if (isWaterPlant && BlockRegistry.enablecropspecialplanting) {
                        fieldType = FieldItems.EnumFullPlantType.Water;
                    } else {
                        fieldType = FieldItems.EnumFullPlantType.Crop;
                    }
                } else {
                    fieldType = FieldItems.convertType(((IPlantable) rawSeed).getPlantType(null, 0, 0, 0));
                }

                List<ItemStack> produce = new ArrayList<>();
                produce.add(new ItemStack(rawProduce));

                output.add(
                    new PlantRecipe(
                        new ItemStack(rawSeed),
                        fieldType,
                        PlantRecipe.EnumPlantProcesses.BASIC,
                        produce,
                        new ArrayList<>(),
                        null,
                        PlantRecipe.RecipeType.CROP,
                        modOrigin));
            }
        }

        return output;
    }
}
