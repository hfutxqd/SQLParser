package cf.imxqd.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class Table {
	String name;//����
	ArrayList<String> fields;//���������ֶ���
	ArrayList<Row> rows;//���еļ�¼
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
