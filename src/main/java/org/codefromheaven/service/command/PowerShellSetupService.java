package org.codefromheaven.service.command;

import org.codefromheaven.dto.Command;
import org.codefromheaven.service.settings.SettingsService;

import java.io.IOException;

public class PowerShellSetupService implements Runnable {

    private static final String SEPARATOR = " ; ";

    private final Command command;

    public PowerShellSetupService(Command command) {
        this.command = command;
    }

    public void run() {
        runCommand(command.scriptPathVarName(), command.autoCloseConsole(), command.command());
    }

    private static void runCommand(String scriptPathVarName, boolean autoCloseConsole, String command) {
        try {
            String finalCommand = getOpenDirectoryCommand(scriptPathVarName) + SEPARATOR + command;

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(
                    "powershell.exe",
                    "Start-Process powershell.exe '" +
                            (!autoCloseConsole ? "-NoExit" : "")
                            + " \"[Console]::Title = ''Buenos Dias PowerShell run''; " + finalCommand + "\"'"
            );
            processBuilder.start();
        } catch (IOException e) {
            System.out.println(" --- Interruption in RunCommand: " + e);
            Thread.currentThread().interrupt();
        }
    }

    private static String getOpenDirectoryCommand(String scriptPathVarName) {
        return "cd \"" + SettingsService.loadValue(scriptPathVarName).get() + "\"";
    }

}
