package com.iishanto.jobhunterbackend.infrastructure.database;

import jakarta.persistence.*;

@Entity
public class IndexingStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

}
