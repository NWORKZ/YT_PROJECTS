package com.nxiv.inlaypresentor.presentation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Presentation {

    private String name;
    private String author;
    private LocalDateTime creationTime;
    private List<Slide> slides;

    public Presentation(){
        slides = new ArrayList<>();
    }

    public Presentation(String name, String author, LocalDateTime creationTime) {
        this.name = name;
        this.author = author;
        this.creationTime = creationTime;

        slides = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public List<Slide> getSlides() {
        return slides;
    }

    public void setSlides(List<Slide> slides) {
        this.slides = slides;
    }
}
