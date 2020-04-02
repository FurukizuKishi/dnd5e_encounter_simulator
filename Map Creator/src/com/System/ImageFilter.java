package com.System;

import javax.swing.filechooser.FileFilter;
import java.io.File;

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
            if (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg")) {
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