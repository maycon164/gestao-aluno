package com.aluno.control;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.aluno.dao.AlunoDao;
import com.aluno.dao.DaoFactory;
import com.aluno.entities.Aluno;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class AlunoControl {

	// CONNECTION WITH DATABASE
	private AlunoDao db = DaoFactory.getAlunoDao();

	// PROPERTY
	public StringProperty id = new SimpleStringProperty();
	public StringProperty ra = new SimpleStringProperty();
	public StringProperty nome = new SimpleStringProperty();
	public StringProperty nascimento = new SimpleStringProperty();
	public StringProperty message = new SimpleStringProperty();
	public StringProperty pesquisarNome = new SimpleStringProperty();

	// 3 LISTAS
	private ObservableList<Aluno> alunos = FXCollections.observableArrayList(db.findAll());
	private FilteredList<Aluno> filteredAlunos = new FilteredList<Aluno>(alunos, aluno -> true);
	private SortedList<Aluno> sortedAlunos = new SortedList<>(filteredAlunos);

	public AlunoControl() {
		this.iniciarEventos();
	}

	public void cadastrar() {
		Aluno aluno = this.boundaryToEntity();

		if (aluno != null) {
			db.insert(aluno);
			System.out.println(aluno);
			alunos.add(aluno);

			this.atualizarLista();
			this.message.set("CADASTRADO COM SUCESSO !!! " + (alunos.size() + 1));
		}

	}

	public boolean alterar() {
		Long id = Long.parseLong(this.id.get());

		for (Aluno aluno : alunos) {
			if (aluno.getId() == id) {

				Aluno aux = this.boundaryToEntity();

				if (aux != null) {
					aluno.setRa(aux.getRa());
					aluno.setNome(aux.getNome());
					aluno.setNascimento(aux.getNascimento());

					db.update(aluno);

					this.atualizarLista();
					this.limparCampos();
					this.message.set("ALTERADO COM SUCESSO");

					return true;
				}
			}
		}

		return false;

	}

	public boolean delete(Long id) {

		boolean resp = db.deleteById(id);

		// how to delete ---> thread safe
		if (resp) {
			alunos.removeIf(aluno -> aluno.getId() == id);
		}

		this.atualizarLista();
		this.limparCampos();
		message.set("Aluno com o registro " + id + " foi deletado");
		return resp;
	}

	public void linhaParaForm(Aluno aluno) {

		this.entityToBoundary(aluno);
		this.message.set("ENCONTREI!!!!");

	}

	private void atualizarLista() {

		filteredAlunos.setPredicate(aluno -> false);
		filteredAlunos.setPredicate(aluno -> true);

	}

	// this method is not being used correctly
	private void verificarCampos() throws Exception {

		if (ra.get().isEmpty() || nome.get().isEmpty() || nascimento.get().toString().isEmpty()) {
			System.out.println("OLA");
			throw new Exception("some fields are empty!!!");

		}
	}

	public void limparCampos() {

		id.set("");
		ra.set("");
		nome.set("");
		nascimento.set("");

	}

	private Aluno boundaryToEntity() {

		try {
			this.verificarCampos();

			String ra = this.ra.get();
			String nome = this.nome.get();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate nascimento = LocalDate.parse(this.nascimento.get().trim(), dtf);
			Aluno aluno = new Aluno(ra, nome, nascimento);

			limparCampos();
			return aluno;

		} catch (NumberFormatException e) {

			this.message.set("O CAMPO ID DEVE SER UM INTEIRO!!!");

		} catch (DateTimeParseException e) {

			this.message.set("INFORME A DATA NO PADRÃO dd/MM/yyyy");

		} catch (Exception e) {

			this.message.set("PREENCHA TODOS OS CAMPOS");

		}

		return null;
	}

	private void entityToBoundary(Aluno aluno) {

		this.id.set(String.valueOf(aluno.getId()));
		this.ra.set(aluno.getRa());
		this.nome.set(aluno.getNome());

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String nascimento = dtf.format(aluno.getNascimento());

		this.nascimento.set(nascimento);
	}

	public ObservableList<Aluno> getAlunos() {
		return this.sortedAlunos;
	}

	private void iniciarEventos() {
		// search event
		pesquisarNome.addListener((obs, oldValue, newValue) -> {

			filteredAlunos.setPredicate(p -> {
				if (newValue.isEmpty() || newValue == null) {
					return true;
				}

				String parametro = newValue.toLowerCase();

				if (p.getNome().toLowerCase().contains(parametro)) {
					return true;
				}

				return false;

			});

		});

	}

}
