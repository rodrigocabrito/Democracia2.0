package com.example.democracia2_desktop.presentation.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class DataModel {


    private Long cidadaoId;

    private final ObservableList<ProjetoLeiPresentation> projetoLeiList =
            FXCollections.observableArrayList(projetoLei ->
                    new Observable[] {projetoLei.idProperty(), projetoLei.tituloProperty(), projetoLei.descricaoProperty(),
                            projetoLei.dataLimiteProperty(), projetoLei.delegadoProperty(),
                            projetoLei.temaProperty(), projetoLei.statusProperty(), projetoLei.apoiosProperty()});
    public ObservableList<ProjetoLeiPresentation> getProjetosLeiList() {
        return projetoLeiList;
    }

    private final ObjectProperty<ProjetoLeiPresentation> currentProjetoLei = new SimpleObjectProperty<>(null);

    public ObjectProperty<ProjetoLeiPresentation> currentProjetoLeiProperty() {
        return currentProjetoLei;
    }

    public final ProjetoLeiPresentation getCurrentProjetoLei() { return currentProjetoLeiProperty().get(); }

    public final void setCurrentProjetoLei(ProjetoLeiPresentation projetoLei) {
        currentProjetoLeiProperty().set(projetoLei);
    }

    private final Map<String, Set<Long>> apoioRegistado = new HashMap<>();

    private void showHasApoiadoWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Apoiar Projeto Lei");
        alert.setHeaderText("Caro cidadão");
        alert.setContentText("Já apoiou este Projeto Lei!");
        alert.showAndWait();
    }

    public void addApoio(Long cidadaoId) {
        ProjetoLeiPresentation projeto = getCurrentProjetoLei();

        try {
            String baseUrl = "http://localhost:8080/api";
            String path1 = "/projetosLei/{projetoLeiId}";
            String path2 = "/apoiaProjetoLei/{cidadaoId}";
            String projetoLeiId = String.valueOf(projeto.getId());
            String id = String.valueOf(cidadaoId);

            if (apoioRegistado.containsKey(projetoLeiId)) {
                Set<Long> cidadaoVoted = apoioRegistado.get(projetoLeiId);
                if (cidadaoVoted.contains(cidadaoId)) {
                    showHasApoiadoWarning();
                    return;
                }
            } else {
                apoioRegistado.put(projetoLeiId, new HashSet<>());
            }

            String urlPath = baseUrl + path1.replace("{projetoLeiId}", projetoLeiId) + path2.replace("{cidadaoId}", id);

            URL url = new URL(urlPath);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("PUT");

            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                apoioRegistado.get(projetoLeiId).add(cidadaoId);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Apoiar Projeto Lei");
                alert.setHeaderText("Caro cidadao " + cidadaoId);
                alert.setContentText("O seu apoio foi registado com sucesso!");
                alert.showAndWait();

            } else {
                System.err.println("Error: " + responseCode);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Apoiar Projeto Lei Erro");
                alert.setHeaderText("Caro cidadao " + cidadaoId);
                alert.setContentText("Erro ao registar o seu apoio :(");
                alert.showAndWait();
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------------------------------

    private final ObservableList<VotacaoPresentation> votacaoList =
            FXCollections.observableArrayList(votacao ->
                    new Observable[] {votacao.idProperty(), votacao.projetoLeiProperty(), votacao.dataFechoProperty(),
                    votacao.votosAFavorProperty(), votacao.votosContraProperty(), votacao.delegadoPropProperty(),
                    votacao.statusProperty(), votacao.votacaoResultProperty()});
    public ObservableList<VotacaoPresentation> getVotacoesList() {
        return votacaoList;
    }

    private final ObjectProperty<VotacaoPresentation> currentVotacao = new SimpleObjectProperty<>(null);

    public ObjectProperty<VotacaoPresentation> currentVotacaoProperty() {
        return currentVotacao;
    }

    public final VotacaoPresentation getCurrentVotacao() {
        return currentVotacaoProperty().get();
    }

    public final void setCurrentVotacao(VotacaoPresentation votacao) {
        currentVotacaoProperty().set(votacao);
    }

    private final Map<String, Set<Long>> votoRegistado = new HashMap<>();

    private void showHasVotedWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Votar Votação");
        alert.setHeaderText("Caro cidadão");
        alert.setContentText("Já votou nesta votação!");
        alert.showAndWait();
    }


    public void addVoto(String voto, Long cidadaoId) {
        VotacaoPresentation votacao = getCurrentVotacao();

        try {
            String baseUrl = "http://localhost:8080/api";
            String path1 = "/votacoes/{votacaoId}";
            String path2 = "/votarVotacao/{cidadaoId}";
            String path3 = "/{voto}";
            String votacaoId = String.valueOf(votacao.getId());
            String id = String.valueOf(cidadaoId);
            String urlPath;

            if (votoRegistado.containsKey(votacaoId)) {
                Set<Long> cidadaoVoted = votoRegistado.get(votacaoId);
                if (cidadaoVoted.contains(cidadaoId)) {
                    showHasVotedWarning();
                    return;
                }
            } else {
                votoRegistado.put(votacaoId, new HashSet<>());
            }

            if (voto.equals("FAVOR")) {
                urlPath = baseUrl + path1.replace("{votacaoId}", votacaoId) +
                        path2.replace("{cidadaoId}", id) + path3.replace("{voto}", "FAVOR");
            } else {
                urlPath = baseUrl + path1.replace("{votacaoId}", votacaoId) +
                        path2.replace("{cidadaoId}", id) + path3.replace("{voto}", "CONTRA");
            }

            URL url = new URL(urlPath);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("PUT");

            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                votoRegistado.get(votacaoId).add(cidadaoId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Votar Votação");
                alert.setHeaderText("Caro cidadao " + cidadaoId);
                alert.setContentText("O seu voto foi registado com sucesso!");
                alert.showAndWait();

            } else {
                System.err.println("Error: " + responseCode);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Votar Votação Erro");
                alert.setHeaderText("Caro cidadao " + cidadaoId);
                alert.setContentText("Erro ao registar o seu voto :(");
                alert.showAndWait();
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------------------------------

    // load all projetosLei
    public void loadProjetosLei() {
        projetoLeiList.clear();
        try {

            URL url = new URL("http://localhost:8080/api/projetosLei");

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
                Map<String, Object>[] projetosLei = objectMapper.readValue(response, Map[].class);

                for (Map<String, Object> projetoLei : projetosLei) {
                    Long id = Long.parseLong(projetoLei.get("id").toString());
                    String titulo = projetoLei.get("titulo").toString();
                    String descricao = projetoLei.get("descricao").toString();
                    String pdfFile = projetoLei.get("anexoPDF").toString(); // Assuming the field is byte[]
                    LocalDateTime dataLimite = LocalDateTime.parse(projetoLei.get("dataLimite").toString());
                    String delegado = projetoLei.get("delegado").toString();
                    String tema = projetoLei.get("tema").toString();
                    String status = projetoLei.get("status").toString();
                    int apoios = Integer.parseInt(projetoLei.get("apoios").toString());

                    ProjetoLeiPresentation projetoLeiPresentation = new ProjetoLeiPresentation(id,titulo,descricao,pdfFile.getBytes(),dataLimite,
                            delegado,tema,status,apoios);

                    projetoLeiList.add(projetoLeiPresentation);
                }
            } else {
                System.err.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // load all votacoes
    public void loadVotacoes() {
        votacaoList.clear();
        try {

            URL url = new URL("http://localhost:8080/api/votacoes");

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
                Map<String, Object>[] votacoes = objectMapper.readValue(response, Map[].class);

                for (Map<String, Object> votacao : votacoes) {
                    Long id = Long.parseLong(votacao.get("id").toString());
                    String projetoLei = votacao.get("projetoLei").toString();
                    LocalDateTime dataLimite = LocalDateTime.parse(votacao.get("dataFecho").toString());
                    int votosAFavor = Integer.parseInt(votacao.get("votosAFavor").toString());
                    int votosContra = Integer.parseInt(votacao.get("votosContra").toString());
                    String delegado = votacao.get("delegadoProp").toString();
                    String status = votacao.get("status").toString();
                    String resultadoVotacao = votacao.get("votacaoResult").toString();

                    VotacaoPresentation votacaoPresentation = new VotacaoPresentation(id,projetoLei,dataLimite,votosAFavor,votosContra,
                            delegado,status,resultadoVotacao);

                    votacaoList.add(votacaoPresentation);
                }
            } else {
                System.err.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
