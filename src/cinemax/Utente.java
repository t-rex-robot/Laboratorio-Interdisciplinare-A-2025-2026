package cinemax;
import java.time.LocalDate;

/**
 * Rappresenta un utente, con i dati personali, le credenziali e il ruolo.
 * I dati possono essere caricati e salvati in formato CSV.
 */
public class Utente {
	private String nome;
	private String cognome;
	private String username;
	private String password;
	private LocalDate datadinascita;
	private String domicilio;
	private Ruolo ruolo;
	
	/**
	 * Costruisce un utente completo con ruolo specificato.
	 *
	 * @param nome nome dell'utente
	 * @param cognome cognome dell'utente
	 * @param username username dell'utente
	 * @param password password dell'utente (in chiaro o hash a seconda del sistema)
	 * @param datadinascita data di nascita dell'utente
	 * @param domicilio domicilio dell'utente
	 * @param ruolo ruolo assegnato all'utente
	 */
	public Utente(String nome, String cognome, String username, String password, LocalDate datadinascita, String domicilio, Ruolo ruolo) {
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.datadinascita = datadinascita;
		this.domicilio = domicilio;
		this.ruolo = ruolo;
	}
	
	/**
	 * Costruisce un nuovo utente cliente con ruolo CLIENTE.
	 *
	 * @param nome nome dell'utente
	 * @param cognome cognome dell'utente
	 * @param username username scelto dall'utente
	 * @param password password dell'utente
	 * @param datadinascita data di nascita dell'utente
	 * @param domicilio domicilio dell'utente
	 */
	public Utente(String nome, String cognome, String username, String password, LocalDate datadinascita, String domicilio) {
	this(nome, cognome, username, password, datadinascita, domicilio, Ruolo.CLIENTE);
	}
	
	/**
	 * Restituisce lo username dell'utente.
	 *
	 * @return lo username dell'utente
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Restituisce la password dell'utente.
	 *
	 * @return la password dell'utente
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Restituisce il nome dell'utente.
	 *
	 * @return il nome dell'utente
	 */
	public String getNome() {
		return this.nome;
	}
	
	/**
	 * Restituisce il cognome dell'utente.
	 *
	 * @return il cognome dell'utente
	 */
	public String getCognome() {
		return this.cognome;
	}
	
	/**
	 * Restituisce la data di nascita dell'utente.
	 *
	 * @return la data di nascita dell'utente
	 */
	public LocalDate getDataDiNascita() {
		return this.datadinascita;
	}
	
	/**
	 * Restituisce il ruolo assegnato all'utente.
	 *
	 * @return il ruolo dell'utente
	 */
	public Ruolo getRuolo() {
		return this.ruolo;
	}
	
/**
	 * Controlla che una stringa non contenga il carattere ',' utilizzato come separatore CSV.
	 *
	 * @param p la stringa da controllare
	 * @return true se la stringa non contiene virgole, false altrimenti
	 */
	public boolean controlloDati(String p) {
		p.trim();
		for (int i=0; i<p.length(); i++)
			if(p.charAt(i) == ',')
				return false;
		return true;		
	}
	
	/**
	 * Converte l'utente in una riga CSV compatibile con il file utenti.csv.
	 *
	 * @return la rappresentazione CSV dell'utente
	 */
	public String toCSV() {
		return this.nome + "," + this.cognome + "," + this.username + "," + this.password + "," + this.datadinascita + "," + this.domicilio + "," + this.ruolo;
	}
	
	/**
	 * Restituisce una stringa descrittiva dell'utente.
	 *
	 * @return rappresentazione testuale dell'utente
	 */
	@Override
	public String toString() {
		return "Username: " + this.username + " Password: " + this.password + " Ruolo: " + this.ruolo;
	}
}
