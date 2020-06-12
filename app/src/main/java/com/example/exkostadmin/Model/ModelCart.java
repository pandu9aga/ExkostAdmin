package com.example.exkostadmin.Model;

public class ModelCart {

    String namabarang, idbarang, namajenis, hargabarang, waktulelang, gambarbarang, highbid, mybid, statbid, stattrans;

    public ModelCart(){}

    public ModelCart(String namabarang, String idbarang, String namajenis, String hargabarang, String waktulelang, String gambarbarang, String highbid, String mybid, String statbid, String stattrans) {
        this.namabarang = namabarang;
        this.idbarang = idbarang;
        this.hargabarang = hargabarang;
        this.namajenis = namajenis;
        this.waktulelang = waktulelang;
        this.gambarbarang = gambarbarang;
        this.highbid = highbid;
        this.mybid = mybid;
        this.statbid = statbid;
        this.stattrans =stattrans;
    }

    public String getNamaBarang() {
        return namabarang;
    }
    public void setNamaBarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public String getIdBarang() {
        return idbarang;
    }
    public void setIdBarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public String getNamaJenis() {
        return namajenis;
    }
    public void setNamaJenis(String namajenis) { this.namajenis = namajenis; }

    public String getHargaBarang() {
        return hargabarang;
    }
    public void setHargaBarang(String hargabarang) {
        this.hargabarang = hargabarang;
    }

    public String getWaktuLelang() {
        return waktulelang;
    }
    public void setWaktuLelang(String waktulelang) {
        this.waktulelang = waktulelang;
    }

    public String getGambarBarang() {
        return gambarbarang;
    }
    public void setGambarBarang(String gambarbarang) {
        this.gambarbarang = gambarbarang;
    }

    public String getHighBid() {
        return highbid;
    }
    public void setHighBid(String highbid) {
        this.highbid = highbid;
    }

    public String getMyBid() {
        return mybid;
    }
    public void setMyBid(String mybid) {
        this.mybid = mybid;
    }

    public String getStatBid() {
        return statbid;
    }
    public void setStatBid(String statbid) {
        this.statbid = statbid;
    }

    public String getStatTrans() {
        return stattrans;
    }
    public void setStatTrans(String stattrans) {
        this.stattrans = stattrans;
    }
}
