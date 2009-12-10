
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.ScalableGraphics;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGSVGElement;

/*SVGStaticCanvas loads the scalable vector graphics files when called, uses Graphics screen,
 * Paint, repaint, zoom in and out, moving between the stops as well as running the 
 * main part of the program with threads, sleeping them when necessary
 */
class SVGStaticCanvas extends GameCanvas implements Runnable {
    private SVGImage svgImage = null;
    /*following variables are used in order to correlate the gps coordinates to
     * the pixels on the svg images
     */
    double maxlong = -73.66281509399414f;
    double minlong = -73.68903636932373f;
    double maxlat = 42.72204709206166f;
    double minlat = 42.73903830914378f;
    double longvar = 240/(maxlong - minlong);
    double latvar = 210/(maxlat - minlat);


    //the following variables set up the SVG image and deal with positioning
    private ScalableGraphics graphics;
    SVGSVGElement svgelement = null;
    SVGPoint point;
    SVGRect myRect;
    float rectwidth,rectheight,zoom;
    int xcord=0, ycord=0;
    LocPosition locpos;
    float xoffset = 0, yoffset = 0;

    //the following variables set up, call and slow down the parser
    int parseno = 0;
    Parser myparse = new Parser();
    int counter = 0;
    Data thisdata = new Data();
    

    /* SVGStaticCanvas sets up our canvases, depending on which canvas we want called.
     * Canvas 1 = East bus route, 2 = West bus route, else regular campus map
     * Takes in a key event, Location and a canvas variable
     */
    protected   SVGStaticCanvas(boolean supKeyEvent,LocPosition lp, int canvas ) {
        super(true);
        locpos = lp;
        graphics = ScalableGraphics.createInstance();
        this.setFullScreenMode(true);
        
        //depending on what canvas number is, try and create the image
        if(canvas == 1){
            try{
               InputStream is = getClass().getResourceAsStream("East.svg");
               svgImage = (SVGImage) SVGImage.createImage(is, null);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else {
            if(canvas== 2){
                try{
                    InputStream is = getClass().getResourceAsStream("West.svg");
                    svgImage = (SVGImage) SVGImage.createImage(is, null);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            else{
                try{
                    InputStream is = getClass().getResourceAsStream("Campus.svg");
                    svgImage = (SVGImage) SVGImage.createImage(is, null);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

        }
                
        svgelement = (SVGSVGElement)(svgImage.getDocument().getDocumentElement());
        myRect = svgelement.getBBox();
        rectwidth=myRect.getWidth();
        rectheight=myRect.getHeight();
        svgelement.setCurrentScale(svgelement.getCurrentScale() /(0.5f));
    }

    /*Paint is responsible for refreshing the SVG image, displaying the user
     * location, as well as the locations of all of the shuttles
     */
    public void paint(Graphics screen){
        //set up initial SVG image with background
        screen.setColor(0x00ffff50);
        screen.fillRect(0, 0, getWidth(), getHeight());
        screen.setColor(0x00ffff50);
        graphics.bindTarget(screen);
        screen.setColor(0x00ff5050);
        graphics.render(0, 0, svgImage);
        screen.setColor(0x0000000);
        //Currently a person is an x that is boxed in
        screen.drawLine(xcord + (int)(xoffset) - 4, ycord + (int)(yoffset) - 4, xcord + (int)(xoffset) + 4, ycord + (int)(yoffset) + 4);
        screen.drawLine(xcord + (int)(xoffset) - 4, ycord + (int)(yoffset) + 4, xcord + (int)(xoffset) + 4, ycord + (int)(yoffset) - 4);
        screen.drawRect(xcord + (int)(xoffset) - 4, ycord + (int)(yoffset) - 4,8,8);
        graphics.releaseTarget();

        //as long as we have data for the shuttles, try and get them
        if(thisdata.getShuttles() != null){
            //x and y coordinate variables for the x and y location of shuttles
            double x;
            double y;
            //loops through shuttles, takes zoom into account
            for (int q = 0; q < thisdata.getShuttles().size(); q++){
                x = (((Shuttle)(thisdata.getShuttles().elementAt(q))).getCoordinates().getLongitude() - minlong) * longvar ;
                y = (((Shuttle)(thisdata.getShuttles().elementAt(q))).getCoordinates().getLatitude() - minlat) * latvar  ;
                x = x/0.5 - 10 + (int)xoffset;
                y = y/0.5 + 185 + (int)yoffset;

                //x = 5 + (int)xoffset;
                //y = 90 + (int)yoffset;

                //displays the shuttles as a rectangle
                graphics.bindTarget(screen);
                screen.setColor(0xFFFF00);
                screen.fillRect((int)x,(int)y,8,8);
                screen.setColor(0x0000000);
                screen.drawRect((int)x,(int)y,8,8);
                graphics.releaseTarget();
            }

        }
        
    }

    /* zoomIn is responsible for the zoom in case, gets current svg point coordinates
     * scales it by 1.2, subtracts current scale from that, then modifies the x and y coordinates.
     * Also responsible for updating the x and y offset for the busses and repaints
     */
    public void zoomIn() {
    /*    //get old coordinates
        float fa =  svgelement.getCurrentScale();
        point= svgelement.getCurrentTranslate();
        float x = point.getX();
        float y=point.getY();
        //update scale
        svgelement.setCurrentScale(svgelement.getCurrentScale() * 1.2f);
        zoom= svgelement.getCurrentScale() - fa;
        //update new coordinates
        float transx = x - (rectwidth/2)*zoom;
        float transy =  y - (rectheight/2)*zoom ;
        point.setX(transx);
        point.setY(transy);
        //update bus coordinates
        xoffset -= (rectwidth/2)*zoom;
        yoffset -= (rectwidth/2)*zoom;
        repaint();*/
    }

    /* zoomOut is responsible for the zoom in case, gets current svg point coordinates
     * scales it by 1.2, subtracts that from current scale, then modifies the x and y coordinates.
     * Also responsible for updating the x and y offset for the busses and repaints
     */
    public void zoomOut() {
        /*//get old coordinates
        float fa =  svgelement.getCurrentScale();
        point= svgelement.getCurrentTranslate();
        float x = point.getX();
        float y=point.getY();
        //update scale
        svgelement.setCurrentScale(svgelement.getCurrentScale() /(1.20f));
        float zoom= fa - svgelement.getCurrentScale() ;
        //update new coordinates
        float transx= x + (rectwidth/2)*zoom;
        float transy =  y + (rectheight/2)*zoom ;
        point.setX( transx);
        point.setY(transy);
        //update bus coordinates
        xoffset += (rectwidth/2)*zoom;
        yoffset += (rectwidth/2)*zoom;
        repaint();*/
    }

    /*showStop takes in a route name and then finds the correct route from the data structure.
     * It then takes this route and based on the user location finds the closest stop number
     * After getting closest stop number, it adjusts the x and y coordinates of the map so that it
     * will center over the selected stop
     */
    public void showStop(String routename){
        int rte;

        if(routename == "East")
            rte = 0;
        else{
            rte = 1;
        }

        //get the user location
        UserLocation user = new UserLocation(locpos.getLatitude(),locpos.getLongitude());
        System.out.println("User:" + locpos.getLatitude() + "," + locpos.getLongitude());
        //get the correct route
        Route thisroute = ((Route)(thisdata.getRoutes()).elementAt(rte));
        int stop = user.getClosestStop(thisroute);
        //based on the stop, get the coordinates
        
        double longit = ((Position)(thisroute.getStopList().elementAt(stop))).getLongitude();
        double lat = ((Position)(thisroute.getStopList().elementAt(stop))).getLatitude();
        System.out.println("stop:" + stop + " At Latitude: " + lat + " and Longitude:" + longit);
        //double longit = -73.66587281227112;
        //double lat = 42.735692973147266;

        //using those coordinates, adjust the viewing plane
        double transy  = (-(lat - minlat) * latvar)*2 + 240/2 - 185;
        double transx  = 210/2 -((longit - minlong) * longvar)*2 + 10;
        System.out.println("Tranxy: " + transx + "," + transy);
        xoffset = (float)transx;
        yoffset = (float)transy;
        point.setX((float) transx);
        point.setY((float)transy);
        repaint();
    }
     /*showBus gets a new user location and finds the closest shuttle to the user
     * After getting shuttle, it adjusts the x and y coordinates of the map so that it
     * will center over the selected shuttle
     */
    public void showBus(){
        //get the user location
        UserLocation user = new UserLocation(locpos.getLatitude(),locpos.getLongitude());
        //get the correct route
        Shuttle thisshuttle = user.getClosestShuttle(thisdata.getShuttles());
        //based on the stop, get the coordinates
        double longit = ((Position)(thisshuttle.getCoordinates())).getLongitude();
        double lat = ((Position)(thisshuttle.getCoordinates())).getLatitude();
        //double longit = -73.67587281227112;
        //double lat = 42.738692973147266;

        //using those coordinates, adjust the viewing plane
        double transy  = (-(lat - minlat) * latvar)*2 + 240/2 - 185;
        double transx  = 210/2 -((longit - minlong) * longvar)*2 + 10;
        System.out.println("Tranxy: " + transx + "," + transy);
        xoffset = (float)transx;
        yoffset = (float)transy;
        point.setX((float) transx);
        point.setY((float)transy);
        repaint();
    }
    /* run is responsible for initializing key states, listening for key presses,
     * getting longitude and latitude and keeping track of threads for downloading
     * the new data as well as when to display the shuttles, also repaints
     */
    public void run() {

        while( true ) {
            //set up key states, current position and check for key presses
            int keyState = getKeyStates();
            point= svgelement.getCurrentTranslate();
            float x= point.getX();
            float y=point.getY();

            //if key is pressed, react correctly (move screen at the moment)
            if ((keyState & LEFT_PRESSED) != 0) {
                point.setX(x + 10);
                xoffset += 10;
                keyState = 0;
            }
            if ((keyState & RIGHT_PRESSED) != 0) {
                point.setX(x - 10);
                xoffset -= 10;
                keyState=0;
            }
            if ((keyState & UP_PRESSED) != 0) {
                point.setY(y + 10);
                yoffset += 10;
                keyState=0;
            }
            if ((keyState & DOWN_PRESSED) != 0) {
                point.setY(y - 10);
                yoffset -= 10;
                keyState=0;
            }
            //get latitude and longitude and calculate coordinate
            double lat = locpos.getLatitude();
            double longit = locpos.getLongitude();
            calc(lat,longit);
            //sleep the thread for 50ms
            try{
                Thread.sleep(50);
                //after 2500 ms, start the parser
                if (counter == 50){
                    counter++;
                    parseno++;
                    if (parseno > 5) {
                        parseno = 1;
                    }
                    myparse.go(Integer.toString(parseno));
                    
                }
                //this is so we do not get new data every 50ms
                else if (counter < 200 ){
                    counter++;
                }
                //after 25000ms, evaluate the shuttles and display them
                else{
                    counter = 0;
                    System.out.println("Something happened!!!!");
                    thisdata.setShuttles(myparse.getShuttles());
                    repaint();
                    
                }
            }
            catch(Exception e){
            System.out.println("Exception: " + e);
            }
            repaint();
        }
    }

    /*calc takes in longitude and latitude and calculates their respective locations
     * with regard to the SVG image in pixels
     */
    void calc(double lat, double longit){  
        
        //Test coordinates Lat :42.730854314509536f   Long:  -73.68256688117981f
        double z = ((locpos.getLatitude() - minlat) * latvar)*2 + 185;
        double r = ((locpos.getLongitude() - minlong) * longvar)*2 - 10;
        //check to see if we are in bounds
         ycord = (int) z;
         xcord = (int) r;
     }
}