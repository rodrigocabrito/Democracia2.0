package pt.ul.fc.css.controllers.web;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.entities.VotacaoResult;
import pt.ul.fc.css.entities.Voto;
import pt.ul.fc.css.facade.dtos.DelegadoDTO;
import pt.ul.fc.css.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.services.CidadaoService;
import pt.ul.fc.css.services.VotacaoService;

import java.util.Optional;

@Controller
public class WebVotacaoController {

    private static final Logger logger = LoggerFactory.getLogger(WebVotacaoController.class);

    @Autowired
    private VotacaoService votacaoService;
    @Autowired
    private CidadaoService cidadaoService;

    @GetMapping( "/votacoes")
    public String listarVotacoes(final Model model){
        model.addAttribute("votacoes", votacaoService.getListaVotacoesAbertas());
        return "listarVotacoes";
    }

    @PostMapping("/votarVotacao")
    public String votarVotacao(final Model model, @RequestParam("button") String button, @RequestParam("id") Long id, HttpSession session) {
        Long cidadaoId = (Long) session.getAttribute("cidadaoId");
        Optional<VotacaoDTO> votacao = votacaoService.getVotacao(id);
        Optional<VotacaoDTO> votacao2;

        if  (votacao.isPresent()) {
            model.addAttribute("votacao", votacao.get());
            if (button.equals("FAVOR")) {
                // Perform action for "Yes" button
                try {
                    votacao2 = votacaoService.votarVotacao(votacao.get().getId(), cidadaoId, Voto.FAVOR);
                    logger.debug("Voted in favor of Votacao.");
                    return "redirect:/votacoes/" + votacao2.get().getId();
                } catch (ApplicationException e) {
                    votacao2 = Optional.of(new VotacaoDTO());
                    model.addAttribute("votacao", votacao2);
                    model.addAttribute("error", e.getMessage());
                    return "consultarVotacao";
                }

            } else if (button.equals("CONTRA")) {
                try {
                    votacao2 = votacaoService.votarVotacao(votacao.get().getId(), cidadaoId, Voto.CONTRA);
                    logger.debug("Voted against Votacao.");
                    if(votacao2.get().getVotacaoResult() == VotacaoResult.NAO_TERMINADA){
                        model.addAttribute("showResult", true);
                    }
                    return "redirect:/votacoes/" + votacao2.get().getId();
                } catch (ApplicationException e) {
                    votacao2 = Optional.of(new VotacaoDTO());
                    model.addAttribute("votacao", votacao2);
                    model.addAttribute("error", e.getMessage());
                    return "consultarVotacao";
                }
            } else if(button.equals("delegadoVote")){
                return "redirect:/votacoes";
            }
        }
        // Default behavior if the button value is neither "FAVOR" nor "CONTRA"
        return "consultarVotacao";
    }


    @GetMapping("/votacoes/{id}")
    public String consultarVotacao(final Model model, @PathVariable Long id) {
        Optional<VotacaoDTO> votacao = votacaoService.getVotacao(id);
        int delegadoCC = cidadaoService.findDelegadoCCByTema(votacao.get().getProjetoLei().getTema().getDesignacao());
        if (votacao.isPresent()) {
            logger.error("VID:" + votacao.get().getId());
            model.addAttribute("votacao", votacao.get());
            if(delegadoCC != -1) {
                Optional<DelegadoDTO> d = cidadaoService.findDelegadoByCC(delegadoCC);
                model.addAttribute("delegado", d.get());
                Voto v = d.get().getVotoByVotacao(id);
                model.addAttribute("delegadoVoto", v);
            }
            return "consultarVotacao";
        } else {
            return "error/Votacao404";
        }
    }
}
