package pt.ul.fc.css.handlers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.entities.Cidadao;
import pt.ul.fc.css.entities.Delegado;
import pt.ul.fc.css.entities.Votacao;
import pt.ul.fc.css.entities.Voto;
import pt.ul.fc.css.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.repositories.CidadaoRepository;
import pt.ul.fc.css.repositories.DelegadoRepository;
import pt.ul.fc.css.repositories.VotacaoRepository;
import pt.ul.fc.css.services.VotacaoService;

import java.util.Optional;

@Component
public class VotarVotacaoHandler {

    @Autowired
    private CidadaoRepository repositorioCidadao;
    @Autowired
    private DelegadoRepository repositorioDelegado;
    @Autowired
    private VotacaoRepository repositorioVotacao;

    /**
     * Adds the given vote to the voting if a citizen hasn't yet voted in the given voting.
     * If the citizen is also a delegate, its vote is made public in that voting.
     * @param votacaoId given voting's id
     * @param cidadaoId given citizen's id
     * @param voto given citizen's vote
     * @throws ApplicationException if an error occurs adding the vote to the voting.
     */
    @Transactional
    public Optional<VotacaoDTO> votarVotacao(Long votacaoId, Long cidadaoId, Voto voto) throws ApplicationException {
        try {
            Optional <Votacao> ov = repositorioVotacao.findById(votacaoId);
            Optional<Cidadao> oc = repositorioCidadao.findById(cidadaoId);

            ov.ifPresent(votacao -> {
                oc.ifPresent(cidadao -> {

                    if(!cidadao.hasVoted(votacao)){
                        Delegado delegado = repositorioDelegado.findByCC(cidadao.getNrCC());
                        if (delegado != null) { //if it is a delegate

                            if(!delegado.hasVoted(votacao)) {
                                //records the vote as a delegate
                                votacao.addDelegadoVoted(cidadao.getNrCC());
                                votacao.addVoto(cidadao, voto);
                                delegado.addVotoVotacao(votacao);
                                delegado.addVotoPublico(votacao, voto);

                                repositorioVotacao.save(votacao);
                                repositorioDelegado.save(delegado);
                            }
                        } else {
                            //records the vote
                            cidadao.addVotoVotacao(votacao);
                            votacao.addVoto(cidadao, voto);

                            repositorioCidadao.save(cidadao);
                            repositorioVotacao.save(votacao);
                        }
                    }
                });
            });
            return ov.map(VotacaoService::dtofy);

        } catch (Exception e) {
            throw new ApplicationException("Erro ao votar na votacao associada ao " +
                    "projeto Lei com id " + votacaoId + ".", e);
        }
    }
}
