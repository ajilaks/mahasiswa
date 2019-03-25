package com.project.mahasiswa;

/**
 * Created by OJI on 24/03/2019.
 */

public class mahasiswaModel {

    String nama;
    String key;
    mahasiswaModel(){


    }

    public mahasiswaModel(String nama,String key){
        this.nama = nama;
        this.key = key;

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
