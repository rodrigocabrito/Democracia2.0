package com.example.democracia2_desktop.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import com.example.democracia2_desktop.presentation.models.DataModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginController {

    private final String PREFIX = "/view/";

    private DataModel model;
    private Long cidadaoId;

    @FXML
    private TextField nrCCField;

    @FXML
    private Button loginAndGoToMenuButton;

    @FXML
    private Button exitButton;


    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
    }

    @FXML
    private void loginAndGoToMenuView(MouseEvent event) {

        String inputText = nrCCField.getText();
        int cidadaoCC = 0;
        boolean validCC = false;

        while (!validCC) {
            try {
                cidadaoCC = Integer.parseInt(inputText);
                validCC = true;
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro Login");
                alert.setHeaderText("Número de Cartão de Cidadão inválido!");
                alert.setContentText("Inserir apenas números por favor!");

                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {
                    nrCCField.clear();
                    return;
                }
            }
        }

        cidadaoId = login(cidadaoCC);

        if (cidadaoId == -1L) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso Login");
            alert.setHeaderText("Número de Cartão de Cidadão não encontrado!");
            alert.setContentText("Inserir um número de Cartão de Cidadão que esteja registado!");

            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                nrCCField.clear();
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login");
        alert.setHeaderText("Bem vindo!");
        alert.setContentText("Login efetuado com sucesso!");
        alert.showAndWait();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + "menu.fxml"));
            Parent anotherView = loader.load();

            MenuController controller = loader.getController();
            controller.setModel(model);
            controller.setCidadaoId(cidadaoId);

            BorderPane mainView = (BorderPane) loginAndGoToMenuButton.getScene().getRoot();
            mainView.setCenter(anotherView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long login(int nrCC) {
        try {

            String baseUrl = "http://localhost:8080/api";
            String path = "/login/{nrCC}";
            String CC = Integer.toString(nrCC);

            String urlPath = baseUrl + path.replace("{nrCC}", CC);

            URL url = new URL(urlPath);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                String response = responseBuilder.toString();

                ObjectMapper objectMapper = new ObjectMapper();
                //objectMapper.registerModule(new JavaTimeModule());
                return objectMapper.readValue(response, Long.class);
            } else {
                System.err.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    @FXML
    private void exit(ActionEvent event) {
        exitButton.getScene().getWindow().hide();
    }

    public void setModel(DataModel model) {
        this.model = model;
    }
}
