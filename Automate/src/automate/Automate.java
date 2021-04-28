package automate;

import java.io.BufferedReader;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections; 
import javax.print.attribute.standard.Copies;


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

	public Automate(final Automate a){
		alphabet = a.alphabet;
		etats = new ArrayList<Etat>(a.etats);
		nbrEtats = a.nbrEtats;
		etatInit = new ArrayList<ArrayList<Integer>>(a.etatInit);
		etatTerm = new ArrayList<ArrayList<Integer>>(a.etatTerm);
	}
	
	public Automate(ArrayList<Etat> etats , int nbrEtats, ArrayList<ArrayList<Integer>> etatInit , ArrayList<ArrayList<Integer>> etatTerm ) {
		this.etats = new ArrayList<Etat>(etats);
		this.nbrEtats = nbrEtats;
		this.etatInit = new ArrayList<ArrayList<Integer>>(etatInit);
		this.etatTerm = new ArrayList<ArrayList<Integer>>(etatTerm);
	}

	public Automate copieDe(Automate a) {
 		Automate copie = new Automate();
 			copie.alphabet = a.alphabet;
 			copie.etats = (ArrayList<Etat>) a.etats.clone();
 			copie.nbrEtats = a.nbrEtats;
 			copie.etatInit = (ArrayList<ArrayList<Integer>>) a.etatInit.clone();
 			copie.etatTerm = (ArrayList<ArrayList<Integer>>) a.etatTerm.clone();
 			return copie;
 	}
	
	
	public ArrayList<Etat> getEtats(){ return etats; } 

	
	//Affichage automate
	public void afficherAutomate() {
		System.out.println("Automate");
		System.out.print("  - Alphabet { ");
		for(char alpha : alphabet) {
			System.out.print(alpha+ " ");
		}
		
		System.out.println("}");
		System.out.print("  - Etats Q = { ");
		for(int i=0; i<nbrEtats ; i++) {  
			System.out.print(etats.get(i).getNomEtat() + " "); 
		} 
		
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
					System.out.print(tab.get(i).get(j));
				}
				System.out.print("  ");
			}
		}
	}

	//Fonction pour afficher une table de transition
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
	
	
	public boolean compareTab(final ArrayList<Integer> tab1, final ArrayList<Integer> tab2 ) {
		if(tab1 != null && tab2 != null) {
			if(tab1.size() == tab2.size()) {
				for ( int i = 0; i< tab1.size(); i++ ) {
					if(tab1.get(i) != tab2.get(i) ) {
						return false ; 
					}
				}	
			}
			else {
				return false ; 
			}
			return true ; 
		}
		return false ; 
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
	
	
	//METHODE POUR VERIFIER SI UN CARACTERE FAIT PARTIE DE L ALPHABET
	public boolean contains(char[] alpha, char carac) {
		for(int i=0 ; i<alpha.length ; i++) {
			if (alpha[i] == carac) {
				return true;
			}
		}
		return false;
	}
	

	//METHODE QUI PERMET A L UTILISATEUR DE SAISIR UN MOT
	//ET QUI VERIFIE SI LE MOT EST VALIDE
	public String lire_mot() {
		
		//On récupère le mot saisi par l'utilisateur
		Scanner scan = new Scanner(System.in);
		System.out.println("Veuillez saisir un mot ('*' représente le mot vide) :");
		String mot = scan.nextLine();
		System.out.println("Vous avez saisi : " + mot);
		
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
	
	
	//METHODE QUI A PARTIR D UNE ARRAYLIST (etatInit et etatTerm) 
	//PERMET D OBTENIR L ETAT CORRESPONDANT DANS LA LISTE DE TOUS LES ETATS
	public Etat etatCorrespondant(ArrayList<Integer> e){
		Etat etat_corres = etats.get(0);
		for(int i=0 ; i<etats.size(); i++) {
			if(comparaisonEtat(e, etats.get(i).getNomEtat())){
				etat_corres = etats.get(i);
			}
		}
		return etat_corres;
	}
	
	
	//METHODE QUI VERIFIE SI UN ETAT EST TERMINAL
	public boolean estTerminal(Etat e) {
		for(int i=0 ; i<etatTerm.size(); i++) {
			if(comparaisonEtat(etatTerm.get(i), e.getNomEtat())){
				return true;
			}
		}
		return false;
	}
	
	
	//METHODE QUI A PARTIR D UN ETAT DONNE ET D UNE LETTRE 
	//DIT S IL EXISTE UNE TRANSITION PASSANT PAR CETTE LETTRE ET ARRIVANT A UN NOUVEL ETAT
	/*
	public boolean transitionExistante(Etat e, char symbole) {
		for(int i=0 ; i<e.getTransition().size(); i++) {
			if (e.getTransition().get(i).getLettre() == symbole) {
				return true;
			}
		}
		return false;
	}
	*/
	
	
	//METHODE QUI A PARTIR D UN ETAT DONNE ET D UNE LETTRE
	//RETOURNE LA TRANSITION PARTANT DE CETTE ETAT ET PASSANT PAR CETTE LETTRE
	public Transition getTransitionExistante(Etat e, char symbole) {
		Transition trans = e.getTransition().get(0);
		for(int i=0 ; i<e.getTransition().size(); i++) {
			if (e.getTransition().get(i).getLettre() == symbole) {
				trans = e.getTransition().get(i);
			}
		}
		return trans;
	}
	
	//METHODE QUI DETERMINE SI UN MOT EST RECONNU OU NON PAR UN AUTOMMATE DETERMINISTE COMPLET
	public boolean reconnaitre_mot(String mot){
		int compteur = 1;
		Etat etat_courant = etatCorrespondant(etatInit.get(0));
		char symbole_courant = mot.charAt(0);
		Transition trans = etat_courant.getTransition().get(0);
		
		//On parcourt l'automate déterministe complet en prenant les lettres du mot une à une 
		while (compteur != mot.length()) {
			trans = getTransitionExistante(etat_courant, symbole_courant);
			etat_courant = etatCorrespondant(trans.getEtatSortie());
			symbole_courant = mot.charAt(compteur);
			compteur ++;
		}
		trans = getTransitionExistante(etat_courant, symbole_courant);
		etat_courant = etatCorrespondant(trans.getEtatSortie());
		
		//On vérifie si l'état final sur lequel on est arrivé est termianl ou non
		//Si oui, le mot est reconnu
		//Sinon, le mot n'est pas reconnu
		if (estTerminal(etat_courant)) {
			return true;
		}
		return false;
	}
	


	public void fusion_entree(){
		if (etatInit.size() > 1) {
			System.out.println("\nFUSION DES ENTREES\n") ; 
			
			int i=0;
			while(etatInit.size()>1){
				etats.get(etatInit.get(i).get(0)).fusion(etats.get(etatInit.get(i+1).get(0))); 
				etatInit.get(i).addAll(etatInit.get(i+1));   //1+2->1,2
				etats.remove(etatInit.get(i+1).get(0).intValue());
				nbrEtats--;
				etatInit.remove(i+1);
				triTransitions(etats.get(etatInit.get(i).get(0)));
			}
		System.out.println("REUSSI " ) ;
		}
		
	}
	

	//savoir si un états doit être remplacer par sa fermeture epsilon
	public Boolean test_fermeture_epsilon(Etat e1){ 
		for (Etat e : etats) {
			for (Transition t : e.getTransition()) {
				if((t.getEtatSortie().equals(e1.getNomEtat()) && t.getLettre() != '*' && e1.contient_epsilon()) || etatInit.contains(e1.getNomEtat()) ){ //on doit remplacer par sa fermeture epsilon
					return true;
				}
			}
		}
		return false;
	}


	public void remove_etat(Etat e){
		ArrayList<Integer> etat = e.getNomEtat();
		if(etatInit.contains(etat)){
			etatInit.remove(etat);
		}
		if (etatTerm.contains(etat)) {
			etatTerm.remove(etat);
		}
		etats.remove(e);
		nbrEtats--;
	}
	
	

	public Etat etat_a_fusioner(Etat e){ //etat à fusionner avec e
		for (Transition t : e.getTransition()) {
			if(t.getLettre() == '*'){
				return etats.get(t.getEtatSortie().get(0));
			}
			
		}
		return null;
	}

	public Etat fermeture(Etat e) { //permet d'éviter de faire sa propre copie de l'automate
		Etat copie = new Etat(e);
		while (copie.contient_epsilon()) { //remplacer par sa transition epsilon tant qu'il y a epsilon
			copie.fusion(etat_a_fusioner(copie));
			return copie;
		}
		return e;
	}

	
	
	public void elimination_epsilon(){
		for (int i = 0; i < nbrEtats; i++) {
			if (test_fermeture_epsilon(etats.get(i))) { //remplacer_fermeture_epsilon  --> test_fermeture_epsilon ??   la premiere methode n'existe pas 
				etats.set(i, fermeture(etats.get(i)));
			}
			else{
				remove_etat(etats.get(i));
			}
		}
	}
	

	public void determinisation_et_completion_asynchrone(){
		//si plusieurs entree -> fusion des entrees
		fusion_entree();
		elimination_epsilon();
		//determinisation_et_completion_synchrone();
		//completion();
	}	
	
	public boolean est_un_automate_deterministe() {  //Vérifier si l’automate synchrone AF est déterministe ou non. Le résultat du test est affiché.
		
		if(this.est_un_automate_asynchrone()) {
			System.out.println("L'automate n'est pas deterministe car il est asynchrone"); 
			return false; 
		}
		else if (this.etatInit.size()!=1) {
			System.out.println("L'automate contient " + this.etatInit.size() + " etats initiaux"); 
			System.out.println("L'automate n'est pas deterministe car il contient plus d'une entree")  ;
			return false ;
			
		}
		else {
			for(int i=0 ; i<this.etats.size();i++){
				//System.out.println("TEST ETAT = " + this.etats.get(i).getNomEtat()); 
				//System.out.println("TEST TRANSITION  = " +  this.etats.get(i).getNbrTrans()) ; 
					
				for(int j=0 ; j <this.etats.get(i).getNbrTrans() ; j++ ) {
					Transition trans1 =  this.etats.get(i).getTransition().get(j);
					//System.out.println("\nTEST TRANS1 = " + trans1 + "\n") ;
					
						
					for(int k=j+1 ; k <this.etats.get(i).getNbrTrans()    ; k++ ) {
						Transition trans2 =  this.etats.get(i).getTransition().get(k);
						//System.out.println("TEST TRANS2 = " + trans2 ) ; 
							
						if(trans1.getEtatDepart()==trans2.getEtatDepart() || trans1.getLettre() == trans2.getLettre() ) {
							System.out.println("L'automate n'est pas deterministe car deux transitions ont le meme etat entree ET la meme lettre :  " + trans1 + trans2) ;  
							return false ; 
						}
					}
				}
			}
		}
		
		System.out.println("L'automate est deterministe") ; 
		return true ;
		
		
		
	}
	
	
	//trie les transitions dans un etat
	//ne les classe pas 
	public void triTransitions(Etat e ) {      
		if (e.getNomEtat().size() > 1) {
			//suppression des redondances 
			for (int k=0 ; k < e.getTransition().size(); k++ ) {
				for (int l=k+1 ; l <e.getTransition().size(); l++ ) {
					
					if(e.getTransition().get(k).getLettre()== e.getTransition().get(l).getLettre() && compareTab(e.getTransition().get(k).getEtatSortie(), e.getTransition().get(l).getEtatSortie())) {
						e.removeTransition(e.getTransition().get(l));    
						e.setnbrTrans(e.getNbrTrans()-1) ;
					}
				}
			}
			
			//etat de depart ->  nomEtat
			int nombreT = e.getTransition().size() ; 
			for( int i = 0 ; i< nombreT ; i++) { 
				Transition t = new Transition(e.getNomEtat() , e.getTransition().get(i).getLettre(), e.getTransition().get(i).getEtatSortie() ) ;
				e.ajoutTransition(t.getEtatDepart(), t.getLettre(), t.getEtatSortie()); 
				
			}
			for( int j = 0 ; j< nombreT ; j++) { 
				e.removeTransition(e.getTransition().get(0));
				e.setnbrTrans(e.getNbrTrans() - 1 );
			}
		}
	}
	
	
	public boolean compareTransition(final Transition t1, final Transition t2) {
		if(compareTab(t1.getEtatDepart(),t2.getEtatDepart()) == false || compareTab(t1.getEtatSortie(), t2.getEtatSortie())==false  || t1.getLettre()!=t2.getLettre() ) {
			return false ; 
		}
		else {
			return true ; 
		}
	}
	
	
	public void affichertabEtat(ArrayList<Etat> tab) {
		for (Etat e : tab) {
			System.out.println(e) ; 
		}
	}
	
	
	public void fusion_entree2 () {
		System.out.println("\n------------------------------ FUSION 2 \n") ; 
		if (etatInit.size() > 1) {	
			Etat new_etatI = new Etat() ; 
			
			for(int i =0 ; i< etatInit.size() ; i ++ ) {
				boolean test = false ; 
				int e=0  ;
				while ( test == false  && e< etats.size() ) {   
					if(etats.get(e).getNomEtat().size() == 1) {
						if(etats.get(e).getNomEtat().get(0) == etatInit.get(i).get(0)) {
							Etat etat_temp = new Etat(etats.get(e)) ; 
							new_etatI.fusion(etat_temp);
							etats.remove(e) ; 
							nbrEtats -- ; 
							test = true ; 
						}	
					}
					e = e + 1  ; 					 
				}
			}
			triTransitions(new_etatI); 
			etats.add(new_etatI) ; 
			nbrEtats ++ ; 
			etatInit.clear();
			etatInit.add(new_etatI.getNomEtat()) ; 	
		}
	}
	
	
 	public Automate determinisation() {
		if(this.est_un_automate_deterministe()==false) {
			System.out.println("\n\nDETERMINISATION \n") ; 
			 
			Automate automateInit = new Automate(this) ;       //on copie l'automate initiale  
			
			
			System.out.println("\n--------------- AFFICHER AUTOMATE INITIAL--------------\n") ; 
			this.afficherAutomate();  
			this.fusion_entree2() ; 
			
			System.out.println("\n--------------- AFFICHER AUTOMATE INITIAL APRES FUSION--------------\n") ; 
			this.afficherAutomate() ; 
			
			
			
			//liste de tous les nouveaux etat + ajout de l'etat entree 
			ArrayList<Etat> tabEtat = new ArrayList<>() ;     
			//tabEtat.add(this.etats.get(0)) ; 
			
			int b = 0 ; 
			while (!compareTab(this.etats.get(b).getNomEtat(), this.etatInit.get(0)) ) {
				b++ ; 
			}
			tabEtat.add(this.etats.get(b)); 
			
			
			System.out.println("\n--------------- AFFICHER Etat initial --------------\n") ; 
			System.out.println(this.etats.get(b)) ; 
			
			
			
			int i = 0 ; 
			while ( i != tabEtat.size() ) {           // i regarde si on a traite tous les etats de la liste 
				
				//System.out.println("\n ----- ETUDIONS LES TRANSITIONS DE L' " + tabEtat.get(i) ) ;
				
				for ( char a : this.alphabet) {   
					//System.out.println("\nCEST QUOI A " + a) ;
					ArrayList<Integer> nvEtat = new ArrayList<>() ;    //lui c'est que le nom de l'etat
					
					for  ( int j=0 ; j<tabEtat.get(i).getTransition().size(); j++ ){    
						
						if(tabEtat.get(i).getTransition().get(j).getLettre() == a ) {    //on prend toutes les transitions passant par une lettre 
							//System.out.println("TROUVE" + tabEtat.get(i).getTransition().get(j) ) ; 
				
							for(int k=0 ; k<tabEtat.get(i).getTransition().get(j).getEtatSortie().size() ; k++ ) {    //on sauvegarde l'etat de sortie
								nvEtat.add(tabEtat.get(i).getTransition().get(j).getEtatSortie().get(k)) ;
								Collections.sort(nvEtat);		//trier le tableau 
								supp_repetition_tab(nvEtat) ; //on supprime les numeros redondants
							}
						
						} 
						
					}	

					if(nvEtat.size()>=1) {
						//System.out.println("LE NOUVEAU ETAT EST " + nvEtat) ; 
					
						
						//fusion des etats de sortie
						Etat new_etat = new Etat() ; 
						for (int n : nvEtat) {
							//System.out.println("JE CHERCHE L'ETAT " + n + " Dans l'automate initial") ;
							for(int l = 0 ; l< automateInit.nbrEtats ; l++ ) {
								if( n == automateInit.etats.get(l).getNomEtat().get(0)) {  //dans l'automate initiale, les etats sont constitue d'un seul nombre
									Etat etat_int = new Etat(automateInit.etats.get(l)) ;
									//System.out.println("TROUVE ETAT" + automateInit.etats.get(l)) ; 
									new_etat.fusion(etat_int);
								}
							}
						}
						triTransitions(new_etat) ;
						
						if(nvEtat.size()>1) {
							Transition t = new Transition(tabEtat.get(i).getNomEtat(), a, nvEtat) ; 
							tabEtat.get(i).ajoutTransition(t.getEtatDepart() , t.getLettre(), t.getEtatSortie());    //ajoute la nouvelle transition qui fusionne les etatTerm
							for( int j=0 ; j<nvEtat.size(); j++ ) {          //supprime les transitions constituees des etatTerm
								for (int k =0 ; k<tabEtat.get(i).getTransition().size() ; k++ ) {
									if (tabEtat.get(i).getTransition().get(k).getEtatSortie().size() == 1 ) {
										if(nvEtat.get(j) == tabEtat.get(i).getTransition().get(k).getEtatSortie().get(0) && tabEtat.get(i).getTransition().get(k).getLettre() == a ){
											tabEtat.get(i).removeTransition(tabEtat.get(i).getTransition().get(k));
											tabEtat.get(i).setnbrTrans(tabEtat.get(i).getNbrTrans() - 1 );
										}
									}
								}
							}
						}

						//on regarde si le nouveau etat est dans la liste des etats tabEtat, sinon on l'ajoute
						boolean test = false ; 
						for ( Etat e : tabEtat) {
							if (compareTab(nvEtat, e.getNomEtat())) {
								test = true ; 
							}
						}
						if (test == false ) {
							tabEtat.add(new_etat); 
						}
					}
						
				}
				i++ ; 
			}
			
			
			System.out.println("\n------------------------------------------------------------ \n") ; 
			
			
			//On redefinit les etats sorties
			this.etatTerm.clear() ; 
			for(int n = 0 ; n<automateInit.etatTerm.size() ; n ++) {
				int t = automateInit.etatTerm.get(n).get(0) ; 
					for (Etat x : tabEtat ) {
						if (x.getNomEtat().contains(t) && !etatTerm.contains(x.getNomEtat()) ) {
							this.etatTerm.add(x.getNomEtat()) ; 
						}
					}
			}
			this.etats.clear() ; 
			this.etats.addAll(tabEtat) ; 
			this.nbrEtats = tabEtat.size(); 
			
			
			System.out.println("\n------------------------------------------------------------ \n") ; 
			System.out.println("Automate determinise : \n" ) ; 
			
			
			System.out.println("Etat : " + this.etats) ;
			System.out.println("Nb etat : " + this.nbrEtats) ;
			System.out.println("Etat init: " + this.etatInit) ;
			System.out.println("Etat term: " + this.etatTerm) ; 
			
			
			System.out.println("\n-------------------------------AFFICHAGE----------------------------- \n") ; 
			
			this.afficherAutomate() ;
			
			System.out.println("\n------------------------------FIN DETERMINISATION------------------------------ \n") ; 
			
			this.est_un_automate_deterministe() ; 
			
			
			
			return this ; 
		}
		
		else {
			System.out.println("L'automate est deterministe") ; 
			return this ; 
		}

	}
	
	
	
	public void supp_repetition_tab(ArrayList<Integer> tab) {
		if(tab != null && tab.size() >=2 ) {
			for (int i=0 ; i< tab.size(); i++ ) {
				for (int j=i+1; j<tab.size(); j++ ) {
					if(tab.get(i) == tab.get(j)) {
						tab.remove(j) ; 
					}
				}
			}
		}
	}
	
	
}


