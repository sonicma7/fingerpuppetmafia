

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


/**
 * @author jayavenu
 */
public class SVGSimpleMidlet extends MIDlet implements CommandListener {
    SVGStaticCanvas svgCanvas = null;
    Command exit,zoomin,zoomout,cmd1,eastroute,westroute;
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
        // write pre-action user code here
        if (command == cmd1) {
            disp.setCurrent(getList());
        }
        if (displayable == list) {
            if (command == List.SELECT_COMMAND) {
                // write pre-action user code here
                listAction();
                // write post-action user code here
            }
        }
        // write post-action user code here
    }

    /*getCanvas sets up the canvas to have a menu in the lower right hand
     * corner that has the following options: zoom in/out, exit, display shuttle
     * route east/west 
     */

      public SVGStaticCanvas getCanvas(int canv)
      {
         if (svgCanvas == null){
             svgCanvas = new SVGStaticCanvas(true, lp, canv);
        
        exit = new Command("Exit", Command.SCREEN, 0);
        zoomin = new Command("Zoom in", Command.SCREEN, 1);
        zoomout = new Command("Zoom out", Command.SCREEN, 1);
        eastroute = new Command("Show East Shuttle Route", Command.SCREEN, 1);
        westroute = new Command("Show West Shuttle Route", Command.SCREEN, 1);
        svgCanvas.addCommand(exit);
        svgCanvas.addCommand(zoomin);
        svgCanvas.addCommand(zoomout);
        svgCanvas.addCommand(eastroute);
        svgCanvas.addCommand(westroute);
        svgCanvas.setCommandListener(new CommandListener() {

 /* CommandAction listens for menu options once in the campus map, such as
  * zoom in, zoom out, exit, etc.  As of right now it also listens for displaying
  * the shuttle routes (east/west) that are available
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
                      if(c== eastroute){
                          svgCanvas = null;
                          getCanvas(1);
                          canvThr = new Thread(svgCanvas);
                          canvThr.start();
                          disp.setCurrent(svgCanvas);
                      } else {
                      if(c== westroute){
                          svgCanvas = null;
                          getCanvas(2);
                          canvThr = new Thread(svgCanvas);
                          canvThr.start();
                          disp.setCurrent(svgCanvas);

                      }

                    }

                    }
                  }
                }
         }          }
        );

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
            list.append("Exit",null);
            list.setCommandListener(this);
            // write post-init user code here
        }
        return list;
    }
       void listAction()
       {String __selectedString = getList().getString(getList().getSelectedIndex());
        if (__selectedString != null) {
            if (__selectedString.equals("Get Location")) {
               disp.setCurrent(tb1); // write pre-action user code here
                //switchDisplayable(null, getForm());
                // write post-action user code here
            } else if (__selectedString.equals("See Campus")) {
                if(svgCanvas == null) {
                    svgCanvas = getCanvas(3);
                    canvThr = new Thread(svgCanvas);
                    canvThr.start();
                }
                disp.setCurrent(svgCanvas);// write pre-action user code here

                // write post-action user code here
            } else if (__selectedString.equals("Exit")) {
               notifyDestroyed(); // write pre-action user code here

                // write post-action user code here
            }
        }
       }
}


