package cinemax;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class menu {

    // Scanner per leggere input utente
    private Scanner scanner;

    // Gestori del sistema
    private GestioneFilm gestioneFilm;
    private GestioneUtenti gestioneUtenti;
    private GestionePrenotazioni gestionePrenotazioni;
    private Utente utenteLoggato = null;

    // Costruttore
    public menu(GestioneFilm gestioneFilm, GestioneUtenti gestioneUtenti,GestionePrenotazioni gestionePrenotazioni) {

        this.scanner = new Scanner(System.in);

        this.gestioneFilm = gestioneFilm;
        this.gestioneUtenti = gestioneUtenti;
        this.gestionePrenotazioni = gestionePrenotazioni;
    }
    
    //Router menu in base al ruolo
    private void menuRuolo() {

        switch (utenteLoggato.getRuolo()) {

            case CLIENTE:
                menuCliente();
                break;

            case BIGLIETTAIO:
                menuBigliettaio();
                break;

            case PROIEZIONISTA:
                menuProiezionista();
                break;

            default:
                System.out.println("Ruolo non valido");
                utenteLoggato = null;
        }
    }

    // Avvio del menu
    public void avvia() {

        boolean esegui = true;

        while (esegui) {

            mostraTitolo();

            if (utenteLoggato == null) {

                //MENU NON LOGGATO
                System.out.println("\nPrego selezionare l'opzione che volete eseguire:");
                System.out.println("1. Login");
                System.out.println("2. Registrarsi");
                System.out.println("3. Palinsesto attuale");
                System.out.println("4. Chiudere il programma");

                System.out.print("\nScelta: ");

                String scelta = scanner.nextLine();

                switch (scelta) {

                    case "1":
                        login();
                        break;

                    case "2":
                        registrazione();
                        break;

                    case "3":
                        mostraProiezioni();
                        break;

                    case "4":
                        System.out.println("\nArrivederci e torna quando vuoi su CINEMAX!");
                        esegui = false;
                        break;

                    default:
                        System.out.println("\nOpzione non valida.");
                }

            } else {

                // MENU IN BASE AL RUOLO
                menuRuolo();
            }

            pausa();
        }

        scanner.close();
    }

    // Titolo iniziale
    private void mostraTitolo() {

        pulisciSchermo();

        System.out.println("""
                
 ██████╗██╗███╗   ██╗███████╗███╗   ███╗ █████╗ ██╗  ██╗
██╔════╝██║████╗  ██║██╔════╝████╗ ████║██╔══██╗╚██╗██╔╝
██║     ██║██╔██╗ ██║█████╗  ██╔████╔██║███████║ ╚███╔╝
██║     ██║██║╚██╗██║██╔══╝  ██║╚██╔╝██║██╔══██║ ██╔██╗
╚██████╗██║██║ ╚████║███████╗██║ ╚═╝ ██║██║  ██║██╔╝ ██╗
 ╚═════╝╚═╝╚═╝  ╚═══╝╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝

        """);

        System.out.println("============== BENVENUTI IN CINEMAX ==============");
    }

    // Login 
    private void login() {

        System.out.println("\n--- LOGIN ---");

    }

    // Registrazione 
    private void registrazione() {
    	
        System.out.println("\n--- REGISTRAZIONE ---");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Data di nascita (YYYY-MM-DD): ");
        String dataNascitaStr = scanner.nextLine();
        LocalDate dataNascita = LocalDate.parse(dataNascitaStr);

        System.out.print("Domicilio: ");
        String domicilio = scanner.nextLine();

        // CHIAMATA AL METODO
        boolean successo = gestioneUtenti.registraCliente(nome, cognome, username, password, dataNascita,domicilio
        );

        if (successo) {

            System.out.println("\nRegistrazione effettuata con successo!");
            System.out.println("Ora puoi effettuare il login per accedere alle nuove opzioni.");

        } else {

            System.out.println("\nRegistrazione fallita (username già esistente o errore).");
        }


    }

    // Proiezioni 
    private void mostraProiezioni() {

        System.out.println("\n--- PALINSESTO ATTUALE ---");

        System.out.println("\n--- PROIEZIONI DISPONIBILI ---\n");

        List<Film> filmList = new ArrayList<>(gestioneFilm.getTuttiFilm());

        if (filmList.isEmpty()) {

            System.out.println("Nessuna proiezione disponibile al momento.");
            return;
        }

        for (Film f : filmList) {

            System.out.println("-----------------------------------");
            System.out.println("Titolo: " + f.getTitolo());
            System.out.println("Genere: " + f.getGenere());
            System.out.println("Regista: " + f.getRegista());
            System.out.println("Data: " + f.getData());
            System.out.println("Ora: " + f.getOra());
            System.out.println("Durata: " + f.getDurata() + " min");
            System.out.println("Costo: " + f.getCostoBiglietto());
            System.out.println("Posti disponibili: " + f.getPostiSala());
        }

        System.out.println("-----------------------------------");
    }
    
    //Menu del CLIENTE
    
    private void menuCliente() {

        System.out.println("\n--- MENU CLIENTE ---");

        System.out.println("1. Crea prenotazione");
        System.out.println("2. Modifica prenotazione");
        System.out.println("3. Visualizza prenotazioni");
        System.out.println("4. Elimina prenotazione");
        System.out.println("5. Visualizza proiezioni");
        System.out.println("6. Logout");
        System.out.println("7. Chiudi programma");

        System.out.print("\nScelta: ");

        String scelta = scanner.nextLine();

        switch (scelta) {

            case "1":
                System.out.println("Inserisci titolo film:");
                String titolo = scanner.nextLine();

                System.out.println("Inserisci numero biglietti:");
                int n = Integer.parseInt(scanner.nextLine());

                // ricerca film (semplificata: da palinsesto)
                List<Film> filmList = gestioneFilm.getTuttiFilm();

                Film scelto = null;

                for (Film f : filmList) {
                    if (f.getTitolo().equalsIgnoreCase(titolo)) {
                        scelto = f;
                        break;
                    }
                }

                if (scelto == null) {
                    System.out.println("Film non trovato.");
                    break;
                }

                boolean ok = gestionePrenotazioni.creaPrenotazione(
                        utenteLoggato,
                        scelto,
                        n
                );

                if (ok) {
                    System.out.println("Prenotazione creata con successo!");
                }

                break;
            case "2":
            	   System.out.println("Inserisci codice prenotazione:");
            	    String codiceMod = scanner.nextLine();

            	    System.out.println("Nuovo numero biglietti:");
            	    int nuovi = Integer.parseInt(scanner.nextLine());

            	    boolean mod = gestionePrenotazioni.modificaPrenotazione(codiceMod, nuovi);

            	    if (mod) {
            	        System.out.println("Prenotazione modificata con successo!");
            	    }

            	    break;
            case "3":
                List<Prenotazione> miePrenotazioni =
                gestionePrenotazioni.cercaPerUtente(utenteLoggato.getUsername());

                if (miePrenotazioni.isEmpty()) {
                	System.out.println("Nessuna prenotazione trovata.");
                } else {
                	for (Prenotazione p : miePrenotazioni) {
                		gestionePrenotazioni.visualizzaPrenotazione(p);
                	}
                }

                	break;
            case "4":
            	 System.out.println("Inserisci codice prenotazione:");
            	    String codiceElim = scanner.nextLine();

            	    boolean elim = gestionePrenotazioni.eliminaPrenotazione(codiceElim);

            	    if (elim) {
            	        System.out.println("Prenotazione eliminata con successo!");
            	    }

            	    break;
            case "5":
                
            	 mostraProiezioni();
            	 
                break;

            case "6":
                gestioneUtenti.logout();
                utenteLoggato = null;
                System.out.println("Logout effettuato con successo.");
                break;

            case "7":
                System.out.println("Arrivederci!");
                System.exit(0);
                break;

            default:
                System.out.println("Opzione non valida.");
        }
    }
    
    //Menu del BIGLIETTAIO
    
    private void menuBigliettaio() {

        System.out.println("\n--- MENU BIGLIETTAIO ---");

        System.out.println("1. Visualizza prenotazioni di oggi");
        System.out.println("2. Cerca prenotazione");
        System.out.println("3. Logout");
        System.out.println("4. Chiudi programma");

        System.out.print("\nScelta: ");

        String scelta = scanner.nextLine();

        switch (scelta) {

            case "1":
                List<Prenotazione> oggi = gestionePrenotazioni.prenotazioniOggi();

                if (oggi.isEmpty()) {
                    System.out.println("Nessuna prenotazione per oggi.");
                } else {
                    for (Prenotazione p : oggi) {
                        gestionePrenotazioni.visualizzaPrenotazione(p);
                    }
                }

                break;
            case "2":
                System.out.println("Cerca per:");
                System.out.println("1. Codice");
                System.out.println("2. Utente");
                System.out.println("3. Film");

                String tipo = scanner.nextLine();

                System.out.print("Inserisci valore: ");
                String valore = scanner.nextLine();

                List<Prenotazione> risultati =
                        gestionePrenotazioni.cercaPrenotazione(Integer.parseInt(tipo), valore);

                if (risultati.isEmpty()) {
                    System.out.println("Nessuna prenotazione trovata.");
                } else {
                    for (Prenotazione p : risultati) {
                        gestionePrenotazioni.visualizzaPrenotazione(p);
                    }
                }

                break;

            case "3":
                gestioneUtenti.logout();
                utenteLoggato = null;
                System.out.println("Logout effettuato con successo.");
                break;

            case "4":
                System.out.println("Arrivederci!");
                System.exit(0);
                break;

            default:
                System.out.println("Opzione non valida.");
        }
    }
    
    //Menu del PROIEZIONISTA
    
    private void menuProiezionista() {

        System.out.println("\n--- MENU PROIEZIONISTA ---");

        System.out.println("1. Aggiungi proiezione");
        System.out.println("2. Modifica proiezione");
        System.out.println("3. Elimina proiezione");
        System.out.println("4. Cerca proiezione");
        System.out.println("5. Logout");
        System.out.println("6. Chiudi programma");

        System.out.print("\nScelta: ");

        String scelta = scanner.nextLine();

        switch (scelta) {

            case "1":
            	System.out.print("Titolo: ");
            	String titolo = scanner.nextLine();

            	System.out.print("Genere: ");
            	String genere = scanner.nextLine();

            	System.out.print("Regista: ");
            	String regista = scanner.nextLine();

            	System.out.print("Anno: ");
            	int anno = Integer.parseInt(scanner.nextLine());

            	System.out.print("Durata (minuti): ");
            	int durata = Integer.parseInt(scanner.nextLine());

            	System.out.print("Età minima: ");
            	int eta = Integer.parseInt(scanner.nextLine());

            	System.out.print("Data (AAAA-MM-GG): ");
            	LocalDate data = LocalDate.parse(scanner.nextLine());

            	System.out.print("Ora (HH:MM): ");
            	LocalTime ora = LocalTime.parse(scanner.nextLine());

            	System.out.print("Costo biglietto: ");
            	double costo = Double.parseDouble(scanner.nextLine());

            	boolean ok = gestioneFilm.creaProiezione(
            	        data,
            	        ora,
            	        titolo,
            	        genere,
            	        regista,
            	        anno,
            	        durata,
            	        eta,
            	        costo
            	);

            	if (ok) {
            	    System.out.println("Film aggiunto con successo!");
            	} else {
            	    System.out.println("Errore nella creazione della proiezione.");
            	}

            	    break;
            case "2":
            	System.out.print("Titolo film: ");
            	String titoloM = scanner.nextLine();

            	System.out.print("Data attuale (AAAA-MM-GG): ");
            	LocalDate dataVecchia = LocalDate.parse(scanner.nextLine());

            	System.out.print("Ora attuale (HH:MM): ");
            	LocalTime oraVecchia = LocalTime.parse(scanner.nextLine());

            	Film f = gestioneFilm.trovaProiezione(titoloM, dataVecchia, oraVecchia);

            	if (f == null) {
            	    System.out.println("Proiezione non trovata!");
            	    break;
            	}

            	System.out.print("Nuova data (AAAA-MM-GG): ");
            	LocalDate nuovaData = LocalDate.parse(scanner.nextLine());

            	System.out.print("Nuova ora (HH:MM): ");
            	LocalTime nuovaOra = LocalTime.parse(scanner.nextLine());

            	// modifica data
            	gestioneFilm.modificaProiezione(f, 1, nuovaData);

            	// modifica ora
            	gestioneFilm.modificaProiezione(f, 2, nuovaOra);

                  break;
            case "3":
            	System.out.print("Titolo film: ");
            	String titoloE = scanner.nextLine();

            	System.out.print("Data (AAAA-MM-GG): ");
            	LocalDate dataE = LocalDate.parse(scanner.nextLine());

            	System.out.print("Ora (HH:MM): ");
            	LocalTime oraE = LocalTime.parse(scanner.nextLine());

            	Film filmDaEliminare = gestioneFilm.trovaProiezione(titoloE, dataE, oraE);

            	if (filmDaEliminare == null) {
            	    System.out.println("Proiezione non trovata!");
            	    break;
            	}

            	boolean eliminato = gestioneFilm.eliminaProiezione(filmDaEliminare);

            	if (eliminato) {
            	    System.out.println("Proiezione eliminata!");
            	} else {
            	    System.out.println("Impossibile eliminare la proiezione.");
            	}

                break;
            case "4":
            	System.out.print("Titolo film: ");
            	String titoloC = scanner.nextLine();

            	System.out.print("Data (AAAA-MM-GG): ");
            	LocalDate dataC = LocalDate.parse(scanner.nextLine());

            	System.out.print("Ora (HH:MM): ");
            	LocalTime oraC = LocalTime.parse(scanner.nextLine());

            	Film film = gestioneFilm.trovaProiezione(titoloC, dataC, oraC);

            	if (film == null) {
            	    System.out.println("Proiezione non trovata!");
            	} else {
            	    gestioneFilm.visualizzaProiezione(film);
            	}
                if (film != null) {

                    System.out.println(
                            "\n===== PROIEZIONE TROVATA ====="
                    );

                    System.out.println(film);

                } else {

                    System.out.println( "Proiezione non trovata.");
                }

                break;

            case "5":
                gestioneUtenti.logout();
                utenteLoggato = null;
                System.out.println("Logout effettuato con successo.");
                break;

            case "6":
                System.out.println("Arrivederci!");
                System.exit(0);
                break;

            default:
                System.out.println("Opzione non valida.");
        }
    }

    // Pausa terminale
    private void pausa() {

        System.out.println("\nPremere INVIO per continuare...");
        scanner.nextLine();
    }

    // Simula pulizia schermo
    private void pulisciSchermo() {

        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }
}
