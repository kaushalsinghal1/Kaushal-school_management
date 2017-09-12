package com.banti.framework.platform;

import com.banti.framework.core.Account;


/**
 * 
 */
public interface LogoutListener {

    public void logoutSucceeded(Account account);
    public void logoutFailed();
    
}
