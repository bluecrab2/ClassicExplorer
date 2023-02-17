package ui;

import java.awt.Color;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class DarkModeScrollBarUI extends BasicScrollBarUI {
	@Override
    public void configureScrollBarColors()
    {

        LookAndFeel.installColors(scrollbar, "ScrollBar.background",
                                  "ScrollBar.foreground");
        thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
        thumbLightShadowColor = UIManager.getColor("ScrollBar.thumbShadow");
        thumbDarkShadowColor = UIManager.getColor("ScrollBar.thumbDarkShadow");
        if(Settings.darkMode) {
	        thumbColor = Color.DARK_GRAY;
	        trackColor = Color.BLACK;
        } else {
	        thumbColor = UIManager.getColor("ScrollBar.thumb");
	        trackColor = UIManager.getColor("ScrollBar.track");
        }
        trackHighlightColor = UIManager.getColor("ScrollBar.trackHighlight");
    }
}
