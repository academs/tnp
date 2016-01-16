package forms;

import forms.dialogs.FilmDialog;
import forms.dialogs.DirectorDialog;
import forms.tablemodels.DirectorsTableModel;
import forms.tablemodels.FilmsTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.EventObject;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import model.communication.protocol.ModelMessage;
import model.communication.ServerHandler;

/**
 *
 * @author Айна, Лена
 */
public class MainFrame extends javax.swing.JFrame {

    ServerHandler serverHandler;
    
    private JTable jTableDirectors;
    private DirectorsTableModel directorsTableModel;
    private DirectorDialog directorDialog;

    private JTable jTableFilms;
    private FilmsTableModel filmsTableModel;
    private FilmDialog filmDialog;

    public MainFrame() {
        initComponents();
        tablesPane.setTitleAt(0, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>Режиссёры</body></html>");
        tablesPane.setTitleAt(1, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>Фильмы</body></html>");
        statusLabel.setForeground(Color.red);
        statusLabel.setText("");
        initDirectors();
        initFilms();
        initHandlers();
        enableActions(false);
    }

    private void initDirectors() {
        //Create table Model
        directorsTableModel = new DirectorsTableModel();
        //Create Table
        jTableDirectors = new JTable(directorsTableModel);
        jTableDirectors.setRowHeight(32);
        jTableDirectors.setFont(new Font("Courier new", 2, 16));
        jTableDirectors.getTableHeader().setReorderingAllowed(false);
        jTableDirectors.setFocusable(false);
        jTableDirectors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Init two buttons in last column
        TableColumn column = jTableDirectors.getColumnModel().getColumn(3);
        column.setMinWidth(170);
        column.setMaxWidth(170);
        column.setCellRenderer(new ButtonsRenderer());
        column.setCellEditor(new ButtonsEditor(jTableDirectors));
        //Draw table
        directorsField.setViewportView(jTableDirectors);
        //Create details Dialog
        directorDialog = new DirectorDialog(this, Dialog.ModalityType.APPLICATION_MODAL);
    }

    private void initFilms() {
        //Create table Model
        filmsTableModel = new FilmsTableModel();
        //Create Table
        jTableFilms = new JTable(filmsTableModel);
        jTableFilms.setRowHeight(32);
        jTableFilms.setFont(new Font("Courier new", 2, 16));
        jTableFilms.getTableHeader().setReorderingAllowed(false);
        jTableFilms.setFocusable(false);
        jTableFilms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Init two buttons in last column
        TableColumn column = jTableFilms.getColumnModel().getColumn(6);
        column.setMinWidth(170);
        column.setMaxWidth(170);
        column.setCellRenderer(new ButtonsRenderer());
        column.setCellEditor(new ButtonsEditor(jTableFilms));
        //Draw table
        filmsField.setViewportView(jTableFilms);
        //Create details Dialog
        filmDialog = new FilmDialog(this, Dialog.ModalityType.APPLICATION_MODAL);
    }
    
    private void initHandlers() {
        ServerHandler.addCloseConnectionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    closeConnection((String)e.getSource());
                }
            });
        
        ServerHandler.addUpdateListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateAllEntities((ModelMessage)e.getSource());
                }
            });
        ServerHandler.addUpdateListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addEntity((ModelMessage)e.getSource());
                }
            });
        ServerHandler.addUpdateListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateOneEntity((ModelMessage)e.getSource());
                }
            });
        ServerHandler.addUpdateListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteEntity((ModelMessage)e.getSource());
                }
            });
    }
    
    private void enableActions(boolean  value) {
        addDirectorButton.setEnabled(value);
        loadButton.setEnabled(value);
        saveButton.setEnabled(value);
        addFilmButton.setEnabled(value);
    }
    
    private void updateAllEntities(ModelMessage message) {
        if (message.getType() != ModelMessage.MessageType.UPDATE) return;
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) { 
            directorsTableModel.updateData((Object[][])message.getData());
            filmDialog.updateDirectorsInfo(directorsTableModel.getDirectorsInfo());
        }
        else filmsTableModel.updateData((Object[][])message.getData());
        statusLabel.setText("Таблица " + message.getTarget() + " полностью обновлена");
    }
    
    private void addEntity(ModelMessage message) {
        if (message.getType() != ModelMessage.MessageType.CREATE) return;
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
            directorsTableModel.addRowData((Object[])message.getData());
            filmDialog.updateDirectorsInfo(directorsTableModel.getDirectorsInfo());
        }
        else filmsTableModel.addRowData((Object[])message.getData());
        statusLabel.setText("Запись добавлена в таблицу " + message.getTarget());
    } 
    
    private void updateOneEntity(ModelMessage message) {
        if (message.getType() != ModelMessage.MessageType.STOP_EDIT) return;
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
            directorsTableModel.updateRowData((Object[])message.getData());
            filmDialog.updateDirectorsInfo(directorsTableModel.getDirectorsInfo());
        }
        else filmsTableModel.updateRowData((Object[])message.getData());
        statusLabel.setText("Запись изменена в таблице " + message.getTarget());
    }
    
    private void deleteEntity(ModelMessage message) {
        if (message.getType() != ModelMessage.MessageType.DELETE) return;
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
            directorsTableModel.deleteRowData(message.getData());
            filmDialog.updateDirectorsInfo(directorsTableModel.getDirectorsInfo());
        }
        else filmsTableModel.deleteRowData(message.getData());
        statusLabel.setText("Запись удалена из таблицы " + message.getTarget());
    }
    
    private void closeConnection(String str) {
        connectionButton.setBackground(Color.red);        
        enableActions(false);
        directorsTableModel.updateData(null);
        filmsTableModel.updateData(null);
        if (!str.isEmpty()) statusLabel.setText(str);
    }
    
    private class ButtonsPanel extends JPanel {

        public final JButton editButton = new JButton("изменить");
        public final JButton deleteButton = new JButton("удалить");

        public ButtonsPanel() {
            super();
            setLayout(new GridLayout(1, 0, 10, 0));
            add(editButton);
            add(deleteButton);
        }
    }

    private class ButtonsRenderer extends ButtonsPanel implements TableCellRenderer {

        public ButtonsRenderer() {
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }

        private class ButtonsEditor extends ButtonsPanel implements TableCellEditor {

        /**
         * Editing and deleting entities
         * @param table
         */
        public ButtonsEditor(final JTable table) {

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = table.convertRowIndexToModel(table.getEditingRow());
                    fireEditingStopped();
                    if (table.getModel() instanceof DirectorsTableModel) {                        
                        ModelMessage response = serverHandler.sendRespMessage(
                                new ModelMessage(ModelMessage.MessageType.START_EDIT, ModelMessage.EntityTarget.DIRECTOR, directorsTableModel.getValueAt(row, 0)));
                        if (response == null) {
                            closeConnection("Ошибка установления соединения");
                        } else if (response.getType() == ModelMessage.MessageType.REPSONSE_ALLOWED) {
                            directorDialog.showDialog(false, (Object[])response.getData());
                            statusLabel.setText("Редактирование завершено");
                        } else {
                            statusLabel.setText(response.getData().toString());
                        }                        
                    } else if (table.getModel() instanceof FilmsTableModel) {
                        ModelMessage response = serverHandler.sendRespMessage(
                                new ModelMessage(ModelMessage.MessageType.START_EDIT, ModelMessage.EntityTarget.FILM, filmsTableModel.getValueAt(row, 0)));
                        if (response == null) {
                            closeConnection("Ошибка установления соединения");
                        } else if (response.getType() == ModelMessage.MessageType.REPSONSE_ALLOWED) {
                            filmDialog.showDialog(false, (Object[])response.getData());
                            statusLabel.setText("Редактирование завершено");
                        } else {
                            statusLabel.setText(response.getData().toString());
                        }
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = table.convertRowIndexToModel(table.getEditingRow());
                    fireEditingStopped();
                    if (table.getModel() instanceof DirectorsTableModel) {
                        boolean accepted = JOptionPane.showConfirmDialog(table, "Удаление кинокомпании приведёт к удалению связанных фильмов. Согласны?", "", 0) == JOptionPane.OK_OPTION;
                        if (accepted) {
                            ModelMessage response = serverHandler.sendRespMessage(
                                    new ModelMessage(ModelMessage.MessageType.DELETE, ModelMessage.EntityTarget.DIRECTOR, directorsTableModel.getValueAt(row, 0)));
                            if (response == null) {
                                closeConnection("Ошибка установления соединения");
                            }else if (response.getType() == ModelMessage.MessageType.REPSONSE_ALLOWED) {
                                statusLabel.setText("Компания удалена");
                            } else {
                                statusLabel.setText(response.getData().toString());
                            }
                        }
                    } else if (table.getModel() instanceof FilmsTableModel) {
                        ModelMessage response = serverHandler.sendRespMessage(
                                new ModelMessage(ModelMessage.MessageType.DELETE, ModelMessage.EntityTarget.FILM, filmsTableModel.getValueAt(row, 0)));
                        if (response == null) {
                            closeConnection("Ошибка установления соединения");
                        } else if (response.getType() == ModelMessage.MessageType.REPSONSE_ALLOWED) {
                            statusLabel.setText("Фильм удалён");
                        } else {
                            statusLabel.setText(response.getData().toString());
                        }
                    }
                }
            });
        }

        // <editor-fold defaultstate="collapsed" desc="Properties">
        transient protected ChangeEvent changeEvent = null;

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.setBackground(table.getSelectionBackground());
            return this;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

        @Override
        public void cancelCellEditing() {
            fireEditingCanceled();
        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {
            listenerList.add(CellEditorListener.class, l);
        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {
            listenerList.remove(CellEditorListener.class, l);
        }

        protected void fireEditingStopped() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == CellEditorListener.class) {
                    if (changeEvent == null) {
                        changeEvent = new ChangeEvent(this);
                    }
                    ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
                }
            }
        }

        protected void fireEditingCanceled() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == CellEditorListener.class) {
                    if (changeEvent == null) {
                        changeEvent = new ChangeEvent(this);
                    }
                    ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
                }
            }
        }
        // </editor-fold>        
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tablesPane = new javax.swing.JTabbedPane();
        directorsPanel = new javax.swing.JPanel();
        directorsField = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        addDirectorButton = new javax.swing.JButton();
        filmsPanel = new javax.swing.JPanel();
        filmsField = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        addFilmButton = new javax.swing.JButton();
        statusBar = new javax.swing.JToolBar();
        statusLabel = new javax.swing.JLabel();
        loadButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        connectionButton = new javax.swing.JButton();
        serverIPTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Кино [режиссеры и фильмы]");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(1000, 400));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tablesPane.setBackground(new java.awt.Color(0, 204, 204));
        tablesPane.setFocusable(false);
        tablesPane.setName(""); // NOI18N

        directorsPanel.setLayout(new javax.swing.BoxLayout(directorsPanel, javax.swing.BoxLayout.PAGE_AXIS));

        directorsField.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        directorsPanel.add(directorsField);

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel3.setMinimumSize(new java.awt.Dimension(100, 40));
        jPanel3.setPreferredSize(new java.awt.Dimension(523, 40));

        addDirectorButton.setText("Добавить");
        addDirectorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDirectorButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(addDirectorButton)
                .addGap(0, 753, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(addDirectorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        directorsPanel.add(jPanel3);

        tablesPane.addTab("Режиссёры", null, directorsPanel, "");

        filmsPanel.setLayout(new javax.swing.BoxLayout(filmsPanel, javax.swing.BoxLayout.PAGE_AXIS));

        filmsField.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        filmsPanel.add(filmsField);

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel4.setMinimumSize(new java.awt.Dimension(100, 40));
        jPanel4.setPreferredSize(new java.awt.Dimension(523, 40));

        addFilmButton.setText("Добавить");
        addFilmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFilmButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(addFilmButton)
                .addGap(0, 753, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(addFilmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        filmsPanel.add(jPanel4);

        tablesPane.addTab("Фильмы", filmsPanel);

        statusBar.setFloatable(false);
        statusBar.setRollover(true);
        statusBar.setMargin(new java.awt.Insets(0, 10, 0, 0));

        statusLabel.setText("Статус");
        statusBar.add(statusLabel);

        loadButton.setFocusable(false);
        loadButton.setLabel("Восстановить резервную копию");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Сохранить резервную копию");
        saveButton.setFocusable(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        connectionButton.setBackground(java.awt.Color.red);
        connectionButton.setText("Соединиться с сервером");
        connectionButton.setFocusable(false);
        connectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionButtonActionPerformed(evt);
            }
        });

        serverIPTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        serverIPTextField.setText("localhost");
        serverIPTextField.setToolTipText("");
        serverIPTextField.setPreferredSize(new java.awt.Dimension(120, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tablesPane, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(serverIPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(connectionButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(loadButton)
                        .addGap(41, 41, 41)
                        .addComponent(saveButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(serverIPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(connectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tablesPane, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tablesPane.getAccessibleContext().setAccessibleName("");
        loadButton.getAccessibleContext().setAccessibleName("Восстановить резервную копию");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void addDirectorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDirectorButtonActionPerformed
        directorDialog.showDialog(true, null);
    }//GEN-LAST:event_addDirectorButtonActionPerformed
    
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        ModelMessage message = serverHandler.sendRespMessage(new ModelMessage(ModelMessage.MessageType.SAVE_MODEL, null, null));
        if (message == null) {
            closeConnection("Ошибка установления соединения");
        } else if (message.getType() == ModelMessage.MessageType.REPSONSE_ALLOWED)
            statusLabel.setText("Модель сохранена");
        else statusLabel.setText(message.getData().toString());
    }//GEN-LAST:event_saveButtonActionPerformed
   
    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        ModelMessage message = serverHandler.sendRespMessage(new ModelMessage(ModelMessage.MessageType.LOAD_MODEL, null, null));
        if (message == null) {
            closeConnection("Ошибка установления соединения");
        } else if (message.getType() == ModelMessage.MessageType.REPSONSE_ALLOWED)
            statusLabel.setText("Модель загружена");
        else statusLabel.setText(message.getData().toString());
    }//GEN-LAST:event_loadButtonActionPerformed
    
    private void addFilmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFilmButtonActionPerformed
        filmDialog.showDialog(true, null);
    }//GEN-LAST:event_addFilmButtonActionPerformed

    private void connectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionButtonActionPerformed
        try {
            serverHandler = ServerHandler.createXMLInstance(serverIPTextField.getText());
            connectionButton.setBackground(Color.green);
            enableActions(true);
            statusLabel.setText("Соединение с сервером установлено");
        } catch (IOException ex) {
            closeConnection("Ошибка установления соединения");
        }
    }//GEN-LAST:event_connectionButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (serverHandler != null) {
            serverHandler.stopHandler();
        }
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDirectorButton;
    private javax.swing.JButton addFilmButton;
    private javax.swing.JButton connectionButton;
    private javax.swing.JScrollPane directorsField;
    private javax.swing.JPanel directorsPanel;
    private javax.swing.JScrollPane filmsField;
    private javax.swing.JPanel filmsPanel;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton loadButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField serverIPTextField;
    private javax.swing.JToolBar statusBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTabbedPane tablesPane;
    // End of variables declaration//GEN-END:variables
}
