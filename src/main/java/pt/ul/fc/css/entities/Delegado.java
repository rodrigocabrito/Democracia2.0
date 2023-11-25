package pt.ul.fc.css.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Delegado extends Cidadao{

	@ElementCollection
	@Column(name = "votacaoVoto")
	@JsonIgnore
	private List<String> votacaoVoto = new ArrayList<>();

	@OneToMany(mappedBy = "delegado")
	@JsonIgnore
	private List<ProjetoLei> projetosLeiPropostos = new ArrayList<>();

	//constructors
	public Delegado() {
	}

	//methods
	
	/**
	 * Assigns a given vote to the given voting.
	 * @param votacao given voting
	 * @param voto given vote
	 */
	public void addVotoPublico(Votacao votacao, Voto voto){
		votacaoVoto.add(votacao.getId().toString() + ";" + voto.toString());
	}

	/**
	 * Returns the delegate's vote to a given voting.
	 * @param votacaoId given voting
	 * @return the vote, as {@code Voto.FAVOR} or {@code Voto.CONTRA}.
	 */
	public Voto getVotoByVotacao(Long votacaoId){
		for (String s : votacaoVoto) {
			String[] split = s.split(";");
			if (split[0].equals(votacaoId.toString())) {
				if(split[1].equals(Voto.FAVOR.toString())) {
					return Voto.FAVOR;
				}else {
					return Voto.CONTRA;
				}
			}
		}
		return null;
	}

	public List<String> getVotacaoVoto() {
		return votacaoVoto;
	}

	public void setVotacaoVoto(List<String> votacaoVoto) {
		this.votacaoVoto = votacaoVoto;
	}

	public List<ProjetoLei> getProjetosLeiPropostos() {
		return projetosLeiPropostos;
	}

	public void setProjetosLeiPropostos(List<ProjetoLei> projetosLeiPropostos) {
		this.projetosLeiPropostos = projetosLeiPropostos;
	}
}
