package api.longpoll.bots.model.events.messages;

import api.longpoll.bots.model.events.Update;
import com.google.gson.annotations.SerializedName;

/**
 * Describes <b>message_typing_state</b> event object.
 */
public class MessageTypingState implements Update.Object {
    /**
     * Typing status.
     */
    @SerializedName("state")
    private String state;

    /**
     * User ID who is typing the text.
     */
    @SerializedName("from_id")
    private Long fromId;

    /**
     * Community ID to which the user writes a message.
     */
    @SerializedName("to_id")
    private Long toId;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    @Override
    public String toString() {
        return "MessageTypingStateEvent{" +
                "state='" + state + '\'' +
                ", fromId=" + fromId +
                ", toId=" + toId +
                '}';
    }
}
