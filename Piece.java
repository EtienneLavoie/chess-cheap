public class Piece {
    private boolean aBouger = false;
    private Couleur couleur;
    private boolean annulerCoupSpecial = false;

    public Piece() {
    }

    public void setAnnulerCoupSpecial(boolean annulerCoupSpecial) {
        this.annulerCoupSpecial = annulerCoupSpecial;
    }

    public boolean isAnnulerCoupSpecial() {
        return annulerCoupSpecial;
    }

    public boolean isABouger() {
        return aBouger;
    }

    public void setABouger(boolean aBouger) {
        this.aBouger = aBouger;
    }

    public String getNom() {
        return this.getClass().getSimpleName();
    }

    public boolean coupValide(int x, int y){
        return false;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    public String longueurString(String mot) {
        while (mot.length() <= 12) {
            mot += " ";
        }
        return mot;
    }

    public boolean coupDroit(int x, int y) {
        return ((x <= 7 && x >= -7) && (y <= 7 && y >= -7) && (y == 0 || x == 0));
    }

    public boolean coupDiagone(int x, int y) {
        return (x <= 7 && x >= -7) && ((y == x)) || (-y == x);
    }

    public boolean coupSpecialPossible() {
        return false;
    }

    public boolean coupSpecialValide(int x, int y) {
        return false;
    }

    public boolean ignoreTestSauter() {
        return false;
    }

    @Override
    public String toString() {
        return  longueurString(" " + getNom() + " " + getCouleur());
    }
}