package com.Game.System.FileFilters;

import com.Game.System.Enums;

public class MapFilter extends ExtensionFilter {
    public Enums.fileAction action;
    public MapFilter(Enums.fileAction action) {
        setExtensions("Map Data File", "mpdata");
        this.action = action;
    }
}
