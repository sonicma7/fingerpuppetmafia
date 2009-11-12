
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.ScalableGraphics;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGSVGElement;

/*SVGStaticCanvas loads the scalable vector graphics files when called, uses Graphics g,
 * Paint, repaint, zoom in and out, moving between the stops as well as running the 
 * main part of the program with threads, sleeping them when necessary
 */
class SVGStaticCanvas extends GameCanvas implements Runnable {
    private SVGImage svgImage = null;
    /*following variables are used in order to correlate the gps coordinates to
     * the pixels on the svg images
     */
    double minlong = -73.66281509399414f;
    double maxlong = -73.68903636932373f;
    double minlat = 42.72204709206166f;
    double maxlat = 42.73903830914378f;
    double longvar = 240/(maxlong - minlong);
    double latvar = 210/(maxlat - minlat);


    //the following variables set up the SVG image and deal with positioning
    private ScalableGraphics gc;
    SVGSVGElement myEl = null;
    SVGPoint p1;
    SVGRect myRect;
    float f1,fw,fh,fz;
    int xcord=0, ycord=0;
    LocPosition mylp;
    float xoffset = 0, yoffset = 0;

    //the following variables set up, call and slow down the parser
    Parser myparse = new Parser();
    Vector shuttles;
    int counter = 0;
    

    /* SVGStaticCanvas sets up our canvases, depending on which canvas we want called.
     * Canvas 1 = East bus route, 2 = West bus route, else regular campus map
     * Takes in a key event, Location and a canvas variable
     */
    protected   SVGStaticCanvas(boolean supKeyEvent,LocPosition lp, int canvas ) {
        super(true);
        mylp = lp;
        gc = ScalableGraphics.createInstance();
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
                
        myEl = (SVGSVGElement)(svgImage.getDocument().getDocumentElement());
        myRect = myEl.getBBox();
        fw=myRect.getWidth();
        fh=myRect.getHeight();        
    }

    /*Paint is responsible for refreshing the SVG image, displaying the user
     * location, as well as the locations of all of the shuttles
     */
    public void paint(Graphics g){
        //set up initial SVG image with background
        g.setColor(0x00ffff50);
        g.fillRect(0, 0, getWidth(), getHeight());    
        g.setColor(0x00ffff50);
        gc.bindTarget(g);
        g.setColor(0x00ff5050);        
        gc.render(0, 0, svgImage);
        g.setColor(0x0000000);
        //Currently a person is an x that is boxed in
        g.drawLine(xcord + (int)(xoffset) - 4, ycord + (int)(yoffset) - 4, xcord + (int)(xoffset) + 4, ycord + (int)(yoffset) + 4);
        g.drawLine(xcord + (int)(xoffset) - 4, ycord + (int)(yoffset) + 4, xcord + (int)(xoffset) + 4, ycord + (int)(yoffset) - 4);
        g.drawRect(xcord + (int)(xoffset) - 4, ycord + (int)(yoffset) - 4,8,8);
        gc.releaseTarget();

        //as long as we have data for the shuttles, try and get them
        if(shuttles != null){
            //x and y coordinate variables for the x and y location of shuttles
            double x;
            double y;
            //loops through shuttles, takes zoom into account
            for (int q = 0; q < shuttles.size(); q++){
                x = (((Shuttle)(shuttles.elementAt(q))).getCoordinates().getLongitude() - minlong) * longvar + (int)xoffset;
                y = (((Shuttle)(shuttles.elementAt(q))).getCoordinates().getLatitude() - minlat) * latvar + 100 + (int)yoffset;

                //displays the shuttles as a rectangle
                gc.bindTarget(g);
                g.setColor(0x0000000);
                g.fillRect((int)x,(int)y,8,8);
                gc.releaseTarget();
            }

        }
        
    }

    /* zoomIn is responsible for the zoom in case, gets current svg point coordinates
     * scales it by 1.2, subtracts current scale from that, then modifies the x and y coordinates.
     * Also responsible for updating the x and y offset for the busses and repaints
     */
    public void zoomIn() {
        //get old coordinates
        float fa =  myEl.getCurrentScale();
        p1= myEl.getCurrentTranslate();
        float fx = p1.getX();
        float fy=p1.getY();
        //update scale
        myEl.setCurrentScale(myEl.getCurrentScale() * 1.2f);
        fz= myEl.getCurrentScale() - fa;
        //update new coordinates
        float transx = fx - (fw/2)*fz;
        float transy =  fy - (fh/2)*fz ;
        p1.setX(transx);
        p1.setY(transy);
        //update bus coordinates
        xoffset -= (fw/2)*fz;
        yoffset -= (fw/2)*fz;
        repaint();
    }

    /* zoomOut is responsible for the zoom in case, gets current svg point coordinates
     * scales it by 1.2, subtracts that from current scale, then modifies the x and y coordinates.
     * Also responsible for updating the x and y offset for the busses and repaints
     */
    public void zoomOut() {
        //get old coordinates
        float fa =  myEl.getCurrentScale();
        p1= myEl.getCurrentTranslate();
        float fx = p1.getX();
        float fy=p1.getY();
        //update scale
        myEl.setCurrentScale(myEl.getCurrentScale() /(1.20f));
        float fz= fa - myEl.getCurrentScale() ;
        //update new coordinates
        float transx= fx + (fw/2)*fz;
        float transy =  fy + (fh/2)*fz ;
        p1.setX( transx);
        p1.setY(transy);
        //update bus coordinates
        xoffset += (fw/2)*fz;
        yoffset += (fw/2)*fz;
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
            p1= myEl.getCurrentTranslate();
            float fx= p1.getX();
            float fy=p1.getY();

            //if key is pressed, react correctly (move screen at the moment)
            if ((keyState & LEFT_PRESSED) != 0) {
                p1.setX(fx + 10);
                xoffset += 10;
                keyState = 0;
            }
            if ((keyState & RIGHT_PRESSED) != 0) {
                p1.setX(fx - 10);
                xoffset -= 10;
                keyState=0;
            }
            if ((keyState & UP_PRESSED) != 0) {
                p1.setY(fy + 10);
                yoffset += 10;
                keyState=0;
            }
            if ((keyState & DOWN_PRESSED) != 0) {
                p1.setY(fy - 10);
                yoffset -= 10;
                keyState=0;
            }
            //get latitude and longitude and calculate coordinate
            double lat = mylp.getLatitude();
            double longit = mylp.getLongitude();
            calc(lat,longit);
            //sleep the thread for 50ms
            try{
                Thread.sleep(50);
                //after 2500 ms, start the parser
                if (counter == 50){
                    counter++;
                    myparse.go();
                }
                //this is so we do not get new data every 50ms
                else if (counter < 200 ){
                    counter++;
                }
                //after 25000ms, evaluate the shuttles and display them
                else{
                    counter = 0;
                    System.out.println("Something happened!!!!");
                    shuttles = myparse.getShuttles();
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
        double z = (42.72204709206166 - minlat) * latvar + 100;
        double r = (-73.66281509399414 - minlong) * longvar;
        //check to see if we are in bounds
         ycord = (int) z;
         xcord = (int) r;
     }
}