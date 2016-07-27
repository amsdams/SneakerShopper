package crawel.console;

import java.util.ArrayList;
import java.util.List;

public class ActionList {
	private List<Action> Actions = new ArrayList<Action>();

	public void addAction(Action Action) {
		Actions.add(Action);
	}

	public List<Action> getActions() {
		return Actions;
	}

	public void setActions(List<Action> Actions) {
		this.Actions = Actions;
	}
}
