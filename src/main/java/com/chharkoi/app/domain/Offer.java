package com.chharkoi.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Offer.
 */
@Entity
@Table(name = "offer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "offer")
public class Offer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "sale_name", nullable = false)
    private String saleName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @NotNull
    @Column(name = "real_price", nullable = false)
    private Integer realPrice;

    @NotNull
    @Column(name = "sale_price", nullable = false)
    private Integer salePrice;

    @Column(name = "sale_percentage")
    private Integer salePercentage;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Lob
    @Column(name = "product_image")
    private byte[] productImage;

    @Column(name = "product_image_content_type")
    private String productImageContentType;

    @ManyToOne
    private Shopkeeper shopkeeper;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSaleName() {
        return saleName;
    }

    public Offer saleName(String saleName) {
        this.saleName = saleName;
        return this;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getDescription() {
        return description;
    }

    public Offer description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public Offer location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRealPrice() {
        return realPrice;
    }

    public Offer realPrice(Integer realPrice) {
        this.realPrice = realPrice;
        return this;
    }

    public void setRealPrice(Integer realPrice) {
        this.realPrice = realPrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public Offer salePrice(Integer salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getSalePercentage() {
        return salePercentage;
    }

    public Offer salePercentage(Integer salePercentage) {
        this.salePercentage = salePercentage;
        return this;
    }

    public void setSalePercentage(Integer salePercentage) {
        this.salePercentage = salePercentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Offer startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Offer endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public Offer productImage(byte[] productImage) {
        this.productImage = productImage;
        return this;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public String getProductImageContentType() {
        return productImageContentType;
    }

    public Offer productImageContentType(String productImageContentType) {
        this.productImageContentType = productImageContentType;
        return this;
    }

    public void setProductImageContentType(String productImageContentType) {
        this.productImageContentType = productImageContentType;
    }

    public Shopkeeper getShopkeeper() {
        return shopkeeper;
    }

    public Offer shopkeeper(Shopkeeper shopkeeper) {
        this.shopkeeper = shopkeeper;
        return this;
    }

    public void setShopkeeper(Shopkeeper shopkeeper) {
        this.shopkeeper = shopkeeper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Offer offer = (Offer) o;
        if(offer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, offer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Offer{" +
            "id=" + id +
            ", saleName='" + saleName + "'" +
            ", description='" + description + "'" +
            ", location='" + location + "'" +
            ", realPrice='" + realPrice + "'" +
            ", salePrice='" + salePrice + "'" +
            ", salePercentage='" + salePercentage + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", productImage='" + productImage + "'" +
            ", productImageContentType='" + productImageContentType + "'" +
            '}';
    }
}
