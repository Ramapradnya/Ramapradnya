package com.BC181.Wiguna;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_film";
    private final static String TABLE_FILM = "t_film";
    private final static String KEY_ID_FILM = "ID_film";
    private final static String KEY_JUDUl = "Judul";
    private final static String KEY_TAHUN = "Tahun";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_GENRE = "Genre";
    private final static String KEY_PEMAIN = "Pemain";
    private final static String KEY_SIPNOSIS= "SIPNOSIS";
    private SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    private Context context;


    public DatabaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_FILM = "CREATE TABLE " +  TABLE_FILM
                + "(" + KEY_ID_FILM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUl + " TEXT, " + KEY_TAHUN + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_GENRE + " TEXT, "
                + KEY_PEMAIN + " TEXT, " + KEY_SIPNOSIS + " TEXT);";
        db.execSQL(CREATE_TABLE_FILM);
        inisialisasiFilmAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILM;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public void tambahFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUl, dataFilm.getJudul());
        cv.put(KEY_TAHUN, sdFormat.format(dataFilm.getTahun()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_PEMAIN, dataFilm.getPemain());
        cv.put(KEY_SIPNOSIS, dataFilm.getSipnosis());


        db.insert(TABLE_FILM, null, cv);
        db.close();
    }

    public void tambahFilm(Film dataFilm, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUl, dataFilm.getJudul());
        cv.put(KEY_TAHUN, sdFormat.format(dataFilm.getTahun()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_PEMAIN, dataFilm.getPemain());
        cv.put(KEY_SIPNOSIS, dataFilm.getSipnosis());

        db.insert(TABLE_FILM, null, cv);

    }

    public void editFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUl, dataFilm.getJudul());
        cv.put(KEY_TAHUN, sdFormat.format(dataFilm.getTahun()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_PEMAIN, dataFilm.getPemain());
        cv.put(KEY_SIPNOSIS, dataFilm.getSipnosis());


        db.update(TABLE_FILM, cv, KEY_ID_FILM + "=?", new String[]{String.valueOf(dataFilm.getIdFilm())});
        db.close();
    }

    public void hapusFilm(int idFilm) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FILM, KEY_ID_FILM + "=?", new String[]{String.valueOf(idFilm)});
        db.close();
    }

    public ArrayList<Film> getAllFilm() {
        ArrayList<Film> dataFilm = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if(csr.moveToFirst()){
            do {
                Date tempDate = new Date();
                try {
                    tempDate = sdFormat.parse(csr.getString(2));
                }
                catch (ParseException er){
                    er.printStackTrace();
                }

                Film tempFilm = new Film(
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6)
                );

                dataFilm.add(tempFilm);
            } while (csr.moveToNext());
        }
        return dataFilm;
    }

    private String storeImageFile(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return location;
    }

    private void inisialisasiFilmAwal(SQLiteDatabase db) {
        int idFilm = 0;
        Date tempDate = new Date();

        //menambah data Film 1
        try{
            tempDate = sdFormat.parse("2016");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film1 = new Film(
                idFilm,
                "Cek Toko Sebelah",
                tempDate,
                storeImageFile(R.drawable.cek),
                "Drama,Comedy",
                "Ernest Prakasa Sebagai Erwin\n" +
                        "Dion Wiyoko Sebagai Yohan\n" +
                        "Chew Kinwah Sebagai Koh Afuk\n" +
                        "Adinia Wirasti Sebagai Ayu\n" +
                        "Gisella Anastasia Sebagai Natalie",
                "Cek Toko Sebelah adalah film komedi Indonesia produksi Starvision Plus yang dirilis pada 28 Desember 2016 dan disutradari oleh Ernest Prakasa. Ide cerita film ini dibuat berdasarkan pada realitas etnis Tionghoa saat anak beranjak dewasa, kuliah yang tinggi, mirisnya ujung-ujungnya bekerja di toko orang tuanya sendiri. Film ini ditulis oleh Ernest Prakasa dan Jenny Jusuf dengan pengembangan cerita dari Meira Anastasia.\n" +
                        "Cek Toko Sebelah mendapatkan reaksi yang sangat positif dari penonton dan kritikus film, terutama untuk skenario film. Dalam Festival Film Indonesia 2017, film ini mendapatkan sembilan nominasi, termasuk Film Terbaik serta Sutradara Terbaik dan Aktor Terbaik untuk Ernest Prakasa, memenangkan satu untuk Skenario Asli Terbaik. Cek Toko Sebelah lebih berhasil pada ajang Festival Film Bandung 2017 di mana film ini memenangkan dua penghargaan (Film Terpuji dan Aktor Terpuji).."
        );

        tambahFilm(film1, db);
        idFilm++;

        // Data Film ke 2
        try{
            tempDate = sdFormat.parse("2014");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film2 = new Film(
                idFilm,
                "Comic 8",
                tempDate,
                storeImageFile(R.drawable.comic),
                "Aksi,komedi",
                "Mongol Stres\n" +
                        "Mudy Taylor\n" +
                        "Ernest Prakasa\n" +
                        "Kemal Palevi\n" +
                        "Bintang Bete\n" +
                        "Babe Cabita\n" +
                        "Fico Fachriza",
                "Comic 8 adalah film aksi-komedi Indonesia yang disutradarai oleh Anggy Umbara dan diproduseri oleh Hb Naveen dan Frederica. Film ini rilis pada tanggal 29 Januari 2014.\n" +
                        "Delapan anak muda masing-masing mempunyai alasan dan motif yang berbeda-beda dalam melakukan perampokan bank. Ada yang merampok karena galau, hobi, iseng, olahraga adrenalin, bahkan ada yang merampok untuk menghidupi panti asuhan dan rakyat miskin.\n" +
                        "Kedelapan Perampok tersebut akan terbagi menjadi tiga tim dengan kemampuan dan jam terbang yang berbeda-beda. Aksi pun berkembang dari perampokan yang awalnya terlihat seperti kebetulan yang aneh, terkepung oleh pasukan polisi dengan AKP-nya yang super cantik, sampai akhirnya mereka harus saling bekerja sama dan menemukan jawaban dari teka teki yang ada serta mencari jalan keluar terbaik untuk semua" );
        tambahFilm(film2, db);
        idFilm++;

        // Tambah Film 3

        try{
            tempDate = sdFormat.parse("2016");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film3 = new Film(
                idFilm,
                "Warkop DKI Reborn",
                tempDate,
                storeImageFile(R.drawable.warkop),
                "komedi",
                "Aliando Syarief sebagai Dono\n" +
                        "Adipati Dolken sebagai Kasino\n" +
                        "Randy Danistha sebagai Indro\n" +
                        "Indro Warkop sebagai Komandan Cok\n" +
                        "Mandra sebagai Karman\n" +
                        "Salshabilla Adriani sebagai Inka\n" +
                        "Ganindra Bimo sebagai Amir Muka\n" +
                        "Aurora Ribero\n" +
                        "Dewa Dayana",
                "Dono (Abimana Aryasatya), Kasino (Vino Bastian), dan Indro (Tora Sudiro) adalah tiga orang sahabat yang bekerja sebagai petugas keamanaan di organisasi CHIIPS (Cara Hebat Ikut Ikutan Pelayanan Sosial) dimana tugas mereka adalah membantu menertibkan dan menjaga keamanan masyarakat. Namun, tingkah mereka yang konyol dan bermasalah selalu membuat jengkel dan marah atasan mereka, Pak Boss (Ence Bagus), walaupun mereka berhasil lolos dari ancaman pemecatan.\n" +
                        "Dikarenakan mereka bertiga merupakan anggota CHIIPS yang memiliki rekor paling buruk, Boss memasukkan anggota CHIIPS dari Paris bernama Sophie (Hannah Al Rashid) untuk membantu mereka. Patroli pertama mereka berakhir buruk setelah mereka gagal mengejar seorang Copet (Arie Kriting), merusak warung warga, dan menyebabkan kebakaran pada sebuah pameran lukisan. Mereka ditangkap dan dibawa ke pengadilan di mana mereka bertiga dituntut untuk mengganti rugi dengan membayar denda sebesar 8 miliar rupiah atau mereka akan dipenjara.\n" +
                        "Dono, Kasino, dan Indro yang kebingungan mencari uang, mengunjungi paman Dono yaitu Pak Selamet (Tarzan) untuk meminjam uang. Rencana mereka gagal setelah mereka menyadari bahwa koper pemberian Pak Selamet berisi uang mainan dan hampir diamuk warga karena dikira mengedarkan uang palsu. Stress, Indro marah-marah dengan Indro dari masa depan (Indro (Warkop)) yang hanya ada di kepalanya. Sophie yang merasa kasihan mengajak mereka bertiga ke pesta. Namun di perjalanan, mereka tidak sengaja melihat seorang pria (Bene Dion) ditabrak oleh mobil misterius. Mereka kemudian membawa pria itu ke rumah sakit, saat sekarat, pria itu menyerahkan sebuah buku berisi peta harta karun pada mereka bertiga. Dono, Kasino, dan Indro pun akhirnya menerima peta tersebut dan berniat untuk mencari harta tersebut agar bisa membayar denda 8 miliar.\n" +
                        "Mereka pergi ke Malaysia sesuai petunjuk kode dalam peta dengan bantuan Sophie dan menjual barang-barang mereka. Sesampainya di bandara Malaysia, mereka menyadari bahwa tas berisi buku harta karun tersebut tertukar. Sempat berpencar untuk mencari tas tersebut, Kasino menemukan layar CCTV bandara yang menunjukkan tas mereka tertukar dengan tas seorang wanita berbaju merah (Nur Fazura). Sempat kejar-kejaran dengan menggunakan taksi hingga ke China Town, mereka kehilangan jejak wanita tersebut karena banyak wanita-wanita di sana yang juga menggunakan baju merah. Film berakhir dengan Indro dari masa depan mengucapkan salam ke penonton dan bersambung ke sekuel Warkop DKI Reborn: Jangkrik Boss! Part 2.");
        tambahFilm(film3, db);


    }

}