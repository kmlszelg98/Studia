import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Klasa reprezentujący ekran widoczny
 * dla użytkowników pokazujący przebieg symulacji
 */
public class Screen extends JPanel {

    private Graphics g;
    public ArrayList<Actor> ac;
    public ArrayList<Building> b;
    public ArrayList<Police> pol;
    public ArrayList<Riot> riot;

    /**
     * Konstruktor klasy
     * @param ac lista wszystkich Aktorów
     * @param b lista Budynków
     * @param pol lista Policjantów
     * @param riot lista atakujących
     */
    public Screen(ArrayList<Actor> ac,ArrayList<Building> b,ArrayList<Police> pol,ArrayList<Riot> riot) {
        super();
        this.ac = ac;
        this.b = b;
        this.pol=pol;
        this.riot=riot;
        setIgnoreRepaint(true);

    }

    /**
     * Metoda która rysuje na mapie wszystkich uczestników manifestacji
     * po kolei Aktorów, Budynki, Policjantów i Atakujących
     * @param g jest to parametr grafika potrzebny do rysowania z klasy java.awt
     */
    public void paintComponent (Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.BLUE);

        for (Actor i:ac)
        {
            i.draw(g);
        }

        for (Building i:b)
        {
            i.draw(g);
        }

        for (Police p:pol){
            p.draw(g);
        }

        for (Riot r:riot){
            r.draw(g);
        }


    }


}
