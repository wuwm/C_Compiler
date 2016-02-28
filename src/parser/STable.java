package parser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class STable {
	private HashSet<String> table;

	public STable() {
		setTable(new HashSet<String>());
	}

	public void add(String symbol) {
		table.add(symbol);
	}

	public Set<String> getTable() {
		return table;
	}

	public boolean isContained(String var) {
		return table.contains(var);
	}

	public void setTable(HashSet<String> table) {
		this.table = table;
	}

	@Override
	public String toString() {
		Iterator<String> iterator = table.iterator();
		if (!iterator.hasNext())
			return null;
		String str = "int ";
		str += iterator.next();
		while (iterator.hasNext())
			str += ", " + iterator.next();
		return str + ";";
	}
}
