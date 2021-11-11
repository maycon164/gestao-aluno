package com.aluno.dao;

import com.aluno.db.DB;

public class DaoFactory {
	
	public static AlunoDao getAlunoDao() {
		return new AlunoDaoJDBC(DB.getConnection());
	}
}
