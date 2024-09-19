package com.insurance.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Table(name = "insurance_types")
public class InsuranceType {

    @Id
    private String insuranceTypeId;

    @Column(nullable = false)
    @NotBlank(message = "name cannot be blank")
    private String name;
    
    private boolean active;

    @OneToMany(mappedBy = "insuranceType", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<InsuranceScheme> insuranceSchemes;
}