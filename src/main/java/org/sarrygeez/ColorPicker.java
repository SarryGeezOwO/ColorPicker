package org.sarrygeez;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.Widget.ColorSelector;
import org.sarrygeez.Widget.RoundedPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorPicker extends RoundedPanel {

    private final JTextField hexField = new JTextField(5);
    private final JTextField redField = new JTextField(5);
    private final JTextField greenField = new JTextField(5);
    private final JTextField blueField = new JTextField(5);
    private final JTextField alphaField = new JTextField(3);

    private final JSlider alphaSlider = new JSlider();
    private final RoundedPanel previewPanel = new RoundedPanel(5);

    private final ColorSelector chooser = new ColorSelector();
    private final List<ColorPickerListener> listeners = new ArrayList<>();

    // Constructor variations
    @SuppressWarnings("unused")
    public ColorPicker() {
        styleComponent();
    }

    @SuppressWarnings("unused")
    public ColorPicker(int radius) {
        super(radius);
        styleComponent();
    }

    @SuppressWarnings("unused")
    public ColorPicker(int topLeft, int topRight, int bottomRight, int bottomLeft) {
        super(topLeft, topRight, bottomRight, bottomLeft);
        styleComponent();
    }




    private void styleComponent() {
        alphaField.setText("255");

        previewPanel.setBorderWidth(1);
        previewPanel.setHasBorder(true);
        previewPanel.setBorderColor(getDarkerColor(getBackground(), 0.5f));

        alphaSlider.setMaximum(255);
        alphaSlider.setMinimum(0);
        alphaSlider.setValue(255);
        alphaSlider.setPaintLabels(true);
        alphaSlider.setDoubleBuffered(true);

        alphaSlider.addChangeListener(e -> {
            Color col = previewPanel.getBackground();
            previewPanel.setBackground(new Color(
                col.getRed(), col.getGreen(), col.getBlue(),
                alphaSlider.getValue()
            ));

            alphaField.setText(String.valueOf(alphaSlider.getValue()));
            for(ColorPickerListener listener : listeners) {
                listener.onTransparencyChanged(alphaSlider.getValue());
            }
        });

        hexField.addActionListener(e -> {
            Color color = tryGetColor(hexField);
            chooser.getSelectionModel().setSelectedColor(color);
        });

        alphaField.addActionListener(e -> {
            Color color = toColor(chooser.getColor(), "Alpha", alphaField.getText());
            chooser.getSelectionModel().setSelectedColor(color);

            int alpha = color.getAlpha();
            alphaSlider.setValue(alpha);
        });

        redField.addActionListener(e -> {
            Color color = toColor(chooser.getColor(), "Red", redField.getText());
            chooser.getSelectionModel().setSelectedColor(color);
        });

        greenField.addActionListener(e -> {
            Color color = toColor(chooser.getColor(), "Green", greenField.getText());
            chooser.getSelectionModel().setSelectedColor(color);
        });

        blueField.addActionListener(e -> {
            Color color = toColor(chooser.getColor(), "Blue", blueField.getText());
            chooser.getSelectionModel().setSelectedColor(color);
        });

        chooser.getSelectionModel().addChangeListener(evt -> {
            Color newColor = chooser.getColor();
            previewPanel.setBackground(newColor);
            redField.setText(String.valueOf(newColor.getRed()));
            greenField.setText(String.valueOf(newColor.getGreen()));
            blueField.setText(String.valueOf(newColor.getBlue()));

            hexField.setText(String.format("#%06X", newColor.getRGB() & 0xFFFFFF));
            for(ColorPickerListener listener : listeners) {
                listener.onColorChanged(newColor);
            }

        });

        // A label...
        JLabel hexLabel = new JLabel("Hex");
        JLabel redLabel = new JLabel("R");
        JLabel greenLabel = new JLabel("G");
        JLabel blueLabel = new JLabel("B");
        propertyChanged(hexLabel, redLabel, greenLabel, blueLabel);

        setPreferredSize(new Dimension(250, 400));
        setLayout(new MigLayout("fillX, insets 15, gap 5"));
        setBorderColor(Color.BLACK);
        setBorderWidth(1);
        setHasBorder(true);

        add(chooser, "gapLeft 10, grow, span, wrap");

        add(previewPanel, "grow, span, wrap, height 40");
        add(alphaSlider, "grow, span, wrap");

        add(hexLabel, "grow,span");
        add(hexField, "grow, span 2");
        add(alphaField, "wrap, grow");

        add(redLabel , "grow");
        add(greenLabel , "grow");
        add(blueLabel, "grow, wrap");

        add(redField, "grow");
        add(greenField, "grow");
        add(blueField, "grow");
        chooser.setColor(Color.BLACK);
    }

    private Color toColor(Color originalColor, String target, String str) {
        int newValue;
        try {
            newValue = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            newValue = 255;
        }

        return switch (target) {
            case "Red" -> new Color(newValue, originalColor.getGreen(), originalColor.getBlue());
            case "Green" -> new Color(originalColor.getRed(), newValue, originalColor.getBlue());
            case "Blue" -> new Color(originalColor.getRed(), originalColor.getGreen(), newValue);
            case "Alpha" -> new Color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), newValue);
            default -> Color.WHITE;
        };
    }

    private Color tryGetColor(JTextField field) {
        Color color;
        try {
            color = Color.decode(field.getText());
        }catch (NumberFormatException ex) {
            color = Color.WHITE;
        }
        return color;
    }


    private void propertyChanged(JLabel ... labels) {
        // Change Chooser panel background when this background also changes
        addPropertyChangeListener(evt -> {
            chooser.setBackground(getBackground());
            alphaSlider.setBackground(getBackground());

            for(JLabel l : labels) {
                l.setForeground(getForeground());
            }

            // Darken the background color of the container
            Color darkerColor = getDarkerColor(getBackground(), 0.8f);

            // Set the TextFields background to the darker color
            hexField.setBackground(darkerColor);
            redField.setBackground(darkerColor);
            greenField.setBackground(darkerColor);
            blueField.setBackground(darkerColor);
            alphaField.setBackground(darkerColor);

            hexField.setForeground(getForeground());
            redField.setForeground(getForeground());
            greenField.setForeground(getForeground());
            blueField.setForeground(getForeground());
            alphaField.setForeground(getForeground());
        });
    }

    private Color getDarkerColor(Color orginalColor, float factor) {
        // Create a new darker color
        int darkerRed = Math.max((int)(orginalColor.getRed() * factor), 0);
        int darkerGreen = Math.max((int)(orginalColor.getGreen() * factor), 0);
        int darkerBlue = Math.max((int)(orginalColor.getBlue() * factor), 0);

        return new Color(darkerRed, darkerGreen, darkerBlue);
    }

    @SuppressWarnings("unused")
    public void addColorPickerListener(ColorPickerListener listener) {
        listeners.add(listener);
    }

    @SuppressWarnings("unused")
    public void setTextFieldFont(Font font) {
        hexField.setFont(font);
        redField.setFont(font);
        greenField.setFont(font);
        blueField.setFont(font);
        alphaField.setFont(font);
    }

    @SuppressWarnings("unused")
    public void setTextFieldBorder(Border border) {
        hexField.setBorder(border);
        redField.setBorder(border);
        greenField.setBorder(border);
        blueField.setBorder(border);
        alphaField.setBorder(border);
    }


    @SuppressWarnings("unused")
    public void setColor(Color color) {
        chooser.setColor(color);
    }

    @SuppressWarnings("unused")
    public Color getColor() {
        return chooser.getColor();
    }

    @SuppressWarnings("unused")
    public JTextField getHexField() {
        return hexField;
    }

    @SuppressWarnings("unused")
    public JTextField getRedField() {
        return redField;
    }

    @SuppressWarnings("unused")
    public JTextField getGreenField() {
        return greenField;
    }

    @SuppressWarnings("unused")
    public JTextField getBlueField() {
        return blueField;
    }

    @SuppressWarnings("unused")
    public JSlider getAlphaSlider() {
        return alphaSlider;
    }
}
