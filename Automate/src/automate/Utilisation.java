package automate;

import java.util.ArrayList;
import java.util.Scanner;


public class Utilisation {
	

	public static void main(String[] args) {

		int choix = 0; 
		boolean premiereVisite = true; 
		Automate automate = new Automate();
		String nomFichier, numeroFichier =""; 
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
			
			switch (choix) {
				case 1:
					boolean existe; 
					do {
						nomFichier = "A6-";
						System.out.println("\t Quel automate voulez vous utilisez ?");
						numeroFichier = scan.nextLine();
						nomFichier += numeroFichier + ".txt";
						automate = new Automate();
					 	existe = automate.lire_automate_fichier("src/Fichier/"+nomFichier);
					} while (!existe);
					automate.afficherAutomate();
					System.out.println("\n\n");
					break;
				case 2:
					if ( automate != null) {
						System.out.println("\n\n\t\t------DETERMINISATION ET COMPLETION--------") ; 
						if (automate.est_un_automate_asynchrone()) {
							System.out.println("\n\n\t ==> L'automate est asynchrone !");
							automate.determinisation_et_completion_asynchrone();
						}else {
							System.out.print("\n\n\t ==> L'automate est synchrone ");
							if (automate.est_un_automate_deterministe()) {
								System.out.print(", deterministe ");
								if (automate.est_un_automate_complet()) {
									// Deja determinisite et complet
									System.out.println(" et complet !");
								}else {
									System.out.println("");
									automate.completion();
								}

							}else {
								automate.determinisation_et_completion_synchrone(); 
							}
						}
						System.out.println("\n");
						automate.afficherAutomate();
						System.out.println("\n");
					}
					
					break;
				case 3:
					if (automate.est_minimal()) {
						System.out.println("\n\n\t--> L'Automate est deja minimal ");
					}else {
						automate.minimisation(true);
					}
					System.out.println("\n\n");
					automate.afficherAutomate();
					break;
				case 4:
					System.out.println("Combien de mot voulez-vous tester?");
					String reponse = scan.nextLine();
					int nb_mot = Integer.parseInt(reponse);
					int compteur = 0;
					
					while (compteur != nb_mot) {
						String mot = automate.lire_mot(scan);
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
	}
}
