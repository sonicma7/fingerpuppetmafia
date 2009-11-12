

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;

/* SVGSimpleMidlet sets up the basic menus, listeners for those menus,
 * different threads for the program and generally controls the application
 * Able to destroy, pause and start application here
 */
public class SVGSimpleMidlet extends MIDlet implements CommandListener {
    //set up variables for new svgCanvas, menus, parser
    SVGStaticCanvas svgCanvas = null;
    Command exit,zoomin,zoomout,cmd1,getstop,getshuttle;
    LocPosition lp;
    SimulateData sd;
    Parser par;
    TextBox tb1;
    TextBox tb2;
    Display disp;
    Thread canvThr,lpThr = null, sdThr = null, parThr = null;
    List list= null;

    /*startApp is responsible for starting the application, adding in commands
     * and starting different threads and command listeners
     */
    public void startApp() {
        //set up display
        disp  = Display.getDisplay(this);
        //svgCanvas = getCanvas();
        //set up menus
        cmd1 = new Command("Back",Command.BACK, 0);
        tb1 = new TextBox(" "," ",150, TextField.ANY );
        tb2 = new TextBox(" "," ", 150, TextField.ANY );

        //set up locations, simulate data and parser
        lp = new LocPosition(tb1);
        sd = new SimulateData(tb2);
        par = new Parser();

        //set command listeners for text boxes
        tb1.addCommand(cmd1);
        tb1.setCommandListener(this);
        tb2.addCommand(cmd1);
        tb2.setCommandListener(this);

        //sets up threads for location position and simulate data
        lpThr = new Thread(lp);
        sdThr = new Thread(sd);

        try {
            lpThr.start();
            sdThr.start();
        }
        catch ( Exception e2) {
            e2.printStackTrace();
        }

        disp.setCurrent(getList());
    }


    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    /* commandAction takes in a command a displayable and queues it in a list
     * It also sets up a way to display this list
     */
    public void commandAction(Command command, Displayable displayable) {
        if (command == cmd1) {
            disp.setCurrent(getList());
        }
        if (displayable == list) {
            if (command == List.SELECT_COMMAND) {
                listAction();
            }
        }
    }

    /*getCanvas sets up the canvas to have a menu in the lower right hand
     * corner that has the following options: zoom in/out, exit, find closest shuttle
     * stop, find closest shuttle
     */
    public SVGStaticCanvas getCanvas(int canv){
        //set up canvas as long as it is null
        if (svgCanvas == null){
            svgCanvas = new SVGStaticCanvas(true, lp, canv);
            //set up menu with exit, zoom in/out, getstop and getshuttle
            exit = new Command("Exit", Command.SCREEN, 0);
            zoomin = new Command("Zoom in", Command.SCREEN, 1);
            zoomout = new Command("Zoom out", Command.SCREEN, 1);
            getstop = new Command("Show Closest Shuttle Stop", Command.SCREEN, 1);
            getshuttle = new Command("Show Closest Shuttle", Command.SCREEN, 1);
            svgCanvas.addCommand(exit);
            svgCanvas.addCommand(zoomin);
            svgCanvas.addCommand(zoomout);
            svgCanvas.addCommand(getstop);
            svgCanvas.addCommand(getshuttle);
            svgCanvas.setCommandListener(new CommandListener() {

                /* commandAction listens for menu options once in the campus map, such as
                * zoom in, zoom out, find closest shuttle stop and find closest shuttle.
                */
                public void commandAction(Command c, Displayable d) {
                    if(c == exit) {
                        canvThr = null;
                        svgCanvas = null;
                        disp.setCurrent( getList());
                    }
                    else if(c == zoomin) {
                            svgCanvas.zoomIn();
                        }
                    else if(c== zoomout) {
                                svgCanvas.zoomOut();
                    }
                    else if(c== getstop){
                    }
                    else if(c== getshuttle){
                    }
                }
            });
        }
        return svgCanvas;
    }

    /*
     * getList is responsible for creating a new list of all available commands for user
     * Will include See Campus, Get Nearest Bus Stop, Exit, Find Nearest Bus, etc.
    */
    public List getList() {
        if (list == null) {
            // sets up list for getting location, different maps, data and exit
            list = new List("list", Choice.IMPLICIT);
            list.append("Get Location",null);
            list.append("See Campus",null);
            list.append("See Shuttle Route East",null);
            list.append("See Shuttle Route West",null);
            list.append("Simulate Data", null);
            list.append("Get Data", null);
            list.append("Exit",null);
            list.setCommandListener(this);
        }
        return list;
    }

    /*listAction takes in the menu options set up in getList and reacts to them
     * if selected, displays correct map, reacts to get and simulate data
     */
    void listAction(){
        String __selectedString = getList().getString(getList().getSelectedIndex());
        int route = 0;      //used to determine what route is selected
        if (__selectedString != null) {
            if (__selectedString.equals("Get Location")) {
                disp.setCurrent(tb1);
            }
            else if (__selectedString.equals("See Campus")) {
                route = 3;      //case for empty map
            }
            else if (__selectedString.equals("See Shuttle Route East")) {
                route = 1;      //case for East Shuttle Route Highlighted
            }
            else if (__selectedString.equals("See Shuttle Route West")) {
                route = 2;      //case for West Shuttle Route Highlighted
            }
            else if (__selectedString.equals("Simulate Data")) {
                disp.setCurrent(tb2);
            }
            else if (__selectedString.equals("Get Data")) {
                par.go();      //case for Getting Data
            }
                
            else if (__selectedString.equals("Exit")) {
                notifyDestroyed(); //moves to the main screen
            }
        }
        // if there is a change in route from when it was created, display new canvas
        if (route != 0){
            svgCanvas = null;
            svgCanvas = getCanvas(route);
            canvThr = new Thread(svgCanvas);
            canvThr.start();
            disp.setCurrent(svgCanvas);
        }
    }
}