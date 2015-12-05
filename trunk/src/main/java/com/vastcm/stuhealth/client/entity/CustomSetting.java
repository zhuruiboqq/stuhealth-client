/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity;

import com.vastcm.stuhealth.client.entity.core.CoreEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author house
 */
@Entity
@Table(name="CustomSetting")
public class CustomSetting extends CoreEntity {
    @Column(name="lookAndFeelName")         private String lookAndFeelName;
    @Column(name="lookAndFeelClassName")    private String lookAndFeelClassName;

    public String getLookAndFeelName() {
        return lookAndFeelName;
    }

    public void setLookAndFeelName(String lookAndFeelName) {
        this.lookAndFeelName = lookAndFeelName;
    }

    public String getLookAndFeelClassName() {
        return lookAndFeelClassName;
    }

    public void setLookAndFeelClassName(String lookAndFeelClassName) {
        this.lookAndFeelClassName = lookAndFeelClassName;
    }

}
