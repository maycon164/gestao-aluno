package com.aluno.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aluno.db.DB;
import com.aluno.entities.Aluno;

public class AlunoDaoJDBC implements AlunoDao {

	private Connection conn;

	public AlunoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Aluno aluno) {

		PreparedStatement ps = null;

		try {
			StringBuilder sql = new StringBuilder("INSERT INTO aluno (ra, nome, nascimento) VALUES (?, ?, ?)");
			ps = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, aluno.getRa());
			ps.setString(2, aluno.getNome());
			Date data = Date.from(aluno.getNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant());
			ps.setDate(3, new java.sql.Date(data.getTime()));

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();

				while (rs.next()) {

					aluno.setId(rs.getLong(1));

				}

				DB.closeResultSet(rs);

			}

		} catch (SQLException e) {

			e.printStackTrace();

		} finally {

			DB.closeStatement(ps);

		}
	}

	@Override
	public void update(Aluno aluno) {
		PreparedStatement ps = null;

		try {
			StringBuilder sql = new StringBuilder("UPDATE aluno SET ra = ?, nome = ?, nascimento = ? WHERE id = ?");
			ps = conn.prepareStatement(sql.toString());

			ps.setString(1, aluno.getRa());
			ps.setString(2, aluno.getNome());
			Date data = Date.from(aluno.getNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant());
			ps.setDate(3, new java.sql.Date(data.getTime()));
			ps.setInt(4, (int) aluno.getId());

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Linhas afetadas: " + rowsAffected);
			} else {
				System.out.println("Algo deu errado");
			}

		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	@Override
	public boolean deleteById(long id) {
		PreparedStatement ps = null;
		boolean resp = false;
		
		try {
			StringBuilder sql = new StringBuilder("DELETE aluno WHERE id = ?");
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, (int) id);

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Linhas Afetadas: " + rowsAffected);
				resp = true;
			}

		} catch (SQLException e) {

			e.printStackTrace();

		} finally {

			DB.closeStatement(ps);

		}
		
		return resp;
	}

	@Override
	public List<Aluno> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			StringBuilder sql = new StringBuilder("SELECT * FROM aluno");
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			List<Aluno> alunos = new ArrayList<>();

			while (rs.next()) {

				alunos.add(instantiateAluno(rs));

			}
			System.out.println("TUDO CERTO");
			return alunos;

		} catch (SQLException | ParseException e) {

			e.printStackTrace();

		} finally {

			DB.closeStatement(ps);
			DB.closeResultSet(rs);

		}

		return null;

	}

	private Aluno instantiateAluno(ResultSet rs) throws SQLException, ParseException {

		// from a java.sql.Date
		@SuppressWarnings("deprecation")
		String dataString = rs.getDate("nascimento").toLocaleString();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		// to a java.util.Date
		java.util.Date dataUtil = sdf.parse(dataString);

		// then i can use in my LocalDate variable
		LocalDate local = dataUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		return new Aluno(rs.getLong("id"), rs.getString("ra"), rs.getString("nome"), local);
	}

}
