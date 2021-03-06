package com.Game.GUI.System;

import com.Game.main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Fonts {
    public static HashMap<font, Font> fonts = new HashMap<>();                      //All of the fonts currently installed in the program.
    public enum font {                                                              //Types of program font.
        TITLE, SUBTITLE, TEXT, DAMAGE
    }

    //Add a new font to the database, taking a font file from the fonts folder and assigning it a font type.
    public static boolean addFont(String filepath, font key, int size) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            InputStream fileInput = main.class.getResource("/com/Game/resources/fonts/" + filepath).openStream();
            Font font = Font.createFont(Font.TRUETYPE_FONT, fileInput).deriveFont((float) size);
            fonts.put(key, font);
            ge.registerFont(font);
            //System.out.println("[FNT]: " + font);
            return true;
        } catch (IOException e) {
            //System.out.println("[DBG]: File not found.");
            return false;
        } catch (FontFormatException e) {
            //System.out.println("[DBG]: Incorrect font format.");
            return false;
        }
    }
}
