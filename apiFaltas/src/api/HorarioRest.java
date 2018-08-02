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

import domain.Horario;
import utils.Authorization;
import utils.HibernateProxyTypeAdapter;
import utils.HibernateUtil;

@Path("/horarios")
public class HorarioRest {

	Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create();
	private String hqlGetList = "FROM Horario WHERE baja is null";
	private String hqlGet = "FROM Horario WHERE idHorario = :id and baja is null";
	private String hqlUpdate = "UPDATE Horario SET inicio = :newInicio, fin = :newFin WHERE idHorario = :id AND baja is null";
	private String hqlBaja = "UPDATE Horario SET  baja = :newBaja WHERE idHorario = :id AND baja is null";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHorarios(@HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

		if (Authorization.isAuthorized(token)) {
			List<?> listaHorarios = null;
			Session session = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				listaHorarios = session.createQuery(hqlGetList).list();
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

			return Response.status(200).entity(gson.toJson(listaHorarios)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHorariosID(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {
		if (Authorization.isAuthorized(token)) {
			Horario horario = null;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				horario = (Horario) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id))
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

			return Response.status(201).entity(gson.toJson(horario)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postHorario(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, String json) {

		if (Authorization.isAuthorized(token)) {
			Horario horario = gson.fromJson(json, Horario.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(horario);
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
			return Response.status(200).entity(gson.toJson(horario)).build();

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putHorarios(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id,
			String json) {

		if (Authorization.isAuthorized(token)) {
			Horario horarioRecibido = gson.fromJson(json, Horario.class);
			Session session = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Horario horarioRecuperado = (Horario) session.createQuery(hqlGet)
						.setParameter("id", Integer.parseInt(id)).getSingleResult();

				if (horarioRecibido.getInicio() == null) {
					horarioRecibido.setInicio(horarioRecuperado.getInicio());
				}

				if (horarioRecibido.getFin() == null) {
					horarioRecibido.setFin(horarioRecuperado.getFin());
				}

				session.createQuery(hqlUpdate).setParameter("newInicio", horarioRecibido.getInicio())
						.setParameter("newFin", horarioRecibido.getFin()).setParameter("id", Integer.parseInt(id))
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
			return Response.status(200).entity(gson.toJson(horarioRecibido)).build();

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteHorarios(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {
		if (Authorization.isAuthorized(token)) {
			Horario horario = null;
			boolean validado = false;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				horario = (Horario) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id))
						.getSingleResult();

				if (horario != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date dropDate = format.parse(LocalDate.now().toString());

					horario.setBaja(dropDate);

					session.createQuery(hqlBaja).setParameter("id", horario.getIdHorario())
							.setParameter("newBaja", horario.getBaja()).executeUpdate();
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
				return Response.status(201).entity(gson.toJson(horario)).build();
			} else {
				return Response.status(401).build();
			}
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

}
