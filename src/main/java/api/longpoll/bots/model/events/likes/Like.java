package api.longpoll.bots.model.events.likes;

import api.longpoll.bots.model.events.Update;
import com.google.gson.annotations.SerializedName;

/**
 * Describes <b>like_add</b> event objects.
 */
public class Like implements Update.Object {
    /**
     * User ID.
     */
    @SerializedName("liker_id")
    private Long likerId;

    /**
     * What was liked. Possible values:
     * <ul>
     *     <li>video</li>
     *     <li>photo</li>
     *     <li>comment</li>
     *     <li>note</li>
     *     <li>topic_comment</li>
     *     <li>photo_comment</li>
     *     <li>video_comment</li>
     *     <li>market</li>
     *     <li>market_comment</li>
     * </ul>
     */
    @SerializedName("object_type")
    private String objectType;

    /**
     * Liked object owner ID.
     */
    @SerializedName("object_owner_id")
    private Long objectOwnerId;

    /**
     * Liked object ID.
     */
    @SerializedName("object_id")
    private Integer objectId;

    /**
     * Parent object ID.
     */
    @SerializedName("thread_reply_id")
    private Integer threadReplyId;

    /**
     * Post ID.
     */
    @SerializedName("post_id")
    private Integer postId;

    public Long getLikerId() {
        return likerId;
    }

    public void setLikerId(Long likerId) {
        this.likerId = likerId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Long getObjectOwnerId() {
        return objectOwnerId;
    }

    public void setObjectOwnerId(Long objectOwnerId) {
        this.objectOwnerId = objectOwnerId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getThreadReplyId() {
        return threadReplyId;
    }

    public void setThreadReplyId(Integer threadReplyId) {
        this.threadReplyId = threadReplyId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "LikeEvent{" +
                "likerId=" + likerId +
                ", objectType='" + objectType + '\'' +
                ", objectOwnerId=" + objectOwnerId +
                ", objectId=" + objectId +
                ", threadReplyId=" + threadReplyId +
                ", postId=" + postId +
                '}';
    }
}
