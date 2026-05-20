package cinemax;
import java.time.LocalDate;
import java.time.LocalTime;

public class Film {
	//Attributi del film
	private LocalDate data;
    private LocalTime ora;
    private String titolo;
    private String genere;
    private String regista;
    private int anno;
    private int durata;
    private int etaMinima;
    private double costoBiglietto;
    private int postiSala;
    
    //capienza massima in sala
    public static final int capienza_max = 200;
    
    //costruttore del film
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
    
    public int getPostiSala() {
        return postiSala;
    }
    
    //Dato un numero di posti prenotatio cancellazioni di prenotazioni il metodo modifica la capienza della sala del film scelto
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
    
  //scrive l'oggetto Film già in formato corretto per il file utenti.csv
    
    public String toCSV() {
        return titolo + "," + genere + "," + regista + "," + anno + "," + durata + "," + etaMinima + "," + data + "," + ora + "," + costoBiglietto + "," + postiSala;
    }
    
    @Override
    public String toString() {
        return "Titolo: " + titolo + "\nGenere: " + genere + "\nRegista: " + regista + "\nAnno: " + anno + "\nDurata: " + durata + "\nEtà minima: " + etaMinima + "\nData: " + data + "\nOra: " + ora + "\nCosto: " + costoBiglietto + "\nPosti disponibili: " + postiSala;
    }
}
