package automate;

import java.util.*;

public class Transition implements Comparable<Transition>{
	private ArrayList<Integer> etatDepart; 
	private char lettre; 
	private ArrayList<Integer> etatSortie; 
	
	// Constructeurs
	public Transition(final ArrayList<Integer> eD, char let, final ArrayList<Integer> eS) {
		etatDepart = new ArrayList<Integer>(); 
		etatSortie = new ArrayList<Integer>();
		lettre = let; 
		etatDepart.addAll(eD); 
		etatSortie.addAll(eS);
	}
	public Transition(final int eD, char let, final int eS) {
		etatDepart = new ArrayList<Integer>(); 
		etatSortie = new ArrayList<Integer>();
		lettre = let; 
		etatDepart.add(eD); 
		etatSortie.add(eS);
	}
	
	public Transition() {
		etatDepart = new ArrayList<Integer>(); 
		etatSortie = new ArrayList<Integer>();  
	}

	public Transition(Transition t) {
		this(t.getEtatDepart(), t.getLettre(), t.getEtatSortie());
	}
	
	
	public Transition copie() {
		return new Transition(etatDepart,lettre, etatSortie);
	}

	//Getters et setters
	public ArrayList<Integer> getEtatDepart() { return etatDepart; }
	public char getLettre() { return lettre; }
	public ArrayList<Integer> getEtatSortie() { return etatSortie; }
	
	public void setEtatDepart(ArrayList<Integer> etatD) { 
		this.etatDepart = copieArraylist(etatD);  
	}
	public void setLettre(char lettre) { this.lettre = lettre; }
	public void setEtatSortie(ArrayList<Integer> etatS) { 
		this.etatSortie = copieArraylist(etatS); 
	}
	
	public ArrayList<Integer> copieArraylist(ArrayList<Integer> etat){
		ArrayList<Integer> copie = new ArrayList<>(); 
		for (int i = 0; i < etat.size(); i++) {
			copie.add(etat.get(i)); 
		}
		return copie;
	}
	
	
	public void afficherTransition() {
		System.out.print(this.toString());
	}
	
	@Override
	public String toString() {
		return "(" + etatDepart + lettre + etatSortie + ")"; 
	}
	
	
	
	@Override
	public int compareTo(Transition t) {
		return this.lettre - t.lettre; 
	}
	
	
	// AFFICHAGE
	
	public void afficherEtatDepart() {
		afficherArrayListNom(etatDepart);
	}
	public void afficherEtatSortie() {
		afficherArrayListNom(etatSortie);
	}
	
	public void afficherArrayListNom(final ArrayList<Integer> etat) {
		if(this != null) {
			for (int i = 0; i < etat.size(); i++) {
				if (etat.get(i) == -1) {
					System.out.print("P");
				}else {
					System.out.print(etat.get(i));
				}				
				if (etat.size() > 1 && i < etat.size()-1) {
					System.out.print(".");
				}
			}
		}
	}
	public void affichageTransition() {
		afficherArrayListNom(etatDepart);
		System.out.print(lettre);
		afficherArrayListNom(etatSortie);
	}

	//1.2b2 et 2a2 retourne vrai (il ne sert a rien de les fusionner)
	public boolean etatDejaPresent(Etat e){
		for (Transition t : e.getTransition()) {
			for (int i = 0; i < etatSortie.size(); i++) {
				if (t.getEtatSortie().contains(etatSortie.get(i)) && t.getLettre()==lettre) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean estPresent(){
		for (int i = 0; i < etatDepart.size(); i++) {
			for (int j = 0; j < etatSortie.size(); j++) {
				if (etatDepart.get(i).equals(etatSortie.get(j))) { //mettre si lettre egale aussi
					return true;
				}
			}
		}
		return false;
	}
}
