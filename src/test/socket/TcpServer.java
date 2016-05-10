package test.socket;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ServerSocketFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TcpServer {
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(18001);
			//ServerSocket server = ServerSocketFactory.getDefault().createServerSocket(18001, 30, null);
			while(true){
				Socket socket = server.accept();
				
				// 设置接收数据时等待的超时时间  0表示永不超时
				socket.setSoTimeout(0 * 1000);
				
				// 调用socket.close()方法时，是否立即关闭底层链接 60表示阻塞60秒，然后关闭底层链接
				socket.setSoLinger(true, 60);
				
				// SimpleXmlHandler
				InputStream in = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "8859_1"));
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				Matcher matcher = null;

				do{
					String temp = reader.readLine();
					if(temp == null){
						break;
					}
					out.write(temp.getBytes("8859_1"));
					out.write("\r\n".getBytes());
					matcher = Pattern.compile("</ *" + "Message" + " *>").matcher(temp);
				}while(!matcher.find());
				
				byte[] b = out.toByteArray();
				
				
				// parse
				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
				DefaultHandler hander = new DefaultHandler(){

					@Override
					public void startDocument() throws SAXException {
						System.out.println("解析开始！");
					}

					@Override
					public void endDocument() throws SAXException {
						System.out.println("解析结束！");
					}

					@Override
					public void startElement(String uri, String localName, String qName, Attributes attributes)
							throws SAXException {
						System.out.println("uri       :" + uri);
						System.out.println("localName :" + localName);
						System.out.println("qName     :" + qName);
						System.out.println("attributes:" + attributes);
					}

					@Override
					public void endElement(String uri, String localName, String qName) throws SAXException {
						System.out.println("uri       :" + uri);
						System.out.println("localName :" + localName);
						System.out.println("qName     :" + qName);
					}

					@Override
					public void characters(char[] ch, int start, int length) throws SAXException {
						System.out.println("content   :" + new String(ch, start, length));
					}
					
				};
				parser.parse(new ByteArrayInputStream(b), hander);
				hander.toString();
				
				// process
				
				// format
				String response = "<Message><name>fan</name><age>18</age></Message>";
				socket.getOutputStream().write(response.getBytes());
				socket.getOutputStream().flush();
				
				
				socket.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
