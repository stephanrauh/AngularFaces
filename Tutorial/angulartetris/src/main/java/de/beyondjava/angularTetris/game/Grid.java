package de.beyondjava.angularTetris.game;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class Grid {
	public List<Row> rows;

	public List<Row> getRows() {
		return rows;
	}

	public Grid() {
		init();
	}

	public void init() {
		rows = new ArrayList<Row>();
		for (int y = 0; y < 20; y++) {
			rows.add(new Row(y));
		}
	}

}
