package my_project.view;

import KAGO_framework.control.Interactable;
import KAGO_framework.model.GraphicalObject;
import KAGO_framework.view.DrawTool;
import my_project.model.Animatable;
import my_project.model.VisualizationConfig;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.http.HttpConnectTimeoutException;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;


public class Visual2DArray<T extends GraphicalObject & Animatable> extends GraphicalObject implements Interactable {

    private final T[][] internalRepresentation;
    private final VisualizationConfig config;

    private int xPointer, yPointer;

    public Visual2DArray(int width, int height, int startPointerX, int startPointerY, VisualizationConfig config) {
        this.config = config;
        this.internalRepresentation = getArray(width, height);

        // copy config into main
        this.x = config.getX();
        this.y = config.getY();

        if (!config.isFitObjectSize()) {
            this.width = config.getCellWidth() * width;
            this.height = config.getCellHeight() * height;
        }

        setPointer(startPointerX, startPointerY);
    }

    public Visual2DArray(int width, int height) {
        this(width, height, 0, 0, new VisualizationConfig());
    }

    private void checkOOB() {
        if (yPointer >= internalRepresentation.length || yPointer < 0) {
            throw new IndexOutOfBoundsException(yPointer + " out of bounds for size " + internalRepresentation.length);
        } else if (xPointer >= internalRepresentation[0].length || xPointer < 0) {
            throw new IndexOutOfBoundsException(xPointer + " out of bounds for size " + internalRepresentation[0].length);
        }
    }

    @SuppressWarnings("unchecked")
    private T[][] getArray(int width, int height) {
        return (T[][]) new GraphicalObject[width][height];
    }

    public void set(T value, int x, int y) {
        if (value != null) {
            value.fadeIn();
        }

        if (internalRepresentation[x][y] != null) {
            internalRepresentation[x][y].fadeOut();
        }

        internalRepresentation[x][y] = value;
    }

    public void set(T value) {
        set(value, xPointer, yPointer);
    }

    public void setPointer(int x, int y) {
        xPointer = x;
        yPointer = y;
        checkOOB();
    }

    private void forEach(BiConsumer<Integer, Integer> action) {
        for (int i = 0; i < internalRepresentation.length; i++) {
            for (int j = 0; j < internalRepresentation[i].length; j++) {
                action.accept(i, j);
            }
        }
    }

    @Override
    public void draw(DrawTool drawTool) {
        double cellWidth = config.getCellWidth();
        double cellHeight = config.getCellHeight();

        if (config.isFitObjectSize()) {
            // obtain biggest element
            Function<ToDoubleFunction<T>, OptionalDouble> supplier = func -> Arrays
                    .stream(internalRepresentation)
                    .flatMapToDouble(array -> Arrays.stream(array).mapToDouble(value -> value == null ? 0 : func.applyAsDouble(value)))
                    .max();

            var w = supplier.apply(T::getWidth);
            if (w.isPresent())
                cellWidth = w.getAsDouble();

            var h = supplier.apply(T::getHeight);
            if (h.isPresent())
                cellHeight = h.getAsDouble();
        }


        // lambda
        double finalCellWidth = cellWidth + config.getMargin() * 2;
        double finalCellHeight = cellHeight + config.getMargin() * 2;

        forEach((i, j) -> {
            double x = i * finalCellWidth + config.getX() + config.getMargin();
            double y = j * finalCellHeight + config.getY() + config.getMargin();

            // grid
            if (config.isDrawOutline()) {
                drawTool.setCurrentColor(
                        i == xPointer
                        && j == yPointer
                        && config.isTraversable()
                        ? config.getPointerBackgroundColor()
                        : config.getBackgroundColor()
                );

                drawTool.drawFilledRectangle(x, y, finalCellWidth, finalCellHeight);
                drawTool.setCurrentColor(config.getGridOutLineColor());
                drawTool.drawRectangle(x, y, finalCellWidth, finalCellHeight);
            }

            // object(s)
            T visualObj = internalRepresentation[i][j];

            if (visualObj != null) {
                visualObj.setX(x + config.getMargin());
                visualObj.setY(y + config.getMargin());
                visualObj.draw(drawTool);
            }
        });
    }

    @Override
    public void update(double dt) {

    }

    // interactable implementation

    @Override
    public void keyPressed(int key) {
        switch (key) {
            case KeyEvent.VK_LEFT -> xPointer--;
            case KeyEvent.VK_RIGHT -> xPointer++;
            case KeyEvent.VK_DOWN -> yPointer++;
            case KeyEvent.VK_UP -> yPointer--;
        }
    }

    @Override
    public void keyReleased(int key) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }
}
