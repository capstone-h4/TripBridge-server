package com.example.tripbridgeserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scraps", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "place"}))
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "place")
    private String place;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude", precision = 20, scale = 10)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 20, scale = 10)
    private BigDecimal longitude;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Scrap(String place, String address, BigDecimal latitude, BigDecimal longitude, User user) {
        this.place = place;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }
}

