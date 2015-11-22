package cf.imxqd.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class Table {
	String name;//表名
	ArrayList<String> fields;//保存所有字段名
	ArrayList<Row> rows;//所有的记录
	public Table(String name, String[] fields)
	{
		this.name = name;
		this.fields = new ArrayList<>();
		for(String str:fields)
		{
			this.fields.add(str);
		}
		rows = new ArrayList<Row>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void add(Row arg)
	{
		rows.add(arg);
	}
	
	public ArrayList<Row> getRows()
	{
		return rows;
	}
	
	public ArrayList<String> getFields()
	{
		return fields;
	}
}
