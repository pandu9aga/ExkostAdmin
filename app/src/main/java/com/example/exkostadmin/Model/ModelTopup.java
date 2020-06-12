package com.example.exkostadmin.Model;

public class ModelTopup {

    String id,nominal,status,bukti;

    public ModelTopup(){}

    public ModelTopup(String id,String nominal, String status, String bukti) {
        this.id = id;
        this.nominal = nominal;
        this.status = status;
        this.bukti = bukti;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
       this.id = id;
    }

    public String getNominal() {
        return nominal;
    }
    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getBukti() {
        return bukti;
    }
    public void setBukti(String bukti) { this.bukti = bukti; }
}
