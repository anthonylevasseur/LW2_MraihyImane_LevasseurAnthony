package client;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
class MyTableCellEditor extends DefaultCellEditor {
	protected JScrollPane scrollpane;
	protected JTextArea textarea;

	public MyTableCellEditor() {
		super(new JCheckBox());
		scrollpane = new JScrollPane();
		textarea = new JTextArea(); 
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(BorderLayout.CENTER, textarea);
		scrollpane.getViewport().add(panel);
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		textarea.setText((String) value);
		return scrollpane;
	}

	public Object getCellEditorValue() {
		return textarea.getText();
	}
}
