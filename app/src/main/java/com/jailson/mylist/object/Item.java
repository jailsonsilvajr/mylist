package com.jailson.mylist.object;

import java.io.Serializable;

public class Item implements Serializable {

    private String id;
    private String name;
    private String mark;
    private double price;
    private int quantity;
    private String id_list;
    private int into_cart;
    private String url_img;

    public Item(String id, String name, String mark, double price, int quantity, String id_list, int into_cart, String url_img) {
        this.id = id;
        this.name = name;
        this.mark = mark;
        this.price = price;
        this.quantity = quantity;
        this.id_list = id_list;
        this.into_cart = into_cart;
        this.url_img = url_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQtd() {
        return quantity;
    }

    public void setQtd(int qtd) {
        this.quantity = qtd;
    }

    public String getId_list() {
        return id_list;
    }

    public void setId_list(String id_list) {
        this.id_list = id_list;
    }

    public int getInto_cart() {
        return into_cart;
    }

    public void setInto_cart(int into_cart) {
        this.into_cart = into_cart;
    }

    public String getUrl_img() {
        return url_img;
    }
}
