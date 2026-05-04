package api.longpoll.bots.model.events.video;

import api.longpoll.bots.model.events.Update;
import com.google.gson.annotations.SerializedName;

/**
 * Describes <b>video_comment_delete</b> event objects.
 */
public class VideoCommentDelete implements Update.Object {
    /**
     * Comment ID.
     */
    @SerializedName("id")
    private Integer id;

    /**
     * Video owner ID.
     */
    @SerializedName("owner_id")
    private Long ownerId;

    /**
     * User ID who deleted the comment.
     */
    @SerializedName("deleter_id")
    private Long deleterId;

    /**
     * Video ID.
     */
    @SerializedName("video_id")
    private Integer videoId;

    /**
     * ID of the user who deleted a comment.
     */
    @SerializedName("user_id")
    private Long userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getDeleterId() {
        return deleterId;
    }

    public void setDeleterId(Long deleterId) {
        this.deleterId = deleterId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "VideoCommentDeleteEvent{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", deleterId=" + deleterId +
                ", videoId=" + videoId +
                ", userId=" + userId +
                '}';
    }
}
