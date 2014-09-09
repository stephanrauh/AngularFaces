package de.beyondjava.angularTetris.game;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class Row {
	public List<Cell> cells;

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public Row(int y) {
		init(y);
	}

	public void init(int y) {
		cells = new ArrayList<Cell>();
		for (int x = 0; x < 10; x++)
				cells.add(new Cell(0));
	}

}
