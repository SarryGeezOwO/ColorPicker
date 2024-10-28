package org.sarrygeez;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ColorPickerDemo extends JFrame {

    public ColorPickerDemo() {
        initFrame();

        ColorPicker picker = new ColorPicker(15);
        picker.setTextFieldBorder(new EmptyBorder(5, 10, 5, 10));
        picker.addColorPickerListener(new ColorPickerListener() {
            @Override
            public void onColorChanged(Color newColor) {
                System.out.println("New Color: " + newColor);
            }
        });
        add(picker, "center");
        setVisible(true);
    }

    private void initFrame() {
        setTitle("Color picker demo");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("Fill, insets 20"));
    }

    public static void main(String[] args) {
        new ColorPickerDemo();
    }
}