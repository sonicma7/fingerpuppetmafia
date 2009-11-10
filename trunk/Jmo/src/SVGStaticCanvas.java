
import java.io.InputStream;
import java.util.Vector;
import java.util.Enumeration;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.ScalableGraphics;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGSVGElement;

class SVGStaticCanvas extends GameCanvas implements Runnable {
    private SVGImage svgImage = null;
    double minlong = -73.66212844848633f;
    double maxlong = -73.69083881378174f;
    double minlat = 42.72207861996528f;
    double maxlat = 42.73913286689956f;
    double longvar = 240/(maxlong - minlong);
    double latvar = 400/(maxlat - minlat);

    private ScalableGraphics gc;
    SVGSVGElement myEl = null;
    SVGPoint p1;
    SVGRect myRect;
    float f1,fw,fh;
    float globx=0, globy=0;
    int xcord=0, ycord=0;
    LocPosition mylp;

    /* SVGStaticCanvas sets up our canvases, depending on which canvas we want called.
     * Canvas 1 = East bus route, 2 = West bus route, else regular campus map
     *
     */
    protected   SVGStaticCanvas(boolean supKeyEvent,LocPosition lp, int canvas ) {
        super(true);
        mylp = lp;
        gc = ScalableGraphics.createInstance();
        this.setFullScreenMode(true);
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
    public void paint(Graphics g){                
        g.setColor(0x00ffff50);
        g.fillRect(0, 0, getWidth(), getHeight());    
        g.setColor(0x00ffff50);
        gc.bindTarget(g);
        g.setColor(0x00ff5050);        
        gc.render(0, 0, svgImage);
        g.setColor(0x0ffffff);
        g.fillRect(xcord + (int)(globx), ycord + (int)(globy),8,8);
        gc.releaseTarget();    
    }

    public void updatebus(Vector shuttles, Graphics g) {
        double x;
        double y;
        Shuttle s1;
        gc.bindTarget(g);
        gc.render(0, 0, svgImage);
        gc.releaseTarget();
        for (int q = 0; q < shuttles.size(); q++){
            x = - (((Shuttle)(shuttles.elementAt(q))).getCoordinates()[0] - minlong) * longvar;
            y = (((Shuttle)(shuttles.elementAt(q))).getCoordinates()[1] - minlat) * latvar + 100;
            gc.bindTarget(g);
            g.setColor(0x0000000);
            g.fillRect((int)x,(int)y,8,8);
            gc.releaseTarget();
        }
    }
        
    public void zoomin() {
        float fa =  myEl.getCurrentScale();
        p1= myEl.getCurrentTranslate();
        float fx = p1.getX();
        float fy=p1.getY();
        myEl.setCurrentScale(myEl.getCurrentScale() * 1.2f);
        float fz= myEl.getCurrentScale() - fa;   
        float transx= fx - (fw/2)*fz;
        float transy =  fy - (fh/2)*fz ;
        p1.setX( transx);
        p1.setY(transy);
        repaint();
    }

    public void zoomout() {
        float fa =  myEl.getCurrentScale();
        p1= myEl.getCurrentTranslate();
        float fx = p1.getX();
        float fy=p1.getY();
        myEl.setCurrentScale(myEl.getCurrentScale() /(1.20f));
        float fz= fa - myEl.getCurrentScale() ;
        float transx= fx + (fw/2)*fz;
        float transy =  fy + (fh/2)*fz ;
        p1.setX( transx);
        p1.setY(transy);
        repaint();
    }
    public void run() {
        while( true ) {
            int keyState = getKeyStates();
            p1= myEl.getCurrentTranslate();
            float fx= p1.getX();
            float fy=p1.getY();

            if ((keyState & LEFT_PRESSED) != 0) {
                p1.setX(fx + 10);
                globx+= 10;
                keyState = 0;
            }
            if ((keyState & RIGHT_PRESSED) != 0) {
                p1.setX(fx - 10);
                globx-=10;
                keyState=0;
            }
            if ((keyState & UP_PRESSED) != 0) {
                p1.setY(fy + 10);
                globy+=10;
                keyState=0;
            }
            if ((keyState & DOWN_PRESSED) != 0) {
                p1.setY(fy - 10);
                globy-=10;
                keyState=0;
            }
            double lat = mylp.getLatitude();
            double longit = mylp.getLongitude();
            calc(lat,longit);
            try{
                Thread.sleep(50);
            }
            catch(Exception e){};
            repaint();
        }
    }

    void calc(double lat, double longit){   
        double z = ( (lat - minlat)/(maxlat -minlat))*400;
        double r = ((longit - minlong)/(maxlong - minlong))*240;
         
        if (z < 0)
            z = 0;
        if (z > 400.0)
            z = 400.0;
         if (r <0)
             r = 0;
         if (r > 240.0)
             r = 240.0;
         z = myEl.getCurrentScale()*(z -200) + 200;
         r  = myEl.getCurrentScale()*(r - 120) + 120;
         xcord = (int) z;
         ycord = (int) r;
     }
}