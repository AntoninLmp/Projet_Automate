package automate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;




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
		return false; 
	}
	public boolean est_un_automate_complet() {
		// Verif synchrone et déterministe
		if(this.est_un_automate_deterministe() && !this.est_un_automate_asynchrone()) {
			System.out.println("nbr etats : "+ nbrEtats);
			boolean bool = true; 
			for (int a = 0; a < nbrEtats; a++) {
				int b =0;
				if(etats.get(a).getNbrTrans() != alphabet.length) {
					System.out.println("L'Automate n'est pas complet car : ");
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
						bool = false; 
					}
					//Saut de ligne entre chaque état
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
		
		// Pour minimiser un automate il doit être déterministe et complet
		if(this.est_un_automate_complet() && this.est_un_automate_deterministe()) {
			System.out.println("\n   - MINIMISATION -");
			// ETAPE 0 : Création d'une COPIE du tableau d'etats qui pourra être modifier sans impacter le tableau etats
			ArrayList<Etat> autoMinimiser = new ArrayList<>(); 
			for (int i = 0; i < etats.size(); i++) {
				autoMinimiser.add(etats.get(i).copie());			
			}
			
			// ETAPE 1 : SEPARER LES ETATS TERMINAUX ET NON TERMINAUX
			for (int j = 0; j < autoMinimiser.size(); j++) {
				boolean estTerm = false; // Etat : T (terminal) OU NT (non terminaux)
				//Boucle pour vérifier chaque etatsFinal : On cherche les etats qui sont terminaux
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
								// Boucle pour vérifier que les états sont bien identiques
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
									// Remplacement partout où il y avait des les anciens états
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
				// Si on se retrouve à la fin des tours on FUSION les états identiques
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
	
	
}

	

