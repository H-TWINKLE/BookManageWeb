package com.lxs.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.lxs.common.model.User;

public class GobalInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation arg0) {

		Controller c = arg0.getController();

		User u = c.getSessionAttr("user");

		if (u == null) {
			c.forwardAction("/index");
		} else {
			arg0.invoke();
		}

	}

}
