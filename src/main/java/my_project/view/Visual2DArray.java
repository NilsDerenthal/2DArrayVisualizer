package my_project.view;

import KAGO_framework.model.GraphicalObject;
import KAGO_framework.view.DrawTool;
import my_project.model.Animatable;
import my_project.model.VisualizationConfig;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;


public class Visual2DArray<T extends GraphicalObject & Animatable> extends GraphicalObject implements Animatable {

    private final T[][] internalRepresentation;
    private final VisualizationConfig config;

    public Visual2DArray(int width, int height, VisualizationConfig config) {
        this.config = config;
        this.internalRepresentation = getArray(width, height);

        // copy config into main
        this.x = config.getX();
        this.y = config.getY();
        if (!config.isFitObjectSize()) {
            this.width = config.getCellWidth() * width;
            this.height = config.getCellHeight() * height;
        }
    }

    public Visual2DArray(int width, int height) {
        this(width, height, new VisualizationConfig());
    }

    @SuppressWarnings("unchecked")
    private T[][] getArray(int width, int height) {
        return (T[][]) new GraphicalObject[width][height];
    }

    public void set(T value, int x, int y) {
        // TODO fade?
        internalRepresentation[x][y] = value;
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
            drawTool.drawRectangle(x, y, finalCellWidth, finalCellHeight);

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

    @Override
    public void fadeIn(double dt) {

    }

    @Override
    public void fadeOut(double dt) {

    }
}
