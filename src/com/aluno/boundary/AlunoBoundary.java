package com.aluno.boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.aluno.control.AlunoControl;
import com.aluno.entities.Aluno;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AlunoBoundary {

	private SplitPane split = new SplitPane();

	// TEXT FIELD
	private TextField txtId = new TextField();
	private TextField txtRa = new TextField();
	private TextField txtNome = new TextField();
	private TextField txtNascimento = new TextField();

	private TextField txtPesquisarNome = new TextField();

	// LABELS
	private Label message = new Label();
	private Label lblVizualizacao = new Label("Lista de Vizualização!!!");
	private Label lblPesquisarNome = new Label("Nome:	");

	// BUTTONS
	private Button btnCadastrar = new Button("Cadastrar");
	private Button btnAlterar = new Button("Alterar");
	private Button btnCancelar = new Button("Cancelar");

	private Button btnDelete = new Button("Delete");

	// Table view
	private TableView<Aluno> tableViewAluno = new TableView<>();

	// PANES
	private VBox vbox = new VBox();
	private GridPane grid = new GridPane();

	// Controller Aluno
	private AlunoControl control = new AlunoControl();

	public void start() throws Exception {

		this.iniciarControl();
		this.iniciarTableViewAluno();

		this.configurarVbox();
		this.configurarGrid();

		this.iniciarEventos();

		split.setOrientation(Orientation.VERTICAL);
		split.getItems().addAll(grid, vbox);

	}

	private void iniciarEventos() {

		// Locking txtId field and btnDelete
		txtId.setDisable(true);
		btnDelete.setDisable(true);

		// EVENTO DE SELECIONAR A LINHA DA TABLEVIEW
		tableViewAluno.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getClickCount() > 1) {

				Aluno aluno = tableViewAluno.getSelectionModel().getSelectedItem();

				// this.message.setTextFill(Color.GREEN);

				control.linhaParaForm(aluno);

				txtId.setDisable(true);
				btnAlterar.setDisable(false);
				btnCancelar.setDisable(false);
				btnDelete.setDisable(false);
				btnCadastrar.setDisable(true);

			}
		});

		// Cadastrar novo usuario
		btnCadastrar.setOnAction(e -> {

			control.cadastrar();

		});

		// Logica de alterar
		btnAlterar.setOnAction(e -> {

			boolean aux = control.alterar();

			if (aux) {
				txtId.clear();
				btnAlterar.setDisable(true);
				btnCadastrar.setDisable(false);
				btnCancelar.setDisable(true);
				btnDelete.setDisable(true);
			}

		});

		// CANCELAR
		btnCancelar.setOnAction(e -> {

			this.control.limparCampos();
			this.message.setText("Pode cadastrar um novo Aluno");

			btnCadastrar.setDisable(false);

			btnAlterar.setDisable(true);
			btnCancelar.setDisable(true);
			btnDelete.setDisable(true);
		});

		// BTN DELETE
		btnDelete.setOnAction(e -> {
			boolean resp = control.delete(Long.parseLong(txtId.getText()));
			
			if(resp) {
				
			}
			
		});
	}

	private void configurarGrid() {
		// CONFIGURANDO OS ITENS DO GRID PANE

		grid.addRow(0, new Label("Crud de Alunos"));
		grid.addRow(1, new Label("ID:"), txtId);
		grid.addRow(2, new Label("RA:"), txtRa);
		grid.addRow(3, new Label("NOME:"), txtNome);
		grid.addRow(4, new Label("NASCIMENTO:"), txtNascimento);
		grid.addRow(5, message);
		message.setTextFill(Color.RED);
		GridPane.setColumnSpan(message, 2);

		// System.out.println(grid.getChildren().get(0));

		GridPane.setColumnIndex(grid.getChildren().get(0), 1);
		grid.setAlignment(Pos.CENTER);

		grid.addRow(6, btnCadastrar, btnAlterar, btnCancelar);
		btnAlterar.setDisable(true);
		btnCancelar.setDisable(true);

		grid.setHgap(5);
		grid.setVgap(10);
	}

	private void configurarVbox() {

		// lblVizualização
		lblVizualizacao.setFont(new Font("Monospaced", 25));
		lblVizualizacao.setMaxWidth(900);
		lblVizualizacao.setAlignment(Pos.CENTER);
		VBox.setMargin(lblVizualizacao, new Insets(20d, 0d, 5d, 0d));

		// Campos de Pesquisa
		FlowPane flowPane = new FlowPane();
		flowPane.getChildren().addAll(lblPesquisarNome, txtPesquisarNome);
		flowPane.setAlignment(Pos.CENTER);
		VBox.setMargin(flowPane, new Insets(10d, 0d, 10d, 0d));

		// TableViewAluno
		tableViewAluno.setMaxWidth(800d);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0d, 30d, 40d, 0d));

		// BTN DELETE
		VBox.setMargin(btnDelete, new Insets(20d, 0d, 0d, 0d));

		vbox.getChildren().addAll(lblVizualizacao, flowPane, tableViewAluno, btnDelete);

	}

	@SuppressWarnings("unchecked")
	private void iniciarTableViewAluno() {

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		TableColumn<Aluno, Long> idCol = new TableColumn<>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Aluno, String> raCol = new TableColumn<>("RA");
		raCol.setCellValueFactory(new PropertyValueFactory<>("ra"));

		TableColumn<Aluno, String> nomeCol = new TableColumn<>("NOME");
		nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

		TableColumn<Aluno, String> nascimentoCol = new TableColumn<>("NASCIMENTO");
		nascimentoCol.setCellValueFactory(item -> {
			LocalDate d = item.getValue().getNascimento();
			return new ReadOnlyStringWrapper(d.format(fmt));
		});

		tableViewAluno.getColumns().setAll(idCol, raCol, nomeCol, nascimentoCol);
		tableViewAluno.setItems(control.getAlunos());

	}

	private void iniciarControl() {

		Bindings.bindBidirectional(txtId.textProperty(), control.id);
		Bindings.bindBidirectional(txtRa.textProperty(), control.ra);
		Bindings.bindBidirectional(txtNome.textProperty(), control.nome);
		Bindings.bindBidirectional(txtNascimento.textProperty(), control.nascimento);
		Bindings.bindBidirectional(message.textProperty(), control.message);
		Bindings.bindBidirectional(txtPesquisarNome.textProperty(), control.pesquisarNome);

	}

	public SplitPane getPanel() throws Exception {
		this.start();
		return this.split;
	}

}
