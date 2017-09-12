package com.banti.framework.platform;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import com.banti.framework.core.Account;
import com.banti.framework.core.InitializationFailedException;
import com.banti.framework.logging.LoggerFactory;
import com.banti.framework.platform.module.DefaultModuleSchemeFactory;
import com.banti.framework.platform.peer.SplashScreen;
import com.banti.framework.theme.ToolKit;

/**
 * <p>
 * This is an abstract class which your Java application can extend for having Menu Bar, Tool Bar, 
 * Status Bar, Window Bar and DeskTop Area. 
 * Application provides all of core functionalities for your Client-side application.
 * <br>
 * <p>This is a main, core and important class when you construct a new application on the platform.
 * See <a href="../../../../../index.html" target="_blank">Tutorial</a> carefully.
 *  
 */
public abstract class Application extends JFrame implements WindowFocusListener {
    private static java.util.logging.Logger DEBUG = LoggerFactory.getInstance().getLogger(
        "APPLICATION");
    private static java.util.logging.Logger EVENT = LoggerFactory.getInstance().getEventLogger();

    public static Application Frame;

    protected  static RuntimeEngine Engine;
    protected static LoginManager Login;
    public static Account  account;

    /**
     * Class variable of ViewMediator. The sub class can use it by just "View". 
     */
    protected static ViewMediator View;

    /**
     * Used for warning messages.
     */
    public static final int WARNING = JOptionPane.WARNING_MESSAGE;

    /**
     * Used for error messages.
     */
    public static final int ERROR = JOptionPane.ERROR_MESSAGE;

    /**
     * Used for information messages.
     */
    public static final int INFO = JOptionPane.INFORMATION_MESSAGE;

    /**
     * Used for questions.
     */
    public static final int QUESTION = JOptionPane.QUESTION_MESSAGE;

    /**
     *  No icon is used.
     */
    public static final int PLAIN = JOptionPane.PLAIN_MESSAGE;

    /**
     * Return value from class method if YES is chosen.
     */
    public static final int YES = JOptionPane.YES_OPTION;

    /**
     * Return value from class method if NO is chosen.
     */
    public static final int NO = JOptionPane.NO_OPTION;

    /**
     * String index of  help short-cut
     */
    public static String CURRENT_HELP_ID;

    private static final String LOCATION_X = "X";
    private static final String LOCATION_Y = "Y";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";

    private ModuleSchemeFactory factory;
    private boolean logo = false;

    /**
     *  A default constructor. No title is given when you instantiate your conclete class
     * by this constructor.
     *
     */
    public Application() {
        super();
        super.addWindowFocusListener(this);
        Engine = new RuntimeEngine();
        Login = null;
        Frame = this;
    }

    /**
     * Application is instantiated and super Frame is created 
     * in the specified GraphicsConfiguration of a screen device and a blank title.
     * 
     * @param gc The GraphicsConfiguration that is used to construct the new Frame; 
     * if gc is null, the system default GraphicsConfiguration is assumed.
     */
    public Application(GraphicsConfiguration gc) {
        super(gc);
        super.addWindowFocusListener(this);
        Engine = new RuntimeEngine();
        Login = null;
        Frame = this;
    }

    /**
     * Creates a Frame with the specified title. 
     * The constructor initialize super Frame and Application own initializer. 
     * 
     * @param title The title for the frame.
     */
    public Application(String title) {
        super(title);
        super.addWindowFocusListener(this);
        Engine = new RuntimeEngine();
        Login = null;
        Frame = this;
    }

    /**
     * Creates a Frame with the specified title in the specified GraphicsConfiguration of a screen device.
     * Application itself is instantiated and initialized at the same time.
     * 
     * @param title The title for the frame.
     * @param gc The GraphicsConfiguration that is used to construct the new Frame; 
     * if gc is null, the system default GraphicsConfiguration is assumed.
     */
    public Application(String title, GraphicsConfiguration gc) {
        super(title, gc);
        super.addWindowFocusListener(this);
        Engine = new RuntimeEngine();
        Login = null;
        Frame = this;
    }


    /**
     * Shows a message on the status bar.
     * 
     * @param message a displayed message.
     */
    public void showStatusMessage(String message) {
        if (View != null && message != null) {
            View.setStatusMessage(message);
        }
    }

    /**
     * Brings up a message dialog, its title and the type of contents are specfied by type value.
     * 
     * @param message the message to display.
     * @param type there are three type;
     * <ul>
     * <li>INFO: information message dialog titled "Information".
     * <li>WARNING: warning dialog titled "Warning".
     * <li>ERROR: error dialog titled "Error".
     * </ul>
     */
    public final void showMessageDialog(String message, int type) {
        String title = "";

        switch (type) {
            case INFO:
                title = ToolKit.getString("INFORMATION");
                break;
            case WARNING:
                title = ToolKit.getString("WARNING");
                break;
            case ERROR:
                title = ToolKit.getString("ERROR");
                break;
            default:
                title = "";
        }

        JOptionPane.showMessageDialog(this, message, title, type);
    }

    /**
     * Brings up a confirmation dialog with Yes and No button titled "Confirmation".
     * 
     * @param message a message to display.
     * @return an integer indicating the option selected by the user, YES or NO.
     */
    public final int showConfirmDialog(String message) {
        String title = ToolKit.getString("CONFIRMATION");
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }

    /**
     * Shows a splash screen during the specified <i>timer</i> interval. The contents of the screen is given
     * by mainImage and optionPanel arguments. statusMsg is displayed on the status bar after the screen
     * disappears after timer interval.<br>
     * <br>
     * ImageIcon of mainImage is deployed on the center of the screen. JPanel of optionPanel is put under
     * mainImage. No contents is put if optionPanel is null.  You can create the contents of JPanel freely. 
     * 
     * @param mainImage Image contents on the splash screen
     * @param optionPanel JPanel on the SOUTH of the splash screen. 
     * @param timer int (sec), after the specfied seconds, the splash screen disappers.
     * @param statusMsg the message is displayed on status bar of your application after the splash screen is closed.
     */
    public final void showSplashScreen(
        ImageIcon mainImage,
        JPanel optionPanel,
        int timer,
        String statusMsg) {
        final ImageIcon main;
        final JPanel option;
        final int time;
        final String message;
        final JFrame frame;

        main = mainImage;
        option = optionPanel;
        time = timer;
        message = statusMsg;
        frame = this;

        Thread th = new Thread(new Runnable() {
            public void run() {
                try {
                    SplashScreen splash = new SplashScreen(frame, main, option, false);
                    splash.show();
                    Thread.sleep(time * 1000);
                    splash.hideSplashScreen();
                    splash = null;
                    if (message != null) {
                        if (View != null)
                            View.setStatusMessage(message);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        th.start();
        return;
    }

    /**
     * Returns JDialog as Modal mode titled the specified title.
     * Its parent component is Application itself.
     * 
     * @param title a string of title.
     * @return JDialog instance.
     */
    public final JDialog createModalDialog(String title) {
        return new JDialog(this, title, true);
    }

    /**
     * Returns JDialog as not Modal mode titled the specified title.
     * Its parent component is Application itself.
     * 
     * @param title a title string.
     * @return JDialog instance.
     */
    public final JDialog createDialog(String title) {
        return new JDialog(this, title, false);
    }

    public final void repaint() {
        View.setToolBarRollOver();
        View.stopIndeterminatedProgress();
        View.repaint();
        super.repaint();
    }

    public final void windowGainedFocus(WindowEvent we) {
        if (we == null)
            return;
        if (View != null) {
            if (View.getCurrentDeskTopPane() == null)
                return;
            if (View.getCurrentDeskTopPane().getSelectedWindow() == null) {
                View.changeStatus(Color.BLACK);
            } else {
                View.changeStatus(Color.GREEN);
            }
        }
    }

    public final void windowLostFocus(WindowEvent we) {
        if (we == null)
            return;
        if (View != null) {
            View.changeStatus(Color.RED);
        }
    }

    // --------------- protected methods for the extended classes --------------------------

    /**
     * Enables/Disables a logo button on the tool bar.
     * It is necessary to set the boolean before setup method.
     * 
     * @param mode true or false, a log is deployed if it is true.
     */
    protected void setLogoVisibled(boolean aFlag) {
        logo = aFlag;
    }

    /**
     * Initializes all of components inside of Apllication. <b>Before calling "setup()"</b>, it is necessary 
     * <a href="RuntimeRegistry.html"> to register Plug-in Module through RuntimeRegistry</a>, 
     * <a href="Application.html#setLoginManager(com.cysols.framework.platform.LoginManager)">to set LoginManager</a>,
     * <a href="Application.html#setModuleSchemeFactory(com.cysols.framework.platform.ModuleSchemeFactory)"> to set ModuleSchemeFactory</a>
     * and <a href="ViewMediator.html#setClassicStyleWinLookAndFeel(boolean)">to set the classic style Look & Feel for Windows Styel</a>. 
     * <br>
     * <p> On the other hand, 
     * <a href="ViewMediator.html#setDragMode(boolean)"> to set Drag Mode</a>,
     * <a href="ViewMediator.html#setExcitConfirmationMode(boolean)">  to set Exit Confirmation Mode</a>,
     * <a href="ViewMediator.html#setExitConfirmMessage(java.lang.String)"> to set Exit Confrimation Message</a>
     * and <a href="ViewMediator.html#setToolBarRollOver(boolean)"> to set ToolBar Roll Over available or not</a>
     * must not be called before "setup()" method.
     *  
     * @exception InitializationFailedException is thrown if your implementation or configuration are improper. <br>
     * This is not RuntimeException. It is desirable to use this exception while the development is ongoing.
     */
    protected final void setup() throws InitializationFailedException {

        if (factory == null)
            factory = new DefaultModuleSchemeFactory();

        View = new ViewMediator(this, factory, logo);
        View.deploy();
        super.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        super.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent we) {
                ViewMediator.DefaultCommand.Exit exit = ViewMediator.CommandBuilder.getExitCommand();
                exit.entryPoint(new ActionEvent(we.getSource(), we.getID(), "EIXT"));
            }

            public void windowClosed(java.awt.event.WindowEvent we) {
                saveScreenSize();
                System.exit(0);
            }
        });

        this.loadScreenSize();
        // changed to protected method by daigo
        //this.loadPreference();
    }

    protected final void setModuleSchemeFactory(ModuleSchemeFactory factory) {
        this.factory = factory;
    }

    protected final RuntimeRegistry getRuntimeRegistry() {
        return Engine;
    }

    /**
     * Sets <a href="LoginManager.html"><i>LoginManager</i></a> concrete instance to Application. 
     * <a href="LocalLoginManager.htm"><i>LocalLoginManager</i></a> and 
     * <a href="RemoteLoginManager.html"><i>RemoteLoginManager</i></a> are
     * provided by Platform. Login/Logout functions are available by calling this method and
     * setting either <i>LocalLoginManager</i> or <i>RemoteLoginManager</i>. In addition to them,
     * <a href="TabbedRemoteLoginManager.html"><i>TabbedRemoteLoginManager</i></a> is
     * prepared, which is used for tabbed pane based Server witching like NetSkate Visualizer.
     * 
     * @param loginManager LoginManager concrete instance.
     */
    protected final void setLoginManager(LoginManager loginManager) {
        Login = loginManager;
    }

    /**
     * Displays LoginDialog if a concrete <i>LoginManager</i> instance is set to your application
     * by <a href="Application.html#setLoginManager(com.cysols.framework.platform.LoginManager)">setLoginManager(LoginManager)</a> method.
     *
     */
    protected final void showLoginDialog() {
        if (Login != null) {
            Login.login(this);
        }
    }

    protected final void logOutApp() {
        if (Login != null) {
            Login.logout(this);
        }
    }
    protected void loadPreference() {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String defaultLAF = "javax.swing.plaf.metal.MetalLookAndFeel";

        String laf = prefs.get("laf", defaultLAF);
        try {
            if (laf.equals(defaultLAF)) {
                String theme = prefs.get("theme", null);
                if (theme != null) {
                    Class<?> c = Class.forName(theme);
                    MetalTheme mTheme = (MetalTheme) c.newInstance();
                    MetalLookAndFeel.setCurrentTheme(mTheme);
                    SwingUtilities.updateComponentTreeUI(this);
                }
            }

            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);

        } catch (Exception e) {
            // ignore
        }
    }

    public static boolean isActionPermitted(String action) {
        return isActionPermitted(action, true);
    }

    public static boolean isActionPermitted(String action, boolean showDialog) {
        /*if (Session == null) {
            return false;
        }
        Account account = Session.getActiveAccount();
        if (account == null) {
            return false;
        }*/
    	if(account==null){
    		return false;
    	}
        boolean allowed = account.isActionPermitted(action);
        if (!allowed && showDialog && Frame != null) {
            Frame.showMessageDialog(ToolKit.getString("ACCESS_DENIED"), WARNING);
        }
        if (!allowed) {
            DEBUG.warning("Operation: " + action + " is not permitted for " + account.getName());
            EVENT.warning("Operation: " + action + " is not permitted for " + account.getName());
        }
        return allowed;
    }

    // ------------------ non-public methods ---------------------------------------------------------
    private void loadScreenSize() {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        // Get size
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        int locationX = prefs.getInt(LOCATION_X, 25);
        int locationY = prefs.getInt(LOCATION_Y, 15);
        int width = prefs.getInt(WIDTH, size.width - 50);
        int height = prefs.getInt(HEIGHT, size.height - 55);

        super.setBounds(locationX, locationY, width, height);
    }

    private void saveScreenSize() {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        Rectangle rect = getBounds();
        prefs.putInt(LOCATION_X, rect.x);
        prefs.putInt(LOCATION_Y, rect.y);
        prefs.putInt(WIDTH, rect.width);
        prefs.putInt(HEIGHT, rect.height);
    }

}
