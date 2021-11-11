package com.aluno.dao;

import java.util.List;

import com.aluno.entities.Aluno;

public interface AlunoDao {

	void insert(Aluno aluno);

	void update(Aluno aluno);

	boolean deleteById(long id);

	List<Aluno> findAll();

}
