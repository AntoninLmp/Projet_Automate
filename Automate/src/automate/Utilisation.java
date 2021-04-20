package automate;

public class Utilisation {

	public static void main(String[] args) {

		//Automate a1 = new Automate();	
		Automate a2 = new Automate();
		//a1.lire_automate_fichier("src/Fichier/A6-3.txt");
		a2.lire_automate_fichier("src/Fichier/A6-5.txt");
		//a1.afficherAutomate();
		a2.afficherAutomate();
		//a1.completion();
		//a1.afficherAutomate();
		//a1.minimisation();
		
		//test est_un_automate_asynchrone
		//System.out.println("l'automate 1 est asynchrone (false) :"+a1.est_un_automate_asynchrone());
		//System.out.println("l'automate 2 est asynchrone (true) :"+a2.est_un_automate_asynchrone());

		//determinisation et completion asynchrone
		a2.determinisation_et_completion_asynchrone();
		a2.afficherAutomate();
	}

}
