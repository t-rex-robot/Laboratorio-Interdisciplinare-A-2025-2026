package cinemax;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.UUID;

public class GestionePrenotazioni {

    // Mappa: con chiave = codice prenotazione e valore = oggetto Prenotazione
    private Map<String, Prenotazione> mappaPrenotazioni;

    // percorso file CSV
    private final String PATH = "data/prenotazioni.csv";

    // costruttore
    public GestionePrenotazioni() {
        mappaPrenotazioni = new HashMap<>();
    }
    
    //Carico dal file csv nella mappa  le prenotazioni
    
    public void caricaPrenotazioniDaFile() throws FormatoDatiNonValidoException {

        try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {

            String riga;

            while ((riga = br.readLine()) != null) {

                String[] dati = riga.split(",");

                // controllo numero campi
                if (dati.length != 7) {
                    throw new FormatoDatiNonValidoException(
                        "Errore nel formato prenotazioni.csv"
                    );
                }

                Prenotazione p = new Prenotazione(
                    dati[0],                     // codice
                    dati[1],                     // username
                    dati[2],                     // titolo film
                    LocalDate.parse(dati[3]),   // data
                    LocalTime.parse(dati[4]),   // ora
                    Integer.parseInt(dati[5]),  // numero biglietti
                    Double.parseDouble(dati[6]) // costo unitario
                );

                
                mappaPrenotazioni.put( p.getCodicePrenotazione(), p);
            }

        } catch (IOException e) {

            System.err.println(
                "Errore caricamento prenotazioni: "
                + e.getMessage()
            );
        }
    }
    
    //Carica dalla mappa e salva nel file csv
    
    public void salvaPrenotazioniSuFile() {
    	
    	//Uso filewriter(PATH) perche riscreve tutto per evitare dubplicati e tenere lo stato aggiornato

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH))) {

            // scorre tutte le prenotazioni
            for (Prenotazione p : mappaPrenotazioni.values()) {
                
                bw.write(p.toCSV());

                bw.newLine();
            }

        } catch (IOException e) {

            System.err.println(
                "Errore salvataggio prenotazioni: "
                + e.getMessage()
            );
        }
    }
    
    //Genera il codice univoco per la prenotazione tramite un randomizzatore
    
    private String generaCodicePrenotazione() {

    	return UUID.randomUUID().toString();
    }
    
    //Creazione delle prenotazioni
    
    public boolean creaPrenotazione(Utente utente, Film film, int numeroBiglietti) {

		// controllo numero valido
		if (numeroBiglietti <= 0) {
		return false;
		}
		
		// controllo posti disponibili
		if (film.getPostiSala() < numeroBiglietti) {
		
		System.out.println("Posti insufficienti!");
		return false;
		}
		
		// genera codice univoco
		String codice = generaCodicePrenotazione();
		
		// aggiorna posti film
		film.eliminaPosti(numeroBiglietti);
		
		// crea prenotazione
		Prenotazione p = new Prenotazione( codice, utente.getUsername(), film.getTitolo(), film.getData(), film.getOra(), numeroBiglietti, film.getCostoBiglietto()
		);
		
		mappaPrenotazioni.put( p.getCodicePrenotazione(), p);
		
		salvaPrenotazioniSuFile();
		System.out.println("Prenotazione creata!");
		
		return true;
		}
    
    //Ricerca per codice prenotazione
    
    public Prenotazione cercaPerCodice(String codice) {

        if (mappaPrenotazioni.containsKey(codice)) {
            return mappaPrenotazioni.get(codice);
        }

        System.out.println("Prenotazione non trovata");
        return null;
    }
    
    //Ricerca per Utente (restituisco una lista perchè l'utente ha più di una prenotazione)
    
    public List<Prenotazione> cercaPerUtente(String username) {

        List<Prenotazione> risultato = new ArrayList<>();

        for (Prenotazione p : mappaPrenotazioni.values()) {

            if (p.getUsername().equalsIgnoreCase(username)) {
                risultato.add(p);
            }
        }

        return risultato;
    }
    
    //Ricerca per titolo film parziale (lista)
    
    public List<Prenotazione> cercaPerFilm(String titolo) {

        List<Prenotazione> risultato = new ArrayList<>();

        for (Prenotazione p : mappaPrenotazioni.values()) {

            if (p.getTitoloFilm().toLowerCase().contains(titolo.toLowerCase())) {

                risultato.add(p);
            }
        }

        return risultato;
    }
    
    //Ricerca per intervallo di date (listone)
    
    public List<Prenotazione> cercaPerIntervalloDate(LocalDate inizio, LocalDate fine) {

		List<Prenotazione> risultato = new ArrayList<>();
			
		for (Prenotazione p : mappaPrenotazioni.values()) {
			
			LocalDate data = p.getDataProiezione();
			
			if ((data.isEqual(inizio) || data.isAfter(inizio)) && (data.isEqual(fine) || data.isBefore(fine))) {
			
				risultato.add(p);
			}
		}
			
		return risultato;
	}
    
    //Metodo che riicerca la Prenotazione
    
    public List<Prenotazione> cercaPrenotazione(int tipoRicerca, String... parametri) {

        switch (tipoRicerca) {

            case 1:
                // per codice
                Prenotazione p = cercaPerCodice(parametri[0]);
                return (p != null) ? List.of(p) : new ArrayList<>();

            case 2:
                // per utente
                return cercaPerUtente(parametri[0]);

            case 3:
                // per titolo film
                return cercaPerFilm(parametri[0]);

            case 4:
                // per intervallo date
                LocalDate inizio = LocalDate.parse(parametri[0]);
                LocalDate fine = LocalDate.parse(parametri[1]);
                return cercaPerIntervalloDate(inizio, fine);

            default:
                System.out.println("Tipo ricerca non valido");
                return new ArrayList<>();
        }
    }
    
    //Visualizza le prenotazioni (da rivedere non sono convinto mostri tutto bene, dovrei fare un ciclo poi per più prenotazioni?)
    
    public void visualizzaPrenotazione(Prenotazione p) {

        System.out.println("===== PRENOTAZIONE =====");
        System.out.println("Codice: " + p.getCodicePrenotazione());
        System.out.println("Cliente: " + p.getUsername());
        System.out.println("Data: " + p.getDataProiezione());
        System.out.println("Ora: " + p.getOraProiezione());
        System.out.println("Biglietti: " + p.getNumeroBiglietti());
        System.out.println("Costo unitario: " + p.getCostoUnitario());

        double totale = p.getNumeroBiglietti() * p.getCostoUnitario();
        System.out.println("Totale: " + totale);
    }
    
    //Meotodo che elimina le prenotazioni (Va sistemata la ricerca del film)
    
    public boolean eliminaPrenotazione(String codice) {

        Prenotazione p = mappaPrenotazioni.get(codice);

        if (p == null) {
            System.out.println("Prenotazione non trovata");
            return false;
        }

        // controllo data
        if (p.getDataProiezione().isBefore(LocalDate.now())) {
            System.out.println("Impossibile eliminare: proiezione già avvenuta");
            return false;
        }

        // recupero film (manca modo di trovarlo)
        Film film = trovaFilm(p.getTitoloFilm(), p.getDataProiezione(), p.getOraProiezione());

        if (film != null) {
            film.aggiungiPosti(p.getNumeroBiglietti());
        }

        
        mappaPrenotazioni.remove(codice);

        salvaPrenotazioniSuFile();

        System.out.println("Prenotazione eliminata con successo");
        return true;
    }
    
    //Metodo per modificare le prenotazioni (stesso problema ricerca film)
    
    public boolean modificaPrenotazione(String codice, int nuoviBiglietti) {

        Prenotazione p = mappaPrenotazioni.get(codice);

        if (p == null) {
            System.out.println("Prenotazione non trovata");
            return false;
        }

        // controllo data (solo futuro)
        if (p.getDataProiezione().isBefore(LocalDate.now())) {
            System.out.println("Impossibile modificare una proiezione passata");
            return false;
        }

        Film film = trovaFilm(p.getTitoloFilm(), p.getDataProiezione(), p.getOraProiezione());

        if (film == null) {
            System.out.println("Film non trovato");
            return false;
        }

        int vecchiBiglietti = p.getNumeroBiglietti();

        // CASO 1: aumento biglietti
        if (nuoviBiglietti > vecchiBiglietti) {

            int differenza = nuoviBiglietti - vecchiBiglietti;

            if (film.getPostiSala() < differenza) {
                System.out.println("Posti insufficienti");
                return false;
            }

            film.eliminaPosti(differenza);
        }

        // CASO 2: riduzione biglietti
        else if (nuoviBiglietti < vecchiBiglietti) {

            int differenza = vecchiBiglietti - nuoviBiglietti;
            film.aggiungiPosti(differenza);
        }

        // aggiorna prenotazione
        p.setNumeroBiglietti(nuoviBiglietti);

        salvaPrenotazioniSuFile();

        System.out.println("Prenotazione modificata con successo");
        return true;
    }
}