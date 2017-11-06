package zxjt.inte.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.testng.CommandLineArgs;
import org.testng.IAlterSuiteListener;
import org.testng.IAnnotationTransformer;
import org.testng.IClassListener;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInterceptor;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestObjectFactory;
import org.testng.ITestRunnerFactory;
import org.testng.TestNG;
import org.testng.internal.IConfiguration;
import org.testng.internal.OverrideProcessor;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.FailurePolicy;
import org.testng.xml.XmlSuite.ParallelMode;

public class TestNg extends TestNG {
	private List<String> strParam = new ArrayList<>();

	public TestNg(boolean b) {
		super(b);
	}

	public void setParam(List<String> param) {
		this.strParam = param;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return super.getStatus();
	}

	@Override
	public void setOutputDirectory(String outputdir) {
		// TODO Auto-generated method stub
		super.setOutputDirectory(outputdir);
	}

	@Override
	public void setUseDefaultListeners(boolean useDefaultListeners) {
		// TODO Auto-generated method stub
		super.setUseDefaultListeners(useDefaultListeners);
	}

	public void setTestJar(InputStream is) throws Exception {
	}

	@Override
	public void setXmlPathInJar(String xmlPathInJar) {
		// TODO Auto-generated method stub
		super.setXmlPathInJar(xmlPathInJar);
	}

	@Override
	public void initializeSuitesAndJarFile() {
		InputStream isIn = null;
		InputStream ins = null;
		try {
			isIn = this.getClass().getResourceAsStream("/testcase.xml");

			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(isIn);
			Element root = document.getRootElement();
			// 获取根元素中所有元素
			List<Element> stuEleList = root.elements("test");
			// 循环遍历所有元素
			for (Element stuEle : stuEleList) {

				if ("all".equalsIgnoreCase(strParam.get(0))) {
					stuEle.setAttributeValue("enabled", "true");
				} else {
					// 获取元素
					String number = stuEle.attributeValue("name");
					if (strParam.contains(number)) {
						stuEle.setAttributeValue("enabled", "true");
					} else {
						stuEle.setAttributeValue("enabled", "false");

					}
				}
			}
			ins = new ByteArrayInputStream(document.asXML().getBytes("utf-8"));

			Parser parser = getParser(ins);
			Collection<XmlSuite> suites = parser.parse();
			for (XmlSuite suite : suites) {
				// If test names were specified, only run these test names
				m_suites.add(suite);
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				isIn.close();
			} catch (IOException e) {

				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

	}

	private Parser getParser(InputStream is) {
		Parser result = new Parser(is);
		initProcessor(result);
		return result;
	}

	private void initProcessor(Parser result) {
		result.setPostProcessor(new OverrideProcessor(null, null));
	}

	@Override
	public void setThreadCount(int threadCount) {
		// TODO Auto-generated method stub
		super.setThreadCount(threadCount);
	}

	@Override
	public void setParallel(String parallel) {
		// TODO Auto-generated method stub
		super.setParallel(parallel);
	}

	@Override
	public void setParallel(ParallelMode parallel) {
		// TODO Auto-generated method stub
		super.setParallel(parallel);
	}

	@Override
	public void setCommandLineSuite(XmlSuite suite) {
		// TODO Auto-generated method stub
		super.setCommandLineSuite(suite);
	}

	@Override
	public void setTestClasses(Class[] classes) {
		// TODO Auto-generated method stub
		super.setTestClasses(classes);
	}

	@Override
	public void addMethodSelector(String className, int priority) {
		// TODO Auto-generated method stub
		super.addMethodSelector(className, priority);
	}

	@Override
	public void setTestSuites(List<String> suites) {
		// TODO Auto-generated method stub
		super.setTestSuites(suites);
	}

	@Override
	public void setXmlSuites(List<XmlSuite> suites) {
		// TODO Auto-generated method stub
		super.setXmlSuites(suites);
	}

	@Override
	public void setExcludedGroups(String groups) {
		// TODO Auto-generated method stub
		super.setExcludedGroups(groups);
	}

	@Override
	public void setGroups(String groups) {
		// TODO Auto-generated method stub
		super.setGroups(groups);
	}

	@Override
	protected void setTestRunnerFactory(ITestRunnerFactory itrf) {
		// TODO Auto-generated method stub
		super.setTestRunnerFactory(itrf);
	}

	@Override
	public void setObjectFactory(Class c) {
		// TODO Auto-generated method stub
		super.setObjectFactory(c);
	}

	@Override
	public void setObjectFactory(ITestObjectFactory factory) {
		// TODO Auto-generated method stub
		super.setObjectFactory(factory);
	}

	@Override
	public void setListenerClasses(List<Class<? extends ITestNGListener>> classes) {
		// TODO Auto-generated method stub
		super.setListenerClasses(classes);
	}

	@Override
	public void addListener(Object listener) {
		// TODO Auto-generated method stub
		super.addListener(listener);
	}

	@Override
	public void addListener(ITestNGListener listener) {
		// TODO Auto-generated method stub
		super.addListener(listener);
	}

	@Override
	public void addListener(IInvokedMethodListener listener) {
		// TODO Auto-generated method stub
		super.addListener(listener);
	}

	@Override
	public void addListener(ISuiteListener listener) {
		// TODO Auto-generated method stub
		super.addListener(listener);
	}

	@Override
	public void addListener(ITestListener listener) {
		// TODO Auto-generated method stub
		super.addListener(listener);
	}

	@Override
	public void addListener(IClassListener listener) {
		// TODO Auto-generated method stub
		super.addListener(listener);
	}

	@Override
	public void addListener(IReporter listener) {
		// TODO Auto-generated method stub
		super.addListener(listener);
	}

	@Override
	public void addInvokedMethodListener(IInvokedMethodListener listener) {
		// TODO Auto-generated method stub
		super.addInvokedMethodListener(listener);
	}

	@Override
	public Set<IReporter> getReporters() {
		// TODO Auto-generated method stub
		return super.getReporters();
	}

	@Override
	public List<ITestListener> getTestListeners() {
		// TODO Auto-generated method stub
		return super.getTestListeners();
	}

	@Override
	public List<ISuiteListener> getSuiteListeners() {
		// TODO Auto-generated method stub
		return super.getSuiteListeners();
	}

	@Override
	public void setVerbose(int verbose) {
		// TODO Auto-generated method stub
		super.setVerbose(verbose);
	}

	@Override
	public void initializeEverything() {
		// TODO Auto-generated method stub
		super.initializeEverything();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}

	@Override
	protected List<ISuite> runSuites() {
		// TODO Auto-generated method stub
		return super.runSuites();
	}

	@Override
	public void addAlterSuiteListener(IAlterSuiteListener l) {
		// TODO Auto-generated method stub
		super.addAlterSuiteListener(l);
	}

	@Override
	public void addExecutionListener(IExecutionListener l) {
		// TODO Auto-generated method stub
		super.addExecutionListener(l);
	}

	@Override
	public List<ISuite> runSuitesLocally() {
		// TODO Auto-generated method stub
		return super.runSuitesLocally();
	}

	@Override
	protected IConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return super.getConfiguration();
	}

	@Override
	protected void configure(CommandLineArgs cla) {
		// TODO Auto-generated method stub
		super.configure(cla);
	}

	@Override
	public void setSuiteThreadPoolSize(Integer suiteThreadPoolSize) {
		// TODO Auto-generated method stub
		super.setSuiteThreadPoolSize(suiteThreadPoolSize);
	}

	@Override
	public Integer getSuiteThreadPoolSize() {
		// TODO Auto-generated method stub
		return super.getSuiteThreadPoolSize();
	}

	@Override
	public void setRandomizeSuites(boolean randomizeSuites) {
		// TODO Auto-generated method stub
		super.setRandomizeSuites(randomizeSuites);
	}

	@Override
	public void setSourcePath(String path) {
		// TODO Auto-generated method stub
		super.setSourcePath(path);
	}

	@Override
	public void configure(Map cmdLineArgs) {
		// TODO Auto-generated method stub
		super.configure(cmdLineArgs);
	}

	@Override
	public void setTestNames(List<String> testNames) {
		// TODO Auto-generated method stub
		super.setTestNames(testNames);
	}

	@Override
	public void setSkipFailedInvocationCounts(Boolean skip) {
		// TODO Auto-generated method stub
		super.setSkipFailedInvocationCounts(skip);
	}

	@Override
	public void setJUnit(Boolean isJUnit) {
		// TODO Auto-generated method stub
		super.setJUnit(isJUnit);
	}

	@Override
	public void setMixed(Boolean isMixed) {
		// TODO Auto-generated method stub
		super.setMixed(isMixed);
	}

	@Override
	public boolean hasFailure() {
		// TODO Auto-generated method stub
		return super.hasFailure();
	}

	@Override
	public boolean hasFailureWithinSuccessPercentage() {
		// TODO Auto-generated method stub
		return super.hasFailureWithinSuccessPercentage();
	}

	@Override
	public boolean hasSkip() {
		// TODO Auto-generated method stub
		return super.hasSkip();
	}

	@Override
	public String getOutputDirectory() {
		// TODO Auto-generated method stub
		return super.getOutputDirectory();
	}

	@Override
	public IAnnotationTransformer getAnnotationTransformer() {
		// TODO Auto-generated method stub
		return super.getAnnotationTransformer();
	}

	@Override
	public void setAnnotationTransformer(IAnnotationTransformer t) {
		// TODO Auto-generated method stub
		super.setAnnotationTransformer(t);
	}

	@Override
	public String getDefaultSuiteName() {
		// TODO Auto-generated method stub
		return super.getDefaultSuiteName();
	}

	@Override
	public void setDefaultSuiteName(String defaultSuiteName) {
		// TODO Auto-generated method stub
		super.setDefaultSuiteName(defaultSuiteName);
	}

	@Override
	public String getDefaultTestName() {
		// TODO Auto-generated method stub
		return super.getDefaultTestName();
	}

	@Override
	public void setDefaultTestName(String defaultTestName) {
		// TODO Auto-generated method stub
		super.setDefaultTestName(defaultTestName);
	}

	@Override
	public void setConfigFailurePolicy(FailurePolicy failurePolicy) {
		// TODO Auto-generated method stub
		super.setConfigFailurePolicy(failurePolicy);
	}

	@Override
	public void setConfigFailurePolicy(String failurePolicy) {
		// TODO Auto-generated method stub
		super.setConfigFailurePolicy(failurePolicy);
	}

	@Override
	public FailurePolicy getConfigFailurePolicy() {
		// TODO Auto-generated method stub
		return super.getConfigFailurePolicy();
	}

	@Override
	public void setHasFailure(boolean hasFailure) {
		// TODO Auto-generated method stub
		super.setHasFailure(hasFailure);
	}

	@Override
	public void setHasFailureWithinSuccessPercentage(boolean hasFailureWithinSuccessPercentage) {
		// TODO Auto-generated method stub
		super.setHasFailureWithinSuccessPercentage(hasFailureWithinSuccessPercentage);
	}

	@Override
	public void setHasSkip(boolean hasSkip) {
		// TODO Auto-generated method stub
		super.setHasSkip(hasSkip);
	}

	@Override
	public void setMethodInterceptor(IMethodInterceptor methodInterceptor) {
		// TODO Auto-generated method stub
		super.setMethodInterceptor(methodInterceptor);
	}

	@Override
	public void setDataProviderThreadCount(int count) {
		// TODO Auto-generated method stub
		super.setDataProviderThreadCount(count);
	}

	@Override
	public void addClassLoader(ClassLoader loader) {
		// TODO Auto-generated method stub
		super.addClassLoader(loader);
	}

	@Override
	public void setPreserveOrder(boolean b) {
		// TODO Auto-generated method stub
		super.setPreserveOrder(b);
	}

	@Override
	protected long getStart() {
		// TODO Auto-generated method stub
		return super.getStart();
	}

	@Override
	protected long getEnd() {
		// TODO Auto-generated method stub
		return super.getEnd();
	}

	@Override
	public void setGroupByInstances(boolean b) {
		// TODO Auto-generated method stub
		super.setGroupByInstances(b);
	}

	@Override
	public void setServiceLoaderClassLoader(URLClassLoader ucl) {
		// TODO Auto-generated method stub
		super.setServiceLoaderClassLoader(ucl);
	}

	@Override
	public List<ITestNGListener> getServiceLoaderListeners() {
		// TODO Auto-generated method stub
		return super.getServiceLoaderListeners();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

}
