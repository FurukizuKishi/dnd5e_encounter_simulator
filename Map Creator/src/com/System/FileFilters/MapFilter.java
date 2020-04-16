package com.System.FileFilters;

import com.System.Enums;

public class MapFilter extends ExtensionFilter {
    public Enums.fileAction action;
    public MapFilter(Enums.fileAction action) {
        setExtensions("Map Data File", "mpdata");
        this.action = action;
    }
}
