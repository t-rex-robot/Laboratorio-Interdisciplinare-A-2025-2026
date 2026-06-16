package cinemax;

public class app {

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

	            menu menu = new menu(gestioneFilm, gestioneUtenti, gestionePrenotazioni);

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
