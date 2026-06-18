package cinemax;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.io.*;

public class GestioneFilm {
	private Map<String, Film> mappaFilm;
	private final String PATH="data/film.csv";
	
	public GestioneFilm() {
		mappaFilm = new HashMap<>();
	}
	
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
	//mostra tutti i film caricati
	public List<Film> getTuttiFilm() {
	    return new ArrayList<>(mappaFilm.values());
	}
	
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
	
	public LinkedList<Film> trovaPerTitolo(String t){
		LinkedList<Film> l = new LinkedList<Film>();
		for (Film f : mappaFilm.values()) {
			if((f.getTitolo().toLowerCase()).contains(t.toLowerCase()))
				l.add(f);
		}
		return l;
	}
	
	public LinkedList<Film> trovaPerGenere(String g){
		LinkedList<Film> l = new LinkedList<Film>();
		for (Film f : mappaFilm.values()) {
			if((f.getGenere().toLowerCase())==g.toLowerCase())
				l.add(f);
		}
		return l;
	}
	
	public LinkedList<Film> trovaPerDate(LocalDate inizio, LocalDate fine){
		LinkedList<Film> l = new LinkedList<Film>();
		for (Film f : mappaFilm.values()) {
			if((f.getData().equals(inizio) || inizio.isBefore(f.getData())) && (f.getData().equals(fine) || fine.isAfter(f.getData())))
				l.add(f);
		}
		return l;
	}
	
	public LinkedList<Film> trovaPerCosto(double min, double max){
		LinkedList<Film> l = new LinkedList<Film>();
		for (Film f : mappaFilm.values()) {
			if(f.getCostoBiglietto()>=min && f.getCostoBiglietto()<=max)
				l.add(f);
		}
		return l;
	}
	
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
	
	// Trova una singola proiezione precisa
	// Usato internamente da GestionePrenotazioni
	public Film trovaProiezione(
	        String titolo,
	        LocalDate data,
	        LocalTime ora
	) {

	    for (Film f : mappaFilm.values()) {

	        if (
	            f.getTitolo().equalsIgnoreCase(titolo)
	            &&
	            f.getData().equals(data)
	            &&
	            f.getOra().equals(ora)
	        ) {

	            return f;
	        }
	    }

	    return null;
	}
	
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
