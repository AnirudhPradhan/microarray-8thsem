/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.ui;

import de.erichseifert.gral.graphics.Container;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.gral.io.plots.DrawableWriterFactory;
import de.erichseifert.gral.navigation.Navigable;
import de.erichseifert.gral.navigation.Navigator;
import de.erichseifert.gral.ui.DrawablePanel;
import de.erichseifert.gral.ui.DrawableWriterFilter;
import de.erichseifert.gral.ui.ExportChooser;
import de.erichseifert.gral.ui.ExportDialog;
import de.erichseifert.gral.util.Messages;
import de.erichseifert.gral.util.PointND;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class InteractivePanel
extends DrawablePanel
implements Printable {
    private static final long serialVersionUID = 9084883142053148090L;
    private final PrinterJob a = PrinterJob.getPrinterJob();
    private boolean b;
    private boolean c;
    protected final ActionMap actions;
    private JPopupMenu d;
    private boolean e;
    private Point2D f;
    private final JFileChooser g;
    private a h;
    private b i;

    public InteractivePanel(Drawable object) {
        super((Drawable)object);
        this.a.setPrintable(this);
        object = DrawableWriterFactory.getInstance().getCapabilities();
        this.g = new ExportChooser(true, (List<IOCapabilities>)object);
        this.g.setDialogTitle(Messages.getString("InteractivePanel.exportImageTitle"));
        this.actions = new ActionMap();
        this.actions.put("zoomIn", new AbstractAction(this, Messages.getString("InteractivePanel.zoomIn")){
            private /* synthetic */ InteractivePanel a;
            {
                this.a = interactivePanel;
                super(string);
            }

            @Override
            public final void actionPerformed(ActionEvent actionEvent) {
                InteractivePanel.a(this.a, this.a.f, 1);
            }
        });
        this.actions.put("zoomOut", new AbstractAction(this, Messages.getString("InteractivePanel.zoomOut")){
            private /* synthetic */ InteractivePanel a;
            {
                this.a = interactivePanel;
                super(string);
            }

            @Override
            public final void actionPerformed(ActionEvent actionEvent) {
                InteractivePanel.a(this.a, this.a.f, -1);
            }
        });
        this.actions.put("resetView", new AbstractAction(this, Messages.getString("InteractivePanel.resetView")){
            private /* synthetic */ InteractivePanel a;
            {
                this.a = interactivePanel;
                super(string);
            }

            @Override
            public final void actionPerformed(ActionEvent actionEvent) {
                InteractivePanel.a(this.a, this.a.f);
            }
        });
        this.actions.put("exportImage", new AbstractAction(this, Messages.getString("InteractivePanel.exportImage")){
            private /* synthetic */ InteractivePanel a;
            {
                this.a = interactivePanel;
                super(string);
            }

            @Override
            public final void actionPerformed(ActionEvent actionEvent) {
                int n = this.a.g.showSaveDialog(this.a);
                this.a.repaint();
                if (n != 0) {
                    return;
                }
                File file = this.a.g.getSelectedFile();
                if (file == null) {
                    return;
                }
                if (file.exists()) {
                    int n2 = JOptionPane.showConfirmDialog(this.a, Messages.getString("InteractivePanel.exportExistsWarning"), Messages.getString("InteractivePanel.warning"), 0);
                    this.a.repaint();
                    if (n2 == 1) {
                        return;
                    }
                }
                Drawable drawable = this.a.getDrawable();
                ExportDialog exportDialog = new ExportDialog(this.a, drawable);
                exportDialog.setVisible(true);
                if (!exportDialog.getUserAction().equals((Object)ExportDialog.UserAction.APPROVE)) {
                    return;
                }
                DrawableWriterFilter drawableWriterFilter = (DrawableWriterFilter)this.a.g.getFileFilter();
                InteractivePanel.a(this.a, drawable, drawableWriterFilter.getWriterCapabilities().getMimeType(), file, exportDialog.getDocumentBounds());
            }
        });
        this.actions.put("print", new AbstractAction(this, Messages.getString("InteractivePanel.print")){
            private /* synthetic */ InteractivePanel a;
            {
                this.a = interactivePanel;
                super(string);
            }

            @Override
            public final void actionPerformed(ActionEvent serializable) {
                if (this.a.a.printDialog()) {
                    try {
                        this.a.a.print();
                        return;
                    }
                    catch (PrinterException printerException) {
                        serializable = printerException;
                        printerException.printStackTrace();
                    }
                }
            }
        });
        this.e = true;
        this.addMouseListener(new c(this, 0));
        this.setZoomable(true);
        this.setPannable(true);
    }

    protected JPopupMenu getPopupMenu(MouseEvent mouseEvent) {
        if (this.d == null) {
            this.d = new JPopupMenu();
            this.d.add(this.actions.get("zoomIn"));
            this.d.add(this.actions.get("zoomOut"));
            this.d.add(this.actions.get("resetView"));
            this.d.addSeparator();
            this.d.add(this.actions.get("exportImage"));
            this.d.add(this.actions.get("print"));
        }
        return this.d;
    }

    public boolean isPopupMenuEnabled() {
        return this.e;
    }

    public void setPopupMenuEnabled(boolean bl) {
        this.e = bl;
    }

    @Override
    public int print(Graphics graphics, PageFormat cloneable, int n) throws PrinterException {
        if (n > 0) {
            return 1;
        }
        graphics = (Graphics2D)graphics;
        AffineTransform affineTransform = ((Graphics2D)graphics).getTransform();
        ((Graphics2D)graphics).scale(0.5669291338582678, 0.5669291338582678);
        Rectangle2D rectangle2D = this.getDrawable().getBounds();
        cloneable = new Rectangle2D.Double(cloneable.getImageableX() / 0.5669291338582678, cloneable.getImageableY() / 0.5669291338582678, cloneable.getImageableWidth() / 0.5669291338582678, cloneable.getImageableHeight() / 0.5669291338582678);
        this.getDrawable().setBounds((Rectangle2D)cloneable);
        try {
            this.getDrawable().draw(new DrawingContext((Graphics2D)graphics));
        }
        finally {
            this.getDrawable().setBounds(rectangle2D);
        }
        ((Graphics2D)graphics).setTransform(affineTransform);
        return 0;
    }

    public boolean isZoomable() {
        return this.b;
    }

    public void setZoomable(boolean bl) {
        if (this.b == bl) {
            return;
        }
        this.b = bl;
        if (this.h != null) {
            this.removeMouseWheelListener(this.h);
            this.removeMouseListener(this.h);
            this.h = null;
        }
        if (bl) {
            this.h = new a(this);
            this.addMouseListener(this.h);
            this.addMouseWheelListener(this.h);
        }
        this.actions.get("zoomIn").setEnabled(this.isZoomable());
        this.actions.get("zoomOut").setEnabled(this.isZoomable());
        this.actions.get("resetView").setEnabled(this.isZoomable() && this.isPannable());
    }

    public boolean isPannable() {
        return this.c;
    }

    public void setPannable(boolean bl) {
        if (this.c == bl) {
            return;
        }
        this.c = bl;
        if (this.i != null) {
            this.removeMouseMotionListener(this.i);
            this.removeMouseListener(this.i);
            this.i = null;
        }
        if (bl) {
            this.i = new b(this);
            this.addMouseListener(this.i);
            this.addMouseMotionListener(this.i);
        }
        this.actions.get("resetView").setEnabled(this.isZoomable() && this.isPannable());
    }

    private static Navigable b(Drawable object, Point2D point2D) {
        Object object2;
        if (object instanceof Container) {
            object2 = ((Container)object).getDrawablesAt(point2D);
        } else {
            object2 = new ArrayList<Drawable>(1);
            object2.add((Drawable)object);
        }
        object = object2.iterator();
        while (object.hasNext()) {
            object2 = (Drawable)object.next();
            if (!(object2 instanceof Navigable) || !object2.getBounds().contains(point2D)) continue;
            return (Navigable)object2;
        }
        return null;
    }

    static /* synthetic */ void a(InteractivePanel interactivePanel, Point2D object, int n) {
        if (interactivePanel.isZoomable() && (object = InteractivePanel.b(interactivePanel.getDrawable(), (Point2D)object)) != null) {
            object = object.getNavigator();
            if (n >= 0) {
                for (int i = 0; i < n; ++i) {
                    object.zoomIn();
                }
            } else {
                for (int i = 0; i < -n; ++i) {
                    object.zoomOut();
                }
            }
            interactivePanel.repaint();
        }
    }

    static /* synthetic */ void a(InteractivePanel interactivePanel, Point2D object) {
        if (interactivePanel.isZoomable() && (object = InteractivePanel.b(interactivePanel.getDrawable(), (Point2D)object)) != null) {
            object = object.getNavigator();
            object.reset();
            interactivePanel.repaint();
        }
    }

    /*
     * Loose catch block
     */
    static /* synthetic */ void a(InteractivePanel object, Drawable object2, String object3, File object4, Rectangle2D rectangle2D) {
        Drawable drawable = object2;
        String string = object3;
        File file = object4;
        object4 = rectangle2D;
        object3 = file;
        object2 = string;
        object = drawable;
        try {
            object3 = new FileOutputStream((File)object3);
        }
        catch (FileNotFoundException fileNotFoundException) {
            object2 = fileNotFoundException;
            fileNotFoundException.printStackTrace();
            return;
        }
        object2 = DrawableWriterFactory.getInstance().get((String)object2);
        object2.write((Drawable)object, (OutputStream)object3, ((RectangularShape)object4).getX(), ((RectangularShape)object4).getY(), ((RectangularShape)object4).getWidth(), ((RectangularShape)object4).getHeight());
        try {
            ((FileOutputStream)object3).close();
            return;
        }
        catch (IOException iOException) {
            object = iOException;
            iOException.printStackTrace();
            return;
        }
        catch (IOException iOException) {
            try {
                object = iOException;
                iOException.printStackTrace();
            }
            catch (Throwable throwable) {
                try {
                    ((FileOutputStream)object3).close();
                }
                catch (IOException iOException2) {
                    object2 = iOException2;
                    iOException2.printStackTrace();
                }
                throw throwable;
            }
            try {
                ((FileOutputStream)object3).close();
                return;
            }
            catch (IOException iOException3) {
                object = iOException3;
                iOException3.printStackTrace();
                return;
            }
        }
    }

    private static final class b
    extends MouseAdapter {
        private final InteractivePanel a;
        private Navigable b;
        private Point c;

        public b(InteractivePanel interactivePanel) {
            this.a = interactivePanel;
        }

        @Override
        public final void mousePressed(MouseEvent serializable) {
            serializable = serializable.getPoint();
            this.b = InteractivePanel.b(this.a.getDrawable(), (Point2D)((Object)serializable));
            this.c = serializable;
        }

        @Override
        public final void mouseDragged(MouseEvent serializable) {
            if (this.b == null) {
                return;
            }
            serializable = serializable.getPoint();
            Navigator navigator = this.b.getNavigator();
            int n = ((Point)serializable).x - this.c.x;
            int n2 = ((Point)serializable).y - this.c.y;
            this.c = serializable;
            if (Math.abs(n) > 0 || Math.abs(n2) > 0) {
                serializable = new PointND((Number[])new Integer[]{n, n2});
                navigator.pan((PointND<? extends Number>)serializable);
                this.a.repaint();
            }
        }
    }

    private final class c
    extends MouseAdapter {
        private /* synthetic */ InteractivePanel a;

        private c(InteractivePanel interactivePanel) {
            this.a = interactivePanel;
        }

        @Override
        public final void mousePressed(MouseEvent mouseEvent) {
            this.a(mouseEvent);
        }

        @Override
        public final void mouseReleased(MouseEvent mouseEvent) {
            this.a(mouseEvent);
        }

        private void a(MouseEvent mouseEvent) {
            if (!this.a.isPopupMenuEnabled() || !mouseEvent.isPopupTrigger()) {
                return;
            }
            JPopupMenu jPopupMenu = this.a.getPopupMenu(mouseEvent);
            if (jPopupMenu == null) {
                return;
            }
            this.a.f = (Point2D)mouseEvent.getPoint();
            jPopupMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
        }

        /* synthetic */ c(InteractivePanel interactivePanel, byte by) {
            this(interactivePanel);
        }
    }

    private static final class a
    extends MouseAdapter
    implements MouseWheelListener,
    Serializable {
        private static final long serialVersionUID = -7323541053291673122L;
        private final InteractivePanel a;

        public a(InteractivePanel interactivePanel) {
            this.a = interactivePanel;
        }

        @Override
        public final void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
            Point point = mouseWheelEvent.getPoint();
            InteractivePanel.a(this.a, point, -mouseWheelEvent.getWheelRotation());
        }

        @Override
        public final void mouseClicked(MouseEvent serializable) {
            if (SwingUtilities.isLeftMouseButton(serializable) && serializable.getClickCount() == 2) {
                serializable = serializable.getPoint();
                InteractivePanel.a(this.a, (Point2D)((Object)serializable), 1);
            }
        }
    }
}

