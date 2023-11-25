package pt.ul.fc.css.controllers.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.facade.dtos.CidadaoDTO;
import pt.ul.fc.css.facade.dtos.DelegadoDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.facade.exceptions.CidadaoNotFoundException;
import pt.ul.fc.css.services.CidadaoService;
import pt.ul.fc.css.services.ProjetoLeiService;

import java.util.Optional;

@Controller
public class WebCidadaoController {

    @Autowired
    private CidadaoService cidadaoService;
    @Autowired
    private ProjetoLeiService projetoLeiService;

    public WebCidadaoController(){
        super();
    }
    @GetMapping({"/","/login"})
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/cidadao404")
    public String showError() {
        return "error/Cidadao404";
    }

    @PostMapping("/login")
    public String login(@RequestParam int nrCC, HttpSession session) {
        try {
            Long cidadaoId = cidadaoService.verifyCidadao(nrCC);
            session.setAttribute("cidadaoId", cidadaoId);
            return "redirect:/home"; // Redirect to the home page or any other desired page
        } catch (CidadaoNotFoundException e) {
            // Handle the case when the cidadao is not found
            return "redirect:/cidadao404";
        }
    }

    @GetMapping("/home")
    public String home(final Model model, HttpSession session) {
        Long cidadaoId = (Long) session.getAttribute("cidadaoId");
        Optional<CidadaoDTO> cidadao = cidadaoService.getCidadao(cidadaoId);
        model.addAttribute("cidadao", cidadao.get());
        if (cidadaoId == null) {
            return "redirect:/login";
        }

        int nrCC = cidadaoService.getCidadao(cidadaoId).get().getNrCC();

        Optional<DelegadoDTO> delegado = cidadaoService.findDelegadoByCC(nrCC);
        if (delegado.isPresent()) {
            // Delegado found, add it to the model
            model.addAttribute("delegado", delegado.get());
            model.addAttribute("showNewProjetoLeiButton", true);
        } else {
            model.addAttribute("showNewProjetoLeiButton", false);
        }

        return "home";
    }


    @GetMapping( "/delegados")
    public String listarDelegados(final Model model) {
        model.addAttribute("delegados", cidadaoService.listarDelegados());
        return "listarDelegados";
    }


    @GetMapping("/escolheDelegado")
    public String escolheDelegado(final Model model, @RequestParam("delegadoId") Long delegadoId) throws ApplicationException {
        model.addAttribute("temas", projetoLeiService.getListaTemas());
        model.addAttribute("delegado", cidadaoService.getDelegado(delegadoId));
        return "escolheDelegado";
    }

    @PostMapping("/escolherDelegado")
    public String processEscolherDelegado(@RequestParam("delegadoId") Long delegadoId, @RequestParam("tema") String tema, HttpSession session) throws CidadaoNotFoundException {
        Long cidadaoId = (Long) session.getAttribute("cidadaoId");
        cidadaoService.escolheDelegado(delegadoId, cidadaoId, tema);
        return "redirect:/home";
    }


}
