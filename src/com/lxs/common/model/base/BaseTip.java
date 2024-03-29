package com.lxs.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseTip<M extends BaseTip<M>> extends Model<M> implements IBean {

	public M setTipId(java.lang.Integer tipId) {
		set("tip_id", tipId);
		return (M)this;
	}
	
	public java.lang.Integer getTipId() {
		return getInt("tip_id");
	}

	public M setTipTitle(java.lang.String tipTitle) {
		set("tip_title", tipTitle);
		return (M)this;
	}
	
	public java.lang.String getTipTitle() {
		return getStr("tip_title");
	}

	public M setTipContent(java.lang.String tipContent) {
		set("tip_content", tipContent);
		return (M)this;
	}
	
	public java.lang.String getTipContent() {
		return getStr("tip_content");
	}

	public M setTipUser(java.lang.Integer tipUser) {
		set("tip_user", tipUser);
		return (M)this;
	}
	
	public java.lang.Integer getTipUser() {
		return getInt("tip_user");
	}

	public M setTipView(java.lang.Integer tipView) {
		set("tip_view", tipView);
		return (M)this;
	}
	
	public java.lang.Integer getTipView() {
		return getInt("tip_view");
	}

	public M setTipAuto(java.lang.String tipAuto) {
		set("tip_auto", tipAuto);
		return (M)this;
	}
	
	public java.lang.String getTipAuto() {
		return getStr("tip_auto");
	}

	public M setTipDates(java.util.Date tipDates) {
		set("tip_dates", tipDates);
		return (M)this;
	}
	
	public java.util.Date getTipDates() {
		return get("tip_dates");
	}

	public M setTipAdmin(java.lang.Integer tipAdmin) {
		set("tip_admin", tipAdmin);
		return (M)this;
	}
	
	public java.lang.Integer getTipAdmin() {
		return getInt("tip_admin");
	}

}
