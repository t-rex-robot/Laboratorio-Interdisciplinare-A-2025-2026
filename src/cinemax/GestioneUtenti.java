package cinemax;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class GestioneUtenti {
	
	private Map<String, Utente> mappaUtenti;
	private final String PATH = "data/utenti.csv";
	private Utente utenteCorrente;
	
	public GestioneUtenti() {
		mappaUtenti = new HashMap<>();
		utenteCorrente=null;
	}
	
	public void caricaUtentiDaFile() {
		try (BufferedReader br = new BufferedReader(new FileReader(PATH))){
			String riga = br.readLine();;
			while (riga != null) {
				String[] dati = riga.split(",");
				Utente u = new Utente(dati[0], dati[1], dati[2], dati[3], LocalDate.parse(dati[4]), dati[5], Ruolo.valueOf(dati[6]));
				mappaUtenti.put(u.getUsername(), u);
			}
		}catch(IOException e) {
			System.err.print("Errore caricamento: " + e.getMessage());
		}	
	}
	
	public boolean login(String username, String p) {	
		if(mappaUtenti.containsKey(username)) {
			if(mappaUtenti.get(username).getPassword().equals(p)) {
				System.out.print("login effettuato correttamente!");
				utenteCorrente = mappaUtenti.get(username);
				return true;
			}else {
				System.out.print("Username o Password errati!");
				return false;
			}
		}
		System.out.print("Utente non registrato!");
		return false;
					
	}
	
	public boolean logout() {
		utenteCorrente = null;
		return true;
	}

	

}
