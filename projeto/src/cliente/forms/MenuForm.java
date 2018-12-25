package cliente.forms;

import cliente.ClienteConnection;
import cliente.User;
import servidor.BaseDados;
import servidor.Server;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author KIKO
 */
public class MenuForm extends javax.swing.JFrame {
    private final JFrame login;
    private final User user;
    private final ClienteConnection connection;
    private final BaseDados db; // temporary
    DefaultTableModel modelDemand;
    DefaultTableModel modelBid;
    DefaultTableModel modelMy;

    /**
     * Creates new form MenuForm
     * @param login Login Form reference
     * @param user User that loged in
     * @param connection Connection to the server
     */
    public MenuForm(JFrame login, User user, ClienteConnection connection, BaseDados db) {
        this.login = login;
        this.user = user;
        this.connection = connection;
        this.db = db; // temporary
        fillDemandTable();
        fillBidTable();
        fillMyServersTable();
        initComponents();
        adjustColumnSizes();
        this.setTitle("Minhas Apostas");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
    }
    
    public void fillDemandTable(){
        String[] colunas = {"Tipo","Número","Preço/hora"};
        Object[][] data = new Object[this.db.getAllServers().size()][3];
        int i=0;
        for (String tipo : this.db.getAllServers().keySet()){
            double minPreco=100000000;
            for(Server s : this.db.getAllServers().get(tipo)){
                if(s.getPrice()<minPreco && !s.getUsed()) minPreco=s.getPrice();
            }
            data[i][0] = tipo;
            data[i][1] = this.db.getAllServers().get(tipo).size();
            data[i][2] = minPreco;
            i++;
        }
        modelDemand = new DefaultTableModel(data,colunas);
    }
    public void fillBidTable(){
        String[] colunas = {"Tipo","Número","Preço/hora"};
        Object[][] data = new Object[this.db.getAllServers().size()][3];
        int i=0;
        for (String tipo : this.db.getAllServers().keySet()){
            double minBid=100000000;
            for(Server s : this.db.getAllServers().get(tipo)){
                if(s.getLastBid()<minBid && !s.getUsed()) minBid=s.getLastBid();
            }
            data[i][0] = tipo;
            data[i][1] = this.db.getAllServers().get(tipo).size();
            data[i][2] = minBid;
            i++;
        }
        modelBid = new DefaultTableModel(data,colunas);
    }
    public void fillMyServersTable(){
        String[] colunas = {"ID Reserva","Nome"};
        Object[][] data = new Object[this.user.getServidoresAlocados().size()][2];
        int i=0;
        for (Server s : this.user.getServidoresAlocados()){
            data[i][0] = s.getIdReserva();
            data[i][1] = s.getNome();
            i++;
        }
        modelMy = new DefaultTableModel(data,colunas);
    }

    private void adjustColumnSizes(){
        demandServersTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        demandServersTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        demandServersTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        bidServersTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        bidServersTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        bidServersTable.getColumnModel().getColumn(2).setPreferredWidth(100);
                
        myServersTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        myServersTable.getColumnModel().getColumn(1).setPreferredWidth(150);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        demandPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        demandServersTable = new javax.swing.JTable();
        logoutButton1 = new javax.swing.JButton();
        buyButton = new javax.swing.JButton();
        bidPanel = new javax.swing.JPanel();
        bidSpinner = new javax.swing.JSpinner();
        jScrollPane2 = new javax.swing.JScrollPane();
        bidServersTable = new javax.swing.JTable();
        logoutButton2 = new javax.swing.JButton();
        bidButton = new javax.swing.JButton();
        myServersPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        myServersTable = new javax.swing.JTable();
        logoutButton3 = new javax.swing.JButton();
        bidButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setFocusable(false);
        jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        demandServersTable.setModel(this.modelDemand);
        jScrollPane1.setViewportView(demandServersTable);

        logoutButton1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        logoutButton1.setText("LOGOUT");
        logoutButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButton1ActionPerformed(evt);
            }
        });

        buyButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        buyButton.setText("USE SERVER");
        buyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout demandPanelLayout = new javax.swing.GroupLayout(demandPanel);
        demandPanel.setLayout(demandPanelLayout);
        demandPanelLayout.setHorizontalGroup(
            demandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(demandPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(demandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(demandPanelLayout.createSequentialGroup()
                        .addComponent(logoutButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        demandPanelLayout.setVerticalGroup(
            demandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, demandPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(demandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jTabbedPane1.addTab("Demand server", demandPanel);

        bidSpinner.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        bidSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0f, null, null, 0.01f));

        bidServersTable.setModel(this.modelBid);
        jScrollPane2.setViewportView(bidServersTable);

        logoutButton2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        logoutButton2.setText("LOGOUT");
        logoutButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButton1ActionPerformed(evt);
            }
        });

        bidButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        bidButton.setText("BID SERVER");
        bidButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bidButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bidPanelLayout = new javax.swing.GroupLayout(bidPanel);
        bidPanel.setLayout(bidPanelLayout);
        bidPanelLayout.setHorizontalGroup(
            bidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bidPanelLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(logoutButton2)
                .addGap(71, 71, 71)
                .addComponent(bidSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bidButton, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
            .addGroup(bidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bidPanelLayout.createSequentialGroup()
                    .addContainerGap(15, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(16, 16, 16)))
        );
        bidPanelLayout.setVerticalGroup(
            bidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bidPanelLayout.createSequentialGroup()
                .addGap(408, 408, 408)
                .addGroup(bidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logoutButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bidButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bidSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
            .addGroup(bidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bidPanelLayout.createSequentialGroup()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(61, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Bid server", bidPanel);

        myServersTable.setModel(this.modelMy);
        jScrollPane3.setViewportView(myServersTable);

        logoutButton3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        logoutButton3.setText("LOGOUT");
        logoutButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButton1ActionPerformed(evt);
            }
        });

        bidButton1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        bidButton1.setText("REMOVE SERVER");
        bidButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bidButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout myServersPanelLayout = new javax.swing.GroupLayout(myServersPanel);
        myServersPanel.setLayout(myServersPanelLayout);
        myServersPanelLayout.setHorizontalGroup(
            myServersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myServersPanelLayout.createSequentialGroup()
                .addGroup(myServersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(myServersPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, myServersPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(logoutButton3)
                        .addGap(167, 167, 167)
                        .addComponent(bidButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        myServersPanelLayout.setVerticalGroup(
            myServersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myServersPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(myServersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logoutButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bidButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        jTabbedPane1.addTab("My servers", myServersPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bidButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bidButtonActionPerformed
        double bid = new Double(bidSpinner.getValue().toString());
        int row = bidServersTable.getSelectedRow();
        String tipo = bidServersTable.getModel().getValueAt(row, 0).toString();
        double minBid = new Double(bidServersTable.getModel().getValueAt(row, 2).toString());
        Server servidor = new Server();
        
        for(Server s : db.getAllServers().get(tipo))
            if(s.getLastBid()==minBid) servidor = s;


        boolean r = servidor.setNewBid(bid);
        //Falta adicionar também à lista de servers do cliente!
        if(r){
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("resources/check.png"));
            JOptionPane.showMessageDialog(null, "Licitação efetuada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE, icon);
            
        } else{
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("resources/forbidden.png"));
            JOptionPane.showMessageDialog(null, "Valor licitado não é superior à licitação atual", "Aviso", JOptionPane.INFORMATION_MESSAGE, icon);
        }
        
        MenuForm menu = new MenuForm(this.login,this.user,this.connection,this.db);
        menu.setVisible(true);
        this.setVisible(false);
        // this.dispose();
    }//GEN-LAST:event_bidButtonActionPerformed

    private void buyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyButtonActionPerformed
        int row = bidServersTable.getSelectedRow();
        String tipo = bidServersTable.getModel().getValueAt(row, 0).toString();
        double minPreco = new Double(bidServersTable.getModel().getValueAt(row, 2).toString());
        Server servidor = new Server();
        
        for(Server s : db.getAllServers().get(tipo))
            if(s.getPrice()==minPreco) servidor = s;
        
        //Declarar como utilizado & adicionar à lista de servers do cliente!
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("resources/check.png"));
        JOptionPane.showMessageDialog(null, "Servidor adicionado à sua lista!", "Sucesso", JOptionPane.INFORMATION_MESSAGE, icon);
        
        MenuForm menu = new MenuForm(this.login,this.user,this.connection,this.db);
        menu.setVisible(true);
        this.setVisible(false);
        // this.dispose();
    }//GEN-LAST:event_buyButtonActionPerformed

    private void logoutButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButton1ActionPerformed
        // fecha o menu
        this.connection.closeConnection();
        // abre o login
        this.login.setVisible(true);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_logoutButton1ActionPerformed

    private void bidButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bidButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bidButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bidButton;
    private javax.swing.JButton bidButton1;
    private javax.swing.JPanel bidPanel;
    private javax.swing.JTable bidServersTable;
    private javax.swing.JSpinner bidSpinner;
    private javax.swing.JButton buyButton;
    private javax.swing.JPanel demandPanel;
    private javax.swing.JTable demandServersTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton logoutButton1;
    private javax.swing.JButton logoutButton2;
    private javax.swing.JButton logoutButton3;
    private javax.swing.JPanel myServersPanel;
    private javax.swing.JTable myServersTable;
    // End of variables declaration//GEN-END:variables
}
