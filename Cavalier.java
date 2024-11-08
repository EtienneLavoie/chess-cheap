public class Cavalier extends Piece {

    public Cavalier() {
        super();
    }

    public boolean coupValide(int x, int y){
        if (x == 2 || x == -2) {
            return y == 1 || y == -1;
        }

        if (y == 2 || y == -2) {
            return x == 1 || x == -1;
        }
        return false;
    }

    @Override
    public boolean ignoreTestSauter() {
        return true;
    }

    @Override
    public String toString() {
        return longueurString(" Caval " + getCouleur());
    }
}