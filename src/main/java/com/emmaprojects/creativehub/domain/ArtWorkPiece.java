package com.emmaprojects.creativehub.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.carrotsearch.hppc.HashOrderMixing;
import com.emmaprojects.creativehub.domain.enumeration.CommercialState;

/**
 * A ArtWorkPiece.
 */
@Entity
@Table(name = "art_work_piece")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ArtWorkPiece extends WorkPiece implements Serializable {

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "depth")
    private Integer depth;

    @Enumerated(EnumType.STRING)
    @Column(name = "commercial_state")
    private CommercialState commercialState;

    @Column(name = "price")
    private Double price;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public CommercialState getCommercialState() {
        return commercialState;
    }

    public void setCommercialState(CommercialState commercialState) {
        this.commercialState = commercialState;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtWorkPiece artWorkPiece = (ArtWorkPiece) o;

        if ( ! Objects.equals(id, artWorkPiece.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ArtWorkPiece{" +
            super.toString() +
            ", width='" + width + "'" +
            ", height='" + height + "'" +
            ", depth='" + depth + "'" +
            ", commercialState='" + commercialState + "'" +
            ", price='" + price + "'" +
            '}';
    }
}
