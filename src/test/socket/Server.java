package test.socket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(18001);
			Socket socket = server.accept();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "8859_1"));
			byte[] b = new byte[1024];
			StringBuffer sb = new StringBuffer();
			Matcher matcher = null;
			String temp;
			do{
				sb.append(new String(b));
				matcher = Pattern.compile("</ *" + "Message" + " *>").matcher(sb.toString());
			}while(!matcher.find());
			
//			BufferedInputStream  reader = new BufferedInputStream(socket.getInputStream());
//			byte[] b = new byte[1024];
//			StringBuffer sb = new StringBuffer();
//			Matcher matcher = null;
//			do{
//				if(reader.read(b) == -1){
//					break;
//				}
//				sb.append(new String(b));
//				matcher = Pattern.compile("</ *" + "Message" + " *>").matcher(sb.toString());
//			}while(!matcher.find());
			
			System.out.println(sb);
			
			String request = "<Message><userid>123</userid></Message>";
			socket.getOutputStream().write(request.getBytes());
			socket.getOutputStream().flush();
			
			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
