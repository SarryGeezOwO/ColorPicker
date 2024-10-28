package org.sarrygeez.Widget;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class ColorSelector extends JColorChooser {

    private AbstractColorChooserPanel hsvPanel;

    public ColorSelector() {

        // Remove the default Preview panel
        setPreviewPanel(new JPanel());
        setColor(Color.WHITE);

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
