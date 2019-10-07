package ui.ui_empleado;

public class Ubicacion {
	private String ciudad, estado, pais;

	public Ubicacion(String ciudad, String estado, String pais) {
		super();
		this.ciudad = ciudad;
		this.estado = estado;
		this.pais = pais;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	@Override
	public String toString() {
		return ciudad + ", " + estado + ", " + pais;
	}

}
