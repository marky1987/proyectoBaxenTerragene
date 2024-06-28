package com.terragene.baxen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "domain_dist")
    private String domainDist;

    @Column(name = "role_id")
    private String role;

    @Column(name = "dias_filtro")
    private String diasFiltro;

}
