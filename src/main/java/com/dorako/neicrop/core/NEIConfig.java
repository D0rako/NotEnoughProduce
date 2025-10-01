package com.dorako.neicrop.core;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

@SuppressWarnings("unused")
public class NEIConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new Handler());
        API.registerUsageHandler(new Handler());
    }

    @Override
    public String getName() {
        return "NEICrop";
    }

    @Override
    public String getVersion() {
        return "1";
    }
}
