package cinemax;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Gestisce le prenotazioni degli utenti: caricamento/salvataggio su file,
 * creazione, ricerca, modifica ed eliminazione delle prenotazioni.
 */
public class GestionePrenotazioni {

    // Mappa: con chiave = codice prenotazione e valore = oggetto Prenotazione
    private Map<String, Prenotazione> mappaPrenotazioni;

    // percorso file CSV
    private final String PATH = "data/prenotazioni.csv";
    
 // riferimento a i Film
    private GestioneFilm gestioneFilm;
    
 // Costruttore Principale
 // obbliga il collegamento con GestioneFilm per evitare errori
    public GestionePrenotazioni(GestioneFilm gestioneFilm) {
        this.gestioneFilm = gestioneFilm; // collegamento obbligatorio
        mappaPrenotazioni = new HashMap<>();
    }
    
    /**
     * Carica le prenotazioni dal file CSV in `PATH` e popola la mappa interna.
     *
     * @throws FormatoDatiNonValidoException se una riga del file non rispetta il formato previsto
     */
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
    
    /**
     * Salva tutte le prenotazioni correnti sul file CSV, sovrascrivendo il contenuto.
     */
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
    
    /**
     * Crea una nuova prenotazione per l'utente e aggiorna i posti del film.
     * Genera un codice univoco e persiste la prenotazione su file.
     *
     * @param utente oggetto `Utente` che effettua la prenotazione
     * @param film oggetto `Film` relativo alla proiezione
     * @param numeroBiglietti numero di biglietti richiesti
     * @return true se la prenotazione è stata creata con successo, false altrimenti
     */
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
    
    /**
     * Cerca una prenotazione per codice.
     *
     * @param codice codice della prenotazione
     * @return l'oggetto `Prenotazione` se trovato, altrimenti null
     */
    public Prenotazione cercaPerCodice(String codice) {

        if (mappaPrenotazioni.containsKey(codice)) {
            return mappaPrenotazioni.get(codice);
        }

        System.out.println("Prenotazione non trovata");
        return null;
    }
    
    /**
     * Restituisce tutte le prenotazioni associate a uno username.
     *
     * @param username username dell'utente
     * @return lista di prenotazioni dell'utente
     */
    public List<Prenotazione> cercaPerUtente(String username) {

        List<Prenotazione> risultato = new ArrayList<>();

        for (Prenotazione p : mappaPrenotazioni.values()) {

            if (p.getUsername().equalsIgnoreCase(username)) {
                risultato.add(p);
            }
        }

        return risultato;
    }
    
    /**
     * Cerca prenotazioni il cui titolo del film contiene la stringa fornita.
     *
     * @param titolo sottostringa del titolo da cercare
     * @return lista di prenotazioni corrispondenti
     */
    public List<Prenotazione> cercaPerFilm(String titolo) {

        List<Prenotazione> risultato = new ArrayList<>();

        for (Prenotazione p : mappaPrenotazioni.values()) {

            if (p.getTitoloFilm().toLowerCase().contains(titolo.toLowerCase())) {

                risultato.add(p);
            }
        }

        return risultato;
    }
    
    /**
     * Restituisce le prenotazioni la cui data di proiezione rientra nell'intervallo fornito.
     *
     * @param inizio data di inizio (inclusa)
     * @param fine data di fine (inclusa)
     * @return lista di prenotazioni nell'intervallo
     */
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
    
    /**
     * Interfaccia di ricerca per le prenotazioni: per codice, utente, film o intervallo date.
     *
     * @param tipoRicerca 1=codice,2=utente,3=film,4=intervallo date
     * @param parametri parametri variabili a seconda del tipo di ricerca
     * @return lista di prenotazioni risultato della ricerca
     */
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
    
    /**
     * Stampa a terminale i dettagli di una prenotazione.
     *
     * @param p oggetto `Prenotazione` da visualizzare
     */
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
    
    /**
     * Elimina una prenotazione se la proiezione non è ancora avvenuta.
     * Ripristina i posti sul film associato e aggiorna il file.
     *
     * @param codice codice della prenotazione da eliminare
     * @return true se l'eliminazione è riuscita, false altrimenti
     */
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

        // recupero film
        Film film = gestioneFilm.trovaProiezione( p.getTitoloFilm(), p.getDataProiezione(), p.getOraProiezione());

        if (film != null) {
            film.aggiungiPosti(p.getNumeroBiglietti());
        }

        
        mappaPrenotazioni.remove(codice);

        salvaPrenotazioniSuFile();

        System.out.println("Prenotazione eliminata con successo");
        return true;
    }
    
    /**
     * Modifica il numero di biglietti di una prenotazione futura, adeguando i posti del film.
     *
     * @param codice codice della prenotazione
     * @param nuoviBiglietti nuovo numero di biglietti richiesto
     * @return true se la modifica è stata applicata, false altrimenti
     */
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

        Film film = gestioneFilm.trovaProiezione( p.getTitoloFilm(), p.getDataProiezione(), p.getOraProiezione());

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
    
    /**
     * Restituisce le prenotazioni la cui data di proiezione è oggi.
     *
     * @return lista di prenotazioni per la data odierna
     */
    public List<Prenotazione> prenotazioniOggi() {

        List<Prenotazione> risultato = new ArrayList<>();

        LocalDate oggi = LocalDate.now();

        for (Prenotazione p : mappaPrenotazioni.values()) {

            if (p.getDataProiezione().isEqual(oggi)) {
                risultato.add(p);
            }
        }

        return risultato;
    }
}