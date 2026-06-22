package project.pbo.repository;

import project.pbo.domain.Pelanggan;
import project.pbo.infrastructure.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import project.pbo.repository.interfaces.ReadableRepository;
import project.pbo.repository.interfaces.WritableRepository;

public class PelangganRepository implements ReadableRepository<Pelanggan>, WritableRepository<Pelanggan> {
    private final DatabaseConnection databaseConnection;

    public PelangganRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Membaca seluruh data langsung dari PostgreSQL
    @Override
    public List<Pelanggan> getAll() {
        List<Pelanggan> pelangganList = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan";
        
        //Membuka koneksi, mempersiapkan statement SQL, dan mengeksekusi query
        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                Pelanggan pelanggan = new Pelanggan(
                    rs.getString("nik"),
                    rs.getString("nama_pelanggan"),
                    rs.getString("no_telp")
                );
                pelangganList.add(pelanggan);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal membaca data pelanggan", e);
        }
        return pelangganList;
    }

    // Melakukan INSERT langsung ke tabel PostgreSQL
    @Override
    public void save(Pelanggan baru) {
        String sql = "INSERT INTO pelanggan (nik, nama_pelanggan, no_telp) VALUES (?, ?, ?)";
        
        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, baru.getNik());
            stmt.setString(2, baru.getNamaPelanggan());
            stmt.setString(3, baru.getNoTelp());
            stmt.executeUpdate();
            System.out.println("[SUKSES] Data pelanggan berhasil disimpan ke database.");
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan data pelanggan", e);
        }
    }
}