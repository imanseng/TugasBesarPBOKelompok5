INSERT INTO akun (username, password, role) VALUES
('admin123', 'admin123', 'admin'),
('staff123', 'staff123', 'staff'),
('owner123', 'owner123', 'owner');

INSERT INTO pelanggan (nik, nama_pelanggan, no_telp) VALUES
('1234567890123456', 'Budi Santoso', '081234567890'),
('3201234567890001', 'Siti Aminah', '085712345678'),
('3171012345678901', 'Andi Wijaya', '089988776655');

INSERT INTO kendaraan (
    plat_nomor,
    harga_sewa_per_hari,
    jenis_kendaraan,
    status,
    jumlah_pintu,
    jenis_transmisi
) VALUES
('B 1234 ABC', 300000, 'Mobil', 'TERSEDIA', 4, NULL),
('D 5678 DEF', 100000, 'Motor', 'TERSEDIA', NULL, 'Matic');
