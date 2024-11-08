package org.sarrygeez.Widget;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

/**
 * A custom color selector that extends JColorChooser, focusing on the HSV panel.
 * <p>
 * This class customizes the JColorChooser by removing unnecessary panels and
 * components, retaining only the HSV panel. It synchronizes the background color
 * of the HSV panel with its container's background color. The default preview
 * panel is removed, and additional components such as sliders, radio buttons,
 * spinners, labels, and formatted text fields are recursively removed from the
 * HSV panel to streamline the interface.
 */
public class ColorSelector extends JColorChooser {

    private AbstractColorChooserPanel hsvPanel;

    /**
     * Constructs a ColorSelector object that customizes the JColorChooser
     * by removing unnecessary panels and styling the HSV panel.
     * The default preview panel is removed, and only the HSV panel is retained
     * after removing extra components. The background color of the HSV panel
     * is synchronized with the container's background color.
     */
    public ColorSelector() {

        // Remove the default Preview panel
        setPreviewPanel(new JPanel());

        // Modify each panel existed in the JColorChooser
        AbstractColorChooserPanel[] availablePanels = getChooserPanels();
        for(AbstractColorChooserPanel panel : availablePanels) {

            if(panel.getDisplayName().equals("HSV")) {
                hsvPanel = panel;

                // Style the HSV panel
                removeExtras(panel);
                continue;
            }

            // Remove panels that is not needed
            removeChooserPanel(panel);
        }

        // Change HSV background to its container background color
        addPropertyChangeListener(evt -> {
            hsvPanel.setBackground(getBackground());
            applyBackground(this);
        });
    }

    private void applyBackground(Container container) {
        for(Component comp : container.getComponents()) {
            comp.setBackground(getBackground());
        }
    }

    private void removeExtras(Container container) {
        for(Component comp : container.getComponents()) {
            if  (
                    comp instanceof JSlider ||
                    comp instanceof JRadioButton ||
                    comp instanceof JSpinner ||
                    comp instanceof JLabel ||
                    comp instanceof JFormattedTextField
            )
            {
                container.remove(comp);
            }
            else if (comp instanceof Container) {
                // Recursively remove all things not needed
                removeExtras((Container) comp);
            }
        }
    }
}
