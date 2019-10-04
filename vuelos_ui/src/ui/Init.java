package ui;

import java.sql.SQLException;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import other.MD5;
import sql_conn.vuelos_db;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Init extends JFrame {
	
	private JButton button_admin, button_empleado, button_logon;
	private JTextField jtf_user;
	private JPasswordField jpf_password;
	private JLabel label_helptxt;
	private int modo_seleccionado = 0;
	private static int ADMIN = 1;
	private static int EMPLEADO = 2;
	
	public Init() {
		//General parameters of the graphic interface
		super("VuelosUI");
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
		jtf_user = new JTextField("Ingrese usuario/legajo...");
		jpf_password = new JPasswordField("password");
		label_helptxt = new JLabel("Seleccione operador y complete los datos");
		
		//Parameters of the graphics objects
		button_admin.setLocation(0,20);
		button_admin.setSize(150, 40);
		button_admin.addActionListener(new button_listener_userpw(this,button_admin,jtf_user,jpf_password,button_logon));
		
		button_empleado.setLocation(0,70);
		button_empleado.setSize(150, 40);
		button_empleado.addActionListener(new button_listener_userpw(this,button_empleado,jtf_user,jpf_password,button_logon));
		
		button_logon.setLocation(400,20);
		button_logon.setSize(90, 90);
		button_logon.setEnabled(false);
		button_logon.addActionListener(new button_listener_logon(this));
		
		jtf_user.setSize(250,40);
		jtf_user.setLocation(150,20);
		jtf_user.addMouseListener(new mouse_listener_clean(jtf_user,"Ingrese usuario/legajo..."));
		jtf_user.setEnabled(false);
		
		jpf_password.setSize(250,40);
		jpf_password.setLocation(150,70);
		jpf_password.addMouseListener(new mouse_listener_clean(jpf_password,"password"));
		jpf_password.setEnabled(false);
		
		label_helptxt.setSize(300,20);
		label_helptxt.setLocation(0,0);
		
		//Add graphic objects 
		this.add(button_admin);
		this.add(button_empleado);
		this.add(button_logon);
		this.add(label_helptxt);
		this.add(jtf_user);
		this.add(jpf_password);
		this.setVisible(true);
	}
	
	public static void main (String [] Args)  {
		Init ui = new Init();
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
			System.out.println("Contraseña invalida");
		
		if(modo_seleccionado == ADMIN && correcta) {
			vuelos_db.connection_Vuelos_DB(jtf_user.getText(), pw_string);
		} else if (modo_seleccionado == EMPLEADO && correcta) {
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
					System.out.println("Usuario/contraseña incorrectos");
				} else { 
					System.out.println("Usuario/contraseña correctos, ingresando sistema...");
				}
				rs.close();
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vuelos_db.close_Connection();
			
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
				user.setText("Ingrese usuario/legajo...");
				pw.setEnabled(true);
				frame.set_mode(EMPLEADO);
				logon.setEnabled(true);
			}
				
		}
			
	}
		
	private class mouse_listener_clean implements MouseListener{

		private String txt;
		private JTextComponent component;
		public mouse_listener_clean (JTextComponent component, String txt) {
			this.txt = txt;
			this.component = component;
		}
		public void mouseClicked(MouseEvent arg0) {}			
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			if(component.isEnabled() && component.getText().equals(txt)) {
				component.setText("");
				component.removeMouseListener(this);
			}
		
		}
		public void mouseReleased(MouseEvent e) {}
		
	}
}


