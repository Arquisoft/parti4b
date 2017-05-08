package es.uniovi.asw.dashboard.impl;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

	@RequestMapping("/dashboard")
	public ModelAndView index(HttpSession session) {
		
		return new ModelAndView("dashboard").addObject("proposals", EventControllerImpl.votedProposals).addObject("hidden",
				false);
	}
	
	
}
