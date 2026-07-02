package cinemax;

/**
 * @author Camilla (Sviluppatore principale)
 * @author Elisa (Documentazione Javadoc)
 * Matricole: 766743 (VA), 765763 (VA)
 */

/**
 * Eccezione lanciata quando il formato dei dati caricati dal file non è valido.
 */
public class FormatoDatiNonValidoException extends RuntimeException{
	
	/**
	 * Costruisce l'eccezione con un messaggio di errore.
	 *
	 * @param mess il messaggio di errore che descrive il formato non valido
	 */
	public FormatoDatiNonValidoException(String mess) {
		super(mess);
	}
}
