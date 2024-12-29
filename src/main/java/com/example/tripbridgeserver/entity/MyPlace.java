package com.example.tripbridgeserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "my_places")
public class MyPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String place;
    private String address;
    private Long routeOrder;

    @ManyToOne
    @JoinColumn(name = "myroute_id")
    private MyRoute myRoute;
}
