package cinemax;

/**
 * Classe di avvio dell'applicazione Cinemax. Inizializza i gestori,
 * carica i dati da file e avvia il menu principale.
 */
public class CineMax {

	public static void main(String[] args) {
		
		 try {
	            // CREAZIONE GESTORI

	            GestioneFilm gestioneFilm = new GestioneFilm();

	            GestioneUtenti gestioneUtenti = new GestioneUtenti();

	            GestionePrenotazioni gestionePrenotazioni = new GestionePrenotazioni(gestioneFilm);
	            
	            // CARICAMENTO FILE

	            gestioneFilm.caricaFilmDaFile();

	            gestioneUtenti.caricaUtentiDaFile();

	            gestionePrenotazioni.caricaPrenotazioniDaFile();

	            // CREAZIONE MENU

	            Menu menu = new Menu(gestioneFilm, gestioneUtenti, gestionePrenotazioni);

	            // AVVIO PROGRAMMA

	            menu.avvia();

	        }

	        catch (FormatoDatiNonValidoException e) {

	            System.out.println(
	                    "Errore caricamento dati: "
	                    + e.getMessage()
	            );
	        }

	        catch (Exception e) {

	            System.out.println(
	                    "Errore imprevisto: "
	                    + e.getMessage()
	            );
	        }
	    }

	}
