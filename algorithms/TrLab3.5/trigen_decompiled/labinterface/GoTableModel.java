/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import analysis.biological.GoStudy;
import analysis.biological.GoTerm;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoTableModel
extends DefaultTableModel {
    private static final Logger LOG = LoggerFactory.getLogger(GoTableModel.class);
    private GoStudy study;
    private int rowCount;
    private int colCount = 4;
    private String[] columnNames = new String[]{"GO Term", "Name", "PV", "APV"};

    public GoTableModel(GoStudy study) {
        this.study = study;
        this.rowCount = study.getGoTerms().size();
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
        Object r = null;
        List<GoTerm> goTerms = this.study.getGoTerms();
        GoTerm term = goTerms.get(rowIndex);
        switch (columnIndex) {
            case 0: {
                r = term.getId();
                break;
            }
            case 1: {
                r = term.getName();
                break;
            }
            case 2: {
                r = new Double(term.getP());
                break;
            }
            case 3: {
                r = new Double(term.getpAdjusted());
            }
        }
        return r;
    }

    public Class getColumnClass(int columnIndex) {
        Class r = null;
        switch (columnIndex) {
            case 0: {
                r = String.class;
                break;
            }
            case 1: {
                r = String.class;
                break;
            }
            case 2: {
                r = Double.class;
                break;
            }
            case 3: {
                r = Double.class;
            }
        }
        return r;
    }
}

