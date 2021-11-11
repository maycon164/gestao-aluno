package com.aluno.main;

import com.aluno.boundary.AlunoBoundary;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		AlunoBoundary boundary = new AlunoBoundary();
		Scene scene = new Scene(boundary.getPanel(), 800, 600);

		stage.setScene(scene);
		stage.setTitle("Gestão de AAlunos");
		stage.show();

	}

}
