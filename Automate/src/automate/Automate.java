package automate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


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
				// Affichage de E pour entrée et S pour sortie
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
			System.out.println("Le fichier n'existe pas, veuillez réessayer !");	
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
						j = 0;// On repart à la premier case du tableau 
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
		return false; 
	}
	public boolean est_un_automate_complet() {
		return true;
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
							// Si le nombre de Transition est supérieur alors 
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
				// Ajout de l'état poubelle 
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
		// Pour minimiser un automate il doit être déterministe et complet
		if(this.est_un_automate_complet() && this.est_un_automate_deterministe()) {
			System.out.println("\n   - MINIMISATION -");
			
			// Création du tableau des etats final minimal
			ArrayList<Etat> autoMinimiser = new ArrayList<>(); 
			
			// ETAPE 1 : SEPARATION ETAT T et NT
			// Vérification si T ou NT n'est pas isolé
			boolean Tisole = false, NTisole = false; 
			if(etatTerm.size() == 1) {
				Tisole = true;
				System.out.println("L'etat T (terminal) est isolé");
				for(int i=0; i<etats.size(); i++) {
					if(comparaisonEtat(etats.get(i).getNomEtat(), etatTerm.get(i))) {
						autoMinimiser.add(etats.get(i));
					}
				}
			}else if (etatTerm.size() == nbrEtats-1) {
				NTisole = true; 
				System.out.println("L'etat NT (non terminal) est isolé");
				boolean present; 
				for(int i=0; i<etats.size(); i++) {
					// On cherche l'etat qui n'est pas dans le tableau des etats terminaux
					present = false; 
					for (int j = 0; j < etatTerm.size(); j++) {
						if(comparaisonEtat(etats.get(i).getNomEtat(), etatTerm.get(j))) {
							present = true;
							break; 
						}
					}
					if(present == false) {
						autoMinimiser.add(etats.get(i));
					}
				}
			}
			
			// ETAPE 2 Minimisation 
			if (!NTisole) {
				for (int i = 0; i < (nbrEtats - etatTerm.size()); i++) {
					
				}
			}else if (!Tisole){
				ArrayList<ArrayList<Integer>> copieTransitionEtat = new ArrayList<>();
				copieTransitionEtat.addAll(etatTerm); 
				System.out.println(copieTransitionEtat);
				// Etat T OU NT
				int i = 0;
				boolean present;
				while (i < etatTerm.size()) {
					present = false;
					//On cherche a etudier uniquement les etats Terminaux
					for (int j = 0; j < etatTerm.size(); j++) {
						if(comparaisonEtat(etats.get(i).getNomEtat(), etatTerm.get(j))) {
							present = true;
							break; 
						}
					}
					if(present == true) {
						boolean premAjout = true, terminal; 
						for (int j = 0; j < alphabet.length; j++) {
							terminal = false; 
							// On cherche à savoir si l'état est terminale
							for (int k = 0; k < etatTerm.size(); k++) {
								System.out.println(etats.get(i).getEtatFinal(j) + "  " + etatTerm.get(k));
								if(comparaisonEtat(etats.get(i).getEtatFinal(j), etatTerm.get(k))) {
									terminal = true;
									break; 
								}
							}
							if (terminal == true) {
								// Si etat T : 1 et pour NT : 0
								if(premAjout) {
									copieTransitionEtat.get(i).set(j, 1);
									premAjout = false; 
								}else {
									copieTransitionEtat.get(i).add(1);
								}
							}else {
								
								if(premAjout) {
									copieTransitionEtat.get(i).set(j, 0);
									premAjout = false; 
								}else {
									copieTransitionEtat.get(i).add(0);
								}
							}
						}
						i++; 
						System.out.println(copieTransitionEtat);
					}
					
				}
				
				
				
				/*
				for (int i = 0; i < etatTerm.size(); i++) {
					copieTransitionEtat.add(new ArrayList<>()); 
					//copieTransitionEtat.get(i).add(etatTerm.get(i)); 
					boolean premAjout = true; 
					for (int j = 0; j < alphabet.length; j++) {
						// Est-ce que l'etat est Terminal 
						if(etats.get(i).getNomEtat() == copieTransitionEtat.get(i)) {
							if(etatTerm.contains(etats.get(i).getEtatFinal(j).get(0))){
								// Si etat T : 1 et pour NT : 0
								copieTransitionEtat.get(i).add(1);
								
								if(premAjout) {
									tab.add("1");
									premAjout = false; 
								}else {
									tab.set(i, tab.get(i).concat("1"));
								}
							}else {
								copieTransitionEtat.get(i).add(0);
								if(premAjout) {
									tab.add("0");
									premAjout = false; 
								}else {
									tab.set(i, tab.get(i).concat("0"));
								}
							}
						}
					}
					System.out.println(copieTransitionEtat + " " + tab);
				}
				// Vérification si un état s'isole ou non
				for (int i = 0; i < alphabet.length; i++) {
					
				}
				*/
				
			}
			// 3 Vérification si 2 états ou plus peuvent se rassembler ou pas  
			
			
			
			//PENSER A FAIRE nbrEtat == autoMiniser.size
		}
	}

	
	
}

	

