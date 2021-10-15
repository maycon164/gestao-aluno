package com.aluno.main;

import com.aluno.boundary.AlunoBoundary;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		AlunoBoundary boundary = new AlunoBoundary();
		HBox root = new HBox();

		Scene scene = new Scene(root, 400, 300);
		scene.setRoot(boundary.getPanel());

		stage.setScene(scene);
		stage.setTitle("Minha Tela");
		stage.show();

	}

}
