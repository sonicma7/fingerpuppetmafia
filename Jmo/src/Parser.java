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

    // Temp since main site seems down....
    static final String URL = "http://dl.dropbox.com/u/2923833/current.kml";

    // Shuttle Data
    Vector shuttles = new Vector();

    // String to hold output of parse and display errors.
    StringItem resultItem = new StringItem ("", "");

    // This class reads the XML from the HTTP connection
    class readXML extends Thread{
        public void run() {
            try {
                // Clear Vector List
                shuttles = new Vector();

                //Open http connection
		HttpConnection httpConnection = (HttpConnection) Connector.open(URL);

                //Initilialize XML parser
                KXmlParser parser = new KXmlParser();

                // Read HTTP stream into parser
                try {
                    parser.setInput(new InputStreamReader(httpConnection.openInputStream()));
                }
                catch (Exception e) {
                    System.out.println("HTTP Error: " + e);
                }

                // Find the correct Tag to start on
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, "kml");
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, "Document");

                // Begin parsing the Document Tag
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

                        if (nodeName.compareTo("Placemark") == 0) {
                            parser.require(XmlPullParser.START_TAG, null, "Placemark");
                            shuttles.addElement(parseShuttle(parser));
                            parser.nextTag();
                            parser.nextTag();
                            parser.nextTag();
                            parser.nextTag();
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
                        System.out.println("Parse Exception: " + e);
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

            // Lets check out the data we gathered...
            // This is for debugging...
            /*
            for (Enumeration e = shuttles.elements(); e.hasMoreElements();) {
                Shuttle blah = (Shuttle) e.nextElement();
                System.out.println(blah.getDescription());

                // Exepect these to be the same until I actually parse the String
                System.out.println(blah.getCoordinates().getLongitude());
                System.out.println(blah.getCoordinates().getLatitude());
            }
            System.out.println("All objects printed!");
             */
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

                    if(nodeName.compareTo("name") == 0) {
                        bus.setDescription(parser.nextText());
                        parser.nextTag();
                    }
                    else if(nodeName.compareTo("coordinates") == 0) {

                        try {
                            // Parse the string into 2 doubles. Ignore 3rd arg (altitude)
                            String coor = parser.nextText();
                            parser.nextTag();
                            parser.nextTag();

                            // Convert the string data to numbers
                            String stack1 = "";
                            String stack2 = "";
                            int currentnum = 1;
                            for (int x = 0; x < coor.length(); x++){
                                if (coor.substring(x,x+1).equals(",")) {
                                    if (currentnum == 1){
                                        currentnum = 2;
                                    }
                                    else {
                                        x = coor.length();
                                    }
                                }
                                else {
                                    if (currentnum == 1) {
                                        stack1 = stack1 + coor.substring(x,x+1);
                                    }
                                    else if (currentnum == 2) {
                                        stack2 = stack2 + coor.substring(x,x+1);
                                    }
                                    else {
                                        x = coor.length();
                                    }
                                }
                            }
                            double coord1 = Double.parseDouble(stack1);
                            double coord2 = Double.parseDouble(stack2);
                            Position p = new Position();
                            p.setLatitude(coord2);
                            p.setLongitude(coord1);
                            bus.setCoordiantes(p);
                        }
                        catch (Exception e) {
                            // Something happending during the parse
                            System.out.println("Error during coordinate parse: " + e);
                            bus.setCoordinates(0.0, 0.0);
                        }
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