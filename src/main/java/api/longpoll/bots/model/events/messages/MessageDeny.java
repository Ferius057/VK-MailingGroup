package api.longpoll.bots.model.events.messages;

import api.longpoll.bots.model.events.Update;
import com.google.gson.annotations.SerializedName;

/**
 * Describes <b>message_deny</b> event object.
 */
public class MessageDeny implements Update.Object {
    /**
     * User ID.
     */
    @SerializedName("user_id")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MessageDenyEvent{" +
                "userId=" + userId +
                '}';
    }
}
