package com.wineofzamorakhelper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("wineofzamorakhelper")
public interface WineOfZamorakHelperConfig extends Config {
    @ConfigItem(
            keyName = "showWineCounterBox",
            name = "Show Wine Counter",
            description = "Toggle whether the Wine of Zamorak counter box is shown"
    )
    default boolean showWineCounterBox() {
        return true;
    }
}
