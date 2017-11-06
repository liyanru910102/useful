package zxjt.inte.http;

import java.util.Arrays;
import java.util.List;

import zxjt.inte.report.DBConnection;
import zxjt.inte.testng.listener.HTMLReporter;
import zxjt.inte.util.TestNg;
//1102什么都好了，接下来安装jenkins集成jar包吧，jar包在d盘已经生成跑过了，不过报告还没有看，不过应该也无所谓，集成成功了最后再看一眼
public class httpMain {

	public static void main(String[] args) throws Exception {
		if (args.length <= 0) {
			throw new RuntimeException("请输入需要执行的模块");
		}

		String testCase = String.valueOf(args[0]);
		String[] strTst = testCase.split(",");
		List<String> list = Arrays.asList(strTst);

		TestNg testNew = new TestNg(false);
		testNew.setParam(list);
		testNew.run();
		HTMLReporter.generate();
		DBConnection.close();
	}
}