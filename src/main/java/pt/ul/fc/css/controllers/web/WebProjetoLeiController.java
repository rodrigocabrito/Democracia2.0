package pt.ul.fc.css.controllers.web;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.entities.ProjetoLei;
import pt.ul.fc.css.facade.dtos.CidadaoDTO;
import pt.ul.fc.css.facade.dtos.DelegadoDTO;
import pt.ul.fc.css.facade.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.facade.dtos.TemaDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.facade.exceptions.ProjetoLeiNotFoundException;
import pt.ul.fc.css.services.CidadaoService;
import pt.ul.fc.css.services.ProjetoLeiService;
import pt.ul.fc.css.services.VotacaoService;

import java.util.Optional;

@Controller
public class WebProjetoLeiController {

    private static final  Logger logger = LoggerFactory.getLogger(WebProjetoLeiController.class);

    @Autowired
    private ProjetoLeiService projetoLeiService;
    @Autowired
    private CidadaoService cidadaoService;
    @Autowired
    private VotacaoService votacaoService;

    public WebProjetoLeiController(){
        super();
    }

    @GetMapping( "/projetosLei")
    public String listarProjetosLei(final Model model){
        model.addAttribute("projetosLei", projetoLeiService.listarProjetosLeiAbertos());
        return "listarProjetosLei";
    }

    @GetMapping("/projetosLei/new")
    public String apresentarProjetoLei(final Model model) throws ApplicationException {

        model.addAttribute("temas", projetoLeiService.getListaTemas());
        model.addAttribute("projetoLei", new ProjetoLei());
        return "apresentarProjetoLei";
    }

    @PostMapping("/projetosLei/new")
    public String apresentarProjetoLeiAction(final Model model, @ModelAttribute ProjetoLeiDTO projeto,@RequestParam("tema") Long temaId, HttpSession session) {
        Long cidadaoId = (Long) session.getAttribute("cidadaoId");
        ProjetoLeiDTO projeto2;
        Optional<CidadaoDTO > c = cidadaoService.getCidadao(cidadaoId);
        Optional<DelegadoDTO> d = cidadaoService.findDelegadoByCC(c.get().getNrCC());
        Optional<TemaDTO> tema = projetoLeiService.getTema(temaId);
        try {
            projeto2 = projetoLeiService.apresentarProjetoLei(projeto.getTitulo(), projeto.getDescricao(),
                    projeto.getAnexoPDF(), projeto.getDataLimite(), d.get(),
                    tema.get());
            return "redirect:/projetosLei/" + projeto2.getId();
        } catch (ApplicationException e) {
            projeto2 = new ProjetoLeiDTO();
            model.addAttribute("projetoLei", projeto2);
            model.addAttribute("error", e.getMessage());
            return "apresentarProjetoLei";
        }
    }

    @PostMapping("/apoiarProjetoLei")
    public String apoiarProjetoLei(Model model, @RequestParam("projetoId") Long projetoId, @RequestParam("button") String button, HttpSession session) {
        Long cidadaoId = (Long) session.getAttribute("cidadaoId");

        if (button.equals("Yes")) {
            try {
                Optional<ProjetoLeiDTO> projeto = projetoLeiService.apoia(projetoId, cidadaoId);
                if (projeto.isPresent()) {
                    model.addAttribute("projetoLei", projeto.get());

                    if (votacaoService.upToVotacao(projeto.get().getId()) && !votacaoService.existsVotacao(projeto.get().getId())) {
                        createVotacao(projeto.get().getId());
                    }

                    return "redirect:/projetosLei/" + projetoId;
                } else {
                    return "error/ProjetoLei404";
                }
            } catch (ApplicationException | ProjetoLeiNotFoundException e) {
                model.addAttribute("error", e.getMessage());
                return "consultarProjetoLei";
            }
        } else {
            return "redirect:/projetosLei/" + projetoId;
        }
    }

    @PostMapping("votacao/new/{projetoLeiId}")
    public void createVotacao(@PathVariable Long projetoLeiId) {
        try {
            votacaoService.createVotacao(projetoLeiId);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/projetosLei/{id}")
    public String consultarProjetoLei(final Model model, @PathVariable Long id) {
        Optional<ProjetoLeiDTO> projetoLei = projetoLeiService.getProjetoLei(id);
        if (projetoLei.isPresent()) {
            logger.error("PID: " + projetoLei.get().getId());
            model.addAttribute("projetoLei", projetoLei.get());
            return "consultarProjetoLei";
        } else {
            return "error/ProjetoLei404";
        }
    }
}
