package com.lxs.common.base;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.lxs.common.model.User;
import com.lxs.common.utils.Constant;

public abstract class BaseController extends Controller {

	protected void setPages(Integer pages) {
		setAttr("pages", pages);
	}

	protected int getDefaultPages(Integer pages) {

		if (pages == null || pages == 0) {
			return 1;
		}

		return pages;
	}

	protected void setTitle(String title) {

		if (StrKit.notBlank(title)) {

			setAttr("title", title);
		}

	}

	protected void to404() {
		renderError(404);
		return;
	}

	protected <T> void vailT(T t) {
		if (t == null)
			renderError(404);
		return;
	}

	protected void setTipMsg(String value) {
		if (StrKit.notBlank(value)) {

			setAttr("TipMsg", value);
		}
	}

	protected void setTypes(String types) {
		if (StrKit.notBlank(types)) {

			setAttr("types", types);
		}
	}

	protected void vailId(Integer id) {

		if (id == null) {
			renderError(404);
			return;
		}

		if (id == 0)
			renderError(404);
		return;

	}

	protected void vailTypes(String types) {

		if (StrKit.isBlank(types))
			renderError(404);
		return;

	}

	protected void vailIdAndTypes(Integer id, String types) {

		vailId(id);

		vailTypes(types);

	}

	protected void setUser(User u) {

		if (u != null) {
			setSessionAttr("user", u);
		}

	}

	protected User getUser() {

		return getSessionAttr("user");

	}

	protected void addCookiesInUser(User u) {

		if (u == null)
			return;

		setCookie(Constant.COMLXSA, com.lxs.common.utils.Utils.INSTANCE.encryptionStringByBase64(u.getUserAccount()),
				Constant.MAXCOOKIESTIMES);

		setCookie(Constant.COMLXSP, com.lxs.common.utils.Utils.INSTANCE.encryptionStringByBase64(u.getUserPass()),
				Constant.MAXCOOKIESTIMES);

	}

	protected boolean getCookiesInUserHaveValue(String value) {

		if (StrKit.isBlank(value))
			return false;

		String val = getCookie(value);

		return StrKit.notBlank(val);

	}

	protected String getCookiesValue(String vaString) {

		if (StrKit.isBlank(vaString))
			return "";

		return com.lxs.common.utils.Utils.INSTANCE.decryptStringByBase64(vaString);

	}

}
