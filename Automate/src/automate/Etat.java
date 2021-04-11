package automate;

import java.util.ArrayList;
import java.util.Collections;

public class Etat extends Automate {
	private ArrayList<Integer> numeroEtat; // Contient le numero de l'etat
	private ArrayList<Transition> transition; // Contiendra les transitions ex : 1a1 ou 2c8...
	private int nbrTrans;
	
	// CONSTRUCTEUR 
	public Etat(final int nom, final ArrayList<Transition> transition, final int nbrTrans) {
		this.numeroEtat = new ArrayList<>(); 
		this.numeroEtat.add(nom); 
		this.transition = new ArrayList<Transition>(); 
		this.nbrTrans = nbrTrans; 
		this.transition.addAll(transition); 
	}
	public Etat(final ArrayList<Integer> nom, final ArrayList<Transition> transition, final int nbrTrans) {
		this.numeroEtat = new ArrayList<>();
		this.numeroEtat.addAll(nom); 
		this.transition = new ArrayList<Transition>(); 
		this.nbrTrans = nbrTrans; 
		this.transition.addAll(transition); 
	}
	public Etat(final int nom) {
		this.numeroEtat = new ArrayList<>();
		this.numeroEtat.add(nom); 
		this.transition = new ArrayList<Transition>();
		this.nbrTrans = 0;
	}
	//constructeur de copie
	public Etat copie () {
		return new Etat(this.numeroEtat, this.transition, this.nbrTrans); 
	}
	
	
	// Getter et Setter
	public void setNomEtat(ArrayList<Integer> num) { 
		numeroEtat.clear();
		numeroEtat.addAll(num);  
	}
	public void setNomEtat(int num) { 	numeroEtat.set(0, num); }
	public void setnbrTrans(int num) { 	nbrTrans = num; }
	
	public ArrayList<Integer> getNomEtat() { 	return numeroEtat; }
	public int getNbrTrans() { 	return nbrTrans; }
	public ArrayList<Integer> getEtatDepart(final int index) {  	return transition.get(index).getEtatDepart(); }
	public char getLettre(final int index) { 		return transition.get(index).getLettre(); }
	public ArrayList<Integer> getEtatFinal(final int index) {		return transition.get(index).getEtatSortie(); }
	
	
	
	
	// AJOUT TRANSITION
	public void ajoutTransition( final ArrayList<Integer> eD, final char lettre, final ArrayList<Integer> eS) {
		transition.add( new Transition(eD, lettre, eS));
		nbrTrans++;
	}
	public void ajoutTransition( final int eD, final char lettre, final int eS) {
		transition.add(new Transition(eD, lettre, eS));
		nbrTrans++;
		Collections.sort(transition);
	}
	
	// AFFICHAGE NOM ETAT
	public void afficherEtatDepart(int i) {
		transition.get(i).afficherEtatDepart();
	}
	public void afficherEtatSortie(int i) {
		transition.get(i).afficherEtatSortie();
	}
	
	public void affichageNomEtat() {
		if (numeroEtat != null) {
			for(int i=0; i< numeroEtat.size(); i++) {
				if(i > 0) {
					System.out.print(".");
				}
				System.out.print(numeroEtat.get(i));
			}
		}
	}
	public void affichageEtat() {
		if (this != null) {
			System.out.print("Le nom est " + numeroEtat + " avec "+ nbrTrans +" transitions : ");
			for (int i = 0; i < transition.size(); i++) {
				transition.get(i).affichageTransition();
				System.out.print(", ");
			}
			System.out.println("");
		}
	}
	
	
	
	
	
}
