package metier;

import java.util.*;

public class PyRat {
    private Map<Point, List<Point>> laby;
    private List<Point> fromages;
    private Boolean[][] boolFromages;
    private Map<Point,Boolean[][]> boolAccessiblePointsMap;
    private Map<Point,List<Point>> unaccessiblePointsMap;

    /* Méthode appelée une seule fois permettant d'effectuer des traitements "lourds" afin d'augmenter la performace de la méthode turn. */
    public void preprocessing(Map<Point, List<Point>> laby, int labyWidth, int labyHeight, Point position, List<Point> fromages) {
        this.fromages = fromages;
        this.laby = laby;
        // Construction of a boolean array who indicate the x,y position of all the cheeses
        boolFromages = new Boolean[labyWidth][labyHeight];
        for (Point p: fromages) {
            boolFromages[p.getX()][p.getY()] = true;
        }

        // Construction of a boolean array in a map who indicate all the accesible cells around one particular cell.
        this.boolAccessiblePointsMap = new HashMap<>();
        for (Point pKey: this.laby.keySet()) {
            Boolean[][] boolAccessiblePoints = new Boolean[labyWidth][labyHeight];
            for (Point p :this.laby.get(pKey)) {
                boolAccessiblePoints[p.getX()][p.getY()] = true;
            }
            boolAccessiblePointsMap.put(pKey, boolAccessiblePoints);
        }

        unaccessiblePointsMap = new HashMap<>();
        for(Point p: laby.keySet()) {
            List<Point> accessiblePoints = laby.get(p);
            List<Point> unaccessiblePoints = new ArrayList<>(laby.keySet());
            for (Point p2 : accessiblePoints) {
                unaccessiblePoints.remove(p2);
            }
            unaccessiblePointsMap.put(p, unaccessiblePoints);
        }
    }

    /* Méthode de test appelant les différentes fonctionnalités à développer.
        @param laby - Map<Point, List<Point>> contenant tout le labyrinthe, c'est-à-dire la liste des Points, et les Points en relation (passages existants)
        @param labyWidth, labyHeight - largeur et hauteur du labyrinthe
        @param position - Point contenant la position actuelle du joueur
        @param fromages - List<Point> contenant la liste de tous les Points contenant un fromage. */
    public void turn(Map<Point, List<Point>> laby, int labyWidth, int labyHeight, Point position, List<Point> fromages) {
        Point pt1 = new Point(2,1);
        Point pt2 = new Point(3,1);
        System.out.println((fromageIci(pt1) ? "Il y a un" : "Il n'y a pas de") + " fromage ici, en position " + pt1);
        System.out.println((fromageIci_EnOrdreConstant(pt2) ? "Il y a un" : "Il n'y a pas de") + " fromage ici, en position " + pt2);
        System.out.println((passagePossible(pt1, pt2) ? "Il y a un" : "Il n'y a pas de") + " passage de " + pt1 + " vers " + pt2);
        System.out.println((passagePossible_EnOrdreConstant(pt1, pt2) ? "Il y a un" : "Il n'y a pas de") + " passage de " + pt1 + " vers " + pt2);
        System.out.println("Liste des points inatteignables depuis la position " + position + " : " + pointsInatteignables(position));
    }

    /* Regarde dans la liste des fromages s’il y a un fromage à la position pos.
        @return true s'il y a un fromage à la position pos, false sinon. */
    private boolean fromageIci(Point pos) {
        return fromages.contains(pos);
    }

    /* Regarde de manière performante (accès en ordre constant) s’il y a un fromage à la position pos.
        @return true s'il y a un fromage à la position pos, false sinon. */
    private boolean fromageIci_EnOrdreConstant(Point pos) {
        return boolFromages[pos.getX()][pos.getY()];
    }

    /* Indique si le joueur peut passer de la position (du Point) « de » au point « a ».
        @return true s'il y a un passage depuis  « de » vers « a ». */
    private boolean passagePossible(Point de, Point a) {
        List<Point> accessiblePoints = this.laby.get(de);
        return accessiblePoints.contains(a);
    }

    /* Indique si le joueur peut passer de la position (du Point) « de » au point « a »,
        mais sans devoir parcourir la liste des Points se trouvant dans la Map !
        @return true s'il y a un passage depuis  « de » vers « a ». */
    private boolean passagePossible_EnOrdreConstant(Point de, Point a) {
        Boolean[][] accessiblePoints = this.boolAccessiblePointsMap.get(de);
        return accessiblePoints[a.getX()][a.getY()];
    }

    /* Retourne la liste des points qui ne peuvent pas être atteints depuis la position « pos ».
        @return la liste des points qui ne peuvent pas être atteints depuis la position « pos ». */
    private List<Point> pointsInatteignables(Point pos) {
        return unaccessiblePointsMap.get(pos);
    }
}