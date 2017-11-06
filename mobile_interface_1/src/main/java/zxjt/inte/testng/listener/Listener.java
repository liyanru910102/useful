package zxjt.inte.testng.listener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import org.testng.internal.IResultListener2;

import zxjt.inte.report.DBConnection;
import zxjt.inte.report.StepBean;
import zxjt.inte.report.StepDao;
import zxjt.inte.report.TestBean;
import zxjt.inte.report.TestDao;
import zxjt.inte.util.JsonAssertUtil;
import zxjt.inte.util.StringUtil;

public class Listener implements IResultListener2 {

	private String namePath = "name.json";
	Map<String, String> nameMap = new HashMap<>();
	// ----------------------------------------------------
	private TestDao mTestDao;
	private StepDao mStepDao;
	private TestBean mTest;
	private StepBean mStep;
	// ----------------------------------------------------
	private String currentName;

	@Override
	public void onStart(ITestContext context) {
		nameMap = JsonAssertUtil.parseJson2(namePath);
		initDao();

	}

	@Override
	public void onFinish(ITestContext context) {

	}

	@Override
	public void onTestStart(ITestResult result) {
		createOrUseExistTest(result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String paramStr, resultStr = null;
		paramStr = getParamsDescription(result);

		// 写入数据库
		addStepAndUpdateTest(result, paramStr, resultStr, null, null);

	}

	@Override
	public void onTestFailure(ITestResult result) {
		String paramStr, resultStr;

		paramStr = getParamsDescription(result);

		// 生成错误信息
		Throwable th = result.getThrowable();
		resultStr = getThrowableDescription(th);
		String stackTrace = getStackTrace(th);

		// 写入数据库
		addStepAndUpdateTest(result, paramStr, resultStr, stackTrace, null);

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		Throwable th = result.getThrowable();
		String resultStr = null, stackTrace = null;
		if (th != null) {
			// 因DataProvider异常而被跳过时不执行onTestStart
			// 所以需要在此调用createOrUseExistTest方法
			createOrUseExistTest(result);

			// 生成错误信息
			resultStr = getThrowableDescription(th);
			stackTrace = getStackTrace(th);
		}
		String paramStr = getParamsDescription(result);
		addStepAndUpdateTest(result, paramStr, resultStr, stackTrace, null);
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// not used
	}

	@Override
	public void beforeConfiguration(ITestResult result) {
		createOrUseExistTest(result);
	}

	@Override
	public void onConfigurationSuccess(ITestResult result) {
		addStepAndUpdateTest(result, currentName, null, null, null);
		// 控制台打印结果
		// printResult(result);
	}

	@Override
	public void onConfigurationFailure(ITestResult result) {

	}

	@Override
	public void onConfigurationSkip(ITestResult result) {
		addStepAndUpdateTest(result, currentName, null, null, null);
		// 控制台打印结果
		// printResult(result);
	}

	/*
	 * 初始化DAO
	 */
	private void initDao() {
		try {
			Connection vConn = DBConnection.getConnection();
			mTestDao = new TestDao(vConn);
			mStepDao = new StepDao(vConn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void createOrUseExistTest(ITestResult result) {
		Method method = result.getMethod().getConstructorOrMethod().getMethod();
		String vClassName = method.getDeclaringClass().getSimpleName();
		String vMethodName = method.getName();
		String name = vClassName + "_" + vMethodName;
		if (Test.class==getMethodType(method)){
			if (!name.equals(currentName)) {
				// 创建新的Test

				mTest = new TestBean();
				mTest.setName(nameMap.get(name));
				mTest.setStartTime(new Date());
				mTestDao.add(mTest);
				// System.out.println("currentName="+currentName);
				currentName = name;
				// picNum = 0;
				// System.out.println("currentName="+currentName);
			}
			createStepInstance();
		}
	}

	private Class<? extends Annotation> getMethodType(Method method){
			 Class<? extends Annotation> methodType=null;
			//Method method = result.getMethod().getConstructorOrMethod().getMethod();
			Annotation[] annotations = method.getAnnotations();  
	        for (Annotation annotation : annotations) {  
	            // 获取注解的具体类型  
	            Class<? extends Annotation> annotationType = annotation.annotationType();
	            
	          if(  Test.class==annotationType){
	        	 // System.out.println("annotationType.getName()"+annotationType.getName());
	        	  return annotationType;
	          }
	        }
			return methodType;
			
		}



	/*
	 * 创建Step实例
	 */
	private void createStepInstance() {
		mStep = new StepBean();
		mStep.setTestId(mTest.getId());
		mStep.setTimeStamp(new Date());
		// System.out.println("mTest.getId()="+mTest.getId());
	}

	/*
	 * 将Step写入数据库并更新Test
	 */
	private void addStepAndUpdateTest(ITestResult result, String paramStr, String resultStr, String stack,
			String screen) {
		Method method = result.getMethod().getConstructorOrMethod().getMethod();

		if (Test.class==getMethodType(method)){
			mTest.setEndTime(new Date());
			mTestDao.update(mTest);
			mStep.setStatus(result.getStatus());
			mStep.setParam(paramStr);
			mStep.setResult(resultStr);
			mStep.setStackTrace(stack);
			mStep.setScreenshot(screen);
			mStepDao.add(mStep);
			// System.out.println(mStep);
			printResult(result);
		}

	}

	/*
	 * 控制台打印结果
	 */
	private void printResult(ITestResult result) {
		String resultStr;
		switch (result.getStatus()) {
		case ITestResult.SUCCESS:
			resultStr = "PASS";
			break;
		case ITestResult.FAILURE:
			//System.err.println(currentName + ", FAIL\n" + getThrowableDescription(result.getThrowable()));
			System.err.println(nameMap.get(currentName) + ", FAIL\n");
			return;
		case ITestResult.SKIP:
			resultStr = "SKIP";
			break;
		default:
			resultStr = "unknown status: " + result.getStatus();
			break;
		}
		// System.out.println(currentName + ", " + resultStr);
		System.out.println(nameMap.get(currentName) + ", " + resultStr);
	}

	/*
	 * 生成异常简要描述信息
	 */
	private String getThrowableDescription(Throwable tr) {
		String msg = tr.getMessage();
		if (StringUtil.hasText(msg)) {
			return StringEscapeUtils.escapeHtml4(msg);
		}
		return tr.getClass().getName();
	}

	/*
	 * 生成异常堆栈信息
	 */
	private String getStackTrace(Throwable tr) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		pw.close();
		String ex = sw.toString();
		return StringEscapeUtils.escapeHtml4(ex);
	}

	/*
	 * 根据参数生成参数信息
	 */
	private String getParamsDescription(ITestResult result) {
		StringBuilder ret = new StringBuilder();
		Method method = result.getMethod().getConstructorOrMethod().getMethod();
		Parameter[] params = method.getParameters();
		Object[] values = result.getParameters();

		if (values.length == 0)
			return method.toString();

		for (int i = 0; i < params.length; ++i) {

			ret.append(params[i]).append(' ');

			ret.append(values[i]).append('\n');
		}
		if (ret.length() > 0) {
			// 删除最后一个换行
			ret.deleteCharAt(ret.length() - 1);
		}
		return ret.toString();
	}

}
