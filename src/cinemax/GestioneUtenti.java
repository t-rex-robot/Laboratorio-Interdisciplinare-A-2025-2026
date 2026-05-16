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
	//all'avvio del programma carica tutti gli utenti scritti nel file utenti.csv in una hash map, creando direttamente gli oggetti Utente
	public void caricaUtentiDaFile() throws FormatoDatiNonValidoException {
		try (BufferedReader br = new BufferedReader(new FileReader(PATH))){
			String riga;
			while ((riga = br.readLine()) != null) {
				String[] dati = riga.split(",");
				if (dati.length != 7) throw new FormatoDatiNonValidoException ("Errore nel formato dei dati: attesi 7 campi, trovati " + dati.length + " campi.");
				Utente u = new Utente(dati[0], dati[1], dati[2], dati[3], LocalDate.parse(dati[4]), dati[5], Ruolo.valueOf(dati[6]));
				mappaUtenti.put(u.getUsername(), u);
			}
		}catch(IOException e) {
			System.err.println("Errore caricamento: " + e.getMessage());
		}
		//}catch(Exception e) {
			//System.err.println("Errore nel formato dei dati: " + e.getMessage());
		//}
	}
	
	//nel primo if controlla se effettivamente lo username sia di un Utente già esistente e, in caso positivo, controlla che la password associata allo username sia quella corretta
	public boolean login(String username, String p) {	
		if(mappaUtenti.containsKey(username)) {
			if(mappaUtenti.get(username).getPassword()==(p)) {
				System.out.println("login effettuato correttamente!");
				utenteCorrente = mappaUtenti.get(username);
				return true;
			}else {
				System.out.println("Username o Password errati!");
				return false;
			}
		}
		System.out.println("Utente non registrato!");
		return false;
					
	}
	
	//non sono ancora sicura che utenteCorrente ci serva
	public boolean logout() {
		utenteCorrente = null;
		return true;
	}
	
	//crea un cliente con i valori passati per parametri, lo salva nella hash map e lo scrive nel file utenti.csv
	public boolean registraCliente(String n, String c, String us, String p, LocalDate dn, String d) {
		if (!mappaUtenti.containsKey(us)) {
			System.out.print("Username non disponibile! ");
			return false;
		}
		Utente u = new Utente(n, c, us, p, dn, d);
		mappaUtenti.put(u.getUsername(), u);
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH, true))){
				String utente = u.toCSV();
				bw.write(utente);
				bw.newLine();
				System.out.println("Utente registrato correttamente! ");
			}
			catch (IOException e) {
				System.err.println("Errore nella registrazione " + e.getMessage());
				return false;
			}
		return true;
	}
	
	//stampa tutti i valori all'interno della hash map, quindi tutti gli utenti salvati nel file
	public void stampaMappa() {
		for (Utente u : mappaUtenti.values()) {
			System.out.println(u);
		}
	}

}
