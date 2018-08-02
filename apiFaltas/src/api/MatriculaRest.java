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

import domain.Matricula;
import domain.Modulo;
import utils.Authorization;
import utils.HibernateProxyTypeAdapter;
import utils.HibernateUtil;

@Path("/matriculas")
public class MatriculaRest {

	Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create();
	private String hqlGetList = "FROM Matricula WHERE baja is null";
	private String hqlGet = "FROM Matricula WHERE idMatricula = :idMatricula AND baja is null";
	private String hqlUpdate = "UPDATE Matricula SET alumno = :newAlumno, modulo = :newModulo WHERE idMatricula = :idMatricula AND baja is null";
	private String hqlDelete = "UPDATE Matricula SET baja = :newBaja WHERE idMatricula = :idMatricula AND baja is null";
	private String hqlModulosGet = "FROM Matricula WHERE modulo = :newModulo AND baja is null";
	private String hqlGetModulo = "FROM Modulo WHERE idModulo = :idModulo and baja is null";


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMatricula(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
			@PathParam("id") String idMatricula) {
		if (Authorization.isAuthorized(token)) {
			List<?> listaMatriculas = null;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				listaMatriculas = session.createQuery(hqlGetList).list();
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

			return Response.status(200).entity(gson.toJson(listaMatriculas)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/{idMatricula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMatriculaId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
			@PathParam("idMatricula") String idMatricula) {
		if (Authorization.isAuthorized(token)) {
			Matricula matricula = null;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				matricula = (Matricula) session.createQuery(hqlGet)
						.setParameter("idMatricula", Integer.parseInt(idMatricula)).getSingleResult();
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

			if (matricula != null) {
				return Response.status(200).entity(gson.toJson(matricula)).build();
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
	public Response postMatricula(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, String json) {

		if (Authorization.isAuthorized(token)) {
			Matricula matricula = gson.fromJson(json, Matricula.class);
			Session session = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(matricula);
				session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}
			return Response.status(201).entity(gson.toJson(matricula)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	@PUT
	@Path("/{idMatricula}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putMatricula(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
			@PathParam("idMatricula") String idMatricula, String json) {
		if (Authorization.isAuthorized(token)) {
			Matricula matriculaRecibida = gson.fromJson(json, Matricula.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Matricula matriculaRecuperado = (Matricula) session.createQuery(hqlGet)
						.setParameter("idMatricula", Integer.parseInt(idMatricula)).getSingleResult();

				if (matriculaRecibida.getAlumno() == null) {
					matriculaRecibida.setAlumno(matriculaRecuperado.getAlumno());
				}

				if (matriculaRecibida.getModulo() == null) {
					matriculaRecibida.setModulo(matriculaRecuperado.getModulo());
				}

				session.createQuery(hqlUpdate).setParameter("newAlumno", matriculaRecibida.getAlumno())
						.setParameter("newModulo", matriculaRecibida.getModulo())
						.setParameter("idMatricula", Integer.parseInt(idMatricula)).executeUpdate();

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

			return Response.status(200).entity(gson.toJson(matriculaRecibida)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@DELETE
	@Path("/{idMatricula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAMatricula(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
			@PathParam("idMatricula") String idMatricula) {
		if (Authorization.isAuthorized(token)) {
			Matricula matricula = null;
			boolean validado = false;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				matricula = (Matricula) session.createQuery(hqlGet)
						.setParameter("idMatricula", Integer.parseInt(idMatricula)).getSingleResult();

				if (matricula != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date dropDate = format.parse(LocalDate.now().toString());

					matricula.setBaja(dropDate);

					session.createQuery(hqlDelete).setParameter("idMatricula", Integer.parseInt(idMatricula))
							.setParameter("newBaja", matricula.getBaja()).executeUpdate();
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
				return Response.status(201).entity(gson.toJson(matricula)).build();
			} else {
				return Response.status(401).build();
			}

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/modulo/{idModulo}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMatriculaModulosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
			@PathParam("idModulo") String idModulo) {
		if (Authorization.isAuthorized(token)) {
			List<?> listaMatriculas = null;
			Session session = null;
			Modulo modulo = null;
			
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				
				modulo = (Modulo) session.createQuery(hqlGetModulo)
						.setParameter("idModulo", Integer.parseInt(idModulo)).getSingleResult();
				
				listaMatriculas = session.createQuery(hqlModulosGet)
						.setParameter("newModulo", modulo).list(); 
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

			return Response.status(200).entity(gson.toJson(listaMatriculas)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
	
	/*INSERT INTO faltas.matricula (id_modulo, id_alumno) VALUES (4,4); CONSULTA PARA BBDD*/
}
