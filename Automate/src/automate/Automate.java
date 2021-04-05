package automate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Automate {
	static public final int MAX = 26; // On acte qu'un etat ne peut pas avoir plus de 26 transition
	// Les attributs
	private char[] alphabet;
	private Etat[] etats;
	private int nbrEtats;

	private int[] etatInit;
	private int[] etatTerm;

	// Constructeur par defaut
	public Automate() {
	}

	public Automate(final int nbrEtat) {
		etats = new Etat[nbrEtat];
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
		for(int i=0; i<nbrEtats; i++) { System.out.print(i +" "); }
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
	public void affichertab(final int[] tab) {
		if (tab != null) {
			for(int i=0; i<tab.length; i++) {
				System.out.print(tab[i] +" ");
			}
		}
	}

	//Fonction pour afficher une tableau de transition
	public void tableTransitionAutomate() {
		if (etats != null) {
			// Affichage entete
			System.out.println("\n TABLE DE TRANSITION ");
			System.out.print("   |");
			// Affichage de l'alphabet que l'automate reconnait
			for (char c : alphabet) {
				for(int i=0; i<nbrEtats-1; i++) { System.out.print(" ");}
				System.out.print( c);
				for(int i=0; i<nbrEtats; i++) { System.out.print(" ");}
				System.out.print("|");
			}
			System.out.println();
			ligneSepration();
			int espace = nbrEtats*2;

			//Affichage corps : Nometat | etatlettre a | ...
			for(int i=0; i<nbrEtats; i++) {
				System.out.print(" " + etats[i].getNomEtat()+" |");
				for(int j=0; j<alphabet.length; j++) {
					for(int x=0; x < etats[i].getNbrTrans(); x++) {
						if (etats[i].getLettreDeTransitionIndex(x) == alphabet[j]) {
							System.out.print(" " + etats[i].getEtatFinalDeTransitionIndex(x));
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
	// Fonction pour afficher une separation dans la table de transition
	public void ligneSepration() {
		System.out.print("---|");
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
			System.out.println("Le fichier n'existe pas, veuillez r�essayer !");
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
				etats = new Etat[nbrEtats];

				// Nombre d'etat initiaux
				char nbr = (char) reader.read();
				int nombreEtatInit = Character.getNumericValue(nbr);
				etatInit = new int[nombreEtatInit];
				for (int i = 0; i < nombreEtatInit; i++) {
					nbr = (char) reader.read(); // A cause du caractere espace
					nbr = (char) reader.read();
					etatInit[i] = Character.getNumericValue(nbr);
					//System.out.println("etat = " + etatInit[i] + " ");// A SUPPRIMER
				}

				// Nombre d'etat finaux
				Lines = reader.readLine();
				nbr = (char) reader.read();
				nombreEtatInit = Character.getNumericValue(nbr);
				etatTerm = new int[nombreEtatInit];
				for (int i = 0; i < nombreEtatInit; i++) {
					nbr = (char) reader.read(); // A cause du caractere espace
					nbr = (char) reader.read();
					etatTerm[i] = Character.getNumericValue(nbr);
					//System.out.println("etat = " + etatTerm[i] + " ");// A SUPPRIMER
				}
				Lines = reader.readLine(); // On finit la ligne

				// Nombre de transition
				Lines = reader.readLine();
				int nbrTransition = Integer.parseInt(Lines);

				//Transition tabTransition = new Transition
				// Les transitions
				Transition[] transEtati= new Transition[MAX];
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
						etats[etatActuelle] = new Etat(numeroEtatEntree-1, transEtati, j);
						etatActuelle++; // On passe a un etat N+1
						j = 0;// On repart � la premier case du tableau
					}

					transEtati[j] = t.copie();
					j++;
					//Fin de la ligne
					Lines = reader.readLine();
				}
				etats[numeroEtatEntree] = new Etat(numeroEtatEntree, transEtati, j);
				reader.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}

public boolean est_complet(Automate a){
	for (int i = 0; i<etats[nbrEtats] ; i++) {
		if(nbrTransition >= alphabet.length()){
			return 1;
		}
		return 0;
	}

}
