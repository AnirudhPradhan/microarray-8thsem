/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.ui;

import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.util.Messages;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.Format;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ExportDialog
extends JDialog {
    private static final long serialVersionUID = -1344719157074981540L;
    private final Rectangle2D a;
    private UserAction b;
    private final JFormattedTextField c;
    private final JFormattedTextField d;
    private final JFormattedTextField e;
    private final JFormattedTextField f;

    public ExportDialog(Component component, Drawable object) {
        super(JOptionPane.getFrameForComponent(component), true);
        this.setTitle(Messages.getString("ExportDialog.exportOptionsTitle"));
        this.a = new Rectangle2D.Double();
        this.a.setFrame(object.getBounds());
        Object object2 = UserAction.CANCEL;
        object = this;
        this.b = object2;
        object = new JPanel(new BorderLayout());
        ((JComponent)object).setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setContentPane((Container)object);
        object2 = new DecimalFormat();
        ((DecimalFormat)object2).setMinimumFractionDigits(2);
        JPanel jPanel = new JPanel(new GridLayout(4, 2, 10, 2));
        this.getContentPane().add((Component)jPanel, "North");
        PropertyChangeListener propertyChangeListener = new PropertyChangeListener(this){
            private /* synthetic */ ExportDialog a;
            {
                this.a = exportDialog;
            }

            @Override
            public final void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                this.a.setDocumentBounds(((Number)this.a.c.getValue()).doubleValue(), ((Number)this.a.d.getValue()).doubleValue(), ((Number)this.a.e.getValue()).doubleValue(), ((Number)this.a.f.getValue()).doubleValue());
            }
        };
        this.c = new JFormattedTextField((Format)object2);
        ExportDialog.a(this.c, Messages.getString("ExportDialog.left"), jPanel, this.a.getX(), propertyChangeListener);
        this.d = new JFormattedTextField((Format)object2);
        ExportDialog.a(this.d, Messages.getString("ExportDialog.top"), jPanel, this.a.getY(), propertyChangeListener);
        this.e = new JFormattedTextField((Format)object2);
        ExportDialog.a(this.e, Messages.getString("ExportDialog.width"), jPanel, this.a.getWidth(), propertyChangeListener);
        this.f = new JFormattedTextField((Format)object2);
        ExportDialog.a(this.f, Messages.getString("ExportDialog.height"), jPanel, this.a.getHeight(), propertyChangeListener);
        object2 = new JPanel(new FlowLayout());
        ((Container)object).add((Component)object2, "South");
        object = new JButton(Messages.getString("ExportDialog.confirm"));
        ((AbstractButton)object).addActionListener(new ActionListener(this){
            private /* synthetic */ ExportDialog a;
            {
                this.a = exportDialog;
            }

            @Override
            public final void actionPerformed(ActionEvent actionEvent) {
                v0.b = UserAction.APPROVE;
                this.a.dispose();
            }
        });
        ((Container)object2).add((Component)object);
        object = new JButton(Messages.getString("ExportDialog.abort"));
        ((AbstractButton)object).addActionListener(new ActionListener(this){
            private /* synthetic */ ExportDialog a;
            {
                this.a = exportDialog;
            }

            @Override
            public final void actionPerformed(ActionEvent actionEvent) {
                v0.b = UserAction.CANCEL;
                this.a.dispose();
            }
        });
        ((Container)object2).add((Component)object);
        this.pack();
        this.setLocationRelativeTo(component);
    }

    private static void a(JFormattedTextField jFormattedTextField, String object, Container container, Object object2, PropertyChangeListener propertyChangeListener) {
        object = new JLabel((String)object);
        ((JLabel)object).setHorizontalAlignment(4);
        container.add((Component)object);
        jFormattedTextField.setValue(object2);
        jFormattedTextField.setHorizontalAlignment(4);
        jFormattedTextField.addPropertyChangeListener("value", propertyChangeListener);
        container.add(jFormattedTextField);
        ((JLabel)object).setLabelFor(jFormattedTextField);
    }

    public Rectangle2D getDocumentBounds() {
        Rectangle2D.Double double_ = new Rectangle2D.Double();
        double_.setFrame(this.a);
        return double_;
    }

    protected void setDocumentBounds(double d, double d2, double d3, double d4) {
        if (this.a.getX() == d && this.a.getY() == d2 && this.a.getWidth() == d3 && this.a.getHeight() == d4) {
            return;
        }
        this.a.setFrame(d, d2, d3, d4);
        this.c.setValue(d);
        this.d.setValue(d2);
        this.e.setValue(d3);
        this.f.setValue(d4);
    }

    public UserAction getUserAction() {
        return this.b;
    }

    public static enum UserAction {
        APPROVE,
        CANCEL;

    }
}

