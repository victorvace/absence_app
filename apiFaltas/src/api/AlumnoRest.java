package api;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import domain.Alumno;
//import domain.Matricula;
import utils.Authorization;
import utils.HibernateProxyTypeAdapter;
import utils.HibernateUtil;

@Path("/alumnos")
public class AlumnoRest {
	// Gson gson = new Gson();
	Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create();
	private String hqlGetList = "FROM Alumno WHERE baja is null";
	private String hqlGet = "FROM Alumno WHERE idAlumno = :id and baja is null";
	private String hqlUpdate = "UPDATE Alumno SET nombre = :newNombre, apellidos = :newApellidos, telefono = :newTelefono, dni = :newDni, correo = :newCorreo WHERE idAlumno = :id AND baja is null";
	private String hqlBaja = "UPDATE Alumno SET  baja = :fechaBaja WHERE idAlumno = :id AND baja is null";
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlumnos(@HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

		if (Authorization.isAuthorized(token)) {
			List<?> listaAlumnos = null;
			Session session = null;

			try {

				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				listaAlumnos = session.createQuery(hqlGetList).list();
				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			return Response.status(200).entity(gson.toJson(listaAlumnos)).build();

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlumnosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {

		if (Authorization.isAuthorized(token)) {
			Alumno alumno = null;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				alumno = (Alumno) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id))
						.getSingleResult();
				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			if (alumno != null) {
				return Response.status(200).entity(gson.toJson(alumno)).build();
			} else {
				return Response.status(404).build();
			}

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postAlumnos(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, String json) {

		if (Authorization.isAuthorized(token)) {
			Alumno alumno = gson.fromJson(json, Alumno.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(alumno);
				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}
			return Response.status(201).entity(gson.toJson(alumno)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	// Con este post se hacen las matriculas de los alumnos y de los m�dulos. Es
	// necesario que los m�dulos y los alumnos existan con anterioridad.

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putAlumnosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id,
			String json) {

		if (Authorization.isAuthorized(token)) {
			Alumno alumnoRecibido = gson.fromJson(json, Alumno.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Alumno alumnoRecuperado = (Alumno) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id))
						.getSingleResult();

				// Comparacion nombre
				if (alumnoRecibido.getNombre() == null) {
					alumnoRecibido.setNombre(alumnoRecuperado.getNombre());
				}

				// Comparacion apellidos
				if (alumnoRecibido.getApellidos() == null) {
					alumnoRecibido.setApellidos(alumnoRecuperado.getApellidos());
				}

				// Comparacion telefono
				if (alumnoRecibido.getTelefono() == null) {
					alumnoRecibido.setTelefono(alumnoRecuperado.getTelefono());
				}

				// Comparacion dni
				if (alumnoRecibido.getDni() == null) {
					alumnoRecibido.setDni(alumnoRecuperado.getDni());
				}

				// Comparacion correo
				if (alumnoRecibido.getCorreo() == null) {
					alumnoRecibido.setCorreo(alumnoRecuperado.getCorreo());
				}
				

				// Actualizacion de datos
				session.createQuery(hqlUpdate).setParameter("newNombre", alumnoRecibido.getNombre())
						.setParameter("newApellidos", alumnoRecibido.getApellidos())
						.setParameter("newTelefono", alumnoRecibido.getTelefono())
						.setParameter("newDni", alumnoRecibido.getDni())
						.setParameter("newCorreo", alumnoRecibido.getCorreo())
						.setParameter("id", Integer.parseInt(id)).executeUpdate();

				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			return Response.status(200).entity(gson.toJson(alumnoRecibido)).build();

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAlumnosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {
		if (Authorization.isAuthorized(token)) {
			Alumno alumno = null;
			boolean validado = false;
			Session session = null;

			try {

				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				alumno = (Alumno) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id))
						.getSingleResult();

				if (alumno != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date dropDate = format.parse(LocalDate.now().toString());

					alumno.setBaja(dropDate);

					session.createQuery(hqlBaja).setParameter("id", alumno.getIdAlumno())
							.setParameter("fechaBaja", alumno.getBaja()).executeUpdate();

					validado = true;
				}

				session.getTransaction().commit();
				session.close();

				if (validado) {
					return Response.status(201).entity(gson.toJson(alumno)).build();
				} else {
					return Response.status(401).build();
				}

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

}
