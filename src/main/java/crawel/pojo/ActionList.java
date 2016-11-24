package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ActionList {
	private List<Action> actions;

	public ActionList() {
		actions = new ArrayList<>();
	}

	public void addAction(Action action) {
		actions.add(action);
	}

}
