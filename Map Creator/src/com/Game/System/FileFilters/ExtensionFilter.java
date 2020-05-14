package com.Game.System.FileFilters;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;

public class ExtensionFilter extends FileFilter {
    protected String fileName;
    protected String[] extensions;

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String extension = "";
        int i = file.getName().lastIndexOf(".");
        if (i != -1) {
            extension = file.getName().substring(i + 1);
        }
        if (!extension.equals("")) {
            if (Arrays.asList(extensions).contains(extension.toLowerCase())) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    public String getDescription() {
        if (extensions.length > 0) {
            String str = fileName + " (." + extensions[0];
            for (int i = 1; i < extensions.length; i += 1) {
                str += ", ." + extensions[i];
            }
            return str + ")";
        }
        return "";
    }
    public void setExtensions(String fileName, String ... extensions) {
        this.fileName = fileName;
        this.extensions = extensions;
    }
}
