package com.System;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;

public class ImageFilter extends FileFilter {
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
            String[] extensions = {"png", "jpg", "jpeg"};
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
        return "Image Files (.jpg, .jpeg, .png)";
    }
}