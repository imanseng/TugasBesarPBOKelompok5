package project.pbo.repository;

import project.pbo.domain.Kendaraan;
import project.pbo.domain.Mobil;
import project.pbo.domain.Motor;
import project.pbo.infrastructure.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import project.pbo.repository.interfaces.ModifiableRepository;
import project.pbo.repository.interfaces.ReadableRepository;
import project.pbo.repository.interfaces.WritableRepository;

public class KendaraanRepository implements ReadableRepository<Kendaraan>, WritableRepository<Kendaraan>, ModifiableRepository<Kendaraan, String> {
    private final DatabaseConnection databaseConnection;

    public KendaraanRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public List<Kendaraan> getAll() {
        List<Kendaraan> kendaraanList = new ArrayList<>();
        String sql = "SELECT * FROM kendaraan";
        
        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                String platNomor = rs.getString("plat_nomor");
                double harga = rs.getDouble("harga_sewa_per_hari");
                String jenis = rs.getString("jenis_kendaraan");
                String status = rs.getString("status");
                
                Kendaraan k;
                // Penentuan Objek berdasar kolom jenis_kendaraan
                if ("Mobil".equalsIgnoreCase(jenis)) {
                    int jumlahPintu = rs.getInt("jumlah_pintu");
                    k = new Mobil(platNomor, harga, jenis, jumlahPintu);
                } else {
                    String jenisTransmisi = rs.getString("jenis_transmisi");
                    k = new Motor(platNomor, harga, jenis, jenisTransmisi);
                }
                k.setStatus(status);
                kendaraanList.add(k);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal membaca data kendaraan", e);
        }
        return kendaraanList;
    }

    @Override
    public void save(Kendaraan kendaraan) {
        String sql = "INSERT INTO kendaraan (plat_nomor, harga_sewa_per_hari, jenis_kendaraan, status, jumlah_pintu, jenis_transmisi) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, kendaraan.getPlatNomor());
            stmt.setDouble(2, kendaraan.getHargaSewaPerHari());
            stmt.setString(3, kendaraan.getJenisKendaraan());
            stmt.setString(4, kendaraan.getStatus());
            
            // Mengatur kolom opsional sesuai jenis
            if (kendaraan instanceof Mobil) {
                stmt.setInt(5, ((Mobil) kendaraan).getJumlahPintu());
                stmt.setNull(6, java.sql.Types.VARCHAR);
            } else if (kendaraan instanceof Motor) {
                stmt.setNull(5, java.sql.Types.INTEGER);
                stmt.setString(6, ((Motor) kendaraan).getJenisTransmisi());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan data kendaraan", e);
        }
    }

    public void updateStatus(String platNomor, String statusBaru) {
        String sql = "UPDATE kendaraan SET status = ? WHERE plat_nomor = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, statusBaru);
            stmt.setString(2, platNomor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengubah status kendaraan", e);
        }
    }

    @Override
    public void update(Kendaraan kendaraan) {
        // Implementasi default untuk ModifiableRepository
        updateStatus(kendaraan.getPlatNomor(), kendaraan.getStatus());
    }

    @Override
        public void delete(String platNomor) {
        String sql = "DELETE FROM kendaraan WHERE plat_nomor = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, platNomor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Cek apakah errornya karena Foreign Key (violation)
            if (e.getSQLState().equals("23503")) {
                System.out.println("\n[GAGAL] Kendaraan ini tidak bisa dihapus karena masih memiliki riwayat transaksi/peminjaman.");
            } else {
                throw new RuntimeException("Gagal menghapus kendaraan", e);
            }
        }
    }
}