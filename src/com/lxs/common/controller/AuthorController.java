package com.lxs.common.controller;

import java.util.Date;

import com.jfinal.aop.Inject;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.lxs.common.base.BaseController;
import com.lxs.common.model.Book;
import com.lxs.common.model.Chapter;
import com.lxs.common.model.User;
import com.lxs.common.service.AdminService;
import com.lxs.common.utils.Utils;

public class AuthorController extends BaseController {

	@Inject
	AdminService adminService;

	public void index(Integer pages) {

		setTitle("雪霜文学管理系统");

		setAttr("count", adminService.getAllCountByAuthor(getUser()));

		setAttr("list", adminService.getAllTip(getUser()));

		render("index.html");

	}

	public void book(Integer pages) {

		pages = getDefaultPages(pages);

		setTitle("图书管理");

		setPages(pages);

		setAttr("list", adminService.getBookListByUser(pages, getUser()));

		render("book.html");

	}

	public void addbook(Integer id) {

		if (id != null) {
			setTitle("修改图书");
			setAttr("what", "修改图书");
			setAttr("book", adminService.getBooksByIdAnd(id));
		} else {
			setTitle("新增图书");
			setAttr("what", "新增图书");
		}

		setAttr("atypes", adminService.getAllTypes());

		render("addbook.html");

	}

	public void toaddbook() {

		Book t = getModel(Book.class);

		boolean flag = false;

		t.setBookDates(new Date());
		t.setBookView(0);
		t.setBookUser(getUser().getUserId());

		String img = getPara("cronimg");

		if (StrKit.notBlank(img)) {

			String name = Utils.INSTANCE.getFilePath(t.getBookId());

			String path = PathKit.getWebRootPath() + "/upload/temp/" + name;

			if (Utils.INSTANCE.base64ToImage(img, path)) {
				t.setBookImg(name);
			}

		}

		if (t.getBookId() != null) {
			flag = adminService.update(t);
		} else {
			flag = adminService.save(t);
		}

		if (flag) {
			setTipMsg("操作成功！");
		} else {
			setTipMsg("操作失败！");
		}

		forwardAction("/author/book");

	}

	public void chapter(int pages) {

		pages = getDefaultPages(pages);

		setTitle("章节管理");

		setPages(pages);

		setAttr("list", adminService.getChapterList(pages, getUser()));

		render("chapter.html");

	}

	public void addchapter(Integer id) {

		if (id != null) {
			setTitle("修改章节");
			setAttr("what", "修改章节");
			setAttr("chapter", adminService.getChapterById(id));
		} else {
			setTitle("新增章节");
			setAttr("what", "新增章节");
		}

		setAttr("book", adminService.getBookAllListByUser(getUser()));

		setAttr("atypes", adminService.getAllTypes());

		render("addchapter.html");

	}

	public void toaddchapter() {

		Chapter c = getModel(Chapter.class);

		c.setChapterDates(new Date());
		c.setChapterView(0);
		c.setChapterAuthor(getUser().getUserId());

		boolean flag = false;

		if (c.getChapterId() != null) {
			flag = adminService.update(c);

		} else {
			flag = adminService.save(c);
		}

		if (c.getChapterBook() != null) {
			Book book = adminService.getBookById(c.getChapterBook());

			if (book != null) {
				book.setBookChapter(book.getBookChapter() == null ? 1 : book.getBookChapter() + 1);
				adminService.update(book);
			}
		}

		if (flag) {
			setTipMsg("操作成功！");
		} else {
			setTipMsg("操作失败！");
		}

		forwardAction("/author/chapter");

	}

	public void user() {

		User u = getUser();

		setTitle("个人中心");

		setAttr("user", u);

		render("user.html");

	}

	public void tomodify() {

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

		User user = adminService.updateUserModify(u);

		if (user != null) {
			setUser(user);
			setTipMsg("修改成功！");
		} else {
			setTipMsg("修改失败！");
		}

		forwardAction("/author/user");

	}

}
