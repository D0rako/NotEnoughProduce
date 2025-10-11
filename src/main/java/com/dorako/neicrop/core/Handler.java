package com.dorako.neicrop.core;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.dorako.neicrop.mixins.GuiContainerAccessor;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

// https://github.com/GTNewHorizons/Mobs-Info/blob/master/src/main/java/com/kuba6000/mobsinfo/nei/VillagerTradesHandler.java#L159
// https://github.com/GTNewHorizons/EnderIO/blob/d69d77ecae563496d50bbcb7a82072c6003417ce/src/main/java/crazypants/enderio/nei/SagMillRecipeHandler.java
// https://github.com/GTNewHorizons/BetterQuesting/blob/master/src/main/java/bq_standard/integration/nei/QuestRecipeHandler.java

// add recipes to arecipes

public class Handler extends TemplateRecipeHandler {

    private static final int RECIPE_WIDTH = 166;
    private static final int RECIPE_HEIGHT = 105;

    private static final int[] GUI_BASE_BBOX = { 0, 0, RECIPE_WIDTH, RECIPE_HEIGHT };
    private static final int[] GUI_NOTE_BBOX = { 10, 24, 16, 16 };
    private static final int[] GUI_ARROW_BBOX = { 49, 70, 49, 31 };
    private static final int[] GUI_ADDITIONAL_BBOX = { 102, 23, 36, 42 };

    private static final Rectangle GUI_NOTE_RECT = new Rectangle(
        GUI_NOTE_BBOX[0],
        GUI_NOTE_BBOX[1],
        GUI_NOTE_BBOX[2],
        GUI_NOTE_BBOX[3]);

    /*
     * 0 - bonemeal arrow, unfilled, 1 - filled arrow, 2 - filled bonemeal arrow, 3 - info icon,
     * 4 - additional produce
     */
    private static final int[][] GUI_TEXTURES_BBOX = { { 166, 0, 49, 31 }, { 166, 31, 49, 31 }, { 166, 62, 49, 31 },
        { 166, 93, 16, 16 }, { 166, 109, 36, 42 } };

    private static final int[] SEED_COORD = { 28, 24 };
    private static final int[] FIELD_COORD = { 28, 77 };
    private static final int[] PRODUCE_COORD = { 103, 68 };
    private static final int[] SECONDARY_COORD = { 103, 24 };

    public class CropCachedRecipe extends CachedRecipe {

        /**
         * allows creating a permutable stack without going through the ore dictionary
         */
        public static class PositionedMultiStack extends PositionedStack {

            public PositionedMultiStack(List<ItemStack> stack, int x, int y) {
                super(stack, x, y, false);

                items = stack.toArray(new ItemStack[0]);
                setPermutationToRender(0);
            }
        }

        private final PlantRecipe recipeBase;

        private List<PositionedStack> cachedIngredients;
        private List<PositionedStack> cachedOthers;

        public CropCachedRecipe(PlantRecipe input) {
            // null values should be treated as valid
            recipeBase = input;

            generateIngredients();
            generateOthers();
        }

        private void generateIngredients() {
            cachedIngredients = new ArrayList<>();
            if (recipeBase.fieldType == null) {
                PositionedStack seed = new PositionedStack(recipeBase.seed, FIELD_COORD[0], FIELD_COORD[1]);
                cachedIngredients.add(seed);
            } else {
                PositionedStack seed = new PositionedStack(recipeBase.seed, SEED_COORD[0], SEED_COORD[1]);
                cachedIngredients.add(seed);
            }
        }

        private void generateOthers() {
            cachedOthers = new ArrayList<>();

            if (recipeBase.fieldType != null) {
                List<ItemStack> validFields = FieldItems.getFieldItems(recipeBase.fieldType);
                PositionedStack field = new PositionedMultiStack(validFields, FIELD_COORD[0], FIELD_COORD[1]);
                cachedOthers.add(field);
            }

            // override default cycle-only-for-ingredients logic
            // NOTE: if you want to change this so shift+scroll wheel cycles these faster, fix nei so it uses
            // setPermutationToRender on all items (or at least a handler-level toggle), then remove everything that
            // isn't just returning a list of PositionedMultiStacks
            if (recipeBase.produce.size() > 4) {
                PositionedStack produce = new PositionedMultiStack(
                    recipeBase.produce,
                    PRODUCE_COORD[0],
                    PRODUCE_COORD[1]);
                cachedOthers.add(produce);
            } else {
                for (int i = 0; i < recipeBase.produce.size(); i++) {
                    int x = PRODUCE_COORD[0];
                    int y = PRODUCE_COORD[1];

                    if (i % 2 == 1) x += 18;
                    if (i > 1) y += 18;

                    PositionedStack stack = new PositionedStack(recipeBase.produce.get(i), x, y);
                    cachedOthers.add(stack);
                }
            }

            if (recipeBase.secondaryProduce.size() > 2) {
                PositionedStack produce = new PositionedMultiStack(
                    recipeBase.secondaryProduce,
                    SECONDARY_COORD[0],
                    SECONDARY_COORD[1]);
                cachedOthers.add(produce);
            } else {
                for (int i = 0; i < recipeBase.secondaryProduce.size(); i++) {
                    int x = SECONDARY_COORD[0];
                    int y = SECONDARY_COORD[1];

                    if (i > 0) x += 18;

                    PositionedStack stack = new PositionedStack(recipeBase.secondaryProduce.get(i), x, y);
                    cachedOthers.add(stack);
                }
            }
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, cachedIngredients);
        }

        // private List<List<ItemStack>> produceRotateCache;

        @Override
        public List<PositionedStack> getOtherStacks() {
            return getCycledIngredients(cycleticks / 20, cachedOthers);
        }

        public PlantRecipe getRecipeBase() {
            return this.recipeBase;
        }

        public boolean hasIngredient(ItemStack item) {
            return NEIServerUtils.areStacksSameTypeCraftingWithNBT(item, recipeBase.seed);
        }

        public boolean hasResult(ItemStack item) {
            for (ItemStack compare : recipeBase.produce) {
                if (NEIServerUtils.areStacksSameTypeCraftingWithNBT(item, compare)) {
                    return true;
                }
            }
            for (ItemStack compare : recipeBase.secondaryProduce) {
                if (NEIServerUtils.areStacksSameTypeCraftingWithNBT(item, compare)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("neicrop.header");
    }

    @Override
    public String getGuiTexture() {
        return "NEICrop:textures/gui/template.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "NEICrop";
    }

    private List<CropCachedRecipe> convertList(List<PlantRecipe> list) {
        List<CropCachedRecipe> output = new ArrayList<>(list.size());

        for (PlantRecipe recipe : list) {
            output.add(new CropCachedRecipe(recipe));
        }

        return output;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier())) {
            List<CropCachedRecipe> recipes = convertList(CoreLoader.getRecipes());
            arecipes.addAll(recipes);
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        List<CropCachedRecipe> recipes = convertList(CoreLoader.getRecipes());
        for (CropCachedRecipe recipe : recipes) {
            if (recipe.hasResult(result)) arecipes.add(recipe);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        List<CropCachedRecipe> recipes = convertList(CoreLoader.getRecipes());
        for (CropCachedRecipe recipe : recipes) {
            if (recipe.hasIngredient(ingredient)) arecipes.add(recipe);
        }
    }

    // this makes shift key permutations work
    @Override
    public List<PositionedStack> getIngredientStacks(int recipe) {
        List<PositionedStack> output = new ArrayList<>(
            arecipes.get(recipe)
                .getIngredients());
        output.addAll(
            arecipes.get(recipe)
                .getOtherStacks());
        return output;
    }

    @Override
    public void drawBackground(int recipeInt) {
        CropCachedRecipe recipe = (CropCachedRecipe) arecipes.get(recipeInt);
        PlantRecipe baseRecipe = recipe.getRecipeBase();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, GUI_BASE_BBOX[0], GUI_BASE_BBOX[1], GUI_BASE_BBOX[2], GUI_BASE_BBOX[3]);

        if (!baseRecipe.secondaryProduce.isEmpty()) {
            GuiDraw.drawTexturedModalRect(
                GUI_ADDITIONAL_BBOX[0],
                GUI_ADDITIONAL_BBOX[1],
                GUI_TEXTURES_BBOX[4][0],
                GUI_TEXTURES_BBOX[4][1],
                GUI_TEXTURES_BBOX[4][2],
                GUI_TEXTURES_BBOX[4][3]);
        }
        if (baseRecipe.processType == PlantRecipe.EnumPlantProcesses.BONEMEAL) {
            GuiDraw.drawTexturedModalRect(
                GUI_ARROW_BBOX[0],
                GUI_ARROW_BBOX[1],
                GUI_TEXTURES_BBOX[0][0],
                GUI_TEXTURES_BBOX[0][1],
                GUI_TEXTURES_BBOX[0][2],
                GUI_TEXTURES_BBOX[0][3]);
        }
        if (baseRecipe.notes != null) {
            GuiDraw.drawTexturedModalRect(
                GUI_NOTE_BBOX[0],
                GUI_NOTE_BBOX[1],
                GUI_TEXTURES_BBOX[3][0],
                GUI_TEXTURES_BBOX[3][1],
                GUI_TEXTURES_BBOX[3][2],
                GUI_TEXTURES_BBOX[3][3]);
        }
    }

    @Override
    public void drawExtras(int recipeInt) {
        CropCachedRecipe recipe = (CropCachedRecipe) arecipes.get(recipeInt);
        PlantRecipe baseRecipe = recipe.getRecipeBase();

        if (baseRecipe.processType == PlantRecipe.EnumPlantProcesses.BASIC) {
            drawProgressBar(
                GUI_ARROW_BBOX[0],
                GUI_ARROW_BBOX[1],
                GUI_TEXTURES_BBOX[1][0],
                GUI_TEXTURES_BBOX[1][1],
                GUI_TEXTURES_BBOX[1][2],
                GUI_TEXTURES_BBOX[1][3],
                40,
                0);
        } else {
            drawProgressBar(
                GUI_ARROW_BBOX[0],
                GUI_ARROW_BBOX[1],
                GUI_TEXTURES_BBOX[2][0],
                GUI_TEXTURES_BBOX[2][1],
                GUI_TEXTURES_BBOX[2][2],
                GUI_TEXTURES_BBOX[2][3],
                40,
                0);
        }
    }

    @Override
    public List<String> handleTooltip(GuiRecipe<?> gui, List<String> currenttip, int recipeInt) {
        CropCachedRecipe recipe = (CropCachedRecipe) arecipes.get(recipeInt);
        PlantRecipe baseRecipe = recipe.getRecipeBase();

        if (baseRecipe.notes != null) {
            GuiContainerAccessor castGui = (GuiContainerAccessor) gui;

            currenttip = super.handleTooltip(gui, currenttip, recipeInt);
            Point pos = GuiDraw.getMousePosition();
            Point offset = gui.getRecipePosition(recipeInt);

            Point relMouse = new Point(pos.x - castGui.getGuiLeft() - offset.x, pos.y - castGui.getGuiTop() - offset.y);
            if (GUI_NOTE_RECT.contains(relMouse)) {
                currenttip.addAll(stringToList(baseRecipe.notes));
            }
        }

        return currenttip;
    }

    private List<String> stringToList(String string) {
        List<String> output = new ArrayList<>();
        Collections.addAll(output, string.split("\\|"));
        return output;
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(
            new TemplateRecipeHandler.RecipeTransferRect(
                new Rectangle(GUI_ARROW_BBOX[0], GUI_ARROW_BBOX[1], GUI_ARROW_BBOX[2], GUI_ARROW_BBOX[3]),
                "NEICrop"));
    }

    @Override
    public int recipiesPerPage() {
        return 2;
    }
}
