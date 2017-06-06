package ncd.gui.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class GroupButton<T> extends Menu {

	private final ToggleGroup group = new ToggleGroup();

	public GroupButton(String menuName, T[] objects) {
		super(menuName);
		this.initialise(new ArrayList<T>(Arrays.asList(objects)));
	}

	public GroupButton(String menuName, List<T> objects) {
		super(menuName);
		this.initialise(objects);
	}

	private void initialise(List<T> objects) {
		for (T o : objects) {
			RadioMenuItem menuItem = new RadioMenuItem(o.toString());
			menuItem.setUserData(o);
			menuItem.setToggleGroup(group);
			menuItem.addEventHandler(ActionEvent.ACTION, event -> menuItemChanged(event));
			this.getItems().add(menuItem);
		}
		((RadioMenuItem) this.getItems().get(0)).setSelected(true);
	}

	private void menuItemChanged(ActionEvent event) {
		this.fire();
	}

	public RadioMenuItem getSelectedRadioMenuItem() {
		RadioMenuItem radioMenuItem = (RadioMenuItem) this.group.getSelectedToggle();
		return radioMenuItem;
	}

	public void setSelectedIndex(int index) {
		int nItem = this.getItems().size();
		if (index > nItem) {
			index = nItem - 1;
		}
		if (index < 0) {
			index = 0;
		}
		((RadioMenuItem) this.getItems().get(index)).setSelected(true);
	}

	public void setSelected(T object) {
		ObservableList<MenuItem> menuItems = this.getItems();
		for (MenuItem m : menuItems) {
			RadioMenuItem radioMenuItem = (RadioMenuItem) m;
			if (radioMenuItem.getText().equalsIgnoreCase(object.toString())) {
				radioMenuItem.setSelected(true);
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> getObjects() {
		List<T> objects = new ArrayList<T>();
		ObservableList<MenuItem> menuItems = this.getItems();
		for (MenuItem m : menuItems) {
			RadioMenuItem radioMenuItem = (RadioMenuItem) m;
			objects.add((T) radioMenuItem.getUserData());
		}
		return objects;
	}

}
