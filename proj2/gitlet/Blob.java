package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import static gitlet.Utils.*;

public class Blob {
    /** Blobs folder. */
    public static final File BLOBS_FOLDER = join(Repository.OBJ_FOLDER, "blobs");
    /** Blob data */
    private final BlobData data;

    private static class BlobData implements Serializable {
        /** File name. */
        private final String fileName;
        /** File contents. */
        private final byte[] content;

        BlobData(String fn, byte[] c) {
            fileName = fn;
            content = c;
        }
    }

    public Blob(String fn) {
        File file = join(Repository.CWD, fn);
        data = new BlobData(fn, readContents(file));
    }

    private Blob(BlobData d) {
        data = d;
    }

    public void saveBlob() {
        String id = sha1(serialize(data));
        File blobFile = join(BLOBS_FOLDER, id);
        writeObject(blobFile, data);
    }

    public static Blob getBlob(String id) {
        File blobFile = join(BLOBS_FOLDER, id);
        return new Blob(readObject(blobFile, BlobData.class));
    }

    public String getId() {
        return sha1(serialize(data));
    }

    public byte[] getContent() {
        return data.content;
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
        return Objects.hash(data.fileName, Arrays.hashCode(data.content));
    }

    public boolean equalsWithContent(File file) {
        return sha1(readContents(file)).equals(sha1(data.content));
    }

    public boolean equalsWithContent(String file) {
        return equalsWithContent(join(Repository.CWD, file));
    }

    public void checkout() {
        File file = join(Repository.CWD, data.fileName);
        writeContents(file, data.content);
    }
}
