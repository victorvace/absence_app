package com.absences.victor.absences.domains;
// Generated 26-abr-2018 12:02:16 by Hibernate Tools 5.2.10.Final

import java.util.Date;

/**
 * Usuario generated by hbm2java
 */
@SuppressWarnings("serial")
public class Usuario implements java.io.Serializable {

	private Integer idUsuario;
	private String usuario;
	private String email;
	private String contrasena;
	private int admin;
	private Date alta;
	private Date baja;
	private String newPass;
	private String token;

	public Usuario() {
	}

	public Usuario(String email, String contrasena, int admin, Date alta) {
		this.email = email;
		this.contrasena = contrasena;
		this.admin = admin;
		this.alta = alta;
	}

	public Usuario(String email, String contrasena) {
		this.email = email;
		this.contrasena = contrasena;
	}

	public Usuario(String usuario, String email, String contrasena) {
		this.usuario = usuario;
		this.email = email;
		this.contrasena = contrasena;
	}

	public Usuario(Integer idUsuario, String usuario, String email, String contrasena, int admin, Date alta, Date baja,
			String newPass, String token) {
		this.idUsuario = idUsuario;
		this.usuario = usuario;
		this.email = email;
		this.contrasena = contrasena;
		this.admin = admin;
		this.alta = alta;
		this.baja = baja;
		this.newPass = newPass;
		this.token = token;
	}

	public Usuario(String usuario, String email, String contrasena, String newPass) {
		this.usuario = usuario;
		this.email = email;
		this.contrasena = contrasena;
		this.newPass = newPass;
	}

	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return this.contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public int getAdmin() {
		return this.admin;
	}

	public void setAdmin(int admin) {
		this.admin = admin;
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

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	

}
