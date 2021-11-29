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
}
 */


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
            dynamicResizing;

    public VisualizationConfig(int x, int y, int cellWidth, int cellHeight, int margin, boolean drawOutline, boolean fitObjectSize, boolean dynamicResizing) {
        this.x = x;
        this.y = y;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.margin = margin;
        this.drawOutline = drawOutline;
        this.fitObjectSize = fitObjectSize;
        this.dynamicResizing = dynamicResizing;
    }

    public VisualizationConfig() {
        this(30, 30, 20, 20, 5, true, true, true);
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
}

