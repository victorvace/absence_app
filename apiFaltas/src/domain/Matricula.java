package domain;
// Generated 26-abr-2018 12:02:16 by Hibernate Tools 5.2.10.Final

import java.util.Date;

/**
 * Matricula generated by hbm2java
 */
@SuppressWarnings("serial")
public class Matricula implements java.io.Serializable {

	private Integer idMatricula;
	private Alumno alumno;
	private Modulo modulo;
	private Date alta;
	private Date baja;

	public Matricula() {
	}

	public Matricula(Alumno alumno, Modulo modulo, Date alta) {
		this.alumno = alumno;
		this.modulo = modulo;
		this.alta = alta;
	}

	public Matricula(Alumno alumno, Modulo modulo, Date alta, Date baja) {
		this.alumno = alumno;
		this.modulo = modulo;
		this.alta = alta;
		this.baja = baja;
	}

	public Integer getIdMatricula() {
		return this.idMatricula;
	}

	public void setIdMatricula(Integer idMatricula) {
		this.idMatricula = idMatricula;
	}

	public Alumno getAlumno() {
		return this.alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Modulo getModulo() {
		return this.modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
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
