package org.sarrygeez;

import java.awt.*;

/**
 * Abstract class representing a listener for color picker events.
 * This class provides several callback methods that can be overridden
 * to respond to different user actions in a color picker component,
 * such as color changes, history updates, transparency adjustments, and
 * mouse interactions with preset or history colors.
 */
@SuppressWarnings("unused")
public abstract class ColorPickerListener {

    /**
     * Invoked whenever the selected color from the color chooser model changes.
     * This method should handle actions or UI updates that need to occur
     * when the user selects a new color.
     */
    public void onColorChanged(Color newColor) {
        // Override to respond to color change
    }

    /**
     * Invoked whenever a new color is added to the color history.
     * This can be used to update the color history UI or track
     * changes in the user's color selections.
     */
    public void onColorAdded(Color newColor) {
        // Override to respond to a color being added to the history
    }

    /**
     * Invoked whenever the transparency (alpha value) of the selected color
     * changes in the color chooser model.
     * This can be used to update the UI or any components that depend on
     * the selected color's transparency.
     */
    public void onTransparencyChanged(int alpha) {
        // Override to respond to transparency changes
    }

    /**
     * Invoked when the mouse is clicked on one of the preset colors or a color
     * from the history.
     * This method can handle the selection or application of previously
     * used or predefined colors.
     */
    public void onColorClicked(Color colorSelected) {
        // Override to handle clicks on preset or history colors
    }
}

// Don't hate me about the javaDocs, I'm too lazy to write one myself.
// Don't worry tho, I write the java Doc first and let Chad-GPT enhance my grammar then on.
