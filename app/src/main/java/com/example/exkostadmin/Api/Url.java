package com.example.exkostadmin.Api;

public class Url {
    //    api endpoint untuk request volley
    public static final String BASE = "http://192.168.43.91/exkost/";
    public static final String API_LOGIN = BASE+"api_admin/login";
    public static final String API_AKUN = BASE+"api_admin/akun";
    public static final String API_REGISTER = BASE+"api_admin/register";
    public static final String API_HOME = BASE+"api_home/home";
    public static final String API_BARANG = BASE+"api_barang/barang";
    public static final String ADD_BARANG = BASE+"api_barang/tambahbarang";
    public static final String API_TAWAR = BASE+"api_barang/tawaran";
    public static final String ASSET_BARANG = BASE+"assets/barang/";
    public static final String API_CART = BASE+"api_cart/cart";
    public static final String DEL_CART = BASE+"api_cart/delcart";
    public static final String API_TERIMA = BASE+"api_barang/terima";
    public static final String AUC_BERLANGSUNG = BASE+"api_lelang/berlangsung";
    public static final String AUC_KIRIM = BASE+"api_lelang/kirim";
    public static final String AUC_SELESAI = BASE+"api_lelang/selesai";
    public static final String AUC_BARANG = BASE+"api_lelang/barang";
    public static final String AUC_SEND = BASE+"api_lelang/view_send";
    public static final String TO_SEND = BASE+"api_lelang/to_send";
    public static final String TRANS_VIEW = BASE+"api_lelang/view_trans";
    public static final String ASSET_TRANSFER = BASE+"assets/transfer/";
    public static final String TRANS_PROC = BASE+"api_lelang/proses_konfirm";
    public static final String API_PROFIL = BASE+"api_admin/akun";
    public static final String UPDATE_PROFIL = BASE+"api_admin/update_profil";
    public static final String API_SEARCH = BASE+"api_cari/cari";
    public static final String API_JENIS = BASE+"api_barang/jenis";
    public static final String API_MYTOPUP = BASE+"api_topup/allmyTopup";
    public static final String BANK_ADMIN = BASE+"api_topup/bank_admin";
    public static final String API_CHECK = BASE+"api_topup/checkout";
    public static final String API_DETAILTOP = BASE+"api_topup/detailTopup";
    public static final String ASSET_TOPUP = BASE+"assets/topup/";
    public static final String UPLOAD_BUKTI = BASE+"api_topup/uploadBukti";
    public static final String DEL_TOPUP = BASE+"api_topup/hapus_topup";
    public static final String LUPA_PASS = BASE+"api_resetpass/email_reset_password_validation";
    public static final String RESET_PASS = BASE+"api_resetpass/reset_password_validation";
}