package domain;
// Generated 26-abr-2018 12:02:16 by Hibernate Tools 5.2.10.Final

import java.util.Date;

/**
 * Horario generated by hbm2java
 */
@SuppressWarnings("serial")
public class Horario implements java.io.Serializable {

	private Integer idHorario;
	private String inicio;
	private String fin;
	private Date alta;
	private Date baja;

	public Horario() {
	}

	public Horario(String inicio, String fin, Date alta) {
		this.inicio = inicio;
		this.fin = fin;
		this.alta = alta;
	}

	public Horario(Integer idHorario, String inicio, String fin, Date alta, Date baja) {
		super();
		this.idHorario = idHorario;
		this.inicio = inicio;
		this.fin = fin;
		this.alta = alta;
		this.baja = baja;
	}

	public Integer getIdHorario() {
		return this.idHorario;
	}

	public void setIdHorario(Integer idHorario) {
		this.idHorario = idHorario;
	}

	public String getInicio() {
		return this.inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFin() {
		return this.fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	public Date getAlta() {
		return this.alta;
	}

	public void setAlta(Date alta) {
		this.alta = alta;
	}

	public Date getBaja() {
		return this.baja;
	}

	public void setBaja(Date baja) {
		this.baja = baja;
	}

}
