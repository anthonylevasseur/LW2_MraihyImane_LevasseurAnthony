package client;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
class MyTableCellRenderer extends JScrollPane implements TableCellRenderer {
	JTextArea textarea;

	public MyTableCellRenderer() {
		textarea = new JTextArea();
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(BorderLayout.CENTER, textarea);
		getViewport().add(panel);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
			textarea.setForeground(table.getSelectionForeground());
			textarea.setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
			textarea.setForeground(table.getForeground());
			textarea.setBackground(table.getBackground());
		}
		textarea.setText((String) value);
		textarea.setCaretPosition(0);
		return this;
	}
}
