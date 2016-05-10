package test.socket;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.SocketFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fan.service.common.tcp.AbstractTcpTransport;

public class TcpTransport extends AbstractTcpTransport{

	public static void main(String[] args) {
		try {
			
			Socket socket = new Socket("127.0.0.1", 18001);
			//Socket socket = SocketFactory.getDefault().createSocket("localhost", 18001);
			
			socket.setReceiveBufferSize(8192);
			socket.setSendBufferSize(8192);
			socket.setSoTimeout(10 * 60 * 1000);
			socket.setKeepAlive(false);
			socket.setTcpNoDelay(false);
			
			String request = "<Message><userid>123</userid></Message>";
			socket.getOutputStream().write(request.getBytes());
			socket.getOutputStream().write("\r\n".getBytes());
			socket.getOutputStream().flush();
			
			// SimpleXmlHandler
			InputStream in = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "8859_1"));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			Matcher matcher = null;
			String temp;
			while((temp = reader.readLine()) != null){
				out.write(temp.getBytes("8859_1"));
				out.write("\r\n".getBytes());
				matcher = Pattern.compile("</ *" + "Message" + " *>").matcher(temp);
				if(matcher.find()){
					break;
				}
			}
			
//			Matcher matcher = null;
//			do{
//				String temp = reader.readLine();
//				if(temp == null){
//					break;
//				}
//				out.write(temp.getBytes("8859_1"));
//				out.write("\r\n".getBytes());
//				matcher = Pattern.compile("</ *" + "Message" + " *>").matcher(temp);
//			}while(!matcher.find());
			
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
			
			socket.close();
			// process
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
