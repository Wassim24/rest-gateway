package utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Singleton responsable des opérations sur le FTP.
 * @author wassim
 */
public class Client {

    /**
     * FTPClient pour se conencter et effectuer des opérations sur le servuer.
     */
    private FTPClient client;

    /**
     * Permet de savoir si un utilisateur est connecté et authentifié.
     */
    private boolean logged = false;

    /**
     * L'instance de la classe.
     */
    private static Client clientInstance = new Client();

    /**
     * Constructeur pour l'initialisation du FTPClient.
     */
    private Client() {
            this.client = new FTPClient();
    }

    /**
     * Permet de retourner une instance de la classe.
     * @return Instance de Client.
     */
    public static Client getInstance() {

        if (clientInstance == null)
                clientInstance = new Client();

        return clientInstance;
    }

    /**
     * Méthode permettant l'identification sur le FTP.
     * @param user Le nom d'utilisateur du FTP.
     * @param pass Le mot de passe.
     * @param host L'hôte du FTP.
     * @param port Le port sur lequel ouvrir la communication avec le FTP.
     * @return Le login form si on est pas identifié sur le FTP, sinon la liste de fichier.
     */
    public boolean login(String user, String pass, String host, String port)
    {
        try {

            this.client.connect(host, Integer.valueOf(port));
            logged = this.client.login(user, pass);
            return true;

        }
        catch (IOException e) {e.printStackTrace();}

        return false;
    }

    /**
     * Permet de lancer la commande LIST sur le serveur FTP
     * @return La liste des fichier, null sinon.
     */
    public FTPFile[] getListFiles()
    {
        try {return this.client.listFiles();}
        catch (IOException e) {e.printStackTrace();}

        return null;
    }

    /**
     * Permet de lancer la commande CWD sur le serveur FTP
     * @param directory Le repertoire au quel on veut accéder.
     */
    public void changeWorkingDirectory(String directory)
    {
        try {this.client.changeWorkingDirectory(directory);}
        catch (IOException e) {e.printStackTrace();}
    }

    /**
     * Permet de retourner le repertoire courant.
     * @return Le repertoire courant.
     */
    public String getCurrentWorkingDirectory()
    {
        try {return this.client.printWorkingDirectory();}
        catch (IOException e) {e.printStackTrace();}

        return System.getProperty("user.home");
    }

    /**
     * Permet de retourner le parent du repertorie courant.
     * @return le parent du repertorie courant.
     */
    public boolean getParentDirectory()
    {
        try {return this.client.changeToParentDirectory();}
        catch (IOException e) { e.printStackTrace(); }

        return false;
    }

    /**
     * Permet de lancer la commande RETR sur le serveur FTP.
     * @param filename le nom du fichier a télécharger.
     * @return InputStream si tout se passe bien, null sinon.
     */
    public InputStream getFile(String filename)
    {
        try {return this.client.retrieveFileStream(filename);}
        catch (IOException e) {e.printStackTrace();}

        return null;
    }

    /**
     * Permet de lancer la commande STOR sur le serveur FTP.
     * @param remote Le repertoire où stocker le fichier.
     * @param local Le fichier a stocker.
     * @return true si tout se passe bien, false sinon.
     */
    public boolean saveFile(String remote, InputStream local)
    {
        try { return this.client.storeFile(remote, local); }
        catch (IOException e) { e.printStackTrace(); }

        return false;
    }

    /**
     * Permet de lancer la commande DELE sur le serveur FTP.
     * @param filePath Le nom du fichier / repertoire a supprimer.
     * @return true si tout se passe bien, false sinon.
     */
    public boolean deleteFile(String filePath)
    {
        try { return this.client.deleteFile(filePath); }
        catch (IOException e) { e.printStackTrace(); }

        return false;
    }

    /**
     * Permet de savoir si le client et conencté et authentifié sur le serveur FTP.
     * @return true si tout se passe bien, false sinon.
     */
    public boolean getConnectionStatus()
    {
        return this.client.isConnected() && logged;
    }

}
