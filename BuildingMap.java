import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Klasa reprezentująca mapę Budynków
 */
public class BuildingMap {

    /**
     * Parametry klasy:
     * n - wymiar tablicy
     * tab - tablica Budynków
     */
    private int n;
    private int tab[][];

    /**
     * Konstruktor klasy
     * @param n wymiar
     */
    private BuildingMap(int n){
        this.n=n;
        tab=new int [n][n];
    }


    /**
     * Funkcja zwraca wymiar tablicy
     * @return wymiar
     */
    public int getSize(){
        return n;
    }

    /**
     * Funkcja sprawdza czy pozycja jest wolna
     * @param p pozycja
     * @return jeśli wolne to true, inaczej false
     */
    public boolean isFree(Position p) {  //sprawdzenie, czy pole jest puste, czy zaj�te
        return tab[p.getX()][p.getY()] == 0;
    }

    /**
     * Funkcja tworzy mapę Budynków
     * @param n wymiar
     * @param buildings lista Budynków
     * @return mapa
     */
    public static BuildingMap newBuildingMap(int n, ArrayList<Building> buildings){
        BuildingMap bm=new BuildingMap(n);

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                bm.tab[i][j]=0;
            }
        }
        bm.addBuildingsToMap(buildings);

        return bm;
    }

    /**
     * Funkcja dodaje liste Budynków do mapy
     * @param buildings lista Budynków
     */
    public void addBuildingsToMap(ArrayList<Building> buildings){
        for(Building b : buildings){
            addBuilding(b);
        }
    }

    /**
     * Funkcja dodaje Budynek do mapy
     * @param building Budynek
     */
    public void addBuilding(Building building){
        int x = building.getPosition().getX();
        int y = building.getPosition().getY();
        int w = building.getWidth();
        int h = building.getHeight();

        for(int i=x;i<x+w;i++){
            for(int j=y;j<y+h;j++){
                tab[i][j]=1;
            }
        }
    }

    /**
     * Funkcja znajduje najkrótszą ścieżkę na mapie
     * @param source pozycja startowa
     * @param target pozycja docelowa
     * @return lista pozycji
     */
    public LinkedList<Position> findShortestPath (Position source, Position target) {
        Position[][] previousPosition = new Position[n][n];
        Queue<Position> queue = new LinkedList<Position>();
        previousPosition[source.getX()][source.getY()] = source;
        queue.add(source);

        while(!queue.isEmpty()) {
            Position position = queue.poll();
            int x = position.getX();
            int y = position.getY();

            if(x>0 && tab[x-1][y] == 0 && previousPosition[x-1][y] == null) {
                previousPosition[x-1][y] = position;
                Position newPos = new Position(x-1,y);
                if (newPos.equals(target)) {
                    break;
                }
                queue.add(newPos);
            }
            if(x<n-1 && tab[x+1][y] == 0 && previousPosition[x+1][y] == null) {
                previousPosition[x+1][y] = position;
                Position newPos = new Position(x+1,y);
                if (newPos.equals(target)) {
                    break;
                }
                queue.add(newPos);
            }
            if(y>0 && tab[x][y-1] == 0 && previousPosition[x][y-1] == null) {
                previousPosition[x][y-1] = position;
                Position newPos = new Position(x,y-1);
                if (newPos.equals(target)) {
                    break;
                }
                queue.add(newPos);
            }
            if(y<n-1 && tab[x][y+1] == 0 && previousPosition[x][y+1] == null) {
                previousPosition[x][y+1] = position;
                Position newPos = new Position(x,y+1);
                if (newPos.equals(target)) {
                    break;
                }
                queue.add(newPos);
            }
            if(x>0 && y>0 && tab[x-1][y-1] == 0 && previousPosition[x-1][y-1] == null) {
                previousPosition[x-1][y-1] = position;
                Position newPos = new Position(x-1,y-1);
                if (newPos.equals(target)) {
                    break;
                }
                queue.add(newPos);
            }
            if(x<n-1 && y>0 && tab[x+1][y-1] == 0 && previousPosition[x+1][y-1] == null) {
                previousPosition[x+1][y-1] = position;
                Position newPos = new Position(x+1,y-1);
                if (newPos.equals(target)) {
                    break;
                }
                queue.add(newPos);
            }
            if(x>0 && y<n-1 && tab[x-1][y+1] == 0 && previousPosition[x-1][y+1] == null) {
                previousPosition[x-1][y+1] = position;
                Position newPos = new Position(x-1,y+1);
                if (newPos.equals(target)) {
                    break;
                }
                queue.add(newPos);
            }
            if(x<n-1 && y<n-1 && tab[x+1][y+1] == 0 && previousPosition[x+1][y+1] == null) {
                previousPosition[x+1][y+1] = position;
                Position newPos = new Position(x+1,y+1);
                if (newPos.equals(target)) {
                    break;
                }
                queue.add(newPos);
            }
        }

        LinkedList<Position> result = new LinkedList<Position>();
        Position p = target;
        while(!p.equals(source)) {
            result.add(0,p);
            p = previousPosition[p.getX()][p.getY()];
        }

        return result;

    }



}
