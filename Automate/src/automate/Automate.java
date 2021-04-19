package automate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Automate {
	static public final int MAX = 26; // On acte qu'un etat ne peut pas avoir plus de 26 transition
	// Les attributs
	private char[] alphabet; // L'alphabet ne changera pas
	private ArrayList<Etat> etats;
	private int nbrEtats;

	private ArrayList<ArrayList<Integer>> etatInit;
	private ArrayList<ArrayList<Integer>> etatTerm;

	// Constructeur par defaut
	public Automate() {
	}

	public Automate(final int nbrEtat) {
		etats = new ArrayList<Etat>();
		this.nbrEtats = nbrEtat;
	}

	//Affichage automate
	public void afficherAutomate() {
		System.out.println("Automate");
		System.out.print("  - Alphabet { ");
		for(char alpha : alphabet) {
			System.out.print(alpha+ " ");
		}
		System.out.println("}");
		System.out.print("  - Etats Q = { ");
		for(int i=0; i<nbrEtats; i++) {  System.out.print(etats.get(i).getNomEtat() + " "); }
		System.out.println("}");
		System.out.print("  - Etats I = { ");
		affichertab(etatInit);
		System.out.println("}");
		System.out.print("  - Etats T = { ");
		affichertab(etatTerm);
		System.out.println("}");
		tableTransitionAutomate();
	}

	// Fonction pour afficher un tableau d'entier
	public void affichertab(final ArrayList<ArrayList<Integer>> tab) {
		if (tab != null) {
			for(int i=0; i<tab.size(); i++) {
				for (int j = 0; j < tab.get(i).size(); j++) {
					if (j > 0) {
						System.out.print(".");
					}
					System.out.print(tab.get(i));
				}
				System.out.print(" ");
			}
		}
	}

	//Fonction pour afficher une tableau de transition
	public void tableTransitionAutomate() {
		if (etats != null) {
			
			// Affichage entete
			System.out.println("\n TABLE DE TRANSITION ");
			System.out.print("     |");
			
			// Affichage de l'alphabet que l'automate reconnait
			for (char c : alphabet) {
				for(int i=0; i<nbrEtats-1; i++) { System.out.print(" ");}
				System.out.print(c);
				for(int i=0; i<nbrEtats; i++) { System.out.print(" ");}
				System.out.print("|");
			}
			System.out.println();
			ligneSepration();

			int espace = nbrEtats*2; 
			
			//Affichage corps : T/NT Nometat | etatlettre a | ... 
			for(int i=0; i<nbrEtats; i++) {
				// Affichage de E pour entr�e et S pour sortie
				boolean init = false, term = false;
				for (int k = 0; k < etatInit.size(); k++) {
					if (comparaisonEtat(etatInit.get(k), etats.get(i).getNomEtat())) {
						System.out.print("E");
						init= true; 
					}
				}
				for (int k = 0; k < etatTerm.size(); k++) {
					if (comparaisonEtat(etatTerm.get(k), etats.get(i).getNomEtat())) {
						System.out.print("S");
						term = true; 
					}
				}
				if (init == false && term == false) {
					System.out.print("  ");
				}else if(init == false || term == false) {
					System.out.print(" ");
				} 
				if(etats.get(i).getNomEtat().get(0) == -1) {
					System.out.print(" P |");
				}else {
					System.out.print(" ");
					etats.get(i).affichageNomEtat();
					System.out.print(" |");
				}
				
				// Affichage des transitions
				for(int j=0; j<alphabet.length; j++) {
					for(int x=0; x < etats.get(i).getNbrTrans(); x++) {
						if (etats.get(i).getLettre(x) == alphabet[j]) {
							System.out.print(" ");
							if(etats.get(i).getEtatFinal(x).contains(-1)) {
								System.out.print("P");
							}else {
								etats.get(i).afficherEtatSortie(x);
							} 							
							espace -= 2;
						}
					}
					for(int k = 0; k < espace; k++) {
						System.out.print(" ");
					}
					System.out.print("|");
					espace = nbrEtats*2;
				}
				System.out.println();
				ligneSepration();
			}
		}
	}
	
	public boolean comparaisonEtat(final ArrayList<Integer> etatAutomate, final ArrayList<Integer> etatComparer) {
		if(etatComparer != null && etatAutomate != null) {
			if(etatAutomate.size() == etatComparer.size()) {
				for (int i = 0; i < etatAutomate.size(); i++) {
					if(etatAutomate.get(i) != etatComparer.get(i)) {
						return false; 
					}
				}
			}else {
				return false; 
			}
			return true; 
		}
		return false; 
	}
	
	
	// Fonction pour afficher une separation dans la table de transition
	public void ligneSepration() {
		System.out.print("-----|");
		for (int i = 0; i < alphabet.length; i++) {
			for(int j = 0; j < nbrEtats; j++) {
				System.out.print("--");
			}
			System.out.print("|");
		}
		System.out.println();
	}

	public void lire_automate_fichier(String NomFichier) {

		// ouverture du fichier
		File fichier = new File(NomFichier);
		// On regarde si le fichier n'existe pas
		if (!fichier.exists()) {
			System.out.println("Le fichier n'existe pas, veuillez reessayer !");	
		} 
		// Si il existe on peut travailler
		else {
			try {
				BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(fichier), "UTF-8"));

				// Lecture de l'Alphabet
				String Lines = reader.readLine();
				alphabet = new char[Integer.parseInt(Lines)];
				char lettre = 'a';
				for (int i = 0; i < Integer.parseInt(Lines); i++) {
					alphabet[i] = lettre++;
					//System.out.println(alphabet[i]); // A SUPPRIMER
				}

				// Nombre d'etats total et creation des etats
				Lines = reader.readLine();
				nbrEtats = Integer.parseInt(Lines);
				if (this.etats == null) {
					this.etats = new ArrayList<Etat>(); 
				}
				// Nombre d'etat initiaux
				char nbr = (char) reader.read();
				int nombreEtatInit = Character.getNumericValue(nbr);
				etatInit = new ArrayList<>(); 
				for (int i = 0; i < nombreEtatInit; i++) {
					nbr = (char) reader.read(); // A cause du caractere espace
					nbr = (char) reader.read();
					etatInit.add(new ArrayList<>()); 
					etatInit.get(i).add(Character.getNumericValue(nbr));
					//System.out.println("etat = " + etatInit[i] + " ");// A SUPPRIMER
				}

				// Nombre d'etat finaux
				Lines = reader.readLine();
				nbr = (char) reader.read();
				nombreEtatInit = Character.getNumericValue(nbr);
				etatTerm = new ArrayList<>(); 
				for (int i = 0; i < nombreEtatInit; i++) {
					nbr = (char) reader.read(); // A cause du caractere espace
					nbr = (char) reader.read();
					etatTerm.add(new ArrayList<>()); 
					etatTerm.get(i).add(Character.getNumericValue(nbr));
					//System.out.println("etat = " + etatTerm[i] + " ");// A SUPPRIMER
				}
				Lines = reader.readLine(); // On finit la ligne

				// Nombre de transition
				Lines = reader.readLine();
				int nbrTransition = Integer.parseInt(Lines);

				//Transition tabTransition = new Transition
				// Les transitions
				ArrayList<Transition> tableauTransitionEtati = new ArrayList<Transition>();
				int j = 0, etatActuelle = 0; 
				int numeroEtatEntree = 0, numeroEtatSortie = 0; 
				char translettre;
				for (int i = 0; i < nbrTransition; i++) {
					//Recuperation du l'etat de depart
					nbr =(char) reader.read();
					numeroEtatEntree = Character.getNumericValue(nbr);
					//Recuperation de la lettre
					nbr =(char) reader.read();
					translettre = nbr;
					//Recuperation de l'etat final
					nbr = (char) reader.read();
					numeroEtatSortie = Character.getNumericValue(nbr);

					Transition t = new Transition(numeroEtatEntree, translettre, numeroEtatSortie);
					// Si l'etat de depart change c'est que nous avons parcouru toutes les transitions

					if (numeroEtatEntree != etatActuelle) { 
						ArrayList<Transition> copieArrayList = new ArrayList<>(); 
						copieArrayList.addAll(tableauTransitionEtati); 
						etats.add(new Etat(numeroEtatEntree-1,copieArrayList, j)); 
						tableauTransitionEtati.clear();
						etatActuelle++; // On passe a un etat N+1
						j = 0;// On repart � la premier case du tableau 
					}
					
					tableauTransitionEtati.add(t);
					j++;
					//Fin de la ligne
					Lines = reader.readLine();
				}
				// Pour le dernier etats on ajout ces transitions
				etats.add(numeroEtatEntree, new Etat(numeroEtatEntree, tableauTransitionEtati, j));
				
				if(nbrTransition < nbrEtats) {
					for (int i = nbrTransition; i <= nbrEtats ; i++) {
						etats.add(new Etat(i-1));					
					}
				}
				
				reader.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean est_un_automate_deterministe() {
		return true; 
	}
	public boolean est_un_automate_asynchrone() {
		for(int i=0 ; i < etats.size() ; i++) {
			for(int j=0 ; j < etats.get(i).getNbrTrans() ; j++) {
				if(etats.get(i).getTransition().get(j) != null) { //if there is transition
					if(etats.get(i).getTransition().get(j).getLettre() == '*') {
						etats.get(i).getTransition().get(j).afficherTransition();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// Completion d'un Automate Finis Complet et Deterministe
	public void completion() {
		// Verification que l'automate est bien synchrone et deterministe pour pouvoir le completer
		if(this.est_un_automate_deterministe() && !this.est_un_automate_asynchrone()) {
			if (!this.est_un_automate_complet()) {
				for (int i = 0; i < etats.size() ; i++) {
					// Si le nombre de lettre = nombre de transition alors l'etat est complet sinon 
					if(etats.get(i).getNbrTrans() != alphabet.length) {
						for (int j = 0; j < alphabet.length; j++) {
							// Si le nombre de Transition est sup�rieur alors 
							if (etats.get(i).getNbrTrans() > j) {
								if(etats.get(i).getLettre(j) != alphabet[j]) {
									etats.get(i).ajoutTransition( i, alphabet[j], -1);
								}
							}else {
								etats.get(i).ajoutTransition(i, alphabet[j], -1);
							}
						}
					}	
				}
				// Ajout de l'�tat poubelle 
				etats.add(new Etat(-1));
				nbrEtats++;
				for (int i = 0; i < alphabet.length; i++) {
					etats.get(nbrEtats-1).ajoutTransition(-1, alphabet[i], -1);
				}
				 
			}
		}
	}
	
	
	//Minimisation d'un automate 
	public void minimisation() {
		// Pour minimiser un automate il doit �tre d�terministe et complet
		if(this.est_un_automate_complet() && this.est_un_automate_deterministe()) {
			System.out.println("\n   - MINIMISATION -");
			
			// 1 S�paration �tat T et NT
			// V�rification si T ou NT n'est pas isol�
			boolean Tisole = false, NTisole = false; 
			if(etatTerm.size() == 1) {
				System.out.println("L'etat T (terminal) est isol�");
				Tisole = true;
			}else if (etatTerm.size() == nbrEtats-1) {
				System.out.println("L'etat NT (non terminal) est isol�");
				NTisole = true; 
			}
			
			// 2 Minimisation 
			if (!NTisole) {
				ArrayList<ArrayList<Integer>> tabMinNT = new ArrayList<>(); 
				for (int i = 0; i < (nbrEtats - etatTerm.size()); i++) {
					tabMinNT.add(new ArrayList<>());
				}
			}else if (!Tisole){
				ArrayList<ArrayList<Integer>> copieEtatT = new ArrayList<>();
				copieEtatT.addAll(etatTerm); 
				ArrayList<ArrayList<Integer>> tabMinT = new ArrayList<>();
				
				// Etat T OU NT
				
				//ArrayList<String> tab = new ArrayList<>();
				for (int i = 0; i < etatTerm.size(); i++) {
					tabMinT.add(new ArrayList<>()); 
					/*tabMinT.get(i).add(etatTerm.get(i).); 
					boolean premAjout = true; 
					for (int j = 0; j < alphabet.length; j++) {
						// Est-ce que l'etat est Terminal 
						if(etats.get(i).getNomEtat() == tabMinT.get(i).get(0)) {
							if(etatTerm.contains(etats.get(i).getEtatFinal(j).get(0))){
								// Si etat T : 1 et pour NT : 0
								tabMinT.get(i).add(1);
								
								if(premAjout) {
									tab.add("1");
									premAjout = false; 
								}else {
									tab.set(i, tab.get(i).concat("1"));
								}
							}else {
								tabMinT.get(i).add(0);
								if(premAjout) {
									tab.add("0");
									premAjout = false; 
								}else {
									tab.set(i, tab.get(i).concat("0"));
								}
							}
						}
					}
					System.out.println(tabMinT + " " + tab);*/
				}
				// V�rification si un �tat s'isole ou non
				for (int i = 0; i < alphabet.length; i++) {
					
				}
				
				
				
			}
			// 3 V�rification si 2 �tats ou plus peuvent se rassembler ou pas  
		}
	}


	public boolean est_un_automate_complet() {
		// Verif synchrone et déterministe
		if(this.est_un_automate_deterministe() && !this.est_un_automate_asynchrone()) {
			boolean bool = true;
			System.out.println("nbr etats : "+ nbrEtats);
			for (int a = 0; a < nbrEtats; a++) {
				if(bool == true) {
					System.out.println("L'Automate n'est pas complet car : ");
				}
				bool = false;
				int b =0;
				System.out.print("	Etat "+etats.get(a).getNomEtat()+" :");
				for (char lettre : alphabet) {
					/*Si b est supérieur au nombres d'états sachant que les états sont triés
					 * ou si l'état ne possède aucune transition */
					if (b > etats.get(a).getNbrTrans() || etats.get(a).getNbrTrans() == 0) {
						System.out.print(" en " + lettre );
					}else if (etats.get(a).getLettre(b) > lettre) {
						System.out.print(" en " +lettre );
					}else if(etats.get(a).getLettre(b) == lettre) { // La lettre est présente
						b++; //On augmente que si on a dépasser la p
					}
				}
				//Saut de ligne entre chaque état
				System.out.println("");
			}
			return bool;
		}
		return false;
	}

	
	
	
	
	
	//Fonction qui permet de voir si un caractère fait parti de l'alphabet d'un automate
	public boolean contains(char[] alpha, char carac) {
		for(int i=0 ; i<alpha.length ; i++) {
			if (alpha[i] == carac) {
				return true;
			}
		}
		return false;
	}

	//Fonction permettant à l'utilisateur de saisir un mot et qui vérifie s'il est valide
	public String lire_mot() {
		//On récupère le mot saisi par l'utilisateur
		Scanner scan = new Scanner(System.in);
		System.out.println("Veuillez saisir un mot ('*' représente le mot vide) :");
		String mot = scan.nextLine();
		System.out.println("Vous avez sisi : " + mot);
		
		//On vérifie si le mot est valide
		boolean test = true;
		do {
			test = true;
			for (int i=0 ; i<mot.length(); i++) {
				char carac = mot.charAt(i);
				if (contains(this.alphabet, carac) == false && carac != '*'){
					test = false;
				}
			}
			if (test == false) {
				System.out.println("Le mot n'est pas valide !");
				System.out.println("Veuillez saisir un mot ('*' représente le mot vide) :");
				mot = scan.nextLine();
				System.out.println("Vous avez saisi : " + mot);
			}
			else if (test == true) {
				System.out.println("Le mot est valide");
			}
		}while(test == false);
		return mot;
	}
	
	
	/*
	//A partir d'une ArrayList, cette fonction permet d'obtenir
	//l'état correspondant dans la liste d'états globale
	public Etat etatCorrespondant(ArrayList<Integer> e){
		Etat etat_final = etats.get(0);
		int compteur = 0;
		for (int i=0 ; i<etats.size(); i++) {
			if(etats.get(i).getNomEtat().size() == e.size()) {
				compteur = 0;
				for (int j=0 ; j<etats.get(i).getNomEtat().size(); j++) {
					if (etats.get(i).getNomEtat().get(j) == e.get(j)) {
						compteur ++;
					}
					if (compteur == 3) {
						etat_final = etats.get(i);
					}
				}
			}
		}
		return etat_final;
	}
		
	public boolean estTerminal(Etat e) {
		int compteur = 0;
		for (int i=0 ; i<etatTerm.size() ; i++) {
			if (etatTerm.get(i).size() == e.getNomEtat().size()){
				compteur = 0;
				for (int j=0 ; j<etatTerm.get(i).size(); j++) {
					if (etatTerm.get(i).get(j) == e.getNomEtat().get(j)) {
						compteur ++;
					}
					if (compteur == 3) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean transitionExistante(Etat e, char symbole) {
		for(int i=0 ; i<e.getTransition().size(); i++) {
			if (e.getTransition().get(i).getLettre() == symbole) {
				return true;
			}
		}
		return false;
	}
	
	public Transition getTransitionExistante(Etat e, char symbole) {
		for(int i=0 ; i<e.getTransition().size(); i++) {
			if (e.getTransition().get(i).getLettre() == symbole) {
				return e.getTransition().get(i);
			}
		}
		return e.getTransition().get(0);
	}
	
	//Reconnaissance d'un mot sur un automate deterministe complet
	public boolean reconnaitre_mot(String mot){
		Etat etat_courant = etatCorrespondant(etatInit.get(0));
		char symbole_courant = mot.charAt(0);
		int compteur = 0;
		Transition trans = etat_courant.getTransition().get(0);
		while (compteur != mot.length()) {
			compteur ++;
			System.out.println(compteur);
			trans = getTransitionExistante(etat_courant, symbole_courant);
			symbole_courant = trans.getLettre();
			etat_courant = etatCorrespondant(trans.getEtatSortie());
			etat_courant.affichageNomEtat();
			if (estTerminal(etat_courant) == true) {
				return true;
			}
			
		}
		if (estTerminal(etat_courant) == true) {
			return true;
		}
		return false;
	}
	*/
}
