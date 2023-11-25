package pt.ul.fc.css.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.entities.*;
import pt.ul.fc.css.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.facade.dtos.VotacaoRestDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.facade.exceptions.ProjetoLeiNotFoundException;
import pt.ul.fc.css.facade.exceptions.VotacaoNotFoundException;
import pt.ul.fc.css.handlers.FecharVotacaoHandler;
import pt.ul.fc.css.handlers.VotarVotacaoHandler;
import pt.ul.fc.css.repositories.DelegadoRepository;
import pt.ul.fc.css.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.repositories.VotacaoRepository;

@Service
public class VotacaoService {

	@Autowired
	private VotacaoRepository repositorioVotacao;
	@Autowired
	private ProjetoLeiRepository repositorioProjetoLei;
	@Autowired
	private DelegadoRepository repositorioDelegado;
	@Autowired
	private FecharVotacaoHandler fecharVotacaoHandler;
	@Autowired
	private VotarVotacaoHandler votarVotacaoHandler;

	/**
	 * Finds a voting by its bill's title.
	 * @param projetoLeiId given voting bill's title
	 * @return the voting associated with the bill's title, if it exists.
	 * @throws VotacaoNotFoundException if no such voting exists.
	 */
	public VotacaoDTO findByProjetoLeiId(Long projetoLeiId) throws VotacaoNotFoundException {
		try {
			return dtofy(repositorioVotacao.findByProjetoLeiId(projetoLeiId));
		} catch (Exception e) {
			throw new VotacaoNotFoundException("Votacao associada ao projeto Lei com id " + projetoLeiId + " nao existe.", e);
		}
	}

	/**
	 * Lists all open voting.
	 * @return a list of all open voting.
	 */
	@Transactional
	public List<VotacaoDTO> getListaVotacoesAbertas() {
		ArrayList<VotacaoDTO> arr = new ArrayList<>();
		for(Votacao v : repositorioVotacao.findByStatus(Status.ABERTO)){
			VotacaoDTO v2 = dtofy(v);
			arr.add(v2);
		}
		return arr;
	}

	@Transactional
	public List<VotacaoRestDTO> getListaVotacoesAbertasRest() {
		ArrayList<VotacaoRestDTO> arr = new ArrayList<>();
		for(Votacao v : repositorioVotacao.findByStatus(Status.ABERTO)){
			VotacaoRestDTO v2 = dtofyRest(v);
			arr.add(v2);
		}
		return arr;
	}

	/**
	 * Checks if the bill is up to voting.
	 * @param projetoLeiId given bill's id
	 * @return {@code true} if the bill has 10000 supporters or more,
	 * {@code false} otherwise.
	 * @throws ProjetoLeiNotFoundException if no bill with the given title exists.
	 */
	public boolean upToVotacao(Long projetoLeiId) throws ProjetoLeiNotFoundException {
		try {
			Optional<ProjetoLei> projeto = repositorioProjetoLei.findById(projetoLeiId);

			return projeto.map(p -> p.getApoios() >= 10000).orElse(false);
		} catch (Exception e) {
			throw new ProjetoLeiNotFoundException("Projeto Lei com id " + projetoLeiId + " nao existe.", e);
		}
	}

	/**
	 * Creates a new voting. By default, the {@code Status.ABERTO} is assigned to this voting and
	 * its proposing delegate is given the bill's delegate associated.
	 * @param projetoLeiId given bill's id
	 * @throws ApplicationException if an errror occurs while creating the voting.
	 */
	@Transactional
	public VotacaoDTO createVotacao(Long projetoLeiId) throws ApplicationException {
		try {
			Votacao votacao = new Votacao();
			Optional<ProjetoLei> op = repositorioProjetoLei.findById(projetoLeiId);
			op.ifPresent(projeto -> {

				Delegado delegado = projeto.getDelegado();
				votacao.setProjetoLei(projeto);
				votacao.setDataFecho(projeto.getDataLimite().truncatedTo(ChronoUnit.SECONDS));
				votacao.setDelegadoProp(delegado);
				votacao.setStatus(Status.ABERTO);
				votacao.setVotacaoResult(VotacaoResult.NAO_TERMINADA);
				votacao.addDelegadoVoted(delegado.getNrCC());
				votacao.setVotosAFavor(1);

				repositorioVotacao.save(votacao);

				delegado.addVotoVotacao(votacao);
				delegado.addVotoPublico(votacao,Voto.FAVOR);
				repositorioDelegado.save(delegado);
			});
			return dtofy(votacao);

		} catch (Exception e) {
			throw new ApplicationException("Erro ao criar votacao a partir de projeto de lei " +
					"com id " + projetoLeiId + ".", e);
		}
	}

	/**
	 * Adds the given vote to the voting if a citizen hasn't yet voted in the given voting.
	 * If the citizen is also a delegate, its vote is made public in that voting.
	 * @param id given id
	 * @param cidadaoId given citizen's id
	 * @param voto given citizen's vote
	 * @throws ApplicationException if an error occurs adding the vote to the voting.
	 */
	@Transactional
	public Optional<VotacaoDTO> votarVotacao(Long id, Long cidadaoId, Voto voto) throws ApplicationException {
		return votarVotacaoHandler.votarVotacao(id, cidadaoId, voto);
	}

	/**
	 * Closes a voting, changing its status to closed.
	 * After that, if any citizen, for that given voting, hasn't voted, the vote is given
	 * by its delegate assigned to the theme of the given voting (no abstention is allowed).
	 * Then, the result of the voting is calculated.
	 *
	 * @throws ApplicationException if an error occurs while closing the voting.
	 */
	@Transactional
	@Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Execute every 12 hours
	public void fecharVotacoes() throws ApplicationException {
		fecharVotacaoHandler.fecharVotacoes();
	}

	/**
	 * Checks if a voting with the given bill's id exists.
	 * @param projetoLeiId the given bill's id.
	 * @return {@code true} if a voting with the given bill's id exists, {@code false} otherwise.
	 */
	public boolean existsVotacao(Long projetoLeiId){
		return repositorioVotacao.existsVotacaoById(projetoLeiId);
	}

	public Optional<VotacaoDTO> getVotacao(Long id) {
		return repositorioVotacao.findById(id).map(VotacaoService::dtofy);
	}

	/**
	 * Updates the expiry date of a voting.
	 * @param votacaoId given voting's id
	 * @param data given data to update
	 * @throws VotacaoNotFoundException if the voting with the given id doesn't exist.
	 */
	@Transactional
	public void updateDataLimite(Long votacaoId, LocalDateTime data) throws VotacaoNotFoundException {
		//author: chatGPT
		Optional<Votacao> votacao = repositorioVotacao.findById(votacaoId);
		Votacao votacaoToUpdate = votacao.orElseThrow(() -> new VotacaoNotFoundException("Votacao com id "
				+ votacaoId + " nao existe."));
		votacaoToUpdate.setDataFechoTest(data);

		repositorioVotacao.save(votacaoToUpdate);
	}

	/**
	 * COnverts Votacao to VotacaoDTO
	 * @param v the given voting
	 * @return a new object VotacaoDTO with the same attributes as the given voting
	 */
	public static VotacaoDTO dtofy(Votacao v) {
		VotacaoDTO v2 = new VotacaoDTO();
		v2.setId(v.getId());
		v2.setProjetoLei(v.getProjetoLei());
		v2.setDataFecho(v.getDataFecho());
		v2.setCidadaosVotantes(v.getCidadaosVotantes());
		v2.setDelegadoProp(v.getDelegadoProp());
		v2.setListaIdsDelegadoVoted(v.getListaIdsDelegadoVoted());
		v2.setStatus(v.getStatus());
		v2.setVotacaoResult(v.getVotacaoResult());
		v2.setVotosAFavor(v.getVotosAFavor());
		v2.setVotosContra(v.getVotosContra());
		return v2;
	}

	public static VotacaoRestDTO dtofyRest(Votacao v) {
		VotacaoRestDTO v2 = new VotacaoRestDTO();
		v2.setId(v.getId());
		v2.setProjetoLei(v.getProjetoLei().getTitulo());
		v2.setDataFecho(v.getDataFecho());
		v2.setCidadaosVotantes(v.getCidadaosVotantes());
		v2.setDelegadoProp(v.getDelegadoProp().getNome());
		v2.setListaIdsDelegadoVoted(v.getListaIdsDelegadoVoted());
		v2.setStatus(v.getStatus().toString());
		v2.setVotacaoResult(v.getVotacaoResult().toString());
		v2.setVotosAFavor(v.getVotosAFavor());
		v2.setVotosContra(v.getVotosContra());
		return v2;
	}
}
