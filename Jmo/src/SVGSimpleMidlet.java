

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
    Command exit,zoomin,zoomout,cmd1,getstopeast,getstopwest,getshuttle,getinfo;
    LocPosition lp1,lp2;
    SimulateData sd;
    Parser par;
    TextBox tb1;
    TextBox tb2;
    TextBox tb3;
    Display disp;
    Data data = new Data();
    Thread canvThr,lpThr1 = null, lpThr2 = null, sdThr = null, parThr = null;
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
        tb3 = new TextBox(" "," ", 150, TextField.ANY );
        tb1.setTitle("East");
        tb3.setTitle("West");

        //set up locations, simulate data and parser
        lp1 = new LocPosition(tb1);
        lp2 = new LocPosition(tb3);
        sd = new SimulateData(tb2);


        //set command listeners for text boxes
        tb1.addCommand(cmd1);
        tb1.setCommandListener(this);
        tb2.addCommand(cmd1);
        tb2.setCommandListener(this);
        tb3.addCommand(cmd1);
        tb3.setCommandListener(this);

        //sets up threads for location position and simulate data
        lpThr1= new Thread(lp1);
        lpThr2= new Thread(lp2);
        sdThr = new Thread(sd);




        try {
            lpThr1.start();
            lpThr2.start();
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
            svgCanvas = new SVGStaticCanvas(true, lp1, canv);
            //set up menu with exit, zoom in/out, getstop and getshuttle
            exit = new Command("Exit", Command.SCREEN, 0);
            //zoomin = new Command("Zoom in", Command.SCREEN, 1);
            //zoomout = new Command("Zoom out", Command.SCREEN, 1);
            getstopeast = new Command("Closest East Stop", Command.SCREEN, 1);
            getstopwest = new Command("Closest West Stop", Command.SCREEN, 1);
            getshuttle = new Command("Closest Shuttle", Command.SCREEN, 1);
            getinfo = new Command("Get Stop Info", Command.SCREEN, 1);
            svgCanvas.addCommand(exit);
            //svgCanvas.addCommand(zoomin);
            //svgCanvas.addCommand(zoomout);
            svgCanvas.addCommand(getstopeast);
            svgCanvas.addCommand(getstopwest);
            svgCanvas.addCommand(getshuttle);
            svgCanvas.addCommand(getinfo);
            svgCanvas.setCommandListener(new CommandListener() {

                /* commandAction listens for menu options once in the campus map, such as
                * zoom in, zoom out, find closest shuttle stop and find closest shuttle.
                */
                public void commandAction(Command c, Displayable d) {
                    if(c == exit) {

                        /*svgCanvas = null;
                        notifyDestroyed();*/
                        canvThr = new Thread(svgCanvas);
                        canvThr.start();
                        disp.setCurrent( getList());
                    }
                    /*else if(c == zoomin) {
                            svgCanvas.zoomIn();
                        }
                    else if(c== zoomout) {
                                svgCanvas.zoomOut();
                    }*/
                    else if(c== getstopeast){
                        svgCanvas.showStop("East");
                    }
                    else if(c== getstopwest){
                        svgCanvas.showStop("West");
                    }
                    else if(c== getshuttle){
                        svgCanvas.showBus();
                    }
                    else if(c== getinfo){
                        //canvThr = null;
                        //svgCanvas = null;
                        disp.setCurrent( tb1);
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
            list.append("Closest East Shuttle Information",null);
            list.append("Closest West Shuttle Information",null);
            list.append("See Campus",null);
            list.append("See Shuttle Route East",null);
            list.append("See Shuttle Route West",null);
            list.append("Simulate Data", null);
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
            if (__selectedString.equals("Closest East Shuttle Information")) {
                disp.setCurrent(tb1);
            }
            else if (__selectedString.equals("Closest West Shuttle Information")) {
                disp.setCurrent(tb3);
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
                
            else if (__selectedString.equals("Exit")) {
                svgCanvas = null;
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