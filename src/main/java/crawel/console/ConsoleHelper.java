package crawel.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.pojo.Action;
import crawel.pojo.ActionList;

public class ConsoleHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleHelper.class);
	BufferedReader br;
	PrintStream ps;

	public ConsoleHelper() {
		br = new BufferedReader(new InputStreamReader(System.in));
		ps = System.out;

	}

	public PrintStream format(String format, Object... objects) {
		return ps.format(format, objects);
	}

	public ActionList getActionList() {
		ActionList actionList = new ActionList();

		Action sortProductsByBrand = new Action();
		sortProductsByBrand.setAction("sbb<ASC|DESC>");
		sortProductsByBrand.setDescription("sort by brand");
		actionList.getActions().add(sortProductsByBrand);

		Action sortProductsByName = new Action();
		sortProductsByName.setAction("sbn<ASD|DESC>");
		sortProductsByName.setDescription("sort by name");
		actionList.getActions().add(sortProductsByName);

		Action sortProductsByShop = new Action();
		sortProductsByShop.setAction("sbs<ASD|DESC>");
		sortProductsByShop.setDescription("sort by shop");
		actionList.getActions().add(sortProductsByShop);

		Action sortProductsByNewPrice = new Action();
		sortProductsByNewPrice.setAction("sbp<ASD|DESC>");
		sortProductsByNewPrice.setDescription("sort by (new) price");
		actionList.getActions().add(sortProductsByNewPrice);

		Action sortProductsByDiscount = new Action();
		sortProductsByDiscount.setAction("sbd<ASD|DESC>");
		sortProductsByDiscount.setDescription("sort by discount");
		actionList.getActions().add(sortProductsByDiscount);

		Action filterByBrand = new Action();
		filterByBrand.setAction("fbb<brand>");
		filterByBrand.setDescription("filter by brand");
		actionList.getActions().add(filterByBrand);

		Action filterByName = new Action();
		filterByName.setAction("fbn<name>");
		filterByName.setDescription("filter by name");
		actionList.getActions().add(filterByName);

		Action exit = new Action();
		exit.setAction("bye");
		exit.setDescription("exits the program");
		actionList.getActions().add(exit);

		Action psl = new Action();
		psl.setAction("psl");
		psl.setDescription("print shop list");
		actionList.getActions().add(psl);

		Action pbl = new Action();
		pbl.setAction("pbl");
		pbl.setDescription("print brand list");
		actionList.getActions().add(pbl);

		Action ppl = new Action();
		ppl.setAction("ppl");
		ppl.setDescription("print product list");
		actionList.getActions().add(ppl);

		Action pcl = new Action();
		pcl.setAction("pcl");
		pcl.setDescription("print currency list");
		actionList.getActions().add(pcl);

		Action pft = new Action();
		pft.setAction("pftl");
		pft.setDescription("print file transfer list");
		actionList.getActions().add(pft);

		Action gp = new Action();
		gp.setAction("gp");
		gp.setDescription("get products");
		actionList.getActions().add(gp);

		Action sp = new Action();
		sp.setAction("sp");
		sp.setDescription("save products");
		actionList.getActions().add(sp);

		Action ftp = new Action();
		ftp.setAction("ftp");
		ftp.setDescription("save products on ftp");
		actionList.getActions().add(ftp);

		return actionList;
	}

	public String printActionList(ActionList actionList) {

		StringBuilder question = new StringBuilder();
		question.append("What do you want to do?\r\n");
		for (Action action : actionList.getActions()) {
			question.append("type " + action.getAction() + " to " + action.getDescription() + "\r\n");
		}

		return question.toString();

	}

	public String readLine(String out) {
		ps.format(out);
		try {
			return br.readLine();
		} catch (IOException e) {
			LOGGER.warn("could not readline {}", e.getMessage(), e);
			return null;
		}
	}

}
