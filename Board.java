import java.util.Scanner;

public class Board {

    public Object[][] board;
    private final Object rien = "   none      ";
    private static final Scanner scanner = new Scanner(System.in);

    public Board() {
        setBoard();
    }

    public void setBoardTest() {  //todo methode pour faire des test
        board = new Object[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = rien;
            }
        }

//        this.board[0][0] = new Tour();
//        getPiece(1, 1).setCouleur(Couleur.n);

        this.board[2][0] = new Roi();
        getPiece(3, 1).setCouleur(Couleur.n);

        this.board[0][7] = new Roi();
        getPiece(1, 8).setCouleur(Couleur.b);

//        this.board[2][7] = new Tour();
//        getPiece(3, 8).setCouleur(Couleur.b);

//        this.board[4][1] = new Pion();
//        getPiece(5, 2).setCouleur(Couleur.b);

        this.board[3][4] = new Reine();
        getPiece(4, 5).setCouleur(Couleur.b);
    }

    public void setBoard() {
        board = new Object[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 2; j < 6; j++) {
                board[i][j] = rien;
            }
        }
    }

    public void setPiece() {
        this.board[0][0] = new Tour();
        this.board[7][0] = new Tour();
        this.board[0][7] = new Tour();
        this.board[7][7] = new Tour();

        this.board[7][5] = new Tour();
        getPiece(8, 6).setCouleur(Couleur.n);

        this.board[1][0] = new Cavalier();
        this.board[6][0] = new Cavalier();
        this.board[1][7] = new Cavalier();
        this.board[6][7] = new Cavalier();

        this.board[2][0] = new Fou();
        this.board[5][0] = new Fou();
        this.board[2][7] = new Fou();
        this.board[5][7] = new Fou();

        this.board[4][0] = new Reine();
        this.board[3][0] = new Roi();

        this.board[3][7] = new Reine();
        this.board[4][7] = new Roi();

        for (int i = 0; i < 8; i++) {
            this.board[i][1] = new Pion();
        }
        for (int i = 0; i < 8; i++) {
            this.board[i][6] = new Pion();
        }
    }

    public int[] chercherRoi(boolean couleur) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getClass().getSimpleName().equals("Roi") && getCouleurPiece(i + 1, j + 1).equals(Couleur.n) && couleur) {
                    return new int[]{++i, ++j};
                } else if (board[i][j].getClass().getSimpleName().equals("Roi") && getCouleurPiece(i + 1, j + 1).equals(Couleur.b) && !couleur) {
                    return new int[]{++i, ++j};
                }
            }
        }
        return new int[]{0, 10};
    }

    public boolean estPasUnePiece(int x, int y) {
        if (x < 1 || x > 8 || y < 1 || y > 8) { //todo y a un truc bizarre ici
            System.out.println("erreur valeur invalide x:" + (1 + x) + " y" + (1 + y) + " faudrait eviter de faire crash le jeu");  //todo test
            return false;
        }
        return !(this.board[--x][--y].equals(rien));
    }

    public Piece getPiece(int x, int y) {
        return (Piece) board[--x][--y];
    }

    public Couleur getCouleurPiece(int x, int y) {
        Piece valeur = (Piece) board[--x][--y];
        return valeur.getCouleur();
    }

    public void setCouleur() {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 2; j++) {
                getPiece(i, j).setCouleur(Couleur.b);
            }
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 7; j <= 8; j++) {
                getPiece(i, j).setCouleur(Couleur.n);
            }
        }
    }

    public void retirerPiece(int x, int y) {
        board[--x][--y] = rien;
    }

    public void ajouterPiece(Piece piece, int x, int y) {
        board[--x][--y] = piece;
    }

    public void deplacerPiece(int x1, int y1, int x2, int y2) {
        board[x2 - 1][y2 - 1] = board[--x1][--y1];
        getPiece(x2, y2).setABouger(true);
        board[x1][y1] = rien;
    }

    public boolean siPionAtteintFin(boolean couleur) {
        boolean atteintFin = false;
        for (int i = 0; i < 8; i++) {
            if (board[i][7].getClass().getSimpleName().equals("Pion") && couleur) {
                System.out.println("le pion a atteint la fin\nVous voulez quoi? (reine,tour,fou,cavalier");
                String str = scanner.next();
                switch (str) {
                    case "reine" -> board[i][7] = new Reine();
                    case "cavalier" -> board[i][7] = new Cavalier();
                    case "tour" -> board[i][7] = new Tour();
                    case "fou" -> board[i][7] = new Fou();
                    default -> {
                    }
                }
                getPiece(++i, 8).setCouleur(Couleur.b);
                atteintFin = true;
            } else if (board[i][0].getClass().getSimpleName().equals("Pion") && !couleur) {
                System.out.println("le pion a atteint la fin\nVous voulez quoi? (reine,tour,fou,cavalier");
                String str = scanner.next();
                switch (str) {
                    case "reine" -> board[i][0] = new Reine();
                    case "cavalier" -> board[i][0] = new Cavalier();
                    case "tour" -> board[i][0] = new Tour();
                    case "fou" -> board[i][0] = new Fou();
                    default -> {
                    }
                }
                getPiece(++i, 1).setCouleur(Couleur.n);
                atteintFin = true;
            }
        }
        return atteintFin;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            str.append("\n\n");
            for (int j = 0; j < 8; j++) {
                str.append(board[j][i]);
            }
        }
        return str.toString();
    }
}