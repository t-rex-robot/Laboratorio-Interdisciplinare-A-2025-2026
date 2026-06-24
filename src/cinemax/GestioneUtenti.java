package cinemax;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;

/**
 * Gestisce il caricamento, l'autenticazione e la registrazione degli utenti.
 * Legge gli utenti da un file CSV e mantiene in memoria una mappa username->Utente.
 *
 * @see cinemax.Utente
 * @see cinemax.PasswordHasher
 */
public class GestioneUtenti {
	
	private final Map<String, Utente> mappaUtenti;
	private final String PATH = "data/utenti.csv";
	private Utente utenteCorrente;
	
	/**
	 * Costruisce un gestore utenti e inizializza la mappa degli utenti.
	 */
	public GestioneUtenti() {
		mappaUtenti = new HashMap<>();
		utenteCorrente=null;
	}
	
	/**
	 * All'avvio del programma carica gli utenti dal file CSV nella mappa.
	 *
	 * @throws FormatoDatiNonValidoException se una riga del file non ha il formato corretto
	 */
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

	/**
	 * Esegue il login confrontando la password inserita con l'hash SHA-256.
	 * Mantiene compatibilità con le password in chiaro già presenti nei dati.
	 *
	 * @param username lo username dell'utente
	 * @param p la password in chiaro da verificare
	 * @return true se il login è riuscito, false altrimenti
	 */
	public boolean loginHashed(String username, String p) {
		if (mappaUtenti.containsKey(username)) {
			try {
				if (passwordMatches(username, p)) {
					System.out.println("login effettuato correttamente!");
					utenteCorrente = mappaUtenti.get(username);
					return true;
				}
			} catch (NoSuchAlgorithmException e) {
				System.err.println("Errore hashing password: " + e.getMessage());
				return false;
			}
			System.out.println("Username o Password errati!");
			return false;
		}
		System.out.println("Utente non registrato!");
		return false;
	}

	/**
	 * Controlla se la password in chiaro corrisponde a quella memorizzata.
	 * Se la password memorizzata è un hash SHA-256, confronta l'hash calcolato;
	 * altrimenti confronta direttamente la password in chiaro.
	 *
	 * @param username lo username dell'utente
	 * @param plainPassword la password in chiaro inserita
	 * @return true se la password corrisponde, false altrimenti
	 * @throws NoSuchAlgorithmException se l'algoritmo SHA-256 non è disponibile
	 */
	public boolean passwordMatches(String username, String plainPassword) throws NoSuchAlgorithmException {
		if (!mappaUtenti.containsKey(username)) {
			return false;
		}
		String storedPassword = mappaUtenti.get(username).getPassword();
		String hashedInput = PasswordHasher.hashPassword(plainPassword);
		return storedPassword.equals(hashedInput) || storedPassword.equals(plainPassword);
	}

	/**
	 * Esegue il logout cancellando l'utente corrente.
	 *
	 * @return true se l'operazione è stata completata
	 */
	public boolean logout() {
		utenteCorrente = null;
		return true;
	}
	
	/**
	 * Registra un nuovo cliente e lo aggiunge sia nella mappa sia sul file CSV.
	 *
	 * @param n nome del cliente
	 * @param c cognome del cliente
	 * @param us username scelto dal cliente
	 * @param p password in chiaro
	 * @param dn data di nascita del cliente
	 * @param d domicilio del cliente
	 * @return true se la registrazione ha avuto successo, false altrimenti
	 */
	public boolean registraCliente(String n, String c, String us, String p, LocalDate dn, String d) {
		if (mappaUtenti.containsKey(us)) {
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
	
	/**
	 * Stampa tutti gli utenti attualmente caricati nella mappa.
	 */
	public void stampaMappa() {
		for (Utente u : mappaUtenti.values()) {
			System.out.println(u);
		}
	}

}
