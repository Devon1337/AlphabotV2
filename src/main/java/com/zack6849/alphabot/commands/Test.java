/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zack6849.alphabot.commands;

import com.zack6849.alphabot.api.Command;
import com.zack6849.alphabot.api.Config;
import com.zack6849.alphabot.api.PermissionManager;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Zack
 */
public class Test extends Command
{

    private Config config;
    private PermissionManager manager;

    @Override
    public void setConfig(Config conf)
    {
        this.config = conf;
    }

    @Override
    public void setManager(PermissionManager man)
    {
        this.manager = man;
    }

    @Override
    public void run(MessageEvent event)
    {
        
    }

    @Override
    public boolean isSecret()
    {
        return false;
    }
}