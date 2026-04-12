/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.IdentityHashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.layout.UnitValue;
import net.miginfocom.swing.MigLayout;

public class CallbackDemo
extends JFrame
implements ActionListener,
MouseMotionListener,
MouseListener {
    private final Timer repaintTimer = new Timer(20, new ActionListener(){

        public void actionPerformed(ActionEvent actionEvent) {
            ((JPanel)CallbackDemo.this.getContentPane()).revalidate();
        }
    });
    private final IdentityHashMap<Object, Long> pressMap = new IdentityHashMap();
    private Point mousePos = null;
    private static Font[] FONTS = new Font[120];

    public CallbackDemo() {
        super("MiG Layout Callback Demo");
        MigLayout migLayout = new MigLayout("align center bottom, insets 30");
        final JPanel jPanel = new JPanel(migLayout){

            protected void paintComponent(Graphics graphics) {
                ((Graphics2D)graphics).setPaint(new GradientPaint(0.0f, this.getHeight() / 2, Color.WHITE, 0.0f, this.getHeight(), new Color(240, 238, 235)));
                graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
        };
        this.setContentPane(jPanel);
        migLayout.addLayoutCallback(new LayoutCallback(){

            public BoundSize[] getSize(ComponentWrapper componentWrapper) {
                if (componentWrapper.getComponent() instanceof JButton) {
                    Component component = (Component)componentWrapper.getComponent();
                    Point point = CallbackDemo.this.mousePos != null ? SwingUtilities.convertPoint(jPanel, CallbackDemo.this.mousePos, component) : new Point(-1000, -1000);
                    float f = (float)Math.sqrt(Math.pow(Math.abs((float)point.x - (float)component.getWidth() / 2.0f), 2.0) + Math.pow(Math.abs((float)point.y - (float)component.getHeight() / 2.0f), 2.0));
                    f = Math.max(2.0f - f / 200.0f, 1.0f);
                    return new BoundSize[]{new BoundSize(new UnitValue(70.0f * f), ""), new BoundSize(new UnitValue(70.0f * f), "")};
                }
                return null;
            }

            public void correctBounds(ComponentWrapper componentWrapper) {
                Long l = (Long)CallbackDemo.this.pressMap.get(componentWrapper.getComponent());
                if (l != null) {
                    long l2 = System.nanoTime() - l;
                    double d = 100.0 - (double)l2 / 1.0E8;
                    int n = (int)Math.round(Math.abs(Math.sin((double)l2 / 3.0E8) * d));
                    componentWrapper.setBounds(componentWrapper.getX(), componentWrapper.getY() - n, componentWrapper.getWidth(), componentWrapper.getHeight());
                    if (d < 0.5) {
                        CallbackDemo.this.pressMap.remove(componentWrapper.getComponent());
                        if (CallbackDemo.this.pressMap.size() == 0) {
                            CallbackDemo.this.repaintTimer.stop();
                        }
                    }
                }
            }
        });
        for (int i = 0; i < 10; ++i) {
            jPanel.add((Component)this.createButton(i), "aligny 0.8al");
        }
        JLabel jLabel = new JLabel("Can't you just feel the urge to press one of those Swing JButtons?");
        jLabel.setFont(new Font("verdana", 0, 24));
        jLabel.setForeground(new Color(150, 150, 150));
        jPanel.add((Component)jLabel, "pos 0.5al 0.2al");
        jPanel.addMouseMotionListener(this);
        jPanel.addMouseListener(this);
    }

    private JButton createButton(int n) {
        JButton jButton = new JButton(String.valueOf("MIG LAYOUT".charAt(n))){

            public Font getFont() {
                if (FONTS[0] == null) {
                    for (int i = 0; i < FONTS.length; ++i) {
                        FONTS[i] = new Font("tahoma", 0, i);
                    }
                }
                return FONTS[this.getWidth() >> 1];
            }
        };
        jButton.setForeground(new Color(100, 100, 100));
        jButton.setFocusPainted(false);
        jButton.addMouseMotionListener(this);
        jButton.addActionListener(this);
        jButton.setMargin(new Insets(0, 0, 0, 0));
        return jButton;
    }

    public void mouseDragged(MouseEvent mouseEvent) {
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        this.mousePos = mouseEvent.getSource() instanceof JButton ? SwingUtilities.convertPoint((Component)mouseEvent.getSource(), mouseEvent.getPoint(), this.getContentPane()) : mouseEvent.getPoint();
        ((JPanel)this.getContentPane()).revalidate();
    }

    public void mousePressed(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseClicked(MouseEvent mouseEvent) {
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
        this.mousePos = null;
        ((JPanel)this.getContentPane()).revalidate();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        this.pressMap.put(actionEvent.getSource(), System.nanoTime());
        this.repaintTimer.start();
    }

    public static void main(String[] stringArray) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception exception) {
            // empty catch block
        }
        CallbackDemo callbackDemo = new CallbackDemo();
        callbackDemo.setDefaultCloseOperation(3);
        callbackDemo.setSize(970, 500);
        callbackDemo.setLocationRelativeTo(null);
        callbackDemo.setVisible(true);
    }
}

