/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import analysis.Solution;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class CommonExpTableModel
extends AbstractTableModel {
    private static final Logger LOG = LoggerFactory.getLogger(CommonExpTableModel.class);
    private List<Solution> solutions;
    private int rowCount;
    private int colCount = 5;
    private String[] columnNames = new String[]{"Solution", "TRIQ", "GRQ", "PEQ", "SPQ"};

    public CommonExpTableModel(List<Solution> solutions) {
        this.solutions = solutions;
        this.rowCount = solutions.size();
    }

    @Override
    public String getColumnName(int col) {
        return this.columnNames[col];
    }

    @Override
    public int getRowCount() {
        return this.rowCount;
    }

    @Override
    public int getColumnCount() {
        return this.colCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String r = null;
        Solution sol = this.solutions.get(rowIndex);
        DecimalFormat format = null;
        switch (columnIndex) {
            case 0: {
                r = sol.getName();
                break;
            }
            case 1: {
                format = TextUtilities.getDecimalFormat('.');
                r = format.format(sol.getValue("triq"));
                break;
            }
            case 2: {
                format = TextUtilities.getDecimalFormat('.');
                r = format.format(sol.getValue("grq"));
                break;
            }
            case 3: {
                format = TextUtilities.getDecimalFormat('.');
                r = format.format(sol.getValue("peq"));
                break;
            }
            case 4: {
                format = TextUtilities.getDecimalFormat('.');
                r = format.format(sol.getValue("spq"));
            }
        }
        return r;
    }
}

