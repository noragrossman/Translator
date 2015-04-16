package com.translator;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import com.translator.partsOfSpeech.*;

public class Translator {
	public static HashMap<String, ArrayList<Word>> english = new HashMap<String, ArrayList<Word>>();
	public static HashMap<String, ArrayList<Word>> spanish = new HashMap<String, ArrayList<Word>>();

	public Translator() {
		english = readIn("WEB-INF/ResourceFiles/english.ser");
		spanish = readIn("WEB-INF/ResourceFiles/spanish.ser");
		System.out.println("Translator constructed");
	}

	public static HashMap<String, ArrayList<Word>> readIn(String fileName) {
		// deserialize the hashmap from file
		HashMap<String, ArrayList<Word>> dict = null;
		try {
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			dict = (HashMap<String, ArrayList<Word>>) ois.readObject();
			ois.close();
			fis.close();

		} catch (IOException e) {
			System.out.println("IOException when deserializing dictionary for " + fileName + ": " + e);
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found for " + fileName + ": " + c);
		}

		return dict;
	}

	public ArrayList<Word> translate(String original) {
		ArrayList<Word> translations = null;
		System.out.println("About to try to translate " + original);
		if (english.containsKey(original)) {
			System.out.println("Found english word...");
			translations = english.get(original);
		} else {
			System.out.println("Didn't find english word");
		}
		System.out.println("Returning translation...");
		return translations;
	}
}