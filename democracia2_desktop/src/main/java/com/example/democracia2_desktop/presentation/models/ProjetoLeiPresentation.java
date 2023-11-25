package com.example.democracia2_desktop.presentation.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class ProjetoLeiPresentation {

    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty titulo = new SimpleStringProperty();
    private final StringProperty descricao = new SimpleStringProperty();
    private final ObjectProperty<byte[]> pdfFile = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> dataLimite = new SimpleObjectProperty<>();
    private final StringProperty delegado = new SimpleStringProperty();
    private final StringProperty tema = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final IntegerProperty apoios = new SimpleIntegerProperty();

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

    public final StringProperty tituloProperty() {
        return this.titulo;
    }

    public final String getTitulo() {
        return this.tituloProperty().get();
    }

    public final void setTitulo(final String titulo) {
        this.tituloProperty().set(titulo);
    }

    //----------------------------------------------------------------

    public final StringProperty descricaoProperty() {
        return this.descricao;
    }

    public final String getDescricao() {
        return this.descricaoProperty().get();
    }

    public final void setDescricao(final String descricao) {
        this.descricaoProperty().set(descricao);
    }

    //----------------------------------------------------------------

    public final ObjectProperty<byte[]> pdfFileProperty() { return this.pdfFile; }

    public final byte[] getPdfFile() { return this.pdfFileProperty().get(); }

    public final void setPdfFile(final byte[] pdfFile) { this.pdfFileProperty().set(pdfFile); }

    //----------------------------------------------------------------

    public final ObjectProperty<LocalDateTime> dataLimiteProperty() { return this.dataLimite; }

    public final LocalDateTime getDataLimite() { return this.dataLimiteProperty().get(); }

    public final void setDataLimite(final LocalDateTime dataLimite) { this.dataLimiteProperty().set(dataLimite); }

    //----------------------------------------------------------------

    public final StringProperty delegadoProperty() { return this.delegado; }

    public final String getDelegado() { return this.delegadoProperty().get(); }

    public final void setDelegado(final String delegado) { this.delegadoProperty().set(delegado); }

    //----------------------------------------------------------------

    public final StringProperty temaProperty() { return this.tema; }

    public final String getTema() { return this.temaProperty().get(); }

    public final void setTema(final String tema) { this.temaProperty().set(tema); }

    //----------------------------------------------------------------

    public final StringProperty statusProperty() { return this.status; }

    public final String getStatus() { return this.statusProperty().get(); }

    public final void setStatus(final String status) { this.statusProperty().set(status); }

    //----------------------------------------------------------------

    public final IntegerProperty apoiosProperty() { return this.apoios; }

    public final int getApoios() { return this.apoiosProperty().get(); }

    public final void setApoios(final int apoios) { this.apoiosProperty().set(apoios); }

    //----------------------------------------------------------------

    public ProjetoLeiPresentation(Long id, String titulo, String descricao, byte[] pdfFile, LocalDateTime dataLimite,
                                  String delegado, String tema, String status, int apoios) {
        setId(id);
        setTitulo(titulo);
        setDescricao(descricao);
        setPdfFile(pdfFile);
        setDataLimite(dataLimite);
        setDelegado(delegado);
        setTema(tema);
        setStatus(status);
        setApoios(apoios);
    }

    @Override
    public String toString() {
        return "ProjetoLei: \n" +
                "-> id = " + getId() + "\n" +
                "-> titulo = " + getTitulo() + "\n" +
                "-> descricao = " + getDescricao() + "\n" +
                "-> data limite = " + getDataLimite() + "\n" +
                "-> delegado = " + getDelegado() + "\n" +
                "-> tema = " + getTema() + "\n" +
                "-> apoios = " + getApoios() + "\n" +
                "-> status = " + getStatus();
    }
}
