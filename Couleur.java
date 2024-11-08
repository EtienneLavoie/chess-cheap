public enum Couleur {
    n,
    b;

    public String getNom(){
        switch (this){
            case n:
                return "noir";
            case b:
                return "blanc";
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return getNom();
    }
}