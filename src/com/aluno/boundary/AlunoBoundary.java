package com.aluno.boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.aluno.entities.Aluno;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AlunoBoundary {

	GridPane grid = new GridPane();

	TextField txtId = new TextField();
	TextField txtRa = new TextField();
	TextField txtNome = new TextField();
	TextField txtNascimento = new TextField();

	Label error = new Label();

	Button btnCadastrar = new Button("Cadastrar");
	Button btnPesquisar = new Button("Pesquisar");

	List<Aluno> alunos = new ArrayList<>();

	public void start() throws Exception {

		grid.addRow(1, new Label("ID:"), txtId);
		grid.addRow(2, new Label("RA:"), txtRa);
		grid.addRow(3, new Label("NOME:"), txtNome);
		grid.addRow(4, new Label("NASCIMENTO:"), txtNascimento);

		grid.addRow(5, error);
		GridPane.setColumnSpan(error, 2);

		grid.addRow(6, btnCadastrar, btnPesquisar);

		grid.setHgap(10);
		grid.setVgap(20);

		grid.setAlignment(Pos.TOP_CENTER);

		btnCadastrar.setOnAction(e -> {

			Aluno aluno = this.boundaryToEntity();
			if (aluno != null)
				alunos.add(aluno);
		});

		btnPesquisar.setOnAction(e -> {
			String nome = txtNome.getText();

			for (Aluno aluno : alunos) {

				if (aluno.getNome().contains(nome.trim())) {
					this.entityToBoundary(aluno);
					return;
				}

			}
			error.setText("Não encontrei ninguem!!!");
		});

		/*
		 * Scene scene = new Scene(grid, 400, 300); stage.setScene(scene);
		 * stage.setTitle("Gestão de Alunos"); stage.show();
		 */
	}

	private Aluno boundaryToEntity() {

		try {
			this.verificarCampos();

			long id = Long.parseLong(txtId.getText());
			String ra = txtRa.getText();
			String nome = txtNome.getText();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate nascimento = LocalDate.parse(txtNascimento.getText().trim(), dtf);
			Aluno aluno = new Aluno(id, ra, nome, nascimento);
			this.error.setText("Cadastrado com sucesso!!! " + (alunos.size() + 1));
			limparCampos();
			return aluno;

		} catch (NumberFormatException e) {

			this.error.setText("O campo id deve ser um número inteiro");

		} catch (DateTimeParseException e) {

			this.error.setText("informe a data dd/MM/yyyy");

		} catch (Exception e) {

			this.error.setText("preencha todos os campos");

		}

		return null;
	}

	private void verificarCampos() throws Exception {

		if (txtRa.getText().isEmpty() || txtNome.getText().isEmpty() || txtId.getText().isEmpty()
				|| txtNascimento.getText().isEmpty()) {

			throw new Exception("Campo vazio");

		}
	}

	private void limparCampos() {

		txtId.clear();
		txtRa.clear();
		txtNome.clear();
		txtNascimento.clear();

	}

	private void entityToBoundary(Aluno aluno) {

		txtId.setText(String.valueOf(aluno.getId()));
		txtRa.setText(aluno.getRa());
		txtNome.setText(aluno.getNome());

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String nascimento = dtf.format(aluno.getNascimento());

		txtNascimento.setText(nascimento);

	}

	public GridPane getPanel() throws Exception {
		this.start();
		return grid;
	}

}
