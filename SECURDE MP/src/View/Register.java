
package View;

public class Register extends javax.swing.JPanel {

    public Frame frame;
    
    public Register() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        registerButton = new javax.swing.JButton();
        username = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        errorField = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        confpass = new javax.swing.JPasswordField();

        registerButton.setBackground(new java.awt.Color(0, 0, 0));
        registerButton.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        registerButton.setForeground(new java.awt.Color(255, 255, 255));
        registerButton.setText("REGISTER");
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        username.setBackground(new java.awt.Color(240, 240, 240));
        username.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        username.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        username.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "USERNAME", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SECURITY Svcs");
        jLabel1.setToolTipText("");

        backButton.setBackground(new java.awt.Color(0, 0, 0));
        backButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        backButton.setForeground(new java.awt.Color(255, 255, 255));
        backButton.setText("<Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        errorField.setBackground(new java.awt.Color(240, 240, 240));
        errorField.setForeground(new java.awt.Color(204, 0, 0));
        errorField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        errorField.setBorder(null);
        errorField.setFocusable(false);
        errorField.setMargin(new java.awt.Insets(0, 0, 0, 0));

        password.setBackground(new java.awt.Color(240, 240, 240));
        password.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "PASSWORD", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        password.setMinimumSize(new java.awt.Dimension(12, 45));
        password.setPreferredSize(new java.awt.Dimension(12, 45));

        confpass.setBackground(new java.awt.Color(240, 240, 240));
        confpass.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        confpass.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        confpass.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "CONFIRM PASSWORD", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(errorField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(username)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                    .addComponent(confpass))
                .addGap(168, 168, 168))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton)
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(confpass, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        if(checkError()){    
            frame.registerAction(username.getText(), String.valueOf(password.getPassword()), String.valueOf(confpass.getPassword()));
            frame.loginNav();
            clearFields();
        }
    }//GEN-LAST:event_registerButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        frame.loginNav();
        clearFields();
    }//GEN-LAST:event_backButtonActionPerformed

    private void clearFields(){
        username.setText("");
        password.setText("");
        confpass.setText("");
        errorField.setText("");
    }
    
    private boolean checkPassEqual(){
        return String.valueOf(password.getPassword()).equals(String.valueOf(confpass.getPassword()));
    }
    
    private boolean checkPassIsBlank(){
        return String.valueOf(password.getPassword()).equals("");
    }
    
    private boolean checkError(){
        if(checkPassIsBlank()){
            errorField.setText("Password Field is Blank");
            return false;
        }
        if(!frame.checkUniqueUserNav(username.getText())){
            errorField.setText("Username Already Exists");
            return false;
        }
        
        if(!checkPassEqual()){
            errorField.setText("Please retype your password");
            return false;
        }
        
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JPasswordField confpass;
    private javax.swing.JTextField errorField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPasswordField password;
    private javax.swing.JButton registerButton;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
