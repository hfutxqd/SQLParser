package cf.imxqd.sql;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Parser {
	ArrayList<String> selectList = null;
	String table = null;
	String whereCase = null;
	Database database;
	String SQL;
	public Parser(Database database)
	{
		this.database = database;
	}
	
	private void ParserSetect() {
		Pattern pattern = Pattern.compile("select *\\(.*?\\)",Pattern.CASE_INSENSITIVE);//ƥ��select���
		Matcher matcher = pattern.matcher(SQL);
		matcher.find();
		try{
			String select = matcher.group();
			select = select.replace("select", "");
			select = select.replace("(", "");
			select = select.replace(")", "");
			select = select.replace(" ", "");
			if(!select.equals("*"))
			{
				String[] fields = select.split(",");
				for(String s:fields)//��ѡ���ֶμ���selectList
				{
					selectList.add(s);
				}
			}
		}catch(Exception e)
		{
//			System.out.println("select ������");
		}
	}
	
	private void ParserFrom() {
		Pattern pattern = Pattern.compile("from *\\(.*?\\)",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(SQL);
		matcher.find();
		try{
			String from = matcher.group();
			from = from.replace("from", "");
			from = from.replace("(", "");
			from = from.replace(")", "");
			from = from.trim();
			if(!from.equals(""))
			{
				table = from;//��
			}
		}catch(Exception e)
		{
//			System.out.println("from ������");
		}
	}
	
	String replaceOp(String where, Pattern pattern, Matcher matcher)
	{
		//����Ѷ���ַ��������תΪ���ַ��ģ���������
		pattern = Pattern.compile("not",Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(where);
		where = matcher.replaceAll("^");
		pattern = Pattern.compile("and",Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(where);
		where = matcher.replaceAll("&");
		pattern = Pattern.compile("or",Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(where);
		where = matcher.replaceAll("|");
		pattern = Pattern.compile(">=",Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(where);
		where = matcher.replaceAll("}");
		pattern = Pattern.compile("<=",Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(where);
		where = matcher.replaceAll("{");
		pattern = Pattern.compile("<>",Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(where);
		where = matcher.replaceAll("!");
		return where;
	}
	
	private void ParserWhere() {
		Pattern pattern = Pattern.compile("where *\\(.*\\)",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(SQL);
		matcher.find();
		try{
			String where = matcher.group();
			pattern = Pattern.compile("where",Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(where);
			where = matcher.replaceAll("");
			where = where.replace(" ", "");
			whereCase = replaceOp(where, pattern, matcher);
		}catch(Exception e)
		{
			whereCase = "T";
//			System.out.println("where ������");
		}
	}
	
	
	public void query(String SQL)
	{
		selectList = new ArrayList<>();
		SQL = toLowerSQL(SQL);//��������Ĺؼ���תΪСд
		this.SQL = SQL;
		ParserSetect();
		ParserFrom();
		ParserWhere();
		screen();
	}
	
	void printTitle(Table tab)//��ӡ��ͷ
	{
		for(String title:selectList)
		{
			System.out.print(title+"\t");
		}
		if(selectList.size() == 0)
		{
			for(String title:tab.fields)
			{
				System.out.print(title+"\t");
			}
		}
		System.out.println();
		System.out.print("--------------------------------------");
		System.out.println();
	}
	
	void printResult()
	{
		Table tab = (Table)database.get(table);
		ArrayList<String> tabFields = tab.getFields();
		if(tabFields.containsAll(selectList))
		{
			ArrayList<Row> rows =  tab.getRows();//��ȡ���м�¼
			printTitle(tab);
			for(Row row:rows)//������¼
			{
				if(computeWhereCase(row))
				{
					for(String str:selectList)
					{
						System.out.print(row.get(str)+"\t");
					}
					if(selectList.size() == 0)
					{
						for(String str:tab.fields)
						{
							System.out.print(row.get(str)+"\t");
						}
					}
					System.out.println();
				}
			}
		}else{
			System.out.println("some fields not exist.");
		}
	}
	
	void screen()
	{
		Table tab = (Table)database.get(table);
		if(tab == null)
		{
			System.out.println("Table:"+ table + " not exist.");
		}else{
			printResult();
		}
	}
	
	public int priorityLevel(char op)//�����������ȼ�
	{
		switch (op) {
		case ')':
			return 1;
		case '|':
			return 2;
		case '&':
			return 3;
		case '^':	
			return 4;
		case '(':	
			return 5;
		default:
			break;
		}
		return op;
	}
	
	
	public boolean comparePriority(char stack, char input)
	{
		if(stack == '(')
		{
			return false;
		}else if(stack == ')')
		{
			return true;
		}else
		{
			return priorityLevel(stack) > priorityLevel(input);
		}
	}
	
	public char computeLogic(char a, char b, char op)//����T��F���߼�����
	{
//		System.out.println("op---------->"+op);
		if(op == '&')
		{
			if(a == 'F' || b == 'F')
				return 'F';
		}else if(op == '|'){
			if(a == 'T' || b == 'T')
				return 'T';
		}
		return b;
	}
	
	private int operatorCompute(int i,char[] where) {
		char c = where[i];
//		System.out.println("The current char is "+c);
		if(c == 'T' || c == 'F')//�ǲ��������߼�������ջ
		{
			values.push(c);
//			System.out.println("push to value: "+ c);
		}else{
			if(operator.size() == 0)//��ջ��û�в����������������ջ
			{
				operator.push(c);
//				System.out.println("push to operator: "+ c);
			}else{
				char stack = operator.peek();
				if(stack == '(' && c == ')')
				{
					operator.pop();
//					System.out.println("pop from operator: "+ stack);
					return i;
				}
				if(comparePriority(stack, c))//ջ�����������ȼ����ڵ�ǰ������
				{
					operator.pop();
//					System.out.println("pop from values: "+ stack);
					if(stack == '^')
					{
						char v = values.pop();
//						System.out.println("pop from operator: "+ v);
						v = (v == 'T'?'F':'T');
						values.push(v);
						i--;
//						System.out.println("push to values: "+ v);
					}else{
						char a = values.pop();
//						System.out.println("pop from values: "+ a);
						char b = values.pop();
//						System.out.println("pop from values: "+ b);
						char v = computeLogic(a,b,stack);
//						System.out.println("push to values: "+ v);
						values.push(v);
						i--;
					}
				}else{
					operator.push(c);
//					System.out.println("push to operator: "+ c);
				}
			}
		}
		return i;
	}
	Stack<Character> operator = new Stack<>();
	Stack<Character> values = new Stack<>();
	private boolean TFComputer(String wheretmp) {
		operator.clear();
		values.clear();
		char[] where = wheretmp.toCharArray();
		for(int i = 0; i < where.length; i++)
		{
			i = operatorCompute(i, where);
		}
//		System.out.println(values.peek());
		return values.peek() == 'T';
	}
	
	public boolean computeWhereCase(Row row)//����where�������ض��ļ�¼����ȷ��
	{
//		System.out.println(whereCase);
		String wheretmp = new String(whereCase);
		Pattern pattern = Pattern.compile("\\b\\w+\\b[}{<>=!]");//��where����е��ֶ���ѡ��
		Matcher matcher = pattern.matcher(wheretmp);
		while(matcher.find())
		{
			String tmp = matcher.group();
			String key = tmp.substring(0, tmp.length()-1);//�ֶ���
			char op = tmp.charAt(tmp.length()- 1);//�����
			int num = wheretmp.indexOf(tmp);
			String behindstr = wheretmp.substring(num + tmp.length());
//			System.out.println(behindstr);
			String value = behindstr.substring(0, behindstr.indexOf(")"));//where����и�����ֵ
			String all = "("+key+ op + value + ")";
//			System.out.println(all);
			if(row.compare(key, value, op))
			{
//				System.out.println(true);
				wheretmp = wheretmp.replace(all, "T");
			}else{
//				System.out.println(false);
				wheretmp = wheretmp.replace(all, "F");
			}
		}
//		System.out.println(whereCase);
		return TFComputer(wheretmp);
	}
	
	//��SQL���������Ĺؼ���תΪСд
	public String toLowerSQL(String SQL)
	{
		int bracket = 0;
		char[] SQLArr = SQL.toCharArray();
		for(int i = 0; i < SQLArr.length; i++)
		{
			if(SQLArr[i] == '(')
			{
				bracket++;
			}else if(SQLArr[i] == ')')
			{
				bracket--;
			}
			if(bracket == 0)
			{
				SQLArr[i] = Character.toLowerCase(SQLArr[i]);
			}
		}
		return String.copyValueOf(SQLArr);
	}
}
