package ui.ui_empleado;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
//import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import other.TuneadaTableModel;
import sql_conn.vuelos_db;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

import java.sql.CallableStatement;
import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
//import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

public class Consulta {

	private JFrame frame;
	private JLabel lblSeleccioneElTipo, lblAyuda;
	private JRadioButton rdbtnSoloIda;
	private JRadioButton rdbtnIdaYVuelta;
	private ButtonGroup bg;
	private JLabel lblCiudadOrigen;
	private JComboBox<String> comboBox;
	private JLabel lblCiudadDestino;
	private JComboBox<String> comboBox_1;
	private JLabel lblFechaIda;
	private JFormattedTextField jftfFechaIda;
	private JLabel lblFechaVuelta;
	private JFormattedTextField jftfFechaVuelta;
	private JButton btnVer;
	private JLabel lblVuelosDeIda;
	private JTable tablaIDA;
	private JScrollPane jspTablaIDA;
	private JLabel lblVuelosDeVuelta;
	private JTable tablaVUELTA;
	private JScrollPane jspTablaVUELTA;
	private ArrayList<Ubicacion> ubicaciones;
	private JButton btnReserva;
	private Reserva reserva;
	private boolean performinReserva;
	private boolean valoresIda;
	private boolean valoresVuelta;
	private String numeroLegajo;
	private JLabel lblStatus;


	/**
	 * Launch the application.
	 */
	public static void Iniciar(String legajo) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Consulta window = new Consulta(legajo);
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
	public Consulta(String legajo) {
		numeroLegajo = legajo;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		ubicaciones = new ArrayList<Ubicacion>();
		//FechasIda = new ArrayList<Date>();
		
		
		frame = new JFrame("GUI Empleado");
		frame.setBounds(100, 100, 800, 540);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
		frame.addKeyListener(new KeyAdapter() {
		

			public void keyPressed(KeyEvent arg0) {
				System.out.println("Entro");
				int key = arg0.getKeyCode(); 
				if (key == KeyEvent.VK_ESCAPE) {
					cancelarReserva();
				}
			}
		});
		
		reserva = null;
		performinReserva = false;
		valoresIda = false;
		valoresVuelta = false;
		
		lblSeleccioneElTipo = new JLabel("Tipo de vuelo");
		lblSeleccioneElTipo.setBounds(12, 17, 100, 15);
		frame.getContentPane().add(lblSeleccioneElTipo);

		lblAyuda = new JLabel("Seleccione tipo de vuelo, origen, destino y fechas correspondientes");
		lblAyuda.setBounds(0, 0, 481, 15);
		frame.getContentPane().add(lblAyuda);
		
		rdbtnSoloIda = new JRadioButton("Solo Ida");
		rdbtnSoloIda.setBounds(17, 35, 248, 23);
		rdbtnSoloIda.setSelected(true);
		rdbtnSoloIda.addItemListener(new RadioButtonListener ());
		frame.getContentPane().add(rdbtnSoloIda);
		
		rdbtnIdaYVuelta = new JRadioButton("Ida y vuelta");
		rdbtnIdaYVuelta.setBounds(17, 54, 248, 23);
		rdbtnIdaYVuelta.addItemListener(new RadioButtonListener ());
			
		frame.getContentPane().add(rdbtnIdaYVuelta);
		
		bg = new ButtonGroup();
		bg.add(rdbtnSoloIda);
		bg.add(rdbtnIdaYVuelta);
		
		lblCiudadOrigen = new JLabel("Ciudad Origen");
		lblCiudadOrigen.setBounds(275, 17, 248, 15);
		frame.getContentPane().add(lblCiudadOrigen);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(275, 32, 248, 23);
		
		frame.getContentPane().add(comboBox);
		
		lblCiudadDestino = new JLabel("Ciudad Destino");
		lblCiudadDestino.setBounds(275, 55, 248, 15);
		frame.getContentPane().add(lblCiudadDestino);
		
		comboBox_1 = new JComboBox<String>();
		comboBox_1.setBounds(275, 70, 248, 23);

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
		lblFechaIda.setBounds(541, 17, 248, 15);
		frame.getContentPane().add(lblFechaIda);
		
		MaskFormatter mascara = null;
		try {
			mascara = new MaskFormatter("##/##/####");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    jftfFechaIda = new JFormattedTextField(mascara);
//		jftfFechaIda = new JComboBox<String>();
		jftfFechaIda.setBounds(541, 32, 248, 23);
		jftfFechaIda.setEnabled(false);
		jftfFechaIda.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent arg0) {
				boolean idayvuelta =(rdbtnIdaYVuelta.isSelected())? true : false ;
				if(!idayvuelta) {
					btnVer.setEnabled(true);
					btnReserva.setEnabled(false);
				} else {
					btnVer.setEnabled(false);
					jftfFechaVuelta.setEnabled(true);
					btnReserva.setEnabled(false);
				}
			}
		});
//		jftfFechaIda.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent ae) {
//				boolean idayvuelta =(rdbtnIdaYVuelta.isSelected())? true : false ;
//				if(!idayvuelta) {
//					btnVer.setEnabled(true);
//				} else {
//					btnVer.setEnabled(false);
//					jftfFechaVuelta.setEnabled(true);
//				}		
//			}	
//		});
		
		frame.getContentPane().add(jftfFechaIda);
		
		lblFechaVuelta = new JLabel("Fecha Vuelta");
		lblFechaVuelta.setBounds(541, 55, 248, 15);
		frame.getContentPane().add(lblFechaVuelta);
		
		jftfFechaVuelta = new JFormattedTextField(mascara);
		jftfFechaVuelta.setBounds(541, 70, 248, 23);
		jftfFechaVuelta.setEnabled(false);
		jftfFechaVuelta.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent arg0) {
				btnVer.setEnabled(true);
			}
		});
//		jftfFechaVuelta.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent ae) {
//				btnVer.setEnabled(true);
//			}	
//		});
		
		frame.getContentPane().add(jftfFechaVuelta);
		
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
		tablaIDA.addMouseListener(new TablaListenerMostrarInfo(tablaIDA));
		jspTablaIDA = new JScrollPane(tablaIDA);
		jspTablaIDA.setBounds(12, 129, 772, 340);
		frame.getContentPane().add(jspTablaIDA);
		
		lblVuelosDeVuelta = new JLabel("Vuelos de vuelta");
		lblVuelosDeVuelta.setBounds(12, 298, 139, 15);
		lblVuelosDeVuelta.setVisible(false);
		frame.getContentPane().add(lblVuelosDeVuelta);
		
		tablaVUELTA = new JTable();
		tablaVUELTA.setBounds(12, 312, 772, 157);
		tablaVUELTA.addMouseListener(new TablaListenerMostrarInfo(tablaVUELTA));
		jspTablaVUELTA = new JScrollPane(tablaVUELTA);
		jspTablaVUELTA.setBounds(12, 312, 772, 157);
		frame.getContentPane().add(jspTablaVUELTA);
		
		btnReserva = new JButton("Habilitar modo reserva");
		btnReserva.setBounds(541, 481, 243, 25);
		btnReserva.addActionListener(new BotonReservaListener(btnReserva));
		btnReserva.setEnabled(false);
		frame.getContentPane().add(btnReserva);
		
		JLabel lblEstadoActual = new JLabel("Estado actual:");
		lblEstadoActual.setBounds(12, 486, 106, 15);
		frame.getContentPane().add(lblEstadoActual);
		
		lblStatus = new JLabel("Seleccione Datos de vuelo");
		lblStatus.setBounds(130, 486, 317, 15);
		frame.getContentPane().add(lblStatus);
	}
	
	private void rowSeleccionada(JTable tabla) {
		try {
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
			if(performinReserva) {
				int resu = JOptionPane.showConfirmDialog(
						frame,
						new JScrollPane(table),
						"Vuelos para "+fecha,
						JOptionPane.OK_CANCEL_OPTION);
				if(performinReserva && resu == JOptionPane.OK_OPTION) {
					String clase = table.getValueAt(tabla.getSelectedRow(), 1).toString();
					setearReserva(tabla, vuelo, fecha, clase);
				}/*else { VER ESTO, queda inconsistente con el boton para cerrar ventana del joptionpane
					cancelarReserva();
				}*/
			}else {

				JOptionPane.showOptionDialog(
						frame,
						new JScrollPane(table),
						"Vuelos para "+fecha,
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE,
						null,
						new Object[]{}, null);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}
	
	private void setearReserva(JTable tabla, String vuelo, String fecha, String clase) {
		if(tabla == tablaIDA) {
			reserva.setNumeroVueloIda(vuelo);
			reserva.setFechaVueloIda(fecha);
			reserva.setClaseVueloIda(clase);
			valoresIda = true;
		}else if(tabla == tablaVUELTA){
			reserva.setNumeroVueloVuelta(vuelo);
			reserva.setFechaVueloVuelta(fecha);
			reserva.setClaseVueloVuelta(clase);
			valoresIda = true;
			valoresVuelta = true;
		}
		setearReservaSegundaFase();
	}

	private void setearReservaSegundaFase() {
		if((!reserva.isIdaYVuelta() && valoresIda) || (valoresIda && valoresVuelta)) {
			JOptionPane.showMessageDialog(frame,
					"Vuelo(s) seleccionado(s) \n"
					+ "Solo resta ingresar informacion del pasajero\n",
                    "Reservas",
                    JOptionPane.INFORMATION_MESSAGE);
			
			//aca pido el tipo de documento
			String[] tDoc = { "DNI", "LE", "LU", "PASAPORTE", "OTRO" };
			String tipoDoc = (String) JOptionPane.showInputDialog(frame, 
					"Seleccione su Tipo de documento",
			        "",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        tDoc, 
			        tDoc[0]);
			
			if(tipoDoc == null) { 
				cancelarReserva();			
			} else {
				//aca el numero del documento
			    NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
			    formatter.setValueClass(Integer.class);
			    formatter.setMinimum(0);
			    formatter.setMaximum(Integer.MAX_VALUE);
			    formatter.setAllowsInvalid(false);
			    formatter.setCommitsOnValidEdit(true);
			    JFormattedTextField nDoc = new JFormattedTextField(formatter);
				JComponent[] inputs = new JComponent[] { new JLabel("Ingrese su numero de documento"), nDoc };
				String nroDoc = "";
				int result = JOptionPane.showConfirmDialog(null, inputs, "My custom dialog", JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
				    nroDoc = nDoc.getText();
				} else {
				    System.out.println("User canceled / closed the dialog, result = " + result);
				}
				try {
					reserva.setNumeroDocumento((NumberFormat.getInstance().parse(nroDoc)).intValue() + "");
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				reserva.setTipoDocumento(tipoDoc);
				reserva.setLegajoEmpleado(numeroLegajo);		
				
				performinReserva = false;
				lblStatus.setText("Mostrando vuelos");
				//TODO aca llamo al store procedure
				CallableStatement stmt;
				String sql = "";
				if (reserva.isIdaYVuelta()) {
					sql = "{CALL reservar_vuelo_ida_vuelta(?,?,?,?,?,?,?,?,?,?,?)}";
				} else {
					sql = "{CALL reservar_vuelo_ida(?,?,?,?,?,?,?,?)}";

				}
				try {
					//PREPARACION STATMENT
					stmt = vuelos_db.get_Connection_Vuelos_DB().prepareCall(sql);
					
					//Parametros entrada
					stmt.setString(1, reserva.getNumeroVueloIda());
					stmt.setString(2, reserva.getFechaVueloIda());
					stmt.setString(3, reserva.getClaseVueloIda());
					
					if (reserva.isIdaYVuelta()) {
						stmt.setString(4, reserva.getNumeroVueloVuelta());
						stmt.setString(5, reserva.getFechaVueloVuelta());
						stmt.setString(6, reserva.getClaseVueloVuelta());	
						stmt.setString(7, reserva.getTipoDocumento());
						stmt.setString(8, reserva.getNumeroDocumento());
						stmt.setString(9, reserva.getLegajoEmpleado());		
						stmt.registerOutParameter(10, java.sql.Types.INTEGER);
						stmt.registerOutParameter(11, java.sql.Types.VARCHAR);
					} else {
						stmt.setString(4, reserva.getTipoDocumento());
						stmt.setString(5, reserva.getNumeroDocumento());
						stmt.setString(6, reserva.getLegajoEmpleado());
						stmt.registerOutParameter(7, java.sql.Types.INTEGER);
						stmt.registerOutParameter(8, java.sql.Types.VARCHAR);
					}

					stmt.executeUpdate();
					
					int out_param_salida_num = 0;						
					int out_param_salida = 0;
					
					if (reserva.isIdaYVuelta()) {
						out_param_salida_num = 10;
						out_param_salida = 11;
					} else {
						out_param_salida_num = 7;
						out_param_salida = 8;
					}
					
					if (stmt.getInt(out_param_salida_num) == 0) {
						JOptionPane.showMessageDialog(frame,
			                    "Estado reserva: " + stmt.getString(out_param_salida),
			                    "RESERVA EXITOSA",
			                    JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(frame,
								stmt.getString(out_param_salida),
			                    "ERROR AL INGRESAR RESERVA",
			                    JOptionPane.ERROR_MESSAGE);
						cancelarReserva();
					}
					
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Luego de llamar al store procedure 
				cancelarReserva();
			}
				
			
						
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
		String fecha_ida = null;
		try {
			fecha_ida = sdf2.format(sdf.parse(jftfFechaIda.getText()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String queryIda = generarQuery(origen, destino,fecha_ida);
//		System.out.println("boton VER");
//		System.out.println(queryIda);
		if(idayvuelta) {
			String fecha_vuelta = null;
			try {
				fecha_vuelta = sdf2.format(sdf.parse(jftfFechaVuelta.getText()));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			jspTablaIDA.setBounds(12, 129, 772, 157);
			lblVuelosDeVuelta.setVisible(true);
			jspTablaVUELTA.setVisible(true);
			jspTablaIDA.repaint();
			String queryVuelta = generarQuery(destino, origen,fecha_vuelta);
			inflarUnaTabla(tablaVUELTA, queryVuelta);
		}
		inflarUnaTabla(tablaIDA, queryIda);
		jspTablaIDA.repaint();
		btnReserva.setEnabled(true);
	}
	
	private void inflarUnaTabla(JTable tabla_db, String query) {
		try {
			Statement stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			tabla_db.setModel(new TuneadaTableModel(rs));
			tabla_db.repaint();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	protected String generarQuery(Ubicacion origen, Ubicacion destino, String fecha) {
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

		if (fecha != null) {
			s += " and (vd.fecha = '"+ fecha.toString() + "')";
		}
		return s;
	}
	
	private class ListenerComboBox implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			btnVer.setEnabled(false);
			jftfFechaIda.repaint();
			jftfFechaVuelta.repaint();
			jftfFechaIda.setEnabled(true);
			boolean idayvuelta =(rdbtnIdaYVuelta.isSelected())? true : false ;
			if (!idayvuelta)
				jftfFechaVuelta.setEnabled(false);
			btnReserva.setEnabled(false);
		}
		
		
	}
	
	private class TablaListenerMostrarInfo implements MouseListener{
		
		private JTable tablaLocal;
		
		public TablaListenerMostrarInfo(JTable tablaLocal) {
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
			lblStatus.setText("Mostrando vuelos");
		}

	}
	
	private class RadioButtonListener implements ItemListener{

		public void itemStateChanged(ItemEvent e) {
		    if (e.getStateChange() == ItemEvent.SELECTED) {
		        // Your selected code here.
		    	jftfFechaVuelta.setEnabled(false);
		    	btnVer.setEnabled(false);
		    	btnReserva.setEnabled(false);
		    }
		}
	}
	
	private class BotonReservaListener implements ActionListener{

		JButton reservaButton;
		public BotonReservaListener (JButton reservaButton) {
			this.reservaButton = reservaButton;
			
		}
		public void actionPerformed(ActionEvent arg0) {
			if (reservaButton.getText().equals("Habilitar modo reserva")) {
				JOptionPane.showMessageDialog(frame,
						"Uds esta por realizar una reserva \n"
						+ "en cualquier momento puede tocar la tecla <<ESC>> \n"
						+ "para cancelar el proceso de reservacion",
	                    "Reservas",
	                    JOptionPane.INFORMATION_MESSAGE);
				JOptionPane.showMessageDialog(frame,
						"Para comenzar el proceso de reserva \n"
						+ "seleccion el vuelo de ida (y vuelta si corresponde) \n"
						+ "y luego la clase con la que desea viajar",
	                    "Reservas",
	                    JOptionPane.INFORMATION_MESSAGE);
				performinReserva = true;
				reserva = new Reserva(rdbtnIdaYVuelta.isSelected());
				reservaButton.setText("Deshabilitar modo reserva");
				lblStatus.setText("Realizando reserva");
			} else {
				cancelarReserva();
				reservaButton.setText("Habilitar modo reserva");
			}
			
			
		}
		
	}
	

	private void cancelarReserva() {
		//aca seteo lo necesario y muestro un cartel de reserva cancelada
		if(performinReserva) {
			performinReserva = false;
			valoresIda = false;
			valoresVuelta = false;
			tablaIDA.clearSelection();
			tablaVUELTA.clearSelection();						
			lblStatus.setText("Reserva cancelada");
			Timer timer = new Timer(3000, new AbstractAction() {
			    /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
			    public void actionPerformed(ActionEvent ae) {
			        lblStatus.setText("Mostrando vuelos");
			    }
			});
			timer.setRepeats(false);
		}
		
	}
}

