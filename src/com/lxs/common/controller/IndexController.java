package com.lxs.common.controller;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.lxs.common.base.BaseController;
import com.lxs.common.interceptor.GobalInterceptor;
import com.lxs.common.model.Book;
import com.lxs.common.model.Chapter;
import com.lxs.common.model.Comment;
import com.lxs.common.model.Types;
import com.lxs.common.model.User;
import com.lxs.common.service.AdminService;
import com.lxs.common.service.FrontService;
import com.lxs.common.utils.Constant;
import com.lxs.common.utils.Utils;

public class IndexController extends BaseController {

	@Inject
	FrontService fService;

	@Inject
	AdminService aSetvice;

	@Clear(GobalInterceptor.class)
	public void index() {

		setTitle(Constant.TITLE);

		List<Types> list = getSessionAttr("atypes");

		if (list == null) {
			list = aSetvice.getAllTypes();
		}

		List<Book> hot = getSessionAttr("hot");

		if (hot == null) {
			hot = fService.getBookLimitInt(4, 101);
		}

		setSessionAttr("atypes", list);

		setSessionAttr("hot", hot);

		setAttr("book", fService.getBookLimitInt(8, 100));

		toLoginByCookies();

		render("index.html");

	}

	@Clear(GobalInterceptor.class)
	public void gettypes(Integer id) {

		vailId(id);

	}

	@Clear(GobalInterceptor.class)
	public void detail(Integer id) {

		vailId(id);

		Chapter c = aSetvice.getChapterByIdAnd(id);

		vailT(c);

		setAttr("c", c);

		setAttr("comm", aSetvice.getAllCommentByChapterId(c.getChapterId(), 1));

		setTitle(c.getChapterName());

		render("detail.html");

	}

	@Clear(GobalInterceptor.class)
	public void books(Integer id) {

		vailId(id);

		Book b = aSetvice.getBooksByIdAnd(id);

		List<Chapter> ch = aSetvice.getAllChapterByBookId(id);

		vailT(b);

		setAttr("b", b);

		setTitle(b.getBookTitle());

		setAttr("chapter", ch);

		setAttr("begin", aSetvice.getOneChapterByBookId(id));

		render("books.html");

	}

	@Clear(GobalInterceptor.class)
	public void types(Integer id, String types) {

		vailId(id);

		vailT(types);

		List<Book> list = fService.getBookByTypes(id);

		setAttr("book", list);

		setAttr("types", types);

		setTitle("查询类型：" + types);

		render("types.html");

	}
	
	@Clear(GobalInterceptor.class)
	@ActionKey("/upload/temp")
	public void base404() {
		
		redirect("/assets/img/empty.png");
		
		return;
		
	}

	@Clear(GobalInterceptor.class)
	public void addcomment() {

		User u = getUser();

		if (u == null) {

			forwardAction("/detail");

			setTipMsg("您不能够评论，请登录！");
			return;
		}

		Comment c = getModel(Comment.class);

		c.setCommentUser(u.getUserId());
		c.setCommentView(1);
		c.setCommentDates(new Date());

		if (aSetvice.save(c)) {
			setTipMsg("评论成功！");
		} else {
			setTipMsg("评论失败！");
		}

		forwardAction("/detail");

	}

	@Clear(GobalInterceptor.class)
	public void search(String what) {

		List<Book> list = fService.getBookBySearch(what);

		setAttr("book", list);

		setAttr("what", what);

		setTitle("查询关键字：" + what);

		render("search.html");

	}

	public void user() {

		setTitle("个人中心");

		render("user.html");

	}

	public void tip() {

		setTitle("我的消息");

		setAttr("tip", aSetvice.getAllTip(getUser()));

		render("tip.html");

	}

	public void mycomment() {

		setTitle("我的评论");

		setAttr("comm", aSetvice.getAllCommentByUserId(((User) getUser()).getUserId(), 1));

		render("comment.html");

	}

	public void usermodi() {

		Integer id = getParaToInt("user.user_id");

		vailId(id);

		User u = getModel(User.class);

		vailT(u);

		String img = getPara("cronimg");

		if (StrKit.notBlank(img)) {

			String name = Utils.INSTANCE.getFilePath(u.getUserId());

			String path = PathKit.getWebRootPath() + "/upload/temp/" + name;

			if (Utils.INSTANCE.base64ToImage(img, path)) {
				u.setUserImg(name);
			}

		}

		if (u.getUserAuth() == Constant.ASAUTHOR) {
			u.setUserAuth(Constant.APPLYASAUTHOR);
			u.setUserApply(1);
		} else if (u.getUserAuth() == Constant.ASADMIN) {
			u.setUserAuth(Constant.APPLYASADMIN);
			u.setUserApply(1);
		} else {
			u.setUserAuth(Constant.ASREADER);
		}

		User user = aSetvice.updateUserModify(u);

		if (user != null) {
			setUser(user);
			setTipMsg("修改成功！");
		} else {
			setTipMsg("修改失败！");
		}

		forwardAction("/user");

	}

	@Clear(GobalInterceptor.class)
	public void login(String account, String pass) {

		vailT(account);

		vailT(pass);

		User u = fService.toLogin(account, pass);

		if (u == null) {
			setTipMsg("用户名或者密码错误");
			forwardAction("/");
			return;
		}

		setUser(u);

		if (getPara("re") != null && getPara("re").equals("on")) {

			addCookiesInUser(u);

		}

		redirect("/");
		return;

	}

	@Clear(GobalInterceptor.class)
	public void logout() {

		removeCookie(Constant.COMLXSA);
		removeCookie(Constant.COMLXSP);
		removeSessionAttr("user");

		redirect("/");

	}

	private void toLoginByCookies() {
		if (getUser() == null) {
			String admin = getCookiesValue(getCookie(Constant.COMLXSA));

			String pass = getCookiesValue(getCookie(Constant.COMLXSP));

			if (StrKit.notBlank(admin) && StrKit.notBlank(pass)) {

				User u = fService.toLogin(admin, pass);

				if (u != null) {
					setUser(u);
				}

			}
		}

	}

}
