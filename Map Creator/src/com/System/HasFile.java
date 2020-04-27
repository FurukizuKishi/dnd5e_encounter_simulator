package com.System;

import com.GUI.GUI;
import com.System.FileFilters.ImageFilter;
import com.System.FileFilters.MapFilter;
import com.globals;

import javax.swing.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.nio.file.Paths;

public class HasFile {
    String defaultFilePath;
    String defaultFileFolder;
    String file;
    GUI frame;
    public HasFile(GUI frame) {
        this.frame = frame;
    }

    public String selectMap(Enums.fileAction action) {
        return selectFile(globals.defaultFilePath, new MapFilter(action));
    }
    public String selectMap(String file, Enums.fileAction action) {
        return selectFile(file, new MapFilter(action));
    }
    public String selectImage() {
        return selectFile(globals.defaultFilePath, new ImageFilter());
    }
    public String selectImage(String file) {
        return selectFile(file, new ImageFilter());
    }
    public String selectFile(String file, FileFilter filter) {
        defaultFilePath = file;
        defaultFileFolder = Paths.get(file).toAbsolutePath().toString();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(defaultFileFolder));
        frame.add(fileChooser);
        System.out.println(defaultFileFolder);

        //Show it.
        int returnVal = fileChooser.showDialog(frame, "Open");
        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String selectedFile = fileChooser.getSelectedFile().getPath();
            int lastSeparator = selectedFile.lastIndexOf("\\");
            if (selectedFile.substring(0, lastSeparator).equals(defaultFileFolder)) {
                this.file = selectedFile.substring(lastSeparator + 1);
            }
            return selectedFile;
        }
        return null;
    }
}
