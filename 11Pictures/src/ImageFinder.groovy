/**
 * Created with IntelliJ IDEA.
 * User: SoyYo
 * Date: 12.03.13
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
class ImageFinder {
    File[] findPreviewImages() {
        File f = new File("Z:\\");
        File[] directories = []
        f.eachDir { if (new File(it, "preview.jpg").exists()){ directories += it}};

        directories=directories.sort{it.lastModified()}

        return directories;
    }

}
