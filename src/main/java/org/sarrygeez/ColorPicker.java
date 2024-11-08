package org.sarrygeez;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.Widget.ColorSelector;
import org.sarrygeez.Widget.RoundedPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The ColorPicker class extends RoundedPanel and provides a graphical user interface
 * component for selecting and modifying colors. It includes input fields for color
 * components <code style="color:yellow;">(Hex, Red, Green, Blue, Alpha)</code>, a color preview panel, and a color
 * selector. The class supports various constructors for different corner radius
 * configurations and allows customization of text field appearance. It also manages
 * listeners for color picker events, enabling responses to color changes and
 * transparency adjustments.
 */
public class ColorPicker extends RoundedPanel {

    private final JTextField[] inputFields = new JTextField[5];
    // 0 = Hex code
    // 1 = Red
    // 2 = Green
    // 3 = Blue
    // 4 = Alpha / Transparency / Opacity

    private final JLabel hexLabel = new JLabel("Hex");
    private final JLabel redLabel = new JLabel("R");
    private final JLabel greenLabel = new JLabel("G");
    private final JLabel blueLabel = new JLabel("B");

    private final JSlider alphaSlider = new JSlider();
    private final RoundedPanel previewPanel = new RoundedPanel(5);

    private final ColorSelector chooser = new ColorSelector();
    private final List<ColorPickerListener> listeners = new ArrayList<>();

    // Constructor variations
    @SuppressWarnings("unused")
    public ColorPicker() {
        initComponent();
    }

    @SuppressWarnings("unused")
    public ColorPicker(int radius) {
        super(radius);
        initComponent();
    }

    @SuppressWarnings("unused")
    public ColorPicker(int topLeft, int topRight, int bottomRight, int bottomLeft) {
        super(topLeft, topRight, bottomRight, bottomLeft);
        initComponent();
    }


    private void initComponent() {
        initInputFields();
        styleComponent();
        initListeners();
        propertyChanged(hexLabel, redLabel, greenLabel, blueLabel);
        addComponents();
        chooser.setColor(Color.WHITE);
    }

    private void initInputFields() {
        // By default, all input fields have 5 width, except for alpha
        for(int i = 0; i < inputFields.length-1; i++) {
            inputFields[i] = new JTextField(5);
        }
        // alpha field is the only one with 3 width
        inputFields[4] = new JTextField(3);
    }

    private void styleComponent() {
        setPreferredSize(new Dimension(250, 400));
        setLayout(new MigLayout("fillX, insets 15, gap 5"));
        setBorderColor(Color.BLACK);
        setBorderWidth(1);
        setHasBorder(true);

        previewPanel.setBorderWidth(1);
        previewPanel.setHasBorder(true);
        previewPanel.setBorderColor(getDarkerColor(getBackground(), 0.5f));

        inputFields[4].setText("255");

        alphaSlider.setMaximum(255);
        alphaSlider.setMinimum(0);
        alphaSlider.setValue(255);
        alphaSlider.setDoubleBuffered(true);

        chooser.setColor(Color.BLACK);
    }

    private void addComponents() {
        add(chooser, "gapLeft 10, grow, span, wrap");

        add(previewPanel, "grow, span, wrap, height 40");
        add(alphaSlider, "grow, span, wrap");

        add(hexLabel, "grow,span");
        add(inputFields[0], "grow, span 2");
        add(inputFields[4], "wrap, grow");

        add(redLabel , "grow");
        add(greenLabel , "grow");
        add(blueLabel, "grow, wrap");

        add(inputFields[1], "grow");
        add(inputFields[2], "grow");
        add(inputFields[3], "grow");
    }

    private void initListeners() {
        alphaSlider.addChangeListener(e -> {
            Color col = previewPanel.getBackground();
            previewPanel.setBackground(new Color(
                    col.getRed(), col.getGreen(), col.getBlue(),
                    alphaSlider.getValue()
            ));

            // notify the alpha text field for updates
            inputFields[4].setText(String.valueOf(alphaSlider.getValue()));
            for(ColorPickerListener listener : listeners) {
                listener.onTransparencyChanged(alphaSlider.getValue());
            }
        });

        inputFields[0].addActionListener(e -> {
            Color color = tryGetColor(inputFields[0]);
            chooser.getSelectionModel().setSelectedColor(color);
        });

        inputFields[4].addActionListener(e -> {
            Color color = modifyColor(chooser.getColor(), "Alpha", inputFields[4].getText());
            chooser.getSelectionModel().setSelectedColor(color);

            // Notify slider for new updates
            int alpha = color.getAlpha();
            alphaSlider.setValue(alpha);
        });

        inputFields[1].addActionListener(e -> {
            Color color = modifyColor(chooser.getColor(), "Red", inputFields[1].getText());
            chooser.getSelectionModel().setSelectedColor(color);
        });

        inputFields[2].addActionListener(e -> {
            Color color = modifyColor(chooser.getColor(), "Green", inputFields[2].getText());
            chooser.getSelectionModel().setSelectedColor(color);
        });

        inputFields[3].addActionListener(e -> {
            Color color = modifyColor(chooser.getColor(), "Blue", inputFields[3].getText());
            chooser.getSelectionModel().setSelectedColor(color);
        });

        chooser.getSelectionModel().addChangeListener(evt -> {
            Color newColor = chooser.getColor();
            previewPanel.setBackground(newColor);
            inputFields[1].setText(String.valueOf(newColor.getRed()));
            inputFields[2].setText(String.valueOf(newColor.getGreen()));
            inputFields[3].setText(String.valueOf(newColor.getBlue()));

            inputFields[0].setText(String.format("#%06X", newColor.getRGB() & 0xFFFFFF));
            for(ColorPickerListener listener : listeners) {
                listener.onColorChanged(newColor);
            }

        });
    }

    private void propertyChanged(JLabel ... labels) {
        // Change Chooser panel background when this background also changes
        addPropertyChangeListener(evt -> {
            chooser.setBackground(getBackground());
            alphaSlider.setBackground(getBackground());

            for(JLabel l : labels) {
                l.setForeground(getForeground());
            }
        });
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

    /**
     * Adds a ColorPickerListener to the list of listeners.
     * The listener will be notified of color picker events such as
     * color changes, transparency adjustments, and color selections.
     *
     * @param listener the ColorPickerListener to be added.
     */
    @SuppressWarnings("unused")
    public void addColorPickerListener(ColorPickerListener listener) {
        listeners.add(listener);
    }


    // ==================================================================================== //
    // ================================== Setters ========================================= //
    // ==================================================================================== //

    /**
     * Sets the font for all JTextFields in the ColorPicker.
     *
     * @param font the Font to set for each JTextField.
     */
    @SuppressWarnings("unused")
    public void setTextFieldFont(Font font) {
        for(JTextField field : inputFields) {
            field.setFont(font);
        }
    }

    /**
     * Sets the border for all JTextFields in the ColorPicker.
     *
     * @param border the Border to set for each JTextField.
     */
    @SuppressWarnings("unused")
    public void setTextFieldBorder(Border border) {
        for(JTextField field : inputFields) {
            field.setBorder(border);
        }
    }

    /**
     * Sets the foreground color for all JTextFields in the ColorPicker.
     *
     * @param foregroundColor the Color to set as the foreground for each JTextField.
     */
    @SuppressWarnings("unused")
    public void setTextFieldForeground(Color foregroundColor) {
        for(JTextField field : inputFields) {
            field.setForeground(foregroundColor);
        }
    }

    /**
     * Sets the background color for all JTextFields in the ColorPicker.
     *
     * @param backgroundColor the Color to set as the background for each JTextField.
     */
    @SuppressWarnings("unused")
    public void setTextFieldBackground(Color backgroundColor) {
        for(JTextField field : inputFields) {
            field.setBackground(backgroundColor);
        }
    }

    /**
     * Sets the selected color in the ColorSelector component.
     *
     * @param color the Color to be set in the ColorSelector.
     */
    @SuppressWarnings("unused")
    public void setColor(Color color) {
        chooser.setColor(color);
    }


    // ==================================================================================== //
    // =========================== Public static Functions ================================ //
    // ==================================================================================== //

    /**
     * Modifies the specified component of the given color based on the target and value.
     * If the value cannot be parsed as an integer, defaults to 255.
     *
     * @param originalColor the original Color to be modified.
     * @param target the color component to modify ("Red", "Green", "Blue", or "Alpha").
     * @param value the new value for the specified component as a String.
     * @return a new Color object with the modified component, or white if the target is invalid.
     */
    public static Color modifyColor(Color originalColor, String target, String value) {
        int newValue;
        try {
            newValue = Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            newValue = 255;
        }
        return modifyColor(originalColor, target, newValue);
    }

    /**
     * Modifies the specified component of the given color based on the target and value.
     * If the value cannot be parsed as an integer, defaults to 255.
     *
     * @param originalColor the original Color to be modified.
     * @param target the color component to modify ("Red", "Green", "Blue", or "Alpha").
     * @param newValue the new value for the specified component.
     * @return a new Color object with the modified component, or white if the target is invalid.
     */
    public static Color modifyColor(Color originalColor, String target, int newValue) {
        return switch (target) {
            case "Red" -> new Color(newValue, originalColor.getGreen(), originalColor.getBlue());
            case "Green" -> new Color(originalColor.getRed(), newValue, originalColor.getBlue());
            case "Blue" -> new Color(originalColor.getRed(), originalColor.getGreen(), newValue);
            case "Alpha" -> new Color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), newValue);
            default -> Color.WHITE;
        };
    }

    /**
     * Returns a darker shade of the specified color by applying a factor to its RGB components.
     *
     * @param originalColor the original Color to be darkened.
     * @param factor the factor by which to darken the color, where a value less than 1.0 results in a darker color.
     * @return a new Color object representing the darker shade of the original color.
     */
    public static Color getDarkerColor(Color originalColor, float factor) {
        // Create a new darker color
        int darkerRed = Math.max((int)(originalColor.getRed() * factor), 0);
        int darkerGreen = Math.max((int)(originalColor.getGreen() * factor), 0);
        int darkerBlue = Math.max((int)(originalColor.getBlue() * factor), 0);

        return new Color(darkerRed, darkerGreen, darkerBlue);
    }

    // ==================================================================================== //
    // ===================================== Getters ====================================== //
    // ==================================================================================== //


    /**
     * Retrieves the currently selected color from the ColorSelector.
     *
     * @return the current Color selected by the ColorSelector.
     */
    @SuppressWarnings("unused")
    public Color getColor() {
        return chooser.getColor();
    }

    /**
     * Calculates and returns the alpha transparency of the currently selected color
     * as a percentage. The alpha value is normalized from a range of 0-255 to 0-100.
     *
     * @return the alpha transparency percentage of the selected color.
     */
    @SuppressWarnings("unused")
    public int getAlphaPercent() {
        return (int)((chooser.getColor().getAlpha() / 255.0) * 100);
    }

    /**
     * A function for retrieving all present JTextField inside a ColorPicker object,
     * has a length of 5 each index representing a JTextField for values
     * <p>0 = Hex</p>
     * <p>1 = Red</p>
     * <p>2 = Green</p>
     * <p>3 = Blue</p>
     * <p>4 = Alpha</p>
     * @return an array containing all JTextField present in this panel
     */
    @SuppressWarnings("unused")
    public JTextField[] getInputFields() {
        return inputFields;
    }

    @SuppressWarnings("unused")
    public JSlider getAlphaSlider() {
        return alphaSlider;
    }
}
