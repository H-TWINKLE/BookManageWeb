package com.lxs.common.routes;

import com.jfinal.config.Routes;
import com.lxs.common.controller.IndexController;

public class FrontsRoutes extends Routes {

	@Override
	public void config() {

		setBaseViewPath("fronts");
		add("/", IndexController.class,"/");
		
	}

}
