package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JMenuBar;

/*
 * Credit to D180 on StackOverflow
 * https://stackoverflow.com/questions/15648030/change-background-and-text-color-of-jmenubar-and-jmenu-objects-inside-it
 */
public class DarkModeMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private Color bgColor=Color.WHITE;

    public void setColor(Color color) {
        bgColor=color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

    }
}
