package razgovorchiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Application {

	public static void main(String[] args)
			throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {

		// готовим URL
		StringBuilder url_b = new StringBuilder();
		String urlS = "http://mobile.navi.yandex.net/" + "userpoi/getpoints?uuid=12345678901234567890&" + "zoom=6&"
				+ "tl_lat=55.882954&" // top left широта
				+ "tl_lon=37.39108&" // top left долгота
				+ "br_lat=55.592408&" // bottom right широта
				+ "br_lon=37.842888&" // bottom right долгота
				+ "catlist=6&" + "rawcatlist=&" + "rawpointsformat=full&" + "ver=2&" + "utf&" + "lang=ru-RU";

		url_b.append(urlS);

		// Читаем данные из потока HTTP запроса, формируем String с содержимым
		// ответа (XML)
		URL new_url = new URL(url_b.toString());
		URLConnection yc = new_url.openConnection();

		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		StringBuilder responceSB = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			responceSB.append(inputLine);
		}
		in.close();
		String responceSTR = responceSB.toString();

		// temp
		PrintWriter pf = new PrintWriter(new File("d://ya-razgovorchiki.xml"));
		pf.write(responceSTR);
		pf.close();

		// Читаем данные вытянутого XML через модель DOM
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource is = new InputSource(new StringReader(responceSTR));
		Document doc = builder.parse(is);

		NodeList talkList = doc.getElementsByTagName("wpt");
		for (int i = 0; i < talkList.getLength(); i++) {

			Node t = talkList.item(i);
			if (t.getNodeType() == Node.ELEMENT_NODE) {

				Element talk = (Element) t;
				String lat = talk.getAttribute("lat");
				String lon = talk.getAttribute("lon");
				String catidx = talk.getAttribute("catidx");
				String point_id = talk.getAttribute("point_id");

				String name = "";
				String comment = "";
				String time = "";

				NodeList talkDetails = talk.getChildNodes();
				for (int y = 0; y < talkDetails.getLength(); y++) {

					Node d = talkDetails.item(y);
					if (d.getNodeType() == Node.ELEMENT_NODE) {
						Element detail = (Element) d;
						switch (detail.getTagName()) {
						case "name":
							name = detail.getTextContent();
							break;
						case "comment":
							comment = detail.getTextContent();
							break;
						case "time":
							time = detail.getTextContent();
							break;
						}
					}
				}
				String rec = "lat:" + lat + " | lon:" + lon + " | catidx:" + catidx + " | name:" + name + " | comment:"
						+ comment + " | time:" + time + " | point_id:" + point_id;
				System.out.println(rec);
			}
		}
	}
}
