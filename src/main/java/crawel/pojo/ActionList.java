package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ActionList {
	private List<Action> actions;

	public ActionList() {
		actions = new ArrayList<Action>();
	}

	public void addAction(Action Action) {
		actions.add(Action);
	}

}
