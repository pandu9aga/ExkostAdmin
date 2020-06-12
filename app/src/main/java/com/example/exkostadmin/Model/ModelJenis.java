package com.example.exkostadmin.Model;

public class ModelJenis {

    String namajenis, idjenis;

    public ModelJenis(){}

    public ModelJenis(String namajenis, String idjenis) {
        this.idjenis = idjenis;
        this.namajenis = namajenis;
    }

    public String getNamaJenis() {
        return namajenis;
    }
    public void setNamaJenis(String namajenis) {
        this.namajenis = namajenis;
    }

    public String getIdJenis() {
        return idjenis;
    }
    public void setIdJenis(String idjenis) {
        this.idjenis = idjenis;
    }

}
