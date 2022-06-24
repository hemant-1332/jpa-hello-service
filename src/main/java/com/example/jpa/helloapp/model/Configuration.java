package com.example.jpa.helloapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "sn_properties")
@Getter
@Setter
@ToString
public class Configuration {

    @Id
    @SequenceGenerator(name = "sn_properties_sequence", sequenceName = "sn_properties_sequence")
    @Column(name = "pk_property")
    private int pk;

    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Column(name = "value")
    private String value;
}
