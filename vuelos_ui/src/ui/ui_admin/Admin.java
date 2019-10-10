package ui.ui_admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import other.TuneadaTableModel;
import sql_conn.vuelos_db;

public class Admin extends JFrame {


	private static final long serialVersionUID = 1L;
	private JLabel label_info,label_dbs,label_cols;
	private JTextArea jta_stmt;
	private JTable table_db;
	private JScrollPane jspTable_db;
	private JButton btn_exec, btn_remove;
	private JScrollPane jsp_jltable, jsp_jlcolumns;
	private JList<String> jl_tables;
	
	public Admin () {
		
		super("Administrador - Vuelos UI");
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
		label_info = new JLabel("Ingrese una consulta SQL y presione ejecutar: ");
		label_dbs = new JLabel("Seleccione una tabla para ver sus atributos: ");
		label_cols = new JLabel();
		jta_stmt = new JTextArea();
		btn_exec = new JButton("Ejecutar");
		btn_remove = new JButton("Borrar");
		table_db = new JTable();
		
		jl_tables = new JList<String>(tables_array);
		jsp_jltable = new JScrollPane();
		jsp_jlcolumns = new JScrollPane();
		
		//Parameters of the graphics objects
		label_info.setLocation(8,0);
		label_info.setSize(800,30);
		
		label_dbs.setLocation(800,0);
		label_dbs.setSize(360,30);
		
		label_cols.setLocation(800,230);
		label_cols.setSize(300,30);
		label_cols.setText("Atributos asociados");
		
		jta_stmt.setTabSize(4);
		jta_stmt.setColumns(100);
		jta_stmt.setSize(796,151);
		jta_stmt.setLocation(1, 30);
		jta_stmt.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.lightGray));

		table_db.setSize(800,340);
		jspTable_db = new JScrollPane(table_db);
		jspTable_db.setBounds(0, 180, 800, 340);
	
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
          			JList<String> jlcolumns = new JList<String>(columns_array);
          			jlcolumns.addListSelectionListener(new ListSelectionListener() {
          	            public void valueChanged(ListSelectionEvent arg0) {
          	                if (!arg0.getValueIsAdjusting()) {
          	                	String selected_column = jlcolumns.getSelectedValue().toString();
	      	          			exec_query("select distinct " + selected_column +" from "+ selected_db);             	  
          	                }
          	            }
          	        });
          			
          			jsp_jlcolumns.setViewportView(jlcolumns);
          			label_cols.setText("Atributos asociados a la tabla " + selected_db + ":");
          			jsp_jlcolumns.setVisible(true);
          			stmt.close();          			
          			 exec_query("select * from " + selected_db);
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
		
		//Add graphic objects 
		this.add(label_info);
		this.add(label_dbs);
		this.add(label_cols);
		this.add(jta_stmt);
		this.add(jspTable_db);
		this.add(btn_exec);
		this.add(btn_remove);
		this.add(jsp_jltable);
		this.add(jsp_jlcolumns);
		
		this.setVisible(true);
		this.exec_query("SELECT * FROM vuelos_disponibles");
		this.update(this.getGraphics());
	}


	protected void exec_query(String sql) {
		if (sql == null)
			sql = this.jta_stmt.getText().trim();
		Connection conn = vuelos_db.get_Connection_Vuelos_DB();
		try {
			String sentence_type = sql.split(" ")[0].toLowerCase();
			Statement stmt = conn.createStatement();
			//Si es una consulta retorno muestro el resultado en la interfaz
			if(sentence_type.equals("select") || sentence_type.equals("show")) {
				ResultSet rs = stmt.executeQuery(sql);
				table_db.setModel(new TuneadaTableModel(rs));
				table_db.setVisible(true);
				jspTable_db.setVisible(true);
				table_db.repaint();
			} else {
				//De lo contrario ejecuto la consulta y notifico que finalizo con exito
				stmt.execute(this.jta_stmt.getText().trim());	
				JOptionPane.showMessageDialog(this, "Sentencia SQL ejecutada con exito ", "Consulta finalizada", JOptionPane.INFORMATION_MESSAGE);
			}
			stmt.close();
		} catch (SQLException e) {
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
			adm.exec_query(null);
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
