package automate;

import java.util.*;

public class Transition implements Comparable<Transition>{
	private ArrayList<Integer> etatDepart; 
	private char lettre; 
	private ArrayList<Integer> etatSortie; 
	
	// Constructeurs
	public Transition() {
		etatDepart = new ArrayList<Integer>(); 
		etatSortie = new ArrayList<Integer>();  
	}
	
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
	public Transition copie() {
		return new Transition(etatDepart,lettre, etatSortie);
	}

	//Getters et setters
	public ArrayList<Integer> getEtatDepart() { return etatDepart; }
	public char getLettre() { return lettre; }
	public ArrayList<Integer> getEtatSortie() { return etatSortie; }
	
	public void setEtatDepart(ArrayList<Integer> etatD) { this.etatDepart.addAll(etatD); }
	public void setLettre(char lettre) { this.lettre = lettre; }
	public void setEtatSortie(ArrayList<Integer> etatS) { this.etatSortie.addAll(etatS); }
	
	public void afficherTransition() {
		System.out.print(this.toString());
	}
	
	@Override
	public String toString() {
		return "(" + etatDepart + lettre + etatSortie + ")"; 
	}
	
	public void afficherTransition(final ArrayList<Integer> etat) {
		if(this != null) {
			for (int i = 0; i < etat.size(); i++) {
				System.out.print(etat.get(i));
				if (etat.size() > 1 && i < etat.size()-1) {
					System.out.print(".");
				}
			}
		}
	}
	
	

	
	@Override
	public int compareTo(Transition t) {
		return this.lettre - t.lettre; 
	}
	
	
}
