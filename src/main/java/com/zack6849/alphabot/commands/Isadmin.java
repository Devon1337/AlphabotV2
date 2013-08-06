/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zack6849.alphabot.commands;

import com.zack6849.alphabot.api.Command;
import com.zack6849.alphabot.api.Config;
import com.zack6849.alphabot.api.PermissionManager;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Zack
 */
public class Isadmin extends Command
{

    private Config config;
    private PermissionManager manager;
    @Override
    public void setConfig(Config conf)
    {
        this.config = conf;
    }
       @Override
    public void setManager(PermissionManager man){
        this.manager = man;
    }


    @Override
    public void run(MessageEvent event)
    {
        PircBotX bot = event.getBot();
        if (config.isAdmin(event.getUser().getNick(), event.getUser().getHostmask()))
        {
            bot.sendMessage(event.getChannel(), "You're an admin!");
        } else
        {
            bot.sendMessage(event.getChannel(), "I sry, no admin 4 u.");
        }
    }

    @Override
    public String getDescription()
    {
        return "Checks if a user is an administrator";
    }

    @Override
    public String getSyntax()
    {
        return config.getTrigger() + "isadmin";
    }

    @Override
    public String getName()
    {
        return "IsAdmin";
    }

    @Override
    public boolean isSecret()
    {
        return false;
    }
}
