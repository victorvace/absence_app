package domain;
// Generated 26-abr-2018 12:02:16 by Hibernate Tools 5.2.10.Final

import java.util.Date;

/**
 * Modulo generated by hbm2java
 */
@SuppressWarnings("serial")
public class Modulo implements java.io.Serializable {

	private Integer idModulo;
	private Curso curso;
	private Usuario usuario;
	private String nombre;
	private String siglas;
	private Date alta;
	private Date baja;

	public Modulo() {
	}

	public Modulo(Curso curso, Usuario usuario, String nombre, String siglas, Date alta) {
		this.curso = curso;
		this.usuario = usuario;
		this.nombre = nombre;
		this.siglas = siglas;
		this.alta = alta;
	}

	public Modulo(Integer idModulo, Curso curso, Usuario usuario, String nombre, String siglas, Date alta, Date baja) {
		super();
		this.idModulo = idModulo;
		this.curso = curso;
		this.usuario = usuario;
		this.nombre = nombre;
		this.siglas = siglas;
		this.alta = alta;
		this.baja = baja;
	}

	public Integer getIdModulo() {
		return this.idModulo;
	}

	public void setIdModulo(Integer idModulo) {
		this.idModulo = idModulo;
	}

	public Curso getCurso() {
		return this.curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSiglas() {
		return this.siglas;
	}

	public void setSiglas(String siglas) {
		this.siglas = siglas;
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
