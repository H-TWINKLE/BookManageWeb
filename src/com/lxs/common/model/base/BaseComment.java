package com.lxs.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseComment<M extends BaseComment<M>> extends Model<M> implements IBean {

	public M setCommentId(java.lang.Integer commentId) {
		set("comment_id", commentId);
		return (M)this;
	}
	
	public java.lang.Integer getCommentId() {
		return getInt("comment_id");
	}

	public M setCommentBook(java.lang.Integer commentBook) {
		set("comment_book", commentBook);
		return (M)this;
	}
	
	public java.lang.Integer getCommentBook() {
		return getInt("comment_book");
	}

	public M setCommentContent(java.lang.String commentContent) {
		set("comment_content", commentContent);
		return (M)this;
	}
	
	public java.lang.String getCommentContent() {
		return getStr("comment_content");
	}

	public M setCommentUser(java.lang.Integer commentUser) {
		set("comment_user", commentUser);
		return (M)this;
	}
	
	public java.lang.Integer getCommentUser() {
		return getInt("comment_user");
	}

	public M setCommentView(java.lang.Integer commentView) {
		set("comment_view", commentView);
		return (M)this;
	}
	
	public java.lang.Integer getCommentView() {
		return getInt("comment_view");
	}

	public M setCommentDates(java.util.Date commentDates) {
		set("comment_dates", commentDates);
		return (M)this;
	}
	
	public java.util.Date getCommentDates() {
		return get("comment_dates");
	}

	public M setCommentChapter(java.lang.Integer commentChapter) {
		set("comment_chapter", commentChapter);
		return (M)this;
	}
	
	public java.lang.Integer getCommentChapter() {
		return getInt("comment_chapter");
	}

}