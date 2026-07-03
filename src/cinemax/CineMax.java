package cinemax;

/**
 * Classe di avvio dell'applicazione Cinemax. Carica i dati delle gestioni da file e avvia il menu principale.
 *
 * @author Paolo (Sviluppatore principale di questa classe)
 * @author Elisa (Documentazione Javadoc)
 * Matricole: 766917 (VA), 765763 (VA)
 */
public class CineMax {

	public static void main(String[] args) {
		
		 try {  
	            // CARICAMENTO FILE

	            GestioneFilm.caricaFilmDaFile();

	            GestioneUtenti.caricaUtentiDaFile();

	            GestionePrenotazioni.caricaPrenotazioniDaFile();

	            // CREAZIONE MENU

	            Menu menu = new Menu();

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
