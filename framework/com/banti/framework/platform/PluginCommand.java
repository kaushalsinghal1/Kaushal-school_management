package com.banti.framework.platform;

import java.awt.event.ActionEvent;

/**
 * This is a sub class of <a href="Command.html"><i>Command</i></a> for Plug-in mechanism.
 * PluginCommand can be plugged into "Tool" menu statically or dynamically. The role is the same
 * as <i>Command</i>. The extended points of Plug-in are as follows.
 * <ul>
 * <li> Activation: there is as "getActivator()" method that returns <a href="Activator.html"><i>Activator</i></a>
 * <i>Activator</i> works for activating that plugin command.
 * <li>Ordering: "getOrder()" method gives an integer so that <i>Application</i> lists all loaded
 * Plug-in in order based on the integer.
 * </ul>
 * 
 */
public abstract class PluginCommand extends Command {

    /* (non-Javadoc)
     * @see com.cysols.framework.platform.Command#entryPoint(java.awt.event.ActionEvent)
     */
    public abstract void entryPoint(ActionEvent ae);

    /**
     * Returns <i>Activator</i> concrete instance that depends on this PluginCommand
     * and avtivates it. 
     * Only when "<a href="Activator.html#isActivated()">Activator.isActivated()</a>" 
     * method returns true, the Plug-in gets enabled and can be called from the 
     * menu bar or the tool bar.
     * 
     * @return Activator of this command.
     */
    public abstract Activator getActivator();

    /**
     * This is used for ordering the menu items or the icon buttons by <i>Application</i>. 
     * Plug-in command/module will be provided by various ways such as the respective
     * Jar file of the other packages, some sub packages, etc. Then each Plug-in is listed 
     * in the tool bar based on the value of this method.
     * 
     * For example, Supposing that there are three plugin commands and two plugin 
     * modules, those implementations are shown as follows.
     * <ol>
     *    <li> Plugin command1: 10 is returnd by "getOrder()"
     *    <li> Plugin command2: 5 is returnd by "getOrder()"
     *    <li> Plugin command3: 20 is returnd by "getOrder()"
     * </ol>
     * <ol>
     *    <li> Plugin module1: 0 is returnd by "getOrder()"
     *    <li> Plugin module2: 1 is returnd by "getOrder()"
     * </ol>
     * When you register those Plug-in to your application, it shows the menu bar like;<br>
     * <table>
     *    <tr>
     *      <td bgcolor="#C0C0C0">View</td>
     *      <td bgcolor="#C0C0C0">Tool</td>
     *      <td bgcolor="#CCCCFF">Plugin module1 (the order is 0)</td>
     *      <td bgcolor="#CCCCFF">Plugin module2 (the order is 1)</td>
     *      <td bgcolor="#C0C0C0">Option</td>
     *    </tr>
     *    <tr>
     *      <td>&nbsp;</td>
     *      <td bgcolor="#FF99FF">Plugin command2 (the order is 5)</td>
     *    </tr>
     *    <tr>
     *      <td>&nbsp;</td>
     *      <td bgcolor="#FF99FF">Plugin command1 (the order is 10)</td>
     *    </tr>
     *    <tr>
     *      <td>&nbsp;</td>
     *      <td bgcolor="#FF99FF">Plugin command3 (the order is 20)</td>
     *    </tr>
     * </table>   <br> 
     * 
     * @return an integer, which means the order of being listed.
     */
    public abstract int getOrder();

    /**
     * A constructor, a name of the command is given only by this constructor.
     * 
     * @param name the name of the command, which is displayed as the menu item.
     */
    public PluginCommand(String name) {
        super(name);
    }

    public PluginCommand(String name, String cmdKey) {
        super(name, cmdKey);
    }

    public void accept(LicenseManager licenseManager) {
        licenseManager.visit(this);
    }

    // ---------- non-public method -----------------------------------------------
    /**
     * This is a method for Visitor pattern.
     */
    void accept(MenuBar bar) {
        bar.visit(this);
    }
}
