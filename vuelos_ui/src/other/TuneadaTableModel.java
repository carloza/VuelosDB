package other;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class TuneadaTableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TuneadaTableModel(ResultSet rs) {
		super(getDatos(rs), nombreColumnas(rs));
	}
	
	public static Vector<String> nombreColumnas(ResultSet rs){
		Vector<String> colNomb = new Vector<String>();
		
		try {
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
		    for (int column = 1; column <= columnCount; column++) {
		    	colNomb.add(metaData.getColumnName(column));
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return colNomb;
	}
	
	public static Vector<Vector<Object>> getDatos(ResultSet rs){
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		try {
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
		    while (rs.next()) {
		        Vector<Object> vector = new Vector<Object>();
		        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
		            vector.add(rs.getObject(columnIndex));
		        }
		        data.add(vector);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return data;
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) 
    {
       return false;
    } 
}
