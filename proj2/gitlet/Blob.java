package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import static gitlet.Utils.*;

public class Blob implements Serializable {
    /** Blobs folder. */
    public static final File BLOBS_FOLDER = join(Repository.OBJ_FOLDER, "blobs");
    /** File name. */
    private final String fileName;
    /** File contents. */
    private final byte[] content;

    public Blob(String fn) {
        File file = join(Repository.CWD, fn);
        content = readContents(file);
        fileName = fn;
    }

    public void saveBlob() {
        String id = sha1(serialize(this));
        File blobFile = join(BLOBS_FOLDER, id);
        writeObject(blobFile, this);
    }

    public static Blob getBlob(String id) {
        File blobFile = join(BLOBS_FOLDER, id);
        return readObject(blobFile, Blob.class);
    }

    public String getId() {
        return sha1(serialize(this));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Blob)) {
            return false;
        }
        Blob b = (Blob) obj;
        return b.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, Arrays.hashCode(content));
    }

    public boolean equalsWithContent(File file) {
        return sha1(readContents(file)).equals(sha1(content));
    }

    public boolean equalsWithContent(String file) {
        return equalsWithContent(join(Repository.CWD, file));
    }

    public void checkout() {
        File file = join(Repository.CWD, fileName);
        writeContents(file, content);
    }
}
