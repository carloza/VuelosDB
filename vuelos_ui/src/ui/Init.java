package ui;

import java.sql.SQLException;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import other.MD5;
import sql_conn.vuelos_db;
import ui.ui_admin.Admin;
import ui.ui_empleado.Consulta;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Init extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JButton button_admin, button_empleado, button_logon;
	private JTextField jtf_user;
	private JPasswordField jpf_password;
	private JLabel label_helptxt;
	private int modo_seleccionado = 1;
	private static int ADMIN = 1;
	private static int EMPLEADO = 2;
	
	public Init() {
		//General parameters of the graphic interface
		super("Vuelos UI");
		this.setSize(495,150);
		this.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		//Creation of graphics objects
		button_admin = new JButton ("Administrador");
		button_empleado = new JButton ("Empleado");
		button_logon = new JButton ("Ingresar");
		jtf_user = new JTextField("Legajo...");
		jpf_password = new JPasswordField("password");
		label_helptxt = new JLabel("Seleccione tipo de operador, complete los datos e ingrese (o presione tecla enter)");
		
		//Parameters of the graphics objects
		label_helptxt.setSize(495,20);
		label_helptxt.setLocation(3,0);
		
		button_admin.setLocation(2,20);
		button_admin.setSize(145, 40);
		button_admin.addActionListener(new button_listener_userpw(this,button_admin,jtf_user,jpf_password,button_logon));
		
		button_empleado.setLocation(2,70);
		button_empleado.setSize(145, 40);
		button_empleado.addActionListener(new button_listener_userpw(this,button_empleado,jtf_user,jpf_password,button_logon));
		
		button_logon.setLocation(401,20);
		button_logon.setSize(86, 90);
		button_logon.setEnabled(false);
		button_logon.addActionListener(new button_listener_logon(this));
		
		jtf_user.setSize(250,40);
		jtf_user.setLocation(150,20);
		jtf_user.addFocusListener(new listener_clean(jtf_user,"Legajo..."));
		jtf_user.setEnabled(false);
		jtf_user.addKeyListener(new button_enter_listener());	
		
		jpf_password.setSize(250,40);
		jpf_password.setLocation(150,70);
		jpf_password.addFocusListener(new listener_clean(jpf_password,"password"));
		jpf_password.setEnabled(false);
		jpf_password.addKeyListener(new button_enter_listener());
		
		//Add graphic objects 
		this.add(button_admin);
		this.add(button_empleado);
		this.add(button_logon);
		this.add(label_helptxt);
		this.add(jtf_user);
		this.add(jpf_password);
	}
	
	public static void main (String [] Args)  {
		Init ui = new Init();		
		ui.setVisible(true);
	}
	
	public void set_mode(int mode) {
		if (mode == ADMIN) {
			modo_seleccionado = mode;
		} else if (mode == EMPLEADO) {
			modo_seleccionado = mode;
		}
	}
	
	public void login_to_db() {
		String pw_string = null;
		char [] pw = jpf_password.getPassword();
		boolean correcta = true;
		for (int i = 0; i < pw.length; i++) {
            char c = pw[i];
            if (!Character.isLetterOrDigit(c)) 
            	correcta = false;      
        }
		
		if (correcta) 
			pw_string = new String(pw);
		else 
			label_helptxt.setText("Reingrese una contrase�a valida");
		
		if(modo_seleccionado == ADMIN && correcta) {
			try {
				vuelos_db.connection_Vuelos_DB(jtf_user.getText(), pw_string);
				Admin ad = new Admin();
				ad.setVisible(true);
				this.dispose();
			} catch (SQLException e) {
				label_helptxt.setText("Usuario o contrase�a invalida, reingrese");
				label_helptxt.setForeground(Color.red);
			}
			
		} else if (modo_seleccionado == EMPLEADO && correcta) {
			try{
				vuelos_db.connection_Vuelos_DB("empleado", "empleado");
				java.sql.Statement stmt;
				try {
					stmt = vuelos_db.get_Connection_Vuelos_DB().createStatement();
					String sql = "SELECT legajo, password FROM empleados";
					java.sql.ResultSet rs = stmt.executeQuery(sql);
					boolean encontro = false;
					while(rs.next()) {
						String legajo = rs.getString("legajo");
						String password = rs.getString("password");
						if (legajo.equals(jtf_user.getText()) && password.equals(MD5.getMd5(pw_string))) {
							encontro = true;
							break;
						}
					}
					if (!encontro) {
						vuelos_db.close_Connection();
						label_helptxt.setText("Nro de legajo o contrase�a incorrectos");
						label_helptxt.setForeground(Color.red);
					} else { 
						label_helptxt.setText("Conectado");
						label_helptxt.setForeground(Color.black);
						Consulta.Iniciar(jtf_user.getText());
						this.dispose();
					}
					rs.close();
					stmt.close();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}catch (SQLException e) {
				label_helptxt.setText("Error al conectar con la BD");
			}
		
			
		}
	}

	private class button_listener_logon implements ActionListener{

		private Init frame;		
		
		public button_listener_logon(Init frame) {
			this.frame = frame;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			frame.login_to_db();
		}
			
	}
	
	private class button_enter_listener extends KeyAdapter {
	    public void keyTyped(KeyEvent e) {
	        char c = e.getKeyChar();
	        if (c == KeyEvent.VK_ENTER) {
	        	login_to_db();
	        }
	    }
	}
	
	private class button_listener_userpw implements ActionListener{

		private JButton component,logon;
		private Init frame;
		private JTextComponent user,pw;
		
		
		public button_listener_userpw(Init frame, JButton component,JTextComponent user, JTextComponent pw, JButton logon) {
			this.logon = logon;
			this.frame = frame;
			this.component = component;
			this.user = user;
			this.pw = pw;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			if(component.getText().equals("Administrador")){
				user.setEnabled(false);
				user.setText("admin");
				pw.setEnabled(true);
				frame.set_mode(ADMIN);
				logon.setEnabled(true);
				
			} else if (component.getText().equals("Empleado")){
				user.setEnabled(true);
				user.setText("Legajo...");
				pw.setEnabled(true);
				frame.set_mode(EMPLEADO);
				logon.setEnabled(true);
			}
				
		}
			
	}
		
	private class listener_clean implements FocusListener{

		private String txt;
		private JTextComponent component;
		public listener_clean (JTextComponent component, String txt) {
			this.txt = txt;
			this.component = component;
		}

		public void focusGained(FocusEvent arg0) {
			if(component.isEnabled() && component.getText().equals(txt)) {
				component.setText("");
			}
		}

		public void focusLost(FocusEvent arg0) {}
		
	}
}


