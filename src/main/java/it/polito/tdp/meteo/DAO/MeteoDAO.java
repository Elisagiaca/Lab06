package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	
	public List<Rilevamento> getUmidita(String localita, int m) {
		
		final String sql = "SELECT Localita, Data, Umidita "
				+ "FROM situazione "
				+ "WHERE localita = ? AND  MONTH (DATA) = ?";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
		
			st.setString(1, localita);
			st.setInt(2, m);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}
			
			conn.close();
			st.close();
			rs.close();
			return rilevamenti;
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	public List<Citta> getAllCitta() {
		final String sql = "SELECT DISTINCT localita "
				+ "FROM situazione "
				+ "ORDER BY localita";
		
		List<Citta> result = new ArrayList<Citta>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Citta c = new Citta(rs.getString("localita"));
				result.add(c);
			}
		conn.close();
		return result;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}




	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, Citta c) {
		// TODO Auto-generated method stub
		
		final String sql = "SELECT localita, data, umidita "
				+ "FROM situazione "
				+ "WHERE localita=? AND MONTH(data)=? "
				+ "ORDER BY data ASC";
		
		List<Rilevamento> listaRilevamenti = new ArrayList<Rilevamento>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, c.getNome());
			st.setString(2, Integer.toString(mese));
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Rilevamento r = new Rilevamento(rs.getString("localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				listaRilevamenti.add(r);
			}
			conn.close();
			return listaRilevamenti;
			
		}catch(SQLException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	


}
