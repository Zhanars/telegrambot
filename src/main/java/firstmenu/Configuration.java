package firstmenu;

public class Configuration {
    static String UniverHost = "jdbc:sqlserver://185.97.115.134\\MSSQLSERVER;database=atu_univer";
    static String Pass = "Desant3205363";
    static String UniverUsername = "sa";
    static String MySQLHost = "jdbc:mysql://srv-pleskdb16.ps.kz:3306/atukz1_netfox";
    static String MySQLUsername = "atukz_ruslan";
    static String TelegramBotHost = "jdbc:sqlserver://185.97.115.131\\RUSGUARD:49181;database=telegrambot";
    static String RusGuardHost = "jdbc:sqlserver://185.97.115.131\\RUSGUARD:49181;database=RusGuardDB";
    static String BotName = "Atudemo_bot";
    static String Bottoken = "745779362:AAEFky83gaEOP4aB8RjUHq4_BcW7hCYSB68";
    static String GmailLogin= "atu.demo.bot@gmail.com";
    static String TelegramUsername = "joker";
    static String UMKDUsername = "Администратор";
    static String UMKDPass = "Univer2018#";
    static String UMKDurl = "smb://185.97.115.134/umkd/";

    public static String getRusGuardHost() {
        return RusGuardHost;
    }

    public static void setRusGuardHost(String rusGuardHost) {
        RusGuardHost = rusGuardHost;
    }

    public static String getUMKDUsername() {
        return UMKDUsername;
    }

    public static void setUMKDUsername(String UMKDUsername) {
        Configuration.UMKDUsername = UMKDUsername;
    }

    public static String getUMKDPass() {
        return UMKDPass;
    }

    public static void setUMKDPass(String UMKDPass) {
        Configuration.UMKDPass = UMKDPass;
    }

    public static String getUMKDurl() {
        return UMKDurl;
    }

    public static void setUMKDurl(String UMKDurl) {
        Configuration.UMKDurl = UMKDurl;
    }

    public static String getTelegramUsername() {
        return TelegramUsername;
    }

    public static void setTelegramUsername(String telegramUsername) {
        TelegramUsername = telegramUsername;
    }

    public static String getMySQLHost() {
        return MySQLHost;
    }

    public static void setMySQLHost(String mySQLHost) {
        MySQLHost = mySQLHost;
    }

    public static String getMySQLUsername() {
        return MySQLUsername;
    }

    public static void setMySQLUsername(String mySQLUsername) {
        MySQLUsername = mySQLUsername;
    }

    public static String getGmailLogin() {
        return GmailLogin;
    }

    public static void setGmailLogin(String gmailLogin) {
        GmailLogin = gmailLogin;
    }

    public static String getPass() {
        return Pass;
    }

    public static void setPass(String pass) {
        Pass = pass;
    }

    public static String getTelegramBotHost() {
        return TelegramBotHost;
    }

    public static void setTelegramBotHost(String telegramBotHost) {
        TelegramBotHost = telegramBotHost;
    }

    public static String getBottoken() {
        return Bottoken;
    }

    public static void setBottoken(String bottoken) {
        Bottoken = bottoken;
    }

    public static String getBotName() {
        return BotName;
    }

    public static void setBotName(String botName) {
        BotName = botName;
    }
    public static String getUniverHost() {
        return UniverHost;
    }

    public void setUniverHost(String univerHost) {
        UniverHost = univerHost;
    }


    public static String getUniverUsername() {
        return UniverUsername;
    }

    public void setUniverUsername(String univerUsername) {
        UniverUsername  = univerUsername;
    }




}
