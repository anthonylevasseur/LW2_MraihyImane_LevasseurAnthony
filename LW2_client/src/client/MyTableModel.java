package client;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class MyTableModel extends DefaultTableModel {
	
	public boolean isCellEditable(int row, int col) {
		return col != 0;
	}

}
