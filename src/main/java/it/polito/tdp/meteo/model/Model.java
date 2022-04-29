package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	MeteoDAO meteoDAO;
	private List<Citta> cittaList;
	private List<Citta> best;
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {
		meteoDAO = new MeteoDAO();
		this.cittaList = meteoDAO.getAllCitta();
	}
	
	public List<Citta> getLeCitta(){
		return cittaList;
	}
	
	
	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(String localita, int mese) {
		int somma = 0;
		double media = 0;
		
		for (Rilevamento r : meteoDAO.getUmidita(localita, mese)){
			somma = somma + r.getUmidita();
		}
		
		media = ((double)somma/meteoDAO.getUmidita(localita, mese).size());
		
		String s = localita + ": " + media;
		
		return s;

	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> parziale = new ArrayList<>();
		this.best = null;
		MeteoDAO meteoDAO = new MeteoDAO();
		
		for(Citta c: cittaList) {
			c.setRilevamenti(meteoDAO.getAllRilevamentiLocalitaMese(mese,c));
		}
		
		cerca(parziale,0);
		
		return best;
	}
	
	private void cerca(List<Citta> parziale, int livello) {
		// TODO Auto-generated method stub
		if (livello==NUMERO_GIORNI_TOTALI) {
			//caso terminale
			Double costo = calcolaCosto(parziale);
			if (best==null || costo < calcolaCosto(best))
				best = new ArrayList<>(parziale);
		}
		else {
			//caso normale
			for (Citta prova : cittaList) {
				if(aggiuntaValida(prova,parziale)) {
					parziale.add(prova);
					cerca(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
	}

	
	
	
	
	private Double calcolaCosto(List<Citta> parziale) {
		 double costo = 0.0;
		 for (int giorno = 1; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			 Citta c = parziale.get(giorno-1);
			 double umid = c.getRilevamenti().get(giorno-1).getUmidita();
			 costo = costo + umid;
		 }
		 
		 
		 //somma 100*numero di volte in cui cambio citta
		 for (int giorno = 2; giorno <=NUMERO_GIORNI_TOTALI; giorno++) {
			 if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				 costo = costo + COST; 
			 }
		 }
		 return costo;
	}
	
	
	
	public boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		int conta = 0;
		for (Citta precedente : parziale) {
			if (precedente.equals(prova)) {
				conta++;
			}
	}
	
		if (conta >= NUMERO_GIORNI_CITTA_MAX) {
			return false;
		}
		
		
		if (parziale.size()==0) {
			return true;
		}
		
		if(parziale.size()==1 || parziale.size()==2) {
			return parziale.get(parziale.size()-1).equals(prova);
		}
		
		
		if(parziale.get(parziale.size()-1).equals(prova)) {
			return true;
		}
		
		
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2))
				&&
				parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3))) {
			return true;
		}
		
		
		
		return false;
	}

}
