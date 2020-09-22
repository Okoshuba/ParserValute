//Задача: Написать программу, которая на основании запроса данных
// с сайта http://www.cbr.ru/scripts/XML_daily.asp определит курс
// гонконского доллара к российскому рублю

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ParserDollar {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        ParserDollar parserDollar = new ParserDollar();
        parserDollar.getResponse();

    }

    public static class Valute {
        private static String name;
        private static String value;

        public Valute (String name, String value) {
            this.name = name;
            this.value = value;
        }

        public static String getName() {
            return name;
        }

        public static String getValue() {
            return value;
        }
    }

    public void getResponse() throws IOException, ParserConfigurationException, SAXException {

        //Получения неотпарсенных данных через ссылку
        String url = "http://www.cbr.ru/scripts/XML_daily.asp";
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        int responseCode = connection.getResponseCode();
        //System.out.print("Response Code = " + responseCode + "\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        in.close();

        //Парсер
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(String.valueOf(obj));

        try {
            printValue(document);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

    }

    private static void printValue(Document document) throws DOMException, XPathExpressionException {
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("ValCurs/Valute[@ID ='R01200']/Value");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            System.out.println("Курс гонконского доллара к российскому рублю составляет " + n.getTextContent());
        }
        System.out.println();
    }
}
