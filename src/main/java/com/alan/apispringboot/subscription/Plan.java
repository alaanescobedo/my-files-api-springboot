package com.alan.apispringboot.subscription;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_name", nullable = false, unique = true)
    private PlanEnum planName;

    @Column(name = "limit_cloud_storage", nullable = false)
    private Integer limitCloudStorage;

    @Column(name = "limit_cloud_monthly_uploads", nullable = false)
    private Integer limitCloudMonthlyUploads;

}
