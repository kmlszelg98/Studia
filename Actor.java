import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.Math;


/**
 * Klasa która reprezentuje ludzi będących w
 * pochodzie
 */
public class Actor {

    /**
     * Parametry klasy:
     * bm - mapa Budynków
     * currLoc, targetLoc - pozycje domyslna i docelowa
     * locations - lista celów
     * ac, family, police, rioter - listy Aktorów, rodziny, Policjantów, Atakujacych
     * happy - radosc uczestnika
     * leader, f - indeks lidera w tablicy, numer rodziny
     */
    public BuildingMap bm;
    protected Position currLoc;
    protected Position targetLoc;
    protected LinkedList<Position> locations = new LinkedList<>();
    private int happy;
    public ArrayList<Actor> ac;
    public ArrayList<Actor> family;
    public ArrayList<Police> police;
    public ArrayList<Riot> rioter;
    public int leader=0;
    private int f;
    private int r = 1;
    private int skala = 5;
    public int sasiad;

    /**
     * Konstruktor klasy Actor
     * @param currLoc obecna pozyycja aktora
     * @param targetLoc pozycja docelowa
     * @param ac lista wszystkich Aktorów
     * @param bm mapa Budynków
     * @param police lista Policjantów
     * @param f numer rodziny do której Aktor należy
     */
    public Actor(Position currLoc, Position targetLoc, ArrayList<Actor> ac, BuildingMap bm,ArrayList<Police> police,ArrayList<Riot> rioter,int f) {
        this.currLoc = currLoc;
        this.targetLoc = targetLoc;
        locations.add(targetLoc);
        this.ac = ac;
        this.bm=bm;
        this.f=f;
        this.police = police;
        this.rioter=rioter;
    }

    /**
     * Funkcja zwracająca radość Aktora
     * @return radość
     */
    public int getHappy() {
        return happy;
    }

    /**
     * Funkcja ustawiająca radosc Aktora
     * @param happy oznacza chwilową radość
     */
    public void setHappy(int happy) {
        this.happy = happy;
    }

    /**
     * Funkcja zwraca numer rodziny Aktora
     * @return numer rodziny
     */
    public int getF(){
        return f;
    }

    /**
     * Funkcja ustawia kolory Aktorów w zależności od
     * poziomu radości, im większa tym bardziej intensywny
     * kolor
     * @param g grafika
     */
    public void color(Graphics g){
        if (happy==5) g.setColor(new Color(255,0,0));
        else if (happy==4) g.setColor(new Color(255,70,0));
        else if (happy==3) g.setColor(new Color(255,120,0));
        else if (happy==2) g.setColor(new Color(255,150,0));
        else if (happy==1) g.setColor(new Color(255,200,0));
        else g.setColor(new Color(0,255,125));
        if(ac.get(0)==this) g.setColor(Color.BLACK);

    }

    /**
     * Funkcja dodaje do listy celów, kolejny cel Aktora
     * każdy aktor może mieć wiele celów w odpowiedniej kolejności
     * @param p nowy cel
     */
    public void addNewTarget(Position p){
        locations.add(p);
    }

    /**
     * Funkcja rysuje Aktorów
     * @param g grafika
     */
    public void draw(Graphics g) {

        color(g);
        g.drawOval(currLoc.getX()*skala, currLoc.getY()*skala, r*skala, r*skala);
        g.fillOval(currLoc.getX()*skala, currLoc.getY()*skala, r*skala, r*skala);
    }

    /**
     * Funkcja ustawia lidera dla danej rodziny f
     * @param leader tablica liderów dla każdej rodziny
     */
    public void setLeader(int[] leader) {
        this.leader = leader[f];

    }

    /**
     * Funkcja ustawia rodzinę dla danego Aktora
     * @param family tablica rodzin
     */
    public void setFamily(ArrayList<Actor> family[]) {
        this.family=family[f];

    }

    /**
     * Funkcja sprawdza czy blisko lidera
     * znajdują się wszyscy członkowie rodziny
     * @return true jeśli wszyscy są, inaczej false
     */
    public boolean checkFamily(){
        Position p=family.get(leader).getPosition();
        for(int i=0;i<family.size();i++){
            if(i!=leader) {
                Position p2 = family.get(i).getPosition();
                {
                    if (Math.abs(p.getX()-p2.getX())>1) return false;
                    if (Math.abs(p.getY()-p2.getY())>1) return false;
                }
            }
        }
        return true;
    }

    /**
     * Funkcja która wykonuje ruch dla danego Aktora uprzednio ustawiając odpowiednio jego cel
     * i znajdując najkrószą ścieżkę do tego celu
     * @return true jeśli Aktor doszedł do celu i false kiedy jescze nie
     */
    @SuppressWarnings("Duplicates")
    public boolean move(){
        if(!currLoc.equals(targetLoc)) {
            Position target = targetLoc;
            boolean ch =true;
            if (ac.get(0) != this) {
                if(family.get(leader)!=this) target = family.get(leader).getPosition();
                else {target = ac.get(0).getPosition();ch=checkFamily();}
            }
            else {
                happy=0;
            }
            if(ch  && checkDistance(this,ac.get(0))) {
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
            }
        }
        else
        {
            if(locations.size()>0)
            return true;
            else {
                locations.remove(0);
                targetLoc = locations.get(0);
            }
        }
        return false;
    }

    /**
     * Funkcja sprawdza dystans pomiędzy Aktorem i liderem
     * @param actor aktor
     * @param leader lider
     * @return true jeśli są daleko i false jeśli blisko
     */
    public boolean checkDistance(Actor actor, Actor leader){

        if(actor!=leader){
            Position p1 = actor.getPosition();
            Position p2 = leader.getPosition();
            if(Math.abs(p1.getX()-p2.getX())<=1 && Math.abs(p1.getY()-p2.getY())<=1) return false;

        }
        return true;
    }

    /**
     * Funkcja sprawdza czy dana pozycja jest już zajęta czy nie
     * @param dest pozycja do sprawdzenia
     * @return true jeśłi miejsce jest wolne i false jeśli zajęte
     */
    boolean check(Position dest)
    {
        if(dest.getX()<0 || dest.getX()>=bm.getSize() || dest.getY()<0 || dest.getY()>=bm.getSize() || !bm.isFree(dest)) {
            return false;
        }

        for (Actor i:ac)
        {
            if(i.getPosition().equals(dest))
                return false;
        }

        for (Police p: police){
            if(p.getPosition().equals(dest))
                return false;
        }

        for (Riot r: rioter){
            if(r.getPosition().equals(dest))
                return false;
        }

        return true;
    }

    /**
     * Funkcje dla ruchów
     */
    public void Left()
    {

        if(check(new Position(currLoc.getX()-1,currLoc.getY()))) {
            moveLeft();
        } else if(check(new Position(currLoc.getX()-1,currLoc.getY()-1))) {
            moveTopLeft();
        } else if(check(new Position(currLoc.getX()-1,currLoc.getY()+1))) {
            moveBotLeft();
        } else if(check(new Position(currLoc.getX(),currLoc.getY()-1))) {
            moveUp();
        } else if(check(new Position(currLoc.getX(),currLoc.getY()+1))) {
            moveDown();
        }

        sasiad =0;
        if(check(new Position(currLoc.getX()-1,currLoc.getY()))) sasiad +=1;
        if(check(new Position(currLoc.getX()-1,currLoc.getY()-1))) sasiad +=1;
        if(check(new Position(currLoc.getX()-1,currLoc.getY()+1))) sasiad +=1;
    }


    public void Right()
    {

        if(check(new Position(currLoc.getX()+1,currLoc.getY()))) {
            moveRight();
        } else if(check(new Position(currLoc.getX()+1,currLoc.getY()+1))) {
            moveBotRight();
        } else if(check(new Position(currLoc.getX()+1,currLoc.getY()-1))) {
            moveTopRight();
        } else if(check(new Position(currLoc.getX(),currLoc.getY()+1))) {
            moveDown();
        } else if(check(new Position(currLoc.getX(),currLoc.getY()-1))) {
            moveUp();
        }

        sasiad =0;
        if(check(new Position(currLoc.getX()+1,currLoc.getY()))) sasiad +=1;
        if(check(new Position(currLoc.getX()+1,currLoc.getY()+1))) sasiad +=1;
        if(check(new Position(currLoc.getX()+1,currLoc.getY()-1))) sasiad +=1;
    }

    public void Up()
    {

        if(check(new Position(currLoc.getX(),currLoc.getY()-1))) {
            moveUp();
        } else if(check(new Position(currLoc.getX()-1,currLoc.getY()-1))) {
            moveTopLeft();
        } else if(check(new Position(currLoc.getX()+1,currLoc.getY()-1))) {
            moveTopRight();
        } else if(check(new Position(currLoc.getX()-1,currLoc.getY()))) {
            moveLeft();
        } else if(check(new Position(currLoc.getX()+1,currLoc.getY()))) {
            moveRight();
        }

        sasiad =0;
        if(check(new Position(currLoc.getX(),currLoc.getY()-1))) sasiad +=1;
        if(check(new Position(currLoc.getX()-1,currLoc.getY()-1))) sasiad +=1;
        if(check(new Position(currLoc.getX()+1,currLoc.getY()-1))) sasiad +=1;
    }

    public void Down()
    {

        if(check(new Position(currLoc.getX(),currLoc.getY()+1))) {
            moveDown();
        } else if(check(new Position(currLoc.getX()+1,currLoc.getY()+1))) {
            moveBotRight();
        } else if(check(new Position(currLoc.getX()-1,currLoc.getY()+1))) {
            moveBotLeft();
        } else if(check(new Position(currLoc.getX()+1,currLoc.getY()))) {
            moveRight();
        } else if(check(new Position(currLoc.getX()-1,currLoc.getY()))) {
            moveLeft();
        }

        sasiad =0;
        if(check(new Position(currLoc.getX(),currLoc.getY()+1))) sasiad +=1;
        if(check(new Position(currLoc.getX()+1,currLoc.getY()+1))) sasiad +=1;
        if(check(new Position(currLoc.getX()-1,currLoc.getY()+1))) sasiad +=1;
    }


    /**
     * Funkcja zwracajaca pozycje Aktora
     * @return pozycja
     */
    public Position getPosition() {
        return currLoc;
    }

    /**
     * Funkcje zmieniajace pozycje w zależności od ruchu
     */
    public void moveUp(){
        this.currLoc = new Position(currLoc.getX(), currLoc.getY()-1);
    }

    public void moveDown(){
        this.currLoc = new Position(currLoc.getX(), currLoc.getY()+1);
    }

    public void moveRight(){
        this.currLoc = new Position(currLoc.getX()+1, currLoc.getY());
    }

    public void moveLeft(){
        this.currLoc = new Position(currLoc.getX()-1, currLoc.getY());
    }

    public void moveTopRight(){
        this.currLoc = new Position(currLoc.getX()+1, currLoc.getY()-1);
    }

    public void moveTopLeft(){
        this.currLoc = new Position(currLoc.getX()-1, currLoc.getY()-1);
    }

    public void moveBotRight(){
        this.currLoc = new Position(currLoc.getX()+1, currLoc.getY()+1);
    }

    public void moveBotLeft(){
        this.currLoc = new Position(currLoc.getX()-1, currLoc.getY()+1);
    }
}
