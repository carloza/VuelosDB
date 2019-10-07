package ui.ui_empleado;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import sql_conn.vuelos_db;

import javax.swing.JComboBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JTable;

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
	private JButton btnVer;
	private JLabel lblVuelosDeIda;
	private DBTable tablaIDA;
	private JLabel lblVuelosDeVuelta;
	private DBTable tablaVUELTA;
	private ArrayList<Ubicacion> ubicaciones;

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
//		Connection conexionDB = vuelos_db.get_Connection_Vuelos_DB();
//		Statement stmt;
		
		ubicaciones = new ArrayList<Ubicacion>();
		
		frame = new JFrame("Consulta");
		frame.setBounds(100, 100, 800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
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
		frame.getContentPane().add(comboBox);
		
		lblCiudadDestino = new JLabel("Ciudad Destino");
		lblCiudadDestino.setBounds(536, 12, 248, 15);
		frame.getContentPane().add(lblCiudadDestino);
		
		comboBox_1 = new JComboBox<String>();
		comboBox_1.setBounds(536, 27, 248, 23);
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
//			e.printStackTrace();
			JOptionPane.showMessageDialog(frame,
                    "Se produjo un error al intentar conectarse a la base de datos.\n",
                    "No se pueden cargar ciudades",
                    JOptionPane.ERROR_MESSAGE);
		}
		btnVer = new JButton("Ver");
		btnVer.setBounds(670, 59, 114, 25);
		btnVer.addActionListener(new Boton_ver_listener());
		frame.getContentPane().add(btnVer);
		
		lblVuelosDeIda = new JLabel("Vuelos de ida");
		lblVuelosDeIda.setBounds(12, 80, 139, 15);
		frame.getContentPane().add(lblVuelosDeIda);
		
		tablaIDA = new DBTable();
		tablaIDA.setBounds(12, 94, 772, 340);
		tablaIDA.setEditable(false);
		frame.getContentPane().add(tablaIDA);
		
		lblVuelosDeVuelta = new JLabel("Vuelos de vuelta");
		lblVuelosDeVuelta.setBounds(12, 263, 139, 15);
		lblVuelosDeVuelta.setVisible(false);
		frame.getContentPane().add(lblVuelosDeVuelta);
		
		tablaVUELTA = new DBTable();
		tablaVUELTA.setBounds(12, 277, 772, 157);
		tablaVUELTA.setEditable(false);
		frame.getContentPane().add(tablaVUELTA);
	}
	
	private void inflarTablas() {
		boolean idayvuelta =(rdbtnIdaYVuelta.isSelected())? true : false ;
		tablaIDA.setBounds(12, 94, 772, 340);
		lblVuelosDeVuelta.setVisible(false);
		tablaVUELTA.setVisible(false);
		Ubicacion origen = ubicaciones.get(comboBox.getSelectedIndex());
		Ubicacion destino = ubicaciones.get(comboBox_1.getSelectedIndex());
		String queryIda = generarQuery(origen, destino);
		if(idayvuelta) {
			tablaIDA.setBounds(12, 94, 772, 157);
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
			tabla_db.refresh();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected String generarQuery(Ubicacion origen, Ubicacion destino) {
		String s = "select distinct "
				+ "vd.vuelo, vd.nombre_ap_salida, vd.hora_sale, vd.nombre_ap_llegada, "
				+ "vd.hora_llega, vd.modelo_avion, vd.tiempo_estimado "
				+ "from vuelos_disponibles as vd "
				+ "where (vd.ciudad_ap_salida = '"+origen.getCiudad()+"') and "
					+ "(vd.estado_ap_salida = '"+origen.getEstado()+"') and "
					+ "(vd.pais_ap_salida = '"+origen.getPais()+"') and "
					+ "(vd.ciudad_ap_llegada = '"+destino.getCiudad()+"') and "
					+ "(vd.estado_ap_llegada = '"+destino.getEstado()+"') and "
					+ "(vd.pais_ap_llegada = '"+destino.getPais()+"')";
//		System.out.println(s);
		return s;
	}
	
	private class Boton_ver_listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			inflarTablas();
		}

	}
}
