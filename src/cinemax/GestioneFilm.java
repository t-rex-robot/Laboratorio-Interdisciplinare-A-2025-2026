package cinemax;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.io.*;

/**
 * Gestisce il caricamento, la memorizzazione e le operazioni sulle proiezioni di film.
 * Conserva le proiezioni in memoria e le sincronizza con il file CSV dei film.
 */
public class GestioneFilm {
	private Map<String, Film> mappaFilm;
	private final String PATH="data/film.csv";
	
	/**
	 * Costruisce il gestore dei film inizializzando la mappa interna.
	 */
	public GestioneFilm() {
		mappaFilm = new HashMap<>();
	}
	
	/**
	 * Carica le proiezioni dei film dal file CSV configurato.
	 *
	 * @throws FormatoDatiNonValidoException se il file contiene un numero di campi non valido
	 */
	public void caricaFilmDaFile() throws FormatoDatiNonValidoException {
		try(BufferedReader br = new BufferedReader(new FileReader(PATH))){
			String riga;
			while((riga=br.readLine()) != null) {
				String[] dati = riga.split(",");
				if(dati.length != 9) throw new FormatoDatiNonValidoException("Errore nel formato dei dati: attesi 9 campi, trovati "+ dati.length+" campi.");
				Film f = new Film(LocalDate.parse(dati[0]), LocalTime.parse(dati[1]), dati[2], dati[3], dati[4], Integer.parseInt(dati[5]), Integer.parseInt(dati[6]), Integer.parseInt(dati[7]), Double.parseDouble(dati[7]));
				mappaFilm.put(f.getChiave(), f);
			}
		}catch (IOException e) {
			System.err.println("Errore nel caricamento: "+e.getMessage());
		}
				
	}
	
	/**
	 * Salva tutte le proiezioni correnti nel file CSV.
	 */
	public void salvaFilmSuFile() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH))){
			for(Film f : mappaFilm.values()) {
				bw.write(f.toCSV());
				bw.newLine();
			}
		}
			catch (IOException e) {
				System.err.println("Errore nel salvataggio dei film " + e.getMessage());
			}
	}
	
	/**
	 * Controlla che la durata di una nuova proiezione non si sovrapponga a quella di altre proiezioni già programmate nello stesso giorno.
	 *
	 * @param data data proposta per la nuova proiezione
	 * @param ora orario proposto per la nuova proiezione
	 * @param durata durata della nuova proiezione in minuti
	 * @return true se l'orario è disponibile, false se si verifica una sovrapposizione
	 */
	public boolean controlloDurata(LocalDate data, LocalTime ora, int durata) {
		for(Film f : mappaFilm.values()) {
			if(f.getData().equals(data)) {
				if(f.getOra().isBefore(ora)) {
					if(ChronoUnit.MINUTES.between(f.getOra(), ora)<f.getDurata()) {System.out.println("Orario non disponibile, proiezione precedente acnora in corso"); return false;}
				}else if(ChronoUnit.MINUTES.between(ora, f.getOra())<durata) {System.out.println("Orario non disponibile, durata del film non compatibile con l'orario della proiezione successiva"); return false;}
			}
		}
		return true;
	}
	
	/**
	 * Crea una nuova proiezione di film se i dati sono validi e l'orario è disponibile.
	 *
	 * @param data data della proiezione
	 * @param ora orario della proiezione
	 * @param titolo titolo del film
	 * @param genere genere del film
	 * @param regista regista del film
	 * @param anno anno di produzione
	 * @param durata durata in minuti
	 * @param etaMinima età minima richiesta
	 * @param costoBiglietto costo del biglietto
	 * @return true se la proiezione viene creata con successo, false altrimenti
	 * @throws FilmException in caso di dati non validi o orari già occupati
	 */
	public boolean creaProiezione(LocalDate data, LocalTime ora, String titolo, String genere, String regista, int anno, int durata, int etaMinima,  double costoBiglietto) {
		if(data.isBefore(LocalDate.now())) {
			throw new FilmException("Data non valida");
		}
		
		if(etaMinima<0) {
			throw new FilmException("Età minima non valida");
		}
		//controlla che non ci sia un film con stessa data e stessa ora
		if(mappaFilm.containsKey(data.toString()+ora.toString())) {
			throw new FilmException("Esiste già una proiezione per la stessa data e ora");
		}
		//controlla che la durata del potenziale nuovo film sia compatibile con quelle delle proiezioni già esistenti nello stesso giorno
		if(controlloDurata(data, ora, durata)) {
			Film f = new Film(data, ora, titolo, genere, regista, anno, durata, etaMinima, costoBiglietto);
			mappaFilm.put(f.getChiave(), f);
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(PATH, true))){
				String film = f.toCSV();
				bw.write(film);
				bw.newLine();
				System.out.println("Proiezione creata con successo!");
				return true;
			}
			catch(IOException e) {
				System.err.println("Errore nella creazione della proiezione" + e.getMessage());
			}
		}
	return false;	
	}
	
	/**
	 * Cerca i film il cui titolo contiene la stringa specificata, ignorando il maiuscolo/minuscolo.
	 *
	 * @param t stringa da cercare nel titolo
	 * @return elenco dei film che corrispondono alla ricerca per titolo
	 */
	public LinkedList<Film> trovaPerTitolo(String t){
		LinkedList<Film> l = new LinkedList<Film>();
		for (Film f : mappaFilm.values()) {
			if((f.getTitolo().toLowerCase()).contains(t.toLowerCase()))
				l.add(f);
		}
		return l;
	}
	
	/**
	 * Cerca i film per genere.
	 *
	 * @param g genere da cercare
	 * @return elenco dei film che appartengono al genere specificato
	 */
	public LinkedList<Film> trovaPerGenere(String g){
		LinkedList<Film> l = new LinkedList<Film>();
		for (Film f : mappaFilm.values()) {
			if((f.getGenere().toLowerCase())==g.toLowerCase())
				l.add(f);
		}
		return l;
	}
	
	/**
	 * Cerca i film programmati in un intervallo di date inclusivo.
	 *
	 * @param inizio data di inizio della ricerca
	 * @param fine data di fine della ricerca
	 * @return elenco dei film che rientrano nell'intervallo di date
	 */
	public LinkedList<Film> trovaPerDate(LocalDate inizio, LocalDate fine){
		LinkedList<Film> l = new LinkedList<Film>();
		for (Film f : mappaFilm.values()) {
			if((f.getData().equals(inizio) || inizio.isBefore(f.getData())) && (f.getData().equals(fine) || fine.isAfter(f.getData())))
				l.add(f);
		}
		return l;
	}
	
	/**
	 * Cerca i film in base al costo del biglietto.
	 *
	 * @param min costo minimo
	 * @param max costo massimo
	 * @return elenco dei film con costo compreso tra min e max
	 */
	public LinkedList<Film> trovaPerCosto(double min, double max){
		LinkedList<Film> l = new LinkedList<Film>();
		for (Film f : mappaFilm.values()) {
			if(f.getCostoBiglietto()>=min && f.getCostoBiglietto()<=max)
				l.add(f);
		}
		return l;
	}
	
	/**
	 * Esegue una ricerca di film in base al tipo selezionato.
	 *
	 * @param tipoRicerca 1=titolo, 2=genere, 3=date, 4=costo
	 * @param parametri parametri della ricerca, variabili in base al tipo
	 * @return elenco dei film risultati dalla ricerca
	 */
	public LinkedList<Film> trovaFilm(int tipoRicerca, String ...parametri){
		switch(tipoRicerca) {
		case 1:
			return trovaPerTitolo(parametri[0]);
			
		case 2:
			return trovaPerGenere(parametri[0]);
			
		case 3:
			if(parametri.length<2) {
				System.out.println("Per la ricerca servono due date!");
				return new LinkedList<Film>();
			}
			return trovaPerDate(LocalDate.parse(parametri[0]), LocalDate.parse(parametri[1]));
			
		case 4:
			if(parametri.length<2) {
				System.out.println("Per la ricerca servono due valori!");
				return new LinkedList<Film>();
			}
			return trovaPerCosto(Double.parseDouble(parametri[0]), Double.parseDouble(parametri[1]));
			
		default:
			System.out.println("Tipo di ricerca non valido!");
			return new LinkedList<Film>();
		}
	}
	
	/**
	 * Mostra i dettagli della proiezione di un film.
	 *
	 * @param f film da visualizzare
	 */
	public void visualizzaProiezione(Film f) {
		System.out.println("===== PROIEZIONE =====");
		System.out.println("Titolo: " + f.getTitolo());
		System.out.println("Data e ora: " + f.getData().toString() + " " + f.getOra().toString());
		System.out.println("Genere: " + f.getGenere());
		System.out.println("Regista e anno di uscita: " + f.getRegista() + " " + f.getAnno());
		System.out.println("Durata ed età minima: " + f.getDurata() + " minuti, " + f.getEtaMinima() + " anni");
		System.out.println("Costo del biglietto: " + f.getCostoBiglietto() + "euro");
		System.out.println("Posti liberi: " + f.getPostiSala());
	}
	
	/**
	 * Elimina una proiezione se non ci sono prenotazioni associate.
	 *
	 * @param f film da eliminare
	 * @return true se la proiezione è stata eliminata, false altrimenti
	 */
	public boolean eliminaProiezione(Film f) {
		if(!mappaFilm.containsKey(f.getChiave())) {
			System.out.println("Proiezione non esistente");
			return false;
		}
		//elimina solo se non ci sono prenotazioni per quella proiezione (quindi se ci sono ancora 200 posti liberi)
		if (f.getPostiSala() == Film.capienza_max) {
			mappaFilm.remove(f.getChiave());
			salvaFilmSuFile();
			System.out.println("Proiezione eliminata con successo! ");
			return true;
		}
		System.out.println("Impossibile cancellare una proiezione con prenotazioni");
		return false;
	}
	
	/**
	 * Modifica i dettagli di una proiezione se non ci sono prenotazioni e i nuovi dati sono validi.
	 *
	 * @param f film da modificare
	 * @param tipoModifica 1=cambia data, 2=cambia ora
	 * @param parametri nuovi valori da applicare alla proiezione
	 * @return true se la modifica è stata applicata, false altrimenti
	 */
	public boolean modificaProiezione(Film f, int tipoModifica, Object ...parametri) {
		if (f.getPostiSala()==Film.capienza_max) {
			switch(tipoModifica) {
			
			//modifica la data
			case 1:
				if(parametri[0] instanceof LocalDate) {
					if(((LocalDate) parametri[0]).isBefore(LocalDate.now()))
						return false;
					if(controlloDurata((LocalDate)parametri[0], f.getOra(), f.getDurata())) {
						f.setData((LocalDate)parametri[0]);
						salvaFilmSuFile();
						return true;
					}
					return false;
				}
				
			//modifica l'orario
			case 2:
				if(parametri[0] instanceof LocalTime) {
					if(((LocalTime)parametri[0]).isBefore(LocalTime.now()))
						return false;
					if(controlloDurata(f.getData(), (LocalTime)parametri[0], f.getDurata())){
						f.setOra((LocalTime)parametri[0]);
						salvaFilmSuFile();
						return true;
					}
					return false;
				}
			
			default:
				System.out.println("Tipo di ricerca non valido! ");
				return false;
			}
		}
		System.out.println("Impossibile modificare una proiezione con prenotazioni");
		return false;
			
	}

}
	
