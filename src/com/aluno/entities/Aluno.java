package com.aluno.entities;

import java.time.LocalDate;

public class Aluno {

	private long id;
	private String ra;
	private String nome;
	private LocalDate nascimento;

	public Aluno(long id, String ra, String nome, LocalDate nascimento) {
		this.id = id;
		this.ra = ra;
		this.nome = nome;
		this.nascimento = nascimento;
	}

	public long getId() {
		return id;
	}

	public String getRa() {
		return ra;
	}

	public void setRa(String ra) {
		this.ra = ra;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		this.nascimento = nascimento;
	}

	public String toString() {

		return "nome: " + this.nome + "\nra: " + this.ra + "\nnascimento: " + this.nascimento;
	}
}
