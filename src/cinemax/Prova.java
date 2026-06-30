package cinemax;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Prova {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GestioneFilm gestoreFilm = new GestioneFilm();
		GestionePrenotazioni gestorePrenotazioni = new GestionePrenotazioni(gestoreFilm);
		GestioneUtenti gestoreUtenti = new GestioneUtenti();
		gestoreFilm.caricaFilmDaFile();
		gestorePrenotazioni.caricaPrenotazioniDaFile();
		gestoreUtenti.caricaUtentiDaFile();
		
		String crit = "COMPRESO_TRA";
		String d1 = "2027-12-29";
		String d2 = "2027-12-31";
		LinkedList<Film> list = new LinkedList<>();
		list = gestoreFilm.trovaFilm(3, crit, d1, d2);
		for (Film tmp : list) {
			System.out.println("film -> " + tmp.toString() + " posti -> " + tmp.getPostiSala());
		}
		
		
		/*String nome = "Francesco";
		String cognome = "Bora";
		String us = "FraSb";
		String password = "Fra00";
		LocalDate data = LocalDate.parse("2003-12-12");
		String d = "Via Libertà 3 Varese";
		
		Utente u = new Utente(nome, cognome, us, password, data, d);
		
		LocalDate dataf = LocalDate.parse("2028-10-10");
		LocalTime or = LocalTime.parse("10:00");
		String titolo = "Matilda";
		String genere = "Comedy";
		String regista = "Alan T";
		int anno = 2000;
		int durata = 120;
		int eta = 0;
		double c = 0.0;
		
		Film f = new Film(dataf, or, titolo, genere, regista, anno, durata, eta, c);
		gestoreFilm.creaProiezione(dataf, or, titolo, genere, regista, anno, durata, eta, eta);
		
		
		int b = 3;
		gestorePrenotazioni.creaPrenotazione(u, f, b);
		
		LocalDate data2 = LocalDate.parse("2028-10-11");
		gestoreFilm.eliminaProiezione(f);
		
		
		Menu prova = new Menu(gestoreFilm, gestoreUtenti, gestorePrenotazioni);
		prova.avvia();*/
	}

}
