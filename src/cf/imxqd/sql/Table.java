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
	
	public static class Row {
		HashMap<String,Object> items;
		
		public Row()
		{
			items = new HashMap<String,Object>();
		}
		
		public void set(String key, Object value)
		{
			items.put(key, value);
		}
		
		public Object get(String key)
		{
			return items.get(key);
		}
		
		//打印该条记录
		public void print()
		{
			Iterator iter = items.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object val = entry.getValue();
				System.out.print(val + "\t");
			}
		}
		//比较某一记录的key字段的值与value的值，比较方式为op:整型比较
		public boolean IntCompare(Object mValue, String value,char op)
		{
			Object wValue = null;
			if(isInteger(value))
			{
				wValue = Integer.valueOf(value);
				switch (op) {
				case '{':
					return (Integer)mValue <= (Integer)wValue;
				case '}':
					return (Integer)mValue >= (Integer)wValue;
				case '=':
					return (Integer)mValue == (Integer)wValue;
				case '!':
					return (Integer)mValue != (Integer)wValue;
				case '<':
					return (Integer)mValue < (Integer)wValue;
				case '>':
					return (Integer)mValue > (Integer)wValue;
				default:
					return false;
				}
			}else{
				return false;
			}
		}
		
		//比较某一记录的key字段的值与value的值，比较方式为op:浮点数比较
		public boolean DoubleCompare(Object mValue, String value,char op)
		{
			Object wValue = null;
//			System.out.println("compare:Double");
			if(isDouble(value))
			{
				wValue = Double.valueOf(value);

				switch (op) {
				case '{':
					return (Double)mValue <= (Double)wValue;
				case '}':
					return (Double)mValue >= (Double)wValue;
				case '=':
					return (Double)mValue == (Double)wValue;
				case '!':
					return (Double)mValue != (Double)wValue;
				case '<':
					return (Double)mValue < (Double)wValue;
				case '>':
					return (Double)mValue > (Double)wValue;
				default:
					return false;
				}
			}else{
				return false;
			}
		}
		
		//比较某一记录的key字段的值与value的值，比较方式为op：字符串比较
		public boolean StringCompare(Object mValue, String value,char op)
		{
//			System.out.println("compare:String");
			value = value.replace("\"", "");
			if(value.compareTo((String)mValue) > 0)
			{
				if(op == '>')
				{
					return true;
				}else{
					return false;
				}
				
			}else if(value.compareTo((String)mValue) < 0)
			{
				if(op == '<')
				{
					return true;
				}else{
					return false;
				}
				
			}else{
				if(op == '{' || op == '}' || op == '=')
				{
					return true;
				}else{
					return false;
				}
			}
		}
		
		//比较某一记录的key字段的值与value的值，比较方式为op
		public boolean compare(String key, String value, char op)//用于比较where语句的单个条件的正确性
		{
			Object mValue = items.get(key);
//			System.out.println("compare:"+ mValue + op + value);
			if (mValue instanceof Integer) {
//				System.out.println("compare:Integer");
				return IntCompare(mValue, value, op);
				
			}else if(mValue instanceof Double)
			{
//				System.out.println("compare:Double");
				return DoubleCompare(mValue, value, op);
			}
			else{
				return StringCompare(mValue, value, op);
			}
		}
		
		//判断字符串是不是整型
		public boolean isInteger(String str) {  
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
			return pattern.matcher(str).matches();
		}
		//判断字符串是不是浮点型
		public boolean isDouble(String str) {  
			Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");  
		    return pattern.matcher(str).matches();  
		}
	}

}
