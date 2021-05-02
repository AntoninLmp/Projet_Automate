package automate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;


public class Automate {
	static public final int MAX = 26; // On acte qu'un etat ne peut pas avoir plus de 26 transition
	// Les attributs
	private char[] alphabet; // L'alphabet ne changera pas
	private ArrayList<Etat> etats;
	private int nbrEtats;

	private ArrayList<ArrayList<Integer>> etatInit;
	private ArrayList<ArrayList<Integer>> etatTerm;

	// CONSTRUCTEUR PAR DEFAUT 
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
	
	
	public ArrayList<Etat> getEtats(){ return etats; } 

	public ArrayList<ArrayList<Integer>> copieDoubleArrayList (ArrayList<ArrayList<Integer>> etat){
		if (etat != null) {
			ArrayList<ArrayList<Integer>> copieEtatInit = new ArrayList<>();
			for (int i = 0; i < etat.size(); i++) {
				copieEtatInit.add(new ArrayList<>()); 
				for (int j = 0; j < etat.get(i).size(); j++) {
					copieEtatInit.get(i).add(etatInit.get(i).get(j)); 
				}
			}
			return copieEtatInit;
		}
		return null; 		 
	}
	


	/*----------------------------------------------------------------------------------*/
	/*****                          AFFICHAGE D'UN AUTOMATE                         *****/
	/*----------------------------------------------------------------------------------*/



	/* Affichage comprend :			FONCTION UTILISEE
	 * - etats qu'il contient 	-> afficherAutomate(), affichertabSimpleArray()
	 * - etats initiaux			-> afficherAutomate(), affichertabDoubleArray()
	 * - etat finaux			-> afficherAutomate(), affichertabDoubleArray()
	 * - table de transition 	-> tableTransitionAutomate(), ligneSepration()
	 */

	public void afficherAutomate() {
		System.out.println("\t\t -- AUTOMATE -- ");
		System.out.print("\t  - Alphabet { ");
		for(char alpha : alphabet) {
			System.out.print(alpha+ " ");
		}
		System.out.println("}");
		System.out.print("\t  - Etats Q = { ");
		for(int i=0; i<etats.size(); i++) {  
			affichertabSimpleArray(etats.get(i).getNomEtat()); 
			if (i != etats.size()-1) {System.out.print(", ");}
		}
		System.out.println(" }");

		System.out.print("\t  - Etats I = { ");
		affichertabDoubleArray(etatInit);
		System.out.println(" }");
		System.out.print("\t  - Etats T = { ");
		affichertabDoubleArray(etatTerm);
		System.out.println(" }");
		tableTransitionAutomate(etats);
	}

	// Fonction pour afficher les ArrayLists d'entiers
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
	public void tableTransitionAutomate(ArrayList<Etat> etats) {
		if (etats != null) {
			
			// Affichage Entete
			System.out.println("\n\t\t╔══════════════════════════╗ ");
			System.out.println("\t\t║    TABLE DE TRANSITION   ║");
			System.out.println("\t\t╚══════════════════════════╝   \n");
			System.out.print("\t         |");
			
			// Affichage de l'alphabet que l'automate reconnait
			for (char c : alphabet) {
				for(int i=0; i<nbrEtats-1; i++) { System.out.print(" ");}
				System.out.print(c);
				for(int i=0; i<nbrEtats; i++) { System.out.print(" ");}
				System.out.print("|");
			}
			// Une COLONNE POUR VISUALISER LE MOT VIDE
			for(int i=0; i<nbrEtats-1; i++) { System.out.print(" ");}
			System.out.print("*");
			for(int i=0; i<nbrEtats; i++) { System.out.print(" ");}
			System.out.print("|");
			System.out.println();
			ligneSepration();

			int espace = nbrEtats*2; 
			
			//Affichage corps : T/NT Nometat | etatlettre a | ... 
			for(int i=0; i<nbrEtats; i++) {
				// Affichage de E pour entree et S pour sortie
				System.out.print("\t"); 
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
					}else if (etats.get(i).getNomEtat().get(0) >= 10) {
						System.out.print("      ");
					}else {
						System.out.print("       ");
					}
				}else if(init == false || term == false) {
					if (etats.get(i).getNomEtat().size() > 1) {
						for (int j = 0; j < 8 - (etats.get(i).getNomEtat().size()*2) ; j++) {
							System.out.print(" ");
						}
					}else if (etats.get(i).getNomEtat().get(0) >= 10) {
						System.out.print("     ");
					}else {
						System.out.print("      ");
					}
				}else {
					if (etats.get(i).getNomEtat().size() > 1) {
						for (int j = 0; j < 7 - (etats.get(i).getNomEtat().size()*2) ; j++) {
							System.out.print(" ");
						}
					}else if (etats.get(i).getNomEtat().get(0) >= 10) {
						System.out.print("    ");
					}else {
						System.out.print("     ");
					}
				}
				// AFFICHAGE nom de l'etat
				etats.get(i).affichageNomEtat();
				System.out.print(" |");
				
				// Affichage des transitions
				for(int j=0; j<alphabet.length; j++) {
					for(int x=0; x < etats.get(i).getNbrTrans(); x++) {
						if (etats.get(i).getLettre(x) == alphabet[j]) {
							System.out.print(" ");
							etats.get(i).afficherEtatSortie(x);
							if (etats.get(i).getEtatFinal(x).size() > 1) {
								espace -= etats.get(i).getEtatFinal(x).size() *2 ; 
							}else if (etats.get(i).getEtatFinal(x).get(0) >= 10) {
								espace -= 3;
							}else {
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
				for(int x=0; x < etats.get(i).getNbrTrans(); x++) {
					if (etats.get(i).getLettre(x) == '*') {
						System.out.print(" ");
						etats.get(i).afficherEtatSortie(x);
						if (etats.get(i).getEtatFinal(x).size() > 1) {
							espace -= etats.get(i).getEtatFinal(x).size() *2 ; 
						}else if (etats.get(i).getEtatFinal(x).get(0) >= 10) {
							espace -= 3;
						}else {
							espace -= 2; 
						}
					}
				}
				for(int k = 0; k < espace; k++) {
					System.out.print(" ");
				}
				System.out.print("|");
				espace = nbrEtats*2;
				System.out.println();
				ligneSepration();
			}
		}
	}
		
	// Fonction pour afficher une separation dans la table de transition
	public void ligneSepration() {
		System.out.print("\t---------|");
		for (int i = 0; i < alphabet.length +1 ; i++) {
			for(int j = 0; j < nbrEtats; j++) {
				System.out.print("--");
			}
			System.out.print("|");
		}
		System.out.println();
	}


	
	/*----------------------------------------------------------------------------------*/
	/*****                         LECTURE AUTOMATE FICHIER                         *****/
	/*----------------------------------------------------------------------------------*/
	


	public boolean lire_automate_fichier(String NomFichier) {
		// ouverture du fichier
		File fichier = new File(NomFichier);
		// On regarde si le fichier n'existe pas
		if (!fichier.exists()) {
			System.out.println("Le fichier n'existe pas, veuillez reessayer !");	
			return false; 
		} 
		// Si il existe on peut travailler
		else {
			try {
				//BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fichier), "UTF-8"));
				RandomAccessFile reader = new RandomAccessFile(NomFichier, "r");
				
				int taille_ligne;
				String Ligne; 
				char caractere;
				long positionDebutLigne;
				
				/**** LECTURE DE ALPHABET *****/
				Ligne = reader.readLine();
				alphabet = new char[Integer.parseInt(Ligne)];
				char lettre = 'a';
				for (int i = 0; i < Integer.parseInt(Ligne); i++) { alphabet[i] = lettre++; }

				/**** LECTURE NOMBRE TOTAL D'ETAT *****/
				Ligne = reader.readLine();
				nbrEtats = Integer.parseInt(Ligne);
				if (this.etats == null) {
					this.etats = new ArrayList<Etat>();
				}
				
				/**** LECTURE ETAT INITIAUX *****/

				// on recupere la position dans le fichier
				positionDebutLigne = reader.getFilePointer(); 
				Ligne = reader.readLine();
				// on recupere la taille de la ligne
				taille_ligne = Ligne.length(); 
				// On se replace ou on avait sauvegarder la position du curseur
				reader.seek(positionDebutLigne);
				
				int nombreEtatInit = Character.getNumericValue(reader.read());
				taille_ligne--; 
				etatInit = new ArrayList<>(); 
				caractere = (char) reader.read(); // A cause du caractere espace
				taille_ligne--; 
				
				for (int i = 0; i < nombreEtatInit; i++) {
					String nombre = ""; 
					do {
						caractere = (char) reader.read();
						taille_ligne--; 
						if (caractere != ' ') {
							nombre += caractere;
						}						
					} while (caractere != ' ' && taille_ligne > 0);
					etatInit.add(new ArrayList<>()); 
					etatInit.get(i).add(Integer.parseInt(nombre));
				}
				// Fin de la ligne
				Ligne = reader.readLine(); 
				
				/**** LECTURE ETAT FINAUX *****/

				// Recuperation de la taille de la ligne
				positionDebutLigne = reader.getFilePointer(); 
				Ligne = reader.readLine();
				taille_ligne = Ligne.length(); 
				reader.seek(positionDebutLigne);
				
				// LECTURE : nombre etats finaux
				nombreEtatInit = Character.getNumericValue(reader.read());
				taille_ligne--; 
				etatTerm = new ArrayList<>();
				
				caractere = (char) reader.read(); // A cause du caractere espace
				taille_ligne--;
				
				for (int i = 0; i < nombreEtatInit; i++) {
					String nombre = ""; 
					do {
						caractere = (char) reader.read();
						taille_ligne--; 
						if (caractere != ' ') {
							nombre += caractere; 
						}
					} while (caractere != ' ' && taille_ligne > 0);
					etatTerm.add(new ArrayList<>()); 
					etatTerm.get(i).add(Integer.parseInt(nombre));
				}
				// Fin de la ligne
				Ligne = reader.readLine(); 		
				
				
				/**** LECTURE NOMBRE DE TRANSITION *****/
				Ligne = reader.readLine();
				int nbrTransition = Integer.parseInt(Ligne);

				// Les transitions
				ArrayList<Transition> tableauTransitionEtati = new ArrayList<Transition>();
				int j = 0, etatActuelle = 0; 
				int numeroEtatEntree = 0, numeroEtatSortie = 0; 
				char translettre;
				for (int i = 0; i < nbrTransition; i++) {
					
					// on recupere la position dans le fichier
					positionDebutLigne = reader.getFilePointer(); 
					Ligne = reader.readLine();
					// on recupere la taille de la ligne
					taille_ligne = Ligne.length(); 
					// On se replace ou on avait sauvegarder la position du curseur
					reader.seek(positionDebutLigne);
					
					numeroEtatEntree = 0;
					numeroEtatSortie = 0;
					String nombre = ""; 
					//LECTURE DE L'ETAT DE DEPART
					caractere =(char) reader.read();
					taille_ligne--; 
					while(caractere >= '0' && caractere <= '9'){
						nombre += caractere;
						caractere =(char) reader.read();
						taille_ligne--; 
					}
					numeroEtatEntree = Integer.parseInt(nombre);
					// LECTURE DE LA LETTRE (Elle est recuperer durant la dernier tour de boucle)
					translettre = caractere;
					
					//LECTURE DE L'ETAT SORTIE
					nombre = "";
					do{
						caractere =(char) reader.read();
						nombre += caractere;  
						taille_ligne--;
					}while(taille_ligne > 0);
					numeroEtatSortie = Integer.parseInt(nombre);
					
					Transition t = new Transition(numeroEtatEntree, translettre, numeroEtatSortie);
					// Si l'etat de depart change c'est que nous avons parcouru toutes les transitions

					if (numeroEtatEntree != etatActuelle) { 
						ArrayList<Transition> copieArrayList = new ArrayList<>(); 
						copieArrayList.addAll(tableauTransitionEtati); 
						etats.add(new Etat(numeroEtatEntree-1,copieArrayList, j)); 
						tableauTransitionEtati.clear();
						etatActuelle++; // On passe a un etat N+1
						j = 0;// On repart a la premier case du tableau 
					}
					
					tableauTransitionEtati.add(t);
					j++;
					//Fin de la ligne
					Ligne = reader.readLine();
				}
				if (etats.size() != nbrEtats) {
					for (int i = etats.size()+1; i < nbrEtats; i++) {
						etats.add(new Etat(i)); 
					}
				}
				// Pour le dernier etats on ajout ces transitions
				System.out.println(numeroEtatEntree);
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
		return true;
	}
	
	/*----------------------------------------------------------------------------------*/
	/*****                           AUTOMATE ASYNCHRONE                            *****/
	/*----------------------------------------------------------------------------------*/
	
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

	/*----------------------------------------------------------------------------------*/
	/*****                         	   DETERMINISATION                              *****/
	/*----------------------------------------------------------------------------------*/

	public boolean est_un_automate_deterministe() {  
		//Verifier si l’automate synchrone AF est déterministe ou non. Le resultat du test est affiche.
		
		if(this.est_un_automate_asynchrone()) {
			System.out.println("L'automate n'est pas deterministe car il est asynchrone"); 
			return false; 
		}
		// 1 seul entree pour etre deterministe 
		else if (this.etatInit.size() != 1) {
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
						if(trans1.getLettre() == trans2.getLettre()) {
							System.out.println("L'automate n'est pas deterministe car deux transitions ont le meme etat entree ET la meme lettre :  " + trans1 + trans2) ;  
							return false ; 
						}
					}
				}
			}
		}
		return true ;
	}


	public Automate determinisation() {
		
		if(this.est_un_automate_deterministe()==false) {
			
			System.out.println("\n\nDETERMINISATION \n") ; 
			Automate automateInit = new Automate(this) ; //on copie l'automate initiale
			
			// ETAPE 1 : FUSION DES ETATS D'ENTREE 
			this.fusion_entree2(); 

			System.out.println("\n--------------- AFFICHER AUTOMATE INITIAL APRES FUSION--------------\n") ; 
			this.afficherAutomate() ; 

			//liste de tous les nouveaux etat + ajout de l'etat entree 
			ArrayList<Etat> tabEtat = new ArrayList<>() ;     
			int b = 0 ; 
			while (!comparaisonEtat(this.etats.get(b).getNomEtat(), this.etatInit.get(0)) ) {
				b++ ; 
			}
			tabEtat.add(this.etats.get(b)); 
			
			
			System.out.println("\n--------------- AFFICHER Etat initial --------------\n") ; 
			System.out.println(this.etats.get(b)) ; 

			int i = 0 ; 
			while ( i != tabEtat.size() ) { // i regarde si on a traite tous les etats de la liste 
				for ( char lettre : this.alphabet) {   
					//lui c'est que le nom de l'etat
					ArrayList<Integer> nvEtat = new ArrayList<>() ;    

					// Creation d'un nouvel etat si 
					for  ( int j=0 ; j<tabEtat.get(i).getNbrTrans(); j++ ){    
						//on prend toutes les transitions passant par une lettre 
						if(tabEtat.get(i).getTransition().get(j).getLettre() == lettre ) {
							for(int k=0 ; k<tabEtat.get(i).getTransition().get(j).getEtatSortie().size() ; k++ ) {    //on sauvegarde l'etat de sortie
								// CREATION DU NOUVEAU NOM
								nvEtat.add(tabEtat.get(i).getTransition().get(j).getEtatSortie().get(k)) ;
								Collections.sort(nvEtat);	  //trier le tableau 
								supp_repetition_tab(nvEtat) ; //on supprime les numeros redondants
							}
						}
					}	
					
					if(nvEtat.size()>=1) {
						System.out.println("LE NOUVEAU ETAT EST " + nvEtat) ; 
						//fusion des etats de sortie
						Etat new_etat = new Etat() ; 
						for (int n : nvEtat) {
							//System.out.println("JE CHERCHE L'ETAT " + n + " Dans l'automate initial") ;
							for(int l = 0 ; l< automateInit.nbrEtats ; l++ ) {
								if( n == automateInit.etats.get(l).getNomEtat().get(0)) {  //dans l'automate initiale, les etats sont constitue d'un seul nombre
									Etat etat_int = new Etat(automateInit.etats.get(l));
									//System.out.println("TROUVE ETAT" + automateInit.etats.get(l)) ; 
									new_etat.fusion(etat_int);
								}
							}
						}
						triTransitions(new_etat) ;

						if(nvEtat.size()>1) {
							Transition t = new Transition(tabEtat.get(i).getNomEtat(), lettre, nvEtat) ; 
							tabEtat.get(i).ajoutTransition(t.getEtatDepart() , t.getLettre(), t.getEtatSortie());    //ajoute la nouvelle transition qui fusionne les etatTerm
							for( int j=0 ; j<nvEtat.size(); j++ ) {          //supprime les transitions constituees des etatTerm
								for (int k =0 ; k<tabEtat.get(i).getTransition().size() ; k++ ) {
									if (tabEtat.get(i).getTransition().get(k).getEtatSortie().size() == 1 ) {
										if(nvEtat.get(j) == tabEtat.get(i).getTransition().get(k).getEtatSortie().get(0) && tabEtat.get(i).getTransition().get(k).getLettre() == lettre ){
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
							if (comparaisonEtat(nvEtat, e.getNomEtat())) {
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
			//On redefinit les etats sorties et d'entree
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

			System.out.println("\n-------------------------------AFFICHAGE----------------------------- \n") ; 
			this.afficherAutomate() ;
			System.out.println("\n------------------------------FIN DETERMINISATION------------------------------ \n") ; 
			return this ; 
		}
		/*
		else {
			System.out.println("L'automate est deterministe") ; 
			 
		}*/
		return this ;
	}

	public void fusion_entree2 () {
		System.out.println("\n------------------------------ FUSION 2 \n") ; 
		if (etatInit.size() > 1) {	
			Etat new_etatI = new Etat() ; 

			for(int i =0 ; i< etatInit.size() ; i ++ ) {
				boolean test = false ; 
				int e=0  ;
				while ( test == false  && e< etats.size() ) {   
					//if(etats.get(e).getNomEtat().size() == 1) {
						//if(etats.get(e).getNomEtat().get(0) == etatInit.get(i).get(0)) {
					if(comparaisonEtat(etats.get(e).getNomEtat(), etatInit.get(i))) {
						Etat etat_temp = new Etat(etats.get(e)) ; 
						new_etatI.fusion(etat_temp);
						etats.remove(e) ; 
						nbrEtats -- ; 
						test = true ; 
					}	
					//}
					e = e + 1 ; 					 
				}
			}
			triTransitions(new_etatI); 
			etats.add(new_etatI) ; 
			nbrEtats ++ ; 
			etatInit.clear();
			etatInit.add(new_etatI.getNomEtat()) ; 	
		}
	}
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	/*****                         		  COMPLETION                                *****/
	/*----------------------------------------------------------------------------------*/

	
	public boolean est_un_automate_complet() {
		// Verif synchrone et deterministe
		if(this.est_un_automate_deterministe() && !this.est_un_automate_asynchrone()) {
			boolean bool = true; 
			for (int a = 0; a < nbrEtats; a++) {
				int indexTransition =0;
				if(etats.get(a).getNbrTrans() != alphabet.length) {
					System.out.println("L'Automate n'est pas complet car : ");
					System.out.print("	Etat "+ etats.get(a).getNomEtat()+" :");
					for (char lettre : alphabet) {
						/*Si b est superieur au nombres d'etats sachant que les etats sont tries
						 * ou si l'etat ne possede aucune transition */
						if (indexTransition == etats.get(a).getNbrTrans() || etats.get(a).getNbrTrans() == 0) {
							System.out.print(" en " + lettre );
						}else if (etats.get(a).getLettre(indexTransition) != lettre) {
							System.out.print(" en " +lettre );
						}else if(etats.get(a).getLettre(indexTransition) == lettre) { // La lettre est presente
							indexTransition++; //On augmente que si on a depasser la p
						}
						bool = false; 
					}
					//Saut de ligne entre chaque etat
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
							// Si le nombre de Transition est superieur alors 
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
				// Ajout de l'etat poubelle 
				etats.add(new Etat(-1));
				nbrEtats++;
				for (int i = 0; i < alphabet.length; i++) {
					etats.get(nbrEtats-1).ajoutTransition(-1, alphabet[i], -1);
				}
			}
		}
	}

	/*----------------------------------------------------------------------------------*/
	/*****         DETERMINISATION ET COMPLETION SYNCHRONE ET ASYNCHRONE            *****/
	/*----------------------------------------------------------------------------------*/
	
	public void determinisation_et_completion_synchrone() {
		this.determinisation() ; 
		this.completion() ; 
	}

	public void determinisation_et_completion_asynchrone(){
		//si plusieurs entree -> fusion des entrees
		fusion_entree();
		elimination_epsilon();
		//determinisation_et_completion_synchrone();
		//completion();
		
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

	//savoir si un etats doit etre remplacer par sa fermeture epsilon
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

	public Etat etat_a_fusioner(Etat e){ //etat ÃƒÂ  fusionner avec e
		for (Transition t : e.getTransition()) {
			if(t.getLettre() == '*'){
				return etats.get(t.getEtatSortie().get(0));
			}
			
		}
		return null;
	}

	public Etat fermeture(Etat e) { //permet d'ÃƒÂ©viter de faire sa propre copie de l'automate
		Etat copie = new Etat(e);
		while (copie.contient_epsilon()) { //remplacer par sa transition epsilon tant qu'il y a epsilon
			copie.fusion(etat_a_fusioner(copie));
			return copie;
		}
		return e;
	}
	public void elimination_epsilon(){
		for (int i = 0; i < nbrEtats; i++) {
			if (test_fermeture_epsilon(etats.get(i))) {
				etats.set(i, fermeture(etats.get(i)));
			}
			else{
				remove_etat(etats.get(i));
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
	
	
	//METHODE QUI VERIFIE SI UN ETAT EST TERMINAL 
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
	
	public boolean estTerminal(Etat e) {
		if (e != null){
			for(int i=0 ; i<etatTerm.size(); i++) {
				if(comparaisonEtat(etatTerm.get(i), e.getNomEtat())){
					return true;
				}
			}
		}		
		return false;
	}
	

	/*----------------------------------------------------------------------------------*/
	/*****                         		MINIMISATION                                *****/
	/*----------------------------------------------------------------------------------*/

	
	// METHODE QUI VERIFIE QUE L'AUTOMATE N'EST PAS DEJA MINIMAL
	public boolean est_minimal () {
		if( this != null && this.est_un_automate_complet() && this.est_un_automate_deterministe()) {
			// On  verifie qu'il n'est pas deja minimal
			// Sauvergarde des donnees car minimisation modifie directement l'automate
			Automate copieAutomate = new Automate(this);
			copieAutomate.minimisation(false);
			if (this.nbrEtats == copieAutomate.nbrEtats) {
				return true; 
			}
		}		
		return false; 
	}
		
	//Minimisation d'un automate 
	public void minimisation(boolean affichage) {
		
		// Pour minimiser un automate il doit etre deterministe et complet
		if(this.est_un_automate_complet() && this.est_un_automate_deterministe()) {
			if(affichage) {	System.out.println("\n   - MINIMISATION -"); }
			// ETAPE 0 : Creation d'une COPIE du tableau d'etats qui pourra etre modifier sans impacter le tableau etats
			ArrayList<Etat> autoMinimiser = new ArrayList<>(); 
			for (int i = 0; i < etats.size(); i++) {
				autoMinimiser.add(etats.get(i).copie());	
			}
			
			// ETAPE 1 : SEPARER LES ETATS TERMINAUX ET NON TERMINAUX
			for (int j = 0; j < autoMinimiser.size(); j++) {
				boolean estTerm = false; // Etat : T (terminal) OU NT (non terminaux)
				//Boucle pour verifier chaque etatsFinal : On cherche les etats qui sont terminaux
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
								// Boucle pour verifier que les etats sont bien identiques
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
									// Remplacement partout oe il y avait des les anciens etats
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
				}
				// Si on se retrouve e la fin des tours on FUSION les etats identiques
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
						if ( comparaisonEtat(autoMinimiser.get(l).getNomEtat(), tabEtatEtudier.get(i)) == false) {
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
						}				
						// SUPPRESSION DES ETATS IDENTIQUES
						for (int m2 = 1; m2 < tabEtatEtudier.get(i).size(); m2++) {
							l = 0;
							m = 0;
							//Changement des etats de depart par le nouvel etat 
							while (!comparaisonEtat(autoMinimiser.get(l).getNomEtat(), tabEtatEtudier.get(i)) && l < autoMinimiser.size()) {
								l++;
							}
							for (int k = 0; k < autoMinimiser.get(l).getNbrTrans(); k++) {
								autoMinimiser.get(l).setEtatDepart(k, tabEtatEtudier.get(i));
							}
							l = 0;
							m = 0;
							// Recherche de l'etat a supprimer
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
				if (affichage) {
					tableTransitionAutomate(autoMinimiser);
				}
			}
			etats = autoMinimiser;
		}
		else {	
			System.out.println("L'automate ne peut pas etre minimiser car il n'est pas complet et/ou deterministe");
		}
	}
	

	/*----------------------------------------------------------------------------------*/
	/*****                       RECONNAISSANCE DE MOTS                             *****/
	/*----------------------------------------------------------------------------------*/


	
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
		
		//On recupere le mot saisi par l'utilisateur
		Scanner scan = new Scanner(System.in);
		System.out.println("Veuillez saisir un mot ('*' reprÃƒÂ©sente le mot vide) :");
		String mot = scan.nextLine();
		System.out.println("Vous avez saisi : " + mot);
		
		//On vÃƒÂ©rifie si le mot est valide
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
				System.out.println("Veuillez saisir un mot ('*' reprÃƒÂ©sente le mot vide) :");
				mot = scan.nextLine();
				System.out.println("Vous avez saisi : " + mot);
			}
			else if (test == true) {
				System.out.println("Le mot est valide");
			}
		}while(test == false);
		scan.close();
		return mot;
	}
	
	
	/* METHODE QUI A PARTIR D UNE ARRAYLIST (etatInit et etatTerm) 
	 * PERMET D OBTENIR L ETAT CORRESPONDANT DANS LA LISTE DE TOUS LES ETATS
	 */
	public Etat etatCorrespondant(ArrayList<Integer> e){
		Etat etat_corres = etats.get(0);
		for(int i=0 ; i<etats.size(); i++) {
			if(comparaisonEtat(e, etats.get(i).getNomEtat())){
				etat_corres = etats.get(i);
			}
		}
		return etat_corres;
	}	
		
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
		
		//On parcourt l'automate dÃƒÂ©terministe complet en prenant les lettres du mot une ÃƒÂ  une 
		while (compteur != mot.length()) {
			trans = getTransitionExistante(etat_courant, symbole_courant);
			etat_courant = etatCorrespondant(trans.getEtatSortie());
			symbole_courant = mot.charAt(compteur);
			compteur ++;
		}
		trans = getTransitionExistante(etat_courant, symbole_courant);
		etat_courant = etatCorrespondant(trans.getEtatSortie());
		
		//On vÃƒÂ©rifie si l'ÃƒÂ©tat final sur lequel on est arrivÃƒÂ© est termianl ou non
		//Si oui, le mot est reconnu
		//Sinon, le mot n'est pas reconnu
		if (estTerminal(etat_courant)) {
			return true;
		}
		return false;
	}
		

  
	/*----------------------------------------------------------------------------------*/
	/*****                          LANGAGE COMPLEMENTAIRE                          *****/
	/*----------------------------------------------------------------------------------*/

  
  
	public void automate_complementaire(){
		if (this != null){
			if (!this.est_un_automate_deterministe() || !this.est_un_automate_complet()){
				System.out.println("ERREUR : l'automate n'est pas complet et/ou deterministe, le complementaire n'est pas possible");
				return; 
			}
			else if (this.est_un_automate_deterministe() && this.est_un_automate_complet() && this.est_minimal() ){
				System.out.println("Le complementaire est obtenu a partir d'un automate deterministe, complet et minimal (AFDCM)");
			}
			else if (this.est_un_automate_deterministe() && this.est_un_automate_complet()){
				System.out.println("Le complementaire est obtenu a partir d'un automate deterministe, complet (AFDC)");

			}
			/** OBTENTION DE L'AUTOMATE COMPLEMENTAIRE  */
			// les etats terminaux deviennent non terminaux et inversement
			ArrayList<ArrayList<Integer>> tab_tout_les_etats = new ArrayList<>();
			for(int i=0; i<etats.size(); i++) {  
				if (!estTerminal(etats.get(i))) {
					tab_tout_les_etats.add(etats.get(i).getNomEtat());
				}
			}
			System.out.print("\tLes etats ");
			affichertabDoubleArray(tab_tout_les_etats);
			System.out.println(" deviennent terminaux !");
			// On enleve les etats et on remplace par des etats qu'on vient de stocker
			etatTerm.clear();
			for (int i = 0; i < tab_tout_les_etats.size(); i++) {
				ArrayList<Integer> copie = new ArrayList<>(); 
				for (int j = 0; j < tab_tout_les_etats.get(i).size(); j++) {
					copie.add(tab_tout_les_etats.get(i).get(j));
				}
				etatTerm.add(copie); 
			}
		}
	}
	
	/*----------------------------------------------------------------------------------*/
	/*****                             STANDARDISATION                              *****/
	/*----------------------------------------------------------------------------------*/

	public void automate_standard(){
		if(this != null){
			boolean standard = true; 
			// ETAPE 1 : Verification qu'il n'est pas deja standard
			if(etatInit.size() == 1) {
				// Parcours de l'automate pour verifier qu'aucune transition revient sur l'etat initial
				for(int i = 0; i < etats.size(); i++){
					for(int j = 0; j < etats.get(i).getNbrTrans(); j++){
						System.out.println(etats.get(i).getEtatFinal(j)+ " =? " + etatInit.get(0));
						if(comparaisonEtat(etats.get(i).getEtatFinal(j), etatInit.get(0))) {
							standard = false;
						}
					}
				}
			}else {  // Si plus d'un etat initial alors pas standard
				standard = false; 
			}
			if(!standard){ // Pas standard
				/* Ajout d'un nouvel etat initial nommer nbrEtats + 1
				 * Variable du nouvel etat  */
				ArrayList<Integer> nouvel_etat_initial = new ArrayList<>(); 
				nouvel_etat_initial.add(this.nbrEtats);
				ArrayList<Transition> tableauTransitions = new ArrayList<>(); 
				int nombre_transitions = 0; 
				boolean dejaAjouter = false; 
				
				// Recuperation des etats des transitions
				for (int i = 0; i < etatInit.size(); i++) {
					for (int j = 0; j < etats.size(); j++) {
						// On cherche les etats initiaux
						if (comparaisonEtat(etatInit.get(i), etats.get(j).getNomEtat())) {
							for (int j2 = 0; j2 < etats.get(j).getNbrTrans(); j2++) {
								System.out.println(etats.get(j).getEtatFinal(j2));
								Transition transition = new Transition(nouvel_etat_initial, etats.get(j).getLettre(j2), etats.get(j).getEtatFinal(j2));
								tableauTransitions.add(transition); 
								nombre_transitions++;
								if(estTerminal(etats.get(j)) && dejaAjouter == false) {
									etatTerm.add(nouvel_etat_initial);
									dejaAjouter = true; 
								}
							}
						}
					}
				}			
				// Ajout d'un etat 
				etats.add(new Etat(nouvel_etat_initial, tableauTransitions, nombre_transitions));
				nbrEtats++;
				// Remplacement de l'etat initiale et reconnaissance du mot vide
				etatInit.clear();
				etatInit.add(nouvel_etat_initial);
			}else {
				System.out.println("L'automate est deja standard !");
			}
		}
	}			
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	/*****                             ECRITURE TRACES                              *****/
	/*----------------------------------------------------------------------------------*/
	
	public boolean ecriture_trace (String numeroAutomate) {
		//Ouverture fichier
		String NomFichier = "A6-trace" + numeroAutomate + ".txt";
		try {
			File fichier = new File("src\\Fichier_trace\\", NomFichier);
			if (fichier.createNewFile()) {
		        System.out.println("Fichier  creer : " + fichier.getName());
		    } else {
		    	System.out.println("Fichier deja existant.");
		    }		
			FileWriter writer = new FileWriter(fichier);
			// AFFICHAGE Nombre de lettres 
			writer.write(alphabet.length + "\n");
			// AFFICHAGE nombre etats 
			writer.write(nbrEtats + "\n");
			// AFFICHAGE etats initiaux
			writer.write(etatInit.size() + " ");
			for (int i = 0; i < etatInit.size(); i++) {
				writer.write(nomEnString(etatInit.get(i)) + "\n");
			}
			// AFFICHAGE etats finaux 
			writer.write(etatTerm.size() + " ");
			for (int i = 0; i < etatTerm.size(); i++) {
				writer.write(nomEnString(etatTerm.get(i)) + "\n");
			}
			// AFFICHAGE Nombre de transitions
			int nombreTransitions = 0;
			for (int i = 0; i < etats.size(); i++) {
				nombreTransitions+=etats.get(i).getNbrTrans(); 
			}
			writer.write(nombreTransitions+"\n");
			// AFFICHAGE chaque transition
			for (int i = 0; i < etats.size(); i++) {
				for (int j = 0; j < etats.get(i).getNbrTrans(); j++) {
					writer.write(nomEnString(etats.get(i).getEtatDepart(j)) + ""+ etats.get(i).getLettre(j)+ ""+ nomEnString(etats.get(i).getEtatFinal(j))+ "\n");
				} 
			}			
			
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false; 
		}
		return true;
		
	}
	
	public String nomEnString(ArrayList<Integer> a) {
		if (a != null) {
			String chaineString = "";
			for (int i = 0; i < a.size(); i++) {
				chaineString+=a.get(i);
				if(a.size() > 1 && i < a.size()-1) {
					chaineString+=".";
				}
			}
			return chaineString;
		}	
		return null;
	}
	
	

	//trie les transitions dans un etat
	//ne les classe pas 
	public void triTransitions(Etat e ) {      
		if (e.getNomEtat().size() > 1) {
			//suppression des redondances 
			for (int k=0 ; k < e.getTransition().size(); k++ ) {
				for (int l=k+1 ; l <e.getTransition().size(); l++ ) {

					if(e.getTransition().get(k).getLettre()== e.getTransition().get(l).getLettre() && comparaisonEtat(e.getTransition().get(k).getEtatSortie(), e.getTransition().get(l).getEtatSortie())) {
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
		if(comparaisonEtat(t1.getEtatDepart(),t2.getEtatDepart()) == false || comparaisonEtat(t1.getEtatSortie(), t2.getEtatSortie())==false  || t1.getLettre()!=t2.getLettre() ) {
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
