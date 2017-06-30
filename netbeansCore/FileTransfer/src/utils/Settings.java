/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class Settings {
    private static boolean showWarnings = true;
    private static boolean createLogs = true;
    

    public static boolean isShowWarnings() {
        return showWarnings;
    }

    public static void setShowWarnings(boolean showWarnings) {
        Settings.showWarnings = showWarnings;
    }

    public static boolean isCreateLogs() {
        return createLogs;
    }

    public static void setCreateLogs(boolean createLogs) {
        Settings.createLogs = createLogs;
    }
    
    
}
