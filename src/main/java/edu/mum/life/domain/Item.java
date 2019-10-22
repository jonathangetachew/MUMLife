package edu.mum.life.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import edu.mum.life.domain.enumeration.ItemStatus;

/**
 * Represents an Item instance in the domain.
 */
@ApiModel(description = "Represents an Item instance in the domain.")
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ItemStatus status;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @OneToOne
    @JoinColumn(unique = true)
    private ItemType type;

    @OneToMany(mappedBy = "item")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReservationRecord> reservationRecords = new HashSet<>();

    @OneToMany(mappedBy = "item")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CheckoutRecord> checkoutRecords = new HashSet<>();

    @OneToMany(mappedBy = "item")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RatingRecord> ratingRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Item imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public Item status(ItemStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Item createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ItemType getType() {
        return type;
    }

    public Item type(ItemType itemType) {
        this.type = itemType;
        return this;
    }

    public void setType(ItemType itemType) {
        this.type = itemType;
    }

    public Set<ReservationRecord> getReservationRecords() {
        return reservationRecords;
    }

    public Item reservationRecords(Set<ReservationRecord> reservationRecords) {
        this.reservationRecords = reservationRecords;
        return this;
    }

    public Item addReservationRecord(ReservationRecord reservationRecord) {
        this.reservationRecords.add(reservationRecord);
        reservationRecord.setItem(this);
        return this;
    }

    public Item removeReservationRecord(ReservationRecord reservationRecord) {
        this.reservationRecords.remove(reservationRecord);
        reservationRecord.setItem(null);
        return this;
    }

    public void setReservationRecords(Set<ReservationRecord> reservationRecords) {
        this.reservationRecords = reservationRecords;
    }

    public Set<CheckoutRecord> getCheckoutRecords() {
        return checkoutRecords;
    }

    public Item checkoutRecords(Set<CheckoutRecord> checkoutRecords) {
        this.checkoutRecords = checkoutRecords;
        return this;
    }

    public Item addCheckoutRecord(CheckoutRecord checkoutRecord) {
        this.checkoutRecords.add(checkoutRecord);
        checkoutRecord.setItem(this);
        return this;
    }

    public Item removeCheckoutRecord(CheckoutRecord checkoutRecord) {
        this.checkoutRecords.remove(checkoutRecord);
        checkoutRecord.setItem(null);
        return this;
    }

    public void setCheckoutRecords(Set<CheckoutRecord> checkoutRecords) {
        this.checkoutRecords = checkoutRecords;
    }

    public Set<RatingRecord> getRatingRecords() {
        return ratingRecords;
    }

    public Item ratingRecords(Set<RatingRecord> ratingRecords) {
        this.ratingRecords = ratingRecords;
        return this;
    }

    public Item addRatingRecord(RatingRecord ratingRecord) {
        this.ratingRecords.add(ratingRecord);
        ratingRecord.setItem(this);
        return this;
    }

    public Item removeRatingRecord(RatingRecord ratingRecord) {
        this.ratingRecords.remove(ratingRecord);
        ratingRecord.setItem(null);
        return this;
    }

    public void setRatingRecords(Set<RatingRecord> ratingRecords) {
        this.ratingRecords = ratingRecords;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
