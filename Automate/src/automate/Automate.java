package automate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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

	//Affichage automate
	public void afficherAutomate() {
		System.out.println("Automate");
		System.out.print("  - Alphabet { ");
		for(char alpha : alphabet) {
			System.out.print(alpha+ " ");
		}
		System.out.println("}");
		System.out.print("  - Etats Q = { ");
		for(int i=0; i<etats.size(); i++) {  
			affichertabSimpleArray(etats.get(i).getNomEtat()); 
			if (i != etats.size()-1) {System.out.print(", ");}
		}
		System.out.println(" }");

		System.out.print("  - Etats I = { ");
		affichertabDoubleArray(etatInit);
		System.out.println(" }");
		System.out.print("  - Etats T = { ");
		affichertabDoubleArray(etatTerm);
		System.out.println(" }");
		tableTransitionAutomate();
	}

	// Fonction pour afficher un tableau d'entier
	public void affichertabDoubleArray(final ArrayList<ArrayList<Integer>> tab) {
		if (tab != null) {
			for(int i=0; i<tab.size(); i++) {
				for (int j = 0; j < tab.get(i).size(); j++) {
					if (j > 0) {
						System.out.print(".");
					}
					if(tab.get(i).get(j) == -1) System.out.print("P");
					else System.out.print(tab.get(i).get(j));
				}
				if (i != tab.size()-1) {System.out.print(", ");}
			}
		}
	}
	public void affichertabSimpleArray(final ArrayList<Integer> tab) {
		if (tab != null) {
			for(int i=0; i<tab.size(); i++) {
				if (i > 0) {
					System.out.print(".");
				}
				if(tab.get(i) == -1) System.out.print("P");
				else System.out.print(tab.get(i));
			}
			System.out.print(" ");
		}
	}

	//Fonction pour afficher une tableau de transition
	public void tableTransitionAutomate() {
		if (etats != null) {
			
			// Affichage entete
			System.out.println("\n TABLE DE TRANSITION ");
			System.out.print("         |");
			
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
				
				// Ajustement
				if (init == false && term == false) {
					if (etats.get(i).getNomEtat().size() > 1) {
						for (int j = 0; j < 9 - (etats.get(i).getNomEtat().size()*2) ; j++) {
							System.out.print(" ");
						}
					}else {
						System.out.print("       ");
					}
				}else if(init == false || term == false) {
					if (etats.get(i).getNomEtat().size() > 1) {
						for (int j = 0; j < 8 - (etats.get(i).getNomEtat().size()*2) ; j++) {
							System.out.print(" ");
						}
					}else {
						System.out.print("      ");
					}
				} 
				if(etats.get(i).getNomEtat().get(0) == -1) {
					System.out.print("P |");
				}else {
					etats.get(i).affichageNomEtat();
					System.out.print(" |");
				}
				
				// Affichage des transitions
				for(int j=0; j<alphabet.length; j++) {
					for(int x=0; x < etats.get(i).getNbrTrans(); x++) {
						if (etats.get(i).getLettre(x) == alphabet[j]) {
							System.out.print(" ");
							etats.get(i).afficherEtatSortie(x);
							if (etats.get(i).getEtatFinal(x).size() > 1) {
								espace -= etats.get(i).getEtatFinal(x).size() *2 ; 
							}
							else {
								espace -= 2; 
							}
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
		
	// Fonction pour afficher une separation dans la table de transition
	public void ligneSepration() {
		System.out.print("---------|");
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
				if (etats.size() != nbrEtats) {
					for (int i = etats.size()+1; i < nbrEtats; i++) {
						etats.add(new Etat(i)); 
					}
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
	public boolean est_un_automate_complet() {
		// Verif synchrone et d�terministe
		if(this.est_un_automate_deterministe() && !this.est_un_automate_asynchrone()) {
			System.out.println("nbr etats : "+ nbrEtats);
			boolean bool = true; 
			for (int a = 0; a < nbrEtats; a++) {
				int b =0;
				if(etats.get(a).getNbrTrans() != alphabet.length) {
					System.out.println("L'Automate n'est pas complet car : ");
					System.out.print("	Etat "+etats.get(a).getNomEtat()+" :");
					for (char lettre : alphabet) {
						/*Si b est sup�rieur au nombres d'�tats sachant que les �tats sont tri�s
						 * ou si l'�tat ne poss�de aucune transition */
						if (b > etats.get(a).getNbrTrans() || etats.get(a).getNbrTrans() == 0) {
							System.out.print(" en " + lettre );
						}else if (etats.get(a).getLettre(b) > lettre) {
							System.out.print(" en " +lettre );
						}else if(etats.get(a).getLettre(b) == lettre) { // La lettre est pr�sente
							b++; //On augmente que si on a d�passer la p
						}
						bool = false; 
					}
					//Saut de ligne entre chaque �tat
					System.out.println("");
				}
			}
			return bool;
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
	
	public boolean comparaisonEtat(final ArrayList<Integer> etatAutomate, final ArrayList<Integer> etatComparer) {
		if(etatComparer != null && etatAutomate != null && etatAutomate.size() == etatComparer.size()) {
			for (int i = 0; i < etatAutomate.size(); i++) {
				if(etatAutomate.get(i) != etatComparer.get(i)) {
					return false; 
				}
			}
			return true; 
		}
		return false; 
	}
	
	// Permet de savoir si un etat est terminal ou pas 
	public boolean estTerminal(final ArrayList<Integer> nomEtat) {
		if (nomEtat != null) {
			for (int k2 = 0; k2 < etatTerm.size(); k2++) {
				if(comparaisonEtat(nomEtat, etatTerm.get(k2))) {
					return true; 
				}
			}
		}
		return false; 
	}
	
	
	//Minimisation d'un automate 
	public void minimisation() {
		
		// Pour minimiser un automate il doit etre deterministe et complet
		if(this.est_un_automate_complet() && this.est_un_automate_deterministe()) {
			System.out.println("\n   - MINIMISATION -");
			// ETAPE 0 : Cr�ation d'une COPIE du tableau d'etats qui pourra �tre modifier sans impacter le tableau etats
			ArrayList<Etat> autoMinimiser = new ArrayList<>(); 
			for (int i = 0; i < etats.size(); i++) {
				autoMinimiser.add(etats.get(i).copie());	
			}
			
			// ETAPE 1 : SEPARER LES ETATS TERMINAUX ET NON TERMINAUX
			for (int j = 0; j < autoMinimiser.size(); j++) {
				boolean estTerm = false; // Etat : T (terminal) OU NT (non terminaux)
				//Boucle pour v�rifier chaque etatsFinal : On cherche les etats qui sont terminaux
				for (int k = 0; k < autoMinimiser.get(j).getNbrTrans(); k++) {
					estTerm = estTerminal(autoMinimiser.get(j).getEtatFinal(k)); 
					// On met un 1 pour T et 0 pour NT
					if(estTerm) {
						autoMinimiser.get(j).setEtatFinal(k, 1);
					}else {
						autoMinimiser.get(j).setEtatFinal(k, 0);
					}
				}
			}
			
			// ETAPE 2: RASSEMBLEMENT DES ETATS SI NECCESSAIRE
			// Boucle pour autant de fois qu'on foit regrouper le tableau
			for (int j = 0; j < autoMinimiser.size(); j++) {
				
				ArrayList<ArrayList<Integer>> tabEtatEtudier = new ArrayList<>();
				// Boucle pour parcourir chaque etat. On compare par rapport aux autres etats
				for (int k = 0; k < autoMinimiser.size() ; k++) {	
					
					// Creation du nouveau Nom et ajout du premoer etat 
					ArrayList<Integer> newNomArrayList = new ArrayList<>();
					for (int a = 0; a < autoMinimiser.get(k).getNomEtat().size(); a++) {newNomArrayList.add(autoMinimiser.get(k).getNomEtat().get(a)); }
					
					boolean identique = false, dejaEtudier = false;
					for (int x = 0; x < tabEtatEtudier.size(); x++) {
						for (int x2 = 0; x2 < autoMinimiser.get(k).getNomEtat().size(); x2++) {
							if (tabEtatEtudier.get(x).contains(autoMinimiser.get(k).getNomEtat().get(x2))) {
								dejaEtudier = true; 
								break; 
							}
						}
					}
					if (!dejaEtudier) {
						// On regroupe les etats identiques 
						boolean isole = true;
						for (int l = k+1; l < autoMinimiser.size(); l++) {
							int nombreEtatIdentique = 0;							
							if(estTerminal(autoMinimiser.get(k).getNomEtat()) == estTerminal(autoMinimiser.get(l).getNomEtat())) {
								// Boucle pour v�rifier que les �tats sont bien identiques
								for (int i = 0; i < alphabet.length; i++) {
									if (comparaisonEtat(autoMinimiser.get(k).getEtatFinal(i), autoMinimiser.get(l).getEtatFinal(i)) == false) {
										break; // Si les 2 transitions ne sont pas identiques ils ne sont identiques
									}
									nombreEtatIdentique++; 
								}
								// Ils sont identiques
								if (nombreEtatIdentique == alphabet.length) {
									identique = true;
									isole = false;
									// On ajouter l'etat au nom du nouvel etat
									for (int a = 0; a < autoMinimiser.get(k).getNomEtat().size(); a++) {newNomArrayList.add(autoMinimiser.get(l).getNomEtat().get(a)); }
								}
							}
							if (tabEtatEtudier.contains(newNomArrayList) == false) {
								tabEtatEtudier.add(newNomArrayList);
							}
						}
						if (identique || isole) {
							// Remplacement des etats par le nom
							for (int i = 0; i < autoMinimiser.size(); i++) {
								for (int m = 0; m < alphabet.length; m++) {
									boolean ajout = false; 
									// Remplacement partout o� il y avait des les anciens �tats
									for (int m2 = 0; m2 < etats.get(i).getEtatFinal(m).size(); m2++) {
										for (int m3 = 0; m3 < newNomArrayList.size(); m3++) {
											if (etats.get(i).getEtatFinal(m).get(m2) == newNomArrayList.get(m3)) {
												autoMinimiser.get(i).setEtatFinal(m,newNomArrayList);
												ajout = true; 
											}
											if(ajout) {
												break; 
											}
										} 
									}
								} 
							}
						}				
					}
					//System.err.println(newNomArrayList + " " + tabEtatEtudier);
					//System.out.println("--------------------------");	
				}
				// Si on se retrouve � la fin des tours on FUSION les �tats identiques
				if (j == autoMinimiser.size()-1) {
					// ETAPE 3 : FUSION 	
					for (int i = 0; i < tabEtatEtudier.size(); i++) {
						// Fusion des nom d'etats
						int l = 0, m = 0; // l : indice ligne, m : indice de case
						// Recherche du premier etat 
						while ( l < autoMinimiser.size() && autoMinimiser.get(l).getNomEtat().get(m) != tabEtatEtudier.get(i).get(0)) {
							m++; 
							if(m == autoMinimiser.get(l).getNomEtat().size()) {
								l++; 
								m = 0; 
							}							
						}
						
						// REMPLACEMENT des etats Initiaux par le nouvel etat
						if (etatInit.contains(autoMinimiser.get(l).getNomEtat())) {
							etatInit.add(tabEtatEtudier.get(i));
							for (int m2 = 0; m2 < tabEtatEtudier.get(i).size(); m2++) {
								ArrayList<Integer> nom = new ArrayList<>(); 
								nom.add(tabEtatEtudier.get(i).get(m2)); 
								for (int n = 0; n < etatInit.size(); n++) {
									if (comparaisonEtat(etatInit.get(n), nom)) {
										etatInit.remove(n); 
									}
								}
							}
						}
						// REMPLACEMENT des etats Terminaux par le nouvel etat
						if (etatTerm.contains(autoMinimiser.get(l).getNomEtat())) {
							etatTerm.add(tabEtatEtudier.get(i));
							for (int m2 = 0; m2 < tabEtatEtudier.get(i).size(); m2++) {
								ArrayList<Integer> nom = new ArrayList<>(); 
								nom.add(tabEtatEtudier.get(i).get(m2)); 
								for (int n = 0; n < etatTerm.size(); n++) {
									if (comparaisonEtat(etatTerm.get(n), nom)) {
										etatTerm.remove(n); 
									}
								}
							}
						}
						autoMinimiser.get(l).setNomEtat(tabEtatEtudier.get(i));
						// SUPPRESSION DES ETATS IDENTIQUES
						for (int m2 = 1; m2 < tabEtatEtudier.get(i).size(); m2++) {
							l = 0;
							m = 0; 
							while ( l < autoMinimiser.size() && autoMinimiser.get(l).getNomEtat().get(m++) != tabEtatEtudier.get(i).get(m2)) {
								if ( comparaisonEtat(autoMinimiser.get(l).getNomEtat(), tabEtatEtudier.get(i)) || m == autoMinimiser.get(l).getNomEtat().size()) {
									l++; 
									m = 0; 
								}								
							}
							autoMinimiser.remove(l);
							nbrEtats --; 
						}
					}					
				}
			}
			etats = autoMinimiser;
			afficherAutomate();
		}
		else {	
			System.out.println("L'automate ne peut pas etre minimiser car il n'est pas compleyt et/ou deterministe");
		}
		
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
	
}

	public void fusion_entree(){
		if (etatInit.size() > 1) {
			int i=0;
			while(etatInit.size()>1){
				etats.get(etatInit.get(i).get(0)).fusion(etats.get(etatInit.get(i+1).get(0)));
				etatInit.get(i).addAll(etatInit.get(i+1));//1+2->1,2
				etats.remove(etatInit.get(i+1).get(0).intValue());
				nbrEtats--;
				etatInit.remove(i+1);
			}
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
			if (remplacer_fermeture_epsilon(etats.get(i))) {
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
		
}
