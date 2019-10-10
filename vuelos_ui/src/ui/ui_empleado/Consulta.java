package ui.ui_empleado;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import other.TuneadaTableModel;
import sql_conn.vuelos_db;

import javax.swing.JComboBox;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;

import quick.dbtable.DBTable;

public class Consulta {

	private JFrame frame;
	private JLabel lblSeleccioneElTipo;
	private JRadioButton rdbtnSoloIda;
	private JRadioButton rdbtnIdaYVuelta;
	private ButtonGroup bg;
	private JLabel lblCiudadOrigen;
	private JComboBox<String> comboBox;
	private JLabel lblCiudadDestino;
	private JComboBox<String> comboBox_1;
	private JLabel lblFechaIda;
	private JComboBox<String> comboBoxFechaIda;
	private JLabel lblFechaVuelta;
	private JComboBox<String> comboBoxFechaVuelta;
	private JButton btnVer;
	private JLabel lblVuelosDeIda;
	private JTable tablaIDA;
	private JScrollPane jspTablaIDA;
	private JLabel lblVuelosDeVuelta;
	private JTable tablaVUELTA;
	private JScrollPane jspTablaVUELTA;
	private ArrayList<Ubicacion> ubicaciones;
	private ArrayList<Date> FechasIda;


	/**
	 * Launch the application.
	 */
	public static void Iniciar() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Consulta window = new Consulta();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Consulta() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		ubicaciones = new ArrayList<Ubicacion>();
		FechasIda = new ArrayList<Date>();
		
		frame = new JFrame("Consulta");
		frame.setBounds(100, 100, 800, 510);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		lblSeleccioneElTipo = new JLabel("Seleccione el tipo de vuelo");
		lblSeleccioneElTipo.setBounds(12, 12, 248, 15);
		frame.getContentPane().add(lblSeleccioneElTipo);
		
		rdbtnSoloIda = new JRadioButton("Solo Ida");
		rdbtnSoloIda.setBounds(12, 30, 248, 23);
		rdbtnSoloIda.setSelected(true);
		rdbtnSoloIda.addItemListener(new RadioButtonListener ());
		frame.getContentPane().add(rdbtnSoloIda);
		
		rdbtnIdaYVuelta = new JRadioButton("Ida y vuelta");
		rdbtnIdaYVuelta.setBounds(12, 49, 248, 23);
		rdbtnIdaYVuelta.addItemListener(new RadioButtonListener ());
			
		
		frame.getContentPane().add(rdbtnIdaYVuelta);
		
		bg = new ButtonGroup();
		bg.add(rdbtnSoloIda);
		bg.add(rdbtnIdaYVuelta);
		
		lblCiudadOrigen = new JLabel("Ciudad Origen");
		lblCiudadOrigen.setBounds(270, 12, 248, 15);
		frame.getContentPane().add(lblCiudadOrigen);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(270, 27, 248, 23);
		
		frame.getContentPane().add(comboBox);
		
		lblCiudadDestino = new JLabel("Ciudad Destino");
		lblCiudadDestino.setBounds(270, 50, 248, 15);
		frame.getContentPane().add(lblCiudadDestino);
		
		comboBox_1 = new JComboBox<String>();
		comboBox_1.setBounds(270, 65, 248, 23);

		frame.getContentPane().add(comboBox_1);

		try {
			String query = "select * from ubicaciones";
			Statement stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Ubicacion u = new Ubicacion(rs.getString("ciudad"), rs.getString("estado"),rs.getString("pais"));
				ubicaciones.add(u);
				comboBox.addItem(u.toString());
				comboBox_1.addItem(u.toString());
			}
			stmt.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
                    "Se produjo un error al intentar conectarse a la base de datos.\n",
                    "No se pueden cargar ciudades",
                    JOptionPane.ERROR_MESSAGE);
		}
		comboBox.setSelectedIndex(0);
		comboBox_1.setSelectedIndex(0);
		
		
		lblFechaIda = new JLabel("Fecha Ida");
		lblFechaIda.setBounds(536, 12, 248, 15);
		frame.getContentPane().add(lblFechaIda);
		
		comboBoxFechaIda = new JComboBox<String>();
		comboBoxFechaIda.setBounds(536, 27, 248, 23);
		comboBoxFechaIda.setEnabled(false);
		
		comboBoxFechaIda.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				boolean idayvuelta =(rdbtnIdaYVuelta.isSelected())? true : false ;
				if(!idayvuelta && comboBoxFechaIda.getSelectedIndex() != -1) {
					btnVer.setEnabled(true);
				} else {
					if(comboBoxFechaIda.getSelectedIndex() != -1) {
						btnVer.setEnabled(false);
						comboBoxFechaVuelta.removeAllItems();
						Ubicacion origen = ubicaciones.get(comboBox.getSelectedIndex());
						Ubicacion destino = ubicaciones.get(comboBox_1.getSelectedIndex());
						Date fecha = FechasIda.get(comboBoxFechaIda.getSelectedIndex());
						comboBoxFechaVuelta.removeAllItems();
						String query = generarQueryFecha(destino,origen,fecha);
						SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
						try {
							Statement stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
							ResultSet rs = stmt.executeQuery(query);
							while(rs.next()) {
								Date f = rs.getDate("fecha");
								comboBoxFechaVuelta.addItem(form.format(f));
							}
							comboBoxFechaVuelta.setEnabled(true);
							stmt.close();
						} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame,
			                    e.getMessage(),
			                    "No se pueden cargar ciudades",
			                    JOptionPane.ERROR_MESSAGE);
						}		
					}
				}
				
							
			}
			
				
		});
		frame.getContentPane().add(comboBoxFechaIda);
		
		lblFechaVuelta = new JLabel("Fecha Vuelta");
		lblFechaVuelta.setBounds(536, 50, 248, 15);
		frame.getContentPane().add(lblFechaVuelta);
		
		comboBoxFechaVuelta = new JComboBox<String>();
		comboBoxFechaVuelta.setBounds(536, 65, 248, 23);
		comboBoxFechaVuelta.setEnabled(false);
		comboBoxFechaVuelta.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				boolean idayvuelta =(rdbtnIdaYVuelta.isSelected())? true : false ;
				if (idayvuelta && comboBoxFechaVuelta.getSelectedIndex() != -1)
					btnVer.setEnabled(true);
							
			}
			
				
		});
		frame.getContentPane().add(comboBoxFechaVuelta);
		
		comboBox.addActionListener(new ListenerComboBox());

		comboBox_1.addActionListener(new ListenerComboBox());

		btnVer = new JButton("Ver");
		btnVer.setBounds(670, 99, 114, 25);
		btnVer.addActionListener(new Boton_ver_listener());
		btnVer.setEnabled(false);
		frame.getContentPane().add(btnVer);
		
		lblVuelosDeIda = new JLabel("Vuelos de ida");
		lblVuelosDeIda.setBounds(12, 115, 139, 15);
		frame.getContentPane().add(lblVuelosDeIda);
		
		tablaIDA = new JTable();
		tablaIDA.setBounds(12, 129, 772, 340);
		tablaIDA.addMouseListener(new TablaListener(tablaIDA));
		jspTablaIDA = new JScrollPane(tablaIDA);
		jspTablaIDA.setBounds(12, 129, 772, 340);
		frame.getContentPane().add(jspTablaIDA);
		
		lblVuelosDeVuelta = new JLabel("Vuelos de vuelta");
		lblVuelosDeVuelta.setBounds(12, 298, 139, 15);
		lblVuelosDeVuelta.setVisible(false);
		frame.getContentPane().add(lblVuelosDeVuelta);
		
		tablaVUELTA = new JTable();
		tablaVUELTA.setBounds(12, 312, 772, 157);
		tablaVUELTA.addMouseListener(new TablaListener(tablaVUELTA));
		jspTablaVUELTA = new JScrollPane(tablaVUELTA);
		jspTablaVUELTA.setBounds(12, 312, 772, 157);
		frame.getContentPane().add(jspTablaVUELTA);
	}
	
	private void rowSeleccionada(JTable tabla) {
		try {
			if (tabla == null)
				System.out.println("si es nula");

			String vuelo = tabla.getValueAt(tabla.getSelectedRow(), 0).toString();
			String fecha = tabla.getValueAt(tabla.getSelectedRow(), 1).toString();
			String query = "SELECT DISTINCT vd.fecha,vd.clase, vd.precio, vd.asientos_disponibles "
						 + "FROM vuelos.vuelos_disponibles AS vd "
						 + "WHERE (vd.vuelo = "+vuelo+") AND (vd.fecha = '"+fecha+"');";
			//nueva forma de mostrar la tabla
			Statement stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			JTable table = new JTable(new TuneadaTableModel(rs));
			table.setSize(300, 300);
			JOptionPane.showMessageDialog(frame,
					new JScrollPane(table),
                    "Vuelos para "+fecha,
                    JOptionPane.INFORMATION_MESSAGE);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("entro aca");
		} 
		
	}
	
	private void inflarTablas() {
		boolean idayvuelta =(rdbtnIdaYVuelta.isSelected())? true : false ;
		jspTablaIDA.setBounds(12, 129, 772, 340);
		lblVuelosDeVuelta.setVisible(false);
		jspTablaVUELTA.setVisible(false);
		//jspTablaIDA.repaint();
		Ubicacion origen = ubicaciones.get(comboBox.getSelectedIndex());
		Ubicacion destino = ubicaciones.get(comboBox_1.getSelectedIndex());
		Date fecha_ida = FechasIda.get(comboBoxFechaIda.getSelectedIndex());
		String queryIda = generarQuery(origen, destino,fecha_ida);
		if(idayvuelta) {
			Date fecha_vuelta = FechasIda.get(comboBoxFechaVuelta.getSelectedIndex());

			jspTablaIDA.setBounds(12, 129, 772, 157);
			lblVuelosDeVuelta.setVisible(true);
			jspTablaVUELTA.setVisible(true);
			jspTablaIDA.repaint();
			String queryVuelta = generarQuery(destino, origen,fecha_vuelta);
			inflarUnaTabla(tablaVUELTA, queryVuelta);
		}
		inflarUnaTabla(tablaIDA, queryIda);
		jspTablaIDA.repaint();
	}
	
	private void inflarUnaTabla(JTable tabla_db, String query) {
		try {
			Statement stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			tabla_db.setModel(new TuneadaTableModel(rs));
			tabla_db.repaint();
			stmt.close();
			/*
			tabla_db.setConnection(vuelos_db.get_Connection_Vuelos_DB());
			tabla_db.setSelectSql(query);
			tabla_db.createColumnModelFromQuery();
			for (int i = 0; i < tabla_db.getColumnCount(); i++) { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
	    		 if	 (tabla_db.getColumn(i).getType()==Types.TIME){    		 
	    			 tabla_db.getColumn(i).setType(Types.CHAR);  
	  	       	 }
	    		 if	 (tabla_db.getColumn(i).getType()==Types.DATE){
	    			 tabla_db.getColumn(i).setDateFormat("dd/MM/YYYY");
	    		 }
	          }
			tabla_db.refresh();
			*/
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected String generarQueryFecha(Ubicacion origen, Ubicacion destino, Date fecha_ida) {
		String s = "select distinct "
					+ "vd.fecha "
					+ "from vuelos_disponibles as vd "
					+ "where (vd.ciudad_ap_salida = '"+origen.getCiudad()+"') and "
						+ "(vd.estado_ap_salida = '"+origen.getEstado()+"') and "
					+ "(vd.pais_ap_salida = '"+origen.getPais()+"') and "
					+ "(vd.ciudad_ap_llegada = '"+destino.getCiudad()+"') and "
					+ "(vd.estado_ap_llegada = '"+destino.getEstado()+"') and "
					+ "(vd.pais_ap_llegada = '"+destino.getPais()+"')";
		if (fecha_ida != null)
			s += " and (DATEDIFF('"+ fecha_ida.toString() + "',vd.fecha) <  0)";
		
		System.out.println(s);

		return s;
	}
	
	protected String generarQuery(Ubicacion origen, Ubicacion destino, Date fecha_ida) {
		String s = "select distinct "
					+ "vd.vuelo, vd.fecha, vd.nombre_ap_salida, vd.hora_sale, vd.nombre_ap_llegada, "
					+ "vd.hora_llega, vd.modelo_avion, vd.tiempo_estimado "
					+ "from vuelos_disponibles as vd "
					+ "where (vd.ciudad_ap_salida = '"+origen.getCiudad()+"') and "
						+ "(vd.estado_ap_salida = '"+origen.getEstado()+"') and "
					+ "(vd.pais_ap_salida = '"+origen.getPais()+"') and "
					+ "(vd.ciudad_ap_llegada = '"+destino.getCiudad()+"') and "
					+ "(vd.estado_ap_llegada = '"+destino.getEstado()+"') and "
					+ "(vd.pais_ap_llegada = '"+destino.getPais()+"')";

		if (fecha_ida != null)
			s += " and (vd.fecha = '"+ fecha_ida.toString() + "')";
		System.out.println(s);

		return s;
	}
	
	public static DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
	
	private class ListenerComboBox implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			btnVer.setEnabled(false);
			boolean idayvuelta =(rdbtnIdaYVuelta.isSelected())? true : false ;
			if (!idayvuelta)
				comboBoxFechaVuelta.setEnabled(false);
			Ubicacion origen = ubicaciones.get(comboBox.getSelectedIndex());
			Ubicacion destino = ubicaciones.get(comboBox_1.getSelectedIndex());
			String query = generarQueryFecha(origen,destino,null);
			SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
			try {
				Statement stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
				ResultSet rs = stmt.executeQuery(query);
				comboBoxFechaIda.removeAllItems();
				comboBoxFechaVuelta.removeAllItems();
				FechasIda = new ArrayList<Date>();
				
				while(rs.next()) {
					Date f = rs.getDate("fecha");
					FechasIda.add(f);
					comboBoxFechaIda.addItem(form.format(f));
				}
				comboBoxFechaIda.setEnabled(true);
				stmt.close();
			} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
	                e.getMessage(),
	                "No se pueden cargar ciudades",
	                JOptionPane.ERROR_MESSAGE);
			}					
		}
		
		
	}
	
	private class TablaListener implements MouseListener{
		
		private JTable tablaLocal;
		
		public TablaListener(JTable tablaLocal) {
			this.tablaLocal = tablaLocal;
		}

		public void mouseClicked(MouseEvent arg0) {
			rowSeleccionada(tablaLocal);
		}

		public void mouseEntered(MouseEvent arg0) {}

		public void mouseExited(MouseEvent arg0) {}

		public void mousePressed(MouseEvent arg0) {}

		public void mouseReleased(MouseEvent arg0) {}
		
	}
	
	private class Boton_ver_listener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			inflarTablas();
		}

	}
	
	private class RadioButtonListener implements ItemListener{

		public void itemStateChanged(ItemEvent e) {
		    if (e.getStateChange() == ItemEvent.SELECTED) {
		        // Your selected code here.
		    	comboBoxFechaVuelta.setEnabled(false);
		    	btnVer.setEnabled(false);
		    }
		}
			
	}
	
}
