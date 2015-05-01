// Upload CSV Servlet //

 package com.translator;

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
*/

public class CSVParser {
	private BufferedReader br1;
	private BufferedReader br2;
	private BufferedReader br3;

	public CSVParser() {
		System.out.println("created CSV parser");
		this.br1 = read("WEB-INF/ResourceFiles/english.csv");
		this.br2 = read("WEB-INF/ResourceFiles/spanish.csv");
		this.br3 = read("WEB-INF/ResourceFiles/translations.csv");
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

	/*public int parseEnglish(int start, int stop) {
		// Read in English file
		int numPut = 0;
		BufferedReader br1 = read("WEB-INF/ResourceFiles/english.csv");
		if (br1 != null) {
			numPut = parseEnglishCsv(br1, start, stop);
		} else {
			System.out.println("Problem reading English CSV");
		}

		return numPut;
	}

	public int parseSpanish(int start, int stop) {
		// Read in Spanish file
		int numPut = 0;
		BufferedReader br2 = read("WEB-INF/ResourceFiles/spanish.csv");
		if (br2 != null) {
			numPut = parseSpanishCsv(br2, start, stop);
		} else {
			System.out.println("Problem reading Spanish CSV");
		}

		return numPut;
	}

	public int parseTrans(int start, int stop) {
		// Read in Translations file
		int numPut = 0;
		BufferedReader br3 = read("WEB-INF/ResourceFiles/translations.csv");
		if (br3 != null) {
			numPut = parseTranslationsCsv(br3, start, stop);
		} else {
			System.out.println("Problem reading Translations CSV");
		}

		return numPut;

	} */
}