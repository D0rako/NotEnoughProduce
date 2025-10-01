package com.dorako.neicrop.core;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class PlantRecipe {

    public enum EnumPlantProcesses {
        BASIC,
        BONEMEAL
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

    public PlantRecipe(ItemStack seed, FieldItems.EnumFullPlantType fieldType, EnumPlantProcesses processType,
        List<ItemStack> produce, List<ItemStack> secondaryProduce, String notes) {
        this.seed = seed;
        this.fieldType = fieldType;
        this.processType = processType;
        this.produce = produce;
        this.secondaryProduce = secondaryProduce;
        this.notes = notes;

        String mushroomNote = StatCollector.translateToLocal("neicrop.notes.mushroomReqs");

        if (fieldType == FieldItems.EnumFullPlantType.Mushroom) {
            if (notes != null) {
                this.notes = mushroomNote + "\n" + notes;
            } else {
                this.notes = mushroomNote;
            }
        }
    }
}
