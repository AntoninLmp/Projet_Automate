package automate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

public class Automate {
	static public final int MAX = 26; // On acte qu'un etat ne peut pas avoir plus de 26 transition 
	// Les attributs
	private char[] alphabet;
	private Etat[] etats;
	private int nbrEtats;

	private int[] etatInit;
	private int[] etatTerm;
	private int index;

	// Constructeur par defaut
	public Automate() {
	}

	public Automate(final int nbrEtat) {
		etats = new Etat[nbrEtat];
		this.nbrEtats = nbrEtat;
	}

}
