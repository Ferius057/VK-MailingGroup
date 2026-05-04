package api.longpoll.bots.model.objects.additional;

/**
 * An uploaded to VK server file.
 */
public class UploadedFile {
    /**
     * File type.
     */
    private final String type;

    /**
     * Owner ID.
     */
    private final long ownerId;

    /**
     * File ID.
     */
    private final long mediaId;

    /**
     * file <i>access_key</i>.
     */
    private final String accessKey;

    public UploadedFile(String type, long ownerId, long mediaId, String accessKey) {
        this.type = type;
        this.ownerId = ownerId;
        this.mediaId = mediaId;
        this.accessKey = accessKey;
    }

    /**
     * Gets file type.
     *
     * @return file type.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets owner ID.
     *
     * @return owner ID.
     */
    public long getOwnerId() {
        return ownerId;
    }

    /**
     * Gets file ID.
     *
     * @return file ID.
     */
    public long getMediaId() {
        return mediaId;
    }

    /**
     * Gets file <i>access_key</i>.
     *
     * @return file <i>access_key</i>.
     */
    public String getAccessKey() {
        return accessKey;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder()
                .append(getType())
                .append(getOwnerId())
                .append("_")
                .append(getMediaId());

        if (getAccessKey() != null) {
            stringBuilder.append("_").append(getAccessKey());
        }

        return stringBuilder.toString();
    }
}
