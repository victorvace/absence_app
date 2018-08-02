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

import domain.Alumno;
import domain.Falta;
import domain.Modulo;
import utils.Authorization;
import utils.HibernateProxyTypeAdapter;
import utils.HibernateUtil;

@Path("/faltas")
public class FaltaRest {

	Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create();
	private String hqlGetList = "FROM Falta WHERE baja is null";
	private String hqlGet = "FROM Falta WHERE idFalta = :idFalta AND baja is null";
	private String hqlUpdate = "UPDATE Falta SET fecha = :newFecha, alumno = :newAlumno, horario = :newHorario, modulo = :newModulo WHERE idFalta = :idFalta AND baja is null";
	private String hqlDelete = "UPDATE Falta SET baja = :newBaja WHERE idFalta = :idFalta AND baja is null";
	private String hqlGetModulo = "FROM Modulo WHERE idModulo = :id AND baja is null";
	private String hqlGetAlumno = "FROM Alumno WHERE idAlumno = :id AND baja is null";
	private String hqlGetListModuloAlumno = "FROM Falta WHERE modulo = :modulo AND alumno = :alumno AND baja is null";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFaltas(@HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

		if (Authorization.isAuthorized(token)) {
			List<?> listaFaltas = null;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				listaFaltas = session.createQuery(hqlGetList).list();
				session.getTransaction().commit();
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
				Response.status(500).build();
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			return Response.status(200).entity(gson.toJson(listaFaltas)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/{idFalta}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFalta(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
			@PathParam("idFalta") String idFalta) {
		if (Authorization.isAuthorized(token)) {
			Falta falta = null;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				falta = (Falta) session.createQuery(hqlGet).setParameter("idFalta", Integer.parseInt(idFalta))
						.getSingleResult();
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

			if (falta != null) {
				return Response.status(200).entity(gson.toJson(falta)).build();
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
	public Response postFalta(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, String json) {

		if (Authorization.isAuthorized(token)) {
			Falta falta = gson.fromJson(json, Falta.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(falta);
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
			return Response.status(201).entity(gson.toJson(falta)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	@PUT
	@Path("/{idFalta}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putFalta(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("idFalta") String idFalta,
			String json) {
		if (Authorization.isAuthorized(token)) {
			Falta faltaRecibida = gson.fromJson(json, Falta.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Falta faltaRecuperada = (Falta) session.createQuery(hqlGet)
						.setParameter("idFalta", Integer.parseInt(idFalta)).getSingleResult();

				if (faltaRecibida.getAlumno() == null) {
					faltaRecibida.setAlumno(faltaRecuperada.getAlumno());
				}

				if (faltaRecibida.getHorario() == null) {
					faltaRecibida.setHorario(faltaRecuperada.getHorario());
				}

				if (faltaRecibida.getModulo() == null) {
					faltaRecibida.setModulo(faltaRecuperada.getModulo());
				}

				if (faltaRecibida.getFecha() == null) {
					faltaRecibida.setFecha(faltaRecuperada.getFecha());
				}

				session.createQuery(hqlUpdate).setParameter("newAlumno", faltaRecibida.getAlumno())
						.setParameter("newHorario", faltaRecibida.getHorario())
						.setParameter("newModulo", faltaRecibida.getModulo())
						.setParameter("newFecha", faltaRecibida.getFecha())
						.setParameter("idFalta", Integer.parseInt(idFalta)).executeUpdate();

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

			return Response.status(200).entity(gson.toJson(faltaRecibida)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@DELETE
	@Path("/{idFalta}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteFalta(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
			@PathParam("idFalta") String idFalta) {
		if (Authorization.isAuthorized(token)) {
			Falta falta = null;
			boolean validado = false;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				falta = (Falta) session.createQuery(hqlGet).setParameter("idFalta", Integer.parseInt(idFalta))
						.getSingleResult();

				if (falta != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date dropDate = format.parse(LocalDate.now().toString());

					falta.setBaja(dropDate);

					session.createQuery(hqlDelete).setParameter("idFalta", Integer.parseInt(idFalta))
							.setParameter("newBaja", falta.getBaja()).executeUpdate();
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
				return Response.status(201).entity(gson.toJson(falta)).build();
			} else {
				return Response.status(401).build();
			}

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/modulo/{idModulo}/alumno/{idAlumno}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFaltasModuloAlumno(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
			@PathParam("idModulo") String idModulo, @PathParam("idAlumno") String idAlumno) {
		if (Authorization.isAuthorized(token)) {
			List<?> listaFaltas = null;
			Session session = null;
			Modulo modulo = null;
			Alumno alumno = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();

				modulo = (Modulo) session.createQuery(hqlGetModulo)
						.setParameter("id", Integer.parseInt(idModulo)).getSingleResult();

				alumno = (Alumno) session.createQuery(hqlGetAlumno)
						.setParameter("id", Integer.parseInt(idAlumno)).getSingleResult();

				listaFaltas = session.createQuery(hqlGetListModuloAlumno).setParameter("modulo", modulo)
						.setParameter("alumno", alumno).list();

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

			return Response.status(200).entity(gson.toJson(listaFaltas)).build();

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
}
