package com.chharkoi.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Shopkeeper.
 */
@Entity
@Table(name = "shopkeeper")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "shopkeeper")
public class Shopkeeper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "location")
    private String location;

    @Column(name = "shop_type")
    private String shopType;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "web_address")
    private String webAddress;

    @Column(name = "shop_tin")
    private String shopTin;

    @Lob
    @Column(name = "shop_logo")
    private byte[] shopLogo;

    @Column(name = "shop_logo_content_type")
    private String shopLogoContentType;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Shopkeeper name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public Shopkeeper address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public Shopkeeper location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShopType() {
        return shopType;
    }

    public Shopkeeper shopType(String shopType) {
        this.shopType = shopType;
        return this;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getPhone() {
        return phone;
    }

    public Shopkeeper phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public Shopkeeper email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public Shopkeeper webAddress(String webAddress) {
        this.webAddress = webAddress;
        return this;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getShopTin() {
        return shopTin;
    }

    public Shopkeeper shopTin(String shopTin) {
        this.shopTin = shopTin;
        return this;
    }

    public void setShopTin(String shopTin) {
        this.shopTin = shopTin;
    }

    public byte[] getShopLogo() {
        return shopLogo;
    }

    public Shopkeeper shopLogo(byte[] shopLogo) {
        this.shopLogo = shopLogo;
        return this;
    }

    public void setShopLogo(byte[] shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopLogoContentType() {
        return shopLogoContentType;
    }

    public Shopkeeper shopLogoContentType(String shopLogoContentType) {
        this.shopLogoContentType = shopLogoContentType;
        return this;
    }

    public void setShopLogoContentType(String shopLogoContentType) {
        this.shopLogoContentType = shopLogoContentType;
    }

    public LocalDate getDate() {
        return date;
    }

    public Shopkeeper date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public Shopkeeper user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shopkeeper shopkeeper = (Shopkeeper) o;
        if(shopkeeper.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, shopkeeper.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Shopkeeper{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", address='" + address + "'" +
            ", location='" + location + "'" +
            ", shopType='" + shopType + "'" +
            ", phone='" + phone + "'" +
            ", email='" + email + "'" +
            ", webAddress='" + webAddress + "'" +
            ", shopTin='" + shopTin + "'" +
            ", shopLogo='" + shopLogo + "'" +
            ", shopLogoContentType='" + shopLogoContentType + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
