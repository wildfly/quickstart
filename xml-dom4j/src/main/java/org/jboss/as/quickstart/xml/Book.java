package org.jboss.as.quickstart.xml;

import java.io.Serializable;
import java.util.Date;

/**
 * Simple Java Bean which is used as a data structure. It stores information about book.
 * 
 * @author baranowb
 * 
 */
@SuppressWarnings("serial")
public class Book implements Serializable{

    private String author;
    private String title;
    private String genre;
    private float price;
    private Date publishDate;
    private String description;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
