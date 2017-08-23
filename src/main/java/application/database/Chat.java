package application.database;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the chat database table.
 *
 */
@Entity
@Table(name="chat")
@NamedQuery(name="Chat.findAll", query="SELECT c FROM Chat c")
public class Chat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="chat_id")
	private int chatId;

	@Column(name="contact_with")
	private int contactWith;

	//bi-directional many-to-one association to Apartment

	@ManyToOne
	private Apartment apartment;

	//bi-directional many-to-one association to Login
	@ManyToOne
	private Login login;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="chat")
	private List<Message> messages;

	public Chat() {
	}
	public int getChatId() {
		return this.chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public Apartment getApartment() {
		return this.apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setChat(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setChat(null);

		return message;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public int getContactWith() {
		return contactWith;
	}

	public void setContactWith(int contactWith) {
		this.contactWith = contactWith;
	}

}
