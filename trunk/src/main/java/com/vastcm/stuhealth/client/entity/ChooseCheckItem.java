/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity;

import com.vastcm.stuhealth.client.entity.core.CoreEntity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author house
 */
@Entity
@Table(name="ChooseItem")
public class ChooseCheckItem extends CoreEntity{
    @Column(name="ItemId")  private int itemId;
    @Column(name="BanBen")  private int banBen;
    @Column(name="Sort")    private int sort;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getBanBen() {
        return banBen;
    }

    public void setBanBen(int banBen) {
        this.banBen = banBen;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
    
    
}
