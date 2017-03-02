import java.awt.*;

/**
 * Klasa reprezentujaca Budynki
 */
public class Building  {

    /**
     * Parametry klasy:
     * width, height - szerokosc, wysokość Budynku
     * position - punkt startowy Budynku, lewy górny róg
     */
    protected int width,height;
    protected Position position;
    private int skala=5;

    /**
     * Konstuktor klasy
     * @param position pozycja startowa
     * @param width szerokosc
     * @param height wysokosc
     */
    public Building (Position position, int width, int height)
    {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    /**
     * Funkcja zwracająca szerokość budynku
     * @return szerokość
     */
    public int getWidth() {
        return width;
    }

    /**
     * Funkcja zwracająca wysokość Budynku
     * @return wysokość
     */
    public int getHeight() {
        return height;
    }

    /**
     * Funkcja zwracająca pozycję startową
     * @return pozycja startowa
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Funkcje rysuje Budynek
     * @param g grafika
     */
    public void draw(Graphics g)
    {
        g.setColor(Color.ORANGE);
        g.fillRect(position.getX()*skala, position.getY()*skala, width*skala, height*skala); //wspolrzedne x i y dla Graphics są na odwrót!!!!!!!!!!!!!!
        g.setColor(Color.BLACK);
        g.drawRect(position.getX()*skala, position.getY()*skala, width*skala, height*skala);

    }
}
