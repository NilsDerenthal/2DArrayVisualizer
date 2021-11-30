package my_project.model;

/*
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VisualizationConfig {
    @Builder.Default
    private final int
            x = 30,
            y = 30,
            cellWidth = 20,
            cellHeight = 20,
            margin = 5;

    @Builder.Default
    private final boolean
            drawOutline = true,
            fitObjectSize = false,
            dynamicResizing = false;

    @Builder.Default
    private final Color
            pointerBackgroundColor = Color.GRAY,
            gridOutLineColor = Color.BLACK,
            backgroundColor = Color.WHITE;
}


 */

import java.awt.*;

public class VisualizationConfig {
    private final int
            x,
            y,
            cellWidth,
            cellHeight,
            margin;

    private final boolean
            drawOutline,
            fitObjectSize,
            dynamicResizing,
            traversable;

    private final Color
            pointerBackgroundColor,
            gridOutLineColor,
            backgroundColor;

    public VisualizationConfig(int x, int y,
                               int cellWidth, int cellHeight,
                               int margin,
                               boolean drawOutline,
                               boolean fitObjectSize,
                               boolean dynamicResizing,
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
        this.dynamicResizing = dynamicResizing;
        this.traversable = traversable;
        this.pointerBackgroundColor = pointerBackgroundColor;
        this.gridOutLineColor = gridOutLineColor;
        this.backgroundColor = backgroundColor;
    }

    public VisualizationConfig() {
        this(30, 30, 20, 20, 5, true, true, true, true, Color.GRAY, Color.BLACK, Color.WHITE);
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

    public boolean isDynamicResizing() {
        return dynamicResizing;
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

