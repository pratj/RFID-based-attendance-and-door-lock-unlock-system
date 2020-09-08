/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import AppPackage.AnimationClass;
//import  home.SERVER.
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
//import static home.SERVER.da;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author prans
 */
public class Reg extends javax.swing.JFrame {
     private Statement statement = null;
    private ResultSet resultSet = null;
    String name,uid,RFID;
    String clientdata;
    
    int ui;
    int c=0;
    public SERVER mfs;
    public Dialog fpd = new Dialog();
AnimationClass ac=new AnimationClass();



/*Timer fptimer = new javax.swing.Timer(100, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
           rfid();
        }});*/
    /**
     * Creates new form Reg
     */


    public Reg() throws SQLException {
        initComponents();
         this.setLocationRelativeTo(this);
      pic();
        SSlide ss=new SSlide();
      ss.start();
      ID();
   HttpServerStart();
      //fptimer.start();
    }
    
     void reset(){
        clientdata="";
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
    }
    
    
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 private void HttpServerStart(){
    try{
       HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/ArdServer", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server Started!"); 
        jButton1.setEnabled(false);
    }
    catch(Exception ex){System.out.println("Error: "+ex.getMessage());}
 
    
}
    

 class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {
       
        
           if (he.getRequestMethod().equalsIgnoreCase("POST")) {
           
          // REQUEST Headers
          Headers requestHeaders = he.getRequestHeaders();
        
       //   Set<Map.Entry<String, List<String>>> entries = requestHeaders.entrySet();
 
          int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));
           
         // REQUEST Body
                    InputStream is = he.getRequestBody();
 
                    byte[] data = new byte[contentLength];
                    int length = is.read(data);
                     clientdata=new String(data);
                    jTextField3.setText(clientdata);
                    ///jTextField1.setText(clientdata);
                    System.out.println("DATA++"+clientdata);
                    RFID=clientdata;
                    System.out.println("RFID++++"+RFID);
                    if(c==1){
            String response = "User Resgitered";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();}
        }
        }
        
    }
    
 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
    public void pic(){
          ImageIcon iconl = new javax.swing.ImageIcon(getClass().getResource("img1.jpg"));
 Image img1 = iconl.getImage();
        jLabel1.setIcon(new ImageIcon(
                            img1.getScaledInstance(jLabel1.getWidth(), jLabel1.getHeight(), Image.SCALE_DEFAULT))); 
        
        ImageIcon icon2 = new javax.swing.ImageIcon(getClass().getResource("img2.jpg"));
 Image img2 = icon2.getImage();
        jLabel2.setIcon(new ImageIcon(
                            img2.getScaledInstance(jLabel2.getWidth(), jLabel2.getHeight(), Image.SCALE_DEFAULT)));
        
          ImageIcon icon3 = new javax.swing.ImageIcon(getClass().getResource("img3.jpg"));
 Image img3 = icon3.getImage();
        jLabel3.setIcon(new ImageIcon(
                            img3.getScaledInstance(jLabel3.getWidth(), jLabel3.getHeight(), Image.SCALE_DEFAULT))); 
        
                  ImageIcon icon4 = new javax.swing.ImageIcon(getClass().getResource("im1.jpg"));
 Image img4 = icon4.getImage();
        jLabel7.setIcon(new ImageIcon(
                            img4.getScaledInstance(jLabel7.getWidth(), jLabel7.getHeight(), Image.SCALE_DEFAULT)));
    }
    
     private class SSlide extends Thread{
         int count;
       public void run(){
               try{
                  while(true){
                     switch(count) {
                         case 0:
                           Thread.sleep(2000);
                              ac.jLabelXLeft( 0,-680,5,1, jLabel1);
                             ac.jLabelXLeft( 680,0,5,1, jLabel2);
                              ac.jLabelXLeft(1360,680,5, 1, jLabel3);
                                Thread.sleep(2000);
                             
                            
                            count=1;
                         case 1:
                             //Thread.sleep(3000);
                              ac.jLabelXLeft( -680,-1360,5,1, jLabel1);
                             ac.jLabelXLeft( 0,-680,5, 1, jLabel2);
                              ac.jLabelXLeft(680,0,5, 1, jLabel3);
                              Thread.sleep(2000);
                              count=2;
                             
                         case 2:
                              Thread.sleep(2000);
                              ac.jLabelXRight( -1360,0,5,1, jLabel1);
                               ac.jLabelXRight( -680,680,5, 1, jLabel2);
                              // Thread.sleep(2000);
                              ac.jLabelXRight(0,1360,5, 1, jLabel3);
                              //Thread.sleep(2000);
                              
                                                             
                                count=0;
                      
                           
                             break;
                             
                     }
                  }
              }
              catch(Exception e){
                  
              }
          }
       }
  /*public void rfid(){
      SERVER as=new SERVER();
      System.out.println("AS DATA++++==="+as.da);
      jTextField2.setText(as.da);
      RFID = as.da;
  }*/
     public void ID() throws SQLException {
      
        try {
            
            
            statement = Login.connect.createStatement();

            String sqlstr = "select max(uid) from USERD";
            resultSet = statement.executeQuery(sqlstr);
            resultSet.next();
            System.out.println("UID======" + resultSet.getString(1));
            ui = Integer.parseInt(resultSet.getString(1));
            ui = ui + 1;
            uid = Integer.toString(ui);
            jTextField1.setText(uid);
        } catch (NullPointerException e) {
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

        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(680, 420));
        setMinimumSize(new java.awt.Dimension(680, 420));
        setPreferredSize(new java.awt.Dimension(680, 420));
        getContentPane().setLayout(null);

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(0, 0, 0));
        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(0, 255, 255));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField1);
        jTextField1.setBounds(120, 280, 190, 40);

        jTextField2.setBackground(new java.awt.Color(0, 0, 0));
        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(0, 255, 255));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField2);
        jTextField2.setBounds(120, 140, 190, 40);

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(0, 0, 0));
        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(0, 255, 255));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField3);
        jTextField3.setBounds(120, 210, 190, 40);

        jButton3.setBackground(new java.awt.Color(0, 0, 153));
        jButton3.setForeground(new java.awt.Color(0, 204, 204));
        jButton3.setText("Submit");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(260, 340, 150, 21);
        getContentPane().add(jLabel7);
        jLabel7.setBounds(430, 120, 210, 210);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 255, 255));
        jLabel6.setText("RFID        :");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(20, 210, 100, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 255, 255));
        jLabel4.setText("Your UID   :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 280, 100, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 255, 255));
        jLabel5.setText("Name       :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(20, 140, 100, 30);

        jLabel1.setMaximumSize(new java.awt.Dimension(680, 420));
        jLabel1.setMinimumSize(new java.awt.Dimension(680, 420));
        jLabel1.setPreferredSize(new java.awt.Dimension(680, 420));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 680, 420);

        jLabel2.setMaximumSize(new java.awt.Dimension(680, 420));
        jLabel2.setMinimumSize(new java.awt.Dimension(680, 420));
        jLabel2.setPreferredSize(new java.awt.Dimension(680, 420));
        getContentPane().add(jLabel2);
        jLabel2.setBounds(680, 0, 680, 420);

        jLabel3.setMaximumSize(new java.awt.Dimension(680, 420));
        jLabel3.setMinimumSize(new java.awt.Dimension(680, 420));
        jLabel3.setPreferredSize(new java.awt.Dimension(680, 420));
        getContentPane().add(jLabel3);
        jLabel3.setBounds(1360, 0, 680, 420);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
name = jTextField2.getText();
c=0;
     if(name.length() !=0 && RFID.length() !=0){   
        try {
            
                        statement = Login.connect.createStatement();
                        statement.executeUpdate("INSERT INTO USERD values(0,'" + name + "',md5('" + RFID + "'))");
                        fpd.setVisible(true);
                        c=1;
                        fpd.jLabel3.setText("   User Registered");
                        reset();
                        ID();
                    } catch (NullPointerException | SQLException ex) {System.out.println(ex);
                     JOptionPane.showMessageDialog(null,ex.toString());
                    }}
     else{
         fpd.setVisible(true);
                        fpd.jLabel3.setText("  Fill all Information!");
     }
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Reg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Reg().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Reg.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
