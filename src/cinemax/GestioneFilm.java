package cinemax;
import java.time.*;
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

}
