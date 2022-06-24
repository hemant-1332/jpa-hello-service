package com.example.jpa.helloapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "configuration")
@Getter
@Setter
@ToString
public class Configuration {

    @Id
    @SequenceGenerator(name = "configuration_sequence", sequenceName = "configuration_sequence")
    @Column(name = "id")
    private int pk;

    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Column(name = "value")
    private String value;
}
