package _utile;

import java.util.List;

public class utile {


    public static void lireTabDeTab(List<int[]> list){
        for (int i = 0; i < list.toArray().length; i++) {
            System.out.print("[" + list.get(i)[0] + "," + list.get(i)[1] + "] ");
        }
    }

    public static boolean verifierActionsPos(int x, int y, List<int[]> list) {
        for (int i = 0; i < list.toArray().length; i++) {
            if (x == list.get(i)[0] && y == list.get(i)[1]) {
                return true;
            }
        }
        return false;
    }
}
