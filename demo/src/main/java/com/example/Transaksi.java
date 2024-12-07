/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Transaksi extends javax.swing.JFrame {
    Connection conn = DatabaseConnection.getConnection();
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;
    
    String driver = "org.postgresql.Driver";
    DefaultTableModel model = new DefaultTableModel();
    private final ArrayList<String[]> dataList = new ArrayList<>();
    private final String[] columns = {"NIK",  "idTiket", "Jumlah", "Tanggal"};
    private boolean isProgramInitialized = false;

    /**
     * Creates new form Login
     */
    public Transaksi() {
        initComponents();
        tampilkanTanggal();
        tableModel();
        loadNIK();
        loadKonser();
        loadCategory();
        tampilStok();
        tampilHarga();
        tampilTiket();
    }
    
    private void tampilkanTanggal() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String tanggalSaatIni = sdf.format(new Date());

        lblTanggal.setText("" + tanggalSaatIni);
    }
    
     private void tableModel() {
        DefaultTableModel model = new DefaultTableModel();

        Tabel.setModel(model);

        model.addColumn("NIK");
        model.addColumn("Id Tiket");
        model.addColumn("Qty");
        model.addColumn("Tanggal");
    }
    
    
public final void getAllData() {
        String sql = "SELECT nik, id_tiket,jumlah,tanggal FROM pemesanan";
        try (Connection conn = DatabaseConnection.getConnection(); 
            PreparedStatement ps = conn.prepareStatement(sql); 
            ResultSet rs = ps.executeQuery()) {

            dataList.clear(); 

            while (rs.next()) {
                String[] row = {
                    rs.getString("idPemesanan"),
                    rs.getString("NIK"),
                    rs.getString("idTiket"),
                    Integer.toString(rs.getInt("Jumlah")), 
                    rs.getString("Tanggal"), 
                };
                dataList.add(row); 
            }
            System.out.println("Data berhasil diambil dari database.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }

public final void refreshModel() {
        model.setColumnIdentifiers(columns);
        for (String[] data : dataList) {
            model.addRow(data);
        }
        Tabel.setModel(model);
    }

private void loadNIK() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();

            String sqlPelanggan = "SELECT nik FROM Pelanggan order by nik";
            ResultSet rsPelanggan = stmt.executeQuery(sqlPelanggan);

            cboxNIK.removeAllItems(); 

            while (rsPelanggan.next()) {
                cboxNIK.addItem(rsPelanggan.getString("nik"));
                System.out.println("Loaded NIK: " + sqlPelanggan);
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat memuat data Pelanggan: " + ex.getMessage());
        }
    }
    
private void loadNamaPelanggan(String nik) {
        try {
            String sql = "SELECT nama FROM PELANGGAN WHERE nik = ?";
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nik); 
            rs = pstmt.executeQuery(); 

            if (rs.next()) {
                String nama = rs.getString("nama");
                lblPelanggan.setText(nama); 
            } else {
                lblPelanggan.setText(""); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(Transaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

private void loadKonser() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();

            String sqlKonser = "SELECT id_konser FROM Konser";
            ResultSet rsPelanggan = stmt.executeQuery(sqlKonser);

            cboxKonser.removeAllItems(); 

            while (rsPelanggan.next()) {
                cboxKonser.addItem(rsPelanggan.getString("id_konser"));
                System.out.println("Loaded Konser: " + sqlKonser);
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat memuat data Konser: " + ex.getMessage());
        }
    }

private void loadNamaKonser(String id_konser) {
        try {
            String sql = "SELECT nama_konser FROM konser WHERE id_konser = ?";
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id_konser); 
            rs = pstmt.executeQuery(); 

            if (rs.next()) {
                String nama = rs.getString("nama_konser");
                lblKonser.setText(nama); 
            } else {
                lblKonser.setText(""); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(Transaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

private void loadCategory() {
       try {
           cboxCat.removeAllItems();
        String sql = "select id_cat from cat where id_konser = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cboxKonser.getSelectedItem().toString());
        ResultSet res = ps.executeQuery();
           while (res.next()) {
               String id = res.getString("id_cat");
               cboxCat.addItem(id);
               
           }
    } catch (Exception e) {
    }
}

private void tampilTiket(){
        try {
            String sql = "select id_tiket from tiket where id_konser = ? and id_cat = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,cboxKonser.getSelectedItem().toString());
            ps.setString(2, cboxCat.getSelectedItem().toString());
            ResultSet res= ps.executeQuery();
            if (res.next()) {
                String stok = res.getString("id_tiket");
                lblTiket.setText(String.valueOf(stok));
            }
        } catch (Exception e) {
        }
    }


private void tampilStok(){
        try {
            String sql = "select stok from tiket where id_konser = ? and id_cat = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,cboxKonser.getSelectedItem().toString());
            ps.setString(2, cboxCat.getSelectedItem().toString());
            ResultSet res= ps.executeQuery();
            if (res.next()) {
                int stok = res.getInt("stok");
                lblStok.setText(String.valueOf(stok));
            }
        } catch (Exception e) {
        }
    }

private void tampilHarga(){
        try {
            String sql = "select harga from tiket where id_konser = ? and id_cat = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,cboxKonser.getSelectedItem().toString());
            ps.setString(2, cboxCat.getSelectedItem().toString());
            ResultSet res= ps.executeQuery();
            if (res.next()) {
                int stok = res.getInt("harga");
                lblHarga.setText(String.valueOf(stok));
            }
        } catch (Exception e) {
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

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabel = new javax.swing.JTable();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblTanggal = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cboxNIK = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblPelanggan = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cboxKonser = new javax.swing.JComboBox<>();
        cboxCat = new javax.swing.JComboBox<>();
        tfJumlah = new javax.swing.JTextField();
        tfHarga = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        lblStok = new javax.swing.JLabel();
        btnCek = new javax.swing.JButton();
        lblHarga = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblKonser = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblTiket = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TRANSAKSI");

        Tabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(Tabel);

        btnHapus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnTambah.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        lblTanggal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTanggal.setForeground(new java.awt.Color(255, 255, 255));
        lblTanggal.setText("dd/mm/yyyy");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Tanggal");

        cboxNIK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));
        cboxNIK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboxNIKActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nama Pelanggan");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("NIK");

        lblPelanggan.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblPelanggan.setForeground(new java.awt.Color(255, 255, 255));
        lblPelanggan.setText("-");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboxNIK, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPelanggan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboxNIK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(lblPelanggan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblTanggal))
                .addContainerGap(171, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 153, 153));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID Konser     :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Pilih Kategori:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Jumlah          :");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Harga            :");

        cboxKonser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));
        cboxKonser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboxKonserActionPerformed(evt);
            }
        });

        cboxCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));
        cboxCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboxCatActionPerformed(evt);
            }
        });

        tfJumlah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfJumlahMouseClicked(evt);
            }
        });
        tfJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfJumlahActionPerformed(evt);
            }
        });

        tfHarga.setEditable(false);
        tfHarga.setBackground(new java.awt.Color(0, 153, 153));
        tfHarga.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        tfHarga.setForeground(new java.awt.Color(255, 255, 255));
        tfHarga.setText("-");
        tfHarga.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Stok, Harga   :");

        lblStok.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblStok.setForeground(new java.awt.Color(255, 255, 255));
        lblStok.setText("-");

        btnCek.setText("Cek");
        btnCek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCekActionPerformed(evt);
            }
        });

        lblHarga.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblHarga.setForeground(new java.awt.Color(255, 255, 255));
        lblHarga.setText("-");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nama Konser:");

        lblKonser.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblKonser.setForeground(new java.awt.Color(255, 255, 255));
        lblKonser.setText("-");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("ID Tiket         :");

        lblTiket.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTiket.setForeground(new java.awt.Color(255, 255, 255));
        lblTiket.setText("-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(tfJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCek)))
                        .addGap(77, 77, 77))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cboxCat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblKonser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cboxKonser, 0, 217, Short.MAX_VALUE))
                            .addComponent(lblTiket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(lblStok, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(52, 52, 52))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboxKonser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblKonser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2)
                        .addComponent(cboxCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lblTiket, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lblStok)
                    .addComponent(lblHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCek, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tfHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("Bayar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnReset)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4)
                        .addGap(105, 105, 105)
                        .addComponent(jButton1)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHapus)
                    .addComponent(btnTambah)
                    .addComponent(btnReset)
                    .addComponent(jButton1)
                    .addComponent(jButton4))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("Akun");

        jMenuItem2.setText("Logout");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Close");
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Data");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Transaksi");
        jMenuBar1.add(jMenu3);

        jMenu4.setText("Laporan");
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void cboxCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxCatActionPerformed
        // TODO add your handling code here:
        tampilStok();
        tampilHarga();
        tampilTiket();
    }//GEN-LAST:event_cboxCatActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:                                       
        String nik = cboxNIK.getSelectedItem().toString();  
        String idTiket = lblTiket.getText();  
        String qtyStr = tfJumlah.getText();  
        String tanggal = lblTanggal.getText();  

        if (idTiket == null || idTiket.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "ID Tiket tidak ditemukan!");
            return;  
        }

        int qty = 0;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Jumlah tidak valid! Harap masukkan angka yang valid.");
            return;  
        }

        if (qty <= 0) {
            JOptionPane.showMessageDialog(null, "Jumlah pembelian harus lebih dari 0.");
            return;
        }

        int stok = Integer.parseInt(lblStok.getText());

        if (qty > stok) {
            JOptionPane.showMessageDialog(null, "Jumlah melebihi stok yang tersedia!");
            return;  
        }

        DefaultTableModel model = (DefaultTableModel) Tabel.getModel();  
        model.addRow(new Object[]{nik, idTiket, qty, tanggal});  

        JOptionPane.showMessageDialog(null, "Item berhasil ditambahkan ke keranjang!");
    }//GEN-LAST:event_btnTambahActionPerformed

    private void cboxNIKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxNIKActionPerformed
        // TODO add your handling code here:
        isProgramInitialized = true;

        Object selectedItem = cboxNIK.getSelectedItem();
        if (selectedItem != null) {
            String NIK = selectedItem.toString();
            loadNamaPelanggan(NIK);
        }
    }//GEN-LAST:event_cboxNIKActionPerformed

    private void cboxKonserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxKonserActionPerformed
        // TODO add your handling code here:
        isProgramInitialized = true;
        
        Object selectedItem = cboxKonser.getSelectedItem();
        if (selectedItem != null) {
            String idKonser = selectedItem.toString();
            loadNamaKonser(idKonser);
        tampilStok();
        tampilHarga();
        tampilTiket();
        loadCategory();
        }
    }//GEN-LAST:event_cboxKonserActionPerformed

    private void tfJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfJumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfJumlahActionPerformed

    private void tfJumlahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfJumlahMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tfJumlahMouseClicked

    private void btnCekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCekActionPerformed
        // TODO add your handling code here:
        int harga = Integer.parseInt(lblHarga.getText());
        int stok = Integer.parseInt(lblStok.getText());

        int jumlah;
        try {
            jumlah = Integer.parseInt(tfJumlah.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Jumlah tidak valid! Silakan masukkan angka yang valid.");
            return;
        }

        if (jumlah > stok) {
            JOptionPane.showMessageDialog(null, "Jumlah melebihi stok yang tersedia!");
            tfHarga.setText(""); 
        } else {
            int totalHarga = harga * jumlah;
            tfHarga.setText("" + totalHarga);
        }
    }//GEN-LAST:event_btnCekActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
         DefaultTableModel model = (DefaultTableModel) Tabel.getModel();

        int selectedRow = Tabel.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Silakan pilih baris yang ingin dihapus.");
            return;  
        }
            model.removeRow(selectedRow);
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
         DefaultTableModel model = (DefaultTableModel) Tabel.getModel();

        int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus semua data?", 
                                                     "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int rowCount = model.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {  
                model.removeRow(i);
            }
            JOptionPane.showMessageDialog(null, "Semua data berhasil dihapus!");
        }
    }//GEN-LAST:event_btnResetActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabel;
    private javax.swing.JButton btnCek;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cboxCat;
    private javax.swing.JComboBox<String> cboxKonser;
    private javax.swing.JComboBox<String> cboxNIK;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JLabel lblKonser;
    private javax.swing.JLabel lblPelanggan;
    private javax.swing.JLabel lblStok;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblTiket;
    private javax.swing.JTextField tfHarga;
    private javax.swing.JTextField tfJumlah;
    // End of variables declaration//GEN-END:variables
}
