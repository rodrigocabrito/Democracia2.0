package pt.ul.fc.css.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.entities.Cidadao;
import pt.ul.fc.css.entities.Delegado;
import pt.ul.fc.css.facade.dtos.CidadaoDTO;
import pt.ul.fc.css.facade.dtos.DelegadoDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.facade.exceptions.CidadaoNotFoundException;
import pt.ul.fc.css.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.repositories.CidadaoRepository;
import pt.ul.fc.css.repositories.DelegadoRepository;

@Service
public class CidadaoService {

	@Autowired
	private CidadaoRepository repositorioCidadao;
	@Autowired
	private DelegadoRepository repositorioDelegado;
	@Autowired
	private EscolherDelegadoHandler escolherDelegadoHandler;

	/**
	 * Creates a new citizen.
	 * @param nrCC given citizen's number.
	 * @param nome given citizen's name.
	 * @param tokenAuth given citizen's token auth.
	 * @throws ApplicationException if an error occurs creating a new citizen.
	 */
	@Transactional
	public CidadaoDTO adicionaCidadao(int nrCC, String nome, Long tokenAuth) throws ApplicationException {
		try {

			Cidadao cidadao = new Cidadao();

			cidadao.setNrCC(nrCC);
			cidadao.setNome(nome);
			cidadao.setTokenAuth(tokenAuth);

			repositorioCidadao.save(cidadao);

			return CidadaoService.dtofy(cidadao);

		} catch (Exception e) {
			throw new ApplicationException("Erro ao adicionar cidadao com CC " + nrCC + ".", e);
		}
	}

	/**
	 * Lists all existing citizens.
	 * @return a list of all existing citizens.
	 */
	@Transactional
	public List<CidadaoDTO> listarCidadaos() {
		ArrayList<CidadaoDTO> arr = new ArrayList<>();
		for(Cidadao c : repositorioCidadao.findAll()){
			CidadaoDTO c2 = dtofy(c);
			arr.add(c2);
		}
		return arr;

	}

	/**
	 * Lists all existing delegates.
	 * @return a list of all existing dlegates.
	 */
	@Transactional
	public List<DelegadoDTO> listarDelegados() {
		ArrayList<DelegadoDTO> arr = new ArrayList<>();
		for(Delegado d : repositorioDelegado.findAll()){
			DelegadoDTO d2 = dtofy(d);
			arr.add(d2);
		}
		return arr;

	}
	/**
	 * Presents a citizen as a delegate. This new delegate has the same CC number,
	 * name and token auth as the citizen.
	 * @param cidadao given citizen.
	 * @throws ApplicationException if an error occurs updating a citizen.
	 */
	@Transactional
	public DelegadoDTO apresentaComoDelegado(CidadaoDTO cidadao) throws ApplicationException {
		try {

			Delegado delegado = new Delegado();
			delegado.setNrCC(cidadao.getNrCC());
			delegado.setNome(cidadao.getNome());
			delegado.setTokenAuth(cidadao.getTokenAuth());

			repositorioDelegado.save(delegado);

			return CidadaoService.dtofy(delegado);

		} catch (Exception e) {
			throw new ApplicationException("Erro ao apresentar cidadao com CC " + cidadao.getNrCC() + " como delegado.", e);
		}
	}

	/**
	 * Assigns a given delegate to a given theme, for a given citizen.
	 * @param delegadoId given delegate's CC id
	 * @param cidadaoId given citizen's CC id
	 * @param temaDesignation given theme designation
	 * @throws CidadaoNotFoundException if no citizen with the given CC number exists.
	 */
	@Transactional
	public CidadaoDTO escolheDelegado(Long delegadoId, Long cidadaoId, String temaDesignation) throws CidadaoNotFoundException {

		return escolherDelegadoHandler.escolheDelegado(delegadoId, cidadaoId, temaDesignation);
	}

	/**
     * Finds a delegate by its CC number.
     * @param nrCC given delegate's CC number
     * @return the delegate with the given CC number, if it exists.
	 */
	public Optional<DelegadoDTO> findDelegadoByCC(int nrCC) {
		Delegado delegado = repositorioDelegado.findByCC(nrCC);
		if (delegado != null) {
			return Optional.of(CidadaoService.dtofyDelegado(delegado));
		} else {
			return Optional.empty();
		}
	}


	/**
	 * Returns the delegate's CC number assigned to a given theme.
	 * @param designacao given theme's designation
	 * @return the delegate's CC number assigned to a given theme, if there is one.
	 */
	public int findDelegadoCCByTema(String designacao){
		String result = repositorioCidadao.findDelegadoCCByTemaDescription(designacao);
		if(result == null){
			return -1; // return -1 if there's no delegado associated with that tema
		}
		String[] split = result.split(";");

		return Integer.parseInt(split[0]);
	}

	public Optional<CidadaoDTO> getCidadao(Long id) {
		return repositorioCidadao.findById(id).map(CidadaoService::dtofy);
	}

	public Optional<DelegadoDTO> getDelegado(Long id) {
		return repositorioDelegado.findById(id).map(CidadaoService::dtofyDelegado);
	}

	public Optional<CidadaoDTO> getCidadao(int nrCC) {
		return Optional.of(CidadaoService.dtofy(repositorioCidadao.findByNrCC(nrCC)));
	}

	public Long verifyCidadao(int nrcc) throws CidadaoNotFoundException {

		Cidadao cidadao = repositorioCidadao.findByNrCC(nrcc);
		if (cidadao != null) {
			return cidadao.getId();
		} else {
			throw new CidadaoNotFoundException("Cidadao not found");
		}
	}

	/**
	 * Converts Cidadao to CidadaoDTO
	 * @param c the given citizen
	 * @return a new object CidadaoDTO with the same attributes as the given citizen
	 */
	public static CidadaoDTO dtofy(Cidadao c) {
		CidadaoDTO c2 = new CidadaoDTO();
		c2.setId(c.getId());
		c2.setNrCC(c.getNrCC());
		c2.setNome(c.getNome());
		c2.setTokenAuth(c.getTokenAuth());
		c2.setProjetosApoiados(c.getProjetosApoiados());
		c2.setVotacoesVotadas(c.getVotacoesVotadas());
		c2.setDelegadoCCTemaDesignacao(c.getDelegadoCCTemaDesignacao());
		return c2;
	}

	public static DelegadoDTO dtofyDelegado(Delegado d) {
		DelegadoDTO d2 = new DelegadoDTO();
		d2.setId(d.getId());
		d2.setNrCC(d.getNrCC());
		d2.setNome(d.getNome());
		d2.setTokenAuth(d.getTokenAuth());
		d2.setProjetosApoiados(d.getProjetosApoiados());
		d2.setVotacoesVotadas(d.getVotacoesVotadas());
		d2.setDelegadoCCTemaDesignacao(d.getDelegadoCCTemaDesignacao());
		d2.setVotacaoVoto(d.getVotacaoVoto());
		d2.setProjetosLeiPropostos(d.getProjetosLeiPropostos());
		return d2;
	}

	/**
	 * Converts Delegado to DelegadoDTO
	 * @param d the given delegate
	 * @return a new object DelegadoDTO with the same attributes as the given delegate
	 */
	public static DelegadoDTO dtofy(Delegado d) {
		DelegadoDTO d2 = new DelegadoDTO();
		d2.setId(d.getId());
		d2.setNrCC(d.getNrCC());
		d2.setNome(d.getNome());
		d2.setTokenAuth(d.getTokenAuth());
		d2.setProjetosApoiados(d.getProjetosApoiados());
		d2.setVotacoesVotadas(d.getVotacoesVotadas());
		d2.setDelegadoCCTemaDesignacao(d.getDelegadoCCTemaDesignacao());
		d2.setVotacaoVoto(d.getVotacaoVoto());
		d2.setProjetosLeiPropostos(d.getProjetosLeiPropostos());
		return d2;
	}

	/**
	 * Converts DelegadoDTO to Delegado
	 * @param d the given delegate
	 * @return a new object Delegado with the same attributes as the given delegate
	 */
	public static Delegado dtofyInvDel(DelegadoDTO d) {
		Delegado d2 = new Delegado();
		d2.setId(d.getId());
		d2.setNrCC(d.getNrCC());
		d2.setNome(d.getNome());
		d2.setTokenAuth(d.getTokenAuth());
		d2.setProjetosApoiados(d.getProjetosApoiados());
		d2.setVotacoesVotadas(d.getVotacoesVotadas());
		d2.setDelegadoCCTemaDesignacao(d.getDelegadoCCTemaDesignacao());
		d2.setVotacaoVoto(d.getVotacaoVoto());
		d2.setProjetosLeiPropostos(d.getProjetosLeiPropostos());
		return d2;
	}
}

