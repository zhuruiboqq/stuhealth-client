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
@Table(name="ServerSetting")
public class ServerSetting  extends CoreEntity {
    @Column(name="ServerIP")    private String serverIp;
    @Column(name="ServerPort")  private String serverPort;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
    
}
