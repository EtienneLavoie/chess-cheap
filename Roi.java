public class Roi extends Piece {
    public Roi() {
        super();
    }

    public boolean coupValide(int x, int y) {
        return (x <= 1 && x >= -1) && (y <= 1 && y >= -1);
    }

    @Override
    public boolean ignoreTestSauter() {
        return true;
    }

    @Override
    public boolean coupSpecialPossible() {
        return !isAnnulerCoupSpecial();
    }

    @Override
    public boolean coupSpecialValide(int x, int y) {
        return (x == 2 || x == -2) && y == 0 && !isABouger();
    }
}