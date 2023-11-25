package pt.ul.fc.css.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Votacao {

	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne(cascade = CascadeType.PERSIST)
	private ProjetoLei projetoLei;

	@Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
	private LocalDateTime dataFecho;
	
	private int votosAFavor;
	private int votosContra;

	@ManyToMany(mappedBy = "votacoesVotadas")
	@JsonIgnore
	private List<Cidadao> cidadaosVotantes = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "delegado_id", nullable = false)
	private Delegado delegadoProp;
	
	@ElementCollection
	private List<Integer> listaIdsDelegadoVoted = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Enumerated(EnumType.STRING)
	private VotacaoResult votacaoResult;

	//constructor
	public Votacao() {
	}

	//methods

	/**
	 * Adds a vote to the voting.
	 * @param cidadao the given citizen
	 * @param voto the given voto
	 */
	public void addVoto(Cidadao cidadao, Voto voto){
		if(voto == Voto.CONTRA){
			this.votosContra++;
		}else if(voto == Voto.FAVOR){
			this.votosAFavor++;
		}
		cidadaosVotantes.add(cidadao);
	}

	/**
	 * Calculates the subtraction between in favor votes and against votes.
	 * @return the Integer value of the difference between in favor votes and against votes.
	 */
	public int getResultadoVotacao(){
		return this.votosAFavor-this.votosContra; // result > 0 ? in favor : against
	}
	
	/**
	 * Adds a delegate to the list of delegates that already voted in the voting.
	 * @param delegadoCC given delegate's CC number
	 */
	public void addDelegadoVoted(int delegadoCC){
		this.listaIdsDelegadoVoted.add(delegadoCC);
	}

	/**
	 * Checks if the date of the voting has expired.
	 * @return {@code true} if the date is before {@code LocalDateTime.now()},
	 * {@code false} otherwise.
	 */
	public boolean hasDateExpired(){
		return this.dataFecho.isBefore(LocalDateTime.now());
	}
	
	
	//getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public ProjetoLei getProjetoLei() {
		return projetoLei;
	}

	public void setProjetoLei(ProjetoLei projetoLei) {
		this.projetoLei = projetoLei;
	}
	
	public LocalDateTime getDataFecho() {
		return dataFecho;
	}
	
	public void setDataFecho(LocalDateTime dataFecho) {
		if (dataFecho.isAfter(LocalDateTime.now().plusMonths(2))) {
			this.dataFecho = LocalDateTime.now().plusMonths(2);
		} else if (dataFecho.isBefore(LocalDateTime.now().plusDays(15))){
			this.dataFecho = LocalDateTime.now().plusDays(15);
		} else {
			this.dataFecho= dataFecho;
		}
	}

	public void setDataFechoTest(LocalDateTime dataFecho) {
		this.dataFecho = dataFecho;
	}
	
	public int getVotosAFavor() {
		return votosAFavor;
	}
	
	public int getVotosContra() {
		return votosContra;
	}

	public void setDelegadoProp(Delegado delegadoProp) {
		this.delegadoProp = delegadoProp;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public VotacaoResult getVotacaoResult() {
		return votacaoResult;
	}

	public void setVotacaoResult(VotacaoResult votacaoResult) {
		this.votacaoResult = votacaoResult;
	}

	public void setVotosAFavor(int votosAFavor) {
		this.votosAFavor = votosAFavor;
	}

	public void setVotosContra(int votosContra) {
		this.votosContra = votosContra;
	}

	public List<Cidadao> getCidadaosVotantes() {
		return cidadaosVotantes;
	}

	public void setCidadaosVotantes(List<Cidadao> cidadaosVotantes) {
		this.cidadaosVotantes = cidadaosVotantes;
	}

	public Delegado getDelegadoProp() {
		return delegadoProp;
	}

	public List<Integer> getListaIdsDelegadoVoted() {
		return listaIdsDelegadoVoted;
	}
}


