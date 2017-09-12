package com.banti.framework.platform;

import com.banti.framework.core.Account;


/**
 * 
 */
public interface LoginListener {

    public void loginSucceeded(Account account);
    public void loginFailed();
}
