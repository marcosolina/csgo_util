package com.marco.csgoutil.roundparser.model.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity which represents the Steam Users scores per every game (map played)
 * 
 * @author Marco
 *
 */
@Entity
@Table(name = "USERS_SCORES")
public class EntityUserScore {
	@EmbeddedId
	private EntityUserScorePk id;
	@Column(name = "SCORE")
	private Long score;

	public EntityUserScorePk getId() {
		return id;
	}

	public void setId(EntityUserScorePk id) {
		this.id = id;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

}
