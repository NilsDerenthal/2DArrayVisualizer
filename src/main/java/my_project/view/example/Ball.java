package my_project.view.example;

import KAGO_framework.model.GraphicalObject;
import KAGO_framework.view.DrawTool;
import my_project.model.Animatable;

public class Ball extends GraphicalObject implements Animatable {

    private int alpha;

    public Ball() {
        width = 5;
        height = 20;
        alpha = 255;
    }

    @Override
    public void draw(DrawTool drawTool) {
        drawTool.drawRectangle(x, y, width, height);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
    }

    @Override
    public void fadeIn() {

    }

    @Override
    public void fadeOut() {

    }
}
