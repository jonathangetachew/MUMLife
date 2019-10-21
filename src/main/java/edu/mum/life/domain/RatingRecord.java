package edu.mum.life.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A RatingRecord.
 */
@Entity
@Table(name = "rating_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RatingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate_value")
    private Integer rateValue;

    @Lob
    @Column(name = "comment")
    private byte[] comment;

    @Column(name = "comment_content_type")
    private String commentContentType;

    @ManyToOne
    @JsonIgnoreProperties("ratingRecords")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("ratingRecords")
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRateValue() {
        return rateValue;
    }

    public RatingRecord rateValue(Integer rateValue) {
        this.rateValue = rateValue;
        return this;
    }

    public void setRateValue(Integer rateValue) {
        this.rateValue = rateValue;
    }

    public byte[] getComment() {
        return comment;
    }

    public RatingRecord comment(byte[] comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(byte[] comment) {
        this.comment = comment;
    }

    public String getCommentContentType() {
        return commentContentType;
    }

    public RatingRecord commentContentType(String commentContentType) {
        this.commentContentType = commentContentType;
        return this;
    }

    public void setCommentContentType(String commentContentType) {
        this.commentContentType = commentContentType;
    }

    public User getUser() {
        return user;
    }

    public RatingRecord user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public RatingRecord item(Item item) {
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
        if (!(o instanceof RatingRecord)) {
            return false;
        }
        return id != null && id.equals(((RatingRecord) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RatingRecord{" +
            "id=" + getId() +
            ", rateValue=" + getRateValue() +
            ", comment='" + getComment() + "'" +
            ", commentContentType='" + getCommentContentType() + "'" +
            "}";
    }
}
