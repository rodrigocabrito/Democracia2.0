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
import com.example.democracia2_desktop.presentation.models.ProjetoLeiPresentation;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PresentationProjetoLeiController {

    private final String PREFIX = "/view/";

    private ProjetoLeiPresentation selectedProjetoLei;
    private DataModel model;
    private Long cidadaoId;

    @FXML
    private ListView<ProjetoLeiPresentation> listView;
    @FXML
    private TextField idField;
    @FXML
    private TextField tituloField;
    @FXML
    private TextField descricaoField;
    @FXML
    private TextField pdfFileField;
    @FXML
    private TextField dataFechoField;
    @FXML
    private TextField delegadoField;
    @FXML
    private TextField temaField;
    @FXML
    private TextField statusField;
    @FXML
    private TextField apoiosField;

    @FXML
    private Button apoiarProjetoLeiButton;
    @FXML
    private Button goToMenuButton;
    @FXML
    private Button goToListarProjetosLeiButton;

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model ;
    }

    public void setAndLoad(DataModel model) {
        setModel(model);
        loadData();

        listView.setItems(model.getProjetosLeiList());

        listView.getSelectionModel().selectedItemProperty().addListener((obs, antigoProjetoLei, novoProjetoLei) ->
                model.setCurrentProjetoLei(novoProjetoLei));

        model.currentProjetoLeiProperty().addListener((obs, antigoProjetoLei, novoProjetoLei) -> {
            if (antigoProjetoLei != null && idField != null) {
                TextFormatter<Long> textFormatter2 = new TextFormatter<>(new LongStringConverter());
                idField.setTextFormatter(textFormatter2);
                textFormatter2.valueProperty().bindBidirectional(antigoProjetoLei.idProperty().asObject());

                tituloField.textProperty().unbindBidirectional(antigoProjetoLei.tituloProperty());
                descricaoField.textProperty().unbindBidirectional(antigoProjetoLei.descricaoProperty());
                pdfFileField.textProperty().unbindBidirectional(antigoProjetoLei.pdfFileProperty());
                dataFechoField.textProperty().unbindBidirectional(antigoProjetoLei.dataLimiteProperty());
                delegadoField.textProperty().unbindBidirectional(antigoProjetoLei.delegadoProperty());
                temaField.textProperty().unbindBidirectional(antigoProjetoLei.temaProperty());
                statusField.textProperty().unbindBidirectional(antigoProjetoLei.statusProperty());

                TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter());
                apoiosField.setTextFormatter(textFormatter);
                textFormatter.valueProperty().bindBidirectional(antigoProjetoLei.apoiosProperty().asObject());
            }
            if (novoProjetoLei == null && idField != null) {
                idField.setText("");
                tituloField.setText("");
                descricaoField.setText("");
                pdfFileField.setText("");
                dataFechoField.setText("");
                delegadoField.setText("");
                temaField.setText("");
                statusField.setText("");
                apoiosField.setText("");
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
    public void goToListarProjetosLeiView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + "listarProjetosLei.fxml"));
            Parent menuView = loader.load();

            PresentationProjetoLeiController controller = loader.getController();
            controller.setAndLoad(model);
            controller.setCidadaoId(getCidadaoId());

            BorderPane root = (BorderPane) listView.getScene().getRoot();

            root.setCenter(menuView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToConsultarProjetoLeiView(MouseEvent event) {
        ProjetoLeiPresentation selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + "consultarProjetoLei.fxml"));
                Parent consultarProjetoLeiView = loader.load();

                PresentationProjetoLeiController consultarProjetoLeiController = loader.getController();
                consultarProjetoLeiController.setSelectedProjetoLei(selectedItem);
                consultarProjetoLeiController.setCidadaoId(getCidadaoId());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dataFechoString = selectedItem.getDataLimite().format(formatter);
                consultarProjetoLeiController.dataFechoField.setText("Data Limite:  " + dataFechoString);


                consultarProjetoLeiController.idField.setText("ID:  " + selectedItem.getId());
                consultarProjetoLeiController.tituloField.setText("Titulo:  " + selectedItem.getTitulo());
                consultarProjetoLeiController.descricaoField.setText("Descrição:  " +selectedItem.getDescricao());
                consultarProjetoLeiController.delegadoField.setText("Delegado Proponente:  " + selectedItem.getDelegado());
                consultarProjetoLeiController.temaField.setText("Tema:  " + selectedItem.getTema());
                consultarProjetoLeiController.apoiosField.setText( "Numero de Apoios:  " + selectedItem.getApoios());
                consultarProjetoLeiController.statusField.setText("Status:  " + selectedItem.getStatus());

                consultarProjetoLeiController.initModel(model);

                BorderPane root = (BorderPane) listView.getScene().getRoot();

                root.setCenter(consultarProjetoLeiView);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddApoioButtonAction(ActionEvent event) {
        model.addApoio(this.cidadaoId);
        goToMenuView(event);
    }

    private void loadData() {
        this.model.loadProjetosLei();
    }

    public void setSelectedProjetoLei(ProjetoLeiPresentation projetoLei) {
        this.selectedProjetoLei = projetoLei;
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

