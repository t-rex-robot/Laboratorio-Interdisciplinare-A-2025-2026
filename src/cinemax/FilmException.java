package cinemax;

/**
 * @author Camilla (Sviluppatore principale)
 * @author Elisa (Documentazione Javadoc)
 * Matricole: 766743 (VA), 765763 (VA)
 */

/**
 * Eccezione specifica per errori relativi alla creazione o gestione delle proiezioni di film.
 */
public class FilmException extends RuntimeException {
	
	/**
	 * Costruisce l'eccezione con un messaggio descrittivo.
	 *
	 * @param mess messaggio di errore che descrive la causa dell'eccezione
	 */
	public FilmException(String mess) {
		super(mess);
	}

}
