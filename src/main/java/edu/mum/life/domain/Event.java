package edu.mum.life.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    
    @Lob
    @Column(name = "poster_image", nullable = false)
    private byte[] posterImage;

    @Column(name = "poster_image_content_type", nullable = false)
    private String posterImageContentType;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "start", nullable = false)
    private ZonedDateTime start;

    @Column(name = "end")
    private ZonedDateTime end;

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RSVPRecord> rSVPRecords = new HashSet<>();

    @OneToMany(mappedBy = "types")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EventType> eventTypes = new HashSet<>();

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

    public Event name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Event description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPosterImage() {
        return posterImage;
    }

    public Event posterImage(byte[] posterImage) {
        this.posterImage = posterImage;
        return this;
    }

    public void setPosterImage(byte[] posterImage) {
        this.posterImage = posterImage;
    }

    public String getPosterImageContentType() {
        return posterImageContentType;
    }

    public Event posterImageContentType(String posterImageContentType) {
        this.posterImageContentType = posterImageContentType;
        return this;
    }

    public void setPosterImageContentType(String posterImageContentType) {
        this.posterImageContentType = posterImageContentType;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Event createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public Event start(ZonedDateTime start) {
        this.start = start;
        return this;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public Event end(ZonedDateTime end) {
        this.end = end;
        return this;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public Set<RSVPRecord> getRSVPRecords() {
        return rSVPRecords;
    }

    public Event rSVPRecords(Set<RSVPRecord> rSVPRecords) {
        this.rSVPRecords = rSVPRecords;
        return this;
    }

    public Event addRSVPRecord(RSVPRecord rSVPRecord) {
        this.rSVPRecords.add(rSVPRecord);
        rSVPRecord.setEvent(this);
        return this;
    }

    public Event removeRSVPRecord(RSVPRecord rSVPRecord) {
        this.rSVPRecords.remove(rSVPRecord);
        rSVPRecord.setEvent(null);
        return this;
    }

    public void setRSVPRecords(Set<RSVPRecord> rSVPRecords) {
        this.rSVPRecords = rSVPRecords;
    }

    public Set<EventType> getEventTypes() {
        return eventTypes;
    }

    public Event eventTypes(Set<EventType> eventTypes) {
        this.eventTypes = eventTypes;
        return this;
    }

    public Event addEventType(EventType eventType) {
        this.eventTypes.add(eventType);
        eventType.setTypes(this);
        return this;
    }

    public Event removeEventType(EventType eventType) {
        this.eventTypes.remove(eventType);
        eventType.setTypes(null);
        return this;
    }

    public void setEventTypes(Set<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", posterImage='" + getPosterImage() + "'" +
            ", posterImageContentType='" + getPosterImageContentType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            "}";
    }
}
