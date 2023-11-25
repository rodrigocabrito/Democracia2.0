package pt.ul.fc.css.facade.dtos;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import pt.ul.fc.css.entities.Cidadao;
import pt.ul.fc.css.entities.Delegado;
import pt.ul.fc.css.entities.Status;
import pt.ul.fc.css.entities.Tema;


@Component
public class ProjetoLeiDTO{

    private Long id;
    private String titulo;
    private String descricao;
    private byte[] anexoPDF;
    private LocalDateTime dataLimite;
    private List<Cidadao> cidadaosApoiantes = new ArrayList<>();
    private Delegado delegado;
    private Tema tema;
    private Status status;
    private int apoios;

    //constructor
    public ProjetoLeiDTO() {
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
        this.cidadaosApoiantes.add(cidadao);
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

    public Delegado getDelegado() {
        return this.delegado;
    }

    public void setDelegado(Delegado delegado) {
        this.delegado = delegado;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public int getApoios() {
        return this.apoios;
    }

    public void setApoiosTest(int apoios){
        this.apoios = apoios;
    }// apenas para uso em testes

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Cidadao> getCidadaosApoiantes() {
        return cidadaosApoiantes;
    }

    public void setCidadaosApoiantes(List<Cidadao> cidadaosApoiantes) {
        this.cidadaosApoiantes = cidadaosApoiantes;
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
                "-> delegado = " + delegado.getNome() + "\n" +
                "-> tema = " + tema.getDesignacao() + "\n" +
                "-> apoios = " + apoios + "\n" +
                "-> status = " + status.toString();
    }
}
