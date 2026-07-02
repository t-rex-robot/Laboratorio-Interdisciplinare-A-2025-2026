package cinemax;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Paolo (Sviluppatore principale)
 * @author Elisa (Documentazione Javadoc)
 * Matricole: 766917 (VA), 765763 (VA)
 */

/**
 * Rappresenta una prenotazione di biglietti per una proiezione.
 * Contiene il codice univoco della prenotazione, l'utente che ha prenotato,
 * il film, la data e l'ora della proiezione, il numero di biglietti e il costo.
 */
public class Prenotazione {
	
	//Attributi Prenotazione
	
    private String codicePrenotazione;
    private final String usernameUtente;
    private final String titoloFilm;
    private LocalDate dataProiezione;
    private LocalTime oraProiezione;
    private int numeroBiglietti;
    private double costoUnitario;
    
    //Costruttore Prenotazione
    /**
     * Costruisce una nuova prenotazione.
     *
     * @param codicePrenotazione codice univoco della prenotazione
     * @param usernameUtente username dell'utente che ha prenotato
     * @param titoloFilm titolo del film
     * @param dataProiezione data della proiezione
     * @param oraProiezione ora della proiezione
     * @param numeroBiglietti numero di biglietti prenotati
     * @param costoUnitario costo per singolo biglietto
     */
    
    public Prenotazione(String codicePrenotazione, String usernameUtente, String titoloFilm, LocalDate dataProiezione, LocalTime oraProiezione, int numeroBiglietti, double costoUnitario) {

    	this.codicePrenotazione = codicePrenotazione;
    	this.usernameUtente = usernameUtente;
    	this.titoloFilm = titoloFilm;
    	this.dataProiezione = dataProiezione;
    	this.oraProiezione = oraProiezione;
    	this.numeroBiglietti = numeroBiglietti;
    	this.costoUnitario = costoUnitario;
    }
    
    // Metodi Getter di Prenotazione 

    /**
     * Restituisce il codice univoco della prenotazione.
     *
     * @return codice prenotazione
     */
    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }

    /**
     * Restituisce lo username dell'utente che ha effettuato la prenotazione.
     *
     * @return username utente
     */
    public String getUsername() {
        return usernameUtente;
    }

    /**
     * Restituisce il titolo del film prenotato.
     *
     * @return titolo del film
     */
    public String getTitoloFilm() {
        return titoloFilm;
    }

    /**
     * Restituisce la data della proiezione.
     *
     * @return data della proiezione
     */
    public LocalDate getDataProiezione() {
        return dataProiezione;
    }

    /**
     * Restituisce l'orario della proiezione.
     *
     * @return ora della proiezione
     */
    public LocalTime getOraProiezione() {
        return oraProiezione;
    }

    /**
     * Restituisce il numero di biglietti prenotati.
     *
     * @return numero di biglietti
     */
    public int getNumeroBiglietti() {
        return numeroBiglietti;
    }

    /**
     * Restituisce il costo unitario del biglietto associato alla prenotazione.
     *
     * @return costo per biglietto
     */
    public double getCostoUnitario() {
        return costoUnitario;
    }
    
    /**
     * Calcola il costo totale della prenotazione in base al numero di biglietti e al costo unitario.
     *
     * @return costo totale della prenotazione
     */
    public double getCostoTotale() {
        return numeroBiglietti * costoUnitario;
    }
    
    // Metodo che prende in conto quanti biglietti sono stati prenotati

    /**
     * Imposta il numero di biglietti prenotati se il valore è positivo.
     * Valori non positivi vengono ignorati.
     *
     * @param numeroBiglietti nuovo numero di biglietti
     */
    public void setNumeroBiglietti(int numeroBiglietti) {
        if (numeroBiglietti > 0) {
            this.numeroBiglietti = numeroBiglietti;
        }
        
      //scrive l'oggetto Prenotazione già in formato corretto per il file utenti.csv
    }
    /**
     * Restituisce la rappresentazione CSV della prenotazione.
     *
     * @return riga CSV con i campi della prenotazione
     */
    public String toCSV() {
        return codicePrenotazione + "," + usernameUtente + "," + titoloFilm + "," + dataProiezione + "," + oraProiezione + "," + numeroBiglietti + "," + costoUnitario;
    }
    
    @Override
    public String toString() {
        return "Codice: " + codicePrenotazione + "\nUtente: " + usernameUtente + "\nFilm: " + titoloFilm + "\nData: " + dataProiezione + "\nOra: " + oraProiezione + "\nBiglietti: " + numeroBiglietti + "\nCosto unitario: " + costoUnitario + "\nCosto totale: " + getCostoTotale();
    }
    

}