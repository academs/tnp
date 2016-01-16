package forms.dialogs;

import entities.Genre;
import forms.SelectItem;
import java.awt.Color;
import java.awt.Window;
import java.util.List;
import model.ClientController;
import model.ModelException;
import model.communication.protocol.ModelMessage;
import model.communication.ServerHandler;

/**
 *
 * @author Айна и Лена
 */
public class FilmDialog extends javax.swing.JDialog {

    private boolean addNew;
    private ServerHandler serverHandler;

    public FilmDialog(Window owner, ModalityType modalityType) {
        super(owner, modalityType);
        initComponents();
        statusLabel.setForeground(Color.red);
        Genre[] genres = Genre.values();
        for (int i = 0; i < genres.length; i++) {
            genreComboBox.addItem(genres[i]);
        }
    }

    public void showDialog(boolean addNew, Object[] rowData) {
        this.addNew = addNew;
        this.serverHandler = ServerHandler.getInstance();
        idTextField.grabFocus();
        statusLabel.setText("");
        acceptButton.setEnabled(true);
        //Create new Film - clear parameters
        if (addNew) {
            idTextField.setEditable(true);
            idTextField.setText("");
            titleTextField.setText("");
            yearTextField.setText("");
            durationTextField.setText("");
            directorComboBox.setSelectedItem(null);
            genreComboBox.setSelectedItem(null);
        } //Modify existing Film - fill parameters
        else {
            idTextField.setEditable(false);
            idTextField.setText(nvl(rowData[0]));
            titleTextField.setText(nvl(rowData[1]));
            yearTextField.setText(nvl(rowData[3]));
            durationTextField.setText(nvl(rowData[4]));
            genreComboBox.setSelectedItem(rowData[2]);
            for (int i = 0; i < directorComboBox.getItemCount(); i++) {
                Object item = directorComboBox.getItemAt(i);
                if(item.equals(rowData[5])) {
                    directorComboBox.setSelectedItem(item);
                    break;
                }
            }
        }
        this.setVisible(true);
    }

    public void updateDirectorsInfo(List<SelectItem<Integer, String>> companies) {
        SelectItem<Integer, String> selected = (SelectItem<Integer, String>) directorComboBox.getSelectedItem();
        //Update companies list        
        directorComboBox.removeAllItems();
        int selectedIndex = 0;
        for (SelectItem<Integer, String> company : companies) {
            directorComboBox.addItem(company);
            if (company.equals(selected)) {
                directorComboBox.setSelectedIndex(selectedIndex);
            }
            selectedIndex++;
        }
    }

    private void lostConnection() {
        acceptButton.setEnabled(false);
        ServerHandler.getInstance().stopHandler();
        statusLabel.setText("Нет ответа от сервера");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        idLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        genreLabel = new javax.swing.JLabel();
        yearLabel = new javax.swing.JLabel();
        durationLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        titleTextField = new javax.swing.JTextField();
        yearTextField = new javax.swing.JTextField();
        durationTextField = new javax.swing.JTextField();
        acceptButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        noteLabel = new javax.swing.JLabel();
        directorLabel = new javax.swing.JLabel();
        directorComboBox = new javax.swing.JComboBox();
        genreComboBox = new javax.swing.JComboBox();

        setTitle("Фильм: параметры");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(331, 279));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        idLabel.setText("ID*");

        titleLabel.setText("Название*");

        genreLabel.setText("Жанр*");

        yearLabel.setText("Год выпуска");

        durationLabel.setText("Длительность");

        acceptButton.setText("Принять");
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Отменить");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        statusLabel.setText("Статус");

        noteLabel.setText("* - обязательно для заполнения");

        directorLabel.setText("Режиссёр*");

        directorComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directorComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(statusLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(acceptButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancelButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(noteLabel)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(directorLabel)
                                            .addComponent(durationLabel)
                                            .addComponent(yearLabel)
                                            .addComponent(genreLabel)
                                            .addComponent(titleLabel))
                                        .addGap(19, 19, 19)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(genreComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(yearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(durationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(directorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(idLabel)
                                        .addGap(75, 75, 75)
                                        .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idLabel)
                    .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel)
                    .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(genreLabel)
                    .addComponent(genreComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yearLabel)
                    .addComponent(yearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(durationLabel)
                    .addComponent(durationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(directorLabel)
                    .addComponent(directorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(noteLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statusLabel)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Cancellation changes
     *
     * @param evt
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if (!addNew && acceptButton.isEnabled()) {
            ModelMessage result = serverHandler.sendRespMessage(
                    new ModelMessage(
                            ModelMessage.MessageType.STOP_EDIT,
                            ModelMessage.EntityTarget.FILM,
                            null));
            if (result == null) {
                lostConnection();
                return;
            }
        }
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed
    /**
     * Applying changes
     *
     * @param evt
     */
    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        Object[] data = new Object[6];
        data[0] = idTextField.getText();
        data[1] = titleTextField.getText();
        data[2] = genreComboBox.getSelectedItem();
        data[3] = yearTextField.getText();
        data[4] = durationTextField.getText();
        Object selectedItem = directorComboBox.getSelectedItem();
        if(selectedItem != null) {
            data[5] = ((SelectItem<?,?>)selectedItem).getKey();
        }

        try {
            ClientController.convertFilmValue(data);
        } catch (ModelException ex) {
            statusLabel.setText(ex.getMessage());
            return;
        }

        ModelMessage result;
        if (addNew) {
            result = serverHandler.sendRespMessage(
                    new ModelMessage(
                            ModelMessage.MessageType.CREATE,
                            ModelMessage.EntityTarget.FILM,
                            data));
        } else {
            result = serverHandler.sendRespMessage(
                    new ModelMessage(
                            ModelMessage.MessageType.STOP_EDIT,
                            ModelMessage.EntityTarget.FILM,
                            data));
        }
        if (result == null) {
            lostConnection();
            return;
        }
        if (result.getType() == ModelMessage.MessageType.REPSONSE_DENIED) {
            statusLabel.setText((String) result.getData());
            return;
        }

        this.setVisible(false);
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cancelButtonActionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void directorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directorComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_directorComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox directorComboBox;
    private javax.swing.JLabel directorLabel;
    private javax.swing.JLabel durationLabel;
    private javax.swing.JTextField durationTextField;
    private javax.swing.JComboBox genreComboBox;
    private javax.swing.JLabel genreLabel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField titleTextField;
    private javax.swing.JLabel yearLabel;
    private javax.swing.JTextField yearTextField;
    // End of variables declaration//GEN-END:variables

    private String nvl(Object object) {
        return object == null ? "" : object.toString();
    }
}
