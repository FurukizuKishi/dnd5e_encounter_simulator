package com.Game;

import com.Game.GUI.Camera;
import com.Game.Map.Creator.EditorFrame;
import com.Game.Map.Creator.TilesetPanel;
import com.Game.Map.Map;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class swingMethods {
    public static JPanel createPanel(JFrame frame, int x, int y, int w, int h) {
        return createPanel(frame, null, x, y, w, h);
    }
    public static JPanel createPanel(JFrame frame, String title, int x, int y, int w, int h) {
        JPanel panel = alterPanel(new JPanel(), title, x, y, w, h);
        frame.add(panel);
        return panel;
    }
    public static JPanel alterPanel(JPanel panel, int x, int y, int w, int h) {
        return alterPanel(panel, null, x, y, w, h);
    }
    public static JPanel alterPanel(JPanel panel, String title, int x, int y, int w, int h) {
        panel.setLocation(x, y);
        panel.setLayout(null);
        if (title != null) {
            panel.setBorder(createBorder(title));
        }
        panel.setSize(w, h);
        panel.setVisible(true);
        return panel;
    }
    public static TilesetPanel createTilesetPanel(JFrame frame, String tilesheet, int x, int y, int w, int h, int ts) {
        TilesetPanel panel = new TilesetPanel((EditorFrame) frame, tilesheet);
        panel.tileset.tileSize = ts;
        panel.setOpaque(false);
        panel.setLocation(x, y);
        panel.setSize(w, h);
        panel.setVisible(true);
        return panel;
    }
    public static JComponent[] createLayeredPane(int x, int y, int w, int h, JComponent ... layers) {
        JLayeredPane pane = new JLayeredPane();
        pane.setLocation(x, y);
        pane.setSize(w, h);
        pane.setPreferredSize(pane.getSize());
        for (int i = 0; i < layers.length; i += 1) {
            pane.add(layers[i], new Integer(i + 1));
        }
        pane.setVisible(true);

        JScrollPane bar = createScrollpane(pane, x, y, w, h);
        bar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        bar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return new JComponent[] {pane, bar};
    }
    public static <T> JComponent[] createList(String title, int x, int y, int w, int h, int ch, T ... items) {
        JList<T> list = new JList<>(items);
        list.setDragEnabled(true);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellWidth(w);
        list.setFixedCellHeight(ch);
        list.setLocation(x, y);
        list.setSize(w, h);

        JScrollPane bar = createScrollpane(list, x, y, w, h);
        bar.setBorder(createBorder(title));
        bar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        bar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return new JComponent[] {list, bar};
    }
    public static JComponent[] createCamera(JFrame frame, String title, Map map, int x, int y, int w, int h) {
        Camera camera = new Camera(frame, map);
        frame.add(camera);
        camera.setVisible(true);

        JScrollPane bar = createScrollpane(camera, x, y, w, h);
        bar.setBorder(createBorder(title));
        bar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        bar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return new JComponent[] {camera, bar};
    }
    public static JScrollPane createScrollpane(JComponent view, int x, int y, int w, int h) {
        JScrollPane bar = new JScrollPane(view);
        bar.setLocation(x, y);
        bar.setSize(w, h);
        bar.setVisible(true);
        return bar;
    }
    public static JButton createButton(String title, int x, int y, int w, int h) {
        JButton button = new JButton(title);
        button.setLocation(x, y);
        button.setSize(w, h);
        button.setVisible(true);
        return button;
    }
    public static JTextField createTextField(String title, JPanel panel, int x, int y, int w, int h) {
        JLabel label = new JLabel(title);
        label.setSize(w, 16);
        label.setLocation(x, y - 16);
        panel.add(label);

        JTextField field = new JTextField();
        field.setLocation(x, y);
        field.setSize(w, h);
        field.setVisible(true);
        panel.add(field);

        return field;
    }
    public static JTextField createIntegerField(String title, JPanel panel, int x, int y, int w, int h) {
        JTextField field = createTextField(title, panel, x, y, w, h);
        field.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer port = Integer.parseInt(((JTextField) e.getSource()).getText()) - 32768;
                    short val = Short.parseShort(Integer.toString(port));
                }
                catch (NumberFormatException ex) {
                    ((JTextField) e.getSource()).setText("");
                }
            }
        });
        return field;
    }
    public static CompoundBorder createBorder(String title) {
        return BorderFactory.createCompoundBorder(new TitledBorder(title), new BevelBorder(1));
    }
}
