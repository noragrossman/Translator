package com.translator;

 /*
  *	@author Nora Grossman
  */

import java.util.*;
import java.io.*;
import java.util.regex.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/*
	Resource files needed:
		> english.csv
		> spanish.csv
		> translations.csv
		> spanishIrrVerbConjs.csv
		> spanishIrrVerbConjsAux.csv
		> englishIrrVerbConjs.csv
*/

public class CSVParser {
	private BufferedReader br1;
	private BufferedReader br2;
	private BufferedReader br3;
	private BufferedReader br4;
	private BufferedReader br5;
	private BufferedReader br6;

	public CSVParser() {
		System.out.println("created CSV parser");
		this.br1 = read("WEB-INF/ResourceFiles/english.csv");
		this.br2 = read("WEB-INF/ResourceFiles/spanish.csv");
		this.br3 = read("WEB-INF/ResourceFiles/translations.csv");
		this.br4 = read("WEB-INF/ResourceFiles/spanishIrrVerbConjs.csv");
		this.br5 = read("WEB-INF/ResourceFiles/spanishIrrVerbConjsAux.csv");
		this.br6 = read("WEB-INF/ResourceFiles/engIrrVerbConjs.csv");
	}

	public int parseAll() {
		// Read in English file
		int numPut = 0;
		BufferedReader br1 = read("WEB-INF/ResourceFiles/english.csv");
		if (br1 != null) {
			numPut += parseEnglishCsv(br1);
		} else {
			System.out.println("Problem reading English CSV");
		}

		// Read in Spanish file
		BufferedReader br2 = read("WEB-INF/ResourceFiles/spanish.csv");
		if (br2 != null) {
			numPut += parseSpanishCsv(br2);
		} else {
			System.out.println("Problem reading Spanish CSV");
		}

		// Read in Translations file
		BufferedReader br3 = read("WEB-INF/ResourceFiles/translations.csv");
		if (br3 != null) {
			numPut += parseTranslationsCsv(br3);
		} else {
			System.out.println("Problem reading Translations CSV");
		}

		return numPut;
	}

	private static BufferedReader read(String filePath) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(filePath), "UTF8"));
		} catch (FileNotFoundException ex) {
			System.out.println("File not found: " + filePath);
		} catch (UnsupportedEncodingException uee) {
			System.out.println("Unsupported encoding exception: " + uee);
		}
		return in;
	}

	public int parseEnglishCsv() {
		return parseEnglishCsv(this.br1);
	}

	public int parseSpanishCsv() {
		return parseSpanishCsv(this.br2);
	}

	public int parseTranslationsCsv() {
		return parseTranslationsCsv(this.br3);
	}

	public int parseSpanishIrregsCsv() {
		return parseSpanishIrregs(this.br4);
	}

	public int parseSpanishIrregsCsv2() {
		return parseSpanishIrregs(this.br5);
	}

	public int parseEnglishIrregs() {
		return parseEnglishIrregs(this.br6);
	}

	private static int parseEnglishIrregs(BufferedReader in) {
		System.out.println("Parsing English irregular verb conjugations CSV");
		int entitiesPut = 0;
		String entityName = "EngConj";
		try {
			String line = in.readLine();
			while (line != null) {
				String[] values = line.split(",");
				String lemma = values[0];
				String tense = values[1];
				String person = values[2];
				String conj = values[3];

				Entity engConj = new Entity(entityName);
				engConj.setProperty("lemma", lemma);
				engConj.setProperty("tense", tense);
				engConj.setProperty("person", person);
				engConj.setProperty("conj", conj);

				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				datastore.put(engConj);
				entitiesPut++;

				line = in.readLine();
			}

		} catch (IOException e) {
			System.err.println(e);
		} finally {
			System.out.println(entitiesPut + " English irr verb entities put");
			return entitiesPut;
		}
	}

	private static int parseSpanishIrregs(BufferedReader in) {
		System.out.println("Parsing Spanish irregular verb conjugations CSV");
		int entitiesPut = 0;
		String entityName = "SpanConj";
		try {
			String line = in.readLine();
			while (line != null) {
				// Split by commas
				String[] values = line.split(",");

				String conj = values[0];
				String lemma = values[1];
				String mode = values[2];
				String tense = values[3];
				String person = values[4];
				String plurality = values[5];
				String gender = values[6];

				// Create entity
				Entity spanConj = new Entity(entityName);
				spanConj.setProperty("conj", conj);
				spanConj.setProperty("mode", mode);
				spanConj.setProperty("lemma", lemma);
				spanConj.setProperty("tense", tense);
				spanConj.setProperty("person", person);
				spanConj.setProperty("plurality", plurality);
				spanConj.setProperty("gender", gender);

				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				datastore.put(spanConj);
				entitiesPut++;

				line = in.readLine();
			}
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			System.out.println(entitiesPut + " spanish conjugations put");
			return entitiesPut;
		}
	}

	private static int parseEnglishCsv(BufferedReader in) {
		System.out.println("Parsing English CSV...");
		int entitiesPut = 0;
		String entityName = "English";
		int i = 0;
		String last = "";
		try {
			String line = in.readLine();
			while (line != null) {
				// Split by commas
				String[] values = line.split(",");
				String lemma = values[0].trim();
				String pos = values[1].trim();

				// Create entity
				Entity english = new Entity(entityName);
				english.setProperty("lemma", lemma);
				english.setProperty("partOfSpeech", pos);

				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    			datastore.put(english);
    			entitiesPut++;
    			last = lemma;

				line = in.readLine();
				i++;
			}

		} catch (IOException iox) {
			System.out.println(iox);
		} finally {
			System.out.println(entitiesPut + " English entities put");
			System.out.println("Last: " + last);
			return entitiesPut;
		}
	}

	private static int parseSpanishCsv(BufferedReader in) {
		System.out.println("Parsing Spanish CSV...");
		int entitiesPut = 0;
		String entityName = "Spanish";
		int i = 0;
		String last = "";
		try {
			String line = in.readLine();
			while (line != null) {
				// Split by commas
				String[] values = line.split(",");
				String lemma = values[0].trim();
				String pos = values[1].trim();
				String gender = values[2].trim();

				// Create entity
				Entity spanish = new Entity(entityName);
				spanish.setProperty("lemma", lemma);
				spanish.setProperty("partOfSpeech", pos);
				if (gender.compareTo("-") != 0) {
					spanish.setProperty("gender", gender);
				}

				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    			datastore.put(spanish);

    			entitiesPut++;
    			last = lemma;
				
				line = in.readLine();
				i++;
			}

		} catch (IOException iox) {
			System.out.println(iox);
		} finally {
			System.out.println(entitiesPut + " Spanish entities put");
			System.out.println("Last: " + last);
			return entitiesPut;
		}
	}	

	private static int parseTranslationsCsv(BufferedReader in) {
		System.out.println("Parsing Translations CSV...");
		int entitiesPut = 0;
		String entityName = "Translation";
		int i = 0;
		String last = "";
		try {
			String line = in.readLine();
			while (line != null) {
				// Split by commas
				String[] values = line.split(",");
				String english = values[0].trim();
				String pos = values[1].trim();
				String spanish = values[2].trim();

				// Create entity
				Entity translation = new Entity(entityName);
				translation.setProperty("english", english);
				translation.setProperty("partOfSpeech", pos);
				translation.setProperty("spanish", spanish);

				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    			datastore.put(translation);
    			System.out.println("just put in " + english + " - " + spanish);

    			entitiesPut++;
    			last = english + " - " + spanish;
				
				line = in.readLine();
				i++;
			}

		} catch (IOException iox) {
			System.out.println(iox);
		} finally {
			System.out.println(entitiesPut + " translation entities put");
			System.out.println("Last: " + last);
			return entitiesPut;
		}
	}
}