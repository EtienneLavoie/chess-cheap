public class Reine extends Piece {

    public Reine() {
        super();
    }

    public boolean coupValide(int x, int y) {
        return coupDroit(x, y) || coupDiagone(x, y);
    }
}