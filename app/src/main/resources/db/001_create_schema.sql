CREATE TABLE akun (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL,
    CONSTRAINT akun_role_check
        CHECK (role IN ('admin', 'staff', 'owner'))
);

CREATE TABLE pelanggan (
    nik VARCHAR(20) PRIMARY KEY,
    nama_pelanggan VARCHAR(100) NOT NULL,
    no_telp VARCHAR(20) NOT NULL
);

CREATE TABLE kendaraan (
    plat_nomor VARCHAR(20) PRIMARY KEY,
    harga_sewa_per_hari NUMERIC(15,2) NOT NULL,
    jenis_kendaraan VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'TERSEDIA',
    jumlah_pintu INTEGER,
    jenis_transmisi VARCHAR(10),

    CONSTRAINT kendaraan_harga_check
        CHECK (harga_sewa_per_hari >= 0),

    CONSTRAINT kendaraan_jenis_check
        CHECK (jenis_kendaraan IN ('Mobil', 'Motor')),

    CONSTRAINT kendaraan_status_check
        CHECK (status IN ('TERSEDIA', 'SEDANG DISEWA')),

    CONSTRAINT kendaraan_subtype_check CHECK (
        (
            jenis_kendaraan = 'Mobil'
            AND jumlah_pintu IS NOT NULL
            AND jenis_transmisi IS NULL
        )
        OR
        (
            jenis_kendaraan = 'Motor'
            AND jumlah_pintu IS NULL
            AND jenis_transmisi IS NOT NULL
        )
    )
);

CREATE SEQUENCE transaksi_id_seq START WITH 1;

CREATE TABLE transaksi (
    id_transaksi VARCHAR(20) PRIMARY KEY,
    nik_pelanggan VARCHAR(20) NOT NULL,
    plat_nomor VARCHAR(20) NOT NULL,
    durasi_hari INTEGER NOT NULL,
    total_bayar NUMERIC(15,2) NOT NULL,
    status VARCHAR(10) NOT NULL,
    is_delivery BOOLEAN NOT NULL DEFAULT FALSE,
    zona_kirim CHAR(1),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT transaksi_pelanggan_fk
        FOREIGN KEY (nik_pelanggan)
        REFERENCES pelanggan(nik),

    CONSTRAINT transaksi_kendaraan_fk
        FOREIGN KEY (plat_nomor)
        REFERENCES kendaraan(plat_nomor),

    CONSTRAINT transaksi_durasi_check
        CHECK (durasi_hari > 0),

    CONSTRAINT transaksi_total_check
        CHECK (total_bayar >= 0),

    CONSTRAINT transaksi_status_check
        CHECK (status IN ('AKTIF', 'SELESAI')),

    CONSTRAINT transaksi_zona_check
        CHECK (
            zona_kirim IS NULL
            OR zona_kirim IN ('A', 'B', 'C')
        )
);

CREATE UNIQUE INDEX satu_transaksi_aktif_per_kendaraan
ON transaksi (plat_nomor)
WHERE status = 'AKTIF';