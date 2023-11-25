package com.example.democracia2_desktop.presentation.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import com.example.democracia2_desktop.presentation.models.DataModel;
import com.example.democracia2_desktop.presentation.models.VotacaoPresentation;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PresentationVotacaoController {

    private final String PREFIX = "/view/";
    private VotacaoPresentation selectedVotacao;
    @FXML
    private ListView<VotacaoPresentation> listView ;
    private Long cidadaoId;

    @FXML
    private TextField idField;
    @FXML
    private TextField projetoLeiField;
    @FXML
    private TextField dataFechoField;
    @FXML
    private TextField votosAFavorField;
    @FXML
    private TextField votosContraField;
    @FXML
    private TextField delegadoPropField;
    @FXML
    private TextField statusField;
    @FXML
    private TextField votacaoResultField;


    @FXML
    private Button goToMenuButton = new Button();
    @FXML
    private Button goToListarVotacoesButton;
    @FXML
    private Button goToConsultarVotacaoButton;
    @FXML
    private Button votarVotacaoButtonF;
    @FXML
    private Button votarVotacaoButtonC;

    private DataModel model;

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }

        this.model = model ;
    }

    public void setAndLoad(DataModel model) {
        setModel(model);
        loadData();

        listView.setItems(model.getVotacoesList());

        listView.getSelectionModel().selectedItemProperty().addListener((obs, antigaVotacao, novaVotacao) ->
                model.setCurrentVotacao(novaVotacao));

        model.currentVotacaoProperty().addListener((obs, antigaVotacao, novaVotacao) -> {
            if (antigaVotacao != null && idField != null) {
                TextFormatter<Long> textFormatter = new TextFormatter<>(new LongStringConverter());
                idField.setTextFormatter(textFormatter);
                textFormatter.valueProperty().bindBidirectional(antigaVotacao.idProperty().asObject());

                projetoLeiField.textProperty().unbindBidirectional(antigaVotacao.projetoLeiProperty());
                dataFechoField.textProperty().unbindBidirectional(antigaVotacao.dataFechoProperty());

                TextFormatter<Integer> textFormatter2 = new TextFormatter<>(new IntegerStringConverter());
                votosAFavorField.setTextFormatter(textFormatter2);
                textFormatter2.valueProperty().bindBidirectional(antigaVotacao.votosAFavorProperty().asObject());

                TextFormatter<Integer> textFormatter3 = new TextFormatter<>(new IntegerStringConverter());
                votosContraField.setTextFormatter(textFormatter3);
                textFormatter3.valueProperty().bindBidirectional(antigaVotacao.votosContraProperty().asObject());

                delegadoPropField.textProperty().unbindBidirectional(antigaVotacao.delegadoPropProperty());
                statusField.textProperty().unbindBidirectional(antigaVotacao.statusProperty());
                votacaoResultField.textProperty().unbindBidirectional(antigaVotacao.votacaoResultProperty());
            }
            if (novaVotacao == null && idField != null) {
                idField.setText("");
                projetoLeiField.setText("");
                dataFechoField.setText("");
                votosAFavorField.setText("");
                votosContraField.setText("");
                delegadoPropField.setText("");
                statusField.setText("");
                votacaoResultField.setText("");
            }
        });
    }

    @FXML
    private void goToMenuView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + "menu.fxml"));
            Parent menuView = loader.load();

            MenuController controller = loader.getController();
            controller.setModel(model);
            controller.setCidadaoId(getCidadaoId());

            BorderPane root = (BorderPane) listView.getScene().getRoot();

            root.setCenter(menuView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToListarVotacoesView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + "listarVotacoes.fxml"));
            Parent menuView = loader.load();

            PresentationVotacaoController controller = loader.getController();
            controller.setAndLoad(model);
            controller.setCidadaoId(getCidadaoId());

            BorderPane root = (BorderPane) listView.getScene().getRoot();


            root.setCenter(menuView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToConsultarVotacaoView(MouseEvent event) {
        VotacaoPresentation selectedItem = listView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + "consultarVotacao.fxml"));
                Parent consultarVotacaoView = loader.load();

                PresentationVotacaoController consultarVotacaoController = loader.getController();
                consultarVotacaoController.setSelectedVotacao(selectedItem);
                consultarVotacaoController.setCidadaoId(getCidadaoId());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dataFechoString = selectedItem.getDataFecho().format(formatter);
                consultarVotacaoController.dataFechoField.setText("Data Limite:  " + dataFechoString);

                String statusString = selectedItem.getStatus();
                consultarVotacaoController.statusField.setText("Status:  " + statusString);

                String resultadoString = selectedItem.getVotacaoResult();
                consultarVotacaoController.votacaoResultField.setText("Resultado:  " + resultadoString);

                consultarVotacaoController.idField.setText("ID:  " + selectedItem.getId());
                consultarVotacaoController.projetoLeiField.setText("Projeto Lei:  " + selectedItem.getProjetoLei());
                consultarVotacaoController.delegadoPropField.setText("Delegado Proponente:  " + selectedItem.getDelegadoProp());
                consultarVotacaoController.votosAFavorField.setText("Votos a favor:  " + selectedItem.getVotosAFavor());
                consultarVotacaoController.votosContraField.setText( "Votos contra:  " + selectedItem.getvotosContra());


                consultarVotacaoController.initModel(model);

                BorderPane root = (BorderPane) listView.getScene().getRoot();

                root.setCenter(consultarVotacaoView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void votarVotacaoFavor(ActionEvent event) {
        model.addVoto("FAVOR", this.cidadaoId);
        goToMenuView(event);
    }
    @FXML
    private void votarVotacaoContra(ActionEvent event) {
        model.addVoto("CONTRA", this.cidadaoId);
        goToMenuView(event);
    }

    private void loadData() {
        this.model.loadVotacoes();
    }

    public void setSelectedVotacao(VotacaoPresentation votacao) {
        this.selectedVotacao = votacao;
    }

    public void setModel(DataModel model) {
        this.model = model;
    }

    public void setCidadaoId(Long id) {
        this.cidadaoId = id;
    }

    public Long getCidadaoId() {
        return this.cidadaoId;
    }
}

