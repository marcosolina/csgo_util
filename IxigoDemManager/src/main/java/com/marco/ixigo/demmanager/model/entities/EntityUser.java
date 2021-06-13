package com.marco.ixigo.demmanager.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity that represents the table which contains the Steam Users definition
 * 
 * @author Marco
 *
 */
@Entity
@Table(name = "USERS")
public class EntityUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "STEAM_ID")
    private String steamId;
    @Column(name = "USER_NAME")
    private String userName;

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((steamId == null) ? 0 : steamId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntityUser other = (EntityUser) obj;
        if (steamId == null) {
            if (other.steamId != null)
                return false;
        } else if (!steamId.equals(other.steamId))
            return false;
        return true;
    }

}
