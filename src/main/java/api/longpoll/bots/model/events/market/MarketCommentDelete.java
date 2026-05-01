package api.longpoll.bots.model.events.market;

import api.longpoll.bots.model.events.Update;
import com.google.gson.annotations.SerializedName;

/**
 * Describes <b>market_comment_delete</b> event objects.
 */
public class MarketCommentDelete implements Update.Object {
    /**
     * Market owner ID.
     */
    @SerializedName("owner_id")
    private Long ownerId;

    /**
     * Comment ID.
     */
    @SerializedName("id")
    private Integer id;

    /**
     * Comment author ID.
     */
    @SerializedName("user_id")
    private Long userId;

    /**
     * ID of the user who deleted a comment.
     */
    @SerializedName("deleter_id")
    private Long deleterId;

    /**
     * Market item ID.
     */
    @SerializedName("item_id")
    private Integer itemId;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeleterId() {
        return deleterId;
    }

    public void setDeleterId(Long deleterId) {
        this.deleterId = deleterId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "MarketCommentDeleteEvent{" +
                "ownerId=" + ownerId +
                ", id=" + id +
                ", userId=" + userId +
                ", deleterId=" + deleterId +
                ", itemId=" + itemId +
                '}';
    }
}
