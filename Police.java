import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Klasa reprezentująca Policjantów, dziedziczy po Aktor
 */
public class Police extends Actor{

    /**
     * Paramety klasy:
     * catchRiot - czy Policjant kogoś złapał
     * catched - złapana osoba
     * attackReady - czy jest atakowany
     */
    public boolean catchRiot = false;
    public Riot catched = null;
    public boolean attackReady = false;

    /**
     * Konstrktor klasy
     * @param currLoc obecna lokalizacja
     * @param targetLoc lokalizacja docelowa
     * @param ac lista Aktorów
     * @param bm lista Budynków
     * @param police lista Policjantów
     * @param riot lista Atakujących
     */
    public Police(Position currLoc, Position targetLoc, ArrayList<Actor> ac, BuildingMap bm, ArrayList<Police> police, ArrayList<Riot> riot) {
        super(currLoc, targetLoc, ac, bm, police,riot, 0);
    }

    /**
     * Funkcja ustawia kolor Policjanta na niebieski
     * @param g grafika
     */
    @Override
    public void color(Graphics g){
        g.setColor(Color.BLUE);
    }

    /**
     * Funkcja ruchu dla Policjanta,
     * wczesniej sprawdzamy czy doszedł do celu
     * @return true jeśli doszedł do celu, inaczej false
     */
    @Override
    @SuppressWarnings("Duplicates")
    public boolean move(){

        if(!currLoc.equals(targetLoc)) {
            Position target = targetLoc;
            LinkedList<Position> pathToTarget = bm.findShortestPath(currLoc, target);
            Position pos = pathToTarget.pollFirst();

            if (pos.getX() - currLoc.getX() == 1) {
                Right();
            } else if (currLoc.getX() - pos.getX() == 1) {
                Left();
            } else if (pos.getY() - currLoc.getY() == 1) {
                Down();
            } else if (currLoc.getY() - pos.getY() == 1) {
                Up();
            }
            if(catched!=null){
                this.targetLoc= catched.getPosition();
            }
        }
        else
        {
            if(locations.size()==1)
                return true;
            else if(locations.size()>1) {
                locations.remove(0);
                targetLoc = locations.get(0);
            }
        }
        return false;
    }

}
