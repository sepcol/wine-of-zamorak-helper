package com.wineofzamorakhelper;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class WineOfZamorakHelperPluginTest {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(WineOfZamorakHelperPlugin.class);
        RuneLite.main(args);
    }
}