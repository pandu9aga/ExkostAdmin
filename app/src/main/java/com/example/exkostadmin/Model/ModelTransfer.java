package com.example.exkostadmin.Model;

public class ModelTransfer {
    String namabarang, idpemenang, namaakun, nominal, rekening, gambarbukti, stattrans;

    public ModelTransfer(){}

    public ModelTransfer(String namabarang, String idpemenang, String namaakun, String nominal, String rekening, String gambarbukti, String stattrans) {
        this.namabarang = namabarang;
        this.idpemenang= idpemenang;
        this.namaakun = namaakun;
        this.nominal = nominal;
        this.rekening = rekening;
        this.gambarbukti = gambarbukti;
        this.stattrans = stattrans;
    }

    public String getNamaBarang() {
        return namabarang;
    }
    public void setNamaBarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public String getIdPemenang() {
        return idpemenang;
    }
    public void setIdPemenang(String idpemenang) {
        this.idpemenang = idpemenang;
    }

    public String getNamaAkun() {
        return namaakun;
    }
    public void setNamaAkun(String namaakun) { this.namaakun = namaakun; }

    public String getNominal() {
        return nominal;
    }
    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getRekening() {
        return rekening;
    }
    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public String getGambarBukti() {
        return gambarbukti;
    }
    public void setGambarBukti(String gambarbukti) {
        this.gambarbukti = gambarbukti;
    }

    public String getStatTrans() {
        return stattrans;
    }
    public void setStatTrans(String stattrans) {
        this.stattrans = stattrans;
    }
}

