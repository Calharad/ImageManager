package scripts;

import javafx.util.Pair;

import java.util.Arrays;

public class Position {

    public static Position extract(String id) {
        String[] idTab = id.split("_");
        idTab[0] = idTab[0].substring(2);
        return new Position(Integer.parseInt(idTab[0]), Integer.parseInt(idTab[1]));
    }

    public Position(int xx, int yy) {
        x = xx;
        y = yy;
    }

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Pair<Integer, Integer> toPair() {
        return new Pair<>(x, y);
    }

    public int getCalcValue(int number) {
        return number*y + x;
    }
}
