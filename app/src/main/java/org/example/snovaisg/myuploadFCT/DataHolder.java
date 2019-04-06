package org.example.snovaisg.myuploadFCT;

import org.json.JSONObject;

/**
 * Created by snovaisg on 1/22/18.
 */

public class DataHolder {
    private String data; //restaurante escolhido
    private JSONObject logins;
    private String ServerLoginInfo = "Login.json";
    private String LocalLoginInfo = "Login.json";
    private String ServerFilename = "maLucasNotification.json";
    private String fileToInternalWrite = "TESTEWrite.json";
    private String fileToInternal = "LocalMenu.json";
    private String PrecoInvalido = "9999";
    private String JsonNotifications = "notifications";
    private String PrecoInvalidoMensagem = "(não tem preco)";
    private boolean editar = false;
    private boolean denovo = false;
    private JSONObject menu;
    private JSONObject fullMenu;
    private JSONObject menuNotifications;

    public String getNotifications(){return JsonNotifications;}
    public void setFullMenu(JSONObject fullMenu){this.fullMenu = fullMenu;}
    public void setMenuNotifications(JSONObject notifications) {this.menuNotifications=notifications;}
    public JSONObject getFullMenu(){return fullMenu;}
    public void setMenu(JSONObject Menu){this.menu = Menu;}
    public JSONObject getMenu(){return menu;}
    public JSONObject getMenuNotifications(){return menuNotifications;}
    public void setEditar(boolean val){this.editar=val;}
    public boolean getEditar(){return editar;}
    public void setDenovo(boolean val){this.denovo=val;}
    public boolean getDenovo(){return denovo;}
    public void setLogin(JSONObject login){this.logins = login;}
    public JSONObject getLogins(){return logins;}
    public void setServerFilename(String name) {ServerFilename = name;}
    public String getServerFilename() {return ServerFilename;}
    public String ServerFilename() {return ServerFilename;}
    public String fileToInternalWrite() {return fileToInternalWrite;}
    public String fileToInternal() {return fileToInternal;}
    public String PrecoInvalido() {return PrecoInvalido;}
    public String PrecoInvalidoMensagem() {return PrecoInvalidoMensagem;}
    public String getLocalLoginInfo(){return LocalLoginInfo;}
    public String getServerLoginInfo() {return ServerLoginInfo;}
    public String getData() {return data;}
    public void setData(String data) {this.data = data;}


    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}