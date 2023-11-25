package com.example.democracia2_desktop.presentation.controllers;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import com.example.democracia2_desktop.presentation.models.DataModel;

public class MenuController {

    private final String PREFIX = "/view/";
    private Long cidadaoId;
    private DataModel model;
    @FXML
    private MenuBar menuBar;
    @FXML
    private BorderPane mainView;
    @FXML
    private Button goToListarProjetosLeiButton = new Button();
    @FXML
    private Button goToListarVotacoesButton = new Button();
    @FXML
    private Button logoutButton = new Button();

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
    }

    @FXML
    public void goToListarProjetosLeiView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + "listarProjetosLei.fxml"));
            Parent anotherView = loader.load();

            PresentationProjetoLeiController controller = loader.getController();
            controller.setAndLoad(model);
            controller.setCidadaoId(getCidadaoId());

            mainView.setCenter(anotherView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToListarVotacoesView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + "listarVotacoes.fxml"));
            Parent anotherView = loader.load();

            PresentationVotacaoController controller = loader.getController();
            controller.setAndLoad(model);
            controller.setCidadaoId(getCidadaoId());

            mainView.setCenter(anotherView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToLoginView(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Caro cidadao " + cidadaoId);
        alert.setContentText("Esperamos que a experência na app tenha sido agradável !");
        alert.showAndWait();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + "login.fxml"));
            Parent anotherView = loader.load();

            LoginController controller = loader.getController();
            controller.setModel(model);

            mainView.setCenter(anotherView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void load(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(menuBar.getScene().getWindow());
        if (file != null) {
            try {
                model.loadProjetosLei();
                model.loadVotacoes();
            } catch (Exception exc) {
                // handle exception...
            }
        }
    }

    @FXML
    public void exit(ActionEvent event) {
        menuBar.getScene().getWindow().hide();
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