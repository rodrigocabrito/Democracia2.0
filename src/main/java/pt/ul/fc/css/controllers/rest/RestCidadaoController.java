package pt.ul.fc.css.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.facade.dtos.CidadaoDTO;
import pt.ul.fc.css.services.CidadaoService;

import java.util.Optional;

@RestController()
@RequestMapping("/api")
public class RestCidadaoController {

    @Autowired
    private CidadaoService cidadaoService;

    @GetMapping("/login/{nrCC}")
    Long login(@PathVariable int nrCC) {
        Optional<CidadaoDTO> self = cidadaoService.getCidadao(nrCC);
        return self.map(CidadaoDTO::getId).orElse(-1L);
    }
}
