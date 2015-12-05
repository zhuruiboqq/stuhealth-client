/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.core;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author House
 */
@MappedSuperclass

public class CoreEntity implements Serializable{
    @Id
    @Column(name="id", nullable=false)
    private String         id; 
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
