package forms.tablemodels;

import entities.Genre;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Айна и Лена
 */
public class FilmsTableModel extends DefaultTableModel {
   
    private final String[] headers = new String[] {"ID", "Название", "Жанр", "Год", "Длительность", "Режиссёр", ""};
    private final List<Object[]> rowsData = new ArrayList<Object[]>();
    
    public FilmsTableModel() {        
    }
    
    public void updateData(Object[][] data) {
        rowsData.clear();
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                rowsData.add(data[i]);
            }
        }
        fireTableDataChanged();
    }
    
    public void addRowData(Object[] row) {
        rowsData.add(row);
        fireTableDataChanged();
    }
    
    public void updateRowData(Object[] data) {
        for (int i = 0; i < rowsData.size(); i++) {
            if (rowsData.get(i)[0].equals(data[0])) {
                rowsData.get(i)[1] = data[1];
                rowsData.get(i)[2] = data[2];
                rowsData.get(i)[3] = data[3];
                rowsData.get(i)[4] = data[4];
                rowsData.get(i)[5] = data[5];
            }
        }
        fireTableDataChanged();
    }
    
    public void deleteRowData(Object data) {
        for (int i = 0; i < rowsData.size(); i++) {
            if (rowsData.get(i)[0].equals(data)) {
                rowsData.remove(i);
                break;
            }
        }
        fireTableDataChanged();
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 6;
    }
    
    @Override
    public int getRowCount() {
        if (rowsData == null) return 0;
        else return rowsData.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }
    
    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 6) return "";
        else return rowsData.get(rowIndex)[columnIndex];
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (column != 6)
            throw new UnsupportedOperationException();
    }
    
    public void invokeFireTableDataChanged() {
        fireTableDataChanged();
    }   
}
