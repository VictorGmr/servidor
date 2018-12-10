/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class Servidor extends javax.swing.JFrame{

    /**
     * Creates new form Servidor
     */
    public Servidor() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Servidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Servidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Servidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Servidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        initComponents();
        
        this.setVisible(true);
        try {
            servidor = new ServerSocket(12345);
            
            new Thread(conexoes).start();
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

    
    
    ServerSocket servidor;
    Socket[] conexao = new Socket[1000];
    int save;
    int count = 1;
    
    ObjectOutputStream[] output = new ObjectOutputStream[1000];
    ObjectInputStream[] input = new ObjectInputStream[1000];
    FileOutputStream fo;
    
    byte[] b = new byte[1000];
    
    
    Thread receberThread[] = new Thread[1000];
    
    // THREAD QUE CONSTANTEMENTE PROCURA POR NOVAS CONEXÕES E AS CONFIGURA
    private Runnable conexoes = new Runnable() {
        @Override
        public void run(){
            while(true){
                
                try {
                    aguardarConexao();
                    configStreams();
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
        
                
                
            }
        }
    };    
    
    // THREAD QUE CONSTANTEMENTE RECEBE INFORMAÇÕES DE DIFERENTES CONEXÕES ATRAVÉS DE SUAS STREAMS
    private Runnable receber = new Runnable() {
        
        
        @Override
        public void run(){
            int i = save;
            System.out.println("save = " + save);
            while(true){
                try {
                    receberMsg(i);
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
    };
    
    
    
    private void aguardarConexao() throws IOException{
        
        do{
            conexao[count] = servidor.accept();
            
        }while(!conexao[count].isConnected());
        
    }
    
    private void configStreams() throws IOException{
        
        
        System.out.println("count: " + count);
        output[count] = new ObjectOutputStream(conexao[count].getOutputStream());
        output[count].flush();
        input[count] = new ObjectInputStream(conexao[count].getInputStream());
        jTextArea1.setText(jTextArea1.getText().concat("\nConectado com " + conexao[count].getInetAddress().getHostName()));
        
        save = count;
        
        receberThread[count] = new Thread(receber);
        if(!receberThread[count].isAlive()){
            receberThread[count].start();
        }
        count++;
    }
    
    
    
    int arquivoCount = 0;
    
    private void receberMsg(int i) throws IOException{
        
        
        
        Object msg = new Object();
        File file;
        
        try {
            System.out.println("i: " + i);
            
            
            msg = input[i].readObject();
            System.out.println(msg);
            if(msg.equals("END")){
                System.out.println("teste");
                jTextArea1.setText(jTextArea1.getText().concat("\nA conexão com o Cliente " + i + " foi encerrada!"));
                try {
                    input[i].close();
                    output[i].close();
                    conexao[i].close(); //ERRO
                    receberThread[i].stop();
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
            }else{
                jTextArea1.setText(jTextArea1.getText().concat("\nCLIENTE " + i + ": " + msg));
            }
            
            
        } catch (ClassNotFoundException ex) {
            System.out.println("Entrou no erro!");
            
            
        } catch (OptionalDataException ex){
            System.out.println(msg);
            fo = new FileOutputStream("C:\\Users\\Victor\\Desktop\\File" + i + arquivoCount);
                
            input[i].read(b, 0, b.length);
            fo.write(b, 0, b.length);
            arquivoCount++;
               
        }
 
          
    }

    private void enviarMsg(String msg){
           
        for(int i = 1; i < count; i++){
            
            try{
                System.out.println("i: " + i);
                output[i].writeObject("SERVIDOR: " + msg);
                output[i].flush();
            }catch (IOException iOException){
            
            }
            
        }
        
        jTextArea1.setText(jTextArea1.getText().concat("\nSERVIDOR: " + msg));
    
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton1.setText("Enviar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Servidor");

        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 255, 51));
        jTextArea1.setRows(5);
        jTextArea1.setText("Aguardando conexão...\n");
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        
        enviarMsg(jTextField1.getText());
        jTextField1.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
   
    
    public static void main(String[] args){
        
        Servidor s = new Servidor();
        
        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
