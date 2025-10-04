package com.dorako.neicrop;

import java.io.File;
import java.io.IOException;

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

public class Config {

    public static boolean showHarvestcraftGardens = true;
    public static boolean overrideForGTNHRecipes = false;

    public static void synchronizeConfiguration(File configFile) {
        try {
            configFile.createNewFile();
        } catch (IOException e) {

        }
        Configuration config = new Configuration(configFile);
        config.load();

        showHarvestcraftGardens = config.getBoolean(
            StatCollector.translateToLocal("neicrop.config.showHarvestcraftGardens.key"),
            Configuration.CATEGORY_GENERAL,
            showHarvestcraftGardens,
            StatCollector.translateToLocal("neicrop.config.showHarvestcraftGardens.value"));

        overrideForGTNHRecipes = config.getBoolean(
            StatCollector.translateToLocal("neicrop.config.overrideForGTNHRecipes.key"),
            Configuration.CATEGORY_GENERAL,
            overrideForGTNHRecipes,
            StatCollector.translateToLocal("neicrop.config.overrideForGTNHRecipes.value"));

        if (config.hasChanged()) {
            config.save();
        }
    }
}
