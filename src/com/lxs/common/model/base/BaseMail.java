package com.lxs.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseMail<M extends BaseMail<M>> extends Model<M> implements IBean {

	public M setMailId(java.lang.Integer mailId) {
		set("mail_id", mailId);
		return (M)this;
	}
	
	public java.lang.Integer getMailId() {
		return getInt("mail_id");
	}

	public M setMailAccount(java.lang.String mailAccount) {
		set("mail_account", mailAccount);
		return (M)this;
	}
	
	public java.lang.String getMailAccount() {
		return getStr("mail_account");
	}

	public M setMailView(java.lang.Integer mailView) {
		set("mail_view", mailView);
		return (M)this;
	}
	
	public java.lang.Integer getMailView() {
		return getInt("mail_view");
	}

	public M setMailPass(java.lang.String mailPass) {
		set("mail_pass", mailPass);
		return (M)this;
	}
	
	public java.lang.String getMailPass() {
		return getStr("mail_pass");
	}

	public M setMailCode(java.lang.String mailCode) {
		set("mail_code", mailCode);
		return (M)this;
	}
	
	public java.lang.String getMailCode() {
		return getStr("mail_code");
	}

	public M setMailNickname(java.lang.String mailNickname) {
		set("mail_nickname", mailNickname);
		return (M)this;
	}
	
	public java.lang.String getMailNickname() {
		return getStr("mail_nickname");
	}

	public M setMailDates(java.util.Date mailDates) {
		set("mail_dates", mailDates);
		return (M)this;
	}
	
	public java.util.Date getMailDates() {
		return get("mail_dates");
	}

}
