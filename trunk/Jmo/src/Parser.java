// KML Imports
import org.kxml2.io.*;
import org.xmlpull.v1.*;

// JME Imports
import javax.microedition.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.Enumeration;

// Misc Java Imports
import java.io.*;
import java.util.Vector;

public class Parser{

    // URL of Master KML file
    static final String URL = "http://shuttles.rpi.edu/positions/current.kml";

    // Shuttle Data
    Vector shuttles = new Vector();

    // String to hold output of parse
    StringItem resultItem = new StringItem ("", "");

    class readXML extends Thread{
        public void run() {
		try {
                        System.out.println("Opening Connection...");

		        //Open http connection
		        HttpConnection httpConnection = (HttpConnection) Connector.open(URL);


                        System.out.println("Create Parser....");
			//Initilialize XML parser
			KXmlParser parser = new KXmlParser();

                        System.out.println("Get From Connection...");
			parser.setInput(new InputStreamReader(httpConnection.openInputStream()));

                        System.out.println("Read Data...");
                        parser.nextTag();
                        parser.require(XmlPullParser.START_TAG, null, "kml");
                        parser.nextTag();
                        parser.require(XmlPullParser.START_TAG, null, "Document");


                        System.out.println("Start parse...");
			//Iterate through our XML file
                        boolean parsing = (parser.getEventType() != XmlPullParser.END_TAG);

			while (parsing) {
                            try {
                                String nodeName = parser.getName();
                                System.out.println("Name: " + parser.getName());

                                if (nodeName.compareTo("Placemark") == 0) {
                                    System.out.println("Shuttle found. Adding...");
                                    shuttles.addElement(parseShuttle(parser));
                                    parser.nextTag();
                                    System.out.println("Shuttle add complete!");

                                }
                                else {
                                    parser.nextTag();
                                }
                                parsing = (parser.getEventType() != XmlPullParser.END_TAG);
                            }
                            catch (Exception e){
                                System.out.println("Exception: " + e);
                                parser.nextTag();
                                if (parsing) {
                                }
                            }
                        }
                }
                catch (Exception e) {
	    	  	e.printStackTrace ();
	    		resultItem.setLabel ("Error:");
	    		resultItem.setText (e.toString ());

		}

                System.out.println("Parse Complete!");

                // Lets check out the data we gathered...
                for (Enumeration e = shuttles.elements(); e.hasMoreElements();) {
                    Shuttle blah = (Shuttle) e.nextElement();
                    System.out.println(blah.getDescription());
                    System.out.println(blah.getCoordinates()[0]);
                    System.out.println(blah.getCoordinates()[1]);
                }
                System.out.println("All objects printed!");
        }


        private Shuttle parseShuttle(KXmlParser parser) throws Exception {
            Shuttle bus = new Shuttle();

            boolean parsing = (parser.getEventType() != XmlPullParser.END_TAG);

            while (parsing) {
                try {

                    String nodeName = parser.getName();
                    System.out.println("Sub-Name: " + parser.getName());

                    if(nodeName.compareTo("name") == 0) {
                        bus.setDescription(parser.nextText());
                        parser.nextTag();
                    }
                    else if(nodeName.compareTo("coordinates") == 0) {
                        // Parse the string into 2 ints. Ignore 3rd arg
                        parser.nextText();
                        bus.setCoordinates(2.0,2.0);
                        parser.nextTag();
                    }
                    else {
                        parser.nextTag();
                    }
                    parsing = (parser.getEventType() != XmlPullParser.END_TAG);
                }
                catch (Exception e){
                    System.out.println("Exception: " + e);
                    parser.nextTag();
                    if (parsing) {
                    }
                }
            }

            return bus;
        }

    }

    public void go(){
        new readXML().start();
    }
}
    
	
