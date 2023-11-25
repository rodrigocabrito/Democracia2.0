package pt.ul.fc.css.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.facade.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.facade.dtos.ProjetoLeiRestDTO;
import pt.ul.fc.css.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.facade.exceptions.ProjetoLeiNotFoundException;
import pt.ul.fc.css.services.ProjetoLeiService;
import pt.ul.fc.css.services.VotacaoService;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api")
public class RestProjetoLeiController {

    @Autowired
    private ProjetoLeiService projetoLeiService;
    @Autowired
    private VotacaoService votacaoService;

    @GetMapping("/projetosLei")
    List<ProjetoLeiRestDTO> all() {
        return projetoLeiService.listarProjetosLeiAbertosRest(); //
    }

    @GetMapping("/projetosLei/{id}")
    Optional<ProjetoLeiDTO> onlyOne(@PathVariable Long id){
        return projetoLeiService.getProjetoLei(id); //
    }

    @PutMapping("/projetosLei/{projetoLeiId}/apoiaProjetoLei/{cidadaoId}")
    ResponseEntity<?> apoiaProjetoLei(@PathVariable Long projetoLeiId, @PathVariable Long cidadaoId) throws ProjetoLeiNotFoundException, ApplicationException {

        Optional<ProjetoLeiDTO> pl = projetoLeiService.apoia(projetoLeiId, cidadaoId);
        if (pl.isPresent()) {
            if (votacaoService.upToVotacao(pl.get().getId()) && !votacaoService.existsVotacao(pl.get().getId())) {
                createVotacao(pl.get().getId());
            }
            return ResponseEntity.ok().body(pl.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/votacao/new/{projetoLeiId}")
    ResponseEntity<?> createVotacao(@PathVariable Long projetoLeiId) {
        try {
            VotacaoDTO votacao = votacaoService.createVotacao(projetoLeiId);
            return ResponseEntity.ok().body(votacao);
        } catch (ApplicationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
