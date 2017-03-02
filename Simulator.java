import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Klasa Symulator dziedziczy po Simulation - tam potrzebne funkcje
 */
public class Simulator extends Simulation
{
    /**
     * Parametry klasy:
     * g - grafika;
     * Liczba, Licczba_policji, Atakujący - liczba Aktorów, Policji, Atakujących
     * Z - zagęszczenie, potrzebne do funkcji prawdopodobieństwa
     * przesunięcie - przesunięcie w prawo na ulicy
     */
    private Graphics g;
    final int Liczba = 100;
    final int  Liczba_policji = 33+Liczba+8;
    final int Atakujacy = 20;
    final double Z = (double)(94+Liczba)/(double)((Liczba/2+7)*3);
    int przesuniecie = 1350;

    /**
     * Funkcje odpowiadająca za symulację
     * ustawiany jest wygląd okna i start całej symulacji, Atakujący zaczynają działanie gdy
     * prawdopodobieństwo ataku > 0.2
     * @param ac lista Aktorów
     * @param b lista Budynków
     * @param test tablica rodzin
     * @param leader tablica liderów rodzin
     * @param pol lista Policjantów
     * @param riot lista Atakujących
     * @param first Policjanci z przodu
     * @param last Policjanci z tyłu
     * @param left Policjanci po lewej
     * @param right Policjanci po prawej
     * @throws InterruptedException
     */
    public void screen(ArrayList<Actor> ac, ArrayList<Building> b,ArrayList[] test,int [] leader,ArrayList<Police> pol,
                       ArrayList<Riot> riot,ArrayList<Police> first,ArrayList<Police> last,
                        ArrayList<Police> left, ArrayList<Police> right) throws InterruptedException {

        final JFrame f = new JFrame("Symulacja");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Screen s = new Screen(ac,b,pol,riot);
        s.setPreferredSize(new Dimension( 10000,10000));
        JScrollPane scrollFrame = new JScrollPane(s);
        s.setAutoscrolls(true);
        scrollFrame.setPreferredSize(new Dimension( 1600,1600));

        f.setSize(1000,500);
        f.add(scrollFrame);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                f.setVisible(true);
            }
        });


        LinkedList<Position> pozycje = new LinkedList<>();

        double prop = Prop(riot);
        /**
         * Ile ruchów podczas ataku
         */
        int licznik_max = 20;
        int licznik = 0;

        while(true) {
            prop = Prop(riot);
            if(prop<0.2) {
                manifestMove(first,left,right,last,ac,test,leader,f,Liczba);
            }
            else {
                double prop2 = prop*100;
                prop2 = 100-prop2;

                riotMove(licznik,licznik_max,riot,first,pozycje,pol,prop2,f);
                licznik +=1;
            }


        }



    }


    /**
     * Funkcja która przygotowuje do symulacji
     * ustawienie wszystkich elementów na mapie i przygotowanie wszystkich uczestników
     * do startu manifestacji
     * @throws InterruptedException wyrzucony błąd
     */
    public void start() throws InterruptedException {

        ArrayList<Actor> ac = new ArrayList<>();
        ArrayList<Police> pol = new ArrayList<>();
        ArrayList[] test = new ArrayList[Liczba/2+1];
        ArrayList<Police> first = new ArrayList<>();
        ArrayList<Police> last = new ArrayList<>();
        ArrayList<Police> left = new ArrayList<>();
        ArrayList<Police> right = new ArrayList<>();
        ArrayList<Riot> riot = new ArrayList<>();
        int [] position = new int[Liczba/2];
        for (int m=0;m<Liczba/2+1;m++) {
            test[m] = new ArrayList<Actor>();
        }

        int leader[] = new int[Liczba/2];

        /**
         * Przygotowanie budynków
         */
        ArrayList<Building> b = new ArrayList<>();

        b.add(new Building(new Position(0,3),1410,4));
        b.add(new Building(new Position(0,31),1410,4));
        b.add(new Building(new Position(1410,3),404,4));
        b.add(new Building(new Position(1406,31),4,368));
        b.add(new Building(new Position(1810,3),4,395));
        b.add(new Building(new Position(1350,383),60,4));
        b.add(new Building(new Position(1380,397),434,4));


        BuildingMap bm=BuildingMap.newBuildingMap(3000,b);

        /**
         * Przygotowanie Aktorów
         */
        Random randomGenerator = new Random();
        Actor ak = new Actor(new Position(Liczba/4+3+przesuniecie,15), new Position(100+przesuniecie,15), ac, bm,pol,riot,Liczba/2);
        ac.add(ak);
        test[Liczba/2].add(ak);
        ac.get(0).setFamily(test);

        int j =0;
        while (j<Liczba){
            int f = randomGenerator.nextInt(Liczba/2);
            int c = randomGenerator.nextInt(17)+11;
            int a = 0;
            if(test[f].size()<3) {
                if (test[f].size() == 0) {
                    a = randomGenerator.nextInt(Liczba/4-5) + 5;
                    a = a+przesuniecie+5;

                } else {
                    boolean p = true;
                    int k = position[f];
                    while (p) {
                        a = randomGenerator.nextInt(10) + (k - 5);//warunek
                        if (a >= 5) p = false;
                        else a=a+przesuniecie+5;
                    }
                }

                if (checkPoint(ac, a, c, b)) {
                    Actor ak2 = new Actor(new Position(a, c), new Position(0, 0), ac, bm,pol,riot, f);
                    test[f].add(ak2);
                    ac.add(ak2);
                    j++;

                    if (test[f].size() == 1) position[f] = a;
                }
            }
        }

        setLeaders(test,ac,leader,Liczba);

        /**
         * Przygotowanie Policjantów
         */

        for (int l=9;l<29;l++) {

            Police p = new Police(new Position(Liczba/4+12+przesuniecie,l), new Position(100+przesuniecie,l), ac, bm, pol,riot);
            pol.add(p);
            first.add(p);
            p = new Police(new Position(Liczba/4+11+przesuniecie,l), new Position(100+przesuniecie,l), ac, bm, pol,riot);
            pol.add(p);
            first.add(p);
            p = new Police(new Position(Liczba/4+10+przesuniecie,l), new Position(100+przesuniecie,l), ac, bm, pol,riot);
            pol.add(p);
            first.add(p);


        }

        for(int i=0;i<3;i++) {
            Police pl = new Police(new Position(Liczba/4 + 9-i + przesuniecie, 10), new Position(100 + przesuniecie, 10), ac, bm, pol, riot);
            pol.add(pl);
            first.add(pl);
            pl = new Police(new Position(Liczba/4 + 9-i + przesuniecie, 27), new Position(100 + przesuniecie, 10), ac, bm, pol, riot);
            pol.add(pl);
            first.add(pl);
        }

        for(int l=Liczba/4+9;l>0;l--){
            Police p = new Police(new Position(l+przesuniecie,9), new Position(100+przesuniecie,8), ac, bm, pol,riot);
            pol.add(p);
            left.add(p);
            p = new Police(new Position(l+przesuniecie,28), new Position(100+przesuniecie,18), ac, bm, pol,riot);
            pol.add(p);
            right.add(p);

        }

        for (int l=9;l<29;l++) {

            Police p = new Police(new Position(przesuniecie,l), new Position(100+przesuniecie,l), ac, bm, pol,riot);
            pol.add(p);
            last.add(p);
        }

        int w =0;
        while(w<Atakujacy){
            int poz = Atakujacy/6;
            int a = randomGenerator.nextInt(poz)+1410;
            int c = randomGenerator.nextInt(21)+8;
            if(checkPoint(riot,a,c)){
                Riot r = new Riot(new Position(a,c),new Position(0,0),ac,bm,pol,riot);
                r.setParametres(Z,1,0.25,0.25,Liczba_policji,Atakujacy,first);
                riot.add(r);
                w++;
            }
        }

        screen(ac,b,test,leader,pol,riot,first,left,right,last);



    }

}