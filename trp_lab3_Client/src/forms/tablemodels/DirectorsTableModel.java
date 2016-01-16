package forms.tablemodels;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Айна и Лена
 */
public class DirectorsTableModel extends DefaultTableModel {
   
    private final String[] headers = new String[] {"ID", "Имя", "Телефон", ""};
    private final List<Object[]> rowsData = new ArrayList<Object[]>();
    
    public DirectorsTableModel() {
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
    
    public List<String> getDirectorsInfo() {
        List<String> result = new ArrayList<>();
        if (rowsData != null) {
            for (int i = 0; i < rowsData.size(); i++) {
                result.add(rowsData.get(i)[1] + " [ID=" + rowsData.get(i)[0] + "]");
            }
        }
        return result;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3;
    }
    
    @Override
    public int getRowCount() {
        if (rowsData == null) return 0;
        else return rowsData.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    
    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 3) return "";
        else return rowsData.get(rowIndex)[columnIndex];
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (column != 3)
            throw new UnsupportedOperationException();
    }
    
    public void invokeFireTableDataChanged() {
        fireTableDataChanged();
    }
}
