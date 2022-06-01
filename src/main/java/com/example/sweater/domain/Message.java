package com.example.sweater.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

//@Table(name = "messages")
@Entity
public class Message {
    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO)
//    @GeneratedValue(
//            strategy= GenerationType.AUTO,
//            generator="native"
//    )
//    @GenericGenerator(
//            name = "native",
//            strategy = "native"
//    )
    private Long id;

    private String text;
    private String tag;

    public Message() {
    }

    public Message(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public String getTag() {
        return tag;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
