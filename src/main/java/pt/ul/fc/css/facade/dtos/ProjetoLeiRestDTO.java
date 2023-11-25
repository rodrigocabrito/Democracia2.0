package pt.ul.fc.css.facade.dtos;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import pt.ul.fc.css.entities.Cidadao;


@Component
public class ProjetoLeiRestDTO{

    private Long id;
    private String titulo;
    private String descricao;
    private byte[] anexoPDF;
    private LocalDateTime dataLimite;
    private String delegado;
    private String tema;
    private String status;
    private int apoios;

    //constructor
    public ProjetoLeiRestDTO() {
    }

    /**
     * Verifies if a given date is between {@code LocalDateTime.now()}
     * and {@code LocalDateTime.now().plusYears(1)}
     * @param data given date
     * @return {@code true} if it is, {@code false} otherwise.
     */
    public boolean isValidDate(LocalDateTime data){
        LocalDateTime currentData = LocalDateTime.now();
        return data.isAfter(currentData) && data.isBefore(currentData.plusYears(1));
    }

    /**
     * Verifies if the given date has expired.
     * @param data given date
     * @return {@code true} if the date is before {@code LocalDateTime.now()},
     * {@code false} otherwise.
     */
    public boolean hasExpired(LocalDateTime data) {
        LocalDateTime today = LocalDateTime.now();
        return data.isBefore(today);
    }

    /**
     * Increments the number of supporters of the bill and adds the citizen to the list of
     * citizens supporting the bill.
     * @param cidadao the given citizen
     */
    public void addApoio(Cidadao cidadao) {
        this.apoios++;
    }

    /**
     * Increments the number of delegates supporting the bill.
     */
    public void addApoioDelegado() {
        this.apoios++;
    }


    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getAnexoPDF() {
        return anexoPDF;
    }

    public void setAnexoPDF(byte[] anexoPDF) {
        this.anexoPDF = anexoPDF;
    }

    public LocalDateTime getDataLimite() {
        return dataLimite.truncatedTo(ChronoUnit.SECONDS);
    }

    public void setDataLimite(LocalDateTime dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getDelegado() {
        return this.delegado;
    }

    public void setDelegado(String delegado) {
        this.delegado = delegado;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public int getApoios() {
        return this.apoios;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void setApoios(int apoios) {
        this.apoios = apoios;
    }

    @Override
    public String toString() {
        return "ProjetoLei: \n" +
                "-> id = " + id + "\n" +
                "-> titulo = " + titulo + "\n" +
                "-> descricao = " + descricao + "\n" +
                "-> anexoPdf = " + Arrays.toString(anexoPDF) + "\n" +
                "-> data = " + dataLimite.toString() + "\n" +
                "-> delegado = " + getDelegado() + "\n" +
                "-> tema = " + getTema() + "\n" +
                "-> apoios = " + apoios + "\n" +
                "-> status = " + status;
    }
}

