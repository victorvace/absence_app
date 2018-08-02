package api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import domain.Usuario;
import utils.Authorization;
import utils.Constants;
import utils.HibernateProxyTypeAdapter;
import utils.HibernateUtil;

@Path("/usuarios")
public class Login {
	Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create();
	private String hql = "FROM Usuario WHERE email = :email AND contrasena = :contrasena AND baja is null";
	private String hqlupdate = "UPDATE Usuario SET contrasena = :newPass where idUsuario = :id AND baja is null";
	private String hqlBaja = "UPDATE Usuario SET baja = :fechaBaja where idUsuario = :id AND baja is null";
	private String hqlCiclos = "FROM Modulo WHERE usuario = :user AND baja is null";
	private String hqlUsuarioID = "FROM Usuario WHERE idUsuario = :id AND baja is null";

	// Login
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(String json) {

		Usuario user = gson.fromJson(json, Usuario.class);
		boolean validado = false;
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Usuario userExist = (Usuario) session.createQuery(hql).setParameter("email", user.getEmail())
					.setParameter("contrasena", user.getContrasena()).getSingleResult();

			if (userExist != null) {
				user.setIdUsuario(userExist.getIdUsuario());
				user.setUsuario(userExist.getUsuario());
				user.setEmail(userExist.getEmail());
				user.setToken(generateToken(userExist.getIdUsuario()));
				user.setAdmin(userExist.getAdmin());
				user.setAlta(userExist.getAlta());
				validado = true;
			}

			session.getTransaction().commit();
			session.close();
			
			user.setContrasena("");

		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.close();
			}
			System.out.println("El usuario no existe o se ha dado de baja");
			return Response.status(500).build();
		}

		if (validado) {
			return Response.status(201).entity(gson.toJson(user)).build();
		} else {
			return Response.status(401).build();
		}

	}

	// Registro
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(String json) {

		Usuario user = gson.fromJson(json, Usuario.class);

		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Usuario userExist = (Usuario) session.createQuery(hql).setParameter("email", user.getEmail())
					.setParameter("contrasena", user.getContrasena()).uniqueResult();
			if (userExist == null) {
				session.save(user);
			} else {
				System.out.println("El usuario ya existe y no está de baja.");
				return Response.status(500).build();
			}
			session.getTransaction().commit();
			session.close();

			user.setContrasena("");

		} catch (HibernateException e) {
			e.printStackTrace();
			if (session != null) {
				session.close();
			}
			return Response.status(500).build();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.close();
			}
			return Response.status(500).build();
		}

		return Response.status(201).entity(gson.toJson(user)).build();

	}

	// Modification (pass)
	@POST
	@Path("/mod")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modification(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, String json) {

		if (Authorization.isAuthorized(token)) {

			Usuario user = gson.fromJson(json, Usuario.class);
			boolean validado = false;
			Usuario userExist;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				userExist = (Usuario) session.createQuery(hql).setParameter("email", user.getEmail())
						.setParameter("contrasena", user.getContrasena()).uniqueResult();

				if (userExist != null) {
					System.out.println(userExist.getContrasena());
					System.out.println(user.getNewPass());
					System.out.println(userExist.getIdUsuario());

					userExist.setContrasena(user.getNewPass());

					System.out.println(userExist.getContrasena());
					session.createQuery(hqlupdate).setParameter("newPass", userExist.getContrasena())
							.setParameter("id", userExist.getIdUsuario()).executeUpdate();

					System.out.println(userExist.getIdUsuario());
					System.out.println(userExist.getContrasena());
					validado = true;
					System.out.println(validado);
					session.getTransaction().commit();
					session.close();
				}

			} catch (HibernateException e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			if (validado) {
				return Response.status(201).entity(gson.toJson(userExist)).build();
			} else {
				System.out.println("WTF");
				return Response.status(401).build();
			}

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	// Baja
	@POST
	@Path("/drop")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response baja(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, String json) {

		if (Authorization.isAuthorized(token)) {

			Usuario user = gson.fromJson(json, Usuario.class);
			boolean validado = false;
			Usuario userExist;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				userExist = (Usuario) session.createQuery(hql).setParameter("email", user.getEmail())
						.setParameter("contrasena", user.getContrasena()).getSingleResult();

				if (userExist != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date dropDate = format.parse(LocalDate.now().toString());

					user.setBaja(dropDate);

					session.createQuery(hqlBaja).setParameter("fechaBaja", user.getBaja())
							.setParameter("id", userExist.getIdUsuario()).executeUpdate();

					validado = true;
				}

				session.getTransaction().commit();
				session.close();

			} catch (HibernateException e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			} catch (ParseException e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			if (validado) {
				return Response.status(201).entity(gson.toJson(userExist)).build();
			} else {
				return Response.status(401).build();
			}

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/{id}/ciclos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ciclosUser(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {
		if (Authorization.isAuthorized(token)) {
			List<?> listaCiclos = null;
			Session session = null;
			Usuario usuario = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();

				usuario = (Usuario) session.createQuery(hqlUsuarioID).setParameter("id", Integer.parseInt(id))
						.getSingleResult();
				
				listaCiclos = session.createQuery(hqlCiclos).setParameter("user", usuario).list();

				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			return Response.status(200).entity(gson.toJson(listaCiclos)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}
	
	private String generateToken(int id) throws Exception {
		// Formateo de fechas
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date issuedDate = format.parse(LocalDate.now().toString());
		Date expirationDate = format.parse(LocalDate.now().plusDays(5).toString()); 

		// Algoritmo con la clave elegida (no funciona)
		Algorithm algorithm = Algorithm.HMAC256(Constants.TOKEN_KEY);

		// Se crea el token
		String token = JWT.create().withSubject(String.valueOf(id)).withIssuedAt(issuedDate)
				.withExpiresAt(expirationDate).sign(algorithm);

		return token;
	}

}
