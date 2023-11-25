package com.example.democracia2_desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.example.democracia2_desktop.presentation.controllers.LoginController;
import com.example.democracia2_desktop.presentation.controllers.MenuController;
import com.example.democracia2_desktop.presentation.controllers.PresentationProjetoLeiController;
import com.example.democracia2_desktop.presentation.controllers.PresentationVotacaoController;
import com.example.democracia2_desktop.presentation.models.DataModel;

import java.io.InputStream;

public class Hello extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String prefix = "/view/";

        BorderPane root = new BorderPane();

        FXMLLoader votacaoLoader = new FXMLLoader(getClass().getResource(prefix + "listarVotacoes.fxml"));
        root.setCenter(votacaoLoader.load());
        PresentationVotacaoController votacaoController = votacaoLoader.getController();

        FXMLLoader projetosLeiLoader = new FXMLLoader(getClass().getResource(prefix + "listarProjetosLei.fxml"));
        root.setCenter(projetosLeiLoader.load());
        PresentationProjetoLeiController projetosLeiController = projetosLeiLoader.getController();

        FXMLLoader menuloader = new FXMLLoader(getClass().getResource(prefix + "menu.fxml"));
        root.setCenter(menuloader.load());
        MenuController menuController = menuloader.getController();

        FXMLLoader loginloader = new FXMLLoader(getClass().getResource(prefix + "login.fxml"));
        root.setCenter(loginloader.load());
        LoginController loginController = loginloader.getController();

        DataModel model = new DataModel();

        votacaoController.initModel(model);
        projetosLeiController.initModel(model);
        loginController.initModel(model);
        menuController.initModel(model);

        String iconPath = "/images/icon_app.png";
        InputStream iconStream = getClass().getResourceAsStream(iconPath);
        Image icon = new Image(iconStream);

        primaryStage.getIcons().add(icon);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
