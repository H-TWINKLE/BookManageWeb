package com.lxs.common.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.lxs.common.interceptor.GobalInterceptor;
import com.lxs.common.model._MappingKit;

import com.lxs.common.routes.AdminRoutes;
import com.lxs.common.routes.FrontsRoutes;

public class MainConfig extends JFinalConfig {
	/**
	 * 配置JFinal常量
	 */
	@Override
	public void configConstant(Constants me) {
		PropKit.use("config.properties");
		me.setDevMode(PropKit.getBoolean("devMode"));
		me.setBaseUploadPath("upload/temp/");
		me.setBaseDownloadPath("upload/temp/");
		me.setViewType(ViewType.JFINAL_TEMPLATE);
		me.setError404View("/fronts/404.html");
		me.setError500View("/fronts/500.html");
		me.setJsonFactory(MixedJsonFactory.me());
		me.setInjectDependency(true);

	}

	/**
	 * 配置JFinal路由映射
	 */
	@Override
	public void configRoute(Routes me) {
		me.add(new FrontsRoutes());
		me.add(new AdminRoutes());

	}

	/**
	 * 配置JFinal插件 数据库连接池 ORM 缓存等插件 自定义插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		DruidPlugin dbPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dbPlugin);
		arp.setShowSql(PropKit.getBoolean("devMode"));
		arp.setDialect(new MysqlDialect());
		dbPlugin.setDriverClass("com.mysql.jdbc.Driver");
		/******** 在此添加数据库 表-Model 映射 *********/
		_MappingKit.mapping(arp);
		me.add(dbPlugin);
		me.add(arp);
	}

	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		me.addGlobalActionInterceptor(new SessionInViewInterceptor());
		me.add(new GobalInterceptor());
	}

	/**
	 * 配置全局处理器
	 */
	@Override
	public void configHandler(Handlers me) {
     me.add(new ContextPathHandler("base"));
	}

	/**
	 * 配置模板引擎
	 */
	@Override
	public void configEngine(Engine me) {
		me.setDevMode(PropKit.getBoolean("devMode"));
		
		me.addSharedFunction("/admin/comm/_layout.html");
		me.addSharedFunction("/fronts/comm/_layout.html");
		me.addSharedFunction("/author/comm/_layout.html");
	}

	public static void main(String[] args) {
		JFinal.start("WebRoot", 1314, "/", 5);
	}

}