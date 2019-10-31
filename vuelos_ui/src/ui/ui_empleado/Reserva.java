package ui.ui_empleado;

public class Reserva {
	protected boolean idaYVuelta;
	protected String numeroVueloIda;
	protected String fechaVueloIda;
	protected String claseVueloIda;
	protected String numeroVueloVuelta;
	protected String fechaVueloVuelta;
	protected String claseVueloVuelta;
	protected String tipoDocumento;
	protected String numeroDocumento;
	protected String legajoEmpleado;
	
	public Reserva(boolean idaYVuelta) {
		this.idaYVuelta = idaYVuelta;
		numeroVueloIda = fechaVueloIda = claseVueloIda = null;
		numeroVueloVuelta = fechaVueloVuelta = claseVueloVuelta = null;
		tipoDocumento = numeroDocumento = legajoEmpleado = null;
	}

	public boolean isIdaYVuelta() {
		return idaYVuelta;
	}

	public void setIdaYVuelta(boolean idaYVuelta) {
		this.idaYVuelta = idaYVuelta;
	}

	public String getNumeroVueloIda() {
		return numeroVueloIda;
	}

	public void setNumeroVueloIda(String numeroVueloIda) {
		this.numeroVueloIda = numeroVueloIda;
	}

	public String getFechaVueloIda() {
		return fechaVueloIda;
	}

	public void setFechaVueloIda(String fechaVueloIda) {
		this.fechaVueloIda = fechaVueloIda;
	}

	public String getClaseVueloIda() {
		return claseVueloIda;
	}

	public void setClaseVueloIda(String claseVueloIda) {
		this.claseVueloIda = claseVueloIda;
	}

	public String getNumeroVueloVuelta() {
		return numeroVueloVuelta;
	}

	public void setNumeroVueloVuelta(String numeroVueloVuelta) {
		this.numeroVueloVuelta = numeroVueloVuelta;
	}

	public String getFechaVueloVuelta() {
		return fechaVueloVuelta;
	}

	public void setFechaVueloVuelta(String fechaVueloVuelta) {
		this.fechaVueloVuelta = fechaVueloVuelta;
	}

	public String getClaseVueloVuelta() {
		return claseVueloVuelta;
	}

	public void setClaseVueloVuelta(String claseVueloVuelta) {
		this.claseVueloVuelta = claseVueloVuelta;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getLegajoEmpleado() {
		return legajoEmpleado;
	}

	public void setLegajoEmpleado(String legajoEmpleado) {
		this.legajoEmpleado = legajoEmpleado;
	}
	
	public String toString () {
		String toReturn = "";
		if (!idaYVuelta) {
			toReturn +="call reservar_vuelo_ida(" ; 
		} else {
			toReturn +="call reservar_vuelo_ida_vuelta(" ; 
		}				
		toReturn+= this.getNumeroVueloIda() + ",'" + this.getFechaVueloIda() +  "','" + this.getClaseVueloIda() + "',";		
		if (idaYVuelta) {
			toReturn+= this.getNumeroVueloVuelta() + ",'" + this.getFechaVueloVuelta() +  "','" + this.getClaseVueloVuelta() + "',";
		}
		toReturn+= "'" + this.getTipoDocumento() + "'," + this.getNumeroDocumento()+ "," + this.getLegajoEmpleado() + ");";		
		return toReturn;		
	}
	
}
