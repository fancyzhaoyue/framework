package test.socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 18001);
			String request = "<Message><userid>123</userid></Message>";
			socket.getOutputStream().write(request.getBytes());
			socket.getOutputStream().flush();
			
			BufferedInputStream  reader = new BufferedInputStream(socket.getInputStream());
			byte[] b = new byte[1024];
			int len;
			StringBuffer sb = new StringBuffer();
			Matcher matcher = null;
			while((len = reader.read(b)) != -1){
				sb.append(new String(b));
				matcher = Pattern.compile("</ *" + "Message" + " *>").matcher(sb.toString());
				if(matcher.find()){
					break;
				}
			}
			System.out.println(sb);
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
