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
	
	public Etat() {
		this.numeroEtat = new ArrayList<>(); 
		this.transition = new ArrayList<Transition>();
		this.nbrTrans = 0;
	}
	
	public Etat(final ArrayList<Integer> nom) {
		this.numeroEtat = new ArrayList<>();
		this.numeroEtat.addAll(nom); 
		this.transition = new ArrayList<Transition>();
		this.nbrTrans = 0;
	}
	
	//constructeur de copie
	public Etat(final Etat e){
		this(e.numeroEtat, e.transition, e.nbrTrans);
	}

	public Etat copie () {
		ArrayList<Transition> transCopie = new ArrayList<>(); 
		for (int i = 0; i < transition.size(); i++) {
			transCopie.add(transition.get(i).copie());
		}
		return new Etat(this.numeroEtat, transCopie, this.nbrTrans); 
	}
	
	@Override
	public String toString() {
		return "Etat [numeroEtat=" + numeroEtat + ", transition=" + transition + ", nbrTrans=" + nbrTrans + "]";
	}
	
	// getter et setter
	public void setNomEtat(ArrayList<Integer> num) { 
		numeroEtat.clear();
		numeroEtat.addAll(num);  
	}
	public void setNomEtat(int num) { 	numeroEtat.set(0, num); }

	public void setNomEtat(int indice, int nom) { 
		numeroEtat.set(indice, nom); 
	}

	public void setnbrTrans(int num) { 	nbrTrans = num; }
	public void setLettre(final int index, final char lettre) {	transition.get(index).setLettre(lettre); }
	public void setEtatDepart(final int index,final ArrayList<Integer> nom) { 	transition.get(index).setEtatDepart(nom); }
	public void setEtatFinal(final int index,final ArrayList<Integer> nom) { 	transition.get(index).setEtatSortie(nom); }
	public void setEtatDepart(final int index,final int nom) { 	
		ArrayList<Integer> nomArrayList = new ArrayList<>(); 
		nomArrayList.add(nom); 
		transition.get(index).setEtatDepart(nomArrayList); 
	}
	public void setEtatFinal(final int index,final int nom) { 
		ArrayList<Integer> nomArrayList = new ArrayList<>(); 
		nomArrayList.add(nom);
		transition.get(index).setEtatSortie(nomArrayList); 
	}
	
	
	
	public ArrayList<Integer> getNomEtat() { 	return numeroEtat; }
	public int getNbrTrans() { 	return nbrTrans; }
	public ArrayList<Transition> geTransitions() { return transition; }
	public ArrayList<Integer> getEtatDepart(final int index) {  	return transition.get(index).getEtatDepart(); }
	public char getLettre(final int index) { 		return transition.get(index).getLettre(); }
	public ArrayList<Integer> getEtatFinal(final int index) {		return transition.get(index).getEtatSortie(); }
	
	public ArrayList<Transition> getTransition() {
		return copie().transition;
	}

	public void setTransition(int i, Transition t){
		transition.set(i, t.copie());
	}
	
	public void setTransition(int i, Transition t){
		transition.set(i, t.copie());
	}
	
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

	public void ajoutTransition( Transition t) {
		transition.add(new Transition(t));
		nbrTrans++;
		Collections.sort(transition);
	}
	
	
	//supprimer une transition 
	public void removeTransition(final Transition t) {
		transition.remove(t) ; 
		nbrTrans--;
	}
	public void removeTransition(final int t) {
		transition.remove(t) ; 
		nbrTrans--;
	}
	
	public void removeTransition(final int entier) {
		transition.remove(entier) ; 
		nbrTrans--; 
	}
	
	// AFFICHAGE NOM ETAT
	public void afficherEtatDepart(int i) {
		transition.get(i).afficherEtatDepart();
	}
	public void afficherEtatSortie(int i) {
		transition.get(i).afficherEtatSortie();
	}
	
	public void affichageNomEtat() {
		if(this != null) {
			for (int i = 0; i < numeroEtat.size(); i++) {
				if (numeroEtat.get(i) == -1) {
					System.out.print("P");
				}else {
					System.out.print(numeroEtat.get(i));
				}				
				if (numeroEtat.size() > 1 && i < numeroEtat.size()-1) {
					System.out.print(".");
				}
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

	public void fusion(final Etat e){
		// nom
        numeroEtat.addAll(e.getNomEtat());
        // transitions
        transition.addAll(e.getTransition());
        // nombre de transition
        nbrTrans = transition.size();
		
		triNomEtat(this);
    
		//renommer les transitions
		for (Transition t : transition) {
			t.setEtatDepart(numeroEtat);
		}
		
    }

	public Boolean contient_epsilon(){
		for (Transition t : transition) {
			if(t.getLettre() == '*'){
				return true;
			}
		}
		return false;
	}

	public int nbrEpsilon(){
		int nbr = 0;
		for (Transition t : transition) {
			if (t.getLettre() == '*') {
				nbr++;
			}
		}
		return nbr;
	}

	public boolean estPresent(ArrayList<Integer> nomDeSortie, char lettre){
		for (Transition t : transition) {
			if (t.getEtatSortie().equals(nomDeSortie) && lettre == t.getLettre()) {
				return true;
			}
		}
		return false;
	}
}

