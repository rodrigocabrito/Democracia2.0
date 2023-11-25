package pt.ul.fc.css.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Cidadao {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private int nrCC;

	@NonNull
	private String nome;

	private long tokenAuth;

	@ManyToMany
	@JoinTable(name = "cidadao_projetoLei",
			joinColumns = @JoinColumn(name = "cidadao_id"),
			inverseJoinColumns = @JoinColumn(name = "projetoLei_id"))
	@JsonIgnore
	private List<ProjetoLei> projetosApoiados = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "cidadao_votacao",
			joinColumns = @JoinColumn(name = "cidadao_id"),
			inverseJoinColumns = @JoinColumn(name = "votacao_id"))
	@JsonIgnore
	private List<Votacao> votacoesVotadas = new ArrayList<>();

	@ElementCollection
	private List<String> delegadoCCTemaDesignacao = new ArrayList<>();


	//constructor
	public Cidadao() {
	}

	//methods

	/**
	 * Adds a bill to the list of bills supported.
	 * @param projeto given bill
	 */
	public void addApoioProjeto(ProjetoLei projeto) {
		this.projetosApoiados.add(projeto);
	}

	/**
	 * Checks if a citizen it's a supporter of the given bill.
	 * @param projeto given bill
	 * @return {@code true} if it's a supporter, {@code false} otherwise.
	 */
	public boolean isApoiante(ProjetoLei projeto) {
		return this.projetosApoiados.contains(projeto);
	}

	/**
	 * Assigns a delegate to a specific theme given.
	 * @param delegadoCC given delegate nrCC
	 * @param temaDesignation given theme designation
	 */
	public void addDelegadoTema(int delegadoCC, String temaDesignation) {
		this.delegadoCCTemaDesignacao.add(delegadoCC + ";" + temaDesignation);
	}

	/**
	 * Checks if a given delegate is assigned to any theme.
	 * @param delegadoCC given delegate CC number
	 * @return {@code true} if it's assigned to any theme, {@code false} otherwise.
	 */
	public boolean hasDelegadoAssignedToAnyTheme(int delegadoCC) {
		for (String s: delegadoCCTemaDesignacao) {
			if(s.contains(Integer.toString(delegadoCC))){
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the delegate CC number given its assigned theme, if there is one.
	 * @param designation given theme designation
	 * @return the {@code Delegado} assigned to the theme, -1 otherwise.
	 */
	public int getDelegadoCcByTema(String designation){
		for (String s : delegadoCCTemaDesignacao) {
			String[] split = s.split(";");
			if (split[1].equals(designation)) {
				return Integer.parseInt(split[0]);
			}
		}
		return -1;
	}

	/**
	 * Adds a voting to the list of voting already voted by the citizen.
	 * @param votacao given voting
	 */
	public void addVotoVotacao(Votacao votacao){
		this.votacoesVotadas.add(votacao);
	}

	/**
	 * Checks if a citizen has voted in a given voting.
	 * @param votacao given voting
	 * @return {@code True} if it has, {@code False} otherwise.
	 */
	public boolean hasVoted(Votacao votacao){
		return this.votacoesVotadas.contains(votacao);
	}


	// getter and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNrCC() {
		return nrCC;
	}

	public void setNrCC(int nrCC) {
		this.nrCC = nrCC;
	}

	@NonNull
	public String getNome() {
		return nome;
	}

	public List<ProjetoLei> getProjetosApoiados() {
		return projetosApoiados;
	}

	public void setProjetosApoiados(List<ProjetoLei> projetosApoiados) {
		this.projetosApoiados = projetosApoiados;
	}

	public List<Votacao> getVotacoesVotadas() {
		return votacoesVotadas;
	}

	public void setVotacoesVotadas(List<Votacao> votacoesVotadas) {
		this.votacoesVotadas = votacoesVotadas;
	}

	public List<String> getDelegadoCCTemaDesignacao() {
		return delegadoCCTemaDesignacao;
	}

	public void setDelegadoCCTemaDesignacao(List<String> delegadoCCTemaDesignacao) {
		this.delegadoCCTemaDesignacao = delegadoCCTemaDesignacao;
	}

	public void setNome(@NonNull String nome) {
		this.nome = nome;
	}

	public long getTokenAuth() {
		return tokenAuth;
	}

	public void setTokenAuth(long tokenAuth) {
		this.tokenAuth = tokenAuth;
	}

}
