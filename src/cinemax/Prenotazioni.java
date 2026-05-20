package cinemax;

import java.time.LocalDate;
import java.time.LocalTime;

public class Prenotazione {
	
	//Attributi Prenotazione
	
    private String codicePrenotazione;
    private String usernameUtente;
    private String titoloFilm;
    private LocalDate dataProiezione;
    private LocalTime oraProiezione;
    private int numeroBiglietti;
    private double costoUnitario;
    
    //Costruttore Prenotazione
    
    public Prenotazione(String codicePrenotazione, String usernameUtente, String titoloFilm, LocalDate dataProiezione, LocalTime oraProiezione, int numeroBiglietti, double costoUnitario) {

    	this.codicePrenotazione = codicePrenotazione;
    	this.usernameUtente = usernameUtente;
    	this.titoloFilm = titoloFilm;
    	this.dataProiezione = dataProiezione;
    	this.oraProiezione = oraProiezione;
    	this.numeroBiglietti = numeroBiglietti;
    	this.costoUnitario = costoUnitario;
    }
    
    //Metodi Getter di Prenotazione 
    
    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }

    public String getUsernameUtente() {
        return usernameUtente;
    }

    public String getTitoloFilm() {
        return titoloFilm;
    }

    public LocalDate getDataProiezione() {
        return dataProiezione;
    }

    public LocalTime getOraProiezione() {
        return oraProiezione;
    }

    public int getNumeroBiglietti() {
        return numeroBiglietti;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }
    
    //Metodo che calcola costi per biglietto complessivo
    public double getCostoTotale() {
        return numeroBiglietti * costoUnitario;
    }
    
    // Metodo che prende in conto quanti biglietti sono stati prenotati
    
    public void setNumeroBiglietti(int numeroBiglietti) {
        if (numeroBiglietti > 0) {
            this.numeroBiglietti = numeroBiglietti;
        }
        
      //scrive l'oggetto Prenotazione già in formato corretto per il file utenti.csv
    }
    public String toCSV() {
        return codicePrenotazione + "," + usernameUtente + "," + titoloFilm + "," + dataProiezione + "," + oraProiezione + "," + numeroBiglietti + "," + costoUnitario;
    }
    
    @Override
    public String toString() {
        return "Codice: " + codicePrenotazione + "\nUtente: " + usernameUtente + "\nFilm: " + titoloFilm + "\nData: " + dataProiezione + "\nOra: " + oraProiezione + "\nBiglietti: " + numeroBiglietti + "\nCosto unitario: " + costoUnitario + "\nCosto totale: " + getCostoTotale();
    }
    

}