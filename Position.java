/**
 * Klasa Pozycja
 */
public class Position {
    /**
     * Parametry klasy:
     * x,y - współrzędne
     */
    private int x;
    private int y;

    /**
     * Konstruktor klasy
     * @param x x
     * @param y y
     */
    public Position(int x, int y){
        setX(x);
        setY(y);
    }

    /**
     * Funkcja zwraca pozycję X
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Funkcja ustawia pozycję X
     * @param x x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Funkcja zwraca pozycję Y
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Funkcja zwraca pozycję Y
     * @param y y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Funkcja sprawdza czy dwa punkty mają te same współrzędne
     * @param p pozycja
     * @return true jeśli identyczne inaczej false
     */
    public boolean equals(Position p) {
        return p.getX() == this.x && p.getY() == this.y;
    }


}
