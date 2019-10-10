package other;

import java.sql.ResultSet;

import javax.swing.JTable;

public class JTableTuneada extends JTable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JTableTuneada(ResultSet rs) {
		super(new TuneadaTableModel(rs));
	}
	
	public JTableTuneada() {
		super();
	}
	
}
