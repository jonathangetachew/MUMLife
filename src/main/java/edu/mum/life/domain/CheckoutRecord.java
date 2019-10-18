package edu.mum.life.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A CheckoutRecord.
 */
@Entity
@Table(name = "checkout_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CheckoutRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private ZonedDateTime dueDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @ManyToOne
    @JsonIgnoreProperties("checkoutRecords")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("checkoutRecords")
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDueDate() {
        return dueDate;
    }

    public CheckoutRecord dueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public CheckoutRecord createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public CheckoutRecord user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public CheckoutRecord item(Item item) {
        this.item = item;
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CheckoutRecord)) {
            return false;
        }
        return id != null && id.equals(((CheckoutRecord) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CheckoutRecord{" +
            "id=" + getId() +
            ", dueDate='" + getDueDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
