package automate;

public class Etat extends Automate {
	private int numeroEtat; // Contient le numero de l'etat
	private Transition[] transition; // Contiendra les transitions ex : 1a1 ou 2c8...
	private int nbrTrans;
	
	public Etat(final int nom, final Transition[] transition, final int nbrTrans) {
		this.numeroEtat = nom; 
		this.transition = new Transition[30]; // 
		this.nbrTrans = nbrTrans; 
		for(int i=0; i < this.nbrTrans; i++) {
			this.transition[i] = new Transition(transition[i].getEtatDepart(), transition[i].getLettre(), transition[i].getEtatSortie());
		}
	}
	public Etat(final int nom) {
		this.numeroEtat = nom; 
	}
	//constructeur de copie
	public Etat copie () {
		return new Etat(this.numeroEtat, this.transition, this.nbrTrans); 
	}
	
	// getter et setter
	public void setNomEtat(int num) { 	numeroEtat = num; }
	public void setnbrTrans(int num) { 	nbrTrans = num; }
	
	public int getNomEtat() { 	return numeroEtat; }
	public int getNbrTrans() { 	return nbrTrans; }
	public int getEtatDepartDeTransitionIndexx(final int i) {  	return transition[i].getEtatDepart(); }
	public char getLettreDeTransitionIndex(final int i) { 		return transition[i].getLettre(); }
	public int getEtatFinalDeTransitionIndex(final int i) {		return transition[i].getEtatSortie(); }
	
	public void afficherEtat() {
		System.out.print("( "+numeroEtat+" ), { ");
		for(int i = 0; i < nbrTrans; i++) {
			transition[i].afficherTransition();
		}
		System.out.println("} "); 
	}
	
	
	
	
}
