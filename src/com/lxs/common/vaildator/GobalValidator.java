package com.lxs.common.vaildator;

import com.jfinal.core.Controller;
import com.lxs.common.utils.Constant;

public class GobalValidator extends com.jfinal.validate.Validator {

	@Override
	protected void handleError(Controller arg0) {
		switch (getActionKey()) {
		case "/register":
			arg0.forwardAction("/register");
			break;
		case "/tomodifypass":
			arg0.forwardAction("/modifypass");
			break;

		default:
			break;
		}

	}

	@Override
	protected void validate(Controller arg0) {

		setShortCircuit(true);

		switch (getActionKey()) {
		case "/register":
			validateEmail("mail.mail_account", Constant.TipMsg, "请正确输入邮箱形式");
			validateRequired("mail.mail_pass", Constant.TipMsg, "请输入密码");
			validateRequiredString("mail.mail_nickname", Constant.TipMsg, "请输入昵称");
			validateString("mail.mail_pass", 6, 20, Constant.TipMsg, "密码最低6位数");
			break;
		case "/tomodifypass":
			validateEmail("email", Constant.TipMsg, "请求错误！");
			validateString("pass1", 6, 20, Constant.TipMsg, "密码最低6位数！");
			validateString("pass2", 6, 20, Constant.TipMsg, "密码最低6位数！");
			validateEqualField("pass1", "pass2", Constant.TipMsg, "两次密码不相同，请重新输入！");
			break;

		default:
			break;
		}

	}

}
