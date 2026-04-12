/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.legends;

import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Dimension2D;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawableContainer;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.graphics.Orientation;
import de.erichseifert.gral.graphics.layout.EdgeLayout;
import de.erichseifert.gral.graphics.layout.Layout;
import de.erichseifert.gral.graphics.layout.OrientedLayout;
import de.erichseifert.gral.graphics.layout.StackedLayout;
import de.erichseifert.gral.plots.legends.Legend;
import de.erichseifert.gral.plots.legends.LegendSymbolRenderer;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.SerializationUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractLegend
extends DrawableContainer
implements Legend,
LegendSymbolRenderer {
    private static final long serialVersionUID = -1561976879958765700L;
    private final Set<DataSource> a;
    private final Map<Row, Drawable> b;
    private transient boolean c;
    private Font d;
    private Paint e;
    private transient Stroke f;
    private Font g;
    private Paint h;
    private Orientation i;
    private double j;
    private double k;
    private Dimension2D l;
    private Dimension2D m;

    public AbstractLegend() {
        this.setInsets(new Insets2D.Double(10.0));
        this.a = new LinkedHashSet<DataSource>();
        this.b = new HashMap<Row, Drawable>();
        this.e = Color.WHITE;
        this.f = new BasicStroke(1.0f);
        this.g = Font.decode(null);
        this.setDrawableFonts(this.g);
        this.h = Color.BLACK;
        this.i = Orientation.VERTICAL;
        this.j = 0.0;
        this.k = 0.0;
        this.setGap(new Dimension2D.Double(2.0, 0.5));
        this.m = new Dimension2D.Double(2.0, 2.0);
        this.setLayout(new StackedLayout(this.i, this.l.getWidth(), this.l.getHeight()));
        this.refreshLayout();
    }

    @Override
    public void draw(DrawingContext drawingContext) {
        if (this.b.isEmpty()) {
            return;
        }
        this.drawBackground(drawingContext);
        this.drawBorder(drawingContext);
        this.drawComponents(drawingContext);
    }

    protected void drawBackground(DrawingContext drawingContext) {
        Paint paint = this.getBackground();
        if (paint != null) {
            GraphicsUtils.fillPaintedShape(drawingContext.getGraphics(), this.getBounds(), paint, null);
        }
    }

    protected void drawBorder(DrawingContext drawingContext) {
        Stroke stroke = this.getBorderStroke();
        if (stroke != null) {
            Paint paint = this.getBorderColor();
            GraphicsUtils.drawPaintedShape(drawingContext.getGraphics(), this.getBounds(), paint, null, stroke);
        }
    }

    protected abstract Iterable<Row> getEntries(DataSource var1);

    protected abstract String getLabel(Row var1);

    @Override
    public void add(DataSource object) {
        this.a.add((DataSource)object);
        for (Row row : this.getEntries((DataSource)object)) {
            Object object2 = this.getLabel(row);
            Font font = this.getFont();
            object2 = new Item(row, this, (String)object2, font);
            this.add((Drawable)object2);
            this.b.put(row, (Drawable)object2);
        }
        this.invalidate();
    }

    @Override
    public boolean contains(DataSource dataSource) {
        return this.a.contains(dataSource);
    }

    @Override
    public void remove(DataSource dataSource) {
        this.a.remove(dataSource);
        Object object = new HashSet<Row>(this.b.keySet());
        object = object.iterator();
        while (object.hasNext()) {
            Object object2 = (Row)object.next();
            if (((DataAccessor)object2).getSource() != dataSource || (object2 = this.b.remove(object2)) == null) continue;
            this.remove((Drawable)object2);
        }
        this.invalidate();
    }

    @Override
    public void clear() {
        Object object = new HashSet<DataSource>(this.a);
        object = object.iterator();
        while (object.hasNext()) {
            DataSource dataSource = (DataSource)object.next();
            this.remove(dataSource);
        }
    }

    @Override
    public void refresh() {
        if (this.isValid()) {
            return;
        }
        Object object = new LinkedHashSet<DataSource>(this.a);
        this.clear();
        object = object.iterator();
        while (object.hasNext()) {
            DataSource dataSource = (DataSource)object.next();
            this.add(dataSource);
        }
        this.c = true;
    }

    protected final void refreshLayout() {
        Object object = this.getGap();
        Layout layout = this.getLayout();
        layout.setGapX(((Dimension2D)object).getWidth());
        layout.setGapY(((Dimension2D)object).getHeight());
        if (layout instanceof OrientedLayout) {
            object = (OrientedLayout)layout;
            object.setOrientation(this.getOrientation());
        }
    }

    @Override
    public void setBounds(double d, double d2, double d3, double d4) {
        Dimension2D dimension2D = this.getPreferredSize();
        double d5 = this.getAlignmentX();
        double d6 = this.getAlignmentY();
        super.setBounds(d + d5 * (d3 - dimension2D.getWidth()), d2 + d6 * (d4 - dimension2D.getHeight()), dimension2D.getWidth(), dimension2D.getHeight());
    }

    protected boolean isValid() {
        return this.c;
    }

    protected void invalidate() {
        this.c = false;
    }

    protected final void setDrawableFonts(Font font) {
        for (Drawable drawable : this.b.values()) {
            if (!(drawable instanceof Item)) continue;
            drawable = (Item)drawable;
            ((Item)drawable).c.setFont(font);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.f = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws ClassNotFoundException, IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(SerializationUtils.wrap(this.f));
    }

    @Override
    public Font getBaseFont() {
        return this.d;
    }

    @Override
    public void setBaseFont(Font font) {
        this.d = font;
    }

    @Override
    public Paint getBackground() {
        return this.e;
    }

    @Override
    public void setBackground(Paint paint) {
        this.e = paint;
    }

    @Override
    public Stroke getBorderStroke() {
        return this.f;
    }

    @Override
    public void setBorderStroke(Stroke stroke) {
        this.f = stroke;
    }

    @Override
    public Font getFont() {
        return this.g;
    }

    @Override
    public void setFont(Font font) {
        this.g = font;
        this.setDrawableFonts(font);
    }

    @Override
    public Paint getBorderColor() {
        return this.h;
    }

    @Override
    public void setBorderColor(Paint paint) {
        this.h = paint;
    }

    @Override
    public Orientation getOrientation() {
        return this.i;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        this.i = orientation;
        this.refreshLayout();
    }

    @Override
    public double getAlignmentX() {
        return this.j;
    }

    @Override
    public void setAlignmentX(double d) {
        this.j = d;
    }

    @Override
    public double getAlignmentY() {
        return this.k;
    }

    @Override
    public void setAlignmentY(double d) {
        this.k = d;
    }

    @Override
    public Dimension2D getGap() {
        return this.l;
    }

    @Override
    public void setGap(Dimension2D dimension2D) {
        this.l = dimension2D;
        if (this.l != null) {
            double d = this.getFont().getSize2D();
            this.l.setSize(this.l.getWidth() * d, this.l.getHeight() * d);
        }
    }

    @Override
    public Dimension2D getSymbolSize() {
        return this.m;
    }

    @Override
    public void setSymbolSize(Dimension2D dimension2D) {
        this.m = dimension2D;
    }

    public static class Item
    extends DrawableContainer {
        private static final long serialVersionUID = 3401141040936913098L;
        private final Row a;
        private final Drawable b;
        private final Label c;

        public Item(Row row, LegendSymbolRenderer legendSymbolRenderer, String string, Font font) {
            double d = font.getSize2D();
            this.setLayout(new EdgeLayout(d, 0.0));
            this.a = row;
            this.b = legendSymbolRenderer.getSymbol(row);
            this.add(this.b, (Object)Location.WEST);
            this.c = new Label(string);
            this.c.setFont(font);
            this.c.setAlignmentX(0.0);
            this.c.setAlignmentY(0.5);
            this.add(this.c, (Object)Location.CENTER);
        }

        public Row getRow() {
            return this.a;
        }

        public Label getLabel() {
            return this.c;
        }

        public Drawable getSymbol() {
            return this.b;
        }
    }

    protected static abstract class AbstractSymbol
    extends AbstractDrawable {
        private static final long serialVersionUID = 7475404103140652668L;
        private final Legend a;

        public AbstractSymbol(Legend legend) {
            this.a = legend;
        }

        @Override
        public Dimension2D getPreferredSize() {
            double d = this.a.getFont().getSize2D();
            Dimension2D dimension2D = this.a.getSymbolSize();
            Dimension2D dimension2D2 = super.getPreferredSize();
            dimension2D2.setSize(dimension2D.getWidth() * d, dimension2D.getHeight() * d);
            return dimension2D2;
        }
    }
}

