package automate;

public class Utilisation {

	public static void main(String[] args) {
		Automate a1 = new Automate();	
		a1.lire_automate_fichier("src/Fichier/A6-4.txt");
		a1.afficherAutomate();
		a1.completion();
		a1.afficherAutomate();
		a1.minimisation();
	}

}
