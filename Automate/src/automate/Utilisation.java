package automate;

import java.util.Scanner;

public class Utilisation {
	

	public static void main(String[] args) {

		Automate a1 = new Automate();	
		Automate a2 = new Automate();
		Automate a3 = new Automate(); 
		a1.lire_automate_fichier("src/Fichier/A6-17.txt");
		
		a2.lire_automate_fichier("src/Fichier/A6-5.txt");
		a3.lire_automate_fichier("src/Fichier/A6-36.txt");
		

		//a3.afficherAutomate();
		//a2.afficherAutomate();
    
		//a1.completion();
		//a1.afficherAutomate();
		//a1.minimisation();
		
		/*
		//test est_un_automate_asynchrone
		System.out.println("l'automate 1 est asynchrone (false) :"+a1.est_un_automate_asynchrone());
		System.out.println("l'automate 2 est asynchrone (true) :"+a2.est_un_automate_asynchrone());
		
		
		//TEST DE LA RECONNAISSANCE DE MOT
		System.out.println("Combien de mot voulez-vous tester?");
		Scanner scan = new Scanner(System.in);
		String reponse = scan.nextLine();
		int nb_mot = Integer.parseInt(reponse);
		int compteur = 0;
		
		while (compteur != nb_mot) {
			String mot = a1.lire_mot();
			if (a1.reconnaitre_mot(mot)) {
				System.out.println("Le mot est reconnu");
			}
			else {
				System.out.println("Le mot n'est pas reconnu");
			}
			compteur ++;
		}	


		//determinisation et completion asynchrone
		a2.determinisation_et_completion_asynchrone();
		a2.afficherAutomate();
		
		*/
		
		//TEST determinisation
		//a3.est_un_automate_deterministe() ;
		
		//a1.afficherAutomate(); 
	
		//a1.fusion_entree2(); 

		a1.determinisation();
		
		//a3.afficherAutomate();
		
		
	}
}
