package cinemax;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

/**
 * @author Paolo (Sviluppatore principale)
 * @author Elisa (Documentazione Javadoc)
 * Matricole: 766917 (VA), 765763 (VA)
 */

public class Menu {

    // Scanner per leggere input utente
    private final Scanner scanner;

    private Utente utenteLoggato = null;

    /**
     * Costruisce il menu principale collegando i moduli di gestione del sistema.
     *
     * @param gestioneFilm gestore delle proiezioni
     * @param gestioneUtenti gestore degli utenti
     * @param gestionePrenotazioni gestore delle prenotazioni
     */
    public Menu() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Indirizza l'utente loggato verso il sottomenu appropriato in base al suo ruolo.
     */
    //Router menu in base al ruolo
    private void menuRuolo() {

        switch (utenteLoggato.getRuolo()) {

            case CLIENTE -> menuCliente();

            case BIGLIETTAIO -> menuBigliettaio();

            case PROIEZIONISTA -> menuProiezionista();

            default -> {
                System.out.println("Ruolo non valido");
                utenteLoggato = null;
            }
        }
    }

     /**
    * Avvia il menu principale. Mostra il titolo, gestisce le opzioni
    * per gli utenti non autenticati (login, registrazione, palinsesto) e
    * indirizza gli utenti connessi ai rispettivi menu in base al ruolo.
    */

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

                        case "1" -> login();

                        case "2" -> registrazione();

                        case "3" -> mostraProiezioni();

                        case "4" -> esegui = false;

                        default -> System.out.println("Opzione non valida.");
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
    /**
     * Stampa il banner iniziale del programma.
     */
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

    /**
     * Gestisce il login dell'utente.
     */
    // Login 
    private void login() {

    	    System.out.println("\n--- LOGIN ---");

    	    System.out.print("Username: ");
    	    String username = scanner.nextLine();

    	    System.out.print("Password: ");
    	    String password = scanner.nextLine();

    	    boolean successo =
    	            GestioneUtenti.loginHashed(username, password);

    	    if (successo) {

    	        utenteLoggato =
    	                GestioneUtenti.getUtenteCorrente();

    	        System.out.println(
    	                "Benvenuto " +
    	                utenteLoggato.getUsername()
    	        );

    	        // entra subito nel menu corretto
    	        menuRuolo();

    	    } else {

    	        System.out.println("Login fallito.");
    	   
    	}

    }

    /**
     * Gestisce la registrazione di un nuovo utente cliente.
     */
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
        boolean successo = GestioneUtenti.registraCliente(nome, cognome, username, password, dataNascita,domicilio
        );

        if (successo) {

            System.out.println("\nRegistrazione effettuata con successo!");
            System.out.println("Ora puoi effettuare il login per accedere alle nuove opzioni.");

        } else {

            System.out.println("\nRegistrazione fallita");
        }


    }

    /**
     * Visualizza il palinsesto delle proiezioni con opzione di ricerca per titolo,
     * genere, data e costo.
     */
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
            System.out.println("""
                               
                               Filtro data: 
                               0 Nessuno
                               1 Dopo
                               2 Prima
                               3 Tra""");

            int sceltaData =
                    Integer.parseInt(sc.nextLine());

            GestioneFilm.Criterio critData = null;

            LocalDate data1 = null;
            LocalDate data2 = null;

            if (sceltaData > 0) {

                System.out.print("Data (YYYY-MM-DD): ");
                data1 =
                        LocalDate.parse(
                                sc.nextLine()
                        );

                switch (sceltaData) {

                    case 1 -> critData =
                                GestioneFilm.Criterio.DOPO_DI;

                    case 2 -> critData =
                                GestioneFilm.Criterio.PRIMA_DI;

                    case 3 -> {
                        critData =
                                GestioneFilm.Criterio.COMPRESO_TRA;

                        System.out.print(
                                "Seconda data: "
                        );

                        data2 =
                                LocalDate.parse(
                                        sc.nextLine()
                                );
                    }
                }
            }

            // COSTO
            System.out.println("""
                    
                    Filtro costo:
                    0 Nessuno
                    1 Maggiore di
                    2 Minore di
                    3 Tra""");

				 int sceltaCosto = Integer.parseInt(sc.nextLine());
				
				 GestioneFilm.Criterio critCosto = null;
				
				 double costo1 = 0.0;
				 double costo2 = 0.0;
				
				 if (sceltaCosto > 0) {
				
				     switch (sceltaCosto) {
				
				         case 1 -> {
				             critCosto = GestioneFilm.Criterio.DOPO_DI;
				             System.out.print("Costo minimo: ");
				             costo1 = Double.parseDouble(sc.nextLine());
				         }
				
				         case 2 -> {
				             critCosto = GestioneFilm.Criterio.PRIMA_DI;
				             System.out.print("Costo massimo: ");
				             costo1 = Double.parseDouble(sc.nextLine());
				         }
				
				         case 3 -> {
				             critCosto = GestioneFilm.Criterio.COMPRESO_TRA;
				
				             System.out.print("Costo minimo: ");
				             costo1 = Double.parseDouble(sc.nextLine());
				
				             System.out.print("Costo massimo: ");
				             costo2 = Double.parseDouble(sc.nextLine());
				         }
				
				         default -> System.out.println("Scelta non valida");
				     }
				 }

            filmList =
                    GestioneFilm.cercaFilm(
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
                    GestioneFilm.getTuttiFilm();
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

            GestioneFilm.visualizzaProiezione(f);
        }
    }
    
    /**
     * Visualizza il menu dedicato ai clienti con opzioni per prenotare,
     * modificare, visualizzare e cancellare prenotazioni.
     */
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

            	System.out.println("Inserisci data (YYYY-MM-DD):");
            	LocalDate data = LocalDate.parse(scanner.nextLine());

            	System.out.println("Inserisci ora (HH:mm):");
            	LocalTime ora = LocalTime.parse(scanner.nextLine());

            	System.out.println("Inserisci numero biglietti:");
            	int n = Integer.parseInt(scanner.nextLine());

            	Film scelto = GestioneFilm.trovaProiezione(data, ora);

            	if (scelto == null) {
            	    System.out.println(" Proiezione non trovata.");
            	    break;
            	}

            	if (scelto.getPostiSala() < n) {
            	    System.out.println(" Posti insufficienti disponibili.");
            	    break;
            	}
            	boolean ok = GestionePrenotazioni.creaPrenotazione(
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

            	    boolean mod = GestionePrenotazioni.modificaPrenotazione(codiceMod, nuovi);

            	    if (mod) {
            	        System.out.println("Prenotazione modificata con successo!");
            	    }

            	    break;
            case "3":
                List<Prenotazione> miePrenotazioni =
                GestionePrenotazioni.cercaPerUtente(utenteLoggato.getUsername());

                if (miePrenotazioni.isEmpty()) {
                	System.out.println("Nessuna prenotazione trovata.");
                } else {
                	for (Prenotazione p : miePrenotazioni) {
                		GestionePrenotazioni.visualizzaPrenotazione(p);
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

            	    boolean elim = GestionePrenotazioni.eliminaPrenotazione(codiceElim);

            	    if (elim) {
            	        System.out.println("Prenotazione eliminata con successo!");
            	    }

            	    break;
            case "5":
                
            	 mostraProiezioni();
            	 
                break;

            case "6":
                GestioneUtenti.logout();
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
    
    /**
     * Visualizza il menu dedicato ai bigliettai con opzioni per visualizzare
     * e cercare prenotazioni.
     */
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
                List<Prenotazione> oggi = GestionePrenotazioni.prenotazioniOggi();

                if (oggi.isEmpty()) {
                    System.out.println("Nessuna prenotazione per oggi.");
                } else {
                    for (Prenotazione p : oggi) {
                        GestionePrenotazioni.visualizzaPrenotazione(p);
                    }
                }

                break;
            case "2":

            	System.out.println("""
            			Scegli tipo ricerca:
            			1 - Codice prenotazione
            			2 - Username
            			3 - Titolo film
            			4 - Intervallo date
            			""");

            			int tipo = Integer.parseInt(scanner.nextLine());

            			List<Prenotazione> risultati = new ArrayList<>();

            			switch (tipo) {

            			    case 1:
            			        System.out.print("Inserisci codice prenotazione: ");
            			        String codice = scanner.nextLine();

            			        risultati = GestionePrenotazioni.cercaPrenotazione(1, codice);
            			        break;

            			    case 2:
            			        System.out.print("Inserisci username: ");
            			        String username = scanner.nextLine();
            			        if(GestioneUtenti.utenteEsiste(username)) {
            			        	risultati = GestionePrenotazioni.cercaPrenotazione(2, username);
            			        }else {
            			        	System.out.println("Utente non trovato");
            			        }
            			        break;

            			    case 3:
            			        System.out.print("Inserisci titolo film: ");
            			        String titolo = scanner.nextLine();

            			        risultati = GestionePrenotazioni.cercaPrenotazione(3, titolo);
            			        break;

            			    case 4:
            			        System.out.print("Data inizio (YYYY-MM-DD): ");
            			        String inizio = scanner.nextLine();

            			        System.out.print("Data fine (YYYY-MM-DD): ");
            			        String fine = scanner.nextLine();
            			        
            			        if(!LocalDate.parse(fine).isBefore(LocalDate.parse(inizio))) {
            			        	risultati = GestionePrenotazioni.cercaPrenotazione(4, inizio, fine);
            			        }else {
            			        	System.out.println("Date non valide");
            			        }
            			        break;

            			    default:
            			        System.out.println("Scelta non valida");
            			        break;
            			}

            			// Visualizzazione risultati
            			if (!risultati.isEmpty()) {

            			    for (Prenotazione p : risultati) {
            			        GestionePrenotazioni.visualizzaPrenotazione(p);
            			    }

            			} else {
            			    System.out.println("Nessuna prenotazione trovata.");
            			}
            			
            			break;

            case "3":
                GestioneUtenti.logout();
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
    
    /**
     * Visualizza il menu dedicato ai proiezionisti con opzioni per aggiungere,
     * modificare e eliminare proiezioni.
     */
    //Menu del PROIEZIONISTA
    
    private void menuProiezionista() {

        System.out.println("\n--- MENU PROIEZIONISTA ---");

        System.out.println("1. Aggiungi proiezione");
        System.out.println("2. Modifica proiezione");
        System.out.println("3. Elimina proiezione");
        System.out.println("4. Visualizza proiezioni");
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

            	System.out.print("Data (YYYY-MM-DD): ");
            	LocalDate data = LocalDate.parse(scanner.nextLine());

            	System.out.print("Ora (HH:mm): ");
            	LocalTime ora = LocalTime.parse(scanner.nextLine());

            	System.out.print("Costo biglietto: ");
            	double costo = Double.parseDouble(scanner.nextLine());

            	boolean ok = GestioneFilm.creaProiezione(
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

            	System.out.print("Data attuale (YYYY-MM-DD): ");
            	LocalDate dataVecchia = LocalDate.parse(scanner.nextLine());

            	System.out.print("Ora attuale (HH:mm): ");
            	LocalTime oraVecchia = LocalTime.parse(scanner.nextLine());

            	Film f = GestioneFilm.trovaProiezione(
            	        dataVecchia,
            	        oraVecchia
            	);

            	if (f == null) {
            	    System.out.println("Proiezione non trovata!");
            	    break;
            	}

            	// scelta modifica
            	System.out.println("\nCosa vuoi modificare?");
            	System.out.println("1. Modifica data");
            	System.out.println("2. Modifica ora");
            	String sceltaModifica = scanner.nextLine();

            	switch (sceltaModifica) {

            	    case "1":

            	        System.out.print("Nuova data (YYYY-MM-DD): ");
            	        LocalDate nuovaData = LocalDate.parse(scanner.nextLine());

            	        GestioneFilm.modificaProiezione(f,1,nuovaData);

            	        break;

            	    case "2":

            	        System.out.print("Nuova ora (HH:mm): ");
            	        LocalTime nuovaOra =LocalTime.parse(scanner.nextLine());

            	        GestioneFilm.modificaProiezione(f,2,nuovaOra);

            	        break;
            	        
            	    default:
            	        System.out.println("Scelta non valida.");
            	}

                  break;
            case "3":
            	System.out.print("Titolo film: ");
            	String titoloE = scanner.nextLine();

            	System.out.print("Data (YYYY-MM-DD): ");
            	LocalDate dataE = LocalDate.parse(scanner.nextLine());

            	System.out.print("Ora (HH:mm): ");
            	LocalTime oraE = LocalTime.parse(scanner.nextLine());

            	Film filmDaEliminare = GestioneFilm.trovaProiezione(dataE, oraE);
            	
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

            	boolean eliminato = GestioneFilm.eliminaProiezione(filmDaEliminare);

            	if (eliminato) {
            	    System.out.println("Proiezione eliminata!");
            	}
                break;
                
            case "4":
             
            	 mostraProiezioni();
            	 
                 break;

            case "5":
                GestioneUtenti.logout();
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

    /**
     * Mette in pausa l'esecuzione in attesa che l'utente prema INVIO.
     */
    // Pausa terminale
    private void pausa() {

        System.out.println("\nPremere INVIO per continuare...");
        scanner.nextLine();
    }

    /**
     * Simula la pulizia dello schermo stampando più righe vuote.
     */
    // Simula pulizia schermo
    private void pulisciSchermo() {

        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }
}
