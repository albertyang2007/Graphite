package org.albertyang2007.graphite.jmeter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

//refer to:
//http://www.cnblogs.com/linglingyuese/p/linglingyuese_sex.html
//http://www.cnblogs.com/linglingyuese/articles/linglingyuese-two.html
//http://www.greatdba.com/1008/1008006/1008006001/%E4%BD%BF%E7%94%A8apache-jmeter%E6%B5%8B%E8%AF%95socket.html
public class SendDataToGraphite extends AbstractJavaSamplerClient {
	private SampleResult results;
	private String serverIp;
	private int serverPort;

	public SampleResult runTest(JavaSamplerContext sc) {
		serverIp = sc.getParameter("serverIp");
		serverPort = Integer.valueOf(sc.getParameter("serverPort"));
		String key = sc.getParameter("key");
		String value = sc.getParameter("value");
		int times = Integer.parseInt(sc.getParameter("times"));
		boolean flag = false;

		results.setSampleLabel("Send Data To Graphite");
		results.sampleStart();

		SocketClient client = new SocketClient(serverIp, serverPort);
		flag = client.sendMessage(key, value, times);

		results.sampleEnd();
		results.setSuccessful(flag);

		client.close();

		return results;
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
		params.addArgument("serverIp", "localhost");
		params.addArgument("serverPort", "2003");
		params.addArgument("key", "metrics.testkey");
		params.addArgument("value", "1");
		params.addArgument("times", "1");
		return params;
	}

	@Override
	public void setupTest(JavaSamplerContext context) {
		super.setupTest(context);
		results = new SampleResult();
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		super.teardownTest(context);
	}
}
