package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import quick.dbtable.*;
import sql_conn.vuelos_db;


public class Admin extends JFrame {

	private JLabel label_info,label_dbs,label_cols;
	private JTextArea jta_stmt;
	private DBTable table_db;
	private JButton btn_exec, btn_remove;
	private JScrollPane jsp_jltable, jsp_jlcolumns;
	private JList<String> jl_tables, jl_columns;
	
	public Admin () {
		
		super("Administrador");
		this.setSize(1100,550);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setLayout(null);
		
		String[] tables_array = null;
		//Get tables of db
		try {
			Statement stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
			ResultSet rs = stmt.executeQuery("show tables");
			ArrayList<String>tmp_list = new ArrayList<String>();
			while (rs.next()) 
				tmp_list.add(rs.getString("Tables_in_vuelos"));
			tables_array = tmp_list.toArray(new String[tmp_list.size()]);
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		//Creation of graphics objects
		label_info = new JLabel("Ingrese consulta SQL y presione ejecutar: ");
		label_dbs = new JLabel("Seleccione una tabla para ver sus atributos: ");
		label_cols = new JLabel();
		jta_stmt = new JTextArea();
		btn_exec = new JButton("Ejecutar");
		btn_remove = new JButton("Borrar");
		table_db = new DBTable();
		jl_tables = new JList<String>(tables_array);
		jl_columns = new JList<String>();
		jsp_jltable = new JScrollPane();
		jsp_jlcolumns = new JScrollPane();

		
		//Parameters of the graphics objects
		label_info.setLocation(0,0);
		label_info.setSize(800,30);
		
		label_dbs.setLocation(800,0);
		label_dbs.setSize(360,30);
		
		label_cols.setLocation(800,230);
		label_cols.setSize(300,30);
		
		jta_stmt.setTabSize(4);
		jta_stmt.setColumns(100);
		jta_stmt.setSize(800,150);
		jta_stmt.setLocation(0, 30);
		
		table_db.setEditable(false);
		table_db.setSize(800,370);
		table_db.setLocation(0,180);
		table_db.setVisible(false);
	
		btn_exec.setSize(100,20);
		btn_exec.setLocation(850, 480);
		btn_exec.addActionListener(new listener_query(this));
		
		btn_remove.setSize(100,20);
		btn_remove.setLocation(950, 480);
		btn_remove.addActionListener(new listener_remove(jta_stmt));
		
		jl_tables.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                  String selected_db = jl_tables.getSelectedValue().toString();
                  String[] columns_array = null;
                  ArrayList<String> tmp_list = new ArrayList<String>();
                  try {
          			Statement stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
          			ResultSet rs = stmt.executeQuery("describe " + selected_db);
          			while (rs.next()) 
          				tmp_list.add(rs.getString("Field"));
          			columns_array = tmp_list.toArray(new String[tmp_list.size()]);
          			jsp_jlcolumns.setViewportView(new JList<String>(columns_array));
          			label_cols.setText("Atributos asociados a la tabla " + selected_db + ":");
          			jsp_jlcolumns.setVisible(true);
          			stmt.close();          			
          		} catch (SQLException e) {
          			e.printStackTrace();
          		}
                }
            }
        });
		
		jsp_jltable.setLocation(800,30);
		jsp_jltable.setSize(290,200);
		jsp_jltable.add(jl_tables);
		jsp_jltable.setViewportView(jl_tables);
		
		jsp_jlcolumns.setLocation(800, 260);
		jsp_jlcolumns.setSize(290, 200);
		jsp_jlcolumns.setVisible(false);
		
		//Add graphic objects 
		this.add(label_info);
		this.add(label_dbs);
		this.add(label_cols);
		this.add(jta_stmt);
		this.add(table_db);
		this.add(btn_exec);
		this.add(btn_remove);
		this.add(jsp_jltable);
		this.add(jsp_jlcolumns);
		
		this.setVisible(true);
		this.update(this.getGraphics());
	}


	protected void exec_query() {
		 try {    
			 table_db.setConnection(vuelos_db.get_Connection_Vuelos_DB());

			 String sql = this.jta_stmt.getText().trim();
			 String sentence_type = sql.split(" ")[0].toLowerCase();
			 
			 //Si es una consulta retorno muestro el resultado en la interfaz
			 if(sentence_type.equals("select")) {
				 table_db.setSelectSql(this.jta_stmt.getText().trim());
					
		    	 table_db.createColumnModelFromQuery();    	    
		    	  for (int i = 0; i < table_db.getColumnCount(); i++) { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
		    		 if	 (table_db.getColumn(i).getType()==Types.TIME){    		 
		    			 table_db.getColumn(i).setType(Types.CHAR);  
		  	       	 }
		    		 if	 (table_db.getColumn(i).getType()==Types.DATE){
		    			 table_db.getColumn(i).setDateFormat("dd/MM/YYYY");
		    		 }
		          }  
		    	  table_db.refresh();	  
		    	  table_db.setVisible(true);
			 } else {
				 //De lo contrario ejecuto la consulta y notifico que finalizo con exito
				 Connection conn = vuelos_db.get_Connection_Vuelos_DB();

				 Statement stmt = conn.createStatement();
				 stmt.execute(this.jta_stmt.getText().trim());
				 stmt.close();	
				 JOptionPane.showMessageDialog(this, "Sentencia SQL ejecutada con exito ", "Consulta finalizada", JOptionPane.INFORMATION_MESSAGE);
			 }
			 
	       }
	      catch (SQLException e) {
    		 String error_msg = e.getMessage()
 					+ "\nSQLState: " + e.getSQLState() 
 					+ "\nVendorError: " + e.getErrorCode();
    		 JOptionPane.showMessageDialog(this, error_msg, "Error al realizar consulta", JOptionPane.ERROR_MESSAGE);
	      }
	}
	
	
	private class listener_query implements ActionListener{

		private Admin adm;
		public listener_query(Admin adm) {
			this.adm = adm;
		}
		public void actionPerformed(ActionEvent arg0) {
			adm.exec_query();
		}
		
		
	}
	
	private class listener_remove implements ActionListener{

		private JTextArea jta_stmt;
		public listener_remove(JTextArea jta_stmt) {
			this.jta_stmt = jta_stmt;
		}
		public void actionPerformed(ActionEvent arg0) {
			jta_stmt.setText("");
		}
		
		
	}
	
	

}
