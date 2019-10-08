package ui.ui_empleado;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import sql_conn.vuelos_db;

import javax.swing.JComboBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

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
	private DBTable tablaIDA;
	private JLabel lblVuelosDeVuelta;
	private DBTable tablaVUELTA;
	private ArrayList<Ubicacion> ubicaciones;
	private ArrayList<String> FechasIda;

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
		frame.getContentPane().add(rdbtnSoloIda);
		
		rdbtnIdaYVuelta = new JRadioButton("Ida y vuelta");
		rdbtnIdaYVuelta.setBounds(12, 49, 248, 23);
		frame.getContentPane().add(rdbtnIdaYVuelta);
		
		bg = new ButtonGroup();
		bg.add(rdbtnSoloIda);
		bg.add(rdbtnIdaYVuelta);
		
		lblCiudadOrigen = new JLabel("Ciudad Origen");
		lblCiudadOrigen.setBounds(270, 12, 248, 15);
		frame.getContentPane().add(lblCiudadOrigen);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(270, 27, 248, 23);
		//comboBox.addActionListener(new CheckBoxListener());
		comboBox.addMouseListener(new CheckBoxListener());
		frame.getContentPane().add(comboBox);
		
		lblCiudadDestino = new JLabel("Ciudad Destino");
		lblCiudadDestino.setBounds(270, 50, 248, 15);
		frame.getContentPane().add(lblCiudadDestino);
		
		comboBox_1 = new JComboBox<String>();
		comboBox_1.setBounds(270, 65, 248, 23);
		comboBox_1.addMouseListener(new CheckBoxListener());
		//comboBox.addActionListener(new CheckBoxListener());
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
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
                    "Se produjo un error al intentar conectarse a la base de datos.\n",
                    "No se pueden cargar ciudades",
                    JOptionPane.ERROR_MESSAGE);
		}
		
		lblFechaIda = new JLabel("Fecha Ida");
		lblFechaIda.setBounds(536, 12, 248, 15);
		frame.getContentPane().add(lblFechaIda);
		
		comboBoxFechaIda = new JComboBox<String>();
		comboBoxFechaIda.setBounds(536, 27, 248, 23);
		comboBoxFechaIda.setEnabled(false);
		frame.getContentPane().add(comboBoxFechaIda);
		
		lblFechaVuelta = new JLabel("Fecha Vuelta");
		lblFechaVuelta.setBounds(536, 50, 248, 15);
		frame.getContentPane().add(lblFechaVuelta);
		
		comboBoxFechaVuelta = new JComboBox<String>();
		comboBoxFechaVuelta.setBounds(536, 65, 248, 23);
		comboBoxFechaVuelta.setEnabled(false);
		frame.getContentPane().add(comboBoxFechaVuelta);
		
		btnVer = new JButton("Ver");
		btnVer.setBounds(670, 99, 114, 25);
		btnVer.addActionListener(new Boton_ver_listener());
		frame.getContentPane().add(btnVer);
		
		lblVuelosDeIda = new JLabel("Vuelos de ida");
		lblVuelosDeIda.setBounds(12, 115, 139, 15);
		frame.getContentPane().add(lblVuelosDeIda);
		
		tablaIDA = new DBTable();
		tablaIDA.setBounds(12, 129, 772, 340);
		tablaIDA.setEditable(false);
		tablaIDA.addMouseListener(new TablaListener(tablaIDA));
		frame.getContentPane().add(tablaIDA);
		
		lblVuelosDeVuelta = new JLabel("Vuelos de vuelta");
		lblVuelosDeVuelta.setBounds(12, 298, 139, 15);
		lblVuelosDeVuelta.setVisible(false);
		frame.getContentPane().add(lblVuelosDeVuelta);
		
		tablaVUELTA = new DBTable();
		tablaVUELTA.setBounds(12, 312, 772, 157);
		tablaVUELTA.setEditable(false);
		tablaVUELTA.addMouseListener(new TablaListener(tablaVUELTA));
		frame.getContentPane().add(tablaVUELTA);
	}
	
	private void rowSeleccionada(DBTable tabla) {
		try {
			if (tabla == null)
				System.out.println("si es nula");

			String vuelo = tabla.getValueAt(tabla.getSelectedRow(), 0).toString();
			String fecha = tabla.getValueAt(tabla.getSelectedRow(), 1).toString();

			String query = "SELECT DISTINCT vd.fecha,vd.clase, vd.precio, vd.asientos_disponibles "
						 + "FROM vuelos.vuelos_disponibles AS vd "
						 + "WHERE (vd.vuelo = "+vuelo+") AND (vd.fecha = '"+fecha+"');";
			Statement stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			String disponibles = "Para el vuelo "+vuelo+" del dia "+fecha+" ofrece: \n";
			while(rs.next()) {
				String clase = rs.getString("clase");
				String precio = rs.getString("precio");
				String asientos = rs.getString("asientos_disponibles");
				String oracion = "La clase "+clase+" cuesta $"+precio+" y quedan "+asientos+" asientos disponibles \n";
				disponibles = disponibles + oracion;
			}
			JOptionPane.showMessageDialog(frame,
                    disponibles,
                    "Vuelos para "+fecha,
                    JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("entro aca");
		} 
		
	}
	
	private void inflarTablas() {
		boolean idayvuelta =(rdbtnIdaYVuelta.isSelected())? true : false ;
		tablaIDA.setBounds(12, 129, 772, 340);
		lblVuelosDeVuelta.setVisible(false);
		tablaVUELTA.setVisible(false);
		Ubicacion origen = ubicaciones.get(comboBox.getSelectedIndex());
		Ubicacion destino = ubicaciones.get(comboBox_1.getSelectedIndex());
		String queryIda = generarQuery(origen, destino);
		if(idayvuelta) {
			tablaIDA.setBounds(12, 129, 772, 157);
			lblVuelosDeVuelta.setVisible(true);
			tablaVUELTA.setVisible(true);
			String queryVuelta = generarQuery(destino, origen);
			inflarUnaTabla(tablaVUELTA, queryVuelta);
		}
		inflarUnaTabla(tablaIDA, queryIda);
	}
	
	private void inflarUnaTabla(DBTable tabla_db, String query) {
		try {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected String generarQuery(Ubicacion origen, Ubicacion destino) {
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
		return s;
	}
	
	private String generarQueryFecha(Ubicacion origen, Ubicacion destino) {
		String s = "select distinct "
				+ "vd.fecha"
				+ "from vuelos_disponibles as vd "
				+ "where (vd.ciudad_ap_salida = '"+origen.getCiudad()+"') and "
					+ "(vd.estado_ap_salida = '"+origen.getEstado()+"') and "
					+ "(vd.pais_ap_salida = '"+origen.getPais()+"') and "
					+ "(vd.ciudad_ap_llegada = '"+destino.getCiudad()+"') and "
					+ "(vd.estado_ap_llegada = '"+destino.getEstado()+"') and "
					+ "(vd.pais_ap_llegada = '"+destino.getPais()+"')";
		return s;
	}
	
	private class CheckBoxListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			try {
				Ubicacion origen = ubicaciones.get(comboBox.getSelectedIndex());
				Ubicacion destino = ubicaciones.get(comboBox_1.getSelectedIndex());
				String query = generarQueryFecha(origen, destino);
				Statement stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					String f = rs.getString("ciudad");
					FechasIda.add(f);
					comboBoxFechaIda.addItem(f);
				}
				comboBoxFechaIda.setEnabled(true);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(frame,
	                    "Se produjo un error al intentar conectarse a la base de datos.\n",
	                    "No se pueden cargar ciudades",
	                    JOptionPane.ERROR_MESSAGE);
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class TablaListener implements MouseListener{
		
		private DBTable tablaLocal;
		
		public TablaListener(DBTable tablaLocal) {
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
}