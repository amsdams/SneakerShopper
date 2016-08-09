package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

public class ActionList {
	private List<Action> actions;

	public ActionList() {
		actions = new ArrayList<Action>();
	}

	public void addAction(Action Action) {
		actions.add(Action);
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> Actions) {
		this.actions = Actions;
	}
}
