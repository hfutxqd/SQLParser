package cf.imxqd.test;

import cf.imxqd.sql.Database;
import cf.imxqd.sql.Parser;
import cf.imxqd.sql.Table;
import cf.imxqd.sql.Table.Row;


public class Main {

	public static void main(String[] args) {
		//新建一个数据库
		Database database = new Database();
		// 在数据库插入一张Student表
		Table student = new Table("student", new String[]{"name", "age", "No"});
		Row r1 = new Row();
		r1.set("name", "imxqd");
		r1.set("age", 20);
		r1.set("No", "2013214400");
		//在Student表中插入记录
		student.add(r1);
		Row r2 = new Row();
		r2.set("name", "asdsa");
		r2.set("age", 22);
		r2.set("No", "2013212133");
		student.add(r2);
		Row r3 = new Row();
		r3.set("name", "asdsa");
		r3.set("age", 12);
		r3.set("No", "2013212133");
		student.add(r3);
		database.add(student);
		//新建一个food表
		Table food = new Table("food", new String[]{"name", "price", "No"});
		Row f1 = new Row();
		f1.set("name", "apple");
		f1.set("price", 2.5);
		f1.set("No", "0001");
		food.add(f1);
		Row f2 = new Row();
		f2.set("name", "banna");
		f2.set("price", 3.0);
		f2.set("No", "0002");
		food.add(f2);
		Row f3 = new Row();
		f3.set("name", "orange");
		f3.set("price", 2.8);
		f3.set("No", "0003");
		food.add(f3);
		database.add(food);
		//新建解析器
		Parser p = new Parser(database);
		//查询
		p.query("sEleCt (*) frOm (student)  WHERE ((age<=20)or(name=\"imxqd\"));");
		System.out.println();
		p.query("sEleCt (No,age) frOm (student)  WHERE (not(age<=20));");
		System.out.println();
		p.query("sEleCt (No,age) frOm (student)  WHERE (not(age<>20));");
		System.out.println();
		p.query("sEleCt (*) frOm (student);");
		System.out.println();
		
		p.query("select (*) from (food) where (price > 2.5);");
		System.out.println();
		p.query("select (name, price) from (food);");
	}

}
