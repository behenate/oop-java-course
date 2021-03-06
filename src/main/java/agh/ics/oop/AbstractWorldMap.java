package agh.ics.oop;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    protected final HashMap<Vector2d, IMapElement> mapElements = new LinkedHashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final MapBoundary boundary = new MapBoundary();
    private final ArrayList<Label> coordinateLabels = new ArrayList<>();
    public boolean place(Animal animal) throws IllegalArgumentException {
        if (!canMoveTo(animal.getPosition())){
            throw new IllegalArgumentException("Pole " + animal.getPosition() + " nie jest dobrym polem dla zwierzaka!");
        }
        animal.addObserver(this);
        animal.addObserver(boundary);
        boundary.positionChanged(animal.getPosition(), animal.getPosition());
        mapElements.put(animal.getPosition(), animal);
        return true;
    }


    public Object objectAt(Vector2d position) {
        return mapElements.get(position);
    }


    public boolean isOccupied(Vector2d position) {
        return mapElements.get(position) != null;
    }

    public String toString() {
        return visualizer.draw(boundary.lowerLeft(), boundary.upperRight());
    }

    public boolean canMoveTo(Vector2d position){
        return !(isOccupied(position) && objectAt(position) instanceof Animal);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        IMapElement movedElement = mapElements.get(oldPosition);
        mapElements.remove(oldPosition);
        mapElements.put(newPosition, movedElement);
    }
    //Zamienia realną pozycję czegoś na odpowiednią pozycję na mapie
    private Vector2d fixPos(Vector2d pos, boolean flipY){
        Vector2d ll = boundary.lowerLeft();
        Vector2d ur = boundary.upperRight();
        Vector2d o = new Vector2d(0,0);
        if (ll.x < 0)
            o = o.add(new Vector2d(-ll.x, 0));
        if (ll.y < 0)
            o = o.add(new Vector2d(0, -ll.y));
        if (flipY)
            return new Vector2d(pos.x + o.x, ur.y - (pos.y-ll.y) + o.y);
        return new Vector2d(pos.x + o.x, pos.y + o.y);
    }

    public GridPane generateGrid(){
        GridPane gridPane = new GridPane();
        Vector2d ll = boundary.lowerLeft();
        Vector2d ur = boundary.upperRight();
        gridPane.setGridLinesVisible(true);
        for (Label label:coordinateLabels) {
            gridPane.getChildren().remove(label);
        }
        Label newLabel = new Label("y/x");
        Vector2d initLabelPos = fixPos(new Vector2d(ll.x, ll.y), false);
        gridPane.add(newLabel, initLabelPos.x, initLabelPos.y,1, 1);
        coordinateLabels.add(newLabel);
        for (int i = 0; i < ur.x-ll.x+2; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(30));
            gridPane.getRowConstraints().add(new RowConstraints(30));
        }
        GridPane.setHalignment(newLabel, HPos.CENTER);
        for (int i = 0; i < ur.x-ll.x+1; i++){
            Vector2d pos = new Vector2d(ll.x+i, ll.y);
            Vector2d posFixed = fixPos(pos, false);
            newLabel = new Label(Integer.toString(ll.x+i));
            gridPane.add(newLabel, posFixed.x+1, posFixed.y ,1, 1);
            coordinateLabels.add(newLabel);
            GridPane.setHalignment(newLabel, HPos.CENTER);
        }

        for (int i = 0; i < ur.y-ll.y+1; i++){
            Vector2d pos = new Vector2d(ll.x, ll.y+i);
            Vector2d posFixed = fixPos(pos, false);
            newLabel = new Label(Integer.toString(ur.y-i));
            gridPane.add(newLabel, posFixed.x, posFixed.y+1 ,1, 1);
            coordinateLabels.add(newLabel);
            GridPane.setHalignment(newLabel, HPos.CENTER);
        }
        for (int i = 0; i < ur.y-ll.y+1; i++) {
            for (int j = 0; j < ur.x-ll.x+1; j++) {
                Vector2d pos = new Vector2d(ll.x+j, ll.y+i);
                Vector2d posFixed = fixPos(pos, true);
                if (isOccupied(pos)){
                    IMapElement elem = (IMapElement) objectAt(pos);
                    newLabel = new Label(elem.toString());
                    gridPane.add(newLabel, posFixed.x + 1, posFixed.y + 1,1, 1);
                    GridPane.setHalignment(newLabel, HPos.CENTER);
                }
            }
        }
        return gridPane;
    }
    abstract Vector2d[] calculateBounds();

}
