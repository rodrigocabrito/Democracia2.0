package pt.ul.fc.css.handlers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.entities.*;
import pt.ul.fc.css.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.facade.exceptions.VotacaoNotFoundException;
import pt.ul.fc.css.repositories.CidadaoRepository;
import pt.ul.fc.css.repositories.DelegadoRepository;
import pt.ul.fc.css.repositories.VotacaoRepository;
import pt.ul.fc.css.services.VotacaoService;

import java.util.List;
import java.util.Optional;

@Component
public class FecharVotacaoHandler {

    @Autowired
    private CidadaoRepository repositorioCidadao;
    @Autowired
    private VotacaoRepository repositorioVotacao;
    @Autowired
    private DelegadoRepository repositorioDelegado;

    /**
     * Closes a voting, changing its status to closed.
     * After that, if any citizen, for that given voting, hasn't voted, the vote is given
     * by its delegate assigned to the theme of the given voting (no abstention is allowed).
     * Then, the result of the voting is calculated.
     * @throws ApplicationException if an error occurs while closing the voting.
     */
    @Transactional
    public void fecharVotacoes() throws ApplicationException {
        try {
            List<Votacao> votacoes = repositorioVotacao.findAll();
            List<Cidadao> listaCidadaos = repositorioCidadao.findAll();

            for (Votacao votacao : votacoes) {

                // change status to closed
                closeVotacao(votacao.getId());

                // check if each citizen has voted and if not, use the vote of their assigned delegate to the theme of the voting
                if (votacao.getStatus().equals(Status.FECHADO)) {

                    for (Cidadao cidadao : listaCidadaos) {

                        if (!cidadao.hasVoted(votacao)) {
                            // hierarchy of themes not implemented
                            int delegadoCc = cidadao.getDelegadoCcByTema(votacao.getProjetoLei().getTema().getDesignacao());

                            if (delegadoCc != -1) {
                                Delegado delegado = repositorioDelegado.findByCC(delegadoCc);

                                // if delegate has voted in the voting, get his public vote
                                if (delegado.hasVoted(votacao)) {
                                    Voto voto = delegado.getVotoByVotacao(votacao.getId());
                                    votacao.addVoto(cidadao, voto);        // vote in name of the citizen
                                    repositorioVotacao.save(votacao);
                                }
                            }
                        }
                    }

                    int resultado = votacao.getResultadoVotacao();
                    if (resultado > 0) {
                        resultadoVotacao(votacao.getId(), VotacaoResult.APROVADA);
                    } else {
                        resultadoVotacao(votacao.getId(), VotacaoResult.REJEITADA);
                    }

                    repositorioVotacao.save(votacao);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException("Erro ao fechar a votacoes.", e);
        }
    }

    /**
     * Closes a voting, setting its status to {@code Status.FECHADO}.
     * @param votacaoId given voting's id
     * @throws ApplicationException an error occurs while closing the voting.
     */
    @Transactional
    public Optional<VotacaoDTO> closeVotacao(Long votacaoId) throws ApplicationException {
        try {
            Optional<Votacao> ov = repositorioVotacao.findById(votacaoId);
            if (ov.isPresent()) {
                Votacao votacao = ov.get();
                if (votacao.hasDateExpired()) {
                    votacao.setStatus(Status.FECHADO);
                    repositorioVotacao.save(votacao);
                }
            } else {
                throw new VotacaoNotFoundException("Votacao associada ao projeto Lei com id " + votacaoId + " nao existe.");
            }
            return ov.map(VotacaoService::dtofy);
        } catch (Exception e) {
            throw new ApplicationException("Erro ao fechar a votacao com id " + votacaoId + ".", e);
        }
    }

    /**
     * Sets the result of a voting, given its bill's id, to {@code VotacaoResult.APROVADA}
     * or {@code VotacaoResult.REJEITADA}, according to its votes.
     * Majority of in favor votes is needed for the status {@code VotacaoResult.APROVADA} to be assigned.
     * @param votacaoId given voting's id
     * @param votacaoResult given final result of voting
     */
    @Transactional
    public Optional<VotacaoDTO> resultadoVotacao(Long votacaoId, VotacaoResult votacaoResult) throws VotacaoNotFoundException {
        Optional<Votacao> ov = repositorioVotacao.findById(votacaoId);

        if (ov.isPresent()) {
            Votacao votacao = ov.get();
            votacao.setVotacaoResult(votacaoResult);
            repositorioVotacao.save(votacao);
        } else {
            throw new VotacaoNotFoundException("Votacao associada ao projeto Lei com id " + votacaoId + " nao existe.");
        }

        return ov.map(VotacaoService::dtofy);
    }

}
