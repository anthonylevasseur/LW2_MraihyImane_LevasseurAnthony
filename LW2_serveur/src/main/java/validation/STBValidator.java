package validation;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class STBValidator {
	
	public Document validate_with_DOM(InputSource is) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
	
			SchemaFactory schemaFactory = 
			    SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
	
			factory.setSchema(schemaFactory.newSchema(
			    new Source[] {new StreamSource(getClass().getResource("/stb.xsd").getFile())}));
			DocumentBuilder builder = factory.newDocumentBuilder();
			SimpleErrorHandler ser = new SimpleErrorHandler();
			builder.setErrorHandler(ser);
			Document document = builder.parse(is);
			return (ser.hasError()) ? null : document;
		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static void main(String[] args) {
		JFileChooser jfc = new JFileChooser();
		jfc.showOpenDialog(null);
		STBValidator val= new STBValidator();
		Document d = null;
			byte[] encoded;
			String stb = null;
			try {
				encoded = Files.readAllBytes(Paths.get(jfc.getSelectedFile().getAbsolutePath()));
				stb = new String(encoded);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(stb);
			d = val.validate_with_DOM(
					new InputSource(new StringReader(stb)));
			System.out.println(d);
	}

}
