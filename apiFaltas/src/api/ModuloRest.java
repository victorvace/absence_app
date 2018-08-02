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

import domain.Modulo;
import utils.Authorization;
import utils.HibernateProxyTypeAdapter;
import utils.HibernateUtil;

@Path("/modulos")
public class ModuloRest {
	Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create();
	private String hqlGetList = "FROM Modulo WHERE baja is null";
	private String hqlGet = "FROM Modulo WHERE idModulo = :id and baja is null";
	private String hqlUpdate = "UPDATE Modulo SET curso = :newCurso, usuario = :newUsuario, nombre = :newNombre, siglas = :newSiglas WHERE idModulo = :id AND baja is null";
	private String hqlBaja = "UPDATE Modulo SET  baja = :newBaja WHERE idModulo = :id AND baja is null";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getModulos(@HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

		if (Authorization.isAuthorized(token)) {
			List<?> listaModulos = null;
			Session session = null;

			try {

				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				listaModulos = session.createQuery(hqlGetList).list();
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
			return Response.status(200).entity(gson.toJson(listaModulos)).build();

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getModulosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {

		if (Authorization.isAuthorized(token)) {
			Modulo modulo = null;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				modulo = (Modulo) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id))
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

			if (modulo != null) {
				return Response.status(200).entity(gson.toJson(modulo)).build();
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
	public Response postModulos(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, String json) {

		if (Authorization.isAuthorized(token)) {
			Modulo modulo = gson.fromJson(json, Modulo.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				session.save(modulo);
				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) {
					session.close();
				}
				return Response.status(500).build();
			}
			return Response.status(201).entity(gson.toJson(modulo)).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putModulosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id,
			String json) {

		if (Authorization.isAuthorized(token)) {
			Modulo modulorecibido = gson.fromJson(json, Modulo.class);
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				Modulo moduloRecuperado = (Modulo) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id))
						.getSingleResult();

				// Comparacion curso
				if (modulorecibido.getCurso() == null) {
					modulorecibido.setCurso(moduloRecuperado.getCurso());
				}

				// Comparación Usuario
				if (modulorecibido.getUsuario() == null) {
					modulorecibido.setUsuario(moduloRecuperado.getUsuario());
				}

				// Comparacion nombre
				if (modulorecibido.getNombre() == null) {
					modulorecibido.setNombre(moduloRecuperado.getNombre());
				}

				// Comparacion siglas
				if (modulorecibido.getSiglas() == null) {
					modulorecibido.setSiglas(moduloRecuperado.getSiglas());
				}

				session.createQuery(hqlUpdate).setParameter("newCurso", modulorecibido.getCurso())
						.setParameter("newUsuario", modulorecibido.getUsuario())
						.setParameter("newNombre", modulorecibido.getNombre())
						.setParameter("newSiglas", modulorecibido.getSiglas()).setParameter("id", Integer.parseInt(id))
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

			return Response.status(200).entity(gson.toJson(modulorecibido)).build();

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteModulosId(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") String id) {
		if (Authorization.isAuthorized(token)) {
			Modulo modulo = null;
			boolean validado = false;
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				modulo = (Modulo) session.createQuery(hqlGet).setParameter("id", Integer.parseInt(id))
						.getSingleResult();

				if (modulo != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date dropDate = format.parse(LocalDate.now().toString());

					modulo.setBaja(dropDate);

					session.createQuery(hqlBaja).setParameter("id", modulo.getIdModulo())
							.setParameter("newBaja", modulo.getAlta()).executeUpdate();

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
				return Response.status(201).entity(gson.toJson(modulo)).build();
			} else {
				return Response.status(401).build();
			}

		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

}
