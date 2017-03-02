import javax.swing.*;
import java.util.*;

/**
 * Klasa posiadająca wszystkie funkcje do symulacji
 */
public class Simulation {

    /**
     * Funkcja znajduje najbliższego Policjanta
     * @param r Atakujący
     * @param first lista Policji z przodu
     * @return
     */
    public Police findPolician(Riot r, ArrayList<Police> first){
        double d = Double.MAX_VALUE;
        Police police = null;
        for(Police p:first){
            double d2 = r.farFromPolice(p);
            if(d2<d && p.catchRiot==false) {
                police = p;
                d=d2;
            }
        }
        return police;
    }

    /**
     * Funkcja odpowiada za przejmowanie Atakujących przez Policję
     * przejmowani jesli odleglosc miedzy Atakującym i Policją <3
     * @param riot lista Atakujących
     * @param first lista Policjantów z przodu
     * @param pozycje lista Pozycji aby uaktualnić Policjantów na przodzie
     */
    public void stop(ArrayList<Riot> riot, ArrayList<Police> first, LinkedList<Position> pozycje){

        for(Riot r:riot) {
            if (r.farFromPolice(r.polician)<3) {
                r.catched=true;
                Police p = findPolician(r,first);
                p.catchRiot = true;
                p.targetLoc = r.getPosition();
                p.catched = r;
                if(p.getPosition().getY()>15)  r.targetLoc = new Position(r.getPosition().getX(),30);
                else r.targetLoc = new Position(r.getPosition().getX(),7);
                r.addNewTarget(new Position(0,15));
                pozycje.add(p.getPosition());
            }
            else {
                r.escape = true;
                r.powrot();
            }
        }
    }

    /**
     * Funkcja aktualizuje Policjantów na przodzie jeśli cześć odejdzie
     * z Atakującymi, wypełnienie tak aby z przodu zawsze był pełny rząd
     * @param police lista Policjantów
     * @param position lista Pozycji
     * @param f grafika
     */
     public void actualizationPolice(ArrayList<Police> police, LinkedList<Position> position,JFrame f){


         for(Police p: police){

             for(Position pos:position){

                 if(p.getPosition().getY()==pos.getY() && p.getPosition().getX()+1==pos.getX()){

                     Position temp = p.targetLoc;
                     p.targetLoc=pos;
                     ruch(p,f);
                     p.targetLoc = temp;

                 }
             }
         }

     }

    /**
     * Funkcja odpowiada za ruch podczas ataku
     * jeśli jest stop to część osób idzie z policją,
     * a reszta się wycofuje, aktualizacja Policji
     * @param licznik numer ruchu
     * @param licznik_max max ilosc ruchów
     * @param riot lista Atakujących
     * @param first lista Policjantów z przodu
     * @param pozycje lista pozycji
     * @param pol lista Policjantów
     * @param prop prawdopodobieństwo ataku
     * @param f grafika
     */
     public void riotMove(int licznik,int licznik_max,ArrayList<Riot> riot,ArrayList<Police> first,LinkedList<Position> pozycje,
                          ArrayList<Police> pol,double prop,JFrame f) {
         if (licznik == licznik_max) {
             stop(riot, first, pozycje);

         } else {
             riotAtack(riot, pol, prop, f);
         }

         if (licznik >= licznik_max) {
             for (Police p : first) {
                 if (p.catchRiot) {
                     if (p.catched.farFromPolice(p) >= 2) {
                         ruch(p, f);
                         actualizationPolice(first, pozycje, f);
                     }
                     ruch(p.catched, f);
                     ruch(p, f);
                 }
             }
             for(Riot r:riot){
                 if(r.escape) {
                     r.targetLoc= new Position(1415,50);
                     ruch(r, f);
                 }
             }

             actualizationPolice(first, pozycje, f);
             for (Position p : pozycje) {
                 p.setX(p.getX() - 1);
             }
             actualizationPolice(first, pozycje, f);
         }
     }

    /**
     * Funkcja odpowiada za ruch podczas manifestacji
     * Ruszamy wszystkich uczestników, jeśli Aktor jest zadowolony to wykonuje
     * kolejny ruch, a jeśli dodatkowo jest za duża odległość w manifestacji
     * to jeszcze jeden ruch, raadość jest redukowana w każdej iteracji
     * @param first lista Policjantów z przodu
     * @param left z lewej
     * @param right z prawej
     * @param last z tyłu
     * @param ac lista Aktorów
     * @param test tablica rodzin
     * @param leader tablica liderów
     * @param f grafika
     * @param Liczba liczba Aktorów
     */
    public void manifestMove(ArrayList<Police> first,ArrayList<Police> left,ArrayList<Police> right,ArrayList<Police> last,ArrayList<Actor> ac,ArrayList[] test,int [] leader, JFrame f,int Liczba){

        for(Police p:first){
            ruch(p,f);
        }

        for(Police p:left){
            ruch(p,f);
        }

        for(Police p:last){
            ruch(p,f);
        }

        keepCalm(ac);
        Random rand = new Random();
        int ak = rand.nextInt(Liczba)+1;
        int ang = rand.nextInt(5)+1;
        ActorHappy(ac,ak,ang);

        for (Actor actor : ac) {
            ruch(actor,f);
        }

        setLeaders(test,ac,leader,Liczba);

        for (Actor actor:ac){
            if(actor.getHappy()>0) ruch(actor,f);
        }

        setLeaders(test,ac,leader,Liczba);

        for (Actor actor:ac){
            if(actor.family.get(actor.leader).sasiad>=2 && ac.get(0)!=actor){
                ruch(actor,f);
            }
        }

        setLeaders(test,ac,leader,Liczba);


        for(Police p:right){
            ruch(p,f);

        }


    }


    /**
     * Funkcja odpowiada za atak, Atakujący zbliżają się
     * do Policji, jeśli blisko to wycofanie o 2 pola i ponowny atak
     * @param riot lista Atakujących
     * @param police lista Policjantów
     * @param prop prawdopodobieństwo
     * @param f grafika
     */
    public void riotAtack(ArrayList<Riot> riot,ArrayList<Police> police, double prop,JFrame f){
        for (Riot r : riot) {
            if (!r.catched && !r.escape) {
                if (r.attackSusceptibility >= prop) {

                    if (r.farFromPolice(r.polician) != 1)
                        if (r.cofnij == false) ruch(r, f);
                        else {
                            r.cofnij();
                            f.getContentPane().validate();
                            f.getContentPane().repaint();
                            r.cofnij = false;
                        }

                    else {
                        r.cofnij();
                        f.getContentPane().validate();
                        f.getContentPane().repaint();
                        r.cofnij = true;
                    }
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Funkcja odpowiada za ruch Aktora
     * @param actor aktor
     * @param f grafika
     */
    public void ruch(Actor actor, JFrame f){
        actor.move();
        f.getContentPane().validate();
        f.getContentPane().repaint();

    }

    /**
     * Funkcja uspokaja Aktorów
     * @param ac lista Aktorów
     */
    public void keepCalm(ArrayList<Actor> ac){
        for (Actor a: ac){
            if(a.getHappy()>0) a.setHappy(a.getHappy()-1);
        }
    }

    /**
     * Funkcja odpowiada za ustawienie radości rodzin rekurencyjnie
     * każda sąsiednia rodzina ma wartość -1
     * @param ac lista Aktorów
     * @param ak numer rodziny
     * @param ang radość
     */
    public void ActorHappy (ArrayList<Actor> ac,int ak,int ang){

        boolean b = setHappy(ac,ak,ang);
        if(b==true && ang>1){
            ArrayList<Integer> l = findFamily(ac,ak);
            for (int m=0;m<l.size();m++){
                ActorHappy(ac,l.get(m),ang-1);
            }
        }
    }

    /**
     * Funkcja odpowiada za ustawienie radości w  rodzinie
     * @param ac lista Aktorów
     * @param ak numer rodziny
     * @param ang radość
     * @return true jeśli ustawiono, inaczej false
     */
    public boolean setHappy(ArrayList<Actor> ac,int ak,int ang ){
        for (Actor a:ac.get(ak).family){
            if(a.getHappy()==0) a.setHappy(ang);
            else return false;
        }
        return true;
    }

    /**
     * Funkcja znajduje sąsiednie rodziny dla Aktora
     * @param ac lista Aktorów
     * @param n numer rodziny dla której szukamy
     * @return lista rodzin
     */
    public ArrayList<Integer> findFamily(ArrayList<Actor> ac,int n){
        ArrayList<Integer> list = new ArrayList<>();
        int fam = ac.get(n).getF();
        for(Actor a:ac){
            if(a.getF()!=fam){
                for(Actor ak:ac.get(n).family){
                    Position p = a.getPosition();
                    Position p2 = ak.getPosition();
                    double d2 = Math.pow((p.getX()-p2.getX()),2)+Math.pow((p.getY()-p2.getY()),2);
                    if(d2<=2) {list.add(a.getF()); break;}
                }
            }
        }
        return list;
    }

    /**
     * Funkcja ustawia lidera dla Aktorów
     * @param test tablica rodzin
     * @param ac lista Aktorów
     * @param leader tablica liderów
     * @param Liczba liczba Aktorów
     */
    public void setLeaders(ArrayList[] test, ArrayList<Actor> ac,int [] leader,int Liczba){
        for(int i=0;i<Liczba/2;i++){
            int k = checkLeader(test[i],ac);
            leader[i]=k;

        }

        for (int i=1;i<Liczba+1;i++)
        {
            ac.get(i).setFamily(test);
            ac.get(i).setLeader(leader);

        }
    }

    /**
     * Funkcja wyszukuje lidera, Aktor najbliżej celu
     * @param family lista rodziny
     * @param ac lista Aktorów
     * @return indeks lidera
     */
    public int checkLeader(ArrayList<Actor> family, ArrayList<Actor> ac){
        double d = Double.MAX_VALUE;
        int l = 0;
        Position p = ac.get(0).getPosition();
        for(int i=0;i<family.size();i++){
            Position p2 = family.get(i).getPosition();
            double d2 = Math.pow((p.getX()-p2.getX()),2)+Math.pow((p.getY()-p2.getY()),2);
            if(d2<d) {d=d2; l=i;}

        }

        return l;
    }

    /**
     * Funkcja sprawdza czy punkt jest wolny
     * @param ac lista Aktorów
     * @param x współrzędna x
     * @param y współrzędna y
     * @param bl lista budynków
     * @return true jeśli punkt wolny, inaczej false
     */
    public boolean checkPoint(ArrayList<Actor> ac, int x, int y, ArrayList<Building> bl)
    {
        for (Actor a:ac) {
            if(a.getPosition().getX()==x && a.getPosition().getY()==y) return false;
        }
        for (Building b:bl){
            int px = b.getPosition().getX();
            int py = b.getPosition().getY();
            int w = b.getWidth();
            int h = b.getHeight();
            if(x>=px && x<(px+w) && y>=py && y<(py+h)) return false;
        }
        return true;
    }

    /**
     * Funkcja sprawdza czy punkt jest dostepny ale dla Atakujacych
     * @param riot lista Atakujących
     * @param x współrzędna x
     * @param y współrzędna y
     * @return true jeśli wolne, inaczej false
     */
    public boolean checkPoint(ArrayList<Riot> riot, int x, int y){
        for (Riot a:riot) {
            if(a.getPosition().getX()==x && a.getPosition().getY()==y) return false;
        }
        return true;
    }


    /**
     * Funkcja wylicza całkowite prawdopodobieństwo Ataku
     * @param riot lista Atakujących
     * @return prawdopodobieństwo
     */
    public double Prop(ArrayList<Riot> riot){
        double pr =0;
        for(Riot r:riot){
            if(!r.catched) {
                r.attractive();
                pr = pr + r.attack;
            }

        }
        double prop = (pr/(1+pr));
        return prop;
    }
}
