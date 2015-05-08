package com.translator;

/*
*	@author Nora Grossman
*/

import java.util.*;
import java.io.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Conjugator {

	/*
		PERSON
		0 - 1st singular
		1 - 2nd singular
		2 - 3rd singular
		3 - 1st plural
		4 - 2nd plural
		5 - 3rd plural

		TENSE
		(Indicative)
		0- PRESENT
		1- PRETERIT
		2- IMPERFECT
		3- FUTURE
		4- CONDITIONAL
		5- PRESENT PERFECT
		6- PLUPERFECT
		7- FUTURE PERFECT
		8- CONDITIONAL PERFECT
		9- PRESENT PROGRESSIVE
		10- PAST PROGRESSIVE

		(Subjunctive)
		11- PRESENT SUBJUNCTIVE
		12- IMPERFECT SUBJUNCTIVE
		13- PRESENT PERFECT SUBJUNCTIVE
		14- PLUPERFECT SUBJUNCTIVE

		(Imperative)
		15- PRESENT IMPERATIVE

		16 - PAST PARTICIPLE
		17 - GERUND
	*/

	private static final String[][] AR_ENDINGS = {{"o", "as", "a", "amos", "áis", "an"},
												{"é", "aste", "o", "amos", "asteis", "aron"},
												{"aba", "abas", "aba", "ábamos", "abáis", "aban"},
												{"é", "ás", "á", "amos", "áis", "án"},
												{"ía", "ías", "ía", "íamos", "iáis", "ían"},
												{"ara", "aras", "ara", "áramos", "aráis", "aran"},
												{"-", "a", "e", "-", "ad", "en"}};

	private static final String[][] ER_ENDINGS = {{"o", "es", "e", "emos", "éis", "en"},
												{"í", "iste", "ió", "imos", "isteis", "ieron"},
												{"ía", "ías", "ía", "íamos", "iáis", "ían"},
												{"é", "as", "a", "amos", "áis", "án"},
												{"ía", "ías", "ía", "íamos", "iáis", "ían"},
												{"iera", "ieras", "iera", "iéramos", "ieráis", "ieran"},
												{"-", "e", "a", "-", "id", "an"}};

	private HashMap<String, String> verbs;
    private static ArrayList<String> irregularVerbs;

	public Conjugator() {
		System.out.println("You created a conjugator!");
	}

	public String conjugateVerb(String lemma, int tense, int person) {
        // Check if the verb is irregular and in a non-compound tense
        // (Compound tense = present perfect, 'ha corrido')
        if (isIrregular(lemma) && (tense < 5 || tense == 11 || tense == 12)) {
            return lookupIrregularVerb(lemma, tense, person);
        }
        
		String root = getStem(lemma);

		boolean isAr = isAr(lemma);
		String[][] sameEnd;
		String[][] diffEnd;

		if (isAr) {
			sameEnd = AR_ENDINGS;
			diffEnd = ER_ENDINGS;
		} else {
			sameEnd = ER_ENDINGS;
			diffEnd = AR_ENDINGS;
		}

		String stem = getStem(lemma);

		String conj = null;

		switch(tense) {
			case 0: 
				conj = stem + sameEnd[tense][person];
				break;
			case 1: 
				conj = stem + sameEnd[tense][person];
				break;
			case 2:
				conj = stem + sameEnd[tense][person];
				break;
			case 3:
				conj = stem + sameEnd[tense][person];
				break;
			case 4:
				conj = stem + sameEnd[tense][person];
				break;
			case 5:
				conj = conjugateVerb("haber", 0, person) + " " + getPP(lemma, person);
				break;
			case 6:
				conj = conjugateVerb("haber", 2, person) + " " + getPP(lemma, person);
				break;
			case 7:
				conj = conjugateVerb("haber", 3, person) + " " + getPP(lemma, person);
				break;
			case 8:
				conj = conjugateVerb("haber", 4, person) + " " + getPP(lemma, person);
				break;
			case 9:
				conj = conjugateVerb("estar", 0, person) + " " + getGerund(lemma);
				break;
			case 10:
				conj = conjugateVerb("estar", 0, person) + " " + getGerund(lemma);
				break;
			case 11:
				conj = stem + diffEnd[0][person];
				break;
			case 12:
				conj = stem + sameEnd[tense][person];
				break;
			case 13:
				conj = conjugateVerb("haber", 11, person) + " " + getPP(lemma, person);
				break;
			case 14:
				conj = conjugateVerb("haber", 12, person) + " " + getPP(lemma, person);
				break;
			case 15: 
				if (!(person == 0) && !(person == 3)) {
					conj = stem + sameEnd[6][person];
				}
				break;
			case 16:
				conj = getPP(lemma, person);
				break;
			case 17:
				conj = getGerund(lemma);
				break;
		}

        return conj;
	}
    

	public boolean isAr(String lemma) {
		int len = lemma.length();
		String end = lemma.substring(len-2, len);
		if (end.compareTo("ar") == 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getStem(String lemma) {
		return lemma.substring(0, lemma.length()-2);
	}

	public String getPP(String lemma, int person) {
		// Check if the verb is irregular
        if (isIrregular(lemma)) {
            return lookupIrregularVerb(lemma, 16, 0);
        }

        // If it's not irregular..
		if (isAr(lemma)) {
			return getStem(lemma) + "ado";
		} else {
			return getStem(lemma) + "ido";
		}
	}

	public String getGerund(String lemma) {
		// Check if the verb is irregular
        if (isIrregular(lemma)) {
            return lookupIrregularVerb(lemma, 17, 0);
        }

        // If it's not irregular...
		if (isAr(lemma)) {
			return getStem(lemma) + "ando";
		} else {
			return getStem(lemma) + "iendo";
		}
	}

	public boolean isIrregular(String lemma) {
		// Here we need to query the Datastore to see if the lemma exists as the 
		// 'lemma' property of any SpanConj entity. If it does, the verb is irregular

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    	Filter lemmaFilter = new FilterPredicate("lemma", FilterOperator.EQUAL, lemma);
		Query query = new Query("SpanConj").setFilter(lemmaFilter);

		List<Entity> existingEntries = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));

		if (existingEntries != null) {
			if (existingEntries.size() > 0) {
				return true;
			}
		}

		return false;
	}
	
    public String lookupIrregularVerb(String lemma, int tense, int person) {

    	// TODO: We don't need the SpanConj entities to be so complicated. Simplify the schema.

        char plurality;

        char charPerson;
        if (person > 2) {
        	person -= 2;
        	charPerson = intToChar(person);
            plurality = 'P';
        } else {
        	person++;
        	charPerson = intToChar(person);
            plurality = 'S';
        }

        // deal with mode
        char mode;
        if (tense < 11) {
        	mode = 'I'; 
        } else if (tense < 15) {
        	mode = 'S';
        } else if (tense == 16) {
        	mode = 'P';
        } else if (tense == 17) {
        	mode = 'G';
        } else {
        	mode = 'M';
        }

        // deal with tense
        char charTense;
        if (tense == 0 || tense == 11) {
        	charTense = 'P';
        } else if (tense == 2 || tense == 12) {
        	charTense = 'I';
        } else if (tense == 3) {
        	charTense = 'F';
        } else if (tense == 4) {
        	charTense = 'C';
        } else if (tense == 1) {
        	charTense = 'S';
        } else if (tense == 16 || tense == 17) {
        	charTense = '0';
    	} else {
        	System.out.println("not an applicable tense");
        	return "not an applicable tense";
        }

        // deal with gender
        char gender;
        if (tense == 16) {
        	gender = 'M';
        } else {
        	gender = '0';
        }
        
        // Here we need to query datastore again, to find a SpanConj entity with all of the beloww

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Filter lemmaF = new FilterPredicate("lemma", FilterOperator.EQUAL, lemma);
		Filter modeF = new FilterPredicate("mode", FilterOperator.EQUAL, mode + "");
		Filter tenseF = new FilterPredicate("tense", FilterOperator.EQUAL, charTense + "");
		Filter personF = new FilterPredicate("person", FilterOperator.EQUAL, person + "");
		Filter pluralityF = new FilterPredicate("plurality", FilterOperator.EQUAL, plurality + "");
		Filter genderF = new FilterPredicate("gender", FilterOperator.EQUAL, gender + "");

		Filter wordFilter = CompositeFilterOperator.and(lemmaF, modeF, tenseF, personF, pluralityF, genderF);
		
		Query query = new Query("SpanConj").setFilter(wordFilter);

		List<Entity> wordInfo = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));

		if (wordInfo != null && wordInfo.size() > 0) {
			Entity e = wordInfo.get(0);
			return (String)e.getProperty("conj");
		}

        return null;
    }

    public static char intToChar(int i) {
    	char c;
    	switch (i) {
    		case 0: c = '0';
    			break;
    		case 1: c = '1';
    			break;
    		case 2: c = '2';
    			break;
    		case 3: c = '3';
    			break;
    		default: c = 'x';
    	}
    	return c;

    }
}