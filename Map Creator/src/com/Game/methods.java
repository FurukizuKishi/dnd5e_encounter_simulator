package com.Game;

import com.Game.GUI.System.Fonts;
import com.Game.System.Enums;
import com.Game.System.Text.FloatingText;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class methods {
    //Print multiples of variables inside brackets.
    public static String encapsulatedTuple(String beginChar, String endChar, String limit, Object ... str) {
        String tuple = beginChar + determineObjectPrintout(str[0], limit, true);
        for (int i = 1; i < str.length; i += 1) {
            if (str[i] != null) {
                tuple += determineObjectPrintout(str[i], limit, false);
            } else {
                tuple += ", " + str[i];
            }
        }
        tuple += endChar;
        return tuple;
    }

    public static String determineObjectPrintout(Object obj, String limit, boolean start) {
        if (start) {
            limit = "";
        }
        if (obj instanceof String) {
            return limit + "\"" + obj + "\"";
        } else if (obj instanceof Collection) {
            return limit + encapsulatedTuple("<", ">", limit, (Collection) obj);
        } else if (obj.getClass().isArray()) {
            return limit + encapsulatedTuple("[", "]", limit, (Object[]) obj);
        } else {
            return limit + obj;
        }
    }

    public static String limitedTuple(String limit, Object ... str) {
        return encapsulatedTuple("", "", limit, str);
    }
    public static String tuple(Object ... str) {
        return encapsulatedTuple("(", ")", ", ", str);
    }

    //Check if the message is an action flag.
    public static boolean messageIsFlag(String message) {
        if (message.contains(":")) {
            if (message.startsWith("ROLL") || message.startsWith("MOVE") || message.startsWith("CHAR")) {
                return true;
            }
            String[] flags = { "ROLL", "MOVE", "CHAR" };
            if (arrayToList(flags).contains(message.split(":")[1])) {
                return true;
            }
        }
        return false;
    }

    //Check whether a number is positive, negative or zero.
    public static int sign(double n) {
        if (n > 0) {
            return 1;
        }
        else if (n < 0) {
            return -1;
        }
        return 0;
    }

    //Check if a coordinate is in a list.
    public static int containsCoordinate(ArrayList<int[]> list, int x, int y) {
        int i = 0;
        for (int[] coord : list) {
            if (Arrays.equals(coord, new int[] {x, y})) {
                return i;
            }
            i += 1;
        }
        return -1;
    }

    //Check if you would be stepping backwards by adding a coordinate to a path.
    public static boolean steppingBackwards(ArrayList<int[]> path, int x, int y) {
        if (path.size() > 0) {
            if (methods.containsCoordinate(path, x, y) == path.size() - 1) {
                return true;
            }
            return false;
        }
        return false;
    }

    //Fill a circle on a map with a particular value.
    public static int[] createCircle(int[][] map, int value, int x, int y, int circleRadius) {
        for (int my = 0; my < map.length; my += 1) {
            for (int mx = 0; mx < map[my].length; mx += 1) {
                if (methods.distance(mx, my, x, y) <= circleRadius) {
                    map[my][mx] = value;
                }
            }
        }
        return new int[] {x, y, circleRadius};
    }
    public static int[] createCircle(boolean[][] map, boolean value, int x, int y, int circleRadius) {
        for (int my = 0; my < map.length; my += 1) {
            for (int mx = 0; mx < map[my].length; mx += 1) {
                if (methods.distance(mx, my, x, y) <= circleRadius) {
                    map[my][mx] = value;
                }
            }
        }
        return new int[] {x, y, circleRadius};
    }

    //Divide integers without truncation.
    public static double doubleDivision(int dividend, int divisor) {
        return (double) dividend / (double) divisor;
    }
    public static int integerDivision(int dividend, int divisor) {
        return integerDivision(dividend, divisor, 1);
    }
    public static int integerDivision(int dividend, int divisor, int multiplier) {
        return (int) (doubleDivision(dividend, divisor) * multiplier);
    }

    //Obtain a random number in a range of values.
    public static double randomRange(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }
    public static int randomRange(int min, int max) {
        return (int) Math.round(Math.random() * (max - min)) + min;
    }

    //Check distance between two points.
    public static double distance(double thisX, double thisY, double otherX, double otherY) {
        return Math.sqrt(Math.pow((otherX - thisX), 2) + Math.pow((otherY - thisY), 2));
    }

    //Compress and stretch a sine wave to transform it.
    public static double transformSine(double theta, double squeezeX, double squeezeY, double delay) {
        return squeezeY * Math.sin((theta * squeezeX) - (delay / Math.PI));
    }

    //Check if two circles intersect.
    public static boolean circlesIntersect(int x1, int y1, int r1, int x2, int y2, int r2) {
        double dist = methods.distance(x1, y1, x2, y2);
        double radSum = r1 + r2;
        if ((Math.pow(dist, 2) - Math.pow(radSum, 2)) < 0) {
            return true;
        }
        return true;
    }

    //Code sourced from stack overflow user Klark - https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
    public static BufferedImage imageDeepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    //Draw a styled string onscreen.
    public static void drawString(Graphics g, String str, int x, int y, Fonts.font font, Color colour) {
        drawString(g, str, x, y, font, colour, 0);
    }
    public static void drawString(Graphics g, String str, int x, int y, Fonts.font font, Color colour, int alignment) {
        Enums.alignmentHorizontal he; Enums.alignmentVertical ve;
        switch (alignment) {
            case 0: he = Enums.alignmentHorizontal.LEFT; ve = Enums.alignmentVertical.TOP; break;
            case 1: he = Enums.alignmentHorizontal.MIDDLE; ve = Enums.alignmentVertical.TOP; break;
            case 2: he = Enums.alignmentHorizontal.RIGHT; ve = Enums.alignmentVertical.TOP; break;
            case 3: he = Enums.alignmentHorizontal.LEFT; ve = Enums.alignmentVertical.MIDDLE; break;
            case 4: he = Enums.alignmentHorizontal.MIDDLE; ve = Enums.alignmentVertical.MIDDLE; break;
            case 5: he = Enums.alignmentHorizontal.RIGHT; ve = Enums.alignmentVertical.MIDDLE; break;
            case 6: he = Enums.alignmentHorizontal.LEFT; ve = Enums.alignmentVertical.BOTTOM; break;
            case 7: he = Enums.alignmentHorizontal.MIDDLE; ve = Enums.alignmentVertical.BOTTOM; break;
            case 8: he = Enums.alignmentHorizontal.RIGHT; ve = Enums.alignmentVertical.BOTTOM; break;
            default: he = Enums.alignmentHorizontal.MIDDLE; ve = Enums.alignmentVertical.MIDDLE; break;
        }
        int h = methods.getNewHorizontalAlignment(g, str, he, Fonts.fonts.get(font));
        int v = methods.getNewVerticalAlignment(g, str, ve, Fonts.fonts.get(font));
        //System.out.println(methods.tuple(alignment, he, ve, h, v));
        g.setColor(colour);
        g.setFont(Fonts.fonts.get(font));
        g.drawString(str, x - h, y - v);
    }

    //Align text using its width in pixels. Justifications include LEFT, MIDDLE and RIGHT.
    public static void alignFloatingText(Graphics g, FloatingText text, Enums.alignmentHorizontal justification) {
        text.alignmentOffset = getNewHorizontalAlignment(g, text.text, justification);
    }
    public static int getNewHorizontalAlignment(Graphics g, String str, Enums.alignmentHorizontal justification) {
        return getNewHorizontalAlignment(g, str, justification, g.getFont());
    }
    public static int getNewHorizontalTextAlignment(Graphics g, String str, Enums.alignmentHorizontal justification, Fonts.font font) {
        return getNewHorizontalAlignment(g, str, justification, Fonts.fonts.get(font));
    }
    public static int getNewHorizontalAlignment(Graphics g, String str, Enums.alignmentHorizontal justification, Font font) {
        FontMetrics fm = g.getFontMetrics(font);
        int fontWidth = fm.stringWidth(str);
        if (justification == Enums.alignmentHorizontal.LEFT) {
            return 0;
        }
        else if (justification == Enums.alignmentHorizontal.MIDDLE) {
            return fontWidth / 2;
        }
        else if (justification == Enums.alignmentHorizontal.RIGHT) {
            return fontWidth;
        }
        return 0;
    }
    //Align text using its width in pixels. Justifications include LEFT, MIDDLE and RIGHT.
    public static int getNewVerticalAlignment(Graphics g, String str, Enums.alignmentVertical justification) {
        return getNewVerticalAlignment(g, str, justification, g.getFont());
    }
    public static int getNewVerticalAlignment(Graphics g, String str, Enums.alignmentVertical justification, Fonts.font font) {
        return getNewVerticalAlignment(g, str, justification, Fonts.fonts.get(font));
    }
    public static int getNewVerticalAlignment(Graphics g, String str, Enums.alignmentVertical justification, Font font) {
        FontMetrics fm = g.getFontMetrics(font);
        int fontHeight = (int) fm.getStringBounds(str, g).getHeight();
        if (justification == Enums.alignmentVertical.TOP) {
            return 0;
        }
        else if (justification == Enums.alignmentVertical.MIDDLE) {
            return fontHeight / 2;
        }
        else if (justification == Enums.alignmentVertical.BOTTOM) {
            return fontHeight;
        }
        return 0;
    }

    //Draw a textbox onscreen.
    public static void drawContainerBox(Graphics g, int x, int y, int w, int h) {
        drawContainerBox(g, x, y, w, h, new Color(0, 0, 0, 105));
    }
    public static void drawContainerBox(Graphics g, int x, int y, int w, int h, Color backColour) {
        g.setColor(Color.WHITE);
        g.drawRect(x - 2, y - 2, w + 4, h + 4);
        g.drawRect(x - 3, y - 3, w + 6, h + 6);
        g.drawRect(x - 5, y - 5, w + 10, h + 10);
        g.setColor(backColour);
        g.fillRect(x, y, w + 2, h + 2);
    }

    //Change an image to a solid colour.
    public static BufferedImage tintImage(BufferedImage sprite, Color colour) {
        for (int sy = 0; sy < sprite.getHeight(); sy += 1) {
            for (int sx = 0; sx < sprite.getWidth(); sx += 1) {
                if ((sprite.getRGB(sx, sy) >> 24) != 0x00) {
                    sprite.setRGB(sx, sy, colour.getRGB());
                }
            }
        }
        return sprite;
    }

    //Code sourced from stack overflow users tommo and alfonx - https://stackoverflow.com/questions/11552092/changing-image-opacity
    public static Graphics fadeImage(Graphics2D g, double opacity) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (opacity / 255)));
        return g;
    }

    //Obtain a sprite from a path.
    public static BufferedImage getExternalImage(String spritePath) {
        File file = new File(spritePath);
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            //System.out.println(e);
            return null;
        }
    }

    //Obtain a sprite from a path.
    public static BufferedImage getImage(String spritePath) {
        try {
            URL fileInput = main.class.getResource("/com/Game/resources/" + spritePath + ".png");
            if (fileInput == null) {
                return null;
            }
            return ImageIO.read(fileInput.openStream());
        } catch (IOException e) {
            return null;
        }
    }

    //Code sourced from stack overflow users Suken Shah and Mr. Polywhirl - https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
    public static BufferedImage getScaledImage(BufferedImage srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    //Cast a primitive array to a list.
    public static <T> ArrayList<T> arrayToList(T[] array) {
        ArrayList<T> list = new ArrayList<>();
        for (T element : array) {
            list.add(element);
        }
        return list;
    }
    //Combine a list into a string with a delimiter.
    public static String concencateList(Object[] list, String limit) {
        if (list.length > 0) {
            String string = (String) list[0];
            for (int i = 1; i < list.length; i += 1) {
                string += limit + list[i];
            }
            //System.out.println(string);
            return string;
        }
        return "";
    }

    //Change an object list into a string list.
    public static String[] changeListTypeToString(Object[] list) {
        String[] stringList = new String[list.length];
        for (int i = 0; i < list.length; i += 1) {
            stringList[i] = "" + list[i];
        }
        return stringList;
    }

    //Determine the direction of an arrow.
    public static int[] checkArrowDirection(char arrow) {
        int[] dir = {0, 0};
        switch (arrow) {
            case '^': dir[1] = -1; break;
            case 'v': dir[1] = 1; break;
            case '<': dir[0] = -1; break;
            case '>': dir[0] = 1; break;
        }
        return dir;
    }

    //Create the dimensions for a drawn value bar.
    public static int[] createValueBarDimensions(int barX, int barY, int barWidth, int barHeight, int border) {
        int fillX = barX + border;
        int fillY = barY + border;
        int fillWidth = barWidth - (border * 2);
        int fillHeight = barHeight - (border * 2);
        return new int[] {fillX, fillY, fillWidth, fillHeight};
    }

    //Create a value bar with a rounded-rectangle shape.
    public static void paintValueBarRound(Graphics g, int value, int maxValue, Color barColour, Color backColour, int barX, int barY, int barWidth, int barHeight) {
        paintValueBarRound(g, value, maxValue, barColour, backColour, null, barX, barY, barWidth, barHeight, 0);
    }
    public static void paintValueBarRound(Graphics g, int value, int maxValue, Color barColour, Color backColour, Color borderColour, int barX, int barY, int barWidth, int barHeight, int border) {
        int[] fill = createValueBarDimensions(barX, barY, barWidth, barHeight, border);
        if (borderColour != null) {
            g.setColor(borderColour);
            g.fillRoundRect(barX, barY, barWidth, barHeight, barHeight, barHeight);
        }
        if (backColour != null) {
            g.setColor(backColour);
            g.fillRoundRect(fill[0], fill[1], fill[2], fill[3], fill[3], fill[3]);
        }
        g.setColor(barColour);
        g.fillRoundRect(fill[0], fill[1], methods.integerDivision(value, maxValue, fill[2]), fill[3], fill[3], fill[3]);
    }

    //Create a rectangular value bar.
    public static void paintValueBarSquare(Graphics g, int value, int maxValue, Color barColour, Color backColour, int barX, int barY, int barWidth, int barHeight) {
        paintValueBarSquare(g, value, maxValue, barColour, backColour, null, barX, barY, barWidth, barHeight, 0);
    }
    public static void paintValueBarSquare(Graphics g, int value, int maxValue, Color barColour, Color backColour, Color borderColour, int barX, int barY, int barWidth, int barHeight, int border) {
        int[] fill = createValueBarDimensions(barX, barY, barWidth, barHeight, border);
        if (borderColour != null) {
            g.setColor(borderColour);
            g.fillRect(barX, barY, barWidth, barHeight);
        }
        if (backColour != null) {
            g.setColor(backColour);
            g.fillRect(fill[0], fill[1], fill[2], fill[3]);
        }
        g.setColor(barColour);
        g.fillRect(fill[0], fill[1], methods.integerDivision(value, maxValue, fill[2]), fill[3]);
    }
}