package pt.ul.fc.css.facade.dtos;

import org.springframework.stereotype.Component;

@Component
public class TemaDTO {

    private Long id;
    private String designacao;

    //constructor
    public TemaDTO() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemaDTO tema = (TemaDTO) o;
        return designacao.equals(tema.designacao);
    }


    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

}

