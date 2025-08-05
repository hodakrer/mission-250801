package org.example;

public class Idiom {
    private int number;
    private String author;
    private String idiom;
    Idiom(int numberParam,
          String authorParam,
          String idiomParam){
        this.number = numberParam;
        this.author = authorParam;
        this.idiom = idiomParam;
    }
    public int getNumber() {return number;}
    public String getAuthor() {return author;}
    public void setAuthor(String authorParam) {this.author = authorParam;}
    public String getIdiom() {return idiom;}
    public void setIdiom(String idiomParam) {this.idiom = idiomParam;}

}


