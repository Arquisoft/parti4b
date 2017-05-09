package es.uniovi.asw.presentation;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uniovi.asw.business.parser.impl.RCitizens;
import es.uniovi.asw.business.parser.writer.FactoryLetter;
import es.uniovi.asw.business.parser.writer.Letter;
import es.uniovi.asw.business.parser.writer.PDFLetter;
import es.uniovi.asw.business.parser.writer.TXTLetter;
import es.uniovi.asw.business.parser.writer.WordLetter;
import es.uniovi.asw.conf.Factories;
import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.Commentary;
import es.uniovi.asw.model.ImprimeDatosComment;
import es.uniovi.asw.model.Proposal;
import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.model.types.status.EstadosComentario;
import es.uniovi.asw.model.types.status.EstadosPropuesta;
import es.uniovi.asw.model.types.topics.Topics;
import es.uniovi.asw.streamkafka.producers.KafkaProducer;
import es.uniovi.asw.util.Printer;

/**
 * Acceso web
 */
@Controller
@Scope("session")
public class MainController {

	@Autowired
	private Factories factory;

	@Autowired
	private KafkaProducer kafkaProducer;

	private Citizen usuario;

	List<String> keyWords = new ArrayList<String>();

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
	public String index(HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		// Cerramos sesion usuario a null y lo mandamos al landing
		// Aunque tambien se inicializa al principio
		session.removeAttribute("user");
		kafkaProducer.send("logout", "El usuario cerro sesion correctamente" + " o es la primera vez que entrea");
		return "landing";
	}

	// Se le llama al realizar login
	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam("dni") String dni, @RequestParam("password") String password,
			HttpSession session) {
		// Para cuando la bbdd tenga contraseñas que nos conocemos
		// usuario = servicio.findLoggableUser(dni , md5(password));

		usuario = factory.getServicesFactory().getCitizenService().findByDni(dni);

		if (usuario != null) {
			if (usuario.isAdmin()) {
				kafkaProducer.send("admin", "El administrador del sistema se ha logueado");
				session.setAttribute("user", usuario);
				return new ModelAndView("admin"); // la contraseña de admin es
			} else {
				List<Proposal> proposals = factory.getServicesFactory().getProposalService()
						.findByStatus(EstadosPropuesta.EnTramite);
				session.setAttribute("user", usuario);
				kafkaProducer.send("user", "El usuario " + usuario.getNombre() + " se ha logueado");
				return new ModelAndView("usuario").addObject("proposals", proposals);
			}
		} else {
			kafkaProducer.send("login", "Usuario o contraseña incorrectos");
			return new ModelAndView("landing").addObject("hidden", true).addObject("mensaje",
					"Usuario o contraseña invalido.");
		}
	}

	@SuppressWarnings("unused")
	@RequestMapping(path = "/comment", method = RequestMethod.GET)
	public ModelAndView comment(@RequestParam String id, HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			List<Commentary> commentaries = null;

			this.idPropuesta = Long.parseLong(id);
			List<ImprimeDatosComment> imp = null;

			if (user.isAdmin()) {
				commentaries = factory.getServicesFactory().getCommentaryService().findByProposalId(idPropuesta);
				imp = new ArrayList<ImprimeDatosComment>();
				for (int i = 0; i < commentaries.size(); i++) {
					ImprimeDatosComment imprime = new ImprimeDatosComment();
					imprime.setContent(commentaries.get(i).getContent());
					imprime.setNombre(factory.getServicesFactory().getCitizenService()
							.findById(commentaries.get(i).getCitizen().getId()).getNombre());
					imprime.setDate(commentaries.get(i).getCreationDate().toString());
					imprime.setStatus(commentaries.get(i).getEstado().toString());
					imprime.setIdComment(commentaries.get(i).getCreationDate().toString());

					imp.add(imprime);
				}

				if (commentaries != null) {
					kafkaProducer.send("admin", "La propuesta seleccionada tiene comentarios");
					return new ModelAndView("commentAdmin").addObject("commentaries", commentaries)
							.addObject("hidden", false).addObject("id", id).addObject("datos", imp);
				} else {
					kafkaProducer.send("admin", "La propuesta seleccionada no tiene comentarios");
					return new ModelAndView("commentAdmin").addObject("hidden", true).addObject("id", id);
				}

			} else {
				commentaries = factory.getServicesFactory().getCommentaryService().findByProposal(Long.parseLong(id));
				imp = new ArrayList<ImprimeDatosComment>();
				for (int i = 0; i < commentaries.size(); i++) {
					ImprimeDatosComment imprime = new ImprimeDatosComment();
					imprime.setContent(commentaries.get(i).getContent());
					imprime.setNombre(factory.getServicesFactory().getCitizenService()
							.findById(commentaries.get(i).getCitizen().getId()).getNombre());
					imprime.setDate(commentaries.get(i).getCreationDate().toString());

					imp.add(imprime);
				}

				if (commentaries != null) {
					kafkaProducer.send("user", "La propuesta seleccionada tiene comentarios");
					return new ModelAndView("comment").addObject("commentaries", commentaries)
							.addObject("hidden", false).addObject("id", id).addObject("datos", imp);
				} else {
					kafkaProducer.send("user", "La propuesta seleccionada no tiene comentarios");
					return new ModelAndView("comment").addObject("hidden", true).addObject("id", id);
				}
			}
		} else {
			kafkaProducer.send("login", "No existe usuario en sesion");
			return fail(session);
		}
	}

	@RequestMapping(path = "/censurar", method = RequestMethod.GET)
	public ModelAndView censurar(@RequestParam("id") String idComment, HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date fec = null;
			try {

				fec = formato.parse(idComment);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Commentary comentario = factory.getServicesFactory().getCommentaryService().findByCreationDate(fec);
			comentario.setEstado(EstadosComentario.Censurado);
			factory.getServicesFactory().getCommentaryService().update(comentario);

			kafkaProducer.send("admin", "El administrador censuro este comentario: " + comentario.getContent());
			return comment(idPropuesta.toString(), session);
		} else {
			kafkaProducer.send("login", "No existe el usuario en sesion");
			return fail(session);
		}
	}

	// Cuando en el menu de comments pulsamos en añadir un nuevo comentario
	@RequestMapping(path = "/crearComment", method = RequestMethod.GET)
	public ModelAndView crearComment(@RequestParam("id") String id, HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			this.idPropuesta = Long.parseLong(id);
			kafkaProducer.send("user", "Cargando formulario para crear comentario");
			return new ModelAndView("crearComment").addObject("hidden", false);
		} else {
			kafkaProducer.send("login", "El usuario no existe en sesion");
			return fail(session);
		}
	}

	// Cuando guardamos el comentario
	@RequestMapping(path = "/salvarComment", method = RequestMethod.POST)
	public ModelAndView salvarComment(@RequestParam("comment") String comment, HttpSession session) throws CitizenException {
		Citizen user = (Citizen) session.getAttribute("user");
		if (idPropuesta != null && user != null) {
			System.out.println(comment + " \nid de la propuesta: " + Long.toString(idPropuesta));

			if (validateComment(comment)) {
				// Arreglar la parte del modelo
				factory.getServicesFactory().getCommentaryService().save(user.getId(), idPropuesta, comment);
				kafkaProducer.send(Topics.NEW_COMMENT, ""+idPropuesta);
			} else {
				kafkaProducer.send("user", "El comentario no es válido.");
			}

			return comment(Long.toString(idPropuesta), session);

		} else {
			kafkaProducer.send("login", "El usuario no existe en sesion");
			return fail(session);
		}
	}

	// Aqui solo llamamos cuando queramos que vaya hacia atras, es decir,
	// nos logeamos como usuario pincha en ver comentarios y pulsa inicio
	@RequestMapping("/usuario")
	public ModelAndView backUser(HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			if (user.isAdmin()) {
				kafkaProducer.send("admin", "El administrador volvio a su pagina principal");
				return new ModelAndView("admin");
			} else {
				idPropuesta = null; // cuando se pulsa en incio reseteo el id de
									// la propuesta para evitar la navegacion
									// incorrecta
				List<Proposal> proposals = factory.getServicesFactory().getProposalService()
						.findByStatus(EstadosPropuesta.EnTramite);
				kafkaProducer.send("user", "El usuario " + user.getNombre() + " volvio a su pagina principal");
				return new ModelAndView("usuario").addObject("proposals", proposals);
			}
		} else {
			kafkaProducer.send("login", "El usuario no existe en sesion");
			return fail(session);
		}
	}

	// Aqui se manda siempre que falle algo
	@RequestMapping("/fail")
	public ModelAndView fail(HttpSession session) {
		session.removeAttribute("user");
		idPropuesta = null;
		return new ModelAndView("error");
	}

	@RequestMapping("/nuevaPropuesta")
	public ModelAndView nuevaPropuesta(HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			kafkaProducer.send("admin", "Sección para crear propuestas");
			return new ModelAndView("nuevaPropuesta");
		} else {
			kafkaProducer.send("admin", "Fallo al acceder a crear propuestas");
			return fail(session);
		}
	}

	@RequestMapping(path = "/crearPropuesta", method = RequestMethod.POST)
	public ModelAndView crearPropuesta(@RequestParam("nombre") String nombre,
			@RequestParam("contenido") String contenido, HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			Proposal propuesta = new Proposal(nombre, contenido, 1000);

			factory.getServicesFactory().getProposalService().save(propuesta);
			List<Proposal> proposals = factory.getServicesFactory().getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);
			kafkaProducer.send("admin", "Propuesta creada");
			return new ModelAndView("usuario").addObject("proposals", proposals);
		} else {
			kafkaProducer.send("admin", "Fallo al crear propuesta");
			return fail(session);
		}
	}

	@RequestMapping(path = "/votarPositivo", method = RequestMethod.GET)
	public ModelAndView votarPositivo(@RequestParam("idPropuesta") String idPropuesta, HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			Proposal propuesta = factory.getServicesFactory().getProposalService()
					.findById(Long.parseLong(idPropuesta));

			List<Proposal> proposals = factory.getServicesFactory().getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);

			if (!propuesta.positiveVote(user)) {
				kafkaProducer.send("user", "Ya ha votado el usuario");
				return new ModelAndView("usuario").addObject("proposals", proposals).addObject("hiddenFalse", true)
						.addObject("mensaje", "El usuario ya ha votado esta propuesta");
			} else {
				factory.getServicesFactory().getVoteService().save(user.getId(), propuesta.getId());
				kafkaProducer.send(Topics.POSITIVE_VOTE, ""+propuesta.getId());
			}
			if (propuesta.getValoration() >= propuesta.getMinVotes()) {
				propuesta.setStatus(EstadosPropuesta.Aceptada);
			}

			kafkaProducer.send("user", "Voto realizado");
			return new ModelAndView("usuario").addObject("proposals", proposals).addObject("hiddenTrue", true)
					.addObject("mensaje", "Voto realizado correctamente");
		} else {
			kafkaProducer.send("user", "Error al votar");
			return fail(session);
		}
	}

	@RequestMapping(path = "/votarNegativo", method = RequestMethod.GET)
	public ModelAndView votarNegativo(@RequestParam("idPropuesta") String idPropuesta, HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			Proposal propuesta = factory.getServicesFactory().getProposalService()
					.findById(Long.parseLong(idPropuesta));
			List<Proposal> proposals = factory.getServicesFactory().getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);

			if (!propuesta.negativeVote(user)) {
				kafkaProducer.send("user", "Ya ha votado el usuario");
				return new ModelAndView("usuario").addObject("proposals", proposals).addObject("hiddenFalse", true)
						.addObject("mensaje", "El usuario ya ha votado esta propuesta");
			} else {
				factory.getServicesFactory().getVoteService().save(user.getId(), propuesta.getId());
				kafkaProducer.send(Topics.NEGATIVE_VOTE, ""+propuesta.getId());
			}
			factory.getServicesFactory().getProposalService().update(propuesta);

			kafkaProducer.send("user", "Voto realizado");
			return new ModelAndView("usuario").addObject("proposals", proposals).addObject("hiddenTrue", true)
					.addObject("mensaje", "Voto realizado correctamente");
		} else {
			kafkaProducer.send("user", "Error al votar");
			return fail(session);
		}
	}

	@RequestMapping("/propuestasTramite")
	public ModelAndView propuestasTramite(HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			List<Proposal> proposals = factory.getServicesFactory().getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);

			if (user.isAdmin()) {
				if (proposals != null) {
					kafkaProducer.send("admin", "Propuestas en trámite");
					return new ModelAndView("enTramiteAdmin").addObject("proposals", proposals)
							.addObject("hidden", false).addObject("id", idPropuesta);
				} else {
					kafkaProducer.send("admin", "Propuestas en trámite");
					return new ModelAndView("enTramiteAdmin").addObject("hidden", true);
				}
			} else {
				if (proposals != null) {
					kafkaProducer.send("user", "Propuestas en trámite");
					return new ModelAndView("enTramite").addObject("proposals", proposals).addObject("hidden", false);
				} else {
					kafkaProducer.send("user", "Propuestas en trámite");
					return new ModelAndView("enTramite").addObject("hidden", true);
				}
			}
		} else {
			kafkaProducer.send("user", "Error en propuestas en trámite");
			return fail(session);
		}
	}

	@RequestMapping("/propuestasRechazadas")
	public ModelAndView propuestasRechazadas(HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			List<Proposal> proposals = factory.getServicesFactory().getProposalService()
					.findByStatus(EstadosPropuesta.Rechazada);
			if (user.isAdmin()) {
				if (proposals != null) {
					kafkaProducer.send("admin", "Propuestas rechazadas");
					return new ModelAndView("rechazadas").addObject("proposals", proposals).addObject("hidden", false);
				} else {
					kafkaProducer.send("admin", "Propuestas rechazadas");
					return new ModelAndView("rechazadas").addObject("hidden", true);
				}
			} else {
				kafkaProducer.send("admin", "Error en propuestas rechazadas");
				return fail(session);
			}
		} else {
			kafkaProducer.send("admin", "Error en propuestas rechazadas");
			return fail(session);
		}
	}

	@RequestMapping("/propuestasAceptadas")
	public ModelAndView propuestasAceptadas(HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			List<Proposal> proposals = factory.getServicesFactory().getProposalService()
					.findByStatus(EstadosPropuesta.Aceptada);
			if (user.isAdmin()) {
				if (proposals != null) {
					kafkaProducer.send("admin", "Propuestas aceptadas");
					return new ModelAndView("aceptadasAdmin").addObject("proposals", proposals).addObject("hidden",
							false);
				} else {
					kafkaProducer.send("admin", "Propuestas aceptadas");
					return new ModelAndView("aceptadasAdmin").addObject("hidden", true);
				}
			} else {
				if (proposals != null) {
					kafkaProducer.send("user", "Propuestas aceptadas");
					return new ModelAndView("aceptadas").addObject("proposals", proposals).addObject("hidden", false);
				} else {
					kafkaProducer.send("user", "Propuestas aceptadas");
					return new ModelAndView("aceptadas").addObject("hidden", true);
				}
			}
		} else {
			kafkaProducer.send("admin", "Error en propuestas aceptadas");
			return fail(session);
		}
	}

	@RequestMapping(path = "/rechazarPropuesta", method = RequestMethod.GET)
	public ModelAndView rechazarPropuesta(@RequestParam("idPropuesta") String idPropuesta, HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			Proposal propuesta = factory.getServicesFactory().getProposalService()
					.findById(Long.parseLong(idPropuesta));
			propuesta.setStatus(EstadosPropuesta.Rechazada);

			factory.getServicesFactory().getProposalService().update(propuesta);
			List<Proposal> proposals = factory.getServicesFactory().getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);
			kafkaProducer.send("admin", "Propuesta rechazada");
			return new ModelAndView("enTramiteAdmin").addObject("proposals", proposals).addObject("hidden", false);
		} else {
			kafkaProducer.send("admin", "Error al rechazar una propuesta");
			return fail(session);
		}
	}

	@RequestMapping(path = "/modificarMinVotes", method = RequestMethod.POST)
	public ModelAndView modificarMinVotes(@RequestParam("minVotes") int minVotes,
			@RequestParam("idPropuesta") String idPropuesta, HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			Proposal propuesta = factory.getServicesFactory().getProposalService()
					.findById(Long.parseLong(idPropuesta));
			propuesta.setMinVotes(minVotes);

			factory.getServicesFactory().getProposalService().update(propuesta);
			List<Proposal> proposals = factory.getServicesFactory().getProposalService()
					.findByStatus(EstadosPropuesta.EnTramite);
			kafkaProducer.send("admin", "Modificar número de votos");
			return new ModelAndView("enTramiteAdmin").addObject("proposals", proposals).addObject("hidden", false);
		} else {
			kafkaProducer.send("admin", "Error en modificar número de votos");
			return fail(session);
		}
	}

	private boolean validateComment(String comment) {

		if (keyWords.size() < 1) {
			inicializarKeyWord();
		}

		for (int i = 0; i < keyWords.size(); i++) {
			if (keyWords.get(i).contains(comment)) {
				return false;
			}
		}

		return true;
	}

	@RequestMapping(path = "/addKeyWords", method = RequestMethod.POST)
	public ModelAndView addKeyWords(@RequestParam("keyWord") String keyWord, HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			
			keyWords.add(keyWord);
			kafkaProducer.send("admin", "Añadida palabra a palabras no permitidas");

			return new ModelAndView("getKeyWords").addObject("getKeyWords", keyWords).addObject("hidden", false);
		} else {
			kafkaProducer.send("admin", "Error añadiendo palabra a palabras no permitidas");
			return fail(session);
		}
	}

	private void inicializarKeyWord() {
		keyWords.add("joder");
		keyWords.add("hostia");
		keyWords.add("mierda");
		keyWords.add("culo");
		keyWords.add("caca");
	}

	@RequestMapping(path = "/getKeyWords", method = RequestMethod.GET)
	public ModelAndView getKeyWords( HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");

		if (user != null) {
			if(user.isAdmin()){
				System.out.println("1");
	
				if (keyWords.size() < 1) {
					inicializarKeyWord();
					System.out.println("2");
				}
	
				kafkaProducer.send("admin", "Accediendo a palabras no permitidas");
				return new ModelAndView("getKeyWords").addObject("getKeyWords", keyWords).addObject("hidden", false);
			} else {
				return fail(session);
			}
		} else {
			kafkaProducer.send("admin", "No se puede acceder a las palabras no permitidas");
			return fail(session);
		}
	}

	@RequestMapping(path = "/citizensLoader", method = RequestMethod.GET)
	public ModelAndView citizensLoader(HttpSession session) {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			if(user.isAdmin()){
				kafkaProducer.send("admin", "Accediendo a citizensLoader");
				return new ModelAndView("citizensLoader");
			}
			else {
				return fail(session);
			}
		} else {
			kafkaProducer.send("admin", "No se puede acceder a citizensLoader");
			return fail(session);
		}
	}

	@RequestMapping(path = "/loadUsers", method = RequestMethod.POST)
	public ModelAndView loadUsers(@RequestParam("censo") File censo, HttpSession session) throws CitizenException {
		Citizen user = (Citizen) session.getAttribute("user");
		if (user != null) {
			kafkaProducer.send("admin", "Accediendo a loadUsers");

			try {
				if (censo != null) {
				
					List<Citizen> citizens = new RCitizens().readCitizens(censo);

					generarCartas(citizens);

					for (Citizen citizen : citizens) {
						factory.getServicesFactory().getCitizenService().save(citizen);
					}
				}
			} catch (Exception e) {
				new Printer().printCitizenException(e);
			}

			return new ModelAndView("citizensLoader");
		} else {
			kafkaProducer.send("admin", "Acceso denegado");
			return fail(session);
		}
	}

	private void generarCartas(List<Citizen> citizens) throws CitizenException {
		Letter letterTxt = new TXTLetter();
		letterTxt = FactoryLetter.generate("txt");

		Letter letterPDF = new PDFLetter();
		letterPDF = FactoryLetter.generate("pdf");

		Letter letterWord = new WordLetter();
		letterWord = FactoryLetter.generate("word");

		for (Citizen citizen : citizens) {
			letterTxt.generateLetter(citizen);
			letterPDF.generateLetter(citizen);
			letterWord.generateLetter(citizen);
		}
	}
}