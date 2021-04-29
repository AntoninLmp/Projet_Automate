package automate;

import java.util.Scanner;

public class Utilisation {
	

	public static void main(String[] args) {

		int choix = 0; 
		boolean premiereVisite = true; 
		Automate automate = new Automate();
		String nomFichier;
		Scanner scan = new Scanner(System.in);
		while ( choix != -1) {
			
			// AFFICHAGE DU MENU
			if (premiereVisite) {
				// ╠ ╣ ═ ╚ ╝ ╬ ╦ ╩ ║ ╔ ╗
				System.out.println("\t\t ╔══════════════════════════╗");
				System.out.println("\t\t ║ Bonjour, Bienvenue à toi ║");
				System.out.println("\t\t ╚══════════════════════════╝\n");
				premiereVisite = false; 
			}
			System.out.println("Veuillez choisir ce que vous desirez faire : ");
			System.out.println("\t ╔═════╦═══════════════════════════════╗");
			System.out.println("\t ║  1  ║ Lecture Automate et Affichage ║");
			System.out.println("\t ╠═════╬═══════════════════════════════╣");
			System.out.println("\t ║  2  ║ Determinisation et Completion ║");
			System.out.println("\t ╠═════╬═══════════════════════════════╣");
			System.out.println("\t ║  3  ║ Minimisation                  ║");
			System.out.println("\t ╠═════╬═══════════════════════════════╣");
			System.out.println("\t ║  4  ║ Reconnaissance de mots        ║");
			System.out.println("\t ╠═════╬═══════════════════════════════╣");
			System.out.println("\t ║  5  ║ Language complementaire       ║");
			System.out.println("\t ╠═════╬═══════════════════════════════╣");
			System.out.println("\t ║  6  ║ Standarsisation               ║");
			System.out.println("\t ╠═════╬═══════════════════════════════╣");
			System.out.println("\t ║ -1  ║ QUITTER                       ║");
			System.out.println("\t ╚═════╩═══════════════════════════════╝");
			
			System.out.print("Entrer votre choix : ");			
			choix = scan.nextInt();
			scan.nextLine();
			
			//Automate AFDC = new Automate(); 
			//Automate AFDCM = new Automate();
			
			switch (choix) {
				case 1:
					boolean existe; 
					do {
						nomFichier = "A6-";
						System.out.println("\t Quel automate voulez vous utilisez ?");
						String numeroFichier = scan.nextLine();
						nomFichier += numeroFichier + ".txt";
						automate = new Automate();
					 	existe = automate.lire_automate_fichier("src/Fichier/"+nomFichier);
					} while (!existe);
					automate.afficherAutomate();
					System.out.println("\n\n");
					break;
				case 2:
					
					if (automate.est_un_automate_asynchrone()) {
						System.out.println("L'automate est asynchrone !");
						// determinisation_et_completion_asynchrone()
					}else {
						System.out.println("L'automate est synchrone !");
						if (automate.est_un_automate_deterministe()) {
							System.out.println("L'automate est deterministe !");
							if (automate.est_un_automate_complet()) {
								// Deja determinisite et complet
								System.out.println("\t L'automate est synchrone, deterministe et complet!");
								
								//AFDC = automate.clone(); 
							}else {
								System.out.println("L'automate n'est pas complet!");
								automate.completion();
							}
						}else {
							//automate.determinisation_et_completion_synchrone(); 
						}
					}
					automate.afficherAutomate();
					break;
				case 3:
					automate.minimisation();
					automate.afficherAutomate();
					break;
				case 4:
					System.out.println("Combien de mot voulez-vous tester?");
					String reponse = scan.nextLine();
					int nb_mot = Integer.parseInt(reponse);
					int compteur = 0;
					
					while (compteur != nb_mot) {
						String mot = automate.lire_mot();
						if (automate.reconnaitre_mot(mot)) {
							System.out.println("Le mot est reconnu");
						}
						else {
							System.out.println("Le mot n'est pas reconnu");
						}
						compteur ++;
					}
					break;
				case 5:
					automate.automate_complementaire();
					automate.afficherAutomate();
					break;
				case 6:
					automate.automate_standard();
					automate.afficherAutomate();
					break;
					
				case -1: 
					System.out.println("\t Au revoir, a bientot ! ツ");
					break; 
				default:
					break;
			}
		}
		scan.close();
		
			
		
		/*
		
		Automate a1 = new Automate();	
		Automate a2 = new Automate();
		a1.lire_automate_fichier("src/Fichier/A6-3.txt");
		a2.lire_automate_fichier("src/Fichier/A6-6.txt");

		a1.afficherAutomate();
		a2.afficherAutomate();
    
		//a1.completion();
		//a1.afficherAutomate();
		//a1.minimisation();
		
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

		//System.out.println("l'automate 1 est asynchrone (false) :"+a1.est_un_automate_asynchrone());
		//System.out.println("l'automate 2 est asynchrone (true) :"+a2.est_un_automate_asynchrone());

		//determinisation et completion asynchrone
		//a2.determinisation_et_completion_asynchrone();
		//a2.afficherAutomate();
		 * */
	}
}
