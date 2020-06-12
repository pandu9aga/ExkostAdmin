package com.example.exkostadmin;

class App {
    private String Title;
    private String Subtitle;
    private int Icon;

    App(String Title, String Subtitle, int Icon) {
        this.Title = Title;
        this.Subtitle = Subtitle;
        this.Icon = Icon;
    }

    String getTitle() {
        return this.Title;
    }

    String getSubtitle() {
        return Subtitle;
    }

    int getIcon() {
        return Icon;
    }

}
