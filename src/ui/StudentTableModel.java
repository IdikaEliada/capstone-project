package ui;

import model.Student;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/** TableModel used by both UIs */
public class StudentTableModel extends AbstractTableModel {
    private final String[] cols = {"ID","First Name","Last Name","CGPA"};
    private List<Student> data;

    public StudentTableModel(List<Student> data) { this.data = data; }

    public void setData(List<Student> data) { this.data = data; fireTableDataChanged(); }

    @Override public int getRowCount() { return data == null ? 0 : data.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column) { return cols[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student s = data.get(rowIndex);
        switch (columnIndex) {
            case 0: return s.getId();
            case 1: return s.getFirstName();
            case 2: return s.getLastName();
            case 3: return String.format("%.2f", s.getComputedCgpa());
            default: return "";
        }
    }
}