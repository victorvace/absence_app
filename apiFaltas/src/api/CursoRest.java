package api;

import java.text.ParseException;
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

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import domain.Curso;
import utils.Authorization;
import utils.HibernateProxyTypeAdapter;
import utils.HibernateUtil;

@Path("/cursos")
public class CursoRest {
	Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create();
	private String hqlGetList = "FROM Curso WHERE baja is null";
	private String hqlGet = "FROM Curso WHERE idCurso = :id AND baja is null";
	private String hqlUpdate = "UPDATE Curso SET ciclo = :newCiclo, nombre = :newNombre WHERE idCurso = :id AND baja is null";
	private String hqlBaja = "UPDATE Curso SET  baja = :newBaja WHERE idCurso = :id AND baja is null";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCursos(@HeaderParam(HttpHeaders.AUTHORIZATION) String token) {
		if (Authorization.isAuthorized(token)) {
			List<?> listaCursos = null;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				listaCursos = session.createQuery(hqlGetList).list();
				session.getTransaction().commit();
				session.close();

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

			return Response.status(200).entity(gson.toJson(listaCursos)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCursosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {
		if (Authorization.isAuthorized(token)) {
			Curso curso = null;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				curso = (Curso) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id)).getSingleResult();
				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			if (curso != null) {
				return Response.status(200).entity(gson.toJson(curso)).build();
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
	public Response postCursos(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, String json) {
		if (Authorization.isAuthorized(token)) {
			Curso curso = gson.fromJson(json, Curso.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(curso);
				session.getTransaction().commit();
				session.close();
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

			return Response.status(201).entity(gson.toJson(curso)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putCursosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id,
			String json) {
		if (Authorization.isAuthorized(token)) {
			Curso cursoRecibido = gson.fromJson(json, Curso.class);
			Session session = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Curso cursoRecuperado = (Curso) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id))
						.getSingleResult();

				// Comparacion ciclo
				if (cursoRecibido.getCiclo() == null) {
					cursoRecibido.setCiclo(cursoRecuperado.getCiclo());
				}

				// Comparacion nombre
				if (cursoRecibido.getNombre() == null) {
					cursoRecibido.setNombre(cursoRecuperado.getNombre());
				}

				session.createQuery(hqlUpdate).setParameter("newCiclo", cursoRecibido.getCiclo())
						.setParameter("newNombre", cursoRecibido.getNombre()).setParameter("id", Integer.parseInt(id))
						.executeUpdate();

				session.getTransaction().commit();
				session.close();

			} catch (HibernateException e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			} catch (NumberFormatException e) {
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

			return Response.status(200).entity(gson.toJson(cursoRecibido)).build();

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCursosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {
		if (Authorization.isAuthorized(token)) {
			Curso curso = null;
			boolean validado = false;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				curso = (Curso) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id)).getSingleResult();

				if (curso != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date dropDate = format.parse(LocalDate.now().toString());

					curso.setBaja(dropDate);

					session.createQuery(hqlBaja).setParameter("id", curso.getIdCurso())
							.setParameter("newBaja", curso.getBaja()).executeUpdate();
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
			} catch (NumberFormatException e) {
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
				return Response.status(201).entity(gson.toJson(curso)).build();
			} else {
				return Response.status(401).build();
			}

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
}
