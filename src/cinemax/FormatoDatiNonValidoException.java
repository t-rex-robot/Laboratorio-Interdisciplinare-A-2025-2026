package cinemax;

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
