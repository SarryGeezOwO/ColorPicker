package org.sarrygeez.Widget;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel{

    private  int topLeft = 0;
    private  int topRight = 0;
    private  int bottomRight = 0;
    private  int bottomLeft = 0;

    private  boolean hasBorder = false;
    private  int borderThickness = 0;
    private  Color borderColor = Color.BLACK;

    @SuppressWarnings("unused")
    public RoundedPanel() {
        setOpaque(false);
        setBorder(null);
    }

    @SuppressWarnings("unused")
    public RoundedPanel(int radius) {
        this.topLeft = radius;
        this.topRight = radius;
        this.bottomRight = radius;
        this.bottomLeft = radius;
        setOpaque(false);
        setBorder(null);
    }

    @SuppressWarnings("unused")
    public RoundedPanel(int topLeft, int topRight, int bottomRight, int bottomLeft) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
        setOpaque(false);
        setBorder(null);
    }

    @SuppressWarnings("unused")
    public boolean isHasBorder() {
        return hasBorder;
    }

    @SuppressWarnings("unused")
    public void setHasBorder(boolean hasBorder) {
        if(!hasBorder) {
            setBorderWidth(0);
        }

        this.hasBorder = hasBorder;
        repaint();
    }

    @SuppressWarnings("unused")
    public int getBorderWidth() {
        return borderThickness;
    }

    @SuppressWarnings("unused")
    public void setBorderWidth(int borderWidth) {
        this.borderThickness = borderWidth;
        repaint();
    }

    @SuppressWarnings("unused")
    public Color getBorderColor() {
        return borderColor;
    }

    @SuppressWarnings("unused")
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawPanel(g);
        super.paintComponent(g);
    }

    private void drawPanel(Graphics g) {
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Area area = new Area(createRoundTopLeft());
        if(topRight > 0) {
            area.intersect(new Area(createRoundTopRight()));
        }
        if(bottomRight > 0) {
            area.intersect(new Area(createRoundBottomRight()));
        }
        if(bottomLeft > 0) {
            area.intersect(new Area(createRoundBottomLeft()));
        }

        g2D.setColor(getBackground());
        g2D.fill(area);

        if(hasBorder) {
            g2D.setColor(borderColor);
            g2D.setStroke(new BasicStroke(borderThickness));
            g2D.draw(area);
        }
        g2D.dispose();
    }

    private Shape createRoundTopLeft() {
        int width = getWidth() - borderThickness;
        int height = getHeight() - borderThickness;
        int roundX = Math.min(width, topLeft);
        int roundY = Math.min(height, topLeft);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
        area.add(new Area(new Rectangle2D.Double((double) roundX /2, 0, width - (double) roundX / 2, height)));
        area.add(new Area(new Rectangle2D.Double(0, (double) roundY / 2, width, height - (double) roundY / 2)));
        return area;
    }

    private Shape createRoundTopRight() {
        int width = getWidth() - borderThickness;
        int height = getHeight() - borderThickness;
        int roundX = Math.min(width, topRight);
        int roundY = Math.min(height, topRight);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
        area.add(new Area(new Rectangle2D.Double(0, 0, width - (double) roundX / 2, height)));
        area.add(new Area(new Rectangle2D.Double(0, (double) roundY / 2, width, height - (double) roundY / 2)));
        return area;
    }

    private Shape createRoundBottomLeft() {
        int width = getWidth() - borderThickness;
        int height = getHeight() - borderThickness;
        int roundX = Math.min(width, bottomLeft);
        int roundY = Math.min(height, bottomLeft);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
        area.add(new Area(new Rectangle2D.Double((double) roundX / 2, 0, width - (double) roundX / 2, height)));
        area.add(new Area(new Rectangle2D.Double(0, 0, width, height - (double) roundY / 2)));
        return area;
    }

    private Shape createRoundBottomRight() {
        int width = getWidth() - borderThickness;
        int height = getHeight() - borderThickness;
        int roundX = Math.min(width, bottomRight);
        int roundY = Math.min(height, bottomRight);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
        area.add(new Area(new Rectangle2D.Double(0, 0, width - (double) roundX / 2, height)));
        area.add(new Area(new Rectangle2D.Double(0, 0, width, height - (double) roundY / 2)));
        return area;
    }
}