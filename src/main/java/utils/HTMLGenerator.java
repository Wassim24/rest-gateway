package utils;

import org.apache.commons.net.ftp.FTPFile;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A singleton used to generate HTML / CSS Pages.
 * @author wassim
 */
public class HTMLGenerator {

    /**
     * Permet de connaitre la racine.
     */
    String rootPath = "http://localhost:8080/rest/access";

    /**
     * Stocke le repertoire courant.
     */
    String cwd = rootPath+"/directory/"+System.getProperty("user.home");

    /**
     * L'instance du générateur de HTML.
     */
    private static HTMLGenerator htmlGenerator = new HTMLGenerator();

    /**
     * Permet de retourner une instance de HTMLGenerator.
     * @return Instance de HTMLGenerator.
     */
    public static HTMLGenerator getInstance() {

        if (htmlGenerator == null)
            htmlGenerator = new HTMLGenerator();

        return htmlGenerator;
    }

    /**
     * Constructeur de la classe HTMLGenerator
     */
    private HTMLGenerator() {}

    /**
     * Méthode responsable de la génération du HTML / CSS pour le listing d'une directory
     * @param filesList La list des fichier d'un repertoire
     * @param cwd Paramètre contenant le répertoire courant
     * @return filesListHtml Le HTML généré
     */
    public String generateFilesList(FTPFile[] filesList, String cwd)
    {
        this.cwd = rootPath + "/directory" + cwd;

        String filesListHtml = "<!DOCTYPE html>" +
                "<title> FTP - "+ cwd +"</title>" +
                this.getStyleSheet() +
                "<body>" +
                "<div class='content'>" +
                "<h2> Index of : " + cwd + "</h2>" +
                getPostForm() +
                "<table class='table'>" +
                "<thead>" +
                "<tr>" +
                "<th> Name </th>" +
                "<th> Type </th>" +
                "<th> Size </th>" +
                "<th> Operations </th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";

        filesListHtml += "<tr>" +
                "<td>"+this.getParentDirectory(cwd)+"</td>" +
                "<td> Directory </td>" +
                "<td> Unknown </td>" +
                "<td> No Operation </td>" +
                "</tr>";

        for (FTPFile file : filesList)

            if (file.isDirectory())
                filesListHtml += this.getDirectoryLink(file, cwd);
            else
                filesListHtml += this.getFileLink(file);

        filesListHtml += "</tbody>" +
                         "</table>" +
                         "</div>" +
                         "</body>" +
                         "</html>";

        return filesListHtml;
    }

    /**
     * Méthode responsable de la génération du code HTML pour un fichier ( Ligne d'un tableau )
     * @param file Le fichier pour lequel on souhaite générer un code HTML / CSS
     * @return html Le HTML généré
     */
    public String getFileLink(FTPFile file)
    {

        return "<tr>" +
                "<td><img src='https://cdn1.iconfinder.com/data/icons/hawcons/32/699326-icon-54-document-16.png' style='vertical-align: middle;'><a style='vertical-align: sub; margin-left:10px;' href= '" + rootPath + "/download/"+ file.getName() +"'>" + file.getName() + "</a></td>" +
                "<td> File </td>" +
                "<td>" + file.getSize() + "</td>" +
                "<td>" +
                "<a style='vertical-align: inherit;' href= '" + rootPath + "/download/"+ file.getName() +"'><img src='https://cdn1.iconfinder.com/data/icons/hawcons/32/698605-icon-92-inbox-download-24.png' style='vertical-align: middle;'> </a>" +
                "<a style='vertical-align: inherit;' href= '"+rootPath+ "/delete/"+ file.getName() +"'><img src='https://cdn1.iconfinder.com/data/icons/hawcons/32/698908-icon-27-trash-can-24.png' style='vertical-align: middle;'> </a>" +
                "</td>" +
                "</tr>";
    }

    /**
     * Méthode responsable de la génération du code HTML pour un repertoire ( Ligne d'un tableau )
     * @param directory Le repertoire pour lequel on souhaite générer un code HTML / CSS
     * @param cwd Le repertoir courant.
     * @return html Le HTML généré
     */
    public String getDirectoryLink(FTPFile directory, String cwd)
    {
        return "<tr>" +
                "<td class='directory'><img src='https://cdn1.iconfinder.com/data/icons/hawcons/32/699652-icon-94-folder-16.png' style='vertical-align: middle;'><a style='vertical-align: sub; margin-left:10px;' href= '" + rootPath + "/directory" + cwd + "/" + directory.getName() + "'>" + directory.getName() + "</a></td>" +
                "<td> Directory </td>" +
                "<td>" + directory.getSize() + "</td>" +
                "<td>" +
                "<a style='vertical-align: inherit;' href= '"+rootPath+ "/delete/"+ directory.getName() +"'><img src='https://cdn1.iconfinder.com/data/icons/hawcons/32/698908-icon-27-trash-can-24.png' style='vertical-align: middle;'> </a>" +
                "</td>" +
                "</tr>";
    }

    /**
     * Méthode responsable de la génération du code HTML du repertoire parent
     * @param cwd Le repertoire courant
     * @return html Le HTML généré
     */
    public String getParentDirectory(String cwd)
    {
        Path parent = Paths.get(cwd).getParent();
        return "<a href= '" + rootPath + "/directory" + parent + "'><img src='https://cdn1.iconfinder.com/data/icons/hawcons/32/698931-icon-98-folder-upload-16.png' /></a><br>";
    }

    /**
     * Méthode reponsable de la génération du code HTML pour le formulaire d'upload.
     * @return Le HTML généré
     */
    public String getPostForm()
    {

        return "<form method='post' enctype='multipart/form-data' class='upload-form'> " +
                "<b>Upload a file to the current directory : </b><input type='file' class='upload' name='upload' size='50' />" +
                "<input type='submit' class='bt' value='Upload' />" +
                "</form>";
    }

    /**
     * Méthode responsable de la génération du message d'échec en HTML de l'upload de fichier.
     * @return Le HTML généré
     */
    public String getNoFileError() {

        return "<!DOCTYPE html>" +
                "<title> Uploading Unsuccessfull ! </title>" +
                this.getStyleSheet() +
                "<body>" +
                "<div class='content'>" +
                "<div class='box'>" +
                "<div class='box_head'>" +
                "<h3>Something went wrong !</h3>" +
                "</div>" +
                "<div class='box_body'> You did not select any file ! </div>" +
                "<div class='box_foot'>" +
                "<div class='actions'>" +
                "<a href='"+ this.cwd+"' class='bt'>Go Back</a>" +
                "</div>" +
                "</div>" +
                "</div></div>" +
                "</body></html>";
    }

    /**
     * Méthode responsable de la génération du message de réussite en HTML de l'upload de fichier.
     * @return Le HTML généré
     */
    public String getFileUploadSuccess() {

        return "<!DOCTYPE html>" +
                "<title> Uploading Successfull ! </title>" +
                this.getStyleSheet() +
                "<body>" +
                "<div class='content'>" +
                "<div class='box'>" +
                "<div class='box_head'>" +
                "<h3>Everything went good !</h3>" +
                "</div>" +
                "<div class='box_body'> The file selected was uploaded successfully </div>" +
                "<div class='box_foot'>" +
                "<div class='actions'>" +
                "<a href='"+ this.cwd+"' class='bt'>Go Back</a>" +
                "</div>" +
                "</div>" +
                "</div></div>" +
                "</body></html>";
    }

    /**
     * Méthode responsable de la génération du message d'échec en HTML de l'upload de fichier.
     * @return Le HTML généré
     */
    public String getUploadFail() {

        return "<!DOCTYPE html>" +
                "<title> Uploading unsuccessfull ! </title>" +
                this.getStyleSheet() +
                "<body>" +
                "<div class='content'>" +
                "<div class='box'>" +
                "<div class='box_head'>" +
                "<h3>Something went wrong...!</h3>" +
                "</div>" +
                "<div class='box_body'> The selected file was not uploaded !</div>" +
                "<div class='box_foot'>" +
                "<div class='actions'>" +
                "<a href='"+ this.cwd+"' class='bt'>Go Back</a>" +
                "</div>" +
                "</div>" +
                "</div></div>" +
                "</body></html>";
    }

    /**
     * Méthode responsable de la génération du HTML pour le login.
     * @return Le HTML généré
     */
    public String getLoginForm() {

        return "<!DOCTYPE html>" +
                "<title> Connect to your FTP </title>" +
                this.getStyleSheet() +
                "<body>" +
                "<div class='content'>" +
                "<div class='box'>" +
                "<div class='box_head'>Login to your FTP</div>" +
                "<div class='box_body'>" +
                "<center>" +
                "<form method='post' class='login-form'>" +
                "<label for='user'>Host : </label><input type='text' class='input' name='host'><br>" +
                "<label for='user'>Port : </label><input type='text' class='input' name='port'><br>" +
                "<label for='user'>Username : </label><input type='text' class='input' name='user'><br>" +
                "<label for='pass'>Password  :</label><input type='password' class='input' name='pass'><br>" +
                "<div class='box_foot'>" +
                "<input type='submit' class='bt' value='Login'>" +
                "</div>" +
                "</form>" +
                "</center></div>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

    /**
     * Méthode responsable de la génération du message de succés de suppression
     * @return Le HTML généré
     */
    public String getDeleteSuccess() {

        return "<!DOCTYPE html>" +
                "<title> Deleting Successfull ! </title>" +
                this.getStyleSheet() +
                "<body>" +
                "<div class='content'>" +
                "<div class='box'>" +
                "<div class='box_head'>" +
                "<h3>Everything went good !</h3>" +
                "</div>" +
                "<div class='box_body'> The selected item has been removed successfully ! </div>" +
                "<div class='box_foot'>" +
                "<div class='actions'>" +
                "<a href='"+ this.cwd+"' class='bt'>Go Back</a>" +
                "</div>" +
                "</div>" +
                "</div></div>" +
                "</body></html>";
    }

    /**
     * Méthode responsable de la génération de l'erreur de suppression en HTML.
     * @return Le HTML généré.
     */
    public String getDeleteFail() {

        return "<!DOCTYPE html>" +
                "<title> Deleting Unsuccessfull ! </title>" +
                this.getStyleSheet() +
                "<body>" +
                "<div class='content'>" +
                "<div class='box'>" +
                "<div class='box_head'>" +
                "<h3>Something went wrong... !</h3>" +
                "</div>" +
                "<div class='box_body'> The selected item has not been removed ! </div>" +
                "<div class='box_foot'>" +
                "<div class='actions'>" +
                "<a href='"+ this.cwd+"' class='bt'>Go Back</a>" +
                "</div>" +
                "</div>" +
                "</div></div>" +
                "</body></html>";
    }

    /**
     * Méthode responsable de la génération du CSS.
     * @return Le CSS généré.
     */
    public String getStyleSheet()
    {
        return "<style>" +
                "html, body, div, span, applet, object, iframe,\n" +
                "p, blockquote, pre,\n" +
                "a, abbr, acronym, address, big, cite, code,\n" +
                "del, dfn, em, img, ins, kbd, q, s, samp,\n" +
                "small, strike, strong, sub, sup, tt, var,\n" +
                "b, u, i, center,\n" +
                "dl, dt, dd, ol, ul, li,\n" +
                "fieldset, form, label, legend,\n" +
                "table, caption, tbody, tfoot, thead, tr, th, td,\n" +
                "article, aside, canvas, details, embed,\n" +
                "figure, figcaption, footer, header, hgroup,\n" +
                "menu, nav, output, ruby, section, summary,\n" +
                "time, mark, audio, video {\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "  border: 0;\n" +
                "  font: inherit;\n" +
                "  font-size: 100%;\n" +
                "  vertical-align: baseline;\n" +
                "}\n" +
                "\n" +
                "body { background: url(http://i.imgur.com/LHFL5vn.jpg) 40% 20%; }" +
                "html {\n" +
                "  line-height: 1;\n" +
                "}\n" +
                "\n" +
                "ol, ul {\n" +
                "  list-style: none;\n" +
                "}\n" +
                "\n" +
                "table {\n" +
                "  border-collapse: collapse;\n" +
                "  border-spacing: 0;\n" +
                "}\n" +
                "\n" +
                "caption, th, td {\n" +
                "  text-align: left;\n" +
                "  font-weight: normal;\n" +
                "  vertical-align: middle;\n" +
                "}\n" +
                "\n" +
                "q, blockquote {\n" +
                "  quotes: none;\n" +
                "}\n" +
                "q:before, q:after, blockquote:before, blockquote:after {\n" +
                "  content: \"\";\n" +
                "  content: none;\n" +
                "}\n" +
                "\n" +
                "a img {\n" +
                "  border: none;\n" +
                "}\n" +
                "\n" +
                "article, aside, details, figcaption, figure, footer, header, hgroup, menu, nav, section, summary {\n" +
                "  display: block;\n" +
                "}\n" +
                "\n" +
                "body {\n" +
                "  font: 12px \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif;\n" +
                "}\n" +
                "\n" +
                ".content {\n" +
                "  max-width: 800px;\n" +
                "  margin: 0 auto;\n" +
                "}\n" +
                "\n" +
                ".thumb {\n" +
                "  border: 2px solid black;\n" +
                "}\n" +
                "\n" +
                "hr {\n" +
                "  margin: 10px 0;\n" +
                "  border: 0;\n" +
                "  border-top: 1px solid #dddddd;\n" +
                "  border-bottom: 1px solid white;\n" +
                "}\n" +
                "\n" +
                "strong, b {\n" +
                "  font-weight: bold;\n" +
                "}\n" +
                "\n" +
                ".badge {\n" +
                "  display: inline-block;\n" +
                "  padding: 0.45em 0.5em;\n" +
                "  font-weight: bold;\n" +
                "  text-align: center;\n" +
                "  color: black;\n" +
                "  background: white;\n" +
                "  border: 2px solid black;\n" +
                "  -webkit-border-radius: 1.5em;\n" +
                "  -moz-border-radius: 1.5em;\n" +
                "  -ms-border-radius: 1.5em;\n" +
                "  -o-border-radius: 1.5em;\n" +
                "  border-radius: 1.5em;\n" +
                "}\n" +
                ".badge.invert {\n" +
                "  background: black;\n" +
                "  color: white;\n" +
                "}\n" +
                "\n" +
                ".box {\n" +
                "  margin-bottom: 20px;\n" +
                "  background: white;\n" +
                "  border: 2px solid black;\n" +
                "}\n" +
                ".box .box_head {\n" +
                "  min-height: 20px;\n" +
                "  padding: 8px 15px;\n" +
                "  position: relative;\n" +
                "  background: black;\n" +
                "  color: white;\n" +
                "}\n" +
                ".box .box_head h1, .box .box_head h2, .box .box_head h3, .box .box_head h4, .box .box_head h5, .box .box_head h6 {\n" +
                "  padding: 0;\n" +
                "  margin: 0;\n" +
                "  font: bold 12px/1.5em \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif;\n" +
                "  text-transform: uppercase;\n" +
                "}\n" +
                ".box .box_head .actions {\n" +
                "  position: absolute;\n" +
                "  top: 2px;\n" +
                "  right: 5px;\n" +
                "}\n" +
                ".box .box_head .actions .bt {\n" +
                "  color: white;\n" +
                "  border-color: white;\n" +
                "}\n" +
                ".box .box_head .actions .bt:hover {\n" +
                "  color: black;\n" +
                "  background: white;\n" +
                "}\n" +
                ".box .box_foot {\n" +
                "  padding: 0 5px 5px;\n" +
                "  text-align: right;\n" +
                "}\n" +
                ".box .box_head .actions .bt,\n" +
                ".box .box_foot .actions .bt {\n" +
                "  height: 30px;\n" +
                "  font-size: 10px;\n" +
                "  vertical-align: middle;\n" +
                "}\n" +
                ".box .box_body {\n" +
                "  padding: 15px;\n" +
                "}\n" +
                "\n" +
                ".bt {\n" +
                "  height: 35px;\n" +
                "  position: relative;\n" +
                "  padding: 0.55em 1em 0.5em;\n" +
                "  margin: 0;\n" +
                "  font: bold 12px/1.5em \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif;\n" +
                "  text-align: center;\n" +
                "  text-decoration: none;\n" +
                "  text-transform: uppercase;\n" +
                "  cursor: pointer;\n" +
                "  display: inline-block;\n" +
                "  vertical-align: top;\n" +
                "  border: 2px solid black;\n" +
                "  color: black;\n" +
                "  background: transparent;\n" +
                "  -webkit-box-sizing: border-box;\n" +
                "  -moz-box-sizing: border-box;\n" +
                "  box-sizing: border-box;\n" +
                "  -webkit-border-radius: 0;\n" +
                "  -moz-border-radius: 0;\n" +
                "  -ms-border-radius: 0;\n" +
                "  -o-border-radius: 0;\n" +
                "  border-radius: 0;\n" +
                "  -webkit-appearance: none;\n" +
                "  -moz-appearance: none;\n" +
                "  appearance: none;\n" +
                "}\n" +
                ".bt:hover, .bt.active {\n" +
                "  background: black;\n" +
                "  color: white;\n" +
                "}\n" +
                ".bt.disabled {\n" +
                "  opacity: .3;\n" +
                "  cursor: default;\n" +
                "}\n" +
                ".bt.disabled:hover {\n" +
                "  background: inherit;\n" +
                "  color: inherit;\n" +
                "}\n" +
                ".bt.bt_small {\n" +
                "  font-size: 10px;\n" +
                "  height: 30px;\n" +
                "}\n" +
                ".bt.bt_medium {\n" +
                "  font-size: 14px;\n" +
                "  height: 40px;\n" +
                "}\n" +
                ".bt.bt_large {\n" +
                "  font-size: 16px;\n" +
                "  height: 45px;\n" +
                "}\n" +
                "\n" +
                "select,\n" +
                "textarea,\n" +
                "input[type=\"text\"],\n" +
                "input[type=\"password\"],\n" +
                "input[type=\"datetime\"],\n" +
                "input[type=\"datetime-local\"],\n" +
                "input[type=\"date\"],\n" +
                "input[type=\"month\"],\n" +
                "input[type=\"time\"],\n" +
                "input[type=\"week\"],\n" +
                "input[type=\"number\"],\n" +
                "input[type=\"email\"],\n" +
                "input[type=\"url\"],\n" +
                "input[type=\"search\"],\n" +
                "input[type=\"tel\"],\n" +
                "input[type=\"color\"] {\n" +
                "  width: 200px;\n" +
                "  height: 35px;\n" +
                "  padding: 8px 6px;\n" +
                "  margin: 0;\n" +
                "  outline: none;\n" +
                "  display: inline-block;\n" +
                "  vertical-align: middle;\n" +
                "  font: 12px \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif;\n" +
                "  background: white;\n" +
                "  color: black;\n" +
                "  border: 2px solid black;\n" +
                "  -webkit-box-sizing: border-box;\n" +
                "  -moz-box-sizing: border-box;\n" +
                "  box-sizing: border-box;\n" +
                "  -webkit-border-radius: 0;\n" +
                "  -moz-border-radius: 0;\n" +
                "  -ms-border-radius: 0;\n" +
                "  -o-border-radius: 0;\n" +
                "  border-radius: 0;\n" +
                "  -webkit-appearance: none;\n" +
                "  -moz-appearance: none;\n" +
                "  appearance: none;\n" +
                "}\n" +
                "select[disabled],\n" +
                "textarea[disabled],\n" +
                "input[type=\"text\"][disabled],\n" +
                "input[type=\"password\"][disabled],\n" +
                "input[type=\"datetime\"][disabled],\n" +
                "input[type=\"datetime-local\"][disabled],\n" +
                "input[type=\"date\"][disabled],\n" +
                "input[type=\"month\"][disabled],\n" +
                "input[type=\"time\"][disabled],\n" +
                "input[type=\"week\"][disabled],\n" +
                "input[type=\"number\"][disabled],\n" +
                "input[type=\"email\"][disabled],\n" +
                "input[type=\"url\"][disabled],\n" +
                "input[type=\"search\"][disabled],\n" +
                "input[type=\"tel\"][disabled],\n" +
                "input[type=\"color\"][disabled] {\n" +
                "  opacity: .35;\n" +
                "  cursor: default;\n" +
                "}\n" +
                "\n" +
                "select {\n" +
                "  width: auto;\n" +
                "  height: 35px;\n" +
                "  -webkit-border-radius: 0;\n" +
                "  -moz-border-radius: 0;\n" +
                "  -ms-border-radius: 0;\n" +
                "  -o-border-radius: 0;\n" +
                "  border-radius: 0;\n" +
                "  -webkit-box-sizing: border-box;\n" +
                "  -moz-box-sizing: border-box;\n" +
                "  box-sizing: border-box;\n" +
                "  /* CSS Statements that only apply on webkit-based browsers (Chrome, Safari, etc.) */\n" +
                "}\n" +
                "@media screen and (-webkit-min-device-pixel-ratio: 0) {\n" +
                "  select {\n" +
                "    padding-right: 25px;\n" +
                "    background: url(\"../images/arrow.png\") no-repeat 100% 50% white;\n" +
                "    -webkit-appearance: none;\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "textarea {\n" +
                "  width: 300px;\n" +
                "  height: 150px;\n" +
                "}\n" +
                "\n" +
                ".label {\n" +
                "  display: inline-block;\n" +
                "  padding: 0.15em 0.4em;\n" +
                "  font-weight: bold;\n" +
                "  text-transform: uppercase;\n" +
                "  background: white;\n" +
                "  color: black;\n" +
                "  border: 2px solid black;\n" +
                "}\n" +
                ".label.invert {\n" +
                "  background: black;\n" +
                "  color: white;\n" +
                "}\n" +
                "\n" +
                "table {\n" +
                "  width: 100%;\n" +
                "  position: relative;\n" +
                "  line-height: 1;\n" +
                "}\n" +
                "\n" +
                ".table {\n" +
                "  margin: 0 0 5px;\n" +
                "  border-collapse: separate;\n" +
                "  border: 2px solid black;\n" +
                "}\n" +
                ".table thead tr th {\n" +
                "  padding: 10px 8px;\n" +
                "  font-weight: bold;\n" +
                "  text-transform: uppercase;\n" +
                "  background: black;\n" +
                "  color: white;\n" +
                "  border-bottom: 2px solid black;\n" +
                "  border-left: 2px solid black;\n" +
                "}\n" +
                ".table thead tr th:first-child {\n" +
                "  border-left: none;\n" +
                "}\n" +
                ".table tbody tr td {\n" +
                "  padding: 9px;\n" +
                "  background: white;\n" +
                "  border-left: 2px solid black;\n" +
                "  border-top: 2px solid black;\n" +
                "}\n" +
                ".table tbody tr td:first-child {\n" +
                "  border-left: none;\n" +
                "}\n" +
                ".table tbody tr:first-child td {\n" +
                "  border-top: none;\n" +
                "}\n" +
                "\n" +
                ".content {\n" +
                "  padding: 20px;\n" +
                "}\n" +
                ".content h1 {\n" +
                "  padding: 15px 0;\n" +
                "  font-size: 50px;\n" +
                "  letter-spacing: -2px;\n" +
                "  font-weight: bold;\n" +
                "  text-align: center;\n" +
                "}\n" +
                ".content h2 {\n" +
                "  font-size: 20px;\n" +
                "  font-weight: bold;\n" +
                "  padding: 10px 0;\n" +
                "}\n" +
                ".content p {\n" +
                "  line-height: 1.4em;\n" +
                "  padding: .3em;\n" +
                "}\n" +
                "\n" +
                ".description {\n" +
                "  font-size: 16px;\n" +
                "}\n" +
                ".description ul {\n" +
                "  padding: 10px 0 10px 25px;\n" +
                "  list-style: disc;\n" +
                "}\n" +
                ".description ul li {\n" +
                "  padding: 2px 0;\n" +
                "}\n" +
                ".description .actions {\n" +
                "  text-align: center;\n" +
                "  margin: 20px 0;\n" +
                "  padding: 40px 0;\n" +
                "  border-top: 1px solid #dddddd;\n" +
                "  border-bottom: 1px solid #dddddd;\n" +
                "}\n" +
                ".description .actions a {\n" +
                "  margin: 0 5px;\n" +
                "  width: 23%;\n" +
                "}\n" +
                "\n" +
                ".components .component {\n" +
                "  margin: 20px 0;\n" +
                "  padding: 20px 0;\n" +
                "}\n" +
                ".components .component > h3 {\n" +
                "  font-size: 12px;\n" +
                "  color: #999;\n" +
                "  padding: 0 0 20px;\n" +
                "  margin: 0;\n" +
                "}\n" +
                "\n" +
                ".footer {\n" +
                "  font-size: 12px;\n" +
                "  padding: 10px 0 50px;\n" +
                "  border-top: 1px solid #dddddd;\n" +
                "}\n" +
                ".footer .credits {\n" +
                "  background: #f2f2f2;\n" +
                "  padding: 10px;\n" +
                "  margin-bottom: 10px;\n" +
                "  font-size: 14px;\n" +
                "  text-align: center;\n" +
                "}\n" +
                ".footer h1 {\n" +
                "  font-size: 30px;\n" +
                "  letter-spacing: -1px;\n" +
                "}\n" +
                ".footer a {\n" +
                "  color: #000;\n" +
                "  font-weight: bold;\n" +
                "}\n" +
                "\n" +
                ".addthis {\n" +
                "  padding: 20px 0 0;\n" +
                "  height: 30px;\n" +
                "}\n" +
                "\n" +
                "@media screen and (max-width: 640px) {\n" +
                "  /* to avoid the adjustment of fonts on iOS devices you should keep this line */\n" +
                "  * {\n" +
                "    -webkit-text-size-adjust: none;\n" +
                "    -ms-text-size-adjust: none;\n" +
                "  }\n" +
                "\n" +
                "  .content {\n" +
                "    min-width: 440px;\n" +
                "  }\n" +
                "}\n" +
                ".upload-form {margin : 10px 10px 30px 20px;}" +
                ".upload { border:2px solid black; padding: 5px; margin-right: 3px;}" +
                ".login-form { margin-left: -77px;}" +
                ".login-form > input { margin-bottom : 10px; }" +
                "a { font-size : 16px; text-decoration:none; }" +
                ".directory {background-color : #F3F3F3 !important;}" +
                "label {display: inline-block; !important; width : 120px !important; }" +

                "</style>";
    }
}
