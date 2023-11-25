package pt.ul.fc.css.facade.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import pt.ul.fc.css.entities.*;

@Component
public class VotacaoRestDTO {

    private Long id;
    private String projetoLei;
    private LocalDateTime dataFecho;
    private int votosAFavor;
    private int votosContra;
    private List<Cidadao> cidadaosVotantes = new ArrayList<>();
    private String delegadoProp;
    private List<Integer> listaIdsDelegadoVoted = new ArrayList<>();
    private String status;
    private String votacaoResult;

    //constructor
    public VotacaoRestDTO() {
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

    public String getProjetoLei() {
        return projetoLei;
    }

    public void setProjetoLei(String projetoLei) {
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

    public void setDelegadoProp(String delegadoProp) {
        this.delegadoProp = delegadoProp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVotacaoResult() {
        return votacaoResult;
    }

    public void setVotacaoResult(String votacaoResult) {
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

    public String getDelegadoProp() {
        return delegadoProp;
    }

    public List<Integer> getListaIdsDelegadoVoted() {
        return listaIdsDelegadoVoted;
    }

    public void setListaIdsDelegadoVoted(List<Integer> listaIdsDelegadoVoted) {
        this.listaIdsDelegadoVoted = listaIdsDelegadoVoted;
    }

}



