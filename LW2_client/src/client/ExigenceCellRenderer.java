package client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import model.Exigence;

@SuppressWarnings("serial")
public class ExigenceCellRenderer extends DefaultListCellRenderer {
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
       	Exigence e = (Exigence) value;
        String labelText = "<html>Identifiant: " + e.getNom() 
        		+ "<br>Priorite: " + e.getPriorite()
        		+ "<br>Description: " + e.getDesc();
        setText(labelText);
        setBackground(COLORS[index % COLORS.length]);
        return this;
    }
}
