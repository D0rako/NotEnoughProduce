package com.dorako.neicrop;

import java.io.File;

import com.dorako.neicrop.core.CoreLoader;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(new File(event.getModConfigurationDirectory() + "/NotEnoughProduce.cfg"));
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        CoreLoader.generateAllRecipes();
    }

}
