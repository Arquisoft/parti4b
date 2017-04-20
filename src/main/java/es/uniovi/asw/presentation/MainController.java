package es.uniovi.asw.presentation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uniovi.asw.conf.Factories;
import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.Commentary;
import es.uniovi.asw.model.Proposal;
import es.uniovi.asw.model.types.status.EstadosComentario;
import es.uniovi.asw.model.types.status.EstadosPropuesta;
import es.uniovi.asw.producers.KafkaProducer;
import es.uniovi.asw.model.ImprimeDatosComment;

/**
 * Acceso web
 */
@Controller
public class MainController {

	@Autowired
	private Factories factory;

	@Autowired
	private KafkaProducer kafkaProducer;

	private Citizen usuario;

	// Con este id controlo la propuesta en la que estoy cuando se navega entre
	// comentarios
	private Long idPropuesta = null;
	//
	// // Para ver las paginas entrar en localhost:8080
	// @RequestMapping(value = "/users", method = RequestMethod.GET)
	// public List<Citizen> getUsers() throws NoSuchAlgorithmException,
	// CitizenException {
	// return servicio.findAll();
	// }

	@RequestMapping("/")
	public String index() {
		// Cerramos sesion usuario a null y lo mandamos al landing
		// Aunque tambien se inicializa al principio
		usuario = null;
		kafkaProducer.send("logout", "El usuario cerro sesion correctamente"
				+ " o es la primera vez que entrea");
		return "landing";
	}

	// Se le llama al realizar login
	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam("dni") String dni,
			@RequestParam("password") String password) {
		// Para cuando la bbdd tenga contraseñas que nos conocemos
		// usuario = servicio.findLoggableUser(dni , md5(password));

		usuario = factory.getServicesFactory().getCitizenService()
				.findByDni(dni);

		if (usuario != null) {
			if (usuario.isAdmin()) {
				kafkaProducer.send("admin",
						"El administrador del sistema se ha logueado");
				return new ModelAndView("admin"); // la contraseña de admin es
			} else {
				List<Proposal> proposals = factory.getServicesFactory()
						.getProposalService()
						.findByStatus(EstadosPropuesta.EnTramite);
				kafkaProducer.send("user", "El usuario " + usuario.getNombre()
						+ " se ha logueado");
				return new ModelAndView("usuario").addObject("proposals",
						proposals);
			}
		} else {
			kafkaProducer.send("login", "Usuario o contraseña incorrectos");
			return new ModelAndView("landing").addObject("hidden", true)
					.addObject("mensaje", "Usuario o contraseña invalido.");
		}
	}

	@SuppressWarnings("unused")
	@RequestMapping(path = "/comment", method = RequestMethod.GET)
	public ModelAndView comment(@RequestParam String id) {
		if (usuario != null) {
			List<Commentary> commentaries = null;

			this.idPropuesta = Long.parseLong(id);
			List<ImprimeDatosComment> imp = null;

			if (usuario.isAdmin()) {
				commentaries = factory.getServicesFactory()
						.getCommentaryService().findByProposalId(idPropuesta);
				imp = new ArrayList<ImprimeDatosComment>();
				for (int i = 0; i < commentaries.size(); i++) {
					ImprimeDatosComment imprime = new ImprimeDatosComment();
					imprime.setContent(commentaries.get(i).getContent());
					imprime.setNombre(factory.getServicesFactory()
							.getCitizenService()
							.findById(commentaries.get(i).getCitizen().getId())
							.getNombre());
					imprime.setDate(
							commentaries.get(i).getCreationDate().toString());
					imprime.setStatus(
							commentaries.get(i).getEstado().toString());
					imprime.setIdComment(
							commentaries.get(i).getCreationDate().toString());

					imp.add(imprime);
				}

				if (commentaries != null) {
					kafkaProducer.send("admin",
							"La propuesta seleccionada tiene comentarios");
					return new ModelAndView("commentAdmin")
							.addObject("commentaries", commentaries)
							.addObject("hidden", false).addObject("id", id)
							.addObject("datos", imp);
				} else {
					kafkaProducer.send("admin",
							"La propuesta seleccionada no tiene comentarios");
					return new ModelAndView("commentAdmin")
							.addObject("hidden", true).addObject("id", id);
				}

			} else {
				commentaries = factory.getServicesFactory()
						.getCommentaryService()
						.findByProposal(Long.parseLong(id));
				imp = new ArrayList<ImprimeDatosComment>();
				for (int i = 0; i < commentaries.size(); i++) {
					ImprimeDatosComment imprime = new ImprimeDatosComment();
					imprime.setContent(commentaries.get(i).getContent());
					imprime.setNombre(factory.getServicesFactory()
							.getCitizenService()
							.findById(commentaries.get(i).getCitizen().getId())
							.getNombre());
					imprime.setDate(
							commentaries.get(i).getCreationDate().toString());

					imp.add(imprime);
				}

				if (commentaries != null) {
					kafkaProducer.send("user",
							"La propuesta seleccionada tiene comentarios");
					return new ModelAndView("comment")
							.addObject("commentaries", commentaries)
							.addObject("hidden", false).addObject("id", id)
							.addObject("datos", imp);
				} else {
					kafkaProducer.send("user",
							"La propuesta seleccionada no tiene comentarios");
					return new ModelAndView("comment").addObject("hidden", true)
							.addObject("id", id);
				}
			}
		} else {
			kafkaProducer.send("login", "No existe usuario en sesion");
			return fail();
		}
	}

	@RequestMapping(path = "/censurar", method = RequestMethod.GET)
	public ModelAndView censurar(@RequestParam("id") String idComment) {
		if (usuario != null) {
			SimpleDateFormat formato = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date fec = null;
			try {

				fec = formato.parse(idComment);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Commentary comentario = factory.getServicesFactory()
					.getCommentaryService().findByCreationDate(fec);
			comentario.setEstado(EstadosComentario.Censurado);
			factory.getServicesFactory().getCommentaryService()
					.update(comentario);

			kafkaProducer.send("admin",
					"El administrador censuro este comentario: "
							+ comentario.getContent());
			return comment(idPropuesta.toString());
		} else {
			kafkaProducer.send("login", "No existe el usuario en sesion");
			return fail();
		}
	}

	// Cuando en el menu de comments pulsamos en añadir un nuevo comentario
	@RequestMapping(path = "/crearComment", method = RequestMethod.GET)
	public ModelAndView crearComment(@RequestParam("id") String id) {
		if (usuario != null) {
			this.idPropuesta = Long.parseLong(id);
			kafkaProducer.send("user",
					"Cargando formulario para crear comentario");
			return new ModelAndView("crearComment").addObject("hidden", false);
		} else {
			kafkaProducer.send("login", "El usuario no existe en sesion");
			return fail();
		}
	}

	// Cuando guardamos el comentario
	@RequestMapping(path = "/salvarComment", method = RequestMethod.POST)
	public ModelAndView salvarComment(@RequestParam("comment") String comment) {
		if (idPropuesta != null && usuario != null) {
			System.out.println(comment + " \nid de la propuesta: "
					+ Long.toString(idPropuesta));
			// Arreglar la parte del modelo
			factory.getServicesFactory().getCommentaryService()
					.save(usuario.getId(), idPropuesta, comment);
			kafkaProducer.send("user", "El comentario se añadio correctamente");
			return comment(Long.toString(idPropuesta));

		} else {
			kafkaProducer.send("login", "El usuario no existe en sesion");
			return fail();
		}
	}

	// Aqui solo llamamos cuando queramos que vaya hacia atras, es decir,
	// nos logeamos como usuario pincha en ver comentarios y pulsa inicio
	@RequestMapping("/usuario")
	public ModelAndView backUser() {
		if (usuario != null) {
			if (usuario.isAdmin()) {
				kafkaProducer.send("admin",
						"El administrador volvio a su pagina principal");
				return new ModelAndView("admin");
			} else {
				idPropuesta = null; // cuando se pulsa en incio reseteo el id de
									// la propuesta para evitar la navegacion
									// incorrecta
				List<Proposal> proposals = factory.getServicesFactory()
						.getProposalService()
						.findByStatus(EstadosPropuesta.EnTramite);
				kafkaProducer.send("user", "El usuario " + usuario.getNombre()
						+ " volvio a su pagina principal");
				return new ModelAndView("usuario").addObject("proposals",
						proposals);
			}
		} else {
			kafkaProducer.send("login", "El usuario no existe en sesion");
			return fail();
		}
	}

	// Aqui se manda siempre que falle algo
	@RequestMapping("/fail")
	public ModelAndView fail() {
		usuario = null;
		idPropuesta = null;
		return new ModelAndView("error");
	}

	@RequestMapping("/nuevaPropuesta")
	public ModelAndView nuevaPropuesta() {
		if (usuario != null) {
			kafkaProducer.send("admin", "Sección para crear propuestas");
			return new ModelAndView("nuevaPropuesta");
		} else {
			kafkaProducer.send("admin", "Fallo al acceder a crear propuestas");
			return fail();
		}
	}

	@RequestMapping(path = "/crearPropuesta", method = RequestMethod.POST)
	public ModelAndView crearPropuesta(@RequestParam("nombre") String nombre,
			@RequestParam("contenido") String contenido) {
		if (usuario != null) {
			Proposal propuesta = new Proposal(nombre, contenido, 1000);

			factory.getServicesFactory().getProposalService().save(propuesta);
			List<Proposal> proposals = factory.getServicesFactory()
					.getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);
			kafkaProducer.send("admin", "Propuesta creada");
			return new ModelAndView("usuario").addObject("proposals",
					proposals);
		} else {
			kafkaProducer.send("admin", "Fallo al crear propuesta");
			return fail();
		}
	}

	@RequestMapping(path = "/votarPositivo", method = RequestMethod.GET)
	public ModelAndView votarPositivo(
			@RequestParam("idPropuesta") String idPropuesta) {
		if (usuario != null) {
			Proposal propuesta = factory.getServicesFactory()
					.getProposalService().findById(Long.parseLong(idPropuesta));
			if (!propuesta.positiveVote(usuario)) {
				kafkaProducer.send("user", "Ya ha votado el usuario");
				return fail();
			} else {
				factory.getServicesFactory().getVoteService()
						.save(usuario.getId(), propuesta.getId());
			}
			if (propuesta.getValoration() >= propuesta.getMinVotes()) {
				propuesta.setStatus(EstadosPropuesta.Aceptada);
			}

			List<Proposal> proposals = factory.getServicesFactory()
					.getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);
			kafkaProducer.send("user", "Voto positivo");
			return new ModelAndView("usuario").addObject("proposals",
					proposals);
		} else {
			kafkaProducer.send("user", "Error al votar positivo");
			return fail();
		}
	}

	@RequestMapping(path = "/votarNegativo", method = RequestMethod.GET)
	public ModelAndView votarNegativo(
			@RequestParam("idPropuesta") String idPropuesta) {
		if (usuario != null) {
			Proposal propuesta = factory.getServicesFactory()
					.getProposalService().findById(Long.parseLong(idPropuesta));
			if (!propuesta.negativeVote(usuario)) {
				kafkaProducer.send("user", "Ya ha votado el usuario");
				return fail();
			} else {
				factory.getServicesFactory().getVoteService()
						.save(usuario.getId(), propuesta.getId());
			}
			factory.getServicesFactory().getProposalService().update(propuesta);
			List<Proposal> proposals = factory.getServicesFactory()
					.getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);
			kafkaProducer.send("user", "Voto negativo");
			return new ModelAndView("usuario").addObject("proposals",
					proposals);
		} else {
			kafkaProducer.send("user", "Error al votar negativo");
			return fail();
		}
	}

	@RequestMapping("/propuestasTramite")
	public ModelAndView propuestasTramite() {
		if (usuario != null) {
			List<Proposal> proposals = factory.getServicesFactory()
					.getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);

			if (usuario.isAdmin()) {
				if (proposals != null) {
					kafkaProducer.send("admin", "Propuestas en trámite");
					return new ModelAndView("enTramiteAdmin")
							.addObject("proposals", proposals)
							.addObject("hidden", false)
							.addObject("id", idPropuesta);
				} else {
					kafkaProducer.send("admin", "Propuestas en trámite");
					return new ModelAndView("enTramiteAdmin")
							.addObject("hidden", true);
				}
			} else {
				if (proposals != null) {
					kafkaProducer.send("user", "Propuestas en trámite");
					return new ModelAndView("enTramite")
							.addObject("proposals", proposals)
							.addObject("hidden", false);
				} else {
					kafkaProducer.send("user", "Propuestas en trámite");
					return new ModelAndView("enTramite").addObject("hidden",
							true);
				}
			}
		} else {
			kafkaProducer.send("user", "Error en propuestas en trámite");
			return fail();
		}
	}

	@RequestMapping("/propuestasRechazadas")
	public ModelAndView propuestasRechazadas() {
		if (usuario != null) {
			List<Proposal> proposals = factory.getServicesFactory()
					.getProposalService()
					.findByStatus(EstadosPropuesta.Rechazada);
			if (usuario.isAdmin()) {
				if (proposals != null) {
					kafkaProducer.send("admin", "Propuestas rechazadas");
					return new ModelAndView("rechazadas")
							.addObject("proposals", proposals)
							.addObject("hidden", false);
				} else {
					kafkaProducer.send("admin", "Propuestas rechazadas");
					return new ModelAndView("rechazadas").addObject("hidden",
							true);
				}
			} else {
				kafkaProducer.send("admin", "Error en propuestas rechazadas");
				return fail();
			}
		} else {
			kafkaProducer.send("admin", "Error en propuestas rechazadas");
			return fail();
		}
	}

	@RequestMapping("/propuestasAceptadas")
	public ModelAndView propuestasAceptadas() {
		if (usuario != null) {
			List<Proposal> proposals = factory.getServicesFactory()
					.getProposalService()
					.findByStatus(EstadosPropuesta.Aceptada);
			if (usuario.isAdmin()) {
				if (proposals != null) {
					kafkaProducer.send("admin", "Propuestas aceptadas");
					return new ModelAndView("aceptadasAdmin")
							.addObject("proposals", proposals)
							.addObject("hidden", false);
				} else {
					kafkaProducer.send("admin", "Propuestas aceptadas");
					return new ModelAndView("aceptadasAdmin")
							.addObject("hidden", true);
				}
			} else {
				if (proposals != null) {
					kafkaProducer.send("user", "Propuestas aceptadas");
					return new ModelAndView("aceptadas")
							.addObject("proposals", proposals)
							.addObject("hidden", false);
				} else {
					kafkaProducer.send("user", "Propuestas aceptadas");
					return new ModelAndView("aceptadas").addObject("hidden",
							true);
				}
			}
		} else {
			kafkaProducer.send("admin", "Error en propuestas aceptadas");
			return fail();
		}
	}

	@RequestMapping(path = "/rechazarPropuesta", method = RequestMethod.GET)
	public ModelAndView rechazarPropuesta(
			@RequestParam("idPropuesta") String idPropuesta) {
		if (usuario != null) {
			Proposal propuesta = factory.getServicesFactory()
					.getProposalService().findById(Long.parseLong(idPropuesta));
			propuesta.setStatus(EstadosPropuesta.Rechazada);

			factory.getServicesFactory().getProposalService().update(propuesta);
			List<Proposal> proposals = factory.getServicesFactory()
					.getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);
			kafkaProducer.send("admin", "Propuesta rechazada");
			return new ModelAndView("enTramiteAdmin")
					.addObject("proposals", proposals)
					.addObject("hidden", false);
		} else {
			kafkaProducer.send("admin", "Error al rechazar una propuesta");
			return fail();
		}
	}

	@RequestMapping(path = "/modificarMinVotes", method = RequestMethod.POST)
	public ModelAndView modificarMinVotes(
			@RequestParam("minVotes") int minVotes,
			@RequestParam("idPropuesta") String idPropuesta) {
		if (usuario != null) {
			Proposal propuesta = factory.getServicesFactory()
					.getProposalService().findById(Long.parseLong(idPropuesta));
			propuesta.setMinVotes(minVotes);

			factory.getServicesFactory().getProposalService().update(propuesta);
			List<Proposal> proposals = factory.getServicesFactory()
					.getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);
			kafkaProducer.send("admin", "Modificar número de votos");
			return new ModelAndView("enTramiteAdmin")
					.addObject("proposals", proposals)
					.addObject("hidden", false);
		} else {
			kafkaProducer.send("admin", "Error en modificar número de votos");
			return fail();
		}
	}
}