package de.beyondjava.angularTetris.settings;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SettingsBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int numberOfRows = 25;
	private int numberOfColumns = 10;
	private boolean preview = false;
	private boolean ignoreGravity = true;

	public boolean isIgnoreGravity() {
		return ignoreGravity;
	}

	public void setIgnoreGravity(boolean ignoreGravity) {
		this.ignoreGravity = ignoreGravity;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	public boolean isPreview() {
		return preview;
	}

	public void setPreview(boolean preview) {
		this.preview = preview;
	}
}
