import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *Klasa reprezentująca Atakujących dziedziczy po Police
 */
public class Riot extends Police {

    /**
     * Parametry klasy:
     * riot - lista Atakujacych
     * neighbour - ilosc sasiadow Atakujacych
     * attack - prawdopodobienstwo ataku
     * z,alfa,beta,gamma - parametry dla prawdopodobieństwa
     * odl - odleglosc od najbliższego Policjanta
     * police, riots - ilość Policjantów i Atakujących
     * first_police - lista Policjantów na czele manifestacji
     * polician - Policjant do ataku
     * attackSuscenbility - szansa ataku
     * odwrot - cel odwrotu
     * cofnij - czy wycofuję
     * catched - czy złapany
     * escape - czy ucieka
     */
    public ArrayList<Riot> riot;
    public int neighbour = 0;
    public double attack;
    public double z;
    public double alfa,beta,gamma;
    public double odl;
    public int police,riots;
    public ArrayList<Police> first_police;
    public Police polician;
    public int attackSusceptibility;
    protected Position odwrot;
    public boolean cofnij = false;
    public boolean catched = false;
    public boolean escape = false;
    private Random randomGenerator = new Random();

    /**
     * Konstruktor klasy
     * @param currLoc obecna lokalizacja
     * @param targetLoc miejsce docelowe
     * @param ac lista Aktorów
     * @param bm lista Budynków
     * @param police lista Policjantów
     * @param riot lista Atakujących
     */
    public Riot(Position currLoc, Position targetLoc, ArrayList<Actor> ac, BuildingMap bm, ArrayList<Police> police,ArrayList<Riot> riot) {
        super(currLoc, targetLoc, ac, bm, police,riot);
        this.riot=riot;
        this.odwrot = currLoc;
    }

    /**
     * Funkcja ustawia parametry dla prawdopodobieństwa
     * z, alfa, beta, gamma
     * @param police ilosc Policjantów
     * @param riots ilość Atakujących
     * @param first_police lista Policjantów z pierwszych linii
     */
    public void setParametres(double z,double alfa,double beta,double gamma,int police, int riots,ArrayList<Police> first_police){
        this.z=z;
        this.alfa=alfa;
        this.beta=beta;
        this.gamma=gamma;
        this.police=police;
        this.riots=riots;this.first_police=first_police;
        attackSusceptibility = randomGenerator.nextInt(100);
    }

    /**
     * Funkcja ustawia kolor Atakujących na szary
     * @param g grafika
     */
    @Override
    public void color(Graphics g){
        g.setColor(Color.GRAY);
    }

    /**
     * Funkcja wylicza ilość sąsiadów wśród Atakujących
     */
    public void sumNeighbours(){
        for (Riot r:riot){
            int x = r.getPosition().getX();
            int y = r.getPosition().getY();
            if(Math.abs(this.getPosition().getX()-x)<=2 && Math.abs(this.getPosition().getY()-y)<=2 ){
                neighbour +=1;
            }
        }
    }

    /**
     * Funkcja oblicza odległość od Policjanta
     * @param polician Policjant
     * @return odległość
     */
    public double farFromPolice(Police polician){
        double d2 = Math.sqrt(Math.pow((this.getPosition().getX()-polician.getPosition().getX()),2)
                +Math.pow((this.getPosition().getY()-polician.getPosition().getY()),2));

        return d2;
    }

    /**
     * Funkcja wyszukuje Policjanta do ataku
     */
    public void setDestiny(){
        sumNeighbours();
        double min = Double.MAX_VALUE;
        for(Police p:first_police){
            Position p1 = p.getPosition();
            Position p2 = this.getPosition();
            double d2 = Math.sqrt(Math.pow((p1.getX()-p2.getX()),2)+Math.pow((p1.getY()-p2.getY()),2));
            if(d2<min) {min=d2; polician=p;}
        }
        targetLoc = new Position(polician.getPosition().getX(),polician.getPosition().getY());
        odl = min;
    }

    /**
     * Funkcja wylicza szanse ataku
     */
    public void attractive(){
        setDestiny();
        z = neighbour/z;
        attack = Math.pow(z,alfa)*Math.exp(-beta*odl)*Math.exp(-gamma*police/riots);
    }

    /**
     * Funkcja odpowiada za wycofanie Atakującego
     */
    public void cofnij(){
        Position temp = targetLoc;
        targetLoc=odwrot;
        move();
        targetLoc = temp;

    }

    /**
     * Funkcja odpowiada za powrót
     */
    public void powrot(){
        targetLoc=odwrot;
        move();
    }


}
