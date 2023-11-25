package com.example.democracia2_desktop.presentation.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class VotacaoPresentation {

    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty projetoLei = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> dataFecho = new SimpleObjectProperty<>();
    private final IntegerProperty votosAFavor = new SimpleIntegerProperty();
    private final IntegerProperty votosContra = new SimpleIntegerProperty();
    private final StringProperty delegadoProp = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty votacaoResult = new SimpleStringProperty();

    //----------------------------------------------------------------

    // getters

    public final LongProperty idProperty() {
        return this.id;
    }

    public final Long getId() {
        return this.idProperty().get();
    }

    public final void setId(final Long id) {
        this.idProperty().set(id);
    }

    //----------------------------------------------------------------

    public final StringProperty projetoLeiProperty() {
        return this.projetoLei;
    }

    public final String getProjetoLei() {
        return this.projetoLeiProperty().get();
    }

    public final void setProjetoLei(final String projetoLei) {
        this.projetoLeiProperty().set(projetoLei);
    }

    //----------------------------------------------------------------

    public final ObjectProperty<LocalDateTime> dataFechoProperty() { return this.dataFecho; }

    public final LocalDateTime getDataFecho() { return this.dataFechoProperty().get(); }

    public final void setDataFecho(final LocalDateTime dataFecho) { this.dataFechoProperty().set(dataFecho); }

    //----------------------------------------------------------------

    public final IntegerProperty votosAFavorProperty() { return this.votosAFavor; }

    public final int getVotosAFavor() { return this.votosAFavorProperty().get(); }

    public final void setVotosAFavor(final int votosAFavor) { this.votosAFavorProperty().set(votosAFavor); }

    //----------------------------------------------------------------

    public final IntegerProperty votosContraProperty() { return this.votosContra; }

    public final int getvotosContra() { return this.votosContraProperty().get(); }

    public final void setVotosContra(final int votosContra) { this.votosContraProperty().set(votosContra); }

    //----------------------------------------------------------------

    public final StringProperty delegadoPropProperty() { return this.delegadoProp; }

    public final String getDelegadoProp() { return this.delegadoPropProperty().get(); }

    public final void setDelegadoProp(final String delegadoProp) { this.delegadoPropProperty().set(delegadoProp); }

    //----------------------------------------------------------------

    public final StringProperty statusProperty() { return this.status; }

    public final String getStatus() { return this.statusProperty().get(); }

    public final void setStatus(final String status) { this.statusProperty().set(status); }

    //----------------------------------------------------------------

    public final StringProperty votacaoResultProperty() { return this.votacaoResult; }

    public final String getVotacaoResult() { return this.votacaoResultProperty().get(); }

    public final void setVotacaoResult(final String votacaoResult) { this.votacaoResultProperty().set(votacaoResult); }

    //----------------------------------------------------------------

    public VotacaoPresentation(Long id, String projetoLei, LocalDateTime dataFecho, int votosAFavor,
                               int votosContra, String delegadoProp, String status, String votacaoResult) {
        setId(id);
        setProjetoLei(projetoLei);
        setDataFecho(dataFecho);
        setVotosAFavor(votosAFavor);
        setVotosContra(votosContra);
        setDelegadoProp(delegadoProp);
        setStatus(status);
        setVotacaoResult(votacaoResult);
    }

    @Override
    public String toString() {

        return "Votacao: \n" +
                "-> id = " + getId() + "\n" +
                "-> Projeto Lei = " + getProjetoLei() + "\n" +
                "-> Data limite = " + getDataFecho() + "\n" +
                "-> Votos a favor = " + getVotosAFavor() + "\n" +
                "-> Votos contra = " + getvotosContra() + "\n" +
                "-> Delegado = " + getDelegadoProp() + "\n" +
                "-> Status = " + getStatus() + "\n" +
                "-> Resultado = " + getVotacaoResult();
    }
}
