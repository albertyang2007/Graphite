package org.albertyang2007.graphite.jmeter;

import java.io.DataOutputStream;
import java.net.Socket;

public class SocketClient {
	private Socket socket = null;
	private DataOutputStream output = null;
	private String ipAddress;
	private int port;

	public SocketClient(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public boolean sendMessage(String key, String value) {
		try {
			long timestamp = System.currentTimeMillis() / 1000;

			String message = "\n" + key + " " + value + " " + timestamp + "\n";

			open();

			output.write(message.getBytes("UTF-8"));
			output.flush();

			System.out.println(String.format("send data to %s:%d %s",
					this.ipAddress, this.port, message));

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			close();
			return false;
		}
	}

	private void open() {
		try {
			if (this.socket == null || this.socket.isClosed()) {
				this.socket = new Socket(this.ipAddress, this.port);
			}

			if (this.output == null) {
				this.output = new DataOutputStream(socket.getOutputStream());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (this.socket != null && !this.socket.isClosed()) {
				this.socket.close();
				this.socket = null;
			}

			if (this.output != null) {
				this.output.close();
				this.output = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean sendMessage(String key, String value, int times) {
		boolean rtn = true;
		for (int i = 0; i < times; i++) {
			rtn &= this.sendMessage(key, value);
		}
		return rtn;
	}

	private void sendMessage(String key, String value, int times,
			int sleepMillseconds) {
		for (int i = 0; i < times; i++) {
			this.sendMessage(key, value);
			this.sleep(sleepMillseconds);
		}
	}

	private void sleep(int millseconds) {
		try {
			System.out.println("Sleep " + millseconds);
			Thread.sleep(millseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SocketClient client = new SocketClient("10.178.255.114", 2003);
		String key = "metrics.testkey3";
		String value = "10";
		int times = 100;
		int sleepMillseconds = 1;
		client.sendMessage(key, value, times);
		client.close();
	}
}
