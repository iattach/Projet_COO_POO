package Model;

public abstract class Request {
	protected String attribut;
	protected String attribut1;
	protected String attribut2;
	protected String resultat;
	protected String user;
	protected String mdp;
	
	public Request() {	
		this.resultat="";
	}
	/**
	 * @return
	 * 	retourner les résultats de requête
	 */
	public abstract String getResultat();
	
	/**
	 * @param s
	 * 	donner le 1er attribut pour le requête
	 */
	public void setAttribut1(String s) {
		attribut=s;
	}
	
	/**
	 * @param text
	 * 	donner le 2ième attribut pour le requête
	 */
	public void setAttribut2(String text) {
		this.attribut1=text;
	}
	/**
	 * @param text
	 * donner le 3ième attribut pour le requête
	 */
	public void setAttribut3(String text) {
		this.attribut2=text;
	}
	
	/**
	 * @param users
	 * @param m
	 * donner les paramètres pour la connextion de BDD
	 */
	public void setParametre(String users, String m) {
		this.user=users;
		this.mdp=m;
	}
}
