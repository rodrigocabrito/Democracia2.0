package pt.ul.fc.css.controllers.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.entities.Voto;
import pt.ul.fc.css.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.facade.dtos.VotacaoRestDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.services.VotacaoService;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api")
class RestVotacaoController {

    @Autowired
    private VotacaoService votacaoService;

    private static final Logger logger = LoggerFactory.getLogger(RestVotacaoController.class);

    @GetMapping("/votacoes")
    List<VotacaoRestDTO> all() {
        return votacaoService.getListaVotacoesAbertasRest(); //
    }

    @GetMapping("/votacoes/{id}")
    Optional<VotacaoDTO> onlyOne(@PathVariable Long id){
        return votacaoService.getVotacao(id); //
    }

    @PutMapping("/votacoes/{votacaoId}/votarVotacao/{cidadaoId}/{voto}")
    ResponseEntity<?> votarVotacao(@PathVariable Long votacaoId, @PathVariable Long cidadaoId, @PathVariable String voto) throws ApplicationException {
        Optional <VotacaoDTO> votacao = votacaoService.getVotacao(votacaoId);

        if (votacao.isPresent()) {

            VotacaoDTO votacaoDTO = votacao.get();
            Optional<VotacaoDTO> ov;

            if (voto.equals(Voto.FAVOR.toString()))
                ov = votacaoService.votarVotacao(votacaoDTO.getId(), cidadaoId, Voto.FAVOR);
            else
                ov = votacaoService.votarVotacao(votacaoDTO.getId(), cidadaoId, Voto.CONTRA);


            if (ov.isPresent())
                return ResponseEntity.ok().body(ov.get());
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
