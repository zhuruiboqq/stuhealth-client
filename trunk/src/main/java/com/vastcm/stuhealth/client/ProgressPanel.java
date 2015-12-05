/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import com.vastcm.stuhealth.client.framework.ui.ScreenUtils;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author house
 */
public class ProgressPanel extends javax.swing.JDialog {
    private Logger logger = LoggerFactory.getLogger(ProgressPanel.class);
    private Runnable progressKeeper;
    
    /**
	 * 
	 */
	public ProgressPanel() {
	}
	
    /**
     * Creates new form ProgressPanel
     */
    private ProgressPanel(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public ProgressPanel(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
    }
    
    public void setProgressKeeper(Runnable progressKeeper) {
        this.progressKeeper = progressKeeper;
    }
    
    @Override
    public void setVisible(boolean b) {
        setSize(450, 200);
        ScreenUtils.center(this);
        start();
        super.setVisible(b);
    }
    
    protected void start() {
        new Thread(progressKeeper).start();
    }

    public JLabel getLblDescription() {
        return lblDescription;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar();
        lblDescription = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().add(progressBar);
        progressBar.setBounds(6, 20, 388, 20);
        getContentPane().add(lblDescription);
        lblDescription.setBounds(6, 52, 388, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProgressPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProgressPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProgressPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProgressPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                ProgressPanel dialog = new ProgressPanel(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblDescription;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}