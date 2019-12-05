package com.lxs.common.controller;

import java.util.Date;

import com.jfinal.aop.Inject;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;
import com.lxs.common.base.BaseController;
import com.lxs.common.model.Book;
import com.lxs.common.model.Chapter;
import com.lxs.common.model.Comment;
import com.lxs.common.model.Tip;
import com.lxs.common.model.Types;
import com.lxs.common.model.User;
import com.lxs.common.service.AdminService;
import com.lxs.common.utils.Constant;
import com.lxs.common.utils.Utils;

public class BookController extends BaseController {

	@Inject
	AdminService adminService;

	public void index(Integer pages) {

		pages = getDefaultPages(pages);

		setTitle("图书管理");

		setPages(pages);

		setAttr("list", adminService.getBookList(pages));

		render("/admin/book.html");

	}

	@ActionKey("/uploadimg")
	public void uploadimg() { // 上传图片 返回图片地址

		UploadFile file = getFile("file");

		if (file != null) {

			renderText(getRequest().getContextPath() + "/upload/temp/" + file.getFileName());

		}

	}

	public void modifybook(Integer id) {

		if (id == null) {
			setTitle("创建图书");
		} else {
			setTitle("修改图书");
			setAttr("book", adminService.getBooksByIdAnd(id));
		}

		setAttr("atypes", adminService.getAllTypes());

		render("/admin/modifybook.html");

	}

	public void tomodifybook() {

		Book t = getModel(Book.class);

		boolean flag = false;

		t.setBookDates(new Date());

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

		forwardAction("/book");

	}

	public void bookexam(Integer pages) {

		pages = getDefaultPages(pages);

		setPages(pages);

		setTitle("图书审核");

		setAttr("list", adminService.getBookListByExam(pages));

		render("/admin/exambook.html");

	}

	public void bookexamend(Integer id, Integer info, String words) {

		vailId(id);

		vailId(info);

		Book b = adminService.getBooksByIdAnd(id);

		vailT(b);

		boolean flag = false;

		b.setBookView(info);

		flag = b.update();

		if (info == Constant.VIEWCANNOTREADBYEXAM) {

			Tip t = new Tip();

			t.setTipContent(words);
			t.setTipTitle(b.getBookTitle() + ":  审核不通过");
			t.setTipContent(words);
			t.setTipUser(b.getBookUser());
			t.setTipAdmin(((User) getUser()).getUserId());
			t.setTipView(1);
			t.setTipDates(new Date());

			flag = t.save();

		}

		if (flag) {
			setTipMsg("操作成功！");
		} else {
			setTipMsg("操作失败！");
		}

		forwardAction("/book");

	}

	public void deletebook(Integer id) {

		vailId(id);

		if (adminService.delete(new Book().setBookId(id))) {

			setTipMsg("删除成功！");

		} else {
			setTipMsg("删除失败！");
		}

		forwardAction("/book");

	}

	public void chapter(int pages) {

		pages = getDefaultPages(pages);

		setTitle("章节管理");

		setPages(pages);

		setAttr("list", adminService.getChapterList(pages));

		render("/admin/chapter.html");

	}

	public void chapterview(Integer id) {

		vailId(id);

		Chapter c = adminService.getChapterById(id);

		vailT(c);

		boolean flag = false;

		if (c.getChapterView() != null && c.getChapterView() == Constant.VIEWCANREAD) {
			c.setChapterView(Constant.VIEWCANNOTREAD);
			flag = adminService.update(c);
		} else {
			c.setChapterView(Constant.VIEWCANREAD);
			flag = adminService.update(c);
		}

		if (flag) {
			setTipMsg("操作成功！");
		} else {
			setTipMsg("操作失败！");
		}

		forwardAction("/book/chapter");

	}

	public void bookview(Integer id) {

		vailId(id);

		Book b = adminService.getBookById(id);

		vailT(b);

		boolean flag = false;

		if (b.getBookView() != null && b.getBookView() == Constant.VIEWCANREAD) {
			b.setBookView(Constant.VIEWCANNOTREAD);

		} else {
			b.setBookView(Constant.VIEWCANREAD);
		}

		flag = adminService.update(b);

		if (flag) {
			setTipMsg("操作成功！");
		} else {
			setTipMsg("操作失败！");
		}

		forwardAction("/book");

	}

	public void chapterexamend(Integer id, Integer info, String words) {

		vailId(id);

		vailId(info);

		Chapter c = adminService.getChapterByIdAnd(id);

		vailT(c);

		boolean flag = false;

		c.setChapterView(info);

		flag = c.update();

		if (info == Constant.VIEWCANNOTREADBYEXAM) {

			Tip t = new Tip();

			t.setTipContent(words);
			t.setTipTitle(c.getChapterName() + ":  审核不通过");
			t.setTipContent(words);
			t.setTipUser(c.getChapterAuthor());
			t.setTipAdmin(((User) getUser()).getUserId());
			t.setTipView(1);
			t.setTipDates(new Date());

			flag = t.save();

		}

		if (flag) {
			setTipMsg("操作成功！");
		} else {
			setTipMsg("操作失败！");
		}

		forwardAction("/book/chapter");

	}

	public void chapterexam(Integer pages) {

		pages = getDefaultPages(pages);

		setPages(pages);

		setTitle("章节审核");

		setAttr("list", adminService.getChapterListByExam(pages));

		render("/admin/examchapter.html");

	}

	public void chapterdelete(Integer id) {

		vailId(id);

		if (adminService.delete(new Chapter().setChapterId(id))) {

			setTipMsg("删除成功！");

		} else {
			setTipMsg("删除失败！");
		}

		forwardAction("/book/chapter");

	}

	public void types(int pages) {

		pages = getDefaultPages(pages);

		setTitle("类型管理");

		setPages(pages);

		setAttr("list", adminService.getTypesList(pages));

		render("/admin/types.html");

	}

	public void gettypes(Integer id) {

		if (id == null) {
			setTitle("新增类型");
			setAttr("what", "新增类型");

		} else {
			setTitle("修改类型");
			setAttr("what", "修改类型");
			setAttr("types", adminService.getTypesById(id));
		}

		render("/admin/modifytypes.html");

	}

	public void tomodifytypes() {

		Types t = getModel(Types.class);

		boolean flag = false;

		t.setTypesDates(new Date());
		t.setTypesLevel(0);

		if (t.getTypesId() != null) {
			flag = adminService.update(t);
		} else {
			flag = adminService.save(t);
		}

		if (flag) {
			setTipMsg("操作成功！");
		} else {
			setTipMsg("操作失败！");
		}

		forwardAction("/book/types");

	}

	public void deletetypes(Integer id) {

		vailId(id);

		if (adminService.delete(new Types().setTypesId(id))) {

			setTipMsg("删除成功！");

		} else {
			setTipMsg("删除失败！");
		}

		forwardAction("/book/types");

	}

	public void comment(Integer pages) {

		pages = getDefaultPages(pages);

		setTitle("评论管理");

		setPages(pages);

		setAttr("list", adminService.geCommmentList(pages));

		render("/admin/comment.html");

	}

	public void deletecomment(Integer id) {
		vailId(id);

		Comment c = new Comment().setCommentId(id);

		if (adminService.delete(c)) {
			setTipMsg("操作成功！");
		} else {
			setTipMsg("操作失败！");
		}

		forwardAction("/book/comment/");

	}

}
