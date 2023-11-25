package pt.ul.fc.css.handlers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.entities.Cidadao;
import pt.ul.fc.css.entities.Delegado;
import pt.ul.fc.css.facade.dtos.CidadaoDTO;
import pt.ul.fc.css.facade.exceptions.CidadaoNotFoundException;
import pt.ul.fc.css.repositories.CidadaoRepository;
import pt.ul.fc.css.repositories.DelegadoRepository;
import pt.ul.fc.css.services.CidadaoService;

import java.util.Optional;

@Component
public class EscolherDelegadoHandler {


    @Autowired
    private CidadaoRepository repositorioCidadao;
    @Autowired
    private DelegadoRepository repositorioDelegado;

    /**
     * Assigns a given delegate to a given theme, for a given citizen.
     * @param delegadoId given delegate's CC id
     * @param cidadaoId given citizen's CC id
     * @param temaDesignation given theme designation
     * @throws CidadaoNotFoundException if no citizen with the given CC number exists.
     */
    @Transactional
    public CidadaoDTO escolheDelegado(Long delegadoId, Long cidadaoId, String temaDesignation) throws CidadaoNotFoundException {
        try {
            Optional<Cidadao> oc = repositorioCidadao.findById(cidadaoId);
            oc.ifPresent(cidadao -> {
                Optional<Delegado> od = repositorioDelegado.findById(delegadoId);
                od.ifPresent(delegado -> {
                    if (!cidadao.hasDelegadoAssignedToAnyTheme(delegado.getNrCC())) {

                        cidadao.addDelegadoTema(delegado.getNrCC(), temaDesignation);
                        repositorioCidadao.save(cidadao);
                    }
                });
            });
            return CidadaoService.dtofy(oc.get());

        } catch (Exception e) {
            throw new CidadaoNotFoundException("Cidadao com o id " + cidadaoId + " nao existe.", e);
        }
    }
}
