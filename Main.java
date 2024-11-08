import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static _utile.utile.lireTabDeTab;
import static _utile.utile.verifierActionsPos;

public class Main {
    public final Board BOARD;
    private static final Scanner scanner = new Scanner(System.in);
    private final List<int[]> list = new ArrayList<>();
    private boolean echecEtMath = false;
    private final List<int[]> echangerRoiEtTour = new ArrayList<>();
    private final List<int[]> listEchecEtMath = new ArrayList<>();
    int grosNombre = 0;


    //todo echanger roi et tour mouv special
    // verifier si les case entre le roi et tour sont vides
    // verifier si le roi peut etre attaquer sur les cases traverse
    // bug echec et math pour le roi noir semble ne pas fonctionner parfois

    //todo bug pion peut sauter piece


    //echec et math faut que les piece puissent bloquer un coup
    // egaliter si un joueur ne peut pu faire de mouvement
    //todo roi peut pas se deplacer devant pion

    //la prise en passant pion
    //compteur de point
    //creer ia totalement random

    public Main(Board board) {
        this.BOARD = board;
        placerPiece();                  //mettre pour jouer
//        BOARD.setBoardTest();        //mettre pour jeu test
        System.out.println(board);
        commencerJeu();
    }

    private void commencerJeu() {
        boolean couleur = true;
        Couleur vraiCouleur = Couleur.b;
        boolean sauterTour = false;
        while (!jeuTerminer()) {

            if (!sauterTour) {
                System.out.println("\n\nau tour du " + vraiCouleur.getNom());
                choisirPieceAJouer(vraiCouleur);
                System.out.println(BOARD);
                if (BOARD.siPionAtteintFin(couleur)) {
                    System.out.println(BOARD);
                }

                list.clear();

                if (siRoiEstEnEchec(couleur)) {
                    System.out.println("echec noir");

                    if (siRoiEstEnEchecEtMath(couleur)) {
                        System.out.println("\nechec et math (a moitie finit)");
                        echecEtMath = true;
                    } else {
                        siPasEchecEtMath(couleur);
                        sauterTour = true;
                    }
                }
            } else {
                sauterTour = false;
            }

            if (couleur) {
                vraiCouleur = Couleur.n;
            } else {
                vraiCouleur = Couleur.b;
            }
            couleur = !couleur;
        }
    }

    private void choisirPieceAJouer(Couleur couleur) {
        Piece piece;
        boolean estBon;
        boolean changerPiece;
        int x1, y1;

        listEchecEtMath.clear();

        do {
            do {
                estBon = true;
                System.out.print("choisie une piece a jouer\ncolone:");
                x1 = scanner.nextInt();
                System.out.print("ranger:");
                y1 = scanner.nextInt();

                if (BOARD.estPasUnePiece(x1, y1)) {
                    piece = BOARD.getPiece(x1, y1);

                    if (piece.getCouleur() == couleur) {
                        System.out.print("\ncoups valide pour" + piece + ":");
                        actionsPossibles(piece, x1, y1, true);

                        if (list.isEmpty()) {
                            System.out.println(" aucun mouvement possible\nPrennez une autre piece\n");
                            estBon = false;
                        }
                    } else {
                        System.out.println("Cette Piece ne t'apartient pas");
                        estBon = false;
                    }
                } else {
                    System.out.println("Ceci n'est pas une piece\n\nVeillez choisir l'une de vos piece");
                    estBon = false;
                }
            } while (!estBon);


            lireTabDeTab(list);
            changerPiece = choisirAction(x1, y1);
        } while (changerPiece);
    }

    private void ajouterAuTableau(int[] position, Piece piece, int x1, int y1) {
        if (position[0] != -1) {
            if (!siRoiSeMetEnEchec(piece, position[0], position[1], x1, y1)) {
                listEchecEtMath.clear();
                list.add(position);
            }
        }
    }

    private void ajouterAuTableauEchec(int[] position) {
        if (position[0] != -1) {
            listEchecEtMath.add(position);
        }
    }

    private boolean choisirAction(int x1, int y1) {
        boolean changerAction = false;
        boolean actionValide = true;


        do {
            if (!actionValide) {
                System.out.print("Les coups valides sont");
                lireTabDeTab(list);
            }
            actionValide = true;
            System.out.print("ou entrée nombre negatif pour changer de piece\ncolone:");
            int x2 = scanner.nextInt();

            if (x2 < 0) {
                changerAction = true;
                list.clear();
                listEchecEtMath.clear();
            } else {
                System.out.print("ranger:");
                int y2 = scanner.nextInt();

                if (verifierActionsPos(x2, y2 , echangerRoiEtTour) && BOARD.getPiece(x1, y1).getNom().equals("Roi")) {
                    deplacerRoiEtTour(x1, y1, x2, y2);
                } else if (verifierActionsPos(x2, y2, list)) {
                    BOARD.deplacerPiece(x1, y1, x2, y2);
                } else {
                    actionValide = false;
                }
            }
            if (!actionValide) {
                System.out.println("\nton action n'est pas valide\nVeillez reesayer");
            }
        } while (!actionValide);

        return changerAction;
    }

    private void actionsPossibles(Piece piece, int x1, int y1, boolean ajouterOu) {
        int[] position;
        for (int x2 = -7; x2 <= 7; x2++) {
            for (int y2 = -7; y2 <= 7; y2++) {

                boolean estSortiDuJeu = x1 + x2 <= 8 && x1 + x2 >= 1 && y1 + y2 <= 8 && y1 + y2 >= 1;
                if (!estSortiDuJeu) continue;

                position = coupSpecialPossible(piece, x2, x1, y2, y1);

                if (piece.coupValide(x2, y2)) {
                    if ((BOARD.estPasUnePiece(x1 + x2, y1 + y2))) {
                        if (!(piece.getNom().equals("Pion"))) {
                            if (!(piece.getCouleur().equals(BOARD.getCouleurPiece(x1 + x2, y1 + y2)))) {
                                position = actionsPossiblesSuite(piece, x1, y1, x2, y2);
                            }
                        }
                    } else {
                        position = actionsPossiblesSuite(piece, x1, y1, x2, y2);
                    }
                }

//                if (position[0] != -1) { //todo test
//                    System.out.println(position[0] + " " + position[1]);
//                }


                if (ajouterOu) {
                    ajouterAuTableau(position, piece, x1, y1);
                } else {
                    ajouterAuTableauEchec(position);
                }
            }
        }
    }

    private int[] coupSpecialPossible(Piece piece, int x2, int x1, int y2, int y1) {
        int[] position = new int[]{-1, -1};
        if (piece.coupSpecialPossible()) {  //verifier pour les coups special uniquement

            if ((piece.getNom().equals("Pion"))) {
                if ((BOARD.estPasUnePiece(x1 + x2, y1 + y2))) {  //verifier si la nouvelle position est une piece
                    if (!BOARD.getPiece(x1 + x2, y1 + y2).getCouleur().equals(piece.getCouleur())) {
                        if (piece.coupSpecialValide(x2, y2)) {
                            position = new int[]{x1 + x2, y1 + y2};
                        }
                    }
                }
            } else {
                if ((!BOARD.estPasUnePiece(x1 + x2, y1 + y2))) {  //verifier si la nouvelle position est PAS une piece
                    if (piece.coupSpecialValide(x2, y2)) {

                        if (deplacerRoiEtTourMarde(x1, y1, x2, y2)) {
                            position = new int[]{x1 + x2, y1 + y2};

                            if (!verifierActionsPos(x1 + x2, y1 + y2, echangerRoiEtTour)) {
                                echangerRoiEtTour.add(new int[]{x1 + x2, y1 + y2});
                            }
                        }
                    }
                }
            }
        }
        return position;
    }

    private int[] actionsPossiblesSuite(Piece piece, int x1, int y1, int x2, int y2) {
        int[] position = new int[]{-1, -1};
        int xAdd = x1 + x2, yAdd = y1 + y2;
        boolean marchePas = false;

        if (!piece.ignoreTestSauter()) {//verifier si c est pas un cavalier ou pion ou roi pour les test suivant
            if (x2 <= 1 && x2 >= -1 && y2 <= 1 && y2 >= -1) {
                position = new int[]{xAdd, yAdd};
            } else if (y2 >= 2 && x2 == 0) {
                if (verifierActionsPos(xAdd, yAdd - 1, list) || verifierActionsPos(xAdd, yAdd - 1, listEchecEtMath)) {
                    if (!BOARD.estPasUnePiece(xAdd, yAdd - 1)) {
                        position = new int[]{xAdd, yAdd};
                    }
                }

            } else if (y2 <= -2 && x2 == 0) {                                                           //de bas en haut
                for (int i = -2; i >= y2; i--) {
                    if (BOARD.estPasUnePiece(xAdd, y1 + i + 1)) {
                        marchePas = true;
                    }
                }
                if (!marchePas) {
                    position = new int[]{xAdd, yAdd};
                }

            } else if (x2 >= 2 && y2 == 0) {                                                           //gauche a droite
                if (verifierActionsPos(xAdd - 1, yAdd, list) || verifierActionsPos(xAdd - 1, yAdd, listEchecEtMath)) {
                    if (!BOARD.estPasUnePiece(xAdd - 1, yAdd)) {
                        position = new int[]{xAdd, yAdd};
                    }
                }

            } else if (x2 <= -2 && y2 == 0) {                                                        //de droit a gauche
                for (int i = -2; i >= x2; i--) {
                    if (BOARD.estPasUnePiece(x1 + i + 1, yAdd)) {
                        marchePas = true;
                    }
                }
                if (!marchePas) {
                    position = new int[]{xAdd, yAdd};
                }

            } else if (y2 >= 2 && x2 >= 2) {//diagonale bas/droit
                if (verifierActionsPos(xAdd - 1, yAdd - 1, list) || verifierActionsPos(xAdd - 1, yAdd - 1, listEchecEtMath)) {
                    if (!BOARD.estPasUnePiece(xAdd - 1, yAdd - 1)) {
                        position = new int[]{xAdd, yAdd};
                    }
                }

            } else if (y2 <= -2 && x2 <= -2) {                                                   //diagonale haut/gauche
                for (int i = -2; i >= y2; i--) {
                    if (BOARD.estPasUnePiece(x1 + i + 1, y1 + i + 1)) {
                        marchePas = true;
                    }
                }
                if (!marchePas) {
                    position = new int[]{xAdd, yAdd};
                }

            } else if (x2 >= 2 && y2 <= -2) {                                                     //diagonale bas/gauche
                if (verifierActionsPos(xAdd - 1, yAdd + 1, list) || verifierActionsPos(xAdd - 1, yAdd + 1, listEchecEtMath)) {
                    if (!BOARD.estPasUnePiece(xAdd - 1, yAdd + 1)) {
                        position = new int[]{xAdd, yAdd};
                    }
                }

            } else if (x2 <= -2 && y2 >= 2) {                                                    //diagonale haut/droite
                for (int i = -2; i >= x2; i--) {
                    if (BOARD.estPasUnePiece(x1 + i + 1, y1 - i - 1)) {
                        marchePas = true;
                    }
                }
                if (!marchePas) {
                    position = new int[]{xAdd, yAdd};
                }
            } else {
                System.out.println("test ceci ne devrait pas s'afficher dans actionPossible");
//                System.out.println((xBase + x) + " " + (yBase + y));
//                System.out.println(x2 + " " + y2);
            }

        } else {
            position = new int[]{xAdd, yAdd};  //pour cavalier pion et roi
        }
        return position;
    }

    private boolean jeuTerminer() {
        return echecEtMath;
    }

    private void placerPiece() {
        BOARD.setPiece();
        BOARD.setCouleur();
    }

    private boolean deplacerRoiEtTourMarde(int x1, int y1, int x2, int y2) {
        int xDT = 0, yDT = 0;
//        System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);

        if (x1 == 4 && x2 == -2) {
            xDT = 1;
            yDT = 1;
        } else if (x1 == 4 && x2 == 2) {
            xDT = 8;
            yDT = 1;
        } else if (x1 == 5 && x2 == -2) {
            xDT = 1;
            yDT = 8;
        } else if (x1 == 5 && x2 == 2) {
            xDT = 8;
            yDT = 8;
        }

        if (xDT != 0) {
            if (BOARD.estPasUnePiece(xDT, yDT)) {
                Piece piece = BOARD.getPiece(xDT, yDT);
                if (piece.getNom().equals("Tour") && !piece.isABouger()) { //pas toucher pour l instant
                    return true;
                }
            }
        } else {
            System.out.println("ne doit pas afficher dans deplacerRoiEtTourMarde"); //todo a remettre apres test
        }
        return false;
    }

    private void deplacerRoiEtTour(int xDR, int yDR, int xFR, int yFR) {
        int xDT = 0, yDT = 0, xFT = 0, yFT = 0; //  x\y    depard\fin    tour/roi

        if ((xDR - xFR == 2 && BOARD.getPiece(xDR, yDR).getCouleur() == Couleur.b)) {
//            System.out.println((xFR - 1) + " " + yDR + " " + (xDR - 1) + " " + yFR);
            xDT = 1;
            yDT = 1;
            xFT = 3;
            yFT = 1;

        } else if ((xDR - xFR == -2 && BOARD.getPiece(xDR, yDR).getCouleur() == Couleur.b)) {
            xDT = 8;
            yDT = 1;
            xFT = 5;
            yFT = 1;
//            System.out.println((xFR - 1) + " " + yDR + " " + (xDR - 1) + " " + yFR);

        } else if ((xDR - xFR == 2 && BOARD.getPiece(xDR, yDR).getCouleur() == Couleur.n)) {
            xDT = 1;
            yDT = 8;
            xFT = 4;
            yFT = 8;
//            System.out.println((xDR - 1) + " " + yFR + " " + (xFR - 1) + " " + yDR);

        } else if ((xDR - xFR == -2 && BOARD.getPiece(xDR, yDR).getCouleur() == Couleur.n)) {
            xDT = 8;
            yDT = 8;
            xFT = 6;
            yFT = 8;
//            System.out.println((xDR - 1) + " " + yFR + " " + (xFR - 1) + " " + yDR);
        }

        if (xDT != 0) {
            if (BOARD.estPasUnePiece(xDT, yDT)) {
                Piece piece = BOARD.getPiece(xDT, yDT);

                if (piece.getNom().equals("Tour") && !piece.isABouger()) {
                        BOARD.deplacerPiece(xDT, yDT, xFT, yFT); //deplacer tour
                        BOARD.deplacerPiece(xDR, yDR, xFR, yFR); //deplacer roi
                }
            }
        }
    }

    private void siPasEchecEtMath(boolean couleur) {
        boolean actionPossible;
        int x, y;
        System.out.println("\n Tu doit deplacer ton roi ici");
        lireTabDeTab(listEchecEtMath);

        do {
            actionPossible = true;
            System.out.print("\ncolone:");
            x = scanner.nextInt();
            System.out.print("ranger:");
            y = scanner.nextInt();

            if (!verifierActionsPos(x, y, listEchecEtMath)) {
                actionPossible = false;
                System.out.println("tu ne peut pas deplacer ton roi ici");
            }

        } while (!actionPossible);
        int[] roi = BOARD.chercherRoi(couleur);
        BOARD.deplacerPiece(roi[0], roi[1], x, y);
        System.out.println(BOARD);
    }

    private boolean siRoiEstEnEchecEtMath(boolean couleur) {
        List<int[]> listEchec = new ArrayList<>();

        for (int i = 0; i < listEchecEtMath.toArray().length; i++) {
            listEchec.add(new int[]{listEchecEtMath.get(i)[0], listEchecEtMath.get(i)[1]});
        }
        listEchecEtMath.clear();
        int[] positionRoi = BOARD.chercherRoi(couleur);

        Piece piece = BOARD.getPiece(positionRoi[0], positionRoi[1]);
        piece.setAnnulerCoupSpecial(true);
        actionsPossibles(piece, positionRoi[0], positionRoi[1], false);
        piece.setAnnulerCoupSpecial(false);

        for (int i = 0; i < listEchec.toArray().length; i++) {
            for (int j = 0; j < listEchecEtMath.toArray().length; j++) {
                if (listEchecEtMath.get(j)[0] == listEchec.get(i)[0] && listEchecEtMath.get(j)[1] == listEchec.get(i)[1]) {
                    listEchecEtMath.remove(j); //ca l air de marcher
                }
            }
        }

        return listEchecEtMath.isEmpty();
    }

    private boolean siRoiEstEnEchec(boolean couleur) {
        listEchecEtMath.clear();
        int[] iRoi = BOARD.chercherRoi(couleur);

        boolean estEnEchec = false;
        Piece pRoi = BOARD.getPiece(iRoi[0], iRoi[1]);


        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {

                if (BOARD.estPasUnePiece(i, j) && !estEnEchec) {
                    Piece piece = BOARD.getPiece(i, j);
                    if ((piece.getCouleur().equals(Couleur.b) && couleur) || (piece.getCouleur().equals(Couleur.n) && !couleur)) {
                        BOARD.retirerPiece(iRoi[0], iRoi[1]);
                        actionsPossibles(piece, i, j, false);
                        BOARD.ajouterPiece(pRoi, iRoi[0], iRoi[1]);

                        for (int compteur = 0; compteur < listEchecEtMath.toArray().length; compteur++) {
                            if (listEchecEtMath.get(compteur)[0] == iRoi[0] && listEchecEtMath.get(compteur)[1] == iRoi[1]) {
                                estEnEchec = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return estEnEchec;
    }

    private boolean siRoiSeMetEnEchec(Piece piece, int x2, int y2, int x1, int y1) {
        boolean estEnEchec = false;

        BOARD.retirerPiece(x1, y1);

        //todo bug probable avec le roi qui disparait et fait crash car je cherche le roi avant de le remettre dans le jeu
        Piece pieceSwitch = null;
        if (BOARD.estPasUnePiece(x2, y2)) {
            pieceSwitch = BOARD.getPiece(x2, y2);
            System.out.println(pieceSwitch);
        }
        BOARD.ajouterPiece(piece, x2, y2);

        if (siRoiEstEnEchec(true) && piece.getCouleur().equals(Couleur.n)) {
            estEnEchec = true;
        }

        if (siRoiEstEnEchec(false) && piece.getCouleur().equals(Couleur.b)) {
            estEnEchec = true;
        }

        BOARD.retirerPiece(x2, y2);
        BOARD.ajouterPiece(piece, x1, y1);
        if (pieceSwitch != null) {
            BOARD.ajouterPiece(pieceSwitch, x2, y2);
        }

        return estEnEchec;
    }
}