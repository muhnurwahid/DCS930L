/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.view;

import dcs_930l.DCS_930L;
import dcs_930l.entity.Ipcam;
import dcs_930l.model.M_Ipcam;
import dcs_930l.socket.ClientHandler;
import dcs_930l.socket.Server;
import dcs_930l.video.RecordIpCameraThread;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dhan
 */
public class MainView extends javax.swing.JFrame {

    ServerSocket serverSocket = null;
    Socket clientSocket;
    M_Ipcam m_Model;
    DefaultTableModel model;
    Server[] servers;
    RecordIpCameraThread[] recordIpCameraThreads;

    public MainView() {
        setTitle("IPCAM DCS 930L : Running server on port : 1990");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        setLocationRelativeTo(null);
        m_Model = new M_Ipcam();
        try {
            serverSocket = new ServerSocket(1990);
        } catch (IOException ex) {
            Logger.getLogger(DCS_930L.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        clientSocket = serverSocket.accept();
                        new ClientHandler(clientSocket).start();
                    } catch (IOException ex) {
                        try {
                            clientSocket.close();
                        } catch (IOException ex1) {
                            Logger.getLogger(DCS_930L.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                        Logger.getLogger(DCS_930L.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
        model = m_Model.readToTable("select ipcam_id,ipcam_name,ipcam_url,ipcam_port_listening,ipcam_status,username,password from ipcam");
        model.fireTableDataChanged();
        tableIpCam.setModel(model);
        servers = new Server[model.getRowCount()];
        recordIpCameraThreads = new RecordIpCameraThread[model.getRowCount()];
        for (int i = 0; i < model.getRowCount(); i++) {
            String name = model.getValueAt(i, 1).toString();
            String url = model.getValueAt(i, 2).toString();
            int port = Integer.parseInt(model.getValueAt(i, 3).toString());
            String status = model.getValueAt(i, 4).toString();
            String[] account = new String[2];
            account[0] = model.getValueAt(i, 5).toString();
            account[1] = model.getValueAt(i, 6).toString();
            servers[i] = new Server(name, port, url, account);
            if (status.equals("Started")) {
                servers[i].start();
            }
            RecordIpCameraThread recordIpCameraThread = new RecordIpCameraThread(this, name, url, account);
            recordIpCameraThreads[i] = recordIpCameraThread;
            recordIpCameraThreads[i].start();
        }
    }

    public void deleteCamIfNotFound(String value,String camName) {
        System.out.println("Delete cam status : " + m_Model.delete("ipcam", "ipcam_url", value));
        DCS_930L.walk(camName,1);
        DCS_930L.walk(camName,2);
        refresh();
    }

    private void refresh() {
        if (servers != null) {
            for (int i = 0; i < servers.length; i++) {
                servers[i].finsih();
                servers[i].stop();
            }
            servers = null;
        }
        model = m_Model.readToTable("select ipcam_id,ipcam_name,ipcam_url,ipcam_port_listening,ipcam_status,username,password from ipcam");
        model.fireTableDataChanged();
        tableIpCam.setModel(model);
        servers = new Server[model.getRowCount()];

        RecordIpCameraThread[] temp = recordIpCameraThreads;
        recordIpCameraThreads = new RecordIpCameraThread[model.getRowCount()];
        System.out.println("TEMP : " + temp.length + ", THREAD : " + recordIpCameraThreads.length);
        if (temp.length < recordIpCameraThreads.length) {
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] != null) {
                    recordIpCameraThreads[i] = temp[i];
                }
            }
        } else if (temp.length > recordIpCameraThreads.length) {
            for (int i = 0; i < temp.length; i++) {
                if (i == temp.length) {
                    temp[i].setStarted(false);
                }
            }
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            String name = model.getValueAt(i, 1).toString();
            String url = model.getValueAt(i, 2).toString();
            int port = Integer.parseInt(model.getValueAt(i, 3).toString());
            String status = model.getValueAt(i, 4).toString();
            String[] account = new String[2];
            account[0] = model.getValueAt(i, 5).toString();
            account[1] = model.getValueAt(i, 6).toString();
            servers[i] = new Server(name, port, url, account);
            if (status.equals("Started")) {
                servers[i].start();
            }

            if (recordIpCameraThreads[i] == null) {
                System.out.println("STARTING KE " + i);
                RecordIpCameraThread recordIpCameraThread = new RecordIpCameraThread(this, name, url, account);
                recordIpCameraThreads[i] = recordIpCameraThread;
                recordIpCameraThreads[i].start();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableIpCam = new javax.swing.JTable();
        btnStart = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        mnuExit = new javax.swing.JMenuItem();
        menuMasterData = new javax.swing.JMenu();
        mnuUsers = new javax.swing.JMenuItem();
        mnuIpcam = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableIpCam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableIpCam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableIpCamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableIpCam);

        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        menuFile.setText("File");

        mnuExit.setText("Exit");
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        menuFile.add(mnuExit);

        jMenuBar1.add(menuFile);

        menuMasterData.setText("Master Data");

        mnuUsers.setText("Users");
        mnuUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuUsersActionPerformed(evt);
            }
        });
        menuMasterData.add(mnuUsers);

        mnuIpcam.setText("Ipcam");
        mnuIpcam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIpcamActionPerformed(evt);
            }
        });
        menuMasterData.add(mnuIpcam);

        jMenuBar1.add(menuMasterData);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnStart)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuUsersActionPerformed
        // TODO add your handling code here:
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final UsersView dialog = new UsersView(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        dialog.dispose();
                    }
                });
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_mnuUsersActionPerformed

    private void mnuIpcamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuIpcamActionPerformed
        // TODO add your handling code here:
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final IpcamView dialog = new IpcamView(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        refresh();
                        dialog.dispose();
                    }
                });
                dialog.setVisible(true);
            }
        });
    }//GEN-LAST:event_mnuIpcamActionPerformed

    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuExitActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        // TODO add your handling code here:
        int selected = tableIpCam.getSelectedRow();
        if (selected >= 0) {
            Ipcam ipcam = new Ipcam();
            ipcam.setIpcam_id(model.getValueAt(selected, 0).toString());
            String old_status = model.getValueAt(selected, 4).toString();
            String new_status;
            if (old_status.equals("Stopped")) {
                new_status = "Started";
            } else {
                new_status = "Stopped";
            }

            if (btnStart.getText().equals("Start")) {
                btnStart.setText("Stop");
            } else {
                btnStart.setText("Start");
            }
            ipcam.setIpcam_status(new_status);
            if (m_Model.updateStatus(ipcam)) {
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to Start");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silahkan Pilih Salah Satu IPCAM");
        }
    }//GEN-LAST:event_btnStartActionPerformed

    private void tableIpCamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableIpCamMouseClicked
        // TODO add your handling code here:
        int selected = tableIpCam.getSelectedRow();
        if (selected >= 0) {
            String status = model.getValueAt(selected, 4).toString();
            if (status.equals("Stopped")) {
                btnStart.setText("Start");
            } else {
                btnStart.setText("Stop");
            }
        }
    }//GEN-LAST:event_tableIpCamMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStart;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuMasterData;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenuItem mnuIpcam;
    private javax.swing.JMenuItem mnuUsers;
    private javax.swing.JTable tableIpCam;
    // End of variables declaration//GEN-END:variables
}
