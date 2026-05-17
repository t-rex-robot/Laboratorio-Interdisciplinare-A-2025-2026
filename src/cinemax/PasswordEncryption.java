package cinemax;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class PasswordEncryption {

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());

        // Converte i byte in una stringa esadecimale
        StringBuilder sb = new StringBuilder();
        for (byte b : hash)
            sb.append(String.format("%02x", b));  //formato esadecimale a due caratteri

        return sb.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {

        GestioneUtenti mappaU = new GestioneUtenti();
		mappaU.caricaUtentiDaFile();
		mappaU.stampaMappa();

        // Test di verifica password usando hash
        Scanner sc = new Scanner(System.in);
        String username = "inserisci_username"; // sostituisci con uno username esistente nel file utenti.csv
        System.out.println("Inserisci username:");
        username = sc.nextLine();

        String enteredPassword = "inserisci_password"; // sostituisci con la password da verificare
        System.out.println("Inserisci password:");
        enteredPassword = sc.nextLine();

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

        
    }
}

