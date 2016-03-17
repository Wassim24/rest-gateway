package tests.passrelle;

import car.tp2.FilesManagerResource;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.junit.*;

import utils.Client;
import utils.HTMLGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FilesManagerTester {

    public FilesManagerTester() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass(){}

    @Before
    public void setUp(){

    }

    @After
    public void tearDown() {

    }

    /**
     * Test du login réussi.
     */
    @Test
    public void loginSuccess(){
        FilesManagerResource fmr = new FilesManagerResource();
        String logged = fmr.login(System.getProperty("user.home"), "user", "pass", "localhost", "1050");
        String res = fmr.accessDirectory(System.getProperty("user.home"));
        assertEquals(logged, res);
    }

    /**
     * Test du login échec.
     */
    @Test
    public void loginFail(){
        FilesManagerResource fmr = new FilesManagerResource();
        String logged = fmr.login(System.getProperty("user.home"), "user", "pess", "localhost", "1050");
        String res = fmr.accessDirectory(System.getProperty("user.home"));
        assertEquals(logged, res);
    }

    /**
     * Test Changement de repertoire succés
     */
    @Test
    public void accessDirectorySuccess()
    {
        FilesManagerResource fmr = new FilesManagerResource();
        Client.getInstance().login("user", "pass", "localhost", "1050");
        String res = fmr.accessDirectory(System.getProperty("user.home"));
        assertEquals(true, res.contains(System.getProperty("user.home") + "/Documents"));
    }

    /**
     * Test Changement de repertoire échec
     */
    @Test
    public void accessDirectoryFail()
    {
        FilesManagerResource fmr = new FilesManagerResource();
        Client.getInstance().login("user", "pass", "localhost", "1050");
        String res = fmr.accessDirectory(System.getProperty("user.home"));
        assertNotEquals(true, res.contains(System.getProperty("user.home") + "/FAILTEST"));
    }

    /**
     * Test suppression d'un fichier succés
     */
    @Test
    public void deleteFileSuccess()
    {
        FilesManagerResource fmr = new FilesManagerResource();
        Client.getInstance().login("user", "pass", "localhost", "1050");
        String res = fmr.deleteFile("successDelete");
        assertEquals(true, res.contains("The selected item has been removed successfully !"));
    }

    /**
     * Test suppression d'un fichier échec
     */
    @Test
    public void deleteFileFail()
    {
        FilesManagerResource fmr = new FilesManagerResource();
        Client.getInstance().login("user", "pass", "localhost", "1050");
        String res = fmr.deleteFile("failedDelete");
        assertEquals(false, res.contains("The selected item has been removed successfully !"));
    }

    /**
     * Test téléchargement d'un fichier succés.
     */
    @Test
    public void downloadSuccess()
    {
        FilesManagerResource fmr = new FilesManagerResource();
        Client.getInstance().login("user", "pass", "localhost", "1050");
        assertEquals(200, fmr.getFile("downloadSuccess").getStatus());
    }

    /**
     * Test téléchargement d'un fichier échec.
     */
    @Test
    public void downloadFail()
    {
        FilesManagerResource fmr = new FilesManagerResource();
        Client.getInstance().login("user", "pass", "localhost", "1050");
        assertNotEquals(200, fmr.getFile("downloadFail").getStatus());
    }

    /**
     * Test upload vide succés
     */
    @Test
    public void uploadNoFileSuccess()
    {
        FilesManagerResource fmr = new FilesManagerResource();
        Client.getInstance().login("user", "pass", "localhost", "1050");
        List<Attachment> attachmentList = new LinkedList<>();
        assertEquals(fmr.uploadFile(attachmentList), HTMLGenerator.getInstance().getNoFileError());
    }

    /**
     * Test upload échec.
     */
    @Test
    public void uploadFileFail()
    {
        FilesManagerResource fmr = new FilesManagerResource();
        Client.getInstance().login("user", "pass", "localhost", "1050");
        List<Attachment> attachmentList = new LinkedList<>();
        try {
            InputStream is = new FileInputStream(System.getProperty("user.home")+"/failUpload");
            ContentDisposition cd = new ContentDisposition("filename");
            Attachment attachment = new Attachment("idFile", is,cd);
            attachmentList.add(attachment);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        assertEquals(fmr.uploadFile(attachmentList), HTMLGenerator.getInstance().getUploadFail());
    }
}
