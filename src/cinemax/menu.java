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

            try {

                mostraTitolo();

                if (utenteLoggato == null) {

                    System.out.println("\n1. Login");
                    System.out.println("2. Registrarsi");
                    System.out.println("3. Palinsesto");
                    System.out.println("4. Esci");

                    System.out.print("Scelta: ");
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
                            esegui = false;
                            break;

                        default:
                            System.out.println("Opzione non valida.");
                    }

                } else {
                    menuRuolo();
                }

            } catch (Exception e) {

                System.out.println("\n Errore: " + e.getMessage());
 
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

        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- PALINSESTO ATTUALE ---");

        System.out.print("Vuoi applicare una ricerca? (s/n): ");

        List<Film> filmList;

        if (sc.nextLine().equalsIgnoreCase("s")) {

            System.out.print("Titolo (INVIO per saltare): ");
            String titolo = sc.nextLine();

            System.out.print("Genere (INVIO per saltare): ");
            String genere = sc.nextLine();

            // DATE
            System.out.println(
                "\nFiltro data: "
                + "\n0 Nessuno"
                + "\n1 Dopo"
                + "\n2 Prima"
                + "\n3 Tra"
            );

            int sceltaData =
                    Integer.parseInt(sc.nextLine());

            GestioneFilm.Criterio critData = null;

            LocalDate data1 = null;
            LocalDate data2 = null;

            if (sceltaData > 0) {

                System.out.print("Data (yyyy-MM-dd): ");
                data1 =
                        LocalDate.parse(
                                sc.nextLine()
                        );

                switch (sceltaData) {

                    case 1:
                        critData =
                                GestioneFilm.Criterio.DOPO_DI;
                        break;

                    case 2:
                        critData =
                                GestioneFilm.Criterio.PRIMA_DI;
                        break;

                    case 3:
                        critData =
                                GestioneFilm.Criterio.COMPRESO_TRA;

                        System.out.print(
                                "Seconda data: "
                        );

                        data2 =
                                LocalDate.parse(
                                        sc.nextLine()
                                );

                        break;
                }
            }

            // COSTO
            System.out.println(
                "\nFiltro costo:"
                + "\n0 Nessuno"
                + "\n1 Maggiore di"
                + "\n2 Minore di"
                + "\n3 Tra"
            );

            int sceltaCosto =
                    Integer.parseInt(
                            sc.nextLine()
                    );

            GestioneFilm.Criterio critCosto = null;

            Double costo1 = null;
            Double costo2 = null;

            if (sceltaCosto > 0) {

                System.out.print("Costo: ");

                costo1 =
                        Double.parseDouble(
                                sc.nextLine()
                        );

                switch (sceltaCosto) {

                    case 1:
                        critCosto =
                                GestioneFilm.Criterio.DOPO_DI;
                        break;

                    case 2:
                        critCosto =
                                GestioneFilm.Criterio.PRIMA_DI;
                        break;

                    case 3:

                        critCosto =
                                GestioneFilm.Criterio.COMPRESO_TRA;

                        System.out.print(
                                "Secondo costo: "
                        );

                        costo2 =
                                Double.parseDouble(
                                        sc.nextLine()
                                );

                        break;
                }
            }

            filmList =
                    gestioneFilm.cercaFilm(
                            titolo,
                            genere,
                            critData,
                            data1,
                            data2,
                            critCosto,
                            costo1,
                            costo2
                    );

        } else {

            filmList =
                    gestioneFilm.getTuttiFilm();
        }

        if (filmList.isEmpty()) {

            System.out.println(
                    "Nessun film trovato."
            );

            return;
        }

        for (Film f : filmList) {

            System.out.println(
                    "\n---------------------"
            );

            gestioneFilm.visualizzaProiezione(f);
        }
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
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nScelta: ");

        String scelta = scanner.nextLine();

        switch (scelta) {

            case "1":
            	System.out.println("=== PRENOTAZIONE BIGLIETTI ===");


            	// INPUT UTENTE
            	System.out.println("Inserisci titolo film:");
            	String titolo = scanner.nextLine();

            	System.out.println("Inserisci data (yyyy-MM-dd):");
            	LocalDate data = LocalDate.parse(scanner.nextLine());

            	System.out.println("Inserisci ora (HH:mm):");
            	LocalTime ora = LocalTime.parse(scanner.nextLine());

            	System.out.println("Inserisci numero biglietti:");
            	int n = Integer.parseInt(scanner.nextLine());

            	Film scelto = gestioneFilm.trovaProiezione(titolo, data, ora);

            	if (scelto == null) {
            	    System.out.println(" Proiezione non trovata.");
            	    break;
            	}

            	if (scelto.getPostiSala() < n) {
            	    System.out.println(" Posti insufficienti disponibili.");
            	    break;
            	}
            	boolean ok = gestionePrenotazioni.creaPrenotazione(
            	        utenteLoggato,
            	        scelto,
            	        n
            	);

            	if (ok) {
            	    System.out.println("Prenotazione effettuata con successo!");
            	} else {
            	    System.out.println("Errore nella prenotazione.");
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
            	    
            	    System.out.println("Sei sicuro di voler eliminare la prenotazione? (s/n)");
            	    String conferma = scanner.nextLine();

            	    if (!conferma.equalsIgnoreCase("s")) {
            	        System.out.println("Operazione annullata.");
            	        break;
            	    }

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
        System.out.println("4. Logout");
        System.out.println("5. Chiudi programma");

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
            	
            	System.out.println("Sei sicuro di voler eliminare la proiezione? (s/n)");
            	String conferma = scanner.nextLine();

            	if (!conferma.equalsIgnoreCase("s")) {
            	    System.out.println("Operazione annullata.");
            	    break;
            	}

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
                gestioneUtenti.logout();
                utenteLoggato = null;
                System.out.println("Logout effettuato con successo.");
                break;

            case "5":
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
