package client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import model.Fonctionnalite;

@SuppressWarnings("serial")
public class FonctionnaliteCellRenderer extends DefaultListCellRenderer {
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
        Fonctionnalite f = (Fonctionnalite) value;
        String labelText = "<html>Priorite: " + f.getPriorite() 
        		+ "<br>Description: " + f.getDesc();
        setText(labelText);
        setBackground(COLORS[index % COLORS.length]);
        return this;
    }
}
