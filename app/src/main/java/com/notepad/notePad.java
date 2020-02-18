package com.notepad;

public class notePad {

    public String notBaslik;
    public String notZaman;

    public notePad(String notBaslik, String notZaman) {
        this.notBaslik = notBaslik;
        this.notZaman = notZaman;
    }

    public notePad(){

    }

    public String getNotBaslik() {
        return notBaslik;
    }

    public void setNotBaslik(String notBaslik) {
        this.notBaslik = notBaslik;
    }

    public String getNotZaman() {
        return notZaman;
    }

    public void setNotZaman(String notZaman) {
        this.notZaman = notZaman;
    }
}
