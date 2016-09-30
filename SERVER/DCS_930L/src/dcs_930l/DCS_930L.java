/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l;

import dcs_930l.config.Koneksi;
import dcs_930l.view.MainView;
import java.io.File;

/**
 *
 * @author Dhan
 */
public class DCS_930L {

    public static void main(String args[]) {
        if(Koneksi.getKoneksi()==null){
            System.exit(0);
        }

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainView().setVisible(true);
            }
        });
    }

    public static void walk(String path, int type) {
        String files;
        if(type==1){
            files = "video";
        }else{
            files = "image";
        }
        File root = new File(files+"/"+path);
        File[] list = root.listFiles();

        if (list == null) {
            root.deleteOnExit();
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                f.delete();
                walk(f.getAbsolutePath(), type);
            } else {
                f.delete();
            }
        }
    }
    
    public static void walk(String path) {        
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) {
            root.deleteOnExit();
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                f.delete();
                walk(f.getAbsolutePath());
            } else {
                f.delete();
            }
        }
    }
    
    public static long getSize(String path) {        
        long size = 0;
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) {            
            return size;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                size +=getSize(f.getAbsolutePath());
            } else {
                size +=f.length();
            }
        }
        return size;
    }
}
