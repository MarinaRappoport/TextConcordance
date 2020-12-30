package service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.DbData;

import java.io.*;

public class XmlSerializer {
	private final static XmlMapper XML_MAPPER = new XmlMapper();
	private final static String XML_NAME = "concordanceDB.xml";

	public static void exportToXml(File folder) throws IOException {
		File xmlFile = new File(folder, XML_NAME);
		DbData dbData = new DbData();
		dbData.initFromDB();
		XML_MAPPER.writerWithDefaultPrettyPrinter().writeValue(xmlFile, dbData);
	}

	public static DbData importFromXml(File xmlFile) throws IOException {
		String xml = inputStreamToString(new FileInputStream(xmlFile));
		return XML_MAPPER.readValue(xml, DbData.class);
	}

	private static String inputStreamToString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}
}
