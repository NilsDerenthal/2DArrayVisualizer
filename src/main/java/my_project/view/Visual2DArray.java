package my_project.view;

import KAGO_framework.control.Interactable;
import KAGO_framework.model.GraphicalObject;
import KAGO_framework.model.abitur.datenstrukturen.List;
import KAGO_framework.view.DrawTool;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;


public class Visual2DArray<T extends GraphicalObject & Visual2DArray.Animatable> extends GraphicalObject implements Interactable {

    /**
     * Configuration for the visualization
     */
    public static class VisualizationConfig {
        private final int
                x,
                y,
                cellWidth,
                cellHeight,
                margin;

        private final boolean
                drawOutline,
                fitObjectSize,
                traversable;

        private final Color
                pointerBackgroundColor,
                gridOutLineColor,
                backgroundColor;

        /**
         * Creates a configuration
         * @param x the x-coordinate
         * @param y the y-coordinate
         * @param cellWidth the width of a cell
         * @param cellHeight the height of a cell
         * @param margin the margin of each cell
         * @param drawOutline if the outline should be drawn or not
         * @param fitObjectSize if the cell-size should depend on the biggest object
         * @param traversable if the pointer should be drawn
         * @param pointerBackgroundColor the background color of the pointer
         * @param gridOutLineColor the outline color
         * @param backgroundColor the background color of the cell
         */
        public VisualizationConfig(int x, int y,
                                   int cellWidth, int cellHeight,
                                   int margin,
                                   boolean drawOutline,
                                   boolean fitObjectSize,
                                   boolean traversable,
                                   Color pointerBackgroundColor,
                                   Color gridOutLineColor,
                                   Color backgroundColor) {
            this.x = x;
            this.y = y;
            this.cellWidth = cellWidth;
            this.cellHeight = cellHeight;
            this.margin = margin;
            this.drawOutline = drawOutline;
            this.fitObjectSize = fitObjectSize;
            this.traversable = traversable;
            this.pointerBackgroundColor = pointerBackgroundColor;
            this.gridOutLineColor = gridOutLineColor;
            this.backgroundColor = backgroundColor;
        }

        /**
         * Constructs a default config.
         * x and y -> 30;
         * width and height -> 20;
         * margin -> 5;
         * draw-outline, fit-object-size, traversable -> true;
         * pointer-color -> Gray;
         * grid-outline-color -> Black;
         * background-color -> white;
         */
        public VisualizationConfig() {
            this(30, 30, 20, 20, 5, true, true, true, Color.GRAY, Color.BLACK, Color.WHITE);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getCellWidth() {
            return cellWidth;
        }

        public int getCellHeight() {
            return cellHeight;
        }

        public int getMargin() {
            return margin;
        }

        public boolean isDrawOutline() {
            return drawOutline;
        }

        public boolean isFitObjectSize() {
            return fitObjectSize;
        }

        public boolean isTraversable() {
            return traversable;
        }

        public Color getPointerBackgroundColor() {
            return pointerBackgroundColor;
        }

        public Color getGridOutLineColor() {
            return gridOutLineColor;
        }

        public Color getBackgroundColor() {
            return backgroundColor;
        }
    }

    /**
     * Adapter for the Interactable interface
     */
    public abstract static class InteractableAdapter implements Interactable {
        @Override
        public void keyPressed(int key) {}

        @Override
        public void keyReleased(int key) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {}
    }

    /**
     * An interface to force animations onto an Object
     */
    public interface Animatable {
        void fadeIn();
        void fadeOut();
    }

    /**
     * The default movement controller
     */
    public final Interactable defaultMovement = new InteractableAdapter() {
        @Override
        public void keyPressed(int key) {
            switch (key) {
                case KeyEvent.VK_RIGHT -> setPointer(getPointerX() + 1, getPointerY());
                case KeyEvent.VK_LEFT -> setPointer(getPointerX() - 1, getPointerY());
                case KeyEvent.VK_UP -> setPointer(getPointerX(), getPointerY() - 1);
                case KeyEvent.VK_DOWN -> setPointer(getPointerX(), getPointerY() + 1);
            }
        }
    };

    private final T[][] internalRepresentation;
    private final VisualizationConfig config;

    private int xPointer, yPointer;

    private final List<Interactable> controllers;

    /**
     * Constructs a new Visual 2D array with the given specs
     * @param width the width of the list (cells)
     * @param height the height of the list (cells)
     * @param startPointerX the origin of the x-pointer
     * @param startPointerY the origin of the y-pointer
     * @param config the configuration
     */
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

    /**
     * Constructs a new Visual 2D array with the given width and height.
     * The pointer is set to (0, 0) by default as well as a default config
     * @param width the width of the list (cells)
     * @param height the height of the list (cells)
     */
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

    /**
     * Sets the value at the given indecies.
     * @param value the value at the given values. Can be null which removes the object
     * @param x the x index for this object to be inserted at.
     * @param y the y index for this object to be inserted at.
     */
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

    /**
     * Sets the value at the current pointer.
     * @param value the value to be set. Can be null which removes the Object
     */
    public void set(T value) {
        set(value, xPointer, yPointer);
    }

    /**
     * Sets the pointer to this x and y index if it is a valid coordinate
     * @param x the x-pointer
     * @param y the y-pointer
     */
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

    /**
     * Draws the Array. Not to be called manually!!
     * @param drawTool the drawTool
     */
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

    /**
     * Add a controller to this array.
     * @param i the new controller
     */
    public void addController(Interactable i) {
        if (this == i) {
            throw new RuntimeException("Cannot register yourself");
        }
        controllers.append(i);
    }

    /**
     * @return the current x-pointer value
     */
    public int getPointerX() {
        return xPointer;
    }

    /**
     * @return the current x-pointer value
     */
    public int getPointerY() {
        return yPointer;
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