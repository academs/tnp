/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms.dialogs;

import java.awt.Color;
import java.awt.Window;
import model.ClientController;
import model.ModelException;
import model.communication.protocol.ModelMessage;
import model.communication.ServerHandler;

/**
 *
 * @author Айна и Лена
 */
public class DirectorDialog extends javax.swing.JDialog {

    private boolean addNew;
    private ServerHandler serverHandler;
        

    public DirectorDialog(Window owner, ModalityType modalityType) {
        super(owner, modalityType);        
        initComponents();
        statusLabel.setForeground(Color.red);
    }
    
    public void showDialog(boolean addNew, Object[] rowData) {
         this.addNew = addNew;
        this.serverHandler = ServerHandler.getInstance();
        idTextField.grabFocus();
        statusLabel.setText("");
        acceptButton.setEnabled(true);
        //Create new Director - clear parameters
        if (addNew) {
            idTextField.setEditable(true);
            idTextField.setText("");
            nameTextField1.setText("");
            phoneTextField.setText("");
        } 
        //Modify existing Director - fill parameters
         else {            
            idTextField.setEditable(false);
            idTextField.setText(rowData[0].toString());
            nameTextField1.setText(rowData[1].toString());
            if (rowData[2] == null) phoneTextField.setText("");
            else phoneTextField.setText(rowData[2].toString());
        }
        this.setVisible(true);
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
        nameLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        noteLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        nameTextField1 = new javax.swing.JTextField();
        phoneTextField = new javax.swing.JTextField();
        acceptButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        setTitle("Режиссёр");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(400, 300));
        setResizable(false);

        idLabel.setText("ID*");

        nameLabel.setText("Имя*");

        phoneLabel.setText("Телефон");

        noteLabel.setText("* - обязательно для заполнения");

        acceptButton.setText("принять");
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("отменить");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        statusLabel.setText("Статус");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(187, Short.MAX_VALUE)
                .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(noteLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(idLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameLabel)
                            .addComponent(phoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(91, 91, 91)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(idTextField)
                            .addComponent(nameTextField1)
                            .addComponent(phoneTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)))
                    .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(idLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(noteLabel)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(acceptButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        Object[] data = new Object[3];        
        data[0] = idTextField.getText();
        data[1] = nameTextField1.getText();
        data[2] = phoneTextField.getText();
                
        try {
            ClientController.convertDirectorValue(data);
        } catch (ModelException ex) {
            statusLabel.setText(ex.getMessage());
            return;
        }
        
        ModelMessage result;
        if (addNew) {
            result = serverHandler.sendRespMessage(
                    new ModelMessage(
                            ModelMessage.MessageType.CREATE,
                            ModelMessage.EntityTarget.DIRECTOR,
                            data));
        } else {
            result = serverHandler.sendRespMessage(
                    new ModelMessage(
                            ModelMessage.MessageType.STOP_EDIT,
                            ModelMessage.EntityTarget.DIRECTOR,
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

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
       if (!addNew && acceptButton.isEnabled()) {
            ModelMessage result = serverHandler.sendRespMessage(
                    new ModelMessage(
                            ModelMessage.MessageType.STOP_EDIT,
                            ModelMessage.EntityTarget.DIRECTOR,
                            null));
            if (result == null) {
                lostConnection();
                return;
            }
        }
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed
                    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        cancelButtonActionPerformed(null);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField1;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JTextField phoneTextField;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}



