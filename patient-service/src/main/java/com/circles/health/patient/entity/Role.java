package com.circles.health.patient.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
   
   @Id
   @GeneratedValue(strategy = GenerationType.TABLE)
   @Column(name = "role_id")
   private Integer id;
    
   @Column(name = "role_name")
   private String name;
    
   @Column(name = "role_description")
   private String description;

   public Role() {
	super();
   }

   public Role(Integer id, String name, String description) {
	super();
	this.id = id;
	this.name = name;
	this.description = description;
   }

   public Integer getId() {
	return id;
   }

   public void setId(Integer id) {
	this.id = id;
   }

   public String getName() {
	return name;
   }

   public void setName(String name) {
	this.name = name;
   }

   public String getDescription() {
	return description;
   }

   public void setDescription(String description) {
	this.description = description;
   }
   
}
