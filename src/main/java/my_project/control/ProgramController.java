package my_project.control;

import KAGO_framework.control.Interactable;
import KAGO_framework.control.ViewController;
import my_project.model.InteractableAdapter;
import my_project.view.Visual2DArray;
import my_project.view.example.Ball;

import java.awt.event.MouseEvent;

/**
 * Ein Objekt der Klasse ProgramController dient dazu das Programm zu steuern. Die updateProgram - Methode wird
 * mit jeder Frame im laufenden Programm aufgerufen.
 */
public class ProgramController {


    private final ViewController viewController;  // diese Referenz soll auf ein Objekt der Klasse viewController zeigen. Über dieses Objekt wird das Fenster gesteuert.

    /**
     * Konstruktor
     * Dieser legt das Objekt der Klasse ProgramController an, das den Programmfluss steuert.
     * Damit der ProgramController auf das Fenster zugreifen kann, benötigt er eine Referenz auf das Objekt
     * der Klasse viewController. Diese wird als Parameter übergeben.
     * @param viewController das viewController-Objekt des Programms
     */
    public ProgramController(ViewController viewController){
        this.viewController = viewController;
    }

    /**
     * Diese Methode wird genau ein mal nach Programmstart aufgerufen.
     * Sie erstellt die leeren Datenstrukturen, zu Beginn nur eine Queue
     */
    public void startProgram() {
        var arr = new Visual2DArray<Ball>(5, 10);
        arr.set(new Ball(), 2, 4);
        arr.set(new Ball(), 0, 1);
        viewController.draw(arr);
        viewController.register(arr);

        arr.addController(new InteractableAdapter() {
            @Override
            public void keyPressed(int key) {
                switch (key) {
                    // too bored
                }
            }
        });
    }

    /**
     * Aufruf mit jeder Frame
     * @param dt Zeit seit letzter Frame
     */
    public void updateProgram(double dt){}

    public void mouseClicked(MouseEvent e){}
}
