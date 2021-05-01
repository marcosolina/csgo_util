package com.marco.csgoutil.roundparser.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ERROR_MAP_SENT")
public class EntityErrorMapSent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "GAME_DATE")
    private LocalDateTime gameDate;
    @Column(name = "SENT_ON")
    private LocalDateTime sentOn;

    public LocalDateTime getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDateTime gameDate) {
        this.gameDate = gameDate;
    }

    public LocalDateTime getSentOn() {
        return sentOn;
    }

    public void setSentOn(LocalDateTime sentOn) {
        this.sentOn = sentOn;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gameDate == null) ? 0 : gameDate.hashCode());
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
        EntityErrorMapSent other = (EntityErrorMapSent) obj;
        if (gameDate == null) {
            if (other.gameDate != null)
                return false;
        } else if (!gameDate.equals(other.gameDate))
            return false;
        return true;
    }

}
