package com.dorako.neicrop.core.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.dorako.neicrop.core.FieldItems;
import com.dorako.neicrop.core.PlantRecipe;
import com.pam.harvestthenether.BlockNetherGarden;
import com.pam.harvestthenether.BlockNetherSapling;
import com.pam.harvestthenether.BlockPamCrop;
import com.pam.harvestthenether.BlockRegistry;
import com.pam.harvestthenether.ItemRegistry;

public class NetherHarvestRecipes {

    private static final String modId = "harvestcraft";
    private static final PlantRecipe.ModOrigin modOrigin = PlantRecipe.ModOrigin.HARVESTCRAFT;

    private static final boolean INCLUDE_GARDENS = true;

    public static List<PlantRecipe> generateRecipes() {
        List<PlantRecipe> output = new ArrayList<>();

        for (Object blockRaw : Block.blockRegistry) {
            // gardens
            if (INCLUDE_GARDENS && blockRaw instanceof BlockNetherGarden garden) {
                ItemStack seed = new ItemStack(garden);

                List<ItemStack> produce = new ArrayList<>();
                produce.add(seed);

                List<ItemStack> secondaryProduce = new ArrayList<>();

                FieldItems.EnumFullPlantType fieldType = FieldItems.EnumFullPlantType.NetherPlains;
                if (garden == BlockRegistry.netherGarden) {
                    secondaryProduce.add(new ItemStack(ItemRegistry.bloodleafItem));
                    secondaryProduce.add(new ItemStack(ItemRegistry.fleshrootItem));
                    secondaryProduce.add(new ItemStack(ItemRegistry.marrowberryItem));
                    fieldType = FieldItems.EnumFullPlantType.FullNether;
                }

                output.add(
                    new PlantRecipe(
                        seed,
                        fieldType,
                        PlantRecipe.EnumPlantProcesses.BASIC,
                        produce,
                        secondaryProduce,
                        StatCollector.translateToLocal("neicrop.notes.duplicating"),
                        PlantRecipe.RecipeType.DUPLICATING,
                        modOrigin));
            }
            // ignis fruit tree
            if (blockRaw instanceof BlockNetherSapling) {
                ItemStack seed = new ItemStack((Block) blockRaw);

                List<ItemStack> produce = new ArrayList<>();
                List<ItemStack> secondaryProduce = new ArrayList<>();

                produce.add(new ItemStack(BlockRegistry.netherLog));
                produce.add(new ItemStack(BlockRegistry.netherLeaves));
                secondaryProduce.add(new ItemStack(ItemRegistry.ignisfruitItem));

                output.add(
                    new PlantRecipe(
                        seed,
                        FieldItems.EnumFullPlantType.FullNether,
                        PlantRecipe.EnumPlantProcesses.BASIC,
                        produce,
                        secondaryProduce,
                        StatCollector.translateToLocal("neicrop.notes.hasFruit"),
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

                FieldItems.EnumFullPlantType fieldType = FieldItems.EnumFullPlantType.Nether;

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
