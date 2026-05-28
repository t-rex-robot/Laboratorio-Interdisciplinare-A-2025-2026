package cinemax;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


/**
 * Fornisce utilità per generare hash SHA-256 di password in chiaro.
 * Usa SHA-256 per ottenere una rappresentazione esadecimale sicura della password.
 * Si tratta di hashing unidirezionale, non di crittografia reversibile.
 *
 * @see java.security.MessageDigest
 */
public class PasswordHasher {

    /**
     * Costruttore privato per impedire l'instanziazione della classe di utilità.
     */
    private PasswordHasher() {
    }

    /**
     * Calcola l'hash SHA-256 della password in chiaro.
     *
     * @param password la password da hashare
     * @return l'hash esadecimale della password
     * @throws NoSuchAlgorithmException se l'algoritmo SHA-256 non è disponibile
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());

        // Converte i byte in una stringa esadecimale
        StringBuilder sb = new StringBuilder();
        for (byte b : hash)
            sb.append(String.format("%02x", b));  //formato esadecimale a due caratteri

        return sb.toString();
    }

    /**
     * Punto di ingresso per testare la generazione dell'hash e la verifica delle password.
     *
     * @param args argomenti della linea di comando (non usati)
     * @throws NoSuchAlgorithmException se l'algoritmo SHA-256 non è disponibile
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {

        GestioneUtenti mappaU = new GestioneUtenti();
		mappaU.caricaUtentiDaFile();
		mappaU.stampaMappa();

        // Test di verifica password usando hash
        Scanner sc = new Scanner(System.in);

        System.out.println("Inserisci username:"); //inserisci uno username esistente nel file utenti.csv
        String username = sc.nextLine();

        System.out.println("Inserisci password:"); //inserisci la password da verificare
        String enteredPassword = sc.nextLine();

        System.out.println("\nVerifica password per utente: " + username);
        boolean passwordOK = mappaU.loginHashed(username, enteredPassword);
        System.out.println(passwordOK ? "La password corrisponde al valore memorizzato." : "La password NON corrisponde.");


        // Esempio diretto di hashing di una password
        String realPassword = "password123";
        String storedHash = hashPassword(realPassword);
        System.out.println("Stored hash:" + storedHash);

        // login di esempio
        String enteredPassword2 = "mypassword";
        String enteredHash = hashPassword(enteredPassword2);
        System.out.println("\nEntered hash:" + enteredHash);
     

        if (enteredHash.equals(storedHash)) {
            System.out.println("\nLogin avvenuto con successo!");
        } else {
            System.out.println("\nPassword errata!");
        }

        sc.close();
    }
}

