package api.longpoll.bots.model.objects.additional;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Stores messages to forward.
 */
public class Forward {
    /**
     * Messages owner.
     * Should be set when forwarding messages from community to dialog.
     */
    @SerializedName("owner_id")
    private Long ownerId;

    /**
     * Messages origin.
     */
    @SerializedName("peer_id")
    private long peerId;

    /**
     * Conversation message IDs.
     */
    @SerializedName("conversation_message_ids")
    private List<Integer> conversationMessageIds;

    /**
     * Message IDs.
     */
    @SerializedName("message_ids")
    private List<Integer> messageIds;

    /**
     * To forward message to the same char.
     */
    @SerializedName("is_reply")
    private Boolean isReply;

    public Long getOwnerId() {
        return ownerId;
    }

    public Forward setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public long getPeerId() {
        return peerId;
    }

    public Forward setPeerId(long peerId) {
        this.peerId = peerId;
        return this;
    }

    public List<Integer> getConversationMessageIds() {
        return conversationMessageIds;
    }

    public Forward setConversationMessageIds(Integer... conversationMessageIds) {
        return setConversationMessageIds(Arrays.asList(conversationMessageIds));
    }

    public Forward setConversationMessageIds(List<Integer> conversationMessageIds) {
        this.conversationMessageIds = conversationMessageIds;
        return this;
    }

    public List<Integer> getMessageIds() {
        return messageIds;
    }

    public Forward setMessageIds(Integer... messageIds) {
        return setMessageIds(Arrays.asList(messageIds));
    }

    public Forward setMessageIds(List<Integer> messageIds) {
        this.messageIds = messageIds;
        return this;
    }

    public Boolean getReply() {
        return isReply;
    }

    public Forward setReply(Boolean reply) {
        isReply = reply;
        return this;
    }


    @Override
    public String toString() {
        return "Forward{" +
                "ownerId=" + ownerId +
                ", peerId=" + peerId +
                ", conversationMessageIds=" + conversationMessageIds +
                ", messageIds=" + messageIds +
                ", isReply=" + isReply +
                '}';
    }
}
