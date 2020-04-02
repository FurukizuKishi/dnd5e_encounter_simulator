package com.System;

import com.GUI.GUI;
import com.methods;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;

public class HasImage {
    String defaultFilePath;
    String defaultFileFolder;
    String file;
    GUI frame;
    public HasImage(GUI frame) {
        this.frame = frame;
    }

    public String selectFile(String file) {
        defaultFilePath = file;
        defaultFileFolder = Paths.get("files/").toAbsolutePath().toString();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new ImageFilter());
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
