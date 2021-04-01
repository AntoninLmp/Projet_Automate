package automate;

public class Transition{
	private int etatDepart; 
	private char lettre; 
	private int etatSortie; 
	
	// Constructeurs
	public Transition(int eD, char let, int eS) {
		etatDepart = eD; 
		lettre = let; 
		etatSortie = eS;
	}
	public Transition copie() {
		return new Transition(etatDepart,lettre, etatSortie);
	}

	//Getters et setters
	public int getEtatDepart() { return etatDepart; }
	public char getLettre() { return lettre; }
	public int getEtatSortie() { return etatSortie; }
	
	public void setEtatDepart(int etatDepart) { this.etatDepart = etatDepart; }
	public void setLettre(char lettre) { this.lettre = lettre; }
	public void setEtatSortie(int etatSortie) { this.etatSortie = etatSortie; }
	
	public void afficherTransition() {
		System.out.print(this.toString());
	}
	
	@Override
	public String toString() {
		return "(" + etatDepart + lettre + etatSortie + ")"; 
	}
}
