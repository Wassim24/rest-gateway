package car.tp2;

import com.sun.istack.NotNull;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import utils.Client;
import utils.HTMLGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.util.List;


/**
 * Ressource reponsable de toutes les opérations possibles sur le FTP.
 * @author wassim
 */
@Path("/")
public class FilesManagerResource {


    /**
     * Méthode responsable du listing de la racine du FTP.
     * @return Le login form si on est pas identifié sur le FTP, sinon la liste de fichier.
     */
    @GET
    @Produces("text/html")
    @Path("/")
    public String accessRoot() {
        return this.accessDirectory(System.getProperty("user.home"));
    }

    /**
     * Méthode par la quelle on peut naviguer dans les dossier.
     * @param directory Le repertoire dans au quel on veut accéder.
     * @return Le login form si on est pas identifié sur le FTP, sinon la liste de fichier.
     */
    @GET
    @Produces("text/html")
    @Path("/directory/{directoryName : .*}")
    public String accessDirectory( @PathParam("directoryName") String directory ) {

        if(Client.getInstance().getConnectionStatus())
        {
            if(directory.equals(".."))
                Client.getInstance().getParentDirectory();
            else
                Client.getInstance().changeWorkingDirectory("/" + directory);

            return HTMLGenerator.getInstance().generateFilesList(Client.getInstance().getListFiles(), Client.getInstance().getCurrentWorkingDirectory());
        }
        else
        {
            return HTMLGenerator.getInstance().getLoginForm();
        }
    }

    /**
     * Méthode permettant l'identification sur le FTP.
     * @param directory Le répertoire au quel on veut accéder après le login.
     * @param user Le nom d'utilisateur du FTP.
     * @param pass Le mot de passe.
     * @param host L'hôte du FTP.
     * @param port Le port sur lequel ouvrir la communication avec le FTP.
     * @return Le login form si on est pas identifié sur le FTP, sinon la liste de fichier.
     */
    @POST
    @Path("/{directoryName: .*}")
    @Produces("text/html")
    public String login(@PathParam("directoryName") String directory, @FormParam("user") String user, @FormParam("pass") String pass, @FormParam("host") String host, @FormParam("port") String port)
    {
        if ( Client.getInstance().login(user, pass, host, port) )
            return accessDirectory(directory);
        else
            return HTMLGenerator.getInstance().getLoginForm();
    }

    /**
     * Méthode permettant l'upload de fichier.
     * @param attachmentList La liste des fichier à uploader.
     * @return Message de succés ou d'échec.
     */
    @POST
    @Path("/directory/{directoryName: .*}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFile(List<Attachment> attachmentList) {

        if(Client.getInstance().getConnectionStatus())
        {
            if (attachmentList != null)
            {
                for (Attachment attachment : attachmentList)
                {
                    try {
                        if (attachment.getContentDisposition().getParameter("filename") != null) {

                            if (!Client.getInstance().saveFile(
                                    Client.getInstance().getCurrentWorkingDirectory() + "/" + attachment.getContentDisposition().getParameter("filename"),
                                    attachment.getDataHandler().getInputStream())
                                )
                                return HTMLGenerator.getInstance().getUploadFail();
                            else
                                return HTMLGenerator.getInstance().getFileUploadSuccess();

                        }else
                            return HTMLGenerator.getInstance().getUploadFail();

                    } catch (IOException e) {return HTMLGenerator.getInstance().getUploadFail();}
                }
                return HTMLGenerator.getInstance().getNoFileError();
            }
            else
                return HTMLGenerator.getInstance().getNoFileError();
        }
        else
            return HTMLGenerator.getInstance().getLoginForm();
    }

    /**
     * Méthode pour l'upload en PUT.
     * @param attachmentList La liste des fichiers a uploader.
     * @return Message de succés ou d'échec.
     * @see #uploadFile
     */
    @PUT
    @Path("/directory/{directoryName: .*}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String putFile(List<Attachment> attachmentList) {
        return this.uploadFile(attachmentList);
    }

    /**
     * Méthode permettant la suppression d'un fichier ou dossier.
     * @param fileName Le nom du dossier / fichier à supprimer.
     * @return Le login form si on est pas identifié sur le FTP, sinon message de succés ou d'échec.
     */
    @GET
    @Path("/delete/{filename}")
    public String deleteFile(@PathParam("filename") String fileName) {

        if(Client.getInstance().getConnectionStatus())
        {
            if( Client.getInstance().deleteFile(Client.getInstance().getCurrentWorkingDirectory()+"/" + fileName) )
                return HTMLGenerator.getInstance().getDeleteSuccess();
            else
                return HTMLGenerator.getInstance().getDeleteFail();
        }
        else
            return HTMLGenerator.getInstance().getLoginForm();
    }

    /**
     * Méthode permettant la suppression d'un fichier ou dossier.
     * @param dir Le nom du dossier à supprimer.
     * @param fileName le nom du fichier a supprimer.
     * @return Le login form si on est pas identifié sur le FTP, sinon message de succés ou d'échec.
     */
    @DELETE
    @Path("/delete/{filename}")
    public String deleteFileVerb ( @PathParam("directoryName") String dir, @PathParam("filename") String fileName) {
        if(Client.getInstance().getConnectionStatus())
            return String.valueOf(Client.getInstance().deleteFile("/"+dir + "/" + fileName));
        else
            return HTMLGenerator.getInstance().getLoginForm();
    }

    /**
     * Méthode permettant le téléchargement d'un fichier.
     * @param filename Le nom du fichier a télécharger.
     * @return Reponse 200 si succés, autre sinon.
     */
    @GET
    @Produces("application/octetstream")
    @Path("/download/{filename}")
    public Response getFile( @PathParam("filename") String filename ) {

        if (Client.getInstance().getConnectionStatus()) {

            InputStream is = Client.getInstance().getFile(filename);

            if (is != null)
                return Response.ok(is, MediaType.APPLICATION_OCTET_STREAM).build();
            else
                return Response.serverError().build();

        } else
            return Response.temporaryRedirect(URI.create("/")).build();
    }
}

