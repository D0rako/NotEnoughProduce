package com.dorako.neicrop.core;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class PlantRecipe implements Comparable<PlantRecipe> {

    public enum EnumPlantProcesses {
        BASIC,
        BONEMEAL
    }

    public enum RecipeType {
        CROP,
        GROWS,
        DUPLICATING,
        VINE,
        TREE,
        OTHER
    }

    public enum ModOrigin {
        VANILLA,
        HARVESTCRAFT
    }

    public ItemStack seed;
    public FieldItems.EnumFullPlantType fieldType;
    public EnumPlantProcesses processType;
    /**
     * base return - carrots, logs, etc
     */
    public List<ItemStack> produce;
    /**
     * secondary return - leaf decay produce, etc
     */
    public List<ItemStack> secondaryProduce;

    public String notes;

    // sorting vars
    public RecipeType type;
    public ModOrigin origin;
    public static int numMade = 0;
    public int addOrder;

    public PlantRecipe(ItemStack seed, FieldItems.EnumFullPlantType fieldType, EnumPlantProcesses processType,
        List<ItemStack> produce, List<ItemStack> secondaryProduce, String notes, RecipeType type, ModOrigin origin) {
        this.seed = seed;
        this.fieldType = fieldType;
        this.processType = processType;
        this.produce = produce;
        this.secondaryProduce = secondaryProduce;
        this.notes = notes;

        this.type = type;
        this.origin = origin;
        this.addOrder = numMade;
        numMade++;

        String caveNote = StatCollector.translateToLocal("neicrop.notes.caveReqs");
        String mushroomNote = StatCollector.translateToLocal("neicrop.notes.mushroomReqs");
        String beachNote = StatCollector.translateToLocal("neicrop.notes.beachReqs");

        if (fieldType == FieldItems.EnumFullPlantType.Cave) {
            if (notes != null) {
                this.notes = caveNote + "\n" + notes;
            } else {
                this.notes = caveNote;
            }
        } else if (fieldType == FieldItems.EnumFullPlantType.Mushroom) {
            if (notes != null) {
                this.notes = mushroomNote + "\n" + notes;
            } else {
                this.notes = mushroomNote;
            }
        } else if (fieldType == FieldItems.EnumFullPlantType.Beach) {
            if (notes != null) {
                this.notes = beachNote + "\n" + notes;
            } else {
                this.notes = beachNote;
            }
        }
    }

    @Override
    public int compareTo(PlantRecipe other) {
        if (this.type != other.type) {
            return this.type.ordinal() - other.type.ordinal();
        }
        if (this.origin != other.origin) {
            return this.origin.ordinal() - other.origin.ordinal();
        }
        if (this.seed.getItem() != other.seed.getItem()) {
            return Item.getIdFromItem(this.seed.getItem()) - Item.getIdFromItem(other.seed.getItem());
        }
        if (this.seed.stackSize != other.seed.stackSize) {
            return this.seed.stackSize - other.seed.stackSize;
        }
        return this.addOrder - other.addOrder;
    }
}
