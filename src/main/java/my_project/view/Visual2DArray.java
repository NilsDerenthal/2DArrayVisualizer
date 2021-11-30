package my_project.view;

import KAGO_framework.control.Interactable;
import KAGO_framework.model.GraphicalObject;
import KAGO_framework.view.DrawTool;
import lombok.NonNull;
import my_project.model.Animatable;
import my_project.model.VisualizationConfig;

import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;


public class Visual2DArray<T extends GraphicalObject & Animatable> extends GraphicalObject implements Interactable {

    private final T[][] internalRepresentation;
    private final VisualizationConfig config;

    private int xPointer, yPointer;

    public Visual2DArray(int width, int height, int startPointerX, int startPointerY, @NonNull VisualizationConfig config) {
        this.config = config;
        this.internalRepresentation = getArray(width, height);

        // copy config into main
        this.x = config.getX();
        this.y = config.getY();
        if (!config.isFitObjectSize()) {
            this.width = config.getCellWidth() * width;
            this.height = config.getCellHeight() * height;
        }
        this.xPointer = startPointerX;
        this.yPointer = startPointerY;
        checkOOB();
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

    private void forEach(@NonNull BiConsumer<Integer, Integer> action) {
        for (int i = 0; i < internalRepresentation.length; i++) {
            for (int j = 0; j < internalRepresentation[i].length; j++) {
                action.accept(i, j);
            }
        }
    }

    @Override
    public void draw(DrawTool drawTool) {
        int cellWidth = config.getCellWidth();
        int cellHeight = config.getCellHeight();

        if (config.isFitObjectSize()) {
            // obtain biggest element
            AtomicInteger
                    maxW = new AtomicInteger(),
                    maxH = new AtomicInteger();

            forEach((i, j) -> {
                if (internalRepresentation[i][j] != null) {
                    maxW.set((int) Math.max(maxW.get(), internalRepresentation[i][j].getWidth()));
                    maxH.set((int) Math.max(maxH.get(), internalRepresentation[i][j].getHeight()));
                }
            });

            cellWidth = maxW.get();
            cellHeight = maxH.get();
        }


        // lambda
        int finalCellWidth = cellWidth + config.getMargin() * 2;
        int finalCellHeight = cellHeight + config.getMargin() * 2;

        forEach((i, j) -> {
            int x = i * finalCellWidth + config.getX() + config.getMargin();
            int y = j * finalCellHeight + config.getY() + config.getMargin();

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

    // Interactable methods

    @Override
    public void keyPressed(int key) {

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
