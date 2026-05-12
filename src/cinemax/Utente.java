package cinemax;
import java.time.LocalDate;

public class Utente {
	private String nome;
	private String cognome;
	private String username;
	private String password;
	private LocalDate datadinascita;
	private String domicilio;
	private Ruolo ruolo;
	
	public Utente(String nome, String cognome, String username, String password, LocalDate datadinascita, String domicilio, Ruolo ruolo) {
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.datadinascita = datadinascita;
		this.domicilio = domicilio;
		this.ruolo = ruolo;
		}
	
	public Utente(String nome, String cognome, String username, String password, LocalDate datadinascita, String domicilio) {
	this.nome = nome;
	this.cognome = cognome;
	this.username = username;
	this.password = password;
	this.datadinascita = datadinascita;
	this.domicilio = domicilio;
	this.ruolo = Ruolo.CLIENTE;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public String getCognome() {
		return this.cognome;
	}
	
	public LocalDate getDataDiNascita() {
		return this.datadinascita;
	}
	
	public Ruolo getRuolo() {
		return this.ruolo;
	}
	
	
}
