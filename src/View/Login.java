
package View;

import javax.swing.JOptionPane;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Login extends javax.swing.JPanel {

    public Frame frame;

    public Login() {
        initComponents();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        usernameFld = new javax.swing.JTextField();
        passwordFld = new javax.swing.JPasswordField();
        registerBtn = new javax.swing.JButton();
        loginBtn = new javax.swing.JButton();


        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SECURITY Svcs");
        jLabel1.setToolTipText("");

        usernameFld.setBackground(new java.awt.Color(240, 240, 240));
        usernameFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        usernameFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usernameFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "USERNAME", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        passwordFld.setBackground(new java.awt.Color(240, 240, 240));
        passwordFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        passwordFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "PASSWORD", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N


        registerBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        registerBtn.setText("REGISTER");
        registerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }


        });

        loginBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        loginBtn.setText("LOGIN");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(200, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(registerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(usernameFld)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordFld, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(usernameFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(126, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        int showIncorrectPrompt = 0;
        
        if (evt.getSource() == loginBtn) {
            String username = usernameFld.getText();
            String password = new String(passwordFld.getText());
            
            if (frame.isLocked(username) == 3) {
                frame.loginNav();
                showIncorrectPrompt = 1;
                JOptionPane.showMessageDialog(null, "Your account is locked. Contact an administrator.");
            } else {
                // If user exists, go to mainNav
                if (frame.loginAuth(username, password)) {
                    // check if locked na ba ung account
                    frame.mainNav();

                    Login.usernameFld.setText("");
                    Login.passwordFld.setText("");

                    frame.resetAttempts(username);

                    frame.hideButtons(frame.getCurrentUser(username).getRole());
                    System.out.println(frame.getCurrentUser(username).getUsername() + frame.getCurrentUser(username).getRole());

                    // role binary
                    int currentrole = 0;

                    byte[] settings = new byte[4];
                    byte[] staffsettings = new byte[4];
                    byte[] clientsettings = new byte[4];
                    byte[] clientsettingshist = new byte[4];
                    byte[] adminsettings = new byte[4];
                    byte[] managersettingshist = new byte[4];
                    byte[] managersettingsProds= new byte[4];

                    if (frame.getCurrentUser(username).getRole() == 2) {
                        settings[1] = 1;
                        settings[2] = 1;
                        clientsettings[0] = 1;
                        clientsettingshist[0] =0;
                        clientsettingshist[1] =0;
                        currentrole = 2;

                        // Write the binary file
                        try {
                            FileOutputStream fos = new FileOutputStream("settings.bin");
                            fos.write(settings);
                            fos.close();
                            System.out.println("settings.bin file created successfully.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            FileOutputStream fos = new FileOutputStream("clientsettings.bin");
                            fos.write(clientsettings);
                            fos.close();
                            System.out.println("clientsettings.bin file created successfully.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            FileOutputStream fos = new FileOutputStream("clientsettingshist.bin");
                            fos.write(clientsettingshist);
                            fos.close();
                            System.out.println("clientsettingshist.bin file created successfully.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else if (frame.getCurrentUser(username).getRole() == 3) { //staff
                        settings[2] = 1;
                        staffsettings[1] = 1;
                        staffsettings[2] = 1;
                        staffsettings[3] = 1;
                        currentrole = 3;

                        // Write the binary file
                        try {
                            FileOutputStream fos = new FileOutputStream("settings.bin");
                            fos.write(settings);
                            fos.close();
                            System.out.println("settings.bin file created successfully.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            FileOutputStream fos = new FileOutputStream("staffsettings.bin");
                            fos.write(staffsettings);
                            fos.close();
                            System.out.println("staffsettings.bin file created successfully.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (frame.getCurrentUser(username).getRole() == 4) { //manager
                        settings[1] = 1;
                        settings[2] = 1;
                        managersettingsProds[1] = 1;
                        managersettingsProds[2] = 1;
                        managersettingsProds[3] = 1;
                        managersettingshist[0] = 1;
                        managersettingshist[1] = 1;
                        currentrole = 4;

                        // Write the binary file
                        try {
                            FileOutputStream fos = new FileOutputStream("settings.bin");
                            fos.write(settings);
                            fos.close();
                            System.out.println("settings.bin file created successfully.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            FileOutputStream fos = new FileOutputStream("managersettingsHist.bin");
                            fos.write(managersettingshist);
                            fos.close();
                            System.out.println("managersettingsHist.bin file created successfully.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            FileOutputStream fos = new FileOutputStream("managersettingsProds.bin");
                            fos.write(managersettingsProds);
                            fos.close();
                            System.out.println("managersettingsProds.bin file created successfully.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (frame.getCurrentUser(username).getRole() == 5) { //admin
                        settings[0] = 1;
                        settings[3] = 1;
                        currentrole = 5;

                        // Write the binary file
                        try {
                            FileOutputStream fos = new FileOutputStream("settings.bin");
                            fos.write(settings);
                            fos.close();
                            System.out.println("settings.bin file created successfully.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    //write binary file for current user role
                    try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("currentrole.bin"))) {
                        dos.writeUTF(username);
                        dos.writeInt(currentrole);
                        dos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // Else, display error
                } else {
                    if (showIncorrectPrompt == 0) {
                        frame.loginAttempt(username, frame.isLocked(username));
                        JOptionPane.showMessageDialog(null, "The username or password is incorrect.");
                    }
                }
            }

            
        }
    }//GEN-LAST:event_loginBtnActionPerformed

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBtnActionPerformed
        frame.registerNav();
        Login.usernameFld.setText("");
        Login.passwordFld.setText("");
    }//GEN-LAST:event_registerBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginBtn;
    static javax.swing.JTextField passwordFld;
    private javax.swing.JButton registerBtn;
    static javax.swing.JTextField usernameFld;
    // End of variables declaration//GEN-END:variables
}
