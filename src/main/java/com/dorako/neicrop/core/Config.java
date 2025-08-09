package com.dorako.neicrop.core;

import codechicken.nei.api.IConfigureNEI;

public class Config implements IConfigureNEI {

    @Override
    public void loadConfig() {

    }

    @Override
    public String getName() {
        return " NEI Plugin";
    }

    @Override
    public String getVersion() {
        return "1";
    }
}
