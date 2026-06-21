package project.pbo.repository;

import project.pbo.domain.Transaksi;
import project.pbo.infrastructure.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransaksiRepository {
    private final DatabaseConnection databaseConnection;

    public TransaksiRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<Transaksi> findAll() {
        List<Transaksi> transaksiList = new ArrayList<>();
        String sql = "SELECT * FROM transaksi";

        try (Connection conn = databaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String zonaKirim = rs.getString("zona_kirim");
                if (zonaKirim == null) {
                    zonaKirim = "-";
                }

                Transaksi t = new Transaksi(
                        rs.getString("id_transaksi"),
                        rs.getString("nik_pelanggan"),
                        rs.getString("plat_nomor"),
                        rs.getInt("durasi_hari"),
                        rs.getDouble("total_bayar"),
                        rs.getString("status"),
                        rs.getBoolean("is_delivery"),
                        zonaKirim);
                transaksiList.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal membaca data transaksi", e);
        }
        return transaksiList;
    }

    public void tambah(Transaksi transaksi) {
        String sql = "INSERT INTO transaksi (id_transaksi, nik_pelanggan, plat_nomor, durasi_hari, total_bayar, status, is_delivery, zona_kirim) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaksi.getIdTransaksi());
            stmt.setString(2, transaksi.getNikPelanggan());
            stmt.setString(3, transaksi.getPlatNomor());
            stmt.setInt(4, transaksi.getDurasiHari());
            stmt.setDouble(5, transaksi.getTotalBayar());
            stmt.setString(6, transaksi.getStatus());
            stmt.setBoolean(7, transaksi.isDelivery());

            if (transaksi.getZonaKirim() == null || transaksi.getZonaKirim().equals("-")) {
                stmt.setNull(8, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(8, transaksi.getZonaKirim());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan data transaksi", e);
        }
    }

    // Fungsi tambahan untuk memperbarui total bayar & status saat pengembalian
    public void update(Transaksi transaksi) {
        String sql = "UPDATE transaksi SET total_bayar = ?, status = ? WHERE id_transaksi = ?";
        try (Connection conn = databaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, transaksi.getTotalBayar());
            stmt.setString(2, transaksi.getStatus());
            stmt.setString(3, transaksi.getIdTransaksi());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengupdate data transaksi", e);
        }
    }
}