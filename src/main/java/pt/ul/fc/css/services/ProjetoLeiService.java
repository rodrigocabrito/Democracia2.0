package pt.ul.fc.css.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.entities.*;
import pt.ul.fc.css.facade.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.facade.dtos.ProjetoLeiRestDTO;
import pt.ul.fc.css.facade.dtos.TemaDTO;
import pt.ul.fc.css.facade.dtos.DelegadoDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.facade.exceptions.ProjetoLeiNotFoundException;
import pt.ul.fc.css.facade.exceptions.TemaNotFoundException;
import pt.ul.fc.css.handlers.ApoiarProjetoLeiHandler;
import pt.ul.fc.css.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.handlers.FecharProjetosLeiExpiradosHandler;
import pt.ul.fc.css.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.repositories.TemaRepository;

@Service
public class ProjetoLeiService {
	
	@Autowired
	private ProjetoLeiRepository repositorioProjetoLei;
	@Autowired
	private TemaRepository repositorioTema;
	@Autowired
	private ApoiarProjetoLeiHandler apoiarProjetoHandler;
	@Autowired
	private ApresentarProjetoLeiHandler apresentarProjetoLeiHandler;
	@Autowired
	private FecharProjetosLeiExpiradosHandler fecharProjetosLeiExpiradosHandler;

	/**
	 * Adds a new bill.
	 * @param titulo given bill's title
	 * @param descricao given bill's description
	 * @param anexoPDF given bill's pdf file
	 * @param validade given bill's date
	 * @param delegado given bill's delegate
	 * @param tema given bill's theme
	 * @throws ApplicationException if an error occurs adding a new bill.
	 */
	@Transactional
	public ProjetoLeiDTO apresentarProjetoLei(String titulo, String descricao, byte[] anexoPDF,
											  LocalDateTime validade, DelegadoDTO delegado, TemaDTO tema) throws ApplicationException {

		return apresentarProjetoLeiHandler.apresentarProjetoLei(titulo, descricao, anexoPDF, validade,
				CidadaoService.dtofyInvDel(delegado), dtofyInvTema(tema));
	}

	/**
	 * Lists all open bills.
	 * @return a list of all open bills.
	 */
	@Transactional
	public List<ProjetoLeiDTO> listarProjetosLeiAbertos() {
		ArrayList<ProjetoLeiDTO> arr = new ArrayList<>();
		for(ProjetoLei pl : repositorioProjetoLei.findByStatus(Status.ABERTO)){
			ProjetoLeiDTO pl2 = dtofy(pl);
			arr.add(pl2);
		}
		return arr;
	}


	@Transactional
	public List<ProjetoLeiRestDTO> listarProjetosLeiAbertosRest() {
		ArrayList<ProjetoLeiRestDTO> arr = new ArrayList<>();
		for(ProjetoLei pl : repositorioProjetoLei.findByStatus(Status.ABERTO)){
			ProjetoLeiRestDTO pl2 = dtofyRest(pl);
			arr.add(pl2);
		}
		return arr;
	}

	/**
	 * Returns some info about the bill, such as id, title, description, pdf file, expiry date,
	 *  delegate associated, theme, supporters and status.
	 * @param id given bill's id.
	 * @return a String with the info of the bill.
	 */
	public String consultarProjetoLei(Long id){
		return repositorioProjetoLei.findById(id).get().toString();
	}

	/**
	 * Increments the number of supporters of the bill given by 1, which is the
	 * citizen, given its CC id.
	 * @param projetoLeiId given bill's id
	 * @param cidadaoId given citizen's id
	 * @throws ApplicationException if an error occurs while supporting the bill.
	 */
	@Transactional
	public Optional<ProjetoLeiDTO> apoia(Long projetoLeiId, Long cidadaoId) throws ApplicationException {
		return apoiarProjetoHandler.apoiarProjetoLei(projetoLeiId, cidadaoId);
	}

	/**
	 * Creates a theme.
	 * @param designacao given theme's designation.
	 * @throws ApplicationException if an error occurs creating the theme.
	 */
	public TemaDTO createTema(String designacao) throws ApplicationException {
		try {
			Tema tema = new Tema();
			tema.setDesignacao(designacao);
			repositorioTema.save(tema);

			return dtofy(tema);

		} catch (Exception e) {
			throw new ApplicationException("Erro ao adicionar tema com designacao " + designacao + ".", e);
		}
	}

	/**
	 * Finds a theme by its designation.
	 * @param designacao given theme's designation.
	 * @return the theme with the given designation, if it exists.
	 * @throws TemaNotFoundException if no theme with the given designation exists.
	 */
	public TemaDTO findTemaByDesignacao(String designacao) throws TemaNotFoundException {
		try {
			return dtofy(repositorioTema.findByDesignacao(designacao));
		} catch (Exception e) {
			throw new TemaNotFoundException("Tema com designacao" + designacao + " nao existe.", e);
		}
	}

	public Optional<TemaDTO> getTema(Long id) {
		return repositorioTema.findById(id).map(ProjetoLeiService::dtofy);
	}

	/**
	 * Updates the number of supporters of a bill. Only used for testing purposes.
	 * @param projetoLeiId given bill's id.
	 * @param apoios given number of supporters to be set.
	 * @throws ProjetoLeiNotFoundException if no bill with the given id exists.
	 */
	public ProjetoLeiDTO updateApoios(Long projetoLeiId, int apoios) throws ProjetoLeiNotFoundException {
		//author: chatGPT
		Optional<ProjetoLei> pl = repositorioProjetoLei.findById(projetoLeiId);
		ProjetoLei plToUpdate = pl.orElseThrow(() -> new ProjetoLeiNotFoundException("Projeto Lei com id "
				+ projetoLeiId + " nao existe."));
		plToUpdate.setApoiosTest(apoios);

		repositorioProjetoLei.save(plToUpdate);

		return dtofy(plToUpdate);
	}

	/**
	 * Updates the expiry date of a bill. Only used for testing purposes.
	 * @param projetoLeiId given bill's id.
	 * @param data given expiry date to be set.
	 * @throws ProjetoLeiNotFoundException if no bill with the given id exists.
	 */
	@Transactional
	public ProjetoLeiDTO updateDataLimite(Long projetoLeiId, LocalDateTime data) throws ProjetoLeiNotFoundException {
		//author: chatGPT
		Optional<ProjetoLei> pl = repositorioProjetoLei.findById(projetoLeiId);
		ProjetoLei plToUpdate = pl.orElseThrow(() -> new ProjetoLeiNotFoundException("Projeto Lei com id "
				+ projetoLeiId + " nao existe."));
        plToUpdate.setDataLimite(data);

		repositorioProjetoLei.save(plToUpdate);

		return dtofy(plToUpdate);
	}

	public List<TemaDTO> getListaTemas() throws ApplicationException {
		try {
			return dtofyListTema(repositorioTema.findAll());
		} catch (Exception e) {
			throw new ApplicationException("Erro ao listar todos os temas.", e);
		}
	}

	/**
	 * Closes all bills with an expired date.
	 * @throws ApplicationException if an error occurs closing all the bills.
	 */
	@Transactional
	@Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Execute every 24 hours
	public void fecharProjetosLeiExpirados() throws ApplicationException {
		fecharProjetosLeiExpiradosHandler.fecharProjetosLeiExpirados();
	}

	public Optional<ProjetoLeiDTO> getProjetoLei(Long id) {
		return repositorioProjetoLei.findById(id).map(ProjetoLeiService::dtofy);
	}

	/**
	 * Converts ProjetoLei to ProjetoLeiDTO
	 * @param p the given bill
	 * @return a new object ProjetoLeiDTO with the same attributes as the given bill
	 */
	public static ProjetoLeiDTO dtofy(ProjetoLei p) {
		ProjetoLeiDTO p2 = new ProjetoLeiDTO();
		p2.setId(p.getId());
		p2.setTitulo(p.getTitulo());
		p2.setDescricao(p.getDescricao());
		p2.setAnexoPDF(p.getAnexoPDF());
		p2.setDataLimite(p.getDataLimite());
		p2.setCidadaosApoiantes(p.getCidadaosApoiantes());
		p2.setDelegado(p.getDelegado());
		p2.setTema(p.getTema());
		p2.setStatus(p.getStatus());
		p2.setApoios(p.getApoios());
		return p2;
	}

	public static ProjetoLeiRestDTO dtofyRest(ProjetoLei p) {
		ProjetoLeiRestDTO p2 = new ProjetoLeiRestDTO();
		p2.setId(p.getId());
		p2.setTitulo(p.getTitulo());
		p2.setDescricao(p.getDescricao());
		p2.setAnexoPDF(p.getAnexoPDF());
		p2.setDataLimite(p.getDataLimite());
		p2.setDelegado(p.getDelegado().getNome());
		p2.setTema(p.getTema().getDesignacao());
		p2.setStatus(p.getStatus().toString());
		p2.setApoios(p.getApoios());
		return p2;
	}

	/**
	 * Converts Tema to TemaDTO
	 * @param t the given theme
	 * @return a new object TemaDTO with the same attributes as the given theme
	 */
	public static TemaDTO dtofy(Tema t) {
		TemaDTO t2 = new TemaDTO();
		t2.setId(t.getId());
		t2.setDesignacao(t.getDesignacao());
		return t2;
	}

	/**
	 * Converts TemaDTO to Tema
	 * @param t the given theme
	 * @return a new object Tema with the same attributes as the given theme
	 */
	public static Tema dtofyInvTema(TemaDTO t) {
		Tema t2 = new Tema();
		t2.setId(t.getId());
		t2.setDesignacao(t.getDesignacao());
		return t2;
	}

	/**
	 * Converts Tema list to TemaDTO list
	 * @param temas the given themes
	 * @return a new object {@code List<TemaDTO>} with the same attributes as the given themes
	 */
	public static List<TemaDTO> dtofyListTema(List<Tema> temas) {
		List <TemaDTO> temasDTO = new ArrayList<>();
		for (Tema t : temas) {
			TemaDTO t2 = new TemaDTO();
			t2.setId(t.getId());
			t2.setDesignacao(t.getDesignacao());

			temasDTO.add(t2);
		}
		return temasDTO;
	}
}
