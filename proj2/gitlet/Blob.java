package gitlet;

import java.io.File;
import java.io.Serializable;
import static gitlet.Utils.*;

public class Blob implements Serializable {
    /** Blobs folder. */
    public static final File BLOBS_FOLDER = join(Repository.OBJ_FOLDER, "blobs");
    /** File contents. */
    private final byte[] content;

    public Blob(String fileName) {
        File file = join(Repository.CWD, fileName);
        content = readContents(file);
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
}
