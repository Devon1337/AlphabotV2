package com.zack6849.alphabot.commands;

import com.zack6849.alphabot.api.Command;
import com.zack6849.alphabot.api.Config;
import com.zack6849.alphabot.api.PermissionManager;
import org.pircbotx.hooks.events.MessageEvent;

public class Help extends Command {

    private PermissionManager manager;
    private Config config;

    public Help() {
        super("Help", "List command names and how to use them.", "help or help <command>");
    }

    @Override
    public void execute(MessageEvent event) {
        String[] args = event.getMessage().split(" ");
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public void setManager(PermissionManager manager) {
        this.manager = manager;
    }
}
