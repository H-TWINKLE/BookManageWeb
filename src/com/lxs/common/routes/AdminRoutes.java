package com.lxs.common.routes;

import com.jfinal.config.Routes;
import com.lxs.common.controller.AuthorController;
import com.lxs.common.controller.BookController;
import com.lxs.common.controller.UserController;

public class AdminRoutes extends Routes {

	@Override
	public void config() {

		add("/admin", UserController.class);

		add("/book", BookController.class);
		
		add("/author", AuthorController.class);

	}

}
