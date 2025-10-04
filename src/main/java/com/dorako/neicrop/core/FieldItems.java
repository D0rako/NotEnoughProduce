package com.dorako.neicrop.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import com.dorako.neicrop.mixins.BiomeGenBaseAccessor;

import biomesoplenty.api.content.BOPCBlocks;
import biomesoplenty.common.blocks.BlockBOPNewDirt;
import biomesoplenty.common.blocks.BlockBOPNewGrass;
import cpw.mods.fml.common.Loader;

public class FieldItems {

    /**
     * A more expansive EnumPlantType to include logs (which cocoa beans, etc, can grow in
     */
    public enum EnumFullPlantType {
        Plains,
        Desert,
        Beach,
        Cave,
        Water,
        Nether,
        Crop,
        Log,
        Mushroom,
        FullNether,
        NetherPlains,
        OvergrownNetherrack,
        Dirt,
        BOPPine,
        BOPMoss
    }

    private static Map<EnumFullPlantType, List<ItemStack>> fieldGroupItems;
    private static List<ItemStack> flowerItems;

    /**
     * Generates a static list of valid items that can sustain each kind of plant
     * done by checking Block.canSustainPlant for each unique block in the game (this feels wasteful but whatever)
     */
    private static void generate() {
        Map<EnumFullPlantType, List<ItemStack>> fieldGroupItems = new HashMap<>();

        for (EnumPlantType plantType : EnumPlantType.values()) {
            List<ItemStack> validFieldList = new ArrayList<>();
            List<Integer> idsAdded = new ArrayList<>();

            // this is literally most blocks in the game - let's just fudge this one
            if (plantType == EnumPlantType.Cave) {
                validFieldList.add(new ItemStack(Blocks.stone, 1));
                fieldGroupItems.put(FieldItems.convertType(plantType), validFieldList);
                continue;
            }

            FakePlant plant = new FakePlant(plantType);
            for (Object blockRaw : Block.blockRegistry) {
                Block block = (Block) blockRaw;
                if (block.canSustainPlant(new FakeWorld(block), 0, 0, 0, null, plant)) {

                    if (Loader.isModLoaded("BiomesOPlenty")) {
                        if (block instanceof BlockBOPNewDirt) {
                            for (int i = 0; i < 6; i++) validFieldList.add(new ItemStack(block, 1, i));
                            continue;
                        } else if (block instanceof BlockBOPNewGrass) {
                            for (int i = 0; i < 3; i++) validFieldList.add(new ItemStack(block, 1, i));
                            continue;
                        }
                    }
                    validFieldList.add(new ItemStack(block, 1));
                }
            }

            fieldGroupItems.put(FieldItems.convertType(plantType), validFieldList);
        }

        // get all mushrooms
        List<ItemStack> mushroomBlocks = new ArrayList<>();
        List<ItemStack> logBlocks = new ArrayList<>();
        for (Object blockRaw : Block.blockRegistry) {
            Block block = (Block) blockRaw;
            if (block == Blocks.mycelium) {
                Item dirt = block.getItem(null, 0, 0, 0);
                mushroomBlocks.add(new ItemStack(dirt, 1));
            }
        }

        // cocoa bean logs
        for (int i = 0; i < 4; i++) logBlocks.add(new ItemStack(Blocks.log, 1, i));

        // flowers
        // get all biomes in list form
        Map<String, BiomeGenBase> biomeList = new HashMap<>();
        for (BiomeDictionary.Type biomeType : BiomeDictionary.Type.values()) {
            BiomeGenBase[] biomes = BiomeDictionary.getBiomesForType(biomeType);
            for (BiomeGenBase biome : biomes) {
                if (!biomeList.containsKey(biome.biomeName)) {
                    biomeList.put(biome.biomeName, biome);
                }
            }
        }

        // get all spawnable flowers from every biome
        Map<String, ItemStack> spawnableFlowers = new HashMap<>();
        for (BiomeGenBase biome : biomeList.values()) {
            List<BiomeGenBase.FlowerEntry> validFlowers = ((BiomeGenBaseAccessor) biome).getFlowers();
            for (BiomeGenBase.FlowerEntry flower : validFlowers) {
                if (!spawnableFlowers.containsKey(flower.block.getUnlocalizedName())) {
                    spawnableFlowers.put(flower.block.getUnlocalizedName(), new ItemStack(flower.block));
                }
            }
        }

        // podzol
        Item dirt = ((Item) (Item.itemRegistry.getObject("dirt")));
        mushroomBlocks.add(new ItemStack(dirt, 1, 2));

        fieldGroupItems.put(EnumFullPlantType.Log, logBlocks);
        fieldGroupItems.put(EnumFullPlantType.Mushroom, mushroomBlocks);

        // dumber reqirements
        List<ItemStack> netherBlocks = new ArrayList<>();
        netherBlocks.add(new ItemStack(Blocks.netherrack));
        netherBlocks.add(new ItemStack(Blocks.soul_sand));

        List<ItemStack> netherPlainBlocks = new ArrayList<>(netherBlocks);
        netherPlainBlocks.add(new ItemStack(Blocks.dirt));
        netherPlainBlocks.add(new ItemStack(Blocks.grass));
        netherPlainBlocks.add(new ItemStack(Blocks.farmland));

        fieldGroupItems.put(EnumFullPlantType.FullNether, netherBlocks);
        fieldGroupItems.put(EnumFullPlantType.NetherPlains, netherPlainBlocks);

        List<ItemStack> overgrownNetherrack = new ArrayList<>();
        if (Loader.isModLoaded("BiomesOPlenty")) {
            overgrownNetherrack.add(new ItemStack(BOPCBlocks.overgrownNetherrack));
        }
        fieldGroupItems.put(EnumFullPlantType.OvergrownNetherrack, overgrownNetherrack);

        List<ItemStack> dirtType = new ArrayList<>();
        dirtType.add(new ItemStack(Blocks.dirt));
        dirtType.add(new ItemStack(Blocks.grass));
        fieldGroupItems.put(EnumFullPlantType.Dirt, dirtType);

        List<ItemStack> bopPineType = new ArrayList<>();
        bopPineType.add(new ItemStack(Blocks.grass));
        bopPineType.add(new ItemStack(Blocks.dirt));
        if (Loader.isModLoaded("BiomesOPlenty")) {
            for (int i = 0; i < 6; i++) bopPineType.add(new ItemStack(BOPCBlocks.newBopDirt, 1, i));
            for (int i = 0; i < 3; i++) bopPineType.add(new ItemStack(BOPCBlocks.newBopGrass, 1, i));
        }
        fieldGroupItems.put(EnumFullPlantType.BOPPine, bopPineType);

        List<ItemStack> bopMossType = new ArrayList<>();
        for (Object rawBlock : Block.blockRegistry) {
            Block block = (Block) rawBlock;
            try {
                if (block.isWood(null, 0, 0, 0)) {
                    // TODO: figure out all subtypes of all logs maybe
                    bopMossType.add(new ItemStack(block));
                }
            } catch (Exception ignored) {}
        }
        bopMossType.add(new ItemStack(Blocks.stone));
        fieldGroupItems.put(EnumFullPlantType.BOPMoss, bopMossType);

        FieldItems.fieldGroupItems = fieldGroupItems;
        FieldItems.flowerItems = new ArrayList<>(spawnableFlowers.values());
    }

    /**
     * @return A map of each block that can sustain a given type of plant, sorted by type
     */
    public static List<ItemStack> getFieldItems(EnumFullPlantType type) {
        if (FieldItems.fieldGroupItems == null) {
            FieldItems.generate();
        }

        return FieldItems.fieldGroupItems.get(type);
    }

    public static List<ItemStack> getFieldItems(EnumPlantType type) {
        return FieldItems.getFieldItems(FieldItems.convertType(type));
    }

    public static List<ItemStack> getFlowerItems() {
        if (FieldItems.flowerItems == null) {
            FieldItems.generate();
        }

        return FieldItems.flowerItems;
    }

    public static EnumFullPlantType convertType(EnumPlantType type) {
        switch (type) {
            case Plains -> {
                return FieldItems.EnumFullPlantType.Plains;
            }
            case Desert -> {
                return FieldItems.EnumFullPlantType.Desert;
            }
            case Beach -> {
                return FieldItems.EnumFullPlantType.Beach;
            }
            case Cave -> {
                return FieldItems.EnumFullPlantType.Cave;
            }
            case Water -> {
                return FieldItems.EnumFullPlantType.Water;
            }
            case Nether -> {
                return FieldItems.EnumFullPlantType.Nether;
            }
            case Crop -> {
                return FieldItems.EnumFullPlantType.Crop;
            }
        }
        return null;
    }

    /**
     * used to find valid planting logic
     */
    static class FakePlant implements IPlantable {

        final EnumPlantType type;

        FakePlant(EnumPlantType type) {
            this.type = type;
        }

        @Override
        public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
            return type;
        }

        @Override
        public Block getPlant(IBlockAccess world, int x, int y, int z) {
            return null;
        }

        @Override
        public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
            return 0;
        }
    }

    /**
     * required to make all blocks look like they're potential beaches (ie, next to water)
     */
    static class FakeWorld implements IBlockAccess {

        private final Block block;

        public FakeWorld(Block block) {
            this.block = block;
        }

        @Override
        public Block getBlock(int x, int y, int z) {
            if (x != 0 || y != 0 || z != 0) {
                return Blocks.water;
            }
            return block;
        }

        @Override
        public TileEntity getTileEntity(int x, int y, int z) {
            return null;
        }

        @Override
        public int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_) {
            return 0;
        }

        @Override
        public int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_) {
            return 0;
        }

        @Override
        public int isBlockProvidingPowerTo(int x, int y, int z, int directionIn) {
            return 0;
        }

        @Override
        public boolean isAirBlock(int x, int y, int z) {
            return false;
        }

        @Override
        public BiomeGenBase getBiomeGenForCoords(int x, int z) {
            return null;
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public boolean extendedLevelsInChunkCache() {
            return false;
        }

        @Override
        public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
            return false;
        }
    }
}
