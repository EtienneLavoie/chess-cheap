public class Fou extends Piece {
    public Fou() {
        super();
    }

    public boolean coupValide(int x, int y) {
        return coupDiagone(x, y);
    }
}