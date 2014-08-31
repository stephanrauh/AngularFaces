package de.beyondjava.angularTetris.highscore;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class HighScoreBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nameOfPlayer;
	public String getNameOfPlayer() {
		return nameOfPlayer;
	}
	public void setNameOfPlayer(String nameOfPlayer) {
		this.nameOfPlayer = nameOfPlayer;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	private int score;
	private Date date;
}
