package com.alhas.airbnb.listing.domain;

import com.alhas.airbnb.sharedkernel.domain.AbstractAuditingEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "listing")
public class Listing extends AbstractAuditingEntity<Long> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "listingSequenceGenerator")
    @SequenceGenerator(name = "listingSequenceGenerator",
            sequenceName = "listing_generator",
            allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @UuidGenerator
    @Column(name = "public_id", nullable = false)
    private UUID publicId;

    private  String title;

    @Column(name = "description")
    private  String description;

    @Column(name = "guests")
    private int guests;


    @Column(name = "bedrooms")
    private int bedrooms;

    @Column(name = "beds")
    private int beds;

    @Column(name = "bathrooms")
    private int bathrooms;

    @Column(name = "price")
    private int price   ;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private BookingCategory bookingCategory;

    @Column(name = "location")
    private String location;

    @Column(name = "landlord_public_id")
    private UUID landlordPublicId;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.REMOVE)
    private Set<ListingPicture> pictures=new HashSet<>();


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public UUID getLandlordPublicId() {
        return landlordPublicId;
    }

    public void setLandlordPublicId(UUID landlordPublicId) {
        this.landlordPublicId = landlordPublicId;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public BookingCategory getBookingCategory() {
        return bookingCategory;
    }

    public void setBookingCategory(BookingCategory bookingCategory) {
        this.bookingCategory = bookingCategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<ListingPicture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<ListingPicture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Listing listing = (Listing) o;
        return beds == listing.beds && price == listing.price && Objects.equals(title, listing.title) && Objects.equals(description, listing.description) && Objects.equals(guests, listing.guests) && Objects.equals(bathrooms, listing.bathrooms) && bookingCategory == listing.bookingCategory && Objects.equals(location, listing.location) && Objects.equals(landlordPublicId, listing.landlordPublicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, guests, beds, bathrooms, price, bookingCategory, location, landlordPublicId);
    }

    @Override
    public String toString() {
        return "Listing{" +
                "landlordPublicId=" + landlordPublicId +
                ", location='" + location + '\'' +
                ", bookingCategory=" + bookingCategory +
                ", price=" + price +
                ", bathrooms='" + bathrooms + '\'' +
                ", beds=" + beds +
                ", guests='" + guests + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", publicId=" + publicId +
                '}';
    }
}
