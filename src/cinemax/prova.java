package cinemax;

import java.util.*;
import java.time.*;


public class prova {

	public static void main(String[] args) {
	GestioneUtenti mappaU = new GestioneUtenti();
		
		mappaU.caricaUtentiDaFile();
		
		mappaU.stampaMappa();
		
		mappaU.registraCliente("Luca", "Zanzi", "iznazacul", "zanzu", LocalDate.parse("2004-07-15"), "via Bainsizza Albizzate");
		
		mappaU.stampaMappa();
		
		// TODO Auto-generated method stub
	}

}
