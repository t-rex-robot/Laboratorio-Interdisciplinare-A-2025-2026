package cinemax;
import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Gestisce la ricerca e la visualizzazione delle proiezioni disponibili.
 * Fornisce metodi per filtrare le proiezioni per titolo parziale, genere,
 * intervallo di date e costo del biglietto senza richiedere autenticazione.
 */
public class GestioneFilm {
	public enum Criterio{
		COMPRESO_TRA, PRIMA_DI, DOPO_DI;
	}
	private Map<String, Film> mappaFilm;
	private final String PATH="data/film.csv";
	
	public GestioneFilm() {
		mappaFilm = new HashMap<>();
	}
	
	/**
	 * Carica le proiezioni dal file CSV nella mappa.
	 * Ogni riga del file deve rispettare il formato previsto, altrimenti viene
	 * sollevata un'eccezione di formato non valido.
	 *
	 * @throws FormatoDatiNonValidoException se il file non rispetta il formato previsto
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
	 * Salva tutte le proiezioni correnti su file CSV.
	 * Sovrascrive il contenuto esistente del file per aggiornare lo stato 
	 * delle proiezioni.
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
	 * Restituisce l'elenco di tutte le proiezioni caricate in memoria.
	 *
	 * @return lista completa delle proiezioni attualmente disponibili
	 */
	public List<Film> getTuttiFilm() {
	    return new ArrayList<>(mappaFilm.values());
	}
	
	/**
	 * Controlla che la nuova proiezione non si sovrapponga a 
	 * quelle già programmate per lo stesso giorno.
	 *
	 * @param data data della nuova proiezione
	 * @param ora ora della nuova proiezione 
	 * @param durata durata in minuti della nuova proiezione
	 * @return true se la nuova proiezione non si sovrappone ad altre proiezioni
	 */
	public boolean controlloDurata(LocalDate data, LocalTime ora, int durata) {
		for(Film f : mappaFilm.values()) {
			if(f.getData().equals(data)) {
				if(f.getOra().isBefore(ora)) {
					if(ChronoUnit.MINUTES.between(f.getOra(), ora)<f.getDurata()) {System.out.println("Orario non disponibile, la seguente proiezione è acnora in corso "+"\n"+f.toString() ); return false;}
				}else if(ChronoUnit.MINUTES.between(ora, f.getOra())<durata) {System.out.println("Orario non disponibile, durata del film non compatibile con l'orario della proiezione successiva"+"\n"+f.toString()); return false;}
			}
		}
		return true;
	}
	
	/**
	 * Crea una nuova proiezione e la salu file.
 	 * Il metodo verifica la validità della data, il limite di età minima e 
	 * controlla che non ci siano sovrapposizioni con altre proiezioni già programmate.
	 *
	 * @param data data della proiezione
	 * @param ora ora della proiezione
	 * @param titolo titolo del film
	 * @param genere genere del film
	 * @param regista regista del film
	 * @param anno anno di uscita del film
	 * @param durata durata in minuti
	 * @param etaMinima età minima richiesta per la proiezione
	 * @param costoBiglietto costo del biglietto
	 * @return true se la proiezione è stata creata e salvata correttamente
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
		//controlla che la durata del nuovo film non si sovrapponga ad altre proiezioni nello stesso giorno
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
	 * Restituisce le proiezioni il cui titolo contiene la sottostringa
	 * specificata. Il confronto è case-insensitive per agevolare la ricerca.
	 *
	 * @param t sottostringa del titolo da cercare
	 * @return elenco delle proiezioni corrispondenti
	 */
	public LinkedList<Film> trovaPerTitolo(String t){
		LinkedList<Film> l = new LinkedList<>();
		for (Film f : mappaFilm.values()) {
			if((f.getTitolo().toLowerCase()).contains(t.toLowerCase()))
				l.add(f);
		}
		return l;
	}
	
	/**
	 * Restituisce le proiezioni aggregate per genere specificato.
	 *
	 * @param g genere del film
	 * @return elenco delle proiezioni corrispondenti al genere
	 */
	public LinkedList<Film> trovaPerGenere(String g){
		LinkedList<Film> l = new LinkedList<>();
		for (Film f : mappaFilm.values()) {
			if (f.getGenere().equalsIgnoreCase(g))
				l.add(f);
		}
		return l;
	}
	
	/**
	 * Applica un filtro temporale alle proiezioni.
	 * Supporta i criteri {@code PRIMA_DI}, {@code DOPO_DI} e 
	 * {@code COMPRESO_TRA} per scegliere l'intervallo di date.
	 *
	 * @param crit criterio di ricerca delle date
	 * @param data1 data principale del filtro
	 * @param data2 data secondaria, valida solo per {@code Criterio.COMPRESO_TRA}
	 * @return elenco delle proiezioni che soddisfano il criterio temporale
	 */
	public LinkedList<Film> trovaPerDate(Criterio crit, LocalDate data1, LocalDate data2){
		LinkedList<Film> l = new LinkedList<>();
		switch(crit) {
		case COMPRESO_TRA:
			if(data2.isBefore(data1)) throw new FormatoDatiNonValidoException("Date non valide");
			for (Film f : mappaFilm.values()) {
				if((f.getData().equals(data1) || data1.isBefore(f.getData())) && (f.getData().equals(data2) || data2.isAfter(f.getData())))
					l.add(f);
			}
			return l;
		
		//nei casi di PRIMA_DI e DOPO_DI la seconda data è messa a null
		case PRIMA_DI: 
			for (Film f : mappaFilm.values()) {
				if (f.getData().isBefore(data1))
					l.add(f);
			}
			return l;
			
		case DOPO_DI:
			for (Film f : mappaFilm.values()) {
				if(f.getData().isAfter(data1))
					l.add(f);
			}
			return l;
			
		default:
			System.out.println("Ricerca non valida");
			return l;	
		}
	}
	
	/**
	 * Applica un filtro sul costo del biglietto.
	 * Supporta ricerche per prezzo minore, maggiore o compreso tra due soglie.
	 *
	 * @param crit criterio di confronto sul costo
	 * @param costo1 primo valore del filtro
	 * @param costo2 secondo valore, valido solo per {@code Criterio.COMPRESO_TRA}
	 * @return elenco delle proiezioni che soddisfano il filtro di costo
	 */
	public LinkedList<Film> trovaPerCosto(Criterio crit, double costo1, double costo2){
		LinkedList<Film> l = new LinkedList<>();
		if(costo1<0) throw new FormatoDatiNonValidoException("Non esistono costi negativi");
		
		switch(crit) {
		case COMPRESO_TRA:
			if (costo1>costo2) throw new FormatoDatiNonValidoException("Valori non validi");
			for (Film f : mappaFilm.values()) {
				if(f.getCostoBiglietto()>=costo1 && f.getCostoBiglietto()<=costo2)
					l.add(f);
			}
			return l;
			
		case PRIMA_DI:
			for (Film f : mappaFilm.values()) {
				if(f.getCostoBiglietto() <= costo1)
					l.add(f);
			}
			return l;
			
		case DOPO_DI:
			for (Film f : mappaFilm.values()) {
				if(f.getCostoBiglietto() >= costo1)
					l.add(f);
			}
			return l;
			
		default:
			System.out.println("Ricerca non valida");
			return l;
	
		}
	}
	
	/**
	 * Esegue una ricerca con un singolo criterio (titolo, genere, data o costo).
	 *
	 * @param tipoRicerca tipo di filtro da applicare (1=titolo, 2=genere, 3=data, 4=costo)
	 * @param parametri parametri del filtro
	 * @return elenco delle proiezioni risultanti
	 */
	public LinkedList<Film> trovaFilm(int tipoRicerca, String ...parametri){
		switch(tipoRicerca) {
		case 1 -> {
                    return trovaPerTitolo(parametri[0]);
                }
			
		case 2 -> {
                    return trovaPerGenere(parametri[0]);
                }
			
		case 3 -> {
                    return trovaPerDate(Criterio.valueOf(parametri[0]), LocalDate.parse(parametri[1]), LocalDate.parse(parametri[2]));
                }
			
		case 4 -> {
                    return trovaPerCosto(Criterio.valueOf(parametri[0]), Double.parseDouble(parametri[1]), Double.parseDouble(parametri[2]));
                }
			
		default -> {
                    System.out.println("Tipo di ricerca non valido!");
                    return new LinkedList<>();
                }
		}
	}
	
	// Trova una singola proiezione precisa
	// Usato internamente da GestionePrenotazioni
	/**
	 * Cerca una proiezione esatta a partire da titolo, data e ora.
	 * Restituisce la proiezione corrispondente oppure {@code null}
	 * se non esiste una corrispondenza completa.
	 *
	 * @param titolo titolo del film
	 * @param data data della proiezione
	 * @param ora ora della proiezione
	 * @return la proiezione trovata, o {@code null} se non presente
	 */
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
	
	//Ricerca di un film con i filtri creati prima
	/**
	 * Cerca proiezioni applicando filtri combinati su titolo, genere,
	 * data e costo del biglietto. I criteri vengono concatenati in modo
	 * da restituire solo le proiezioni che soddisfano tutti i vincoli.
	 *
	 * @param titolo parte del titolo da cercare
	 * @param genere genere del film
	 * @param criterioDate criterio di selezione delle date
	 * @param data1 data principale del filtro
	 * @param data2 data secondaria del filtro date
	 * @param criterioCosto criterio di selezione del costo
	 * @param costo1 primo valore del filtro prezzo
	 * @param costo2 secondo valore del filtro prezzo
	 * @return elenco delle proiezioni che soddisfano tutti i criteri specificati
	 */
	public List<Film> cercaFilm(
	        String titolo,
	        String genere,
	        Criterio criterioDate,
	        LocalDate data1,
	        LocalDate data2,
	        Criterio criterioCosto,
	        Double costo1,
	        Double costo2
	) {

	    List<Film> risultati = getTuttiFilm();

	    // filtro titolo
	    if (titolo != null && !titolo.isBlank()) {
	        risultati.retainAll(
	                trovaPerTitolo(titolo)
	        );
	    }

	    // filtro genere
	    if (genere != null && !genere.isBlank()) {
	        risultati.retainAll(
	                trovaPerGenere(genere)
	        );
	    }

	    // filtro date
	    if (criterioDate != null && data1 != null) {

	        if (criterioDate == Criterio.COMPRESO_TRA && data2 == null)
	            throw new FormatoDatiNonValidoException(
	                    "Serve una seconda data"
	            );

	        risultati.retainAll(
	                trovaPerDate(
	                        criterioDate,
	                        data1,
	                        data2
	                )
	        );
	    }

	    // filtro costo
	    if (criterioCosto != null && costo1 != null) {

	        if (criterioCosto == Criterio.COMPRESO_TRA && costo2 == null)
	            throw new FormatoDatiNonValidoException(
	                    "Serve un secondo costo"
	            );

	        risultati.retainAll(
	                trovaPerCosto(
	                        criterioCosto,
	                        costo1,
	                        costo2
	                )
	        );
	    }

	    return risultati;
	}
	
	/**
	 * Stampa le informazioni principali di una proiezione.
	 * Include titolo, genere, regista, anno, durata, data/ora, costo
	 * e posti liberi residui.
	 *
	 * @param f proiezione da visualizzare
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
				if(parametri[0] instanceof LocalDate localDate) {
					if(localDate.isBefore(LocalDate.now()))
						return false;
					if(controlloDurata(localDate, f.getOra(), f.getDurata())) {
						f.setData(localDate);
						salvaFilmSuFile();
						return true;
					}
					return false;
				}

				
			//modifica l'orario
			case 2:
				if(parametri[0] instanceof LocalTime localTime) {
					if(localTime.isBefore(LocalTime.now()))
						return false;
					if(controlloDurata(f.getData(), localTime, f.getDurata())){
						f.setOra(localTime);
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
