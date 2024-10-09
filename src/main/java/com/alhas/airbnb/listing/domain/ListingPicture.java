package com.alhas.airbnb.listing.domain;

import com.alhas.airbnb.sharedkernel.domain.AbstractAuditingEntity;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "listing_picture")
public class ListingPicture extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "listingPictureSequenceGenerator")
    @SequenceGenerator(name = "listingPictureSequenceGenerator",sequenceName = "listing_picture_generator", allocationSize = 1)
    @Column(name = "id")
    private  Long id;

    @ManyToOne()
    @JoinColumn(name = "listing_fk" ,  referencedColumnName = "id")
    private Listing listing;

    @Lob
    @Column(name ="file" ,nullable = false)
    private byte[] picture;

    @Column(name = "file_content_type")
    private String fileContType;

    @Column(name = "is_cover")
    private boolean isCover;

    public Long getId() {
        return id;
    }

    public void setLong(Long id) {
        id = id;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getFileContType() {
        return fileContType;
    }

    public void setFileContType(String fileContType) {
        this.fileContType = fileContType;
    }

    public boolean isCover() {
        return isCover;
    }

    public void setCover(boolean cover) {
        isCover = cover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListingPicture that = (ListingPicture) o;
        return isCover == that.isCover && Objects.deepEquals(picture, that.picture) && Objects.equals(fileContType, that.fileContType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(picture), fileContType, isCover);
    }
}
