package agh.ics.oop;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class SimulationEngine implements IEngine{
    private final ArrayList<MoveDirection> moveArray;
    private final ArrayList<Animal> animals;
    private final IWorldMap map;
    public SimulationEngine(ArrayList<MoveDirection> moveArray, IWorldMap map, Vector2d[] initialPositions){
        this.moveArray = moveArray;
        this.animals = map.getAnimals();
        this.map = map;
        for (Vector2d position: initialPositions) {
            new Animal(map, position);
        }
    }

    @Override
    public void run() {
        int animalIndex = 0;
        for (MoveDirection move: moveArray) {
            animals.get(animalIndex).move(move);
            animalIndex = (animalIndex + 1) % animals.size();
        }
    }

//    Część z zadania dla chętnych
    public void runAndShow() {
        MapVisualizer visualizer = new MapVisualizer(map);
        JFrame f=new JFrame();
        f.setSize(1000,1000);
        JTextArea b = new JTextArea(map.toString());//creating instance of JButton
        b.setBounds(50,50,900, 900);
        Font font = b.getFont();
        float size = font.getSize() + 5.0f;
        b.setFont( font.deriveFont(size) );
        f.add(b);
        f.setLayout(null);
        f.setVisible(true);
        int animalIndex = 0;
        for (MoveDirection move: moveArray) {
            try {
                Thread.sleep(700);
            }catch (InterruptedException e){
                System.out.println("blabla cos nie dziala");
            };
            animals.get(animalIndex).move(move);
            animalIndex = (animalIndex + 1) % animals.size();
            b.setText(visualizer.draw(new Vector2d(0,0), new Vector2d(10,5)));
            f.revalidate();
            f.repaint();
        }
        f.dispose();
    }
}