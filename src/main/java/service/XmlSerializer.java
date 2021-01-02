package service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.DbData;

import java.io.*;

/**
 * Util for export to XML and import from XML
 */
public class XmlSerializer {
	private final static XmlMapper XML_MAPPER = new XmlMapper();
	private final static String XML_NAME = "concordanceDB.xml";

	/**
	 * Export DB data to XML
	 *
	 * @param folder where to save the xml file
	 * @throws IOException in case of failure
	 */
	public static void exportToXml(File folder) throws IOException {
		File xmlFile = new File(folder, XML_NAME);
		DbData dbData = new DbData();
		dbData.initFromDB();
		XML_MAPPER.writerWithDefaultPrettyPrinter().writeValue(xmlFile, dbData);
	}

	/**
	 * Import data to DB from xml file
	 * @param xmlFile file to use
	 * @throws IOException in case of failure
	 */
	public static void importFromXml(File xmlFile) throws IOException {
		String xml = inputStreamToString(new FileInputStream(xmlFile));
		DbData dbData = XML_MAPPER.readValue(xml, DbData.class);
		DbConnection.getInstance().clearDB();
		WordService.addWords(dbData.getWords());
		BookService.addBooks(dbData.getBooks());
		WordService.addWordLocationList(dbData.getWordLocations(), null);
		GroupService.addGroups(dbData.getGroups());
		GroupService.addWordInGroupList(dbData.getWordInGroupList());
		PhraseService.addPhraseIds(dbData.getPhrases());
		PhraseService.addWordInPhraseList(dbData.getWordInPhraseList());
		FilesManager.init();
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
