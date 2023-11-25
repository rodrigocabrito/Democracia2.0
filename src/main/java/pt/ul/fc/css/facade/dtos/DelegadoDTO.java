package pt.ul.fc.css.facade.dtos;

import org.springframework.stereotype.Component;
import pt.ul.fc.css.entities.ProjetoLei;
import pt.ul.fc.css.entities.Voto;

import java.util.ArrayList;
import java.util.List;

@Component
public class DelegadoDTO extends CidadaoDTO{

    private List<String> votacaoVoto = new ArrayList<>();
    private List<ProjetoLei> projetosLeiPropostos = new ArrayList<>();

    //constructors
    public DelegadoDTO() {
    }

    //methods

    /**
     * Assigns a given vote to the given voting.
     * @param votacao given voting
     * @param voto given vote
     */
    public void addVotoPublico(VotacaoDTO votacao, Voto voto){
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
