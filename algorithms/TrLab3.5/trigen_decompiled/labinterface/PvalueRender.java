/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PvalueRender
extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;
    private static PvalueRender singleton = new PvalueRender();
    private final DecimalFormat formatter = new DecimalFormat("#.##E0");

    private PvalueRender() {
    }

    public static PvalueRender getInstance() {
        return singleton;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        value = this.formatter.format((Number)value);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}

