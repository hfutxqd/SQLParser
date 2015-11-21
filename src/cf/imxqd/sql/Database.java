package cf.imxqd.sql;

import java.util.HashMap;

public class Database {
	HashMap<String,Table> tables;
	
	public Database()
	{
		tables = new HashMap<String,Table>();
	}
	
	public void add(Table table)
	{
		tables.put(table.getName(), table);
	}
	
	public Table get(String name)
	{
		return tables.get(name);
	}
}
