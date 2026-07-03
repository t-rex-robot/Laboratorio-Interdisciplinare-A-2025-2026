package cinemax;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Rappresenta un film programmato in sala, con data e ora della proiezione,
 * i dettagli di produzione, i limiti di età e i posti disponibili.
 *
 * @author Camilla (Sviluppatore principale)
 * @author Elisa (Documentazione Javadoc)
 * @author Leonardo (Revisione e testing)
 * Matricole: 766743 (VA), 765763 (VA), 766745 (VA)
 */

public class Film {
	//Attributi del film
	private LocalDate data;
    private LocalTime ora;
    private final String titolo;
    private final String genere;
    private final String regista;
    private final int anno;
    private final int durata;
    private final int etaMinima;
    private double costoBiglietto;
    private int postiSala;
    private final String chiave;
    
    //capienza massima in sala
    public static final int capienza_max = 200;
    
    /**
     * Costruisce un nuovo oggetto Film con le informazioni principali.
     *
     * @param data data della proiezione
     * @param ora orario della proiezione
     * @param titolo titolo del film
     * @param genere genere del film
     * @param regista regista del film
     * @param anno anno di produzione del film
     * @param durata durata in minuti del film
     * @param etaMinima età minima consentita per la visione
     * @param costoBiglietto costo del biglietto per la proiezione
     */
    public Film(LocalDate data, LocalTime ora, String titolo, String genere, String regista, int anno, int durata, int etaMinima,  double costoBiglietto) {
    
    this.data = data;
    this.ora = ora;
    this.titolo = titolo;
    this.genere = genere;
    this.regista = regista;
    this.anno = anno;
    this.durata = durata;
    this.etaMinima = etaMinima;
    this.costoBiglietto = costoBiglietto;
    this.postiSala = capienza_max;
    this.chiave = data.toString()+ora.toString();
}
   public void setData(LocalDate d) {
	   this.data = d;
   }
   
   public void setOra(LocalTime o) {
	   this.ora = o;
   }
    
    //Metodi getter film
    
    public LocalDate getData() {
        return data;
    }

    public LocalTime getOra() {
        return ora;
    }
    
    public String getTitolo() {
        return titolo;
    }

    public String getGenere() {
        return genere;
    }

    public String getRegista() {
        return regista;
    }
    
    public int getAnno() {
        return anno;
    }

    public int getDurata() {
        return durata;
    }

    public int getEtaMinima() {
        return etaMinima;
    }

    public double getCostoBiglietto() {
        return costoBiglietto;
    }
    
    /**
     * Restituisce la chiave univoca del film, costruita da data e ora.
     *
     * @return chiave univoca della proiezione
     */
    public String getChiave() {
    	return this.chiave;
    }
    
    public int getPostiSala() {
        return postiSala;
    }
    
    /**
     * Riduce il numero di posti disponibili in sala quando vengono prenotati biglietti.
     *
     * @param numero numero di posti da rimuovere
     * @return true se l'operazione ha avuto successo, false se il numero non è valido o non ci sono abbastanza posti
     */
    public boolean eliminaPosti(int numero) {

        if (numero <= 0) return false;

        if (postiSala >= numero) {
            postiSala -= numero;
            return true;
        }

        return false;
    }
    
    public boolean aggiungiPosti(int numero) {

        if (numero <= 0) return false;

        if (postiSala + numero <= capienza_max) {
            postiSala += numero;
            return true;
        }

        return false;
    }
    
    /**
     * Restituisce la rappresentazione CSV del film.
     *
     * @return stringa CSV con i campi del film
     */
    public String toCSV() {
        return data + "," + ora + "," + titolo + "," + genere + "," + regista + "," + anno + "," + durata + "," + etaMinima + "," + costoBiglietto;
    }
    
    @Override
    public String toString() {
        return "Titolo: " + titolo + "\nGenere: " + genere + "\nRegista: " + regista +  "\nDurata: " + durata + "\nData: " + data + "\nOra: " + ora ;
   } 
 }

