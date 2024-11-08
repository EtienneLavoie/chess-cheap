public class Pion extends Piece {

    public Pion() {
        super();
    }

    public boolean coupValide(int x, int y) {
        if (x == 0 && y == 1 && getCouleur().equals(Couleur.b)) {
            return true;
        }
        if (x == 0 && y == -1 && getCouleur().equals(Couleur.n)) {
            return true;
        }
        if (x == 0 && y == 2 && getCouleur().equals(Couleur.b) && !isABouger()) {
            return true;
        }
        return x == 0 && y == -2 && getCouleur().equals(Couleur.n) && !isABouger();
    }

    @Override
    public boolean coupSpecialValide(int x, int y) {
        if ((x == 1 || x == -1) && y == 1 && getCouleur().equals(Couleur.b)) {
            return true;
        }
        if ((x == 1 || x == -1) &&  y == -1 && getCouleur().equals(Couleur.n)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean ignoreTestSauter() {
        return true;
    }

    @Override
    public boolean coupSpecialPossible() {
        return true;
    }
}