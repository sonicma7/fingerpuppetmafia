

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



public class SVGSimpleMidlet extends MIDlet implements CommandListener {
    SVGStaticCanvas svgCanvas = null;
    Command exit,zoomin,zoomout,cmd1,getstop,getshuttle;
    LocPosition lp;
    TextBox tb1;
    Display disp;
    Thread canvThr,lpThr = null;
    List list= null;
    public void startApp() {
        disp  = Display.getDisplay(this);
        //svgCanvas = getCanvas();
         cmd1 = new Command("Back",Command.BACK, 0);
    tb1 = new  TextBox(" "," ",150, TextField.ANY );

     lp = new  LocPosition(tb1);

    tb1.addCommand(cmd1);
    tb1.setCommandListener(this);


    lpThr = new Thread(lp);

    try {
        lpThr.start();
    }  catch ( Exception e2) {
        e2.printStackTrace();
    }
   // canvThr = new Thread(svgCanvas);
  // canvThr.start();
        disp.setCurrent(getList());

        //disp.setCurrent(svgCanvas);

    
    
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

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

      public SVGStaticCanvas getCanvas(int canv)
      {
         if (svgCanvas == null){
            svgCanvas = new SVGStaticCanvas(true, lp, canv);
        
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

 /* CommandAction listens for menu options once in the campus map, such as
  * zoom in, zoom out, find closest shuttle stop and find closest shuttle.  
  */
                public void commandAction(Command c, Displayable d) {
                  if(c == exit) {
                      canvThr = null;
                      svgCanvas = null;

                     disp.setCurrent( getList());
                  }  else {
                      if(c == zoomin) {
                          svgCanvas.zoomin();
                      } else {
                          if(c== zoomout) {
                         svgCanvas.zoomout();
                          } else {
                              if(c== getstop){

                              } else {
                                  if(c== getshuttle){

                                  }

                                }

                            }
                          }
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
            // write pre-init user code here
            list = new List("list", Choice.IMPLICIT);
            list.append("Get Location",null);
            list.append("See Campus",null);
            list.append("See Shuttle Route East",null);
            list.append("See Shuttle Route West",null);
            list.append("Exit",null);
            list.setCommandListener(this);
        }
        return list;
    }
       void listAction()
       {String __selectedString = getList().getString(getList().getSelectedIndex());
        int route = 0;      //used to determine what route is selected
        if (__selectedString != null) {
            if (__selectedString.equals("Get Location")) {
               disp.setCurrent(tb1); 
                //switchDisplayable(null, getForm());
                // write post-action user code here
            } else if (__selectedString.equals("See Campus")) {
                route = 3;      //case for empty map
            }
            else if (__selectedString.equals("See Shuttle Route East")) {
                route = 1;      //case for East Shuttle Route Highlighted
            }
            else if (__selectedString.equals("See Shuttle Route West")) {
                route = 2;      //case for West Shuttle Route Highlighted
            }
            
                
             else if (__selectedString.equals("Exit")) {
               notifyDestroyed(); //moves to the main screen
               
            }
        }
        /* if there is a change in route from when it was created, display new canvas */
        if (route != 0){
            svgCanvas = null;
            svgCanvas = getCanvas(route);
            canvThr = new Thread(svgCanvas);
            canvThr.start();
            disp.setCurrent(svgCanvas);
        }
       }
}


