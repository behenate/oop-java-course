package agh.ics.oop;

public class Animal {
    private MapDirection mapDirection = MapDirection.NORTH;
    private Vector2d position;
    private IWorldMap map;
    public Animal(IWorldMap map, Vector2d initialPosition){
        this.map = map;
        this.position = initialPosition;
        this.map.place(this);
    }
    @Override
    public String toString() {
        return switch (mapDirection){
            case NORTH -> "N";
            case SOUTH -> "S";
            case EAST -> "E";
            case WEST -> "W";
        };
    }
    private Vector2d dirToVector(){
        Vector2d vector = switch (mapDirection){
            case NORTH -> new Vector2d(0,1);
            case SOUTH -> new Vector2d(0, -1);
            case EAST -> new Vector2d(1,0);
            case WEST -> new Vector2d(-1,0);
        };
        return vector;
    }
    public void move(MoveDirection direction){
        Vector2d newPos = position;
        switch (direction){
            case LEFT -> mapDirection = mapDirection.previous();
            case RIGHT -> mapDirection = mapDirection.next();
            case FORWARD -> newPos = position.add(dirToVector());
            case BACKWARD -> newPos = position.add(dirToVector().opposite());
        }
        if (map.canMoveTo(newPos)){
            position = newPos;
        }
    }
    public MapDirection getDirection(){
        return mapDirection;
    }
    public Vector2d getPosition(){
        return position;
    }
}
