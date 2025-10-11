package com.dorako.neicrop.core.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockVine;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import com.dorako.neicrop.core.FieldItems;
import com.dorako.neicrop.core.PlantRecipe;

import cpw.mods.fml.client.FMLClientHandler;

public class VanillaRecipes {

    private static final String modId = "minecraft";
    private static final PlantRecipe.ModOrigin modOrigin = PlantRecipe.ModOrigin.VANILLA;

    public static List<PlantRecipe> generateRecipes() {
        String[] SEED_NAMES = { "wheat_seeds", "carrot", "potato", "melon_seeds", "pumpkin_seeds", "dye",
            /* cocoa */ "reeds", "cactus", "brown_mushroom"/* passive */, "brown_mushroom"/* bonemeal */,
            "red_mushroom", "red_mushroom", "nether_wart", "vine", "sapling"/* oak */, "sapling"/* spruce */,
            "sapling"/* birch */, "sapling"/* jungle */, "sapling"/* mega jungle */, "sapling"/* acadia */,
            "sapling"/* dark oak */, "grass"/* bonemeal flowers */, "double_plant"/* sunflower */,
            "double_plant"/* syringa (lilac) */, "double_plant"/* rose */, "double_plant",/* paeonia (peony) */
        };
        int[] SEED_DAMAGES = { 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 3, 4, 5, 0, 0, 1, 4, 5 };
        PlantRecipe.EnumPlantProcesses DEFAULT_FIELD_PROCESS = PlantRecipe.EnumPlantProcesses.BASIC;

        ItemStack[] META_TO_LOG = { new ItemStack((Item) Item.itemRegistry.getObject("log"), 1, 0),
            new ItemStack((Item) Item.itemRegistry.getObject("log"), 1, 1),
            new ItemStack((Item) Item.itemRegistry.getObject("log"), 1, 2),
            new ItemStack((Item) Item.itemRegistry.getObject("log"), 1, 3),
            new ItemStack((Item) Item.itemRegistry.getObject("log2"), 1, 0),
            new ItemStack((Item) Item.itemRegistry.getObject("log2"), 1, 1) };
        ItemStack[] META_TO_LEAF = { new ItemStack((Item) Item.itemRegistry.getObject("leaves"), 1, 0),
            new ItemStack((Item) Item.itemRegistry.getObject("leaves"), 1, 1),
            new ItemStack((Item) Item.itemRegistry.getObject("leaves"), 1, 2),
            new ItemStack((Item) Item.itemRegistry.getObject("leaves"), 1, 3),
            new ItemStack((Item) Item.itemRegistry.getObject("leaves2"), 1, 0),
            new ItemStack((Item) Item.itemRegistry.getObject("leaves2"), 1, 1) };

        Map<String, ItemStack> SEED_TO_GOURD = new HashMap<>();
        SEED_TO_GOURD.put("seeds_pumpkin", new ItemStack((Item) Item.itemRegistry.getObject("pumpkin")));
        SEED_TO_GOURD.put("seeds_melon", new ItemStack((Item) Item.itemRegistry.getObject("melon_block")));

        Map<Integer, Integer> seedAmountOverrides = new HashMap<>();
        seedAmountOverrides.put(18, 4);
        seedAmountOverrides.put(20, 4);

        Map<Integer, FieldItems.EnumFullPlantType> fieldGroupOverrides = new HashMap<>();
        fieldGroupOverrides.put(5, FieldItems.EnumFullPlantType.Log);
        fieldGroupOverrides.put(8, FieldItems.EnumFullPlantType.Mushroom);
        fieldGroupOverrides.put(9, FieldItems.EnumFullPlantType.Mushroom);
        fieldGroupOverrides.put(10, FieldItems.EnumFullPlantType.Mushroom);
        fieldGroupOverrides.put(11, FieldItems.EnumFullPlantType.Mushroom);

        Map<Integer, PlantRecipe.EnumPlantProcesses> fieldProcessesOverrides = new HashMap<>();
        fieldProcessesOverrides.put(9, PlantRecipe.EnumPlantProcesses.BONEMEAL);
        fieldProcessesOverrides.put(11, PlantRecipe.EnumPlantProcesses.BONEMEAL);
        fieldProcessesOverrides.put(21, PlantRecipe.EnumPlantProcesses.BONEMEAL);
        fieldProcessesOverrides.put(22, PlantRecipe.EnumPlantProcesses.BONEMEAL);
        fieldProcessesOverrides.put(23, PlantRecipe.EnumPlantProcesses.BONEMEAL);
        fieldProcessesOverrides.put(24, PlantRecipe.EnumPlantProcesses.BONEMEAL);
        fieldProcessesOverrides.put(25, PlantRecipe.EnumPlantProcesses.BONEMEAL);

        Map<Integer, List<ItemStack>> fieldSecondaryProducesOverrides = new HashMap<>();
        List<ItemStack> redMushroomOutput = new ArrayList<>();
        redMushroomOutput.add(new ItemStack(((Item) Item.itemRegistry.getObject("red_mushroom")), 1));
        fieldSecondaryProducesOverrides.put(9, redMushroomOutput);
        List<ItemStack> brownMushroomOutput = new ArrayList<>();
        brownMushroomOutput.add(new ItemStack(((Item) Item.itemRegistry.getObject("brown_mushroom")), 1));
        fieldSecondaryProducesOverrides.put(11, brownMushroomOutput);

        Map<Integer, String> fieldNotes = new HashMap<>();
        fieldNotes.put(3, StatCollector.translateToLocal("neicrop.notes.isStem"));
        fieldNotes.put(4, StatCollector.translateToLocal("neicrop.notes.isStem"));
        fieldNotes.put(8, StatCollector.translateToLocal("neicrop.notes.duplicating"));
        fieldNotes.put(10, StatCollector.translateToLocal("neicrop.notes.duplicating"));
        fieldNotes.put(
            13,
            StatCollector.translateToLocal("neicrop.notes.vineLike") + "|"
                + StatCollector.translateToLocal("neicrop.notes.shears")
                + "|"
                + StatCollector.translateToLocal("neicrop.notes.growDownwards"));
        fieldNotes.put(18, StatCollector.translateToLocal("neicrop.notes.megaTree"));
        fieldNotes.put(20, StatCollector.translateToLocal("neicrop.notes.megaTree"));
        fieldNotes.put(21, StatCollector.translateToLocal("neicrop.notes.grassFlowers"));
        fieldNotes.put(22, StatCollector.translateToLocal("neicrop.notes.drop"));
        fieldNotes.put(23, StatCollector.translateToLocal("neicrop.notes.drop"));
        fieldNotes.put(24, StatCollector.translateToLocal("neicrop.notes.drop"));
        fieldNotes.put(25, StatCollector.translateToLocal("neicrop.notes.drop"));

        List<ItemStack> seedList = new ArrayList<>();
        for (int i = 0; i < SEED_NAMES.length; i++) {
            Item item = (Item) Item.itemRegistry.getObject(modId + ":" + SEED_NAMES[i]);

            Integer stackAmount = 1;
            if (seedAmountOverrides.containsKey(i)) stackAmount = seedAmountOverrides.get(i);
            ItemStack stack = new ItemStack(item, stackAmount);

            stack.setItemDamage(SEED_DAMAGES[i]);
            seedList.add(stack);
        }

        PlantRecipe.RecipeType type = null;
        List<PlantRecipe> output = new ArrayList<>();
        for (int i = 0; i < seedList.size(); i++) {
            ItemStack seed = seedList.get(i);
            Item seedItem = seed.getItem();
            Block blockItem;
            if (seedItem instanceof ItemBlock) {
                blockItem = ((ItemBlock) seedItem).field_150939_a;
            } else {
                blockItem = (Block) Block.blockRegistry.getObject(SEED_NAMES[i]);
            }

            FieldItems.EnumFullPlantType fieldType = null;
            if (seedItem instanceof IPlantable) {
                EnumPlantType baseType = ((IPlantable) (seedItem)).getPlantType(null, 0, 0, 0);
                fieldType = FieldItems.convertType(baseType);
            } else if (blockItem instanceof IPlantable) {
                EnumPlantType baseType = ((IPlantable) (blockItem)).getPlantType(null, 0, 0, 0);
                fieldType = FieldItems.convertType(baseType);
            }
            if (fieldGroupOverrides.containsKey(i)) {
                fieldType = fieldGroupOverrides.get(i);
            }

            PlantRecipe.EnumPlantProcesses process = DEFAULT_FIELD_PROCESS;
            if (fieldProcessesOverrides.containsKey(i)) {
                process = fieldProcessesOverrides.get(i);
            }

            // produce
            List<ItemStack> produce = new ArrayList<>();
            if (seedItem instanceof IPlantable || blockItem instanceof IPlantable) {
                BlockBush crop = null;
                if (seedItem instanceof IPlantable) {
                    crop = (BlockBush) ((IPlantable) seedItem).getPlant(null, 0, 0, 0);
                }

                if (blockItem instanceof BlockMushroom) {
                    if (process == PlantRecipe.EnumPlantProcesses.BONEMEAL) {
                        type = PlantRecipe.RecipeType.TREE;
                        // stems don't exist in 1.7
                        if (blockItem == Blocks.red_mushroom) {
                            produce.add(new ItemStack(Blocks.red_mushroom_block, 1));
                        } else if (blockItem == Blocks.brown_mushroom) {
                            produce.add(new ItemStack(Blocks.brown_mushroom_block, 1));
                        }
                    } else {
                        // duplicates self
                        type = PlantRecipe.RecipeType.DUPLICATING;
                        produce.add(new ItemStack(seedItem, 1));
                    }
                } else if (crop instanceof BlockStem) {
                    type = PlantRecipe.RecipeType.CROP;
                    String indexedName = seedItem.getUnlocalizedName();
                    produce.add(SEED_TO_GOURD.get(indexedName.substring(indexedName.indexOf('.') + 1)));
                } else if (seedItem instanceof IPlantable) {
                    type = PlantRecipe.RecipeType.CROP;
                    Item itemCrop = crop.getItemDropped(7, null, 0);
                    // TODO: add chances (1 guaranteed + 2+fortune 50% drops) (test this first, may be left-click)

                    // nether wart
                    if (itemCrop == null) {
                        // TODO: 2 to 4 + (0 to fortune + 1 if fortune exists)
                        produce.add(
                            crop.getDrops(
                                FMLClientHandler.instance()
                                    .getWorldClient(),
                                0,
                                0,
                                0,
                                0,
                                0)
                                .get(0));
                    } else {
                        produce.add(new ItemStack(itemCrop, 1));
                    }
                } else {// if(blockItem instanceof IPlantable){
                    if (blockItem instanceof BlockSapling) {
                        type = PlantRecipe.RecipeType.TREE;
                        // TODO: if you're gonna give amounts for other things, figure these out too
                        // log + leaves
                        produce.add(META_TO_LOG[SEED_DAMAGES[i]]);
                        produce.add(META_TO_LEAF[SEED_DAMAGES[i]]);

                        // mega jungle also has vines on it
                        if (SEED_DAMAGES[i] == 3 && seed.stackSize == 4) {
                            produce.add(new ItemStack(Blocks.vine));
                        }
                    } else if (blockItem instanceof BlockDoublePlant) {
                        // double-tile flowers
                        type = PlantRecipe.RecipeType.OTHER;
                        produce.add(new ItemStack(seedItem, 1, SEED_DAMAGES[i]));
                    } else {
                        // self-duplicating items - reeds & cacti
                        type = PlantRecipe.RecipeType.GROWS;
                        produce.add(new ItemStack(blockItem.getItem(null, 0, 0, 0)));
                    }
                }
            } else if (seedItem instanceof ItemDye) {
                // 3x cocoa beans
                type = PlantRecipe.RecipeType.VINE;
                produce.add(new ItemStack(seedItem, 3, 3));
            } else if (blockItem instanceof BlockGrass) {
                // bonemealing grass - spawnable flowers + grass (the shrub)
                type = PlantRecipe.RecipeType.OTHER;
                produce.addAll(FieldItems.getFlowerItems());
                produce.add(new ItemStack(Blocks.tallgrass, 1, 1));
            } else if (blockItem instanceof BlockVine) {
                // vines
                type = PlantRecipe.RecipeType.VINE;
                produce.add(new ItemStack(seedItem, 1));
            }

            // secondary produce
            List<ItemStack> secondaryProduce = new ArrayList<>();
            if (seedItem instanceof ItemBlock) {
                Object crop = ((ItemBlock) seedItem).field_150939_a;
                if (crop instanceof BlockSapling) {
                    // sapling
                    // TODO: drop chance - 1 / 20 w/ no fortune, 1 / max((20 - 2 * 2^fortune), 10) otherwise
                    // 20 is replaced with 40 for jungle saplings

                    // mega jungle tree isn't GUARANTEED to give 4 from every branch (probably)
                    if (seed.stackSize > 1) {
                        ItemStack reproduction = seed.copy();
                        reproduction.stackSize = 1;
                        secondaryProduce.add(reproduction);
                    } else secondaryProduce.add(seed);

                    // if oak, add apples
                    // TODO: apple drop chance - 1 / 200 w/ no fortune, 1 / max((200 - 10 * 2^fortune), 40) otherwise
                    if (SEED_DAMAGES[i] == 0 || SEED_DAMAGES[i] == 5) {
                        secondaryProduce.add(new ItemStack(((Item) Item.itemRegistry.getObject("apple"))));
                    }
                }
            }
            if (fieldSecondaryProducesOverrides.containsKey(i)) {
                secondaryProduce = fieldSecondaryProducesOverrides.get(i);
            }

            String notes = null;
            if (fieldNotes.containsKey(i)) {
                notes = fieldNotes.get(i);
            }

            PlantRecipe recipe = new PlantRecipe(
                seed,
                fieldType,
                process,
                produce,
                secondaryProduce,
                notes,
                type,
                modOrigin);
            output.add(recipe);
        }

        return output;
    }
}
