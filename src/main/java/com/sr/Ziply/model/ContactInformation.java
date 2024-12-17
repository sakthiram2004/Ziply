package com.sr.Ziply.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ContactInformation {
    private String email;
    private String phone;
    private String twitter;
    private String instagram;
}
