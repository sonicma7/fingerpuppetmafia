// KML Imports
import org.kxml2.io.*;
import org.xmlpull.v1.*;

// JME Imports
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.util.Enumeration;

// Misc Java Imports
import java.io.*;
import java.util.Vector;

// This is the main class. It holds the classes for the parser and such.
public class Parser{

    // URL of shuttle KML file.
    //static final String URL = "http://shuttles.rpi.edu/positions/current.kml";

    // temp
    static final String URL = "http://dl.dropbox.com/u/2923833/current.kml";

    // Shuttle Data
    Vector shuttles = new Vector();

    // String to hold output of parse and display errors.
    StringItem resultItem = new StringItem ("", "");

    // This class reads the XML from the HTTP connection
    class readXML extends Thread{
        public void run() {
            try {
                //Open http connection
                System.out.println("Opening Connection...");
		        HttpConnection httpConnection = (HttpConnection) Connector.open(URL);

                //Initilialize XML parser
                System.out.println("Create Parser....");
                KXmlParser parser = new KXmlParser();

                // Read HTTP stream into parser
                System.out.println("Get From Connection...");
                try {
                    parser.setInput(new InputStreamReader(httpConnection.openInputStream()));
                }
                catch (Exception e) {
                    System.out.println("WTF: " + e);
                }

                // Find the correct Tag to start on
                System.out.println("Read Data...");
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, "kml");
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, "Document");

                // Begin parsing the Document Tag
                System.out.println("Start parse...");

                //Iterate through our XML file
                boolean parsing = (parser.getEventType() != XmlPullParser.END_TAG);
                while (parsing) {
                    /* This checks each tag. If it finds a "Placemark"
                     * tag, it knows it's a new shuttle and adds it then
                     * sends the remainder of the data to another function
                     * to get furthur parsed with the right shuttle
                     * data. */
                    try {
                        String nodeName = parser.getName();
                        System.out.println("Name: " + parser.getName());

                        if (nodeName.compareTo("Placemark") == 0) {
                            System.out.println("Shuttle found. Adding...");
                            parser.require(XmlPullParser.START_TAG, null, "Placemark");
                            shuttles.addElement(parseShuttle(parser));
                            parser.nextTag();
                            parser.nextTag();
                            parser.nextTag();
                            parser.nextTag();
                            System.out.println("Name: " + parser.getName());

                            //parser.require(XmlPullParser.END_TAG, null, "Placemark");
                            System.out.println("Shuttle add complete!");
                        }
                        else {
                            parser.nextTag();
                        }

                        parsing = (parser.getEventType() != XmlPullParser.END_TAG);
                    }
                    catch (Exception e){
                        /* This is required because the XML uses some stupid
                         * tag like <! Text !>. This confuses the parser.
                         * You'll see this same workaround below... */
                        System.out.println("Exception: " + e);
                        parser.nextTag();
                        if (parsing) {
                        }
                    }
                }
            }
            catch (Exception e) {
                // Catch general errors
                e.printStackTrace ();
                resultItem.setLabel ("Error:");
                resultItem.setText (e.toString ());
            }

            System.out.println("Parse Complete!");

            // Lets check out the data we gathered...
            for (Enumeration e = shuttles.elements(); e.hasMoreElements();) {
                Shuttle blah = (Shuttle) e.nextElement();
                System.out.println(blah.getDescription());

                // Exepect these to be the same until I actually parse the String
                System.out.println(blah.getCoordinates()[0]);
                System.out.println(blah.getCoordinates()[1]);
            }
            System.out.println("All objects printed!");
        }

        // Parses the shuttle data
        private Shuttle parseShuttle(KXmlParser parser) throws Exception {
            Shuttle bus = new Shuttle();

            boolean parsing = (parser.getEventType() != XmlPullParser.END_TAG);

            while (parsing) {
                try {
                    // Similar to above, but parses what's below the "Placemark"
                    // tag.
                    String nodeName = parser.getName();
                    System.out.println("Sub-Name: " + parser.getName());

                    if(nodeName.compareTo("name") == 0) {
                        bus.setDescription(parser.nextText());
                        parser.nextTag();
                    }
                    else if(nodeName.compareTo("coordinates") == 0) {
                        // Parse the string into 2 ints. Ignore 3rd arg
                        parser.nextText();
                        bus.setCoordinates(-73.67,42.728);
                        parser.nextTag();
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
            // Send the filled out shuttle object back up to the Vector
            return bus;
        }
    }

    public void go(){
        new readXML().start();
    }

    public Vector getShuttles() {
        return shuttles;
    }


}