package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Types;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import quick.dbtable.*;
import sql_conn.vuelos_db;


public class Admin extends JFrame {

	private JLabel label_info;
	private JTextArea jta_stmt;
	private DBTable table_db;
	private JButton btn_exec, btn_remove;
	private JOptionPane jop_errormsg;
	
	public Admin () {
		
		super("Administrador");
		this.setSize(800,550);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setLayout(null);
		
		//Creation of graphics objects
		label_info = new JLabel("Ingrese consulta SQL: ");
		jta_stmt = new JTextArea();
		btn_exec = new JButton("Ejecutar");
		btn_remove = new JButton("Borrar");
		table_db = new DBTable();
		jop_errormsg = new JOptionPane();
		
		//Parameters of the graphics objects
		label_info.setLocation(0,0);
		label_info.setSize(800,30);
		
		jta_stmt.setTabSize(4);
		jta_stmt.setColumns(100);
		jta_stmt.setSize(800,150);
		jta_stmt.setLocation(0, 30);
		
		table_db.setEditable(false);
		table_db.setSize(800,300);
		table_db.setLocation(0,180);
		table_db.setVisible(false);
	
		btn_exec.setSize(100,20);
		btn_exec.setLocation(280, 490);
		btn_exec.addActionListener(new listener_query(this));
		
		btn_remove.setSize(100,20);
		btn_remove.setLocation(400, 490);
		btn_remove.addActionListener(new listener_remove(jta_stmt));
		
		//Add graphic objects 
		this.add(label_info);
		this.add(jta_stmt);
		this.add(table_db);
		this.add(btn_exec);
		this.add(btn_remove);
		
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
