package com.marco.ixigo.demmanager.model.entities;

import java.time.LocalDateTime;

/**
 * Simple Dao used to wrap the LocaDateTime object. The class LocalDateTime does
 * not have a constructor that I could use when performing the JPA query
 * 
 * @author Marco
 *
 */
public class DaoGames {
    private LocalDateTime gameOf;

    public DaoGames(LocalDateTime gameOf) {
        super();
        this.gameOf = gameOf;
    }

    public LocalDateTime getGameOf() {
        return gameOf;
    }

    public void setGameOf(LocalDateTime gameOf) {
        this.gameOf = gameOf;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gameOf == null) ? 0 : gameOf.hashCode());
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
        DaoGames other = (DaoGames) obj;
        if (gameOf == null) {
            if (other.gameOf != null)
                return false;
        } else if (!gameOf.equals(other.gameOf))
            return false;
        return true;
    }

}