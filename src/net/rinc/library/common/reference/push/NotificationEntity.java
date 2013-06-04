package net.rinc.library.common.reference.push;

import java.io.Serializable;

public class NotificationEntity implements Serializable{
	private static final long serialVersionUID = 5874890670519802880L;
	private int id;
	private String title;
	private String content;
	private String extra;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
}