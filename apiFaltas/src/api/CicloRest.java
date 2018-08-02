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

import domain.Ciclo;
import utils.Authorization;
import utils.HibernateProxyTypeAdapter;
import utils.HibernateUtil;

@Path("/ciclos")
public class CicloRest {
	// Gson gson = new Gson();
	Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create();
	private String hqlGetList = "FROM Ciclo WHERE baja is null";
	private String hqlGet = "FROM Ciclo WHERE idCiclo = :id and baja is null";
	private String hqlUpdate = "UPDATE Ciclo SET nombre = :newNombre, siglas = :newSiglas  WHERE idCiclo = :id AND baja is null";
	private String hqlBaja = "UPDATE Ciclo SET  baja = :fechaBaja WHERE idCiclo = :id AND baja is null";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCiclos(@HeaderParam(HttpHeaders.AUTHORIZATION) String token) {
		if (Authorization.isAuthorized(token)) {
			List<?> listaCiclos = null;
			Session session = null;
			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				listaCiclos = session.createQuery(hqlGetList).list();
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

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCiclosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {
		if (Authorization.isAuthorized(token)) {
			Ciclo ciclo = null;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				ciclo = (Ciclo) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id)).getSingleResult();
				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			if (ciclo != null) {
				return Response.status(200).entity(gson.toJson(ciclo)).build();
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
	public Response postCiclos(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, String json) {

		if (Authorization.isAuthorized(token)) {
			Ciclo ciclo = gson.fromJson(json, Ciclo.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(ciclo);
				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			return Response.status(201).entity(gson.toJson(ciclo)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putCiclosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id,
			String json) {

		if (Authorization.isAuthorized(token)) {
			Ciclo cicloRecibido = gson.fromJson(json, Ciclo.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Ciclo cicloRecuperado = (Ciclo) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id))
						.getSingleResult();

				// Comparación nombre
				if (cicloRecibido.getNombre() == null) {
					cicloRecibido.setNombre(cicloRecuperado.getNombre());
				}

				// Comparación siglas
				if (cicloRecibido.getSiglas() == null) {
					cicloRecibido.setSiglas(cicloRecuperado.getSiglas());
				}

				// Actualización de datos
				session.createQuery(hqlUpdate).setParameter("newNombre", cicloRecibido.getNombre())
						.setParameter("newSiglas", cicloRecibido.getSiglas()).setParameter("id", Integer.parseInt(id))
						.executeUpdate();

				session.getTransaction().commit();
				session.close();

				return Response.status(200).entity(gson.toJson(cicloRecibido)).build();

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

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCiclosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {
		if (Authorization.isAuthorized(token)) {
			Ciclo ciclo = null;
			boolean validado = false;
			Session session = null;
			try {

				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				ciclo = (Ciclo) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id)).getSingleResult();

				if (ciclo != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date dropDate = format.parse(LocalDate.now().toString());

					ciclo.setBaja(dropDate);

					session.createQuery(hqlBaja).setParameter("id", Integer.parseInt(id))
							.setParameter("fechaBaja", ciclo.getBaja()).executeUpdate();

					validado = true;
				}

				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}

			if (validado) {
				return Response.status(201).entity(gson.toJson(ciclo)).build();
			} else {
				return Response.status(401).build();
			}

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

}
