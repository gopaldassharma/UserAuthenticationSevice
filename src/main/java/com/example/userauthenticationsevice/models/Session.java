package com.example.userauthenticationsevice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Session extends BaseModel{
    private String token;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private State sessionState; //default datatype of enum in db is tinyInt
}
