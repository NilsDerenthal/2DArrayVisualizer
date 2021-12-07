package my_project.view;

import KAGO_framework.control.Interactable;
import KAGO_framework.model.GraphicalObject;
import KAGO_framework.model.abitur.datenstrukturen.List;
import KAGO_framework.view.DrawTool;
import my_project.model.Animatable;
import my_project.model.VisualizationConfig;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.http.HttpConnectTimeoutException;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;


public class Visual2DArray<T extends GraphicalObject & Animatable> extends GraphicalObject implements Interactable {

    private final T[][] internalRepresentation;
    private final VisualizationConfig config;

    private int xPointer, yPointer;

    private List<Interactable> controllers;

    public Visual2DArray(int width, int height, int startPointerX, int startPointerY, VisualizationConfig config) {
        this.config = config;
        this.internalRepresentation = getArray(width, height);
        controllers = new List<>();

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

    private boolean isLegal(int x, int y) {
        return y >= 0 && x >= 0 &&
                x < internalRepresentation.length && y < internalRepresentation[0].length;
    }

    @SuppressWarnings("unchecked")
    private T[][] getArray(int width, int height) {
        return (T[][]) new GraphicalObject[width][height];
    }

    public void set(T value, int x, int y) {
        if (isLegal(x, y)) {
            if (value != null) {
                value.fadeIn();
            }

            if (internalRepresentation[x][y] != null) {
                internalRepresentation[x][y].fadeOut();
            }

            internalRepresentation[x][y] = value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void set(T value) {
        set(value, xPointer, yPointer);
    }

    public void setPointer(int x, int y) {
        if (isLegal(x, y)) {
            xPointer = x;
            yPointer = y;
        }
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

    public void addController(Interactable i) {
        if (this == i) {
            throw new RuntimeException("Cannot register yourself");
        }
        controllers.append(i);
    }

    // interactable implementation

    private void forEachController(Consumer<Interactable> c) {
        controllers.toFirst();
        while (controllers.hasAccess()) {
            c.accept(controllers.getContent());
            controllers.next();
        }
    }

    @Override
    public void keyPressed(int key) {
        forEachController(i -> i.keyPressed(key));
    }

    @Override
    public void keyReleased(int key) {
        forEachController(i -> i.keyReleased(key));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        forEachController(i -> i.mouseReleased(e));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        forEachController(i -> i.mouseClicked(e));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        forEachController(i -> i.mouseDragged(e));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        forEachController(i -> i.mouseMoved(e));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        forEachController(i -> i.mousePressed(e));
    }
}
