package pt.ul.fc.css.entities;

import jakarta.persistence.*;

@Entity
public class Tema {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String designacao;
	

	//constructor
	public Tema() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tema tema = (Tema) o;
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

