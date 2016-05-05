package client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import model.STBInfo;

@SuppressWarnings("serial")
public class STBCellRenderer extends DefaultListCellRenderer {
	private static final Color[] COLORS = {
		Color.getHSBColor(32, 100, 38), 
		Color.CYAN,
		Color.LIGHT_GRAY 
	};
	
    @SuppressWarnings("rawtypes")
	@Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        STBInfo stb = (STBInfo) value;
        String labelText = "<html>STB n°" + stb.getId() + ": " + stb.getTitre()
        		+ "<br>Date: " + stb.getDate() + "<br>Version: " + stb.getVersion()
        		+ "<br>Description: " + stb.getDesc();
        setText(labelText);
        setBackground(COLORS[index % COLORS.length]);
        return this;
    }
}
